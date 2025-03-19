package com.ben.smartcv.common.util;

public class StringHelper {

    public static String convertToUpperHyphen(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                result.append("-");
            }
            result.append(c);
        }

        return result.toString().toUpperCase();
    }

    public static String formatFieldName(String fieldName) {
        return fieldName.replaceAll("([a-z])([A-Z])", "$1 $2") // Tách camelCase bằng khoảng trắng
                .replaceFirst("^.", fieldName.substring(0, 1).toUpperCase()); // Viết hoa chữ cái đầu
    }

    public static String arrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        return String.join("|", array);
    }

    // Chuyển từ String thành String[]
    public static String[] stringToArray(String str) {
        if (str == null || str.isEmpty()) {
            return new String[0];
        }
        return str.split("\\|");
    }


}
