package dev.annavincenzi.the_daily_nova.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.annavincenzi.the_daily_nova.dtos.UserDto;
import dev.annavincenzi.the_daily_nova.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request,
            HttpServletResponse response);

    User findUserByEmail(String email);

    User find(Long id);
}
