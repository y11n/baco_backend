package baco.controller;

import baco.domain.Member;
import baco.service.MemberService;
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
public class JoinController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String createMember(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "join";
    }

    @PostMapping("/join")
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){
            return "join";
        }

        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());
        member.setPassword2(form.getPassword2());
        member.setNickname(form.getNickname());

        memberService.join(member);
        return "redirect:/";

    }


}
