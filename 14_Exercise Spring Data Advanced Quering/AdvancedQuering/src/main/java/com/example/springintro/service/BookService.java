package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldBookTitlesWithCopiesLessThan5000();

    List<String> findAllBookTitlesWithPriceLessThan5AndMoreThan40();

    List<String> findNotReleasedBookTitlesInYear(int year);

    List<String> findBookBeforeDate(LocalDate date);

    List<String> finAllBooksWhichContainAPattern(String pattern);

    List<String> findAllBooksWrittenByAuthorWithLastName(String pattern);

    int findCountOfBooksWithTitleLengthLongerThan(int number);

    List<String> findAllBooksByTitle(String title);

    void changePrice(long bookId);
}
