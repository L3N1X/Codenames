package hr.algebra.codenames.utils.Ldocs;

import java.util.Arrays;

public class LDocsFormattingUtils {
    private LDocsFormattingUtils() {

    }

    public static String getFormattedField(String field) {
        StringBuilder sb = new StringBuilder();
        String[] details = field.split(" ");
        for (String detail : details) {
            if (Arrays.asList("private", "default", "protected", "static", "public").contains(detail)) {
                sb.append("<span style='color: lightblue'>").append(detail).append("</span> ");
            } else if (Arrays.asList("final", "throws").contains(detail)) {
                sb.append("<span style='color: lightgreen'>").append(detail).append("</span> ");
            } else {
                String[] packageDetails = detail.split("\\.");
                for (int i = 0; i < packageDetails.length; i++) {
                    if (i == packageDetails.length - 1) {
                        if (detail.equals(details[details.length - 1])) {
                            sb.append("<span style='font-weight: bold; color: #edff69'>").append(packageDetails[i]).append("</span>");
                        } else {
                            sb.append("<span style='font-weight: bold; color: #b300db'>").append(packageDetails[i]).append("</span>");
                        }
                    } else
                        sb.append(packageDetails[i]).append(".");
                }
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String getFormattedMethodReturnType(String method) {
        StringBuilder sb = new StringBuilder();
        String[] details = method.split(" ");
        for (String detail : details) {
            if (Arrays.asList("private", "default", "protected", "static", "public").contains(detail)) {
                sb.append("<span style='color: lightblue'>").append(detail).append("</span> ");
            } else if (Arrays.asList("final", "throws").contains(detail)) {
                sb.append("<span style='color: lightgreen'>").append(detail).append("</span> ");
            } else {
                String[] packageDetails = detail.split("\\.");
                if (packageDetails.length != 1) {
                    for (int i = 0; i < packageDetails.length; i++) {
                        if (i == packageDetails.length - 1) {
                            if (detail.equals(details[details.length - 1])) {
                                sb.append("<span style='font-weight: bold; color: #b300db'>").append(packageDetails[i]).append("</span>");
                            } else {
                                sb.append("<span style='font-weight: bold; color: darkorange'>").append(packageDetails[i]).append("</span>");
                            }
                        } else
                            sb.append(packageDetails[i]).append(".");
                    }

                } else {
                    if (Arrays.asList("void", "int", "char", "boolean", "byte", "long", "float", "double").contains(packageDetails[0])) {
                        sb.append("<span style='font-weight: bold; color: #b300db'>").append(packageDetails[0]).append("</span>");
                    } else {
                        sb.append(packageDetails[0]);
                    }
                }
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String formatForMethodParameter(String parameter){
        StringBuilder sb = new StringBuilder();
        String[] parameterDetails = parameter.split(" ");
        String[] packageDetails = parameterDetails[0].split("\\.");
        for (String packageDetail : packageDetails) {
            if(packageDetail.contains("<") || packageDetail.contains(">")){
                String edit1 = packageDetail.replace("<", "&lt;");
                String edit2 = edit1.replace(">", "&gt;");
                if(packageDetail.equals(packageDetails[packageDetails.length - 1])){
                    sb.append("<span style='font-weight: bold; color: #b300db'>").append(edit2)
                            .append(" ")
                            .append("</span>");
                }
                else {
                    sb.append(edit2).append(".");
                }
                continue;
            }
            if(packageDetail.equals(packageDetails[packageDetails.length - 1]))
                sb.append("<span style='font-weight: bold; color: #b300db'>").append(packageDetail)
                        .append(" ")
                        .append("</span>");
            else
                sb.append(packageDetail).append(".");
        }
        sb.append("<span style='color: #5cffad'>").append(parameterDetails[1]).append(" ").append("</span>");
        return sb.toString();
    }
}
