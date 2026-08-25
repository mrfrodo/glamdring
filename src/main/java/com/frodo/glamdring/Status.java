package com.frodo.glamdring;

import java.io.File;

public class Status {

    public static void main(String[] args) throws Exception {

        // im on linux debian
        // fill inn correct java command and print out memory usage

        // --- 1. JVM Heap Memory Usage ---
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory(); // Memory currently allocated to the JVM (bytes)
        long freeMemory = runtime.freeMemory();   // Free memory within the allocated pool (bytes)
        long maxMemory = runtime.maxMemory();     // Maximum memory the JVM will attempt to use (bytes)
        long usedMemory = totalMemory - freeMemory;

        System.out.println("--- JVM Heap Memory ---");
        System.out.println("Used Memory:  " + formatBytes(usedMemory, true));
        System.out.println("Free Memory:  " + formatBytes(freeMemory, true));
        System.out.println("Total Allocated: " + formatBytes(totalMemory, true));
        System.out.println("Max Memory:   " + formatBytes(maxMemory, true));

        //---------------------

        // --- 2. System Memory Usage (Linux Debian compatible) ---
        com.sun.management.OperatingSystemMXBean osBean =
                (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();

        long totalPhysicalMemory = osBean.getTotalMemorySize();
        long freePhysicalMemory = osBean.getFreeMemorySize();
        long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
        System.out.println("\n--- System Memory (Debian Host) ---");
        System.out.println("Total System Memory: " + formatBytes(totalPhysicalMemory, true));
        System.out.println("Used System Memory:  " + formatBytes(usedPhysicalMemory, true));
        System.out.println("Free System Memory:  " + formatBytes(freePhysicalMemory, true));

        //---------------------------

        // --- 3. Disk Space (Linux Root Partition) ---
        File rootPartition = new File("/");
        long totalDisk = rootPartition.getTotalSpace();
        long freeDisk = rootPartition.getUsableSpace(); // Usable space accounts for OS restrictions/permissions
        long usedDisk = totalDisk - freeDisk;

        System.out.println("\n--- Disk Space (Root /) ---");
        System.out.println("Total Disk Space: " + formatBytes(totalDisk, false));
        System.out.println("Used Disk Space:  " + formatBytes(usedDisk, false));
        System.out.println("Free Disk Space:  " + formatBytes(freeDisk, false));
    }

    // Helper method to convert bytes to megabytes (MB) for readability
    private static String formatBytes(long bytes, boolean inMegabyte) {
        if (inMegabyte) {
            return (bytes / (1024 * 1024)) + " MB (" + bytes + " bytes)";
        }
        return (bytes / (1024 * 1024 * 1024 )) + " GB (" + bytes + " bytes)";


    }

}
