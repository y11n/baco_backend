package solux.baco.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import solux.baco.domain.Member;
import solux.baco.service.MemberService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class passwordController {

    private final MemberService memberService;
    private String myEmail;

    @GetMapping("/update")
    public String getPassword(HttpSession session, Model model) {
        myEmail = (String) session.getAttribute("loginEmail");
        model.addAttribute("passwordForm", new PasswordForm());
        return "update";
    }

    @PostMapping("/update")
    public String updatePassword(@Valid PasswordForm form){
        Optional<Member> presentMember = memberService.findByEmail(myEmail);
        memberService.Password_update(form, presentMember);
        return "home";
    }
}
