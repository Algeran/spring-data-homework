package otus.springfreamwork.springdata.domain.application.servicies;

public interface CommentService {
    String createComment(String username, String commentText, String bookName);

    String getAllComments();

    String countComments();

    String deleteUsernameComments(String usernanme);

    String getUsernameComments(String username);
}
