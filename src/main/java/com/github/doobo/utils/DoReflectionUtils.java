package com.github.doobo.utils;

import com.github.doobo.model.DoAssert;
import java.lang.reflect.*;
import java.util.*;

/**
 * 获取私有成员变量,调用私有方法
 */
public abstract class DoReflectionUtils {

    private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    private static final Map<Class<?>, Method[]> declaredMethodsCache = new WeakHashMap<>(16);

    /**
     * 获取私有成员变量的值
     */
    public static Object getPrivateField(Object instance, String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * 设置私有成员的值
     */
    public static void setPrivateField(Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * 访问私有方法
     */
    public static Object invokePrivateMethod(Object instance, String methodName, Class[] classes, Object... objects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = instance.getClass().getDeclaredMethod(methodName, classes);
        method.setAccessible(true);
        return method.invoke(instance, objects);
    }

    /**
     * 获取参数最少的构造方法
     */
    public static <T> Constructor<T> getMinConstructor(Class<T> tClass){
        if(Objects.isNull(tClass) || tClass.isInterface()){
            throw new IllegalArgumentException("Class type is error!");
        }
        if(Modifier.isAbstract(tClass.getModifiers())){
            throw new IllegalArgumentException("Class type is abstract!");
        }
        Constructor<?>[] constructors = tClass.getDeclaredConstructors();
        if(constructors.length == 0){
            throw new IllegalArgumentException("Class constructor is empty!");
        }
        Constructor<?> tmp = constructors[0];
        int last = tmp.getParameterCount();
        for(Constructor<?> ci : constructors){
            //如果是无参构造方法,直接返回
            if(ci.getParameterCount() < 1){
                tmp = ci;
                break;
            }
            if(last > ci.getParameterCount()){
                tmp = ci;
                last = ci.getParameterCount();
            }
        }
        //noinspection unchecked
        return (Constructor<T>) tmp;
    }

    /**
     * 基本类型判断:boolean,char,double,void等,Long,String不是
     */
    public static boolean isPrivateClass(Class<?> tClass){
        return tClass.isPrimitive();
    }

    /**
     * 基本类型判断:boolean,char,double,void,Long,Character等,String不是
     */
    public static boolean isBasisPrimitive(Class<?> clazz) {
        try {
            if (clazz.isPrimitive()) {
                return true;
            }
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return false;
        }
    }

    /**
     * 获取成员变量的类型
     */
    public static Class<?> getFieldType(Class<?> cls, String filedName) throws NoSuchFieldException {
        Field field = cls.getDeclaredField(filedName);
        return field.getType();
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers())
                || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, EMPTY_OBJECT_ARRAY);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception var4) {
            handleReflectionException(var4);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        } else if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method or field: " + ex.getMessage());
        } else {
            if (ex instanceof InvocationTargetException) {
                handleInvocationTargetException((InvocationTargetException)ex);
            }
            if (ex instanceof RuntimeException) {
                throw (RuntimeException)ex;
            } else {
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException)ex;
        } else if (ex instanceof Error) {
            throw (Error)ex;
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, EMPTY_CLASS_ARRAY);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        DoAssert.notNull(clazz, "Class must not be null");
        DoAssert.notNull(name, "Method name must not be null");
        for(Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType, false);
            Method[] var5 = methods;
            int var6 = methods.length;
            for(int var7 = 0; var7 < var6; ++var7) {
                Method method = var5[var7];
                if (name.equals(method.getName()) && (paramTypes == null || hasSameParams(method, paramTypes))) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Method[] getDeclaredMethods(Class<?> clazz) {
        return getDeclaredMethods(clazz, true);
    }

    private static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
        DoAssert.notNull(clazz, "Class must not be null");
        Method[] result = (Method[])declaredMethodsCache.get(clazz);
        if (result == null) {
            try {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
                if (defaultMethods != null) {
                    result = new Method[declaredMethods.length + defaultMethods.size()];
                    System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                    int index = declaredMethods.length;
                    for(Iterator<?> var6 = defaultMethods.iterator(); var6.hasNext(); ++index) {
                        Method defaultMethod = (Method)var6.next();
                        result[index] = defaultMethod;
                    }
                } else {
                    result = declaredMethods;
                }
                declaredMethodsCache.put(clazz, result.length == 0 ? EMPTY_METHOD_ARRAY : result);
            } catch (Throwable var8) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", var8);
            }
        }
        return result.length != 0 && defensive ? (Method[])result.clone() : result;
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        Class<?>[] var2 = clazz.getInterfaces();
        for (Class<?> ifc : var2) {
            Method[] var6 = ifc.getMethods();
            for (Method ifcMethod : var6) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }

    private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
        return paramTypes.length == method.getParameterCount() && Arrays.equals(paramTypes, method.getParameterTypes());
    }
}
