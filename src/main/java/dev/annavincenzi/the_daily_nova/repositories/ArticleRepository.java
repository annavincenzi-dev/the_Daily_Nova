package dev.annavincenzi.the_daily_nova.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import dev.annavincenzi.the_daily_nova.models.Article;
import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long> {
    List<Article> findByCategory(Category category);

    List<Article> findByUser(User user);

    List<Article> findByIsAcceptedTrue();

    List<Article> findByIsAcceptedFalse();

    List<Article> findByIsAcceptedIsNull();
}
