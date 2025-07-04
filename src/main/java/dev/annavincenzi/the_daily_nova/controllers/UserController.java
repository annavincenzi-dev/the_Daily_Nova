package dev.annavincenzi.the_daily_nova.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.annavincenzi.the_daily_nova.dtos.ArticleDto;
import dev.annavincenzi.the_daily_nova.dtos.UserDto;
import dev.annavincenzi.the_daily_nova.models.User;
import dev.annavincenzi.the_daily_nova.services.ArticleService;
import dev.annavincenzi.the_daily_nova.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String home(Model viewModel) {
        LocalDateTime now = LocalDateTime.now();

        String day = now.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH));

        String date = now.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH));

        List<ArticleDto> articles = articleService.readAll();

        // articles home
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishedOn).reversed());

        List<ArticleDto> lastFiveArticles = articles.stream().limit(5).collect(Collectors.toList());

        viewModel.addAttribute("articles", lastFiveArticles);
        viewModel.addAttribute("day", day);
        viewModel.addAttribute("date", date);
        viewModel.addAttribute("user", new UserDto());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model,
            RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", "", "There is already an account registered with this email.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);

            // Passa anche day e date per la home perché la home li usa per mostrare info
            LocalDateTime now = LocalDateTime.now();
            model.addAttribute("day", now.format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)));
            model.addAttribute("date", now.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)));

            return "home";
        }

        userService.saveUser(userDto, redirectAttributes, request, response);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful");

        return "redirect:/";

    }

    @GetMapping("/search/{id}")
    public String userArticleSearch(@PathVariable("id") Long id, Model viewModel) {
        User user = userService.find(id);
        viewModel.addAttribute("title", "Tutti gli articoli trovati per utente" + user.getUsername());

        List<ArticleDto> articles = articleService.searchByAuthor(user);
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }
}
