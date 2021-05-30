package uz.pdp.appjparelationships.payload;

import lombok.Data;
import uz.pdp.appjparelationships.entity.Group;

@Data
public class StudentDTO {
//    Student
    private String firstName;
    private String lastName;
//    Address
    private String city;
    private String district;
    private String street;
//    Group
    private Integer groupId;

//    subject
    private Integer[] subjectList;



}
