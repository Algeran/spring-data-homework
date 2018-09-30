package otus.springfreamwork.springdata.domain.application.servicies;

public interface AuthorService {

    String createAuthorByNameAndSurname(String name, String surname);

    String getAllAuthors();

    String countAuthors();

    String getAuthor(String name, String surname);

    String deleteAuthor(String name, String surname);
}
