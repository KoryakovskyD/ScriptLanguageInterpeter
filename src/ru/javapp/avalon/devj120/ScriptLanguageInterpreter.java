package ru.javapp.avalon.devj120;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ScriptLanguageInterpreter {
    private final Map<String, String > variables = new HashMap<>();

    public ScriptLanguageInterpreter(File file) throws IOException {
        read(file);

        System.out.println("-----------------");
        // массив с переменными и их значениями
        for (Map.Entry<String ,String> kv : variables.entrySet()) {
                System.out.println(kv.getKey() + ' ' + kv.getValue());
            }
    }

    // чтение файла
    private void read(File file) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.isEmpty()) continue;

                //StringBuilder word = new StringBuilder();
                String  word="";
                char[] charArray = s.toCharArray();
                Character curChar;
                for (int i = 0; i < s.length(); i++) {
                    curChar = charArray[i];

                    if (curChar == null)
                        continue;

                    // если комментарий, то пропускаем строку
                    if (charArray[0] == '#')
                        break;

                    if (word.equals("print")) {
                        printString(s.substring(i + 1));
                        break;
                    }

                    if (word.equals("set")) {
                        saveSetValue(s.substring(i + 1));
                        break;
                    }

                    if (word.equals("input")) {
                        inputValue(s.substring(i + 1));
                        break;
                    }

                    // сформируем ключевое слово
                    if (Character.isLetter(curChar)) {
                        word = word + curChar;
                        if (i != s.length() - 1) continue;
                    }
                }
            }
        }
    }


    // ключевое слово set
    private void saveSetValue(String s) {
        String wordName = "",
               word= "",
               value = "",
               finalStr = "";
        boolean fVal = false;
        char[] charArray = s.toCharArray();
        Character curChar;
        for (int i = 0; i < s.length(); i++) {
            curChar = charArray[i];

            // формируем значение переменной
            if (fVal) {

                if (curChar.equals('='))
                    continue;

                if (curChar.equals('$') || Character.isLetter(curChar) || Character.isDigit(curChar)) {
                    word = word + curChar;
                    if (!word.contains("$"))
                        value = word;
                    if (i!=s.length()) continue;
                }

                if (Character.isWhitespace(curChar)) {
                    if (word.equals(""))
                        continue;

                    if (!word.contains("$")) {
                        finalStr = finalStr + word;
                        value = word;
                        word = "";
                        continue;
                    }

                    if (variables.containsKey(word))
                        finalStr = finalStr + variables.get(word);
                    else {
                        System.out.println("Unknown variable " + word + "!!!");
                        System.exit(1);
                    }
                    if (!word.contains("$"))
                        value = word;
                    word = "";
                    continue;
                }

                if (curChar.equals('-') || curChar.equals('+'))
                    finalStr = finalStr + curChar;


                if (Character.isDigit(curChar)) {
                    value = value + curChar;
                    if (i!=s.length()) continue;
                }
            }

            // формируем название переменной
            if (Character.isLetter(curChar) || curChar.equals('$') ||
            Character.isDigit(curChar)) {
                wordName = wordName + curChar;
            } else {
                fVal = true;
                continue;
            }
        }

        finalStr = finalStr + value;
        value = calculate(finalStr).toString();

        // полжим в коллекцию переменную со значением
        if (!value.equals(""))
            variables.put(wordName, String.valueOf(Integer.parseInt(value)));
    }

    // переделать
    // посчитать значение из строки
    private Integer calculate(String finalStr) {
        int rez = 0;
        String word = "";

        List<Integer> listAll = new ArrayList<>();
        List<Character> znaki = new ArrayList<>();
        char[] charArray = finalStr.toCharArray();
        Character curChar = null;
        for (int i = 0; i < finalStr.length(); i++) {
            curChar = charArray[i];

            if (Character.isDigit(curChar)) {
                word = word + curChar;
                if (i != finalStr.length()) continue;
            }

            if (curChar.equals('-') || curChar.equals('+')) {
                znaki.add(curChar);
                listAll.add(Integer.parseInt(word));
                word="";
            }
        }

        if (znaki.isEmpty()) {
            return  Integer.parseInt(word);
        }

        for (Character znak : znaki) {
            if (znak.equals('+')){
                rez = listAll.get(0) +listAll.get(1);
            }

            if (znak.equals('-')){
                rez = rez - Integer.parseInt(word);
            }
        }

        return rez;
    }

    // ключевое слово print
    private void printString(String s) {
        String word = "";
        String finalStr = "";
        char[] charArray = s.toCharArray();
        Character curChar;
        int fScob = 0;
        for (int i = 0; i < s.length(); i++) {
            curChar = charArray[i];

            if (i==s.length()-1) {
                if (!curChar.equals('"'))
                    word = word + curChar;
                if (variables.containsKey(word))
                    System.out.print(variables.get(word));
                else
                    System.out.print(word);
                break;
            }

            // если первая скобка, то печатаем слово и ставим флаг
            if (curChar.equals('\"')) {
                if (!Character.isWhitespace(curChar)) {
                    if (variables.containsKey(word))
                        System.out.print(variables.get(word));
                    else
                        System.out.print(word);
                    word="";
                }
                fScob++;
                if (fScob==1)
                    continue;
            }

            // создаем слово внутри скобок
            if (fScob == 1) {
                word = word + curChar;
                continue;
            }

            // формируем финальную строку, выходим из скобок
            if (fScob == 2) {
                fScob=0;
                finalStr = finalStr + word;

                word="";
                continue;
            }

            // формируем текущее слово
            if (Character.isLetter(curChar) || curChar.equals('$') || Character.isDigit(curChar)) {
                word = word + curChar;
                }
            }
        // печать резузьтата
        System.out.println(finalStr);
    }

    // ключевое слово input
    private void inputValue(String s) {
        System.out.println(s);
        StringBuilder word = new StringBuilder();
        int flag=0;
        int newValue = 0;
        char[] charArray = s.toCharArray();
        Character curChar;
        for (int i = 0; i < s.length(); i++) {
            curChar = charArray[i];

            if (curChar.equals('\"')) {
                flag++;
                continue;
            }

            if (flag == 1) {
                word.append(curChar);
            }

            if (flag == 2) {
                flag=0;
                System.out.print(word);
                newValue= readToConsole();
                word.delete(0, word.length());
                continue;
            }




            if (flag == 0 && (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar.equals('$'))) {
                word.append(curChar);
                if (i==s.length()-1) {
                    variables.put(word.toString(), String.valueOf(newValue));
                }
            }
        }
    }


    private Integer readToConsole() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            return Integer.parseInt(scanner.next());
        }
        return 0;
    }
}