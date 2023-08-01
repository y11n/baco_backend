package solux.baco.service.ReviewModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class returnReviewDataDTO {
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

    private String startPlace;

    private String endPlace;

    private String content;


    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    //7/30 추가
    private String mapUrl;

}
