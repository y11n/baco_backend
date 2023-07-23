package solux.baco.controller;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@Getter @Setter
public class LoginController {

    private final LoginService loginService;
    private Member presentMember;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginId(@ModelAttribute("form") LoginForm form, BindingResult bindingResult
                          , HttpSession session){

        if(bindingResult.hasErrors()){
            return "login";
        }

        presentMember = loginService.login(form);

        //현재 로그인한 회원의 이메일 정보를 세션에 담아줌
        session.setAttribute("loginEmail", presentMember.getEmail());
        return "sessionValueCheck";
    }
}
