package otus.springfreamwork.springdata.domain.application.servicies;

public interface GenreService {

    String createGenreByName(String name);

    String getAllGenres();

    String countGenres();

    String deleteGenre(String name);

    String getGenre(String name);
}
