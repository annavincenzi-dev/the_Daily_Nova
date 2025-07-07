package dev.annavincenzi.the_daily_nova.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import dev.annavincenzi.the_daily_nova.dtos.CategoryDto;
import dev.annavincenzi.the_daily_nova.models.Article;
import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CrudService<CategoryDto, Category, Long> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto create(Category model, Principal principal, MultipartFile[] file) {
        return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
    }

    @Override
    @Transactional
    public void delete(Long key) {
        if (categoryRepository.existsById(key)) {
            Category category = categoryRepository.findById(key).get();

            if (category.getArticles() != null) {
                Iterable<Article> articles = category.getArticles();
                for (Article article : articles) {
                    article.setCategory(null);
                }
            }

            categoryRepository.deleteById(key);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public CategoryDto read(Long key) {
        return modelMapper.map(categoryRepository.findById(key), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> readAll() {
        List<CategoryDto> dtos = new ArrayList<CategoryDto>();
        for (Category category : categoryRepository.findAll()) {
            dtos.add(modelMapper.map(category, CategoryDto.class));
        }

        return dtos;
    }

    @Override
    public CategoryDto update(Long key, Category model, MultipartFile file) {
        if (categoryRepository.existsById(key)) {
            model.setId(key);
            return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

}
