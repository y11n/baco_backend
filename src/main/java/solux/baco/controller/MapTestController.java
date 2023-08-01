/**
 *html 동적 렌더링 테스트하는 용도
 *

package solux.baco.controller;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solux.baco.service.ReviewModel.ReviewDTO;
import solux.baco.service.ReviewService;
import solux.baco.service.RouteModel.JsonDataEntity;

@Slf4j
@Controller
public class MapTestController {

    private final ReviewService reviewService;

    public MapTestController (ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/mapTest")
    public String mapTestController(Model model) { //@RequestBody : 요청바디와 데이터 매핑.
        //JsonData db에서 가져오기


        JsonDataEntity JsonData = reviewService.getJsonData(); //이 부분은 (상세조회컨트롤러에서) 후기와 함께 데이터 가져온 다음, jsonData변수에 넣어주는걸로 바꾸면 될 듯..
        if (jsonData != null) {
            //JsonData를 html에 렌더링하기 위해 Thymeleaf 템플릿으로 전달. (=Thymeleaf를 통해 html로 전달)
            model.addAttribute("jsonData", jsonData, getJsonData()); //jsonDTO형태로 맞춰서 저장했다가 getter메서드로 가져와서 model로 전달하기.

            //html 렌더링 성공하면 =>
            String serverUrl = "https://port-0-baco-server-eg4e2alkhufq9d.sel4.cloudtype.app/";
            String mapUrl = serverUrl+"mapPage";

            //reviewDetailDTO의 mapUrl 에 setMapUrl(mapUrl)해서 설정 후 객체 형태로 반환.
            // (이후 프론트에서는 json에서 파싱해서 해당 url은 iframe으로 띄우고 다른 정보들도 각각 띄워줌.)
            return reviewDetailDTO; //

        }

    }
}
 *
 */