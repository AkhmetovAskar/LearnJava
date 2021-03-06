package javacore.reflection;

import java.lang.reflect.*;
import java.util.Date;

public class TestReflection {
    public static void main(String[] args) {
        Date date = new Date();
        Class<?> cl = date.getClass();
        System.out.println(cl.getName());
        Class<?> cl2 = Date.class;
        System.out.println(cl2.getName());
        try {
            Date date2 = Date.class.newInstance();
            System.out.println(date2);
            Object obj = Class.forName("java.util.Date").newInstance();
            System.out.println(obj.getClass());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        printClass("javase.reflection.Employee");

        System.out.println("==================");
        Employee employee = new Employee("Askar", 100_000);
        try {
            Field field = Employee.class.getDeclaredField("name");
            field.setAccessible(true);
            Object v = field.get(employee);
            System.out.println("Old value : " + v);
            field.set(employee, "Petya");
            System.out.println("New value : " + field.get(employee));
            Method method = Employee.class.getDeclaredMethod("printSomething");
            method.invoke(employee);
            Method method2 = Employee.class.getDeclaredMethod("printParameter", int.class);
            method2.invoke(employee, 19);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void printClass(String className) {
        try {
            Class<?> cl = Class.forName(className);
            Class<?> supercl = cl.getSuperclass();
            StringBuilder sb = new StringBuilder();
            sb.append(Modifier.toString(cl.getModifiers()))
                    .append(' ')
                    .append("class ")
                    .append(cl.getName())
                    .append(getTypeParametersString(cl.getTypeParameters()))
                    .append(supercl != null && supercl != Object.class ? " extends " + supercl.getName() : "")
                    .append(" {\n");
            System.out.println(sb.toString());
            printConstructors(cl);
            System.out.println();
            printMethods(cl);
            System.out.println();
            printFields(cl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getTypeParametersString(TypeVariable<?>[] typeParameters) {
        StringBuilder sb = new StringBuilder();
        if (typeParameters.length > 0) {
            sb.append('<');
            for (TypeVariable<?> type : typeParameters) {
                sb.append(type.getName()).append(',');
            }
            sb.deleteCharAt(sb.toString().length()-1);
            sb.append('>');
        }
        return sb.toString();
    }

    public static void printConstructors(Class<?> cl) {
        Constructor<?>[] constructors = cl.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t")
                    .append(Modifier.toString(c.getModifiers()))
                    .append(' ')
                    .append(c.getName())
                    .append('(');
            Class<?>[] parameters = c.getParameterTypes();
            for (Class<?> parameter : parameters) {
                sb.append(parameter.getName())
                        .append(getTypeParametersString(parameter.getTypeParameters()))
                        .append(',');
            }
            if (parameters.length > 0) sb.deleteCharAt(sb.toString().length()-1);
            sb.append(");");
            System.out.println(sb.toString());
        }
    }

    public static void printMethods(Class<?> cl) {
        Method[] methods = cl.getDeclaredMethods();
        for (Method m : methods) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t")
                    .append(Modifier.toString(m.getModifiers()))
                    .append(' ')
                    .append(m.getReturnType().getName())
                    .append(' ')
                    .append(m.getName())
                    .append('(');
            Class<?>[] parameters = m.getParameterTypes();
            for (Class<?> parameter : parameters) {
                sb.append(parameter.getName())
                        .append(getTypeParametersString(parameter.getTypeParameters()))
                        .append(',');
            }
            if (parameters.length > 0) sb.deleteCharAt(sb.toString().length()-1);
            sb.append(");");
            System.out.println(sb.toString());
        }
    }

    public static void printFields(Class<?> cl) {
        Field[] fields = cl.getDeclaredFields();
        for (Field f : fields) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t")
                    .append(Modifier.toString(f.getModifiers()))
                    .append(' ')
                    .append(f.getGenericType().getTypeName());
            sb.append(' ')
                    .append(f.getName())
                    .append(';');
            System.out.println(sb.toString());
        }
    }
}