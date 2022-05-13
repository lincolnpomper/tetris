package com.lincolnpomper.tetris.util;

public class Logger {

    private static boolean systemConsole = true;

    public static void info(String message) {
        if (systemConsole) {
            System.out.println(message);
        }
    }

    public static void error(String message) {
        if (systemConsole) {
            System.err.println(message);
        }
    }
}
