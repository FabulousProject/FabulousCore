package me.alpho320.fabulous.core.api.util;

public class RoundedNumberFormat {

    private static String[] formats = new String[]{"K", "M", "B", "T", "Q"};

    public static void setFormats(String[] cc) {
        formats = cc;
    }

    public static String format(double number) {
        int size = (number != 0) ? (int) Math.log10(number) : 0;
        if (size >= 3){
            while (size % 3 != 0) {
                size = size - 1;
            }
        }
        double notation = Math.pow(10, size);
        String result = (size >= 3) ? + (Math.round((number / notation) * 100) / 100.0d)+ formats[(size/3) - 1] : + number + "";
        return result.charAt(result.length()-2) == '0' ? result.replace(".0", "") : result;
    }

}