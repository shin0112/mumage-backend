package mumage.mumagebackend.controller;

import mumage.mumagebackend.exception.NoResultException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice("mumage.mumagebackend.controller")
public class ExceptionController {

    @ExceptionHandler({RuntimeException.class, NoResultException.class})
    public String noResultHandle(Exception e, Model model){

        model.addAttribute("errorMessage",e.getMessage());

        return "error/noResult";
    }

}
