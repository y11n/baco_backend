package solux.baco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapMapController {

    @GetMapping("/mapmap")
    public String showMapPage() {
        return "mapmap"; // HTML 파일 이름 (확장자 제외)을 리턴
    }
}