package dev.annavincenzi.the_daily_nova.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import dev.annavincenzi.the_daily_nova.models.Article;
import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long> {

    List<Article> findByCategory(Category category);

    List<Article> findByUser(User user);

    List<Article> findByIsAcceptedTrue();

    List<Article> findByIsAcceptedFalse();

    List<Article> findByIsAcceptedIsNull();

    @Query("SELECT a FROM Article a WHERE " + "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(a.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(a.body) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(CAST(a.publishedOn AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")

    List<Article> search(@Param("searchTerm") String searchTerm);
}
