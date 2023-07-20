package baco.service;

import baco.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import baco.domain.Member;

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
}
