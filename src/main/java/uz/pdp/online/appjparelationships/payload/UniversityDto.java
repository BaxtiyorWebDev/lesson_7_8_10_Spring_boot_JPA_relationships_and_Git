package uz.pdp.online.appjparelationships.payload;

import lombok.Data;

@Data
public class UniversityDto { // data transfer object, datani tashish uchun xizmat qiladi
    private String name;
    private String city;
    private String district;
    private String street;

    //FOR PUT METHOD
    private Integer addressId;
}
