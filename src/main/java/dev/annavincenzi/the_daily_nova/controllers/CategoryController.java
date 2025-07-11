package dev.annavincenzi.the_daily_nova.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.annavincenzi.the_daily_nova.dtos.ArticleDto;
import dev.annavincenzi.the_daily_nova.dtos.CategoryDto;
import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.services.ArticleService;
import dev.annavincenzi.the_daily_nova.services.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/search/{id}")
    public String categorySearch(@PathVariable("id") Long id, Model viewModel) {
        CategoryDto category = categoryService.read(id);

        viewModel.addAttribute("title", "All articles in " + category.getName() + " category");

        List<ArticleDto> articles = articleService.searchByCategory(modelMapper.map(category, Category.class));

        List<ArticleDto> acceptedArticles = articles.stream()
                .filter(article -> Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());

        viewModel.addAttribute("articles", acceptedArticles);
        viewModel.addAttribute("selectedCategoryId", id);

        return "article/articles";
    }

    @PostMapping("save")
    public String categoryStore(@Valid @ModelAttribute("category") Category category, BindingResult result,
            RedirectAttributes redirectAttributes, Model viewModel) {

        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Crea una categoria");
            viewModel.addAttribute("category", category);
            return "category/create";
        }

        categoryService.create(category, null, null);
        redirectAttributes.addFlashAttribute("successMessage", "Category created successfully!");

        return "redirect:/admin/dashboard";
    }

    @PostMapping("update/{id}")
    public String categoryUpdate(@PathVariable("id") Long id, @Valid @ModelAttribute("category") Category category,
            BindingResult result, RedirectAttributes redirectAttributes, Model viewModel) {

        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Edit category");
            viewModel.addAttribute("category", category);
            return "category/update";
        }

        categoryService.update(id, category, null);
        redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");

        return "redirect:/admin/dashboard";

    }

    @GetMapping("delete/{id}")
    public String categoryDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");

        return "redirect:/admin/dashboard";

    }

}
