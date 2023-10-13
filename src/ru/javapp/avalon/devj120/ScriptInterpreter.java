package ru.javapp.avalon.devj120;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptInterpreter {

    private Map<String, String> variables;

    public ScriptInterpreter() {
        variables = new HashMap<>();
    }

    public void interpret(String script) {
        String[] lines = script.split("\n");
        for (String line : lines) {
            line = line.trim();

            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split(" ", 2);
            String command = parts[0];

            switch (command) {
                case "print":
                    if (parts.length < 2) {
                        System.out.println("Usage: print <message>");
                    } else {
                        String message = parts[1];
                        message = substituteVariables(message);
                        System.out.println(message);
                    }
                    break;
                case "set":
                    if (parts.length < 2) {
                        System.out.println("Usage: set <variable> <value>");
                    } else {
                        String[] setParts = parts[1].split(" ", 2);
                        if (setParts.length == 2) {
                            String variable = setParts[0];
                            String value = setParts[1];
                            variables.put(variable, value);
                            System.out.println("Variable " + variable + " set to: " + value);
                        } else {
                            System.out.println("Usage: set <variable> <value>");
                        }
                    }
                    break;
                case "cat":
                    if (parts.length < 2) {
                        System.out.println("Usage: cat <filename>");
                    } else {
                        String filename = parts[1];
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(filename));
                            String lineRead;
                            while ((lineRead = reader.readLine()) != null) {
                                System.out.println(lineRead);
                            }
                            reader.close();
                        } catch (IOException e) {
                            System.out.println("Error reading file: " + e.getMessage());
                        }
                    }
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        }
    }

    private String substituteVariables(String message) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String variable = "$" + entry.getKey();
            String value = entry.getValue();
            message = message.replace(variable, value);
        }
        return message;
    }

    public static void main(String[] args) {
        String script =  "set name Vasia\n" +
                "print Hello, $name!\n" +
                "// This is a comment\n" +
                "set name Petya\n" +
                "# some comment\n" +
                "print Goodbye, $name!\n" +
                "cat text.txt";

        ScriptInterpreter interpreter = new ScriptInterpreter();
        interpreter.interpret(script);
    }
}