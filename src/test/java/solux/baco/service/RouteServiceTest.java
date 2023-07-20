package solux.baco.service;

import org.junit.jupiter.api.Test;
import solux.baco.service.RouteModel.JsonClass;
import org.junit.jupiter.api.Assertions;

public class RouteServiceTest {



    @Test
    public void testGetRoute() {
        double[] startCoordinate = { 127.12345, 37.12345 };
        double[] endCoordinate = { 128.12345, 38.12345 };

        RouteService routeServce = new RouteService();

        JsonClass result = routeServce.getRoute(startCoordinate,endCoordinate);
        System.out.println(result);

        System.out.println("Code: " + result.getCode());
        System.out.println("message: " + result.getMessage());

        System.out.println("start: " + result.getStart());
        System.out.println("goal: " + result.getGoal());

        System.out.println("distance: " + result.getDistance());
        System.out.println("path: " + result.getPath());

    }

    @Test
    public void testJsonClass(){

        double[] startCoordinate = { 128.12345, 38.12345 };
        double[] endCoordinate = { 127.12345, 37.12345 };

        RouteService routeServce = new RouteService();
        JsonClass result2 = routeServce.getRoute(startCoordinate,endCoordinate);


        System.out.println("Code: " + result2.getCode());
        System.out.println("message: " + result2.getMessage());

        System.out.println("start: " + result2.getStart());
        System.out.println("goal: " + result2.getGoal());

        System.out.println("distance: " + result2.getDistance());
        System.out.println("path: " + result2.getPath());
    }

}
