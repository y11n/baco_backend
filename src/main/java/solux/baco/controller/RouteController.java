package solux.baco.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solux.baco.service.RouteService;
import java.util.Map;


@Slf4j
@RestController

public class RouteController {

    private final RouteService routeService;

    //생성자 만들어서 의존성 주입
    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // (서버 배포 전)http://localhost:8080/Route/passRoute에 요청파라미터는 startCoordinate와 endCoordinate
    @GetMapping("/Route")
    public Map<String, Object> getRouteController(@RequestParam double[] start, @RequestParam double[] end) {
        log.info("checkLog:RouteController - getRouteController called with start: {} and end: {}", start, end);

        //RouteService 호출
        return routeService.passRouteData(start, end);

    }

}
