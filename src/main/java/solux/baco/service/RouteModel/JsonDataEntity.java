package solux.baco.service.RouteModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true) //선택적으로 필요한 값만 선택하기 위해 정의되지않은 내용은 무시.
public class JsonDataEntity {


        private List<List<Double>> path;


        public List<List<Double>> getPath() {
            return path;
        }

        public void setPath(List<List<Double>> path) {
            this.path = path;
        }
    }

