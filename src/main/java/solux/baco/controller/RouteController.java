package solux.baco.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import solux.baco.service.RouteService;

import java.util.Map;

@Controller
@RequestMapping("/Route")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/Route/passRoute")
    public Map<String, Object> getRouteController(@RequestParam double[] startCoordinate, @RequestParam double[] endCoordinate) {
        //RouteService 호출
        return routeService.passRouteData(startCoordinate,endCoordinate);

    }

}
