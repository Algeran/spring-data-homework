package otus.springfreamwork.springdata.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import otus.springfreamwork.springdata.domain.dao.AuthorRepository;
import otus.springfreamwork.springdata.domain.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.jdbc.EmbeddedDatabaseConnection.H2;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static otus.springfreamwork.springdata.domain.model.Conutry.RUSSIA;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(connection = H2, replace = ANY)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void authorRepositoryShouldInsertEntity() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        authorRepository.save(author);

        TypedQuery<Author> query = entityManager
                .createQuery(
                        "SELECT a FROM Author a WHERE a.name = :name AND a.surname = :surname"
                        , Author.class
                );
        query.setParameter("name", "Leo");
        query.setParameter("surname", "Tolstoy");
        Author authorFromRepo = query.getSingleResult();

        assertEquals(author, authorFromRepo);
    }

    @Test
    public void authorRepositoryShouldGetAuthorById() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        entityManager.persist(author);
        entityManager.detach(author);

        Author authorFromRepo = authorRepository.findById(1).orElse(null);

        assertNotNull(authorFromRepo);
        assertEquals(author, authorFromRepo);
    }

    @Test
    public void authorRepositoryShouldGetAuthorByNameAndSurname() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        entityManager.persist(author);
        entityManager.detach(author);

        Author authorFromRepo = authorRepository.findByNameAndSurname("Leo", "Tolstoy").orElse(null);

        assertNotNull(authorFromRepo);
        assertEquals(author, authorFromRepo);
    }

    @Test
    public void authorRepositoryShouldGetAllAuthors() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Author author_2 = new Author("Fyodor", "Dostoevsky", RUSSIA);

        entityManager.persist(author);
        entityManager.persist(author_2);
        entityManager.detach(author);
        entityManager.detach(author_2);

        List<Author> authors = authorRepository.findAll();

        assertTrue(authors.contains(author));
        assertTrue(authors.contains(author_2));
    }

    @Test
    public void authorRepositoryShouldDeleteAuthorById() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        entityManager.persist(author);
        int id = author.getId();

        authorRepository.deleteById(id);

        Author authorFromRepo = authorRepository.findById(id).orElse(null);
        assertNull(authorFromRepo);
    }

    @Test
    public void authorRepositoryShouldDeleteAuthorByNameAndSurname() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        entityManager.persist(author);
        entityManager.detach(author);

        authorRepository.deleteByNameAndSurname("Leo", "Tolstoy");

        Author authorFromRepo = authorRepository.findByNameAndSurname("Leo", "Tolstoy").orElse(null);
        assertNull(authorFromRepo);
    }

    @Test
    public void authorRepositoryShouldReturnCount_2() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Author author_2 = new Author("Fyodor", "Dostoevsky", RUSSIA);

        entityManager.persist(author);
        entityManager.persist(author_2);
        entityManager.detach(author);
        entityManager.detach(author_2);

        long count = authorRepository.count();

        assertEquals(2, count);
    }
}