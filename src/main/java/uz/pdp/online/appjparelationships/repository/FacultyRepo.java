package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.appjparelationships.entity.Faculty;

import java.util.List;

public interface FacultyRepo extends JpaRepository<Faculty,Integer> {

    boolean existsByNameAndUniversityId(String name, Integer universityId);

    boolean existsByNameAndUniversityIdAndIdNot(String name, Integer university_id, Integer id);

    // select * from faculty where university_id=university_id
    Page<Faculty> findAllByUniversityId(Integer universityId, Pageable pageable);

    List<Faculty> findAllByUniversity_Id(Integer universityId);
}
