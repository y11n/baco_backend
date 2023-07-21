package solux.baco.service.RouteModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonClass {

    private int code;

    public int getCode() {
        return code;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    private String currentDateTime;

    public String getCurrentDateTime() {
        return currentDateTime;
    }


    private RouteUnitEnt route;

    public RouteUnitEnt getRoute() {
        return route;
    }

}

