package otus.springfreamwork.springdata.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import otus.springfreamwork.springdata.com.application.servicies.CommentServiceImpl;
import otus.springfreamwork.springdata.domain.application.servicies.CommentService;
import otus.springfreamwork.springdata.domain.dao.BookRepository;
import otus.springfreamwork.springdata.domain.dao.CommentRepository;
import otus.springfreamwork.springdata.domain.model.Author;
import otus.springfreamwork.springdata.domain.model.Book;
import otus.springfreamwork.springdata.domain.model.Comment;
import otus.springfreamwork.springdata.domain.model.Genre;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static otus.springfreamwork.springdata.domain.model.Conutry.RUSSIA;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookRepository bookRepository;

    @Before
    public void init() {
        commentService = new CommentServiceImpl(commentRepository, bookRepository);
    }

    @Test
    public void commentServiceShouldCreateCommentByNameAndSurusername() {
        String username = "user";
        String commentText = "so good";
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Book book = new Book("War And Piece", new Date(2018, 4, 10), parts, Collections.singleton(author), genre);
        Comment comment = new Comment(username, commentText);
        comment.setBooks(Collections.singleton(book));
        when(bookRepository.findByName(eq(book.getName()))).thenReturn(Optional.of(book));

        String result = commentService.createComment(comment.getUsername(), comment.getComment(), book.getName());

        assertEquals("Комментарий создан", result);
        verify(commentRepository, times(1)).save(eq(comment));
        verify(bookRepository, times(1)).findByName(eq(book.getName()));
    }

    @Test
    public void commentServiceShouldNotCreateCommentCauseNoBookInDB() {
        when(bookRepository.findByName(anyString())).thenReturn(Optional.empty());

        String result = commentService.createComment("username", "comment text", "War And Piece");

        assertEquals("Не найдено книги, комментарий не создан", result);
        verify(commentRepository, never()).save(any());
        verify(bookRepository, times(1)).findByName(eq("War And Piece"));
    }

    @Test
    public void commentServiceShouldReturnListOfComments() {
        Comment comment = new Comment("usern", "so good");
        Comment comment_2 = new Comment("Fedor", "Dostoevsky");
        List<Comment> comments = Arrays.asList(comment, comment_2);
        when(commentRepository.findAll()).thenReturn(comments);
        String expected = "Список комментариев:\n" + comment + "\n" + comment_2;

        String result = commentService.getAllComments();

        assertEquals(expected, result);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void commentServiceShouldReturnWarningCauseNoCommentsInDB() {
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());
        String expected = "Нет комментариев в базе";

        String result = commentService.getAllComments();

        assertEquals(expected, result);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void commentServiceShouldReturnCountMessage() {
        when(commentRepository.count()).thenReturn(2L);
        String expected = "Количество комментариев в базе: 2";

        String result = commentService.countComments();

        assertEquals(expected, result);
        verify(commentRepository, times(1)).count();
    }

    @Test
    public void commentRepositoryShouldReturnWarningCauseNoCommentInDBForDelete() {
        when(commentRepository.findAllByUsername(anyString())).thenReturn(Collections.emptyList());
        String expected = "Не найдено комментариев для удаления";

        String result = commentService.deleteUsernameComments("username");

        assertEquals(expected, result);
        verify(commentRepository, times(1)).findAllByUsername(eq("username"));
        verify(commentRepository, never()).deleteByUsername(anyString());
    }

    @Test
    public void commentRepositoryShouldReturnCommentByUsernameMessage() {
        String username = "usern";
        String commentText = "so good";
        Comment comment = new Comment(username, commentText);
        when(commentRepository.findAllByUsername(eq(username))).thenReturn(Collections.singletonList(comment));
        String expected = "Список комментариев юзера:\n" + comment;

        String result = commentService.getUsernameComments(username);

        assertEquals(expected, result);
        verify(commentRepository, times(1)).findAllByUsername(eq(comment.getUsername()));
    }

    @Test
    public void commentRepositoryShouldReturnWarningCauseNoCommentFound() {
        when(commentRepository.findAllByUsername(anyString())).thenReturn(Collections.emptyList());
        String expected = "Не найдено комментариев юзера";

        String result = commentService.getUsernameComments("usern");

        assertEquals(expected, result);
        verify(commentRepository, times(1)).findAllByUsername(eq("usern"));
    }

    @Test
    public void commentRepositoryShouldDeleteCommentByUsername() {
        String username = "usern";
        String commentText = "so good";
        Comment comment = new Comment(username, commentText);
        when(commentRepository.findAllByUsername(eq(username))).thenReturn(Collections.singletonList(comment));
        String expected = "Комментарии успешно удалены";

        String result = commentService.deleteUsernameComments(username);

        assertEquals(expected, result);
        verify(commentRepository, times(1)).findAllByUsername(eq(username));
        verify(commentRepository, times(1)).deleteByUsername(eq(username));
    }
}
