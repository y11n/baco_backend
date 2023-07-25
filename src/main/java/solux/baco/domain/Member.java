package solux.baco.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String password2;

    private String nickname;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review; //FK (Review테이블과 매핑해서 회원정보(ex_닉네임) 알아내기 위해 외래키로 설정.)

    //비밀번호 검증
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
