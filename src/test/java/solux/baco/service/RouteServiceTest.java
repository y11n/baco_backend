package solux.baco.service;

import org.junit.jupiter.api.Test;


public class RouteServiceTest {


    @Test
    public void testGetRoute() {
        double[] startCoordinate = {127.12345, 37.12345};
        double[] endCoordinate = {128.12345, 38.12345};

        RouteService routeServce = new RouteService();
        routeServce.getRoute(startCoordinate,endCoordinate);


    }
}