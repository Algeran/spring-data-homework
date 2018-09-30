package otus.springfreamwork.springdata.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import otus.springfreamwork.springdata.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByName(String name);

    void deleteByName(String name);

    @Query("SELECT b FROM Book b LEFT JOIN b.authors a WHERE a.id = :id")
    List<Book> getByAuthorId(@Param("id") int authorId);

    @Query("SELECT b FROM Book b WHERE b.genre.id = :id")
    List<Book> getByGenreId(@Param("id") int genreId);
}
