package dev.annavincenzi.the_daily_nova.services;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import dev.annavincenzi.the_daily_nova.dtos.ArticleDto;
import dev.annavincenzi.the_daily_nova.models.Article;
import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.models.Image;
import dev.annavincenzi.the_daily_nova.models.User;
import dev.annavincenzi.the_daily_nova.repositories.ArticleRepository;
import dev.annavincenzi.the_daily_nova.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ArticleDto> readAll() {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();

        for (Article article : articleRepository.findAll()) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }

        return dtos;
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile[] files) {
        String url = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        if (article.getPublishedOn() == null) {
            article.setPublishedOn(LocalDate.now());
        }

        article.setIsAccepted(null);

        ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                        url = futureUrl.get();
                        imageService.saveImageOnDB(url, article);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return dto;
    }

    @Override
    public void delete(Long key) {
        if (articleRepository.existsById(key)) {

            Article article = articleRepository.findById(key).get();

            for (Image image : article.getImages()) {
                try {
                    image.setArticle(null);
                    imageService.deleteImage(image.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            articleRepository.deleteById(key);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ArticleDto read(Long key) {
        Optional<Article> optArticle = articleRepository.findById(key);
        if (optArticle.isPresent()) {
            return modelMapper.map(optArticle.get(), ArticleDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id=" + key + " not found");
        }

    }

    public List<ArticleDto> searchByCategory(Category category) {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for (Article article : articleRepository.findByCategory(category)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    public List<ArticleDto> searchByAuthor(User user) {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();

        for (Article article : articleRepository.findByUser(user)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }

        return dtos;
    }

    public void setIsAccepted(Boolean result, Long id) {
        Article article = articleRepository.findById(id).get();
        article.setIsAccepted(result);
        articleRepository.save(article);
    }

    public List<ArticleDto> search(String keyword) {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for (Article article : articleRepository.search(keyword)) {
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }

        return dtos;
    }

    @Override
    public ArticleDto update(Long key, Article updatedArticle, MultipartFile[] files) {
        String url = "";

        Optional<Article> optionalArticle = articleRepository.findById(key);
        if (optionalArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found");
        }

        Article existingArticle = optionalArticle.get();

        updatedArticle.setId(key);
        updatedArticle.setUser(existingArticle.getUser());
        updatedArticle.setPublishedOn(existingArticle.getPublishedOn());

        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            // Elimino immagini esistenti
            for (Image image : existingArticle.getImages()) {
                try {
                    imageService.deleteImage(image.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image");
                }
            }

            // Salvo nuove immagini
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                        url = futureUrl.get();
                        imageService.saveImageOnDB(url, updatedArticle);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed");
                    }
                }
            }
        } else {
            updatedArticle.setImages(existingArticle.getImages());
        }

        updatedArticle.setIsAccepted(null);
        Article saved = articleRepository.save(updatedArticle);
        return modelMapper.map(saved, ArticleDto.class);
    }

}