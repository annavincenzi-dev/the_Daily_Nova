package dev.annavincenzi.the_daily_nova.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.annavincenzi.the_daily_nova.dtos.ArticleDto;
import dev.annavincenzi.the_daily_nova.dtos.CategoryDto;
import dev.annavincenzi.the_daily_nova.models.Article;
import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.repositories.ArticleRepository;
import dev.annavincenzi.the_daily_nova.services.ArticleService;
import dev.annavincenzi.the_daily_nova.services.CrudService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto, Category, Long> categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping
    public String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "Tutti gli articoli");

        List<ArticleDto> articles = new ArrayList<ArticleDto>();

        for (Article article : articleRepository.findByIsAcceptedTrue()) {
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        /* Sort by date (latest to oldest) */
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishedOn).reversed());
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }

    @GetMapping("/create")
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Crea un articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());

        return "article/create";
    }

    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article") Article article,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal,
            MultipartFile[] files,
            Model viewModel) {

        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Crea un articolo");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            return "article/create";
        }

        articleService.create(article, principal, files);

        redirectAttributes.addFlashAttribute("successMessage", "Article created succesfully!");

        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detailArticle(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Article detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "article/detail";
    }

    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Update article");

        viewModel.addAttribute("article", articleService.read(id));

        viewModel.addAttribute("categories", categoryService.readAll());

        return "article/edit";
    }

    @PostMapping("/update/{id}")
    public String articleUpdate(@PathVariable("id") Long id, @Valid @ModelAttribute("article") Article article,
            BindingResult result, RedirectAttributes redirectAttributes, Model viewModel, Principal principal,
            MultipartFile[] files) {

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            viewModel.addAttribute("title", "Update article");

            ArticleDto existingArticle = articleService.read(id);
            article.setImages(existingArticle.getImages());

            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            return "article/edit";
        }

        articleService.update(id, article, files);

        redirectAttributes.addFlashAttribute("successMessage", "Article updated successfully!");

        return "redirect:/writer/dashboard";
    }

    @GetMapping("revisor/detail/{id}")
    public String detailArticleRevisor(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Article detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "revisor/detail";
    }

    @PostMapping("/accept")
    public String articleSetAccepted(@RequestParam("action") String action, @RequestParam("articleId") Long articleId,
            RedirectAttributes redirectAttributes) {
        if (action.equals("accept")) {
            articleService.setIsAccepted(true, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Article accepted!");
        } else if (action.equals("reject")) {
            articleService.setIsAccepted(false, articleId);
            redirectAttributes.addFlashAttribute("resultMessage", "Article rejected!");
        } else {
            redirectAttributes.addFlashAttribute("resultMessage", "Invalid action!");
        }

        return "redirect:/revisor/dashboard";
    }

    @GetMapping("/search")
    public String articleSearch(@Param("keyword") String keyword, Model viewModel) {

        viewModel.addAttribute("title", "Search results");

        List<ArticleDto> articles = articleService.search(keyword);

        List<ArticleDto> acceptedArticles = articles.stream()
                .filter(article -> Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());

        viewModel.addAttribute("articles", acceptedArticles);

        return "article/articles";
    }

    @GetMapping("/delete/{id}")
    public String articleDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        articleService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Article deleted successfully!");

        return "redirect:/writer/dashboard";
    }
}
