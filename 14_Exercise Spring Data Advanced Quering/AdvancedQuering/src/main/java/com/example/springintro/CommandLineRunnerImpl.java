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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
            case 5 -> ex5_booksReleasedBeforeDate();
            case 6 -> ex6_authorsSearch();
            case 7 -> ex7_booksSearch();
            case 8 -> ex8_booksTitlesSearch();
            case 9 -> ex9_countBooks();
            case 10 -> ex10_totalBooksCopies();
            case 11 -> ex11_reducedBook();
            case 12 -> ex12_increaseBookCopies();
            case 99 -> test();
        }


    }

    private void test() {
        bookService
                .changePrice(1L);
    }

    private void ex12_increaseBookCopies() throws IOException {
        System.out.println("Please enter a date: ");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd MMM yyyy").withLocale(Locale.ENGLISH));

        System.out.println("Please enter the number of copies: ");
        int numberOfBookCopies = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService
                .increaseCopiesAfterReleaseDate(localDate, numberOfBookCopies));
    }

    private void ex11_reducedBook() throws IOException {
        System.out.println("Please enter the title of the book: ");
        String title = bufferedReader.readLine().trim();

        bookService
                .findAllBooksByTitle(title)
                .forEach(System.out::println);
    }

    private void ex10_totalBooksCopies() {
        authorService
                .findAllAuthorsBooksByTotalCopies()
                .forEach(System.out::println);

    }

    private void ex9_countBooks() throws IOException {
        System.out.println("Please enter a number: ");
        int number = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService
                .findCountOfBooksWithTitleLengthLongerThan(number));
    }

    private void ex8_booksTitlesSearch() throws IOException {
        System.out.println("Please enter the pattern: ");
        String pattern = bufferedReader.readLine();

        bookService
                .findAllBooksWrittenByAuthorWithLastName(pattern)
                .forEach(System.out::println);
    }

    private void ex7_booksSearch() throws IOException {
        System.out.println("Please enter the pattern: ");
        String pattern = bufferedReader.readLine();

        bookService
                .finAllBooksWhichContainAPattern(pattern)
                .forEach(System.out::println);
    }

    private void ex6_authorsSearch() throws IOException {
        System.out.println("Please enter the pattern: ");
        String pattern = bufferedReader.readLine();

        authorService
                .findAllAuthorsWhoseFirstNameEndsWith(pattern)
                .forEach(System.out::println);

    }

    private void ex5_booksReleasedBeforeDate() throws IOException {
        System.out.println("Please enter date: ");

        try {
            LocalDate date = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            bookService
                    .findBookBeforeDate(date)
                    .forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Invalid type of date!");
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
