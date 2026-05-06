package ru.hse.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String FILE_NAME = "application.log";

    public static void info(String message) {
        write("INFO", message);
    }

    public static void warning(String message) {
        write("WARNING", message);
    }

    public static void error(String message) {
        write("ERROR", message);
    }

    private static void write(String level, String message) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, true);

            String time = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );

            writer.write("[" + time + "] " + level + ": " + message + "\n");

            writer.close();

        } catch (IOException e) {
            System.out.println("Ошибка записи в лог");
        }
    }
}