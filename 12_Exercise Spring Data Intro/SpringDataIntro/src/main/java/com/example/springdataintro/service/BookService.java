package com.example.springdataintro.service;

import com.example.springdataintro.model.entity.Book;

import java.io.IOException;
import java.time.Year;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorName(String firstName, String lastName);
}
