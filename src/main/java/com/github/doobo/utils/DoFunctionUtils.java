package com.github.doobo.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 常用函数工具
 */
public abstract class DoFunctionUtils {

    /**
     * 去重List元素
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * List元素拆解
     */
    public static <T> List<T> flatMap(List<List<T>> rList){
        if(Objects.isNull(rList) || rList.isEmpty()){
            return Collections.emptyList();
        }
        return rList.stream().filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
 }
