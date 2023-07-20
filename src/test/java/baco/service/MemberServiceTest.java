package baco.service;

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
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setEmail("abc@gmail.com");
        member.setPassword("1234");
        member.setPassword2("1234");

        //when
        Long savedId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(savedId);
        Assertions.assertThat(member.getEmail()).isEqualTo(findMember.getEmail());
    }

    @Test
    public void 비밀번호_불일치() throws Exception{
        //given
        Member member = new Member();
        member.setEmail("glory@gmail.com");
        member.setPassword("1234");
        member.setPassword2("5678");

        //when
        try{
            memberService.join(member);
        } catch (IllegalStateException e){
            return;
        }

        //then
        Assertions.fail("예외가 발생해야 합니다.");
    }

}