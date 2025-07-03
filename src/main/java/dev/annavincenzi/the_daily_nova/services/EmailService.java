package dev.annavincenzi.the_daily_nova.services;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);
}
