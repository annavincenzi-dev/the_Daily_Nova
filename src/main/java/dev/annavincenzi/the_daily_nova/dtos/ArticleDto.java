package dev.annavincenzi.the_daily_nova.dtos;

import java.time.LocalDate;

import dev.annavincenzi.the_daily_nova.models.Category;
import dev.annavincenzi.the_daily_nova.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishedOn;
    private User user;
    private Category category;
}
