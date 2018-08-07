package otus.springfreamwork.springdata.domain.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import otus.springfreamwork.springdata.domain.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByUsername(String username);

    void deleteByUsername(String username);

    @Query("SELECT c FROM Comment c LEFT JOIN c.books b WHERE b.name = :name")
    List<Comment> getByBookName(@Param("name") String bookName);

}
