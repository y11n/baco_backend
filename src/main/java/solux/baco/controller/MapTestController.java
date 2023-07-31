package solux.baco.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import solux.baco.service.RouteModel.JsonDataEntity;

@Slf4j
@Controller
public class MapTestController {

    @PostMapping("/mapTest")
    public String showMapPage(@RequestBody String routePoint, Model model) {

        log.info("checklog: MapTestController_showMapPage-routePointParameter:{}",routePoint);
        //if (routeParameter != null)
        //JsonData를 html에 렌더링하기 위해 Thymeleaf 템플릿으로 전달. (=Thymeleaf를 통해 html로 전달)
        model.addAttribute("jsonData", routePoint); //jsonDTO형태로 맞춰서 저장했다가 getter메서드로 가져와서 model로 전달하기.


        return "mapTest"; // HTML 파일 이름 (확장자 제외)을 리턴
    }
}