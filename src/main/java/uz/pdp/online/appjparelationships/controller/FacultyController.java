package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.Faculty;
import uz.pdp.online.appjparelationships.entity.Group;
import uz.pdp.online.appjparelationships.entity.Student;
import uz.pdp.online.appjparelationships.entity.University;
import uz.pdp.online.appjparelationships.payload.FacultyDto;
import uz.pdp.online.appjparelationships.repository.FacultyRepo;
import uz.pdp.online.appjparelationships.repository.GroupRepository;
import uz.pdp.online.appjparelationships.repository.StudentRepo;
import uz.pdp.online.appjparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    @Autowired
    FacultyRepo facultyRepo;
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    StudentRepo studentRepo;

    //CREATE
    @PostMapping()
    public String addFaculty(@RequestBody FacultyDto facultyDto) {
        boolean exists = facultyRepo.existsByNameAndUniversityId(facultyDto.getName(), facultyDto.getUniversityId());
        if (exists)
            return "This university such faculty exist";
        Faculty faculty = new Faculty();
        Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversityId());
        faculty.setName(facultyDto.getName());
        if (optionalUniversity.isPresent()) {
            faculty.setUniversity(optionalUniversity.get());
            facultyRepo.save(faculty);
            return "Faculty saved";
        }
        return "University not saved";
    }

    //READ VAZIRLIK UCHUN
    @GetMapping()
    public Page<Faculty> getFacultyList(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page,15);
        Page<Faculty> facultyPage = facultyRepo.findAll(pageable);
        return facultyPage;
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Integer id) {
        Optional<Faculty> optionalFaculty = facultyRepo.findById(id);
        if (optionalFaculty.isPresent()) {
            return optionalFaculty.get();
        }
        return new Faculty();
    }

    //UNIVERSITET XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public Page<Faculty> getFacultiesByUniversityId(@PathVariable Integer universityId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Faculty> allByUniversityId = facultyRepo.findAllByUniversityId(universityId, pageable);
        return allByUniversityId;
    }

    //UPDATE
    @PutMapping("/{id}")
    public String editFaculty(@PathVariable Integer id, @RequestBody FacultyDto facultyDto) {
        boolean exists = facultyRepo.existsByNameAndUniversityIdAndIdNot(facultyDto.getName(), facultyDto.getUniversityId(), id);
        if (exists)
            return "This faculty already exist";
        Optional<Faculty> optionalFaculty = facultyRepo.findById(id);
        Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversityId());
        if (optionalFaculty.isPresent() && optionalUniversity.isPresent()) {
            Faculty editingFaculty = optionalFaculty.get();
            editingFaculty.setName(facultyDto.getName());
            editingFaculty.setUniversity(optionalUniversity.get());
            facultyRepo.save(editingFaculty);
            return "Data updated";
        }
        return "Data not found";
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String deleteFaculty(@PathVariable Integer id) {
        try {
            List<Student> allByGroup_facultyId = studentRepo.findAllByGroup_FacultyId(id);
            List<Group> allByFaculty_id = groupRepository.findAllByFaculty_Id(id);
            studentRepo.deleteAll(allByGroup_facultyId);
            groupRepository.deleteAll(allByFaculty_id);
            facultyRepo.deleteById(id);
            return "Data deleted";
        } catch (Exception e) {
            return "Error in deleting";
        }
    }
}
