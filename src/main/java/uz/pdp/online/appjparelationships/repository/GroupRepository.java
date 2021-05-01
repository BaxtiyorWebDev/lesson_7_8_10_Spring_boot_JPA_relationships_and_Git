package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.online.appjparelationships.entity.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {

    // groupni ichidagi facultetni university_id orqali bog'langan grouplarni chiqaradi
    List<Group> findAllByFaculty_UniversityId(Integer faculty_university_id); // JPA query

    @Query("select gr from groups gr where gr.faculty.university.id=:universityId")//JPA not native query
    List<Group> getGroupsByUniversityId(Integer universityId);

    @Query(value = "select *\n" +
            "from groups g\n" +
            "         join faculty f on f.id = g.faculty_id\n" +
            "         join university u on u.id = f.university_id\n" +
            "where u.id=:universityId", nativeQuery = true)
    List<Group> getGroupsByUniversityIdNative(Integer universityId);


    // group name and faculty_id is exist
    boolean existsByNameAndFaculty_Id(String name, Integer faculty_id);
}
