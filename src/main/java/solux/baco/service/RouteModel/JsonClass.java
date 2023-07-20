package solux.baco.service.RouteModel;

public class JsonClass {


    private int code;
    public int getCode() {
        return code;
    }

    private String message;
    public String getMessage() {
        return message;
    }

    private LngLatPosition start;
    public LngLatPosition getStart() {
        return start;
    }

    private LngLatPosition goal;
    public LngLatPosition getGoal() {
        return goal;
    }

    private int distance;
    public int getDistance() {
        return distance;
    }

    private LngLatPosition[][] path;
    public LngLatPosition[][] getPath() {
        return path;
    }

}

