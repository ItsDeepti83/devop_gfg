package com.example.library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import java.util.List;

public class LibraryTest {

    @Test
    void addBookAndSerialize() {
        Library lib = new Library();
        lib.addBook(new Book("Test-Driven Development", "Kent Beck", 2002));
        List<Book> books = lib.getBooks();
        assertEquals(1, books.size());
        assertEquals("Kent Beck", books.get(0).getAuthor());

        String json = new Gson().toJson(books);
        assertTrue(json.contains("Test-Driven Development"));
    }
}
