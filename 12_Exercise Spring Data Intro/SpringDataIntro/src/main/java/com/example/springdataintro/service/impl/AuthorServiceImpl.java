package com.example.springdataintro.service.impl;

import com.example.springdataintro.model.entity.Author;
import com.example.springdataintro.repository.AuthorRepository;
import com.example.springdataintro.service.AuthorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final String AUTHOR_FILE_PATH = "src/main/resources/files/authors.txt";
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (authorRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(AUTHOR_FILE_PATH))
                .stream()
                .forEach(row -> {
                    String[] fullName = row.split("\\s+");
                    String firstName = fullName[0];
                    String lastName = fullName[1];
                    Author author = new Author(firstName, lastName);

                    authorRepository.save(author);
                });

    }

    @Override
    public Author getRandomAuthor() {

        Long randomId = ThreadLocalRandom
                .current()
                .nextLong(1, authorRepository.count() + 1);

        Author author = authorRepository.findById(randomId).orElse(null);
        return author;
    }

    @Override
    public List<String> getAllAuthorsOrderByCountOfTheirBooks() {

        return authorRepository
                .findAllByBooksSizeDesc()
                .stream()
                .map(author -> String.format("%s %s %d",
                        author.getFirstName(), author.getLastName(), author.getBooks().size()))
                .collect(Collectors.toList());
    }
}
