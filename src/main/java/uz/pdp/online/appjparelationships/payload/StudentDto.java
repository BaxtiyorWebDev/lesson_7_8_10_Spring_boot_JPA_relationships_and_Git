package uz.pdp.online.appjparelationships.payload;

import lombok.Data;

import java.util.List;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;
    private Integer groupId;
    private List<Integer> subjectsId;

    private String city;
    private String district;
    private String street;
    private Integer addressId;
}
