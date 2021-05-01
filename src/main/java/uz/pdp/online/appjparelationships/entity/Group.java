package uz.pdp.online.appjparelationships.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToOne //  MANY GROUP TO ONE FACULTY
    private Faculty faculty;

//    @OneToMany// ONE GROUP TO MANY STUDENT
//    private List<Student> students;
}
