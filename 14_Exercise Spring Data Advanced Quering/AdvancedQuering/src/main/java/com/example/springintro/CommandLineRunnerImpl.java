package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

//        printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
//        printAllAuthorsAndNumberOfTheirBooks();
//        printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");


        System.out.println("Please enter an exercise number: ");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> ex1_booksTitlesByAgeRestriction();
            case 2 -> ex2_goldenBooks();
            case 3 -> ex3_booksByPrice();
            case 4 -> ex4_notReleasedBooks();
        }


    }

    private void ex4_notReleasedBooks() throws IOException {
        System.out.println("Please enter released year: ");
        int year = Integer.parseInt(bufferedReader.readLine());

        bookService
                .findNotReleasedBookTitlesInYear(year)
                .forEach(System.out::println);

    }

    private void ex3_booksByPrice() {

        bookService
                .findAllBookTitlesWithPriceLessThan5AndMoreThan40()
                .forEach(System.out::println);
    }

    private void ex2_goldenBooks() {

        bookService
                .findAllGoldBookTitlesWithCopiesLessThan5000()
                .forEach(System.out::println);

    }

    private void ex1_booksTitlesByAgeRestriction() throws IOException {
        System.out.println("Please enter age restriction: ");

        try {
            AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());

            bookService
                    .findAllBookTitlesWithAgeRestriction(ageRestriction)
                    .forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Invalid ageRestriction");
        }

    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
