package otus.springfreamwork.springdata.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import otus.springfreamwork.springdata.domain.model.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByNameAndSurname(String name, String surname);

    void deleteByNameAndSurname(String name, String surname);
}
