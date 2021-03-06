package com.sunshines.bookstore.Controllers;

import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.Discount;
import com.sunshines.bookstore.Model.Genre;
import com.sunshines.bookstore.Repository.BookRepository;
import com.sunshines.bookstore.Repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public GenreRepository genreRepository;

    @GetMapping("/")
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books != null)
            for (Book book : books)
                book.setBestDiscount();
        return books;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Book addBook(@RequestBody @Valid Book book) {
        this.bookRepository.save(book);
        return book;
    }

    @GetMapping("/genre/{id}")
    public List<Book> getBooksByGenre(@PathVariable int id) {
        Genre genre = genreRepository.findById(id);
        List<Book> books = bookRepository.findByGenre(genre);
        if (books != null)
            for (Book book : books)
                book.setBestDiscount();
        return books;
    }

    @GetMapping("/genre")
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/genre")
    public List<Genre> addGenre(@RequestBody @Valid Genre genre) {
        genreRepository.save(genre);
        return getAllGenres();
    }


    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        Book book = bookRepository.findFirstById(id);
        book.setBestDiscount();
        return book;
    }

    @PostMapping("/discount/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Book addDiscount(@RequestBody @Valid Discount discount, @PathVariable("id") int id) {
        Book book = this.bookRepository.findFirstById(id);
        discount.setBook(book);
        return book;
    }
}
