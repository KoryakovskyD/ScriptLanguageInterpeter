package ru.javapp.avalon.devj120;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        // проверка на вызов программы без параметров
        if (args.length == 0) {
            help();
        }

        File file = new File(args[0]);
        if (!file.isFile()) {
            System.out.println("File \"" + args[0] + "\" isn't exist or can't read.");
            System.exit(1);
        }
        new ScriptLanguageInterpreter(new File(args[0]));
    }

    private static void help() {
        System.out.println("\nThe program is designed to read abstract script language.\n" +
                "Input parameters: file name\n" +
                "Examples: ScriptLanguageInterpreter file\n");
        System.exit(0);
    }

    public static void systemExit(String str) {
        System.out.println(str);
        System.exit(1);
    }
}