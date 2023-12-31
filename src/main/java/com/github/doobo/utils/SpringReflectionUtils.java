package com.github.doobo.utils;

import com.github.doobo.model.DoAssert;
import com.github.doobo.map.ConcurrentReferenceHashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * spring的反射攻击
 */
public abstract class SpringReflectionUtils {

    private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
    private static final Method[] NO_METHODS = new Method[0];
    private static final Field[] NO_FIELDS = new Field[0];
    private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap(256);
    public static final SpringReflectionUtils.MethodFilter NON_BRIDGED_METHODS = new SpringReflectionUtils.MethodFilter() {
        public boolean matches(Method method) {
            return !method.isBridge();
        }
    };
    public static final SpringReflectionUtils.MethodFilter USER_DECLARED_METHODS = new SpringReflectionUtils.MethodFilter() {
        public boolean matches(Method method) {
            return !method.isBridge() && method.getDeclaringClass() != Object.class;
        }
    };
    public static final SpringReflectionUtils.FieldFilter COPYABLE_FIELDS = new SpringReflectionUtils.FieldFilter() {
        public boolean matches(Field field) {
            return !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
        }
    };

    public SpringReflectionUtils() {
    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, (Class)null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        DoAssert.notNull(clazz, "Class must not be null");
        DoAssert.isTrue(name != null || type != null, "Either name or type of the field must be specified");

        for(Class searchType = clazz; Object.class != searchType && searchType != null; searchType = searchType.getSuperclass()) {
            Field[] fields = getDeclaredFields(searchType);
            Field[] var5 = fields;
            int var6 = fields.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Field field = var5[var7];
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
        }

        return null;
    }

    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException var4) {
            handleReflectionException(var4);
            throw new IllegalStateException("Unexpected reflection exception - " + var4.getClass().getName() + ": " + var4.getMessage());
        }
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException var3) {
            handleReflectionException(var3);
            throw new IllegalStateException("Unexpected reflection exception - " + var3.getClass().getName() + ": " + var3.getMessage());
        }
    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        DoAssert.notNull(clazz, "Class must not be null");
        DoAssert.notNull(name, "Method name must not be null");

        for(Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);
            Method[] var5 = methods;
            int var6 = methods.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Method method = var5[var7];
                if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
        }

        return null;
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception var4) {
            handleReflectionException(var4);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static Object invokeJdbcMethod(Method method, Object target) throws SQLException {
        return invokeJdbcMethod(method, target);
    }

    public static Object invokeJdbcMethod(Method method, Object target, Object... args) throws SQLException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException var4) {
            handleReflectionException(var4);
        } catch (InvocationTargetException var5) {
            if (var5.getTargetException() instanceof SQLException) {
                throw (SQLException)var5.getTargetException();
            }

            handleInvocationTargetException(var5);
        }

        throw new IllegalStateException("Should never get here");
    }

    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        } else if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
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

    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw (Exception)ex;
        } else if (ex instanceof Error) {
            throw (Error)ex;
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static boolean declaresException(Method method, Class<?> exceptionType) {
        DoAssert.notNull(method, "Method must not be null");
        Class<?>[] declaredExceptions = method.getExceptionTypes();
        Class[] var3 = declaredExceptions;
        int var4 = declaredExceptions.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Class<?> declaredException = var3[var5];
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    public static boolean isEqualsMethod(Method method) {
        if (method != null && method.getName().equals("equals")) {
            Class<?>[] paramTypes = method.getParameterTypes();
            return paramTypes.length == 1 && paramTypes[0] == Object.class;
        } else {
            return false;
        }
    }

    public static boolean isHashCodeMethod(Method method) {
        return method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0;
    }

    public static boolean isToStringMethod(Method method) {
        return method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0;
    }

    public static boolean isObjectMethod(Method method) {
        if (method == null) {
            return false;
        } else {
            try {
                Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static boolean isCglibRenamedMethod(Method renamedMethod) {
        String name = renamedMethod.getName();
        if (!name.startsWith("CGLIB$")) {
            return false;
        } else {
            int i;
            for(i = name.length() - 1; i >= 0 && Character.isDigit(name.charAt(i)); --i) {
            }

            return i > "CGLIB$".length() && i < name.length() - 1 && name.charAt(i) == '$';
        }
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }

    }

    public static void doWithLocalMethods(Class<?> clazz, SpringReflectionUtils.MethodCallback mc) {
        Method[] methods = getDeclaredMethods(clazz);
        Method[] var3 = methods;
        int var4 = methods.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];

            try {
                mc.doWith(method);
            } catch (IllegalAccessException var8) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + var8);
            }
        }

    }

    public static void doWithMethods(Class<?> clazz, SpringReflectionUtils.MethodCallback mc) {
        doWithMethods(clazz, mc, (SpringReflectionUtils.MethodFilter)null);
    }

    public static void doWithMethods(Class<?> clazz, SpringReflectionUtils.MethodCallback mc, SpringReflectionUtils.MethodFilter mf) {
        Method[] methods = getDeclaredMethods(clazz);
        Method[] var4 = methods;
        int var5 = methods.length;

        int var6;
        for(var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            if (mf == null || mf.matches(method)) {
                try {
                    mc.doWith(method);
                } catch (IllegalAccessException var9) {
                    throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + var9);
                }
            }
        }

        if (clazz.getSuperclass() != null) {
            doWithMethods(clazz.getSuperclass(), mc, mf);
        } else if (clazz.isInterface()) {
            Class[] var10 = clazz.getInterfaces();
            var5 = var10.length;

            for(var6 = 0; var6 < var5; ++var6) {
                Class<?> superIfc = var10[var6];
                doWithMethods(superIfc, mc, mf);
            }
        }

    }

    public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
        final List<Method> methods = new ArrayList(32);
        doWithMethods(leafClass, new SpringReflectionUtils.MethodCallback() {
            public void doWith(Method method) {
                methods.add(method);
            }
        });
        return (Method[])methods.toArray(new Method[methods.size()]);
    }

    public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) {
        final List<Method> methods = new ArrayList(32);
        doWithMethods(leafClass, new SpringReflectionUtils.MethodCallback() {
            public void doWith(Method method) {
                boolean knownSignature = false;
                Method methodBeingOverriddenWithCovariantReturnType = null;
                Iterator var4 = methods.iterator();

                while(var4.hasNext()) {
                    Method existingMethod = (Method)var4.next();
                    if (method.getName().equals(existingMethod.getName()) && Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
                        if (existingMethod.getReturnType() != method.getReturnType() && existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
                            methodBeingOverriddenWithCovariantReturnType = existingMethod;
                            break;
                        }

                        knownSignature = true;
                        break;
                    }
                }

                if (methodBeingOverriddenWithCovariantReturnType != null) {
                    methods.remove(methodBeingOverriddenWithCovariantReturnType);
                }

                if (!knownSignature && !SpringReflectionUtils.isCglibRenamedMethod(method)) {
                    methods.add(method);
                }

            }
        });
        return (Method[])methods.toArray(new Method[methods.size()]);
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) {
        DoAssert.notNull(clazz, "Class must not be null");
        Method[] result = (Method[])declaredMethodsCache.get(clazz);
        if (result == null) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                int index = declaredMethods.length;

                for(Iterator var5 = defaultMethods.iterator(); var5.hasNext(); ++index) {
                    Method defaultMethod = (Method)var5.next();
                    result[index] = defaultMethod;
                }
            } else {
                result = declaredMethods;
            }

            declaredMethodsCache.put(clazz, result.length == 0 ? NO_METHODS : result);
        }

        return result;
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        Class[] var2 = clazz.getInterfaces();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> ifc = var2[var4];
            Method[] var6 = ifc.getMethods();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Method ifcMethod = var6[var8];
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList();
                    }

                    result.add(ifcMethod);
                }
            }
        }

        return result;
    }

    public static void doWithLocalFields(Class<?> clazz, SpringReflectionUtils.FieldCallback fc) {
        Field[] var2 = getDeclaredFields(clazz);
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];

            try {
                fc.doWith(field);
            } catch (IllegalAccessException var7) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + var7);
            }
        }
    }

    public static void doWithFields(Class<?> clazz, SpringReflectionUtils.FieldCallback fc) {
        doWithFields(clazz, fc, (SpringReflectionUtils.FieldFilter)null);
    }

    public static void doWithFields(Class<?> clazz, SpringReflectionUtils.FieldCallback fc, SpringReflectionUtils.FieldFilter ff) {
        Class targetClass = clazz;

        do {
            Field[] fields = getDeclaredFields(targetClass);
            Field[] var5 = fields;
            int var6 = fields.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Field field = var5[var7];
                if (ff == null || ff.matches(field)) {
                    try {
                        fc.doWith(field);
                    } catch (IllegalAccessException var10) {
                        throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + var10);
                    }
                }
            }

            targetClass = targetClass.getSuperclass();
        } while(targetClass != null && targetClass != Object.class);

    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        DoAssert.notNull(clazz, "Class must not be null");
        Field[] result = (Field[])declaredFieldsCache.get(clazz);
        if (result == null) {
            result = clazz.getDeclaredFields();
            declaredFieldsCache.put(clazz, result.length == 0 ? NO_FIELDS : result);
        }

        return result;
    }

    public static void shallowCopyFieldState(final Object src, final Object dest) {
        DoAssert.notNull(src, "Source for field copy cannot be null");
        DoAssert.notNull(dest, "Destination for field copy cannot be null");
        if (!src.getClass().isAssignableFrom(dest.getClass())) {
            throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
        } else {
            doWithFields(src.getClass(), new SpringReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    SpringReflectionUtils.makeAccessible(field);
                    Object srcValue = field.get(src);
                    field.set(dest, srcValue);
                }
            }, COPYABLE_FIELDS);
        }
    }

    public static void clearCache() {
        declaredMethodsCache.clear();
        declaredFieldsCache.clear();
    }

    public interface FieldFilter {
        boolean matches(Field var1);
    }

    public interface FieldCallback {
        void doWith(Field var1) throws IllegalArgumentException, IllegalAccessException;
    }

    public interface MethodFilter {
        boolean matches(Method var1);
    }

    public interface MethodCallback {
        void doWith(Method var1) throws IllegalArgumentException, IllegalAccessException;
    }
}
