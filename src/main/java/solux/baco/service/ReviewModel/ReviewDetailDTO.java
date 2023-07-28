package solux.baco.service.ReviewModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewDetailDTO {
    //게시글 상세보기로 반환할 값 모음

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String startPlace;

    private String endPlace;

    private String content;

    private LocalDate date ;

    private String nickname;

}
