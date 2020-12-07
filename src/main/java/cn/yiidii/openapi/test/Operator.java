package cn.yiidii.openapi.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author : YiiDii Wang
 * @description: 阈值操作符比较器
 * @date : Created in 2020/9/15 16:40:54
 */
public enum Operator {
    GT("gt", "Greater than") {
        public boolean action(String param1String1, String param1String2) {
            double d1 = 0.0D;
            double d2 = 0.0D;
            try {
                d1 = Double.parseDouble(param1String1);
                d2 = Double.parseDouble(param1String2);
            } catch (Exception exception) {
                return false;
            }
            return (d1 > d2);
        }
    },
    LT("lt", "Less than") {
        public boolean action(String param1String1, String param1String2) {
            double d1 = 0.0D;
            double d2 = 0.0D;
            try {
                d1 = Double.parseDouble(param1String1);
                d2 = Double.parseDouble(param1String2);
            } catch (Exception exception) {
                return false;
            }
            return (d1 < d2);
        }
    },
    EQ("eq", "equals") {
        public boolean action(String param1String1, String param1String2) {
            if (param1String1 == null || param1String2 == null)
                return false;
            if (param1String1.equals(param1String2))
                return true;
            param1String1 = param1String1.trim();
            param1String2 = param1String2.trim();
            if (param1String2.startsWith("[expr:") && param1String2.endsWith("]")) {
                String str = param1String2.substring("[expr:".length(), param1String2.length() - 1);
                return compareExpr(param1String1, str.trim());
            }
            param1String1 = toBooleanVar(param1String1);
            param1String2 = toBooleanVar(param1String2);
            double d1 = 0.0D;
            double d2 = 0.0D;
            try {
                d1 = Double.parseDouble(param1String1);
                d2 = Double.parseDouble(param1String2);
                return (Math.abs(d1 - d2) < 1.0E-5D);
            } catch (Exception exception) {
                return false;
            }
        }
    },
    NE("ne", "not equals") {
        public boolean action(String param1String1, String param1String2) {
            if (param1String1 == null || param1String2 == null)
                return true;
            if (param1String1.equals(param1String2))
                return false;
            param1String1 = param1String1.trim();
            param1String2 = param1String2.trim();
            if (param1String2.startsWith("[expr:") && param1String2.endsWith("]")) {
                String str = param1String2.substring("[expr:".length(), param1String2.length() - 1);
                return !compareExpr(param1String1, str.trim());
            }
            param1String1 = toBooleanVar(param1String1);
            param1String2 = toBooleanVar(param1String2);
            double d1 = 0.0D;
            double d2 = 0.0D;
            try {
                d1 = Double.parseDouble(param1String1);
                d2 = Double.parseDouble(param1String2);
                return (Math.abs(d1 - d2) > 1.0E-5D);
            } catch (Exception exception) {
                return true;
            }
        }
    },
    LE("le", "Less than or equals") {
        public boolean action(String param1String1, String param1String2) {
            double d1 = 0.0D;
            double d2 = 0.0D;
            try {
                d1 = Double.parseDouble(param1String1);
                d2 = Double.parseDouble(param1String2);
            } catch (Exception exception) {
                return false;
            }
            return (Math.abs(d1 - d2) < 1.0E-5D || d2 - d1 > 1.0E-5D);
        }
    },
    GE("ge", "Greater than or equals") {
        public boolean action(String param1String1, String param1String2) {
            double d1 = 0.0D;
            double d2 = 0.0D;
            try {
                d1 = Double.parseDouble(param1String1);
                d2 = Double.parseDouble(param1String2);
            } catch (Exception exception) {
                return false;
            }
            return (Math.abs(d1 - d2) < 1.0E-5D || d1 - d2 > 1.0E-5D);
        }
    },
    CN("cn", "contains") {
        public boolean action(String param1String1, String param1String2) {
            return (param1String1.matches(param1String2));
        }
    },
    NC("nc", "not contains") {
        public boolean action(String param1String1, String param1String2) {
            return (!param1String1.matches(param1String2));
        }
    };

    private String name;

    private String displayName;

    public abstract boolean action(String paramString1, String paramString2);

    Operator(String paramString1, String paramString2) {
        this.name = paramString1;
        this.displayName = paramString2;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Operator getOperatorByName(String paramString) {
        Operator[] arrayOfOperator = values();
        for (Operator operator : arrayOfOperator) {
            if (operator.name.equals(paramString.toLowerCase()))
                return operator;
        }
        return null;
    }

    public static String getDisplayNameByName(String paramString) {
        Operator operator = getOperatorByName(paramString);
        return operator.displayName;
    }

    private static String toBooleanVar(String paramString) {
        paramString = paramString.toLowerCase();
        if (paramString.equals("true") || paramString.equals("yes")) {
            paramString = "1";
        } else if (paramString.equals("false") || paramString.equals("no")) {
            paramString = "0";
        }
        return paramString;
    }


    public static void main(String[] paramArrayOfString) {
        System.out.println(EQ.action("2011-11-01", "[expr:yesterday||today]"));
        String str = "1||0";
        System.out.println(str.replace("||", "或"));
        System.out.println(NE.action("C", "[expr:A||B]"));
        System.out.println(NE.action("A", "[expr:A||B]"));
        System.out.println(EQ.action("A", "[expr:A||B]"));
        System.out.println(EQ.action("C", "[expr:A||B]"));
    }

    private static boolean compareExpr(String paramString1, String paramString2) {
        String[] arrayOfString = paramString2.split("\\|\\|");
        for (String str : arrayOfString) {
            if (paramString1.equals(str.trim()))
                return true;
            if (str.equals("yesterday") || str.equals("today")) {
                Calendar calendar = Calendar.getInstance();
                if (str.equals("yesterday"))
                    calendar.add(5, -1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String str1 = simpleDateFormat.format(calendar.getTime());
                if (paramString1.startsWith(str1))
                    return true;
            }
        }
        return false;
    }
}