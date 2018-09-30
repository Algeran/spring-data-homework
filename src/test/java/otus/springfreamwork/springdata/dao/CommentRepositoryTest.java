package otus.springfreamwork.springdata.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import otus.springfreamwork.springdata.domain.dao.CommentRepository;
import otus.springfreamwork.springdata.domain.model.Author;
import otus.springfreamwork.springdata.domain.model.Book;
import otus.springfreamwork.springdata.domain.model.Comment;
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
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void commentRepositoryShouldInsertEntity() {
        Comment comment = new Comment("user", "so good");
        commentRepository.save(comment);

        TypedQuery<Comment> query = entityManager
                .createQuery(
                        "SELECT c FROM Comment c WHERE c.username = :username AND c.comment = :comment"
                        , Comment.class
                );
        query.setParameter("username", comment.getUsername());
        query.setParameter("comment", comment.getComment());
        Comment commentFromRepo = query.getSingleResult();

        assertEquals(comment, commentFromRepo);
    }

    @Test
    public void commentRepositoryShouldGetCommentById() {
        Comment comment = new Comment("user", "so good");

        entityManager.persist(comment);

        Comment commentFromRepo = commentRepository.findById(comment.getId()).orElse(null);

        assertNotNull(commentFromRepo);
        assertEquals(comment, commentFromRepo);
    }

    @Test
    public void commentRepositoryShouldGetCommentByUsername() {
        Comment comment = new Comment("user", "so good");

        entityManager.persist(comment);

        List<Comment> comments = commentRepository.findAllByUsername(comment.getUsername());

        assertEquals(1, comments.size());
        assertTrue(comments.contains(comment));
    }

    @Test
    public void commentRepositoryShouldGetAllComments() {
        Comment comment = new Comment("user", "so good");
        Comment comment_2 = new Comment("user2", "so bad");

        entityManager.persist(comment);
        entityManager.persist(comment_2);

        List<Comment> comments = commentRepository.findAll();

        assertTrue(comments.contains(comment));
        assertTrue(comments.contains(comment_2));
    }

    @Test
    public void commentRepositoryShouldDeleteCommentById() {
        Comment comment = new Comment("user", "so good");

        entityManager.persist(comment);
        int id = comment.getId();

        commentRepository.deleteById(id);

        Comment commentFromRepo = commentRepository.findById(id).orElse(null);
        assertNull(commentFromRepo);
    }

    @Test
    public void commentRepositoryShouldDeleteCommentByName() {
        Comment comment = new Comment("user", "so good");

        entityManager.persist(comment);
        entityManager.detach(comment);

        commentRepository.deleteByUsername(comment.getUsername());

        List<Comment> comments = commentRepository.findAllByUsername(comment.getUsername());
        assertTrue(comments.isEmpty());
    }

    @Test
    public void commentRepositoryShouldReturnCount_2() {
        Comment comment = new Comment("user", "so good");
        Comment comment_2 = new Comment("user2", "so bad");

        entityManager.persist(comment);
        entityManager.persist(comment_2);
        entityManager.detach(comment);
        entityManager.detach(comment_2);

        long count = commentRepository.count();

        assertEquals(2, count);
    }

    @Test
    public void commentRepositoryShoulReturnCommentsByBookName() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        Comment comment = new Comment("user", "so good");
        comment.setBooks(Collections.singleton(book));

        entityManager.persist(book);
        entityManager.persist(comment);

        List<Comment> comments = commentRepository.getByBookName(book.getName());

        assertFalse(comments.isEmpty());
        assertTrue(comments.contains(comment));
    }
}