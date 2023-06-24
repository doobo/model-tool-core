package javax.doobo.utils;


/**
 * 简单树型编码工具
 */
public abstract class CodeTreeUtils {

    private final static String ZERO = "10000";

    /**
     * 获取叶子节点编码
     */
    public static String getChildCode(String last, String start){
        return getNextCode(last, 1, start);
    }

    /**
     * 获取临近编码
     */
    public static String getNearCode(String last, String start){
        return getNextCode(last, 0, start);
    }

    /**
     * 计算下一个节点的code
     * @param type 0同一节点, 1父节点编码
     * @param  start 起始节点
     */
    public static String getNextCode(String last, int type, String start){
        if(start == null || start.isEmpty()){
            start = ZERO;
        }
        int dx = start.length();
        if(last == null || last.isEmpty() || last.length() < dx){
            return start;
        }
        int dy = dx - 1;
        int a = last.length();
        String next = last.substring(a - dx, a);
        String parent = last.substring(0, a - dx);
        String end;
        //父级节点
        if(type == 1){
            return last + start;
        }
        //同一级节点
        if(next.charAt(0) >= "9".charAt(0) ){
            String n3 = last.substring(a - dy, a);
            int  m3 = Integer.parseInt(n3) + 1;
            int max3 = Integer.parseInt(rightPad("",  dy, "9"));
            if(m3 <= max3){
                end = "" + next.charAt(0) + leftPad("" + m3,  dy, "0");
                return parent + end;
            }
            if(next.charAt(0) == "9".charAt(0)){
                end = "A" + leftPad("" ,  dy, "0");
                return parent + end;
            }
            if(next.charAt(0) == "Z".charAt(0)){
                end = "a" + leftPad("" ,  dy, "0");
                return parent + end;
            }
            if(next.charAt(0) != "9".charAt(0)){
                end = "" + (char) (next.charAt(0)+1) + leftPad("" ,  dy, "0");
                return parent + end;
            }
        }
        int n4 = Integer.parseInt(next) + 1;
        return parent + leftPad("" + n4, dx, "0");
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : repeat(padChar, pads).concat(str);
            }
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";
            }
            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }
                return (new String(padding)).concat(str);
            }
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                return pads > 8192 ? rightPad(str, size, String.valueOf(padChar)) : str.concat(repeat(padChar, pads));
            }
        }
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return rightPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return str.concat(padStr);
            } else if (pads < padLen) {
                return str.concat(padStr.substring(0, pads));
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return str.concat(new String(padding));
            }
        }
    }

    public static String repeat(char ch, int repeat) {
        if (repeat <= 0) {
            return "";
        } else {
            char[] buf = new char[repeat];

            for(int i = repeat - 1; i >= 0; --i) {
                buf[i] = ch;
            }
            return new String(buf);
        }
    }
}
