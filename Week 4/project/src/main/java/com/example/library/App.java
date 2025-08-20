package com.example.library;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Simple app to demonstrate Maven features.
 * - Uses standard Maven layout (src/main/java, resources, tests)
 * - Depends on Gson (declared in pom.xml)
 * - Can be packaged into an executable JAR via maven-shade-plugin
 */
public class App {
    public static void main(String[] args) throws IOException {
        // Load a resource from src/main/resources to show standard layout
        Properties props = new Properties();
        try (InputStream in = App.class.getResourceAsStream("/config.properties")) {
            props.load(in);
        }

        String env = props.getProperty("env");
        Library lib = new Library();
        lib.addBook(new Book("Effective Java", "Joshua Bloch", 2018));
        lib.addBook(new Book("Clean Code", "Robert C. Martin", 2008));

        // Use Gson dependency
        Gson gson = new Gson();
        String json = gson.toJson(lib.getBooks());

        System.out.println("Environment: " + env);
        System.out.println("Books JSON: " + json);
    }
}
