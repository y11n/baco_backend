package solux.baco.controller;

import solux.baco.domain.Member;
import solux.baco.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginId(@Valid LoginForm form, BindingResult bindingResult, HttpServletRequest request){

        if(bindingResult.hasErrors()){
            return "login";
        }

        loginService.login(form);
        //Member loginMember = loginService.login(form);
        return "redirect:/";
    }
}
