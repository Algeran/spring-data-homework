package otus.springfreamwork.springdata.domain.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import otus.springfreamwork.springdata.domain.model.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findByName(String name);

    void deleteByName(String name);
}
