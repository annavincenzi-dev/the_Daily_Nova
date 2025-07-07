package dev.annavincenzi.the_daily_nova.services;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CrudService<ReadDto, Model, Key> {

    List<ReadDto> readAll();

    ReadDto read(Key key);

    ReadDto create(Model model, Principal principal, MultipartFile[] files);

    ReadDto update(Key key, Model model, MultipartFile[] files);

    void delete(Key key);

}
