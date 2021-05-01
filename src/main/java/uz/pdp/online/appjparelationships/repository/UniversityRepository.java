package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.online.appjparelationships.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
}
