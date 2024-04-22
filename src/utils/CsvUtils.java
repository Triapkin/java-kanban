package utils;

import enums.Status;
import enums.TaskType;
import exceptions.ManagerSaveException;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CsvUtils {

    private static final String DEFAULT_FILE_NAME = "src/resources/test_data.csv";

    public static Task fromString(String value) {
        String[] lines = value.split(",");
        String type = lines[1];
        switch (TaskType.valueOf(type)) {
            case EPIC:
                return new Epic(
                        Integer.parseInt(lines[0]), lines[2], lines[4], Status.valueOf(lines[3]), TaskType.valueOf(lines[1]), LocalDateTime.parse(lines[5])
                );
            case TASK:
                return new Task(
                        Integer.parseInt(lines[0]), lines[2], lines[4], Status.valueOf(lines[3]), TaskType.valueOf(lines[1]), Long.parseLong(lines[7]), LocalDateTime.parse(lines[5])
                );
            case SUBTASK:
                return new Subtask(
                        Integer.parseInt(lines[0]), lines[2], lines[4], Status.valueOf(lines[3]), TaskType.valueOf(lines[1]), Integer.parseInt(lines[8]), Long.parseLong(lines[7]), LocalDateTime.parse(lines[5])
                );
            default:
                return null;
        }
    }

    public static File createIfFileNotExist() {
        File file = new File(DEFAULT_FILE_NAME);
        file.getParentFile().mkdirs();
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Не удалось создать файл.");
        }
        return file;
    }
}
