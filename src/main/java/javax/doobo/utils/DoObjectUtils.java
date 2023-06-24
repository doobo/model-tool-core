package javax.doobo.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 公共对象工具
 */
public abstract class DoObjectUtils {

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        } else {
            return obj instanceof Map && ((Map<?, ?>) obj).isEmpty();
        }
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return true;
            } else {
                return (o1.getClass().isArray() && o2.getClass().isArray()) && arrayEquals(o1, o2);
            }
        } else {
            return false;
        }
    }

    public static int nullSafeHashCode(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            if (obj.getClass().isArray()) {
                if (obj instanceof Object[]) {
                    return nullSafeHashCode((Object[])obj);
                }

                if (obj instanceof boolean[]) {
                    return nullSafeHashCode((boolean[])obj);
                }

                if (obj instanceof byte[]) {
                    return nullSafeHashCode((byte[])obj);
                }

                if (obj instanceof char[]) {
                    return nullSafeHashCode((char[])obj);
                }

                if (obj instanceof double[]) {
                    return nullSafeHashCode((double[])obj);
                }

                if (obj instanceof float[]) {
                    return nullSafeHashCode((float[])obj);
                }

                if (obj instanceof int[]) {
                    return nullSafeHashCode((int[])obj);
                }

                if (obj instanceof long[]) {
                    return nullSafeHashCode((long[])obj);
                }

                if (obj instanceof short[]) {
                    return nullSafeHashCode((short[])obj);
                }
            }

            return obj.hashCode();
        }
    }

    public static int nullSafeHashCode(Object[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (Object element : array) {
                hash = 31 * hash + nullSafeHashCode(element);
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(boolean[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (boolean element : array) {
                hash = 31 * hash + Boolean.hashCode(element);
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(byte[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (byte element : array) {
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(char[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (char element : array) {
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(double[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (double element : array) {
                hash = 31 * hash + Double.hashCode(element);
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(float[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (float element : array) {
                hash = 31 * hash + Float.hashCode(element);
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(int[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (int element : array) {
                hash = 31 * hash + element;
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(long[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (long element : array) {
                hash = 31 * hash + Long.hashCode(element);
            }

            return hash;
        }
    }

    public static int nullSafeHashCode(short[] array) {
        if (array == null) {
            return 0;
        } else {
            int hash = 7;
            for (short element : array) {
                hash = 31 * hash + element;
            }
            return hash;
        }
    }

    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[])((Object[])o1), (Object[])((Object[])o2));
        } else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[])((boolean[])o1), (boolean[])((boolean[])o2));
        } else if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[])((byte[])o1), (byte[])((byte[])o2));
        } else if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[])((char[])o1), (char[])((char[])o2));
        } else if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[])((double[])o1), (double[])((double[])o2));
        } else if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[])((float[])o1), (float[])((float[])o2));
        } else if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[])((int[])o1), (int[])((int[])o2));
        } else if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[])((long[])o1), (long[])((long[])o2));
        } else {
            return (o1 instanceof short[] && o2 instanceof short[]) && Arrays.equals((short[]) ((short[]) o1), (short[]) ((short[]) o2));
        }
    }

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for(int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> coll) {
		return !isEmpty(coll);
	}
}
