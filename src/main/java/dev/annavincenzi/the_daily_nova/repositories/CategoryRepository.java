package dev.annavincenzi.the_daily_nova.repositories;

import org.springframework.data.repository.ListCrudRepository;

import dev.annavincenzi.the_daily_nova.models.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Long> {

}
