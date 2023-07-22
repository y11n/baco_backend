package solux.baco.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solux.baco.service.RouteService;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/Route")
public class RouteController {

    private final RouteService routeService;

    //생성자 만들어서 의존성 주입
    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // (서버 배포 전)http://localhost:8080/Route/passRoute에 요청파라미터는 startCoordinate와 endCoordinate
    @GetMapping("/passRoute")
    public Map<String, Object> getRouteController(@RequestParam double[] startCoordinate, @RequestParam double[] endCoordinate) {
        log.info("checkLog:RouteController - getRouteController called with startCoordinate: {} and endCoordinate: {}", startCoordinate, endCoordinate);

        //RouteService 호출
        return routeService.passRouteData(startCoordinate,endCoordinate);

    }

}
