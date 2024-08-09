package dev.brianmiller.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Properties {

    private static final String DEFAULT_PROPERTY_FILE_NAME = "properties.txt";
    private static String propertyFileName = DEFAULT_PROPERTY_FILE_NAME;

    private static final Map<String, String> properties = new HashMap<>();

    private Properties() {
    }

    private static void readPropertiesFromFile(String fileName) {
        if ((fileName == null) || (fileName.isBlank())) {
            throw new IllegalArgumentException("Properties file name is null or empty string");
        }
        propertyFileName = fileName;
        Path path = Path.of(propertyFileName);
        if (Files.exists(path)) {
            if (Files.isRegularFile(path)) {
                try {
                    List<String> lines = Files.readAllLines(path);
                    for (String line : lines) {
                        String separator = " ";
                        if (line.contains("=")) {
                            separator = "=";
                        } else if (line.contains(":")) {
                            separator = ":";
                        }
                        String[] parts = line.split(separator);
                        if ((parts.length == 2) &&
                                !(parts[0].isBlank()) &&
                                !(parts[1].isBlank())) {
                            properties.put(parts[0].toLowerCase().trim(),
                                    parts[1].trim());
                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(
                        new IOException("Not a regular file: " + fileName));
            }
        } else {
            throw new RuntimeException(
                    new FileNotFoundException("File does not exist: " + fileName));
        }
    }

    public static String getValue(String propertyFileName, String property) {
        if (property == null) {
            throw new IllegalArgumentException("property name must not be null");
        }
        if (properties.isEmpty()) {
            readPropertiesFromFile(propertyFileName);
            if (properties.isEmpty()) {
                System.err.println("Error: No property values found!");
                return null; // TODO: throw exception ???
            }
        }
        String key = property.toLowerCase().trim();
        if (properties.containsKey(key)) {
            return properties.get(key);
        } else {
            System.err.println("No property found: " + property);
        }
        return null;
    }

    public static String getValue(String property) {
        return getValue(DEFAULT_PROPERTY_FILE_NAME, property);
    }

}
