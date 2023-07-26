package solux.baco.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long member_id;

    private String email;

    private String password;

    private String password2;

    private String nickname;
<<<<<<< HEAD

    @OneToMany(mappedBy = "member") //회원 고유 식별자로 후기를 찾아야해서 mappedBy ="member"
    private List<Review> review; //FK

=======
/*
    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review; //FK (외래키)
*/
>>>>>>> develop
    //비밀번호 검증
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
