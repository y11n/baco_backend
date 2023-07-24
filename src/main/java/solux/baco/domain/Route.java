/*
package solux.baco.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long id;

    private  String start;

    private String end;

    private String path;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review; //FK

}
*/