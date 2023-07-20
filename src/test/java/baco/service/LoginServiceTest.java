package baco.service;

import baco.controller.LoginForm;
import baco.domain.Member;
import baco.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LoginService loginService;

    @Test
    public void 로그인_성공() throws Exception{
        //given
        Member member = new Member();
        member.setEmail("abc@gmail.com");
        member.setPassword("1234");
        member.setPassword2("1234");
        memberService.join(member);

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("abc@gmail.com");
        loginForm.setPassword("1234");

        //when
        Member findMember = loginService.login(loginForm);

        //then
        Assertions.assertThat(member.getEmail()).isEqualTo(findMember.getEmail());
    }

}