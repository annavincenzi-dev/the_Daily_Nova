package dev.annavincenzi.the_daily_nova.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import dev.annavincenzi.the_daily_nova.repositories.ArticleRepository;
import dev.annavincenzi.the_daily_nova.repositories.CareerRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NotificationInterceptor implements HandlerInterceptor {

    @Autowired
    CareerRequestRepository careerRequestRepository;

    @Autowired
    ArticleRepository articleRepository;

    @SuppressWarnings("null")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && request.isUserInRole("ROLE_ADMIN")) {
            int careerCount = careerRequestRepository.findByIsCheckedFalseAndIsRejectedFalse().size();

            modelAndView.addObject("careerRequests", careerCount);

        }

        if (modelAndView != null && request.isUserInRole("ROLE_REVISOR")) {
            int articleCount = articleRepository.findByIsAcceptedIsNull().size();
            modelAndView.addObject("articlesToCheck", articleCount);
        }
    }
}
