package otus.springfreamwork.springdata.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import otus.springfreamwork.springdata.domain.dao.BookRepository;
import otus.springfreamwork.springdata.domain.model.Author;
import otus.springfreamwork.springdata.domain.model.Book;
import otus.springfreamwork.springdata.domain.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static otus.springfreamwork.springdata.domain.model.Conutry.RUSSIA;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(connection = H2, replace = ANY)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void bookRepositoryShouldInsertEntity() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);
        bookRepository.save(book);

        TypedQuery<Book> query = entityManager
                .createQuery(
                        "SELECT b FROM Book b WHERE b.name = :name"
                        , Book.class
                );
        query.setParameter("name", book.getName());
        Book bookFromRepo = query.getSingleResult();

        assertEquals(book, bookFromRepo);
    }

    @Test
    public void bookRepositoryShouldGetBookById() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);

        Book bookFromRepo = bookRepository.findById(book.getId()).orElse(null);

        assertNotNull(bookFromRepo);
        assertEquals(book, bookFromRepo);
    }

    @Test
    public void bookRepositoryShouldGetBookByName() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);

        Book bookFromRepo = bookRepository.findByName(book.getName()).orElse(null);

        assertNotNull(bookFromRepo);
        assertEquals(book, bookFromRepo);
    }

    @Test
    public void bookRepositoryShouldGetAllBooks() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);
        Book book_2 = new Book("Anna Karenina", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);
        entityManager.persist(book_2);

        List<Book> books = bookRepository.findAll();

        assertTrue(books.contains(book));
        assertTrue(books.contains(book_2));
    }

    @Test
    public void bookRepositoryShouldDeleteBookById() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);
        int id = book.getId();

        bookRepository.deleteById(id);

        Book bookFromRepo = bookRepository.findById(id).orElse(null);
        assertNull(bookFromRepo);
    }

    @Test
    public void bookRepositoryShouldDeleteBookByName() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);
        entityManager.detach(book);

        bookRepository.deleteByName(book.getName());

        Book bookFromRepo = bookRepository.findByName(book.getName()).orElse(null);
        assertNull(bookFromRepo);
    }

    @Test
    public void bookRepositoryShouldReturnCount_2() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);
        Book book_2 = new Book("Anna Karenina", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);
        entityManager.persist(book_2);
        entityManager.detach(book);
        entityManager.detach(book_2);

        long count = bookRepository.count();

        assertEquals(2, count);
    }

    @Test
    public void bookRepositoryShouldGetBookByAuthorId() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);

        List<Book> books = bookRepository.getByAuthorId(author.getId());

        assertTrue(books.contains(book));
    }

    @Test
    public void bookRepositoryShouldGetBookByGenreId() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        entityManager.persist(book);

        List<Book> books = bookRepository.getByGenreId(genre.getId());

        assertEquals(book, books.get(0));
        assertTrue(books.contains(book));
    }
}