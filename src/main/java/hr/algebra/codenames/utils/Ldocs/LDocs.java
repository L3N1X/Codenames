package hr.algebra.codenames.utils.Ldocs;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class LDocs {

    private LDocs() {
    }

    public static String generateLDocs(
            String applicationName,
            String authorName,
            String version,
            Class<?>... classes){
       String startHtml = String.format(
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Codenames LDocs</title>
                    <link rel="stylesheet" href="LDocsStyles.css">
                </head>
                <body><h1>%s %s LDocs (auto-generated)</h1><h3 id='author_heading'>Author: %s</h3>"""
        , applicationName, version, authorName);
       StringBuilder reflectionDocumentation = new StringBuilder();
        reflectionDocumentation.append(startHtml);
        for (Class<?> clazz : classes) {
            readClassAndMembersInfo(clazz, reflectionDocumentation);
        }
        reflectionDocumentation.append("</body>\n").append("</html>");
        return reflectionDocumentation.toString();
    }

    private static void readClassAndMembersInfo(Class<?>clazz, StringBuilder reflectionDocumentation) {
        //Append short class name as a heading
        String className = clazz.getName();
        String[] details = className.split("\\.");
        String classHeading = details[details.length - 1];

        reflectionDocumentation
                        .append("<h2 style='color: #ff00c8'>")
                        .append(classHeading)
                        .append("</h2>");
        reflectionDocumentation.append("<p>").append(System.lineSeparator());
        readClassInfo(clazz, reflectionDocumentation);
        reflectionDocumentation.append(System.lineSeparator()).append("</p>").append(System.lineSeparator());

        reflectionDocumentation.append("<h3>FIELDS</h3>");

        reflectionDocumentation.append("<p>").append(System.lineSeparator());
        appendFields(clazz, reflectionDocumentation);
        reflectionDocumentation.append(System.lineSeparator()).append("</p>").append(System.lineSeparator());

        reflectionDocumentation.append("<h3>METHODS</h3>");

        reflectionDocumentation.append("<p>").append(System.lineSeparator());
        appendMethods(clazz, reflectionDocumentation);
        reflectionDocumentation.append(System.lineSeparator()).append("</p>").append(System.lineSeparator());

        reflectionDocumentation.append("<h3>CONSTRUCTORS</h3>");

        reflectionDocumentation.append("<p>").append(System.lineSeparator());
        appendConstructors(clazz, reflectionDocumentation);
        reflectionDocumentation.append(System.lineSeparator()).append("</p>").append(System.lineSeparator());
    }

    private static void readClassInfo(Class<?> clazz, StringBuilder classInfo) {
        classInfo.append("<h3>");
        appendModifiers(clazz, classInfo);
        classInfo.append(" ").append(LDocsFormattingUtils.getFormattedMethodReturnType(clazz.getName())).append("</h3>");
        appendPackage(clazz, classInfo);
        appendParent(clazz, classInfo, true);
        appendInterfaces(clazz, classInfo);
    }

    private static void appendPackage(Class<?> clazz, StringBuilder classInfo) {
        classInfo
                .append(clazz.getPackage().toString())
                .append(System.lineSeparator())
                .append("</br>")
                .append(System.lineSeparator());
    }

    private static void appendModifiers(Class<?> clazz, StringBuilder classInfo) {
        classInfo
                .append("<span style='color: lightblue'>")
                .append(Modifier.toString(clazz.getModifiers()))
                .append("</span>");
    }

    private static void appendParent(Class<?> clazz, StringBuilder classInfo, boolean first) {
        Class<?> parent = clazz.getSuperclass();
        if(parent == null) {
            return;
        }
        if (first) {
            classInfo
                    .append("<span style='font-weight: bold; color: orange'>extends</span>");
        }
        classInfo
                .append(" ")
                .append(LDocsFormattingUtils.getFormattedField(parent.getName()));
        appendParent(parent, classInfo, false);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder classInfo) {
        if (clazz.getInterfaces().length > 0) {
            classInfo
                    .append(System.lineSeparator())
                    .append("<b style='color: orange'>implements </b>")
                    .append(
                            Arrays.stream(clazz.getInterfaces())
                                    .map(Class::getSimpleName)
                                    .collect(Collectors.joining(" "))
                    );
        }
    }

    private static void appendFields(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Field[] fields = clazz.getDeclaredFields();
        classAndMembersInfo.append("<p>");
        classAndMembersInfo
                .append(
                Arrays.stream(fields)
                        .map(Objects::toString)
                        .map(LDocsFormattingUtils::getFormattedField)
                        .collect(Collectors.joining("</br>\n"))
        );
        classAndMembersInfo.append("</p>");
    }

    private static void appendMethods(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            appendAnnotations(method, classAndMembersInfo);
            classAndMembersInfo
                    .append("<span style='color: lightblue'>")
                    .append(Modifier.toString(method.getModifiers()))
                    .append("</span>")
                    .append(" ")
                    .append(LDocsFormattingUtils.getFormattedMethodReturnType(String.valueOf(method.getReturnType())))
                    .append(" ")
                    .append("<span style='font-weight: bold; color: #dbfa6b'>")
                    .append(method.getName())
                    .append("</span>");
            appendParameters(method, classAndMembersInfo);
            appendExceptions(method, classAndMembersInfo);
            classAndMembersInfo.append("</br>");
        }
    }
    private static void appendAnnotations(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getAnnotations())
                        .map(Objects::toString)
                        .map(annotation -> "<span style='color: pink'>"  + annotation + "</span>")
                        .collect(Collectors.joining(System.lineSeparator())));
        if(executable.getAnnotations().length != 0)
            classAndMembersInfo.append("</br>");
    }
    private static void appendParameters(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getParameters())
                        .map(Objects::toString)
                        .map(LDocsFormattingUtils::formatForMethodParameter)
                        .collect(Collectors.joining(", ", "(", ")"))
        );
    }
    private static void appendExceptions(Executable executable, StringBuilder classAndMembersInfo) {
        if (executable.getExceptionTypes().length > 0) {
            classAndMembersInfo.append("<span style='color: lightgreen'> throws </span>");
            classAndMembersInfo.append(
                    Arrays.stream(executable.getExceptionTypes())
                            .map(Class::getName)
                            .collect(Collectors.joining(" "))
            );
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            classAndMembersInfo
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
            appendAnnotations(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append(System.lineSeparator())
                    .append("<span style='color: lightblue'>")
                    .append(Modifier.toString(constructor.getModifiers()))
                    .append("</span>")
                    .append(" ")
                    .append(constructor.getName());
            appendParameters(constructor, classAndMembersInfo);
            appendExceptions(constructor, classAndMembersInfo);
        }
    }
}
