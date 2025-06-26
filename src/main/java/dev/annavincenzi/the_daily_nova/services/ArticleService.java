package dev.annavincenzi.the_daily_nova.services;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.annavincenzi.the_daily_nova.dtos.ArticleDto;
import dev.annavincenzi.the_daily_nova.models.Article;
import dev.annavincenzi.the_daily_nova.models.User;
import dev.annavincenzi.the_daily_nova.repositories.ArticleRepository;
import dev.annavincenzi.the_daily_nova.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        ArticleDto dto = modelMapper.map(articleRepository.save(article), ArticleDto.class);

        return dto;
    }

    @Override
    public void delete(Long key) {
        // TODO Auto-generated method stub

    }

    @Override
    public ArticleDto read(Long key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ArticleDto> readAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArticleDto update(Long key, Article model, MultipartFile file) {
        // TODO Auto-generated method stub
        return null;
    }

}
