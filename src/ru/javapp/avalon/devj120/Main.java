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

        for (String fileName: args) {
            ScriptLanguageInterpreter sli = new ScriptLanguageInterpreter(new File(args[0]));
        }
    }

    private static void help() {
        System.out.println("\nThe program is designed to calculate the frequency of using words in the text.\n" +
                "Input parameters: file names\n" +
                "Examples: FrequencyDictionary text1 text2 text3\n");
        System.exit(0);
    }
}