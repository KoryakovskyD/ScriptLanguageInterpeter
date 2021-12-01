package ru.javapp.avalon.devj120;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ScriptLanguageInterpreter {
    private final Map<String, Integer > variables = new HashMap<>();

    public ScriptLanguageInterpreter(File file) throws IOException {
        read(file);
        for (Map.Entry<String ,Integer> kv : variables.entrySet()) {
                System.out.println(kv.getKey() + '=' + kv.getValue());
            }
    }

    // чтение файла
    private void read(File file) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            int fPrint = 0;
            while ((s = br.readLine()) != null) {
                if (s.isEmpty()) continue;

                String word = "";
                char[] charArray = s.toCharArray();
                Character curChar;
                Character nextChar = null;
                for (int i = 0; i < s.length(); i++) {
                    curChar = charArray[i];

                    if (curChar.equals(""))
                        continue;

                    // если комментарий, то пропускаем строку
                    if (charArray[0] == '#')
                        break;

                    if (curChar.equals("\""))
                        fPrint++;


                    if (word.equals("print") ) {
                        word = word + curChar;
                        if (fPrint==2)
                            fPrint=0;
                        System.out.println(word);
                    }
                    if (word.equals("print"))
                        continue;


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


                    if (word != "") {
                        if (variables.containsKey(word.toLowerCase(Locale.ROOT))) {
                            int savVal = variables.get(word.toLowerCase(Locale.ROOT));
                            variables.replace(word.toLowerCase(Locale.ROOT), savVal, savVal+1);
                            word="";
                            continue;
                        } else {
                            variables.put(word.toLowerCase(), 1);
                            word = "";
                        }
                    }
                }
            }
        }
    }
}
