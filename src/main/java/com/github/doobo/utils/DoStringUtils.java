package com.github.doobo.utils;

import java.lang.reflect.Array;
import java.util.*;

public abstract class DoStringUtils {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (isEmpty(arr)) {
            return "";
        } else if (arr.length == 1) {
            return nullSafeToString(arr[0]);
        } else {
            StringJoiner sj = new StringJoiner(delim);
            Object[] var3 = arr;
            int var4 = arr.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Object elem = var3[var5];
                sj.add(String.valueOf(elem));
            }

            return sj.toString();
        }
    }

    public static String[] trimArrayElements(String[] array) {
        if (isEmpty(array)) {
            return array;
        } else {
            String[] result = new String[array.length];
            for(int i = 0; i < array.length; ++i) {
                String element = array[i];
                result[i] = element != null ? element.trim() : null;
            }
            return result;
        }
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, (String)null);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return EMPTY_STRING_ARRAY;
        } else if (delimiter == null) {
            return new String[]{str};
        } else {
            List<String> result = new ArrayList<>();
            int pos;
            if (delimiter.isEmpty()) {
                for(pos = 0; pos < str.length(); ++pos) {
                    result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
                }
            } else {
                int delPos;
                for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                    result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                }

                if (str.length() > 0 && pos <= str.length()) {
                    result.add(deleteAny(str.substring(pos), charsToDelete));
                }
            }
            return toStringArray(result);
        }
    }

    public static String deleteAny(String inString, String charsToDelete) {
        if (hasLength(inString) && hasLength(charsToDelete)) {
            int lastCharIndex = 0;
            char[] result = new char[inString.length()];

            for(int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if (charsToDelete.indexOf(c) == -1) {
                    result[lastCharIndex++] = c;
                }
            }

            if (lastCharIndex == inString.length()) {
                return inString;
            } else {
                return new String(result, 0, lastCharIndex);
            }
        } else {
            return inString;
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return !isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static String[] toStringArray(Enumeration<String> enumeration) {
        return enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY;
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    public static Set<String> commaDelimitedListToSet(String str) {
        String[] tokens = commaDelimitedListToStringArray(str);
        return new LinkedHashSet(Arrays.asList(tokens));
    }

    public static String nullSafeToString(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof String) {
            return (String)obj;
        } else if (obj instanceof Object[]) {
            return nullSafeToString((Object[])((Object[])obj));
        } else if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[])((boolean[])obj));
        } else if (obj instanceof byte[]) {
            return nullSafeToString((byte[])((byte[])obj));
        } else if (obj instanceof char[]) {
            return nullSafeToString((char[])((char[])obj));
        } else if (obj instanceof double[]) {
            return nullSafeToString((double[])((double[])obj));
        } else if (obj instanceof float[]) {
            return nullSafeToString((float[])((float[])obj));
        } else if (obj instanceof int[]) {
            return nullSafeToString((int[])((int[])obj));
        } else if (obj instanceof long[]) {
            return nullSafeToString((long[])((long[])obj));
        } else if (obj instanceof short[]) {
            return nullSafeToString((short[])((short[])obj));
        } else {
            String str = obj.toString();
            return str != null ? str : "";
        }
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional)obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }

    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[])((Object[])source);
        } else if (source == null) {
            return EMPTY_OBJECT_ARRAY;
        } else if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        } else {
            int length = Array.getLength(source);
            if (length == 0) {
                return EMPTY_OBJECT_ARRAY;
            } else {
                Class<?> wrapperType = Array.get(source, 0).getClass();
                Object[] newArray = (Object[])((Object[])Array.newInstance(wrapperType, length));

                for(int i = 0; i < length; ++i) {
                    newArray[i] = Array.get(source, i);
                }

                return newArray;
            }
        }
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            int len = str.length();
            StringBuilder sb = new StringBuilder(str.length());

            for(int i = 0; i < len; ++i) {
                char c = str.charAt(i);
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }
}
