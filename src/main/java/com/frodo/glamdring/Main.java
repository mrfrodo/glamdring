package com.frodo.glamdring;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Prints all classes in a nice hierarchical tree based on packages
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String basePackage = "com.frodo.glamdring";
        List<Class<?>> classes = getClassesRecursive(basePackage);

        // Build package tree
        PackageNode root = new PackageNode(basePackage);
        for (Class<?> clazz : classes) {
            // Skip helper classes
            if (clazz.getSimpleName().equals("Main") || clazz.getSimpleName().equals("PackageNode")) continue;
            root.addClass(clazz);
        }

        // Print tree
        root.print("", true);
    }

    private static List<Class<?>> getClassesRecursive(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) return classes;

        File directory = new File(resource.toURI());
        if (!directory.exists()) return classes;

        scanDirectory(packageName, directory, classes);
        return classes;
    }

    private static void scanDirectory(String packageName, File directory, List<Class<?>> classes) throws ClassNotFoundException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                scanDirectory(packageName + "." + file.getName(), file, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replaceAll("\\.class$", "");
                classes.add(Class.forName(className));
            }
        }
    }

    // Helper class to build package tree
    static class PackageNode {
        String name;
        Map<String, PackageNode> subPackages = new TreeMap<>();
        Set<String> classes = new TreeSet<>();

        PackageNode(String name) {
            this.name = name;
        }

        void addClass(Class<?> clazz) {
            String packageName = clazz.getPackageName();
            if (packageName.equals(this.name)) {
                classes.add(clazz.getSimpleName());
            } else {
                String subPackageName = packageName.substring(this.name.length() + 1);
                String firstPart = subPackageName.split("\\.")[0];
                subPackages.computeIfAbsent(firstPart, k -> new PackageNode(this.name + "." + firstPart))
                        .addClass(clazz);
            }
        }

        void print(String prefix, boolean isLast) {
            System.out.println(prefix + (prefix.isEmpty() ? "" : (isLast ? "└─ " : "├─ ")) + name.substring(name.lastIndexOf('.') + 1));

            List<String> classList = new ArrayList<>(classes);
            for (int i = 0; i < classList.size(); i++) {
                boolean lastClass = (i == classList.size() - 1) && subPackages.isEmpty();
                System.out.println(prefix + (isLast ? "   " : "│  ") + (lastClass ? "└─ " : "├─ ") + classList.get(i));
            }

            List<PackageNode> subList = new ArrayList<>(subPackages.values());
            for (int i = 0; i < subList.size(); i++) {
                subList.get(i).print(prefix + (isLast ? "   " : "│  "), i == subList.size() - 1);
            }
        }
    }
}