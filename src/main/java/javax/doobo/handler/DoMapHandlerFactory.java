package javax.doobo.handler;

import lombok.extern.slf4j.Slf4j;

import javax.doobo.map.DoMultiValueMap;
import javax.doobo.map.LinkedMultiValueMap;
import javax.doobo.model.DoHookTuple;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 包装类
 */
@Slf4j
public abstract class DoMapHandlerFactory<R, H extends DoMapHandler<R>> extends DoBasisFactory<R> {

    /**
     * 导入处理器列表
     */
    protected DoMultiValueMap<String, H> handlerMap;

    /**
     * 添加字段处理器
     */
    public synchronized void addHandler(H handler){
        if(Objects.isNull(handlerMap)){
            handlerMap = new LinkedMultiValueMap<>();
        }
        if(Objects.nonNull(handler) &&
                Objects.equals(handler._rsType(), _rsType())){
            synchronized (this) {
                Set<String> mapKeys = handler.getMapKeys();
                if(Objects.nonNull(mapKeys) && !mapKeys.isEmpty()){
                    mapKeys.forEach(key -> {
                            handlerMap.add(key, handler);
                            List<H> tmpList = handlerMap.get(key);
                            if (Objects.nonNull(tmpList) && tmpList.size() > 1) {
                                tmpList.sort(Comparator.comparing(H::getPhase));
                            }
                        });
                    return;
                }
                handlerMap.add(handler.getMapKey(), handler);
                List<H> tmpList = handlerMap.get(handler.getMapKey());
                if (Objects.nonNull(tmpList) && tmpList.size() > 1) {
                    tmpList.sort(Comparator.comparing(H::getPhase));
                }
            }
        }
    }

    /**
     * 添加字段处理器列表
     */
    public void addHandlerList(List<H> handlers){
        if(Objects.isNull(handlerMap)){
            handlerMap = new LinkedMultiValueMap<>();
        }
        if(Objects.isNull(handlers) || handlers.isEmpty()){
            return;
        }
        handlers = handlers.stream().filter(Objects::nonNull)
                .filter(f -> Objects.equals(f._rsType(),_rsType()))
                .collect(Collectors.toList());
        if(handlers.isEmpty()){
            return;
        }
        synchronized (this) {
            handlers.forEach(m -> {
                Set<String> mapKeys = m.getMapKeys();
                if(Objects.nonNull(mapKeys) && !mapKeys.isEmpty()){
                    mapKeys.forEach(key -> handlerMap.add(key, m));
                }else{
                    handlerMap.add(m.getMapKey(), m);
                }
            });
            handlerMap.forEach((key, hList) -> {
                if (Objects.nonNull(hList) && hList.size() > 1) {
                    hList.sort(Comparator.comparing(H::getPhase));
                }
            });
        }
    }

    /**
     * 具体执行器
     */
    public <T> T executeHandler(R request, Function<H, T> fun){
        H handler = getInstanceHandler(request);
        return execute(handler, fun, request, null);
    }

    /**
     * 具体执行器
     */
    public <T> T executeHandler(R request, Function<H, T> fun, DoHookTuple tuple){
        H handler = getInstanceHandler(request);
        return execute(handler, fun, request, tuple);
    }

    /**
     * void执行器
     */
    public void voidHandler(R request, Consumer<H> fun){
        H handler = getInstanceHandler(request);
        execute(handler, fun, request, null);
    }

    /**
     * void执行器
     */
    public void voidHandler(R request, Consumer<H> fun, DoHookTuple tuple){
        H handler = getInstanceHandler(request);
        execute(handler, fun, request, tuple);
    }

    /**
     * 判断,并具体执行方法
     */
    private <T> T execute(H handler, Function<H, T> fun
            , R request, DoHookTuple tuple) {
        if(Objects.isNull(handler)){
            log.error("未匹配到执行器.");
            return null;
        }
        try {
            Optional.ofNullable(tuple)
                    .ifPresent(c -> Optional.ofNullable(c.beforeTuple(handler))
                            .ifPresent(n -> n.accept(request)));
            return fun.apply(handler);
        }catch (Throwable e){
            if(Objects.isNull(tuple)){
                throw e;
            }
            Consumer<Object> orElse = Optional.ofNullable(tuple.errorTuple(e)).orElse(null);
            if(Objects.isNull(orElse)){
                throw e;
            }
            orElse.accept(request);
        }finally {
            Optional.ofNullable(tuple)
                    .ifPresent(c -> Optional.ofNullable(c.endTuple(handler))
                            .ifPresent(n->n.accept(request)));
        }
        log.error("方法执行异常.");
        return null;
    }

    /**
     * 判断,并具体执行方法
     */
    private void execute(H handler, Consumer<H> fun
            , R request, DoHookTuple tuple) {
        if(Objects.isNull(handler)){
            log.error("未匹配到执行器");
            return;
        }
        try {
            Optional.ofNullable(tuple)
                    .ifPresent(c -> Optional.ofNullable(c.beforeTuple(handler))
                            .ifPresent(n -> n.accept(request)));
            fun.accept(handler);
            return;
        }catch (Throwable e){
            if(Objects.isNull(tuple)){
                throw e;
            }
            Consumer<Object> orElse = Optional.ofNullable(tuple.errorTuple(e)).orElse(null);
            if(Objects.isNull(orElse)){
                throw e;
            }
            orElse.accept(request);
        }finally {
            Optional.ofNullable(tuple)
                    .ifPresent(c -> Optional.ofNullable(c.endTuple(handler))
                            .ifPresent(n->n.accept(request)));
        }
        log.error("方法执行异常.");
    }

    /**
     * 获取处理器
     */
    public H getInstanceHandler(R request){
        if(Objects.isNull(handlerMap) || handlerMap.isEmpty()){
            return null;
        }
        if(Objects.isNull(request)){
            return null;
        }
        List<H> list = getHandlerList(request);
        if(Objects.isNull(list) || list.size() < 1){
            return null;
        }
        for (H item : list) {
            if (matching(request, item::matching)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 匹配执行器
     */
    private boolean matching(R request, Predicate<R> predicate){
        return predicate.test(request);
    }

    public void setHandlerList(List<H> handlerList){
        addHandlerList(handlerList);
    }

    public DoMultiValueMap<String, H> getHandlerMap() {
        return handlerMap;
    }

    /**
     * 获取处理器
     */
    public List<H> getHandlerList(R request){
        if(Objects.isNull(handlerMap) || handlerMap.isEmpty()){
            return Collections.emptyList();
        }
        return handlerMap.getOrDefault(getMapKey(request), Collections.emptyList());
    }

    /**
     * 获取匹配的处理器列表
     */
    public List<H> getMatchHandlerList(R request){
        if(Objects.isNull(handlerMap) || handlerMap.isEmpty()){
            return null;
        }
        if(Objects.isNull(request)){
            return null;
        }
        List<H> list = getHandlerList(request);
        if(Objects.isNull(list) || list.size() < 1){
            return null;
        }
        List<H> result = new ArrayList<>();
        for (H item : list) {
            if (matching(request, item::matching)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 获取执行参数的KEY
     */
    public String getMapKey(R request){
        return DoMapHandler.class.getName();
    }
}
