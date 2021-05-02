package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.online.appjparelationships.entity.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {

    // groupni ichidagi facultetni university_id orqali bog'langan grouplarni chiqaradi
    Page<Group> findAllByFaculty_UniversityId(Integer faculty_university_id, Pageable pageable); // JPA query

    List<Group> findAllByFaculty_UniversityId(Integer faculty_university_id);

    List<Group> findAllByFaculty_Id(Integer faculty_id);

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

    boolean existsByNameAndFaculty_IdAndIdNot(String name, Integer faculty_id, Integer id);
}
