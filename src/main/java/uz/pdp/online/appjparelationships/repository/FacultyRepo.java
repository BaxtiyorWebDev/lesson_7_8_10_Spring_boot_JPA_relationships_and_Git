package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.appjparelationships.entity.Faculty;

import java.util.List;

public interface FacultyRepo extends JpaRepository<Faculty,Integer> {

    boolean existsByNameAndUniversityId(String name, Integer universityId);

    // select * from faculty where university_id=university_id
    List<Faculty> findAllByUniversityId(Integer universityId);
}
