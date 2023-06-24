package javax.doobo.utils;

import java.beans.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 简单Bean操作工具
 */
public abstract class DoBeanUtils {

    private static final Set<Class<?>> unknownEditorTypes = Collections.newSetFromMap(new WeakHashMap<>(16));

    /**
     * 设置bean的某个属性值
     */
    public static void setProperty(Object target, String field, Object... args) throws Exception {
        try {
            if(target instanceof Map){
                //noinspection rawtypes
                Map map = (Map) target;
                //noinspection unchecked
                map.put(field, Objects.nonNull(args) && args.length > 0 ? args[0]: null);
                return;
            }
            // 获取bean的某个属性的描述符
            PropertyDescriptor propDesc = new PropertyDescriptor(field, target.getClass());
            // 获得用于写入属性值的方法
            Method methodSetUserName = propDesc.getWriteMethod();
            // 写入属性值
            methodSetUserName.invoke(target, args);
        }catch(IntrospectionException exception){
            DoReflectionUtils.setPrivateField(target, field, Objects.nonNull(args) && args.length > 0 ? args[0]: null);
        }
    }

    /**
     * 获取bean的某个属性值
     */
    public static Object getProperty(Object target, String field) throws Exception {
        try {
            if(target instanceof Map){
                //noinspection rawtypes
                Map map = (Map) target;
                return map.get(field);
            }
            // 获取Bean的某个属性的描述符
            PropertyDescriptor proDescriptor = new PropertyDescriptor(field, target.getClass());
            // 获得用于读取属性值的方法
            Method methodGetUserName = proDescriptor.getReadMethod();
            // 读取属性值
            return methodGetUserName.invoke(target);
        }catch(IntrospectionException exception){
           return DoReflectionUtils.getPrivateField(target, field);
        }
    }

    /**
     * 通过内省设置bean的某个属性值
     */
    public static void setPropertyByBean(Object target, String filed, Object... args) throws Exception {
        if(target instanceof Map){
            //noinspection rawtypes
            Map map = (Map) target;
            //noinspection unchecked
            map.put(filed, Objects.nonNull(args) && args.length > 0 ? args[0]: null);
            return;
        }
        // 获取bean信息
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
        // 获取bean的所有属性列表
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        // 遍历属性列表，查找指定的属性
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                // 找到则写入属性值
                if (propDesc.getName().equals(filed)) {
                    Method methodSetUserName = propDesc.getWriteMethod();
                    if(Objects.isNull(methodSetUserName)){
                        DoReflectionUtils.setPrivateField(target, filed, Objects.nonNull(args) && args.length > 0 ? args[0]: null);
                        return;
                    }
                    //写入属性值
                    methodSetUserName.invoke(target, args);
                    return;
                }
            }
        }
        DoReflectionUtils.setPrivateField(target, filed, Objects.nonNull(args) && args.length > 0 ? args[0]: null);
    }

    /**
     * 通过内省获取bean的某个属性值
     */
    public static Object getPropertyByBean(Object target, String field) throws Exception {
        if(target instanceof Map){
            //noinspection rawtypes
            Map map = (Map) target;
            return map.get(field);
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                if (propDesc.getName().equals(field)) {
                    Method methodGetUserName = propDesc.getReadMethod();
                    if(Objects.isNull(methodGetUserName)){
                        return DoReflectionUtils.getPrivateField(target, field);
                    }
                    return methodGetUserName.invoke(target);
                }
            }
        }
        return DoReflectionUtils.getPrivateField(target, field);
    }

    /**
     * 初始化基础class,封装对象直接返回Null
     */
    public static Object initBasisClass(Class<?> cls){
        if(!DoReflectionUtils.isPrivateClass(cls)){
            return null;
        }
        if(cls == int.class){
            return 0;
        }else if (cls == boolean.class){
            return false;
        }else if (cls == long.class){
            return 0L;
        }else if (cls == byte.class){
            return (byte)0;
        }else if (cls == double.class){
            return 0D;
        }else if (cls == float.class){
            return 0f;
        }else if (cls == char.class){
            return '\u0000';
        }else if (cls == short.class){
            return (short)0;
        }
        return null;
    }

    /**
     * 实例化Bean
     */
    public static <T> T instantiateClass(Class<T> targetClass) throws Exception {
        Constructor<T> constructor = DoReflectionUtils.getMinConstructor(targetClass);
        if(!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        int count = constructor.getParameterCount();
        if(count < 1){
            return constructor.newInstance();
        }
        Object[] args = new Object[count];
        for(int i = 0; i < count; i++){
            Class<?> parameterType = constructor.getParameterTypes()[i];
            args[i] = initBasisClass(parameterType);
        }
        return constructor.newInstance(args);
    }

    /**
     * 实例化对象
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws Exception {
        if(Objects.isNull(ctor)){
            return null;
        }
        DoReflectionUtils.makeAccessible(ctor);
        return ctor.newInstance(args);
    }

    /**
     * 查找符合规则的属性编辑器
     */
    public static PropertyEditor findEditorByConvention(Class<?> targetType) {
        if (targetType != null && !targetType.isArray() && !unknownEditorTypes.contains(targetType)) {
            ClassLoader cl = targetType.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                    if (cl == null) {
                        return null;
                    }
                } catch (Throwable var6) {
                    return null;
                }
            }
            String targetTypeName = targetType.getName();
            String editorName = targetTypeName + "Editor";
            try {
                Class<?> editorClass = cl.loadClass(editorName);
                if (editorClass != null) {
                    if (!PropertyEditor.class.isAssignableFrom(editorClass)) {
                        unknownEditorTypes.add(targetType);
                        return null;
                    }
                    return (PropertyEditor)instantiateClass(editorClass);
                }
            } catch (Exception var5) {
            }
            unknownEditorTypes.add(targetType);
            return null;
        } else {
            return null;
        }
    }
}
