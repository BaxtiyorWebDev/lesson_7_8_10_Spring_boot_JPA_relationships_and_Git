package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.appjparelationships.entity.Student;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Integer> {

    //universitetga tegishli talabalar ro'yxatini olish uchun
    List<Student> findAllByGroup_FacultyId_UniversityId(Integer group_faculty_university_id);

    Page<Student> findAllByGroup_Faculty_UniversityId(Integer group_faculty_university_id, Pageable pageable);

    //aynan bir facultetda o'qiydigan talabalarni ro'yxatini olish
    Page<Student> findAllByGroup_FacultyId(Integer group_faculty_id, Pageable pageable);

    List<Student> findAllByGroup_FacultyId(Integer group_faculty_id);

    //aynan bir guruhga tegishli talabalarni ro'yxatini olish uchun
    List<Student> findAllByGroupId(Integer group_id);

    boolean existsByFirstNameAndLastNameAndAddressId(String firstName, String lastName, Integer address_id);

    boolean existsByFirstNameAndLastNameAndAddressIdAndIdNot(String firstName, String lastName, Integer address_id, Integer id);
}
