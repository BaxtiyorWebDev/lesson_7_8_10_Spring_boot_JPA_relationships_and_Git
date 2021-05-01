package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.appjparelationships.entity.Subject;

public interface SubjectRepo extends JpaRepository<Subject, Integer> {
    boolean existsByName(String name);
}
