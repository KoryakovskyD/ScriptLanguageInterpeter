package ru.javapp.avalon.devj120;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ScriptLanguageInterpreter {
    private final Map<String, String > variables = new HashMap<>();

    public ScriptLanguageInterpreter(File file) throws IOException {
        read(file);

        System.out.println("-------------------------");


        for (Map.Entry<String ,String> kv : variables.entrySet()) {
                System.out.println(kv.getKey() + ' ' + kv.getValue());
            }
    }


    // чтение файла
    private void read(File file) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            int fPrint = 0;
            int fNewLine = 0;
            while ((s = br.readLine()) != null) {
                if (s.isEmpty()) continue;

                String word = "";
                char[] charArray = s.toCharArray();
                Character curChar;
                Character nextChar = null;
                for (int i = 0; i < s.length(); i++) {
                    curChar = charArray[i];

                    if (curChar == null)
                        continue;

                    // если комментарий, то пропускаем строку
                    if (charArray[0] == '#')
                        break;


                    if ((curChar >= 'a' && curChar <= 'z') || (curChar >= 'A' && curChar <= 'Z')) { //|| (curChar >= '0' && curChar <= '9')) {
                        word = word + curChar;
                        if (i!=s.length()-1) continue;
                    }

                    if (curChar == '$') {
                        word = "$";
                        continue;
                    }


                    if ( word != "" && (curChar >= '0' && curChar <= '9')) {
                        word = word + curChar;
                        if (i!=s.length()-1) continue;
                    }

                    if (word.equals("print")) {
                        printString(s.substring(i+1));
                        System.out.println("\n");
                        break;
                    }

                    if (word.equals("set")) {
                        saveSetValue(s.substring(i+1));
                        break;
                    }


                    if (word != "")
                        word = "";


                }
            }
        }
    }

    private void saveSetValue(String s) {
        System.out.println("s=" + s);
        String word = "";
        String  value = "";
        boolean fVal = false;
        char[] charArray = s.toCharArray();
        Character curChar;
        for (int i = 0; i < s.length(); i++) {
            curChar = charArray[i];

            if (fVal) {
                if (Character.isDigit(curChar)) {
                    value = value + curChar;
                    if (i!=s.length()) continue;
                }
            }


            if ((curChar >= 'a' && curChar <= 'z') || (curChar >= 'A' && curChar <= 'Z') || curChar.equals('$') ||
            Character.isDigit(curChar)) {
                word = word + curChar;

            } else {
                fVal = true;
                continue;
            }
        }

        if (!value.equals(""))
            variables.put(word, String.valueOf(Integer.parseInt(value)));
    }

    private void printString(String s) {
        System.out.println("s=" + s);
        String word = "";
        String finalStr = "";
        char[] charArray = s.toCharArray();
        Character curChar;
        int fScob = 0;
        for (int i = 0; i < s.length(); i++) {
            curChar = charArray[i];

            if (curChar.equals('\"')) {
                fScob++;
                if (fScob==1)
                    continue;
            }

            if (fScob == 1) {
                word = word + curChar;
                continue;
            }

            if (fScob == 2) {
                fScob=0;
                finalStr = finalStr + word;
                word="";
                continue;
            }

            if ((curChar >= 'a' && curChar <= 'z') || (curChar >= 'A' && curChar <= 'Z') || curChar.equals('$') ||
                    Character.isDigit(curChar)) {
                word = word + curChar;
                if (i==s.length()-1)
                    finalStr = finalStr + word;
            }

/*
            if (curChar == '+' || curChar == '-' || curChar == '=') {

                if (variables.containsKey(word)) {
                    System.out.print(variables.get(word));
                }
                word="";
            }
 */
        }
        System.out.println(finalStr);
    }
}
