package solux.baco.service;

import lombok.extern.slf4j.Slf4j;
import solux.baco.controller.PasswordForm;
import solux.baco.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solux.baco.domain.Member;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member){
        password_check(member.getPassword(), member.getPassword2());
        memberRepository.save(member);
        return member.getId();
    }

    private void password_check(String pwd, String pwd2) {
        if( !pwd.equals(pwd2)){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);}

    public void Password_update(PasswordForm form, Optional<Member> presentMember) {
        //예외 처리 필요함...
        password_check(form.getNewPassword(), form.getNewPasswordConfirm());
        Member updateMember = new Member();
        updateMember.setId(presentMember.get().getId());
        updateMember.setEmail(presentMember.get().getEmail());
        updateMember.setNickname(presentMember.get().getNickname());
        updateMember.setPassword(form.getNewPassword());
        updateMember.setPassword2(form.getNewPasswordConfirm());
        memberRepository.save(updateMember);
    }
}
