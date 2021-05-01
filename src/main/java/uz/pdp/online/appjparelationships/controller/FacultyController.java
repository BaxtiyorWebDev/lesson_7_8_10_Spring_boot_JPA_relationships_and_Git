package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.Faculty;
import uz.pdp.online.appjparelationships.entity.University;
import uz.pdp.online.appjparelationships.payload.FacultyDto;
import uz.pdp.online.appjparelationships.repository.FacultyRepo;
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
    public List<Faculty> getFacultyList() {
        List<Faculty> facultyList = facultyRepo.findAll();
        return facultyList;
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
    public List<Faculty> getFacultiesByUniversityId(@PathVariable Integer universityId) {
        List<Faculty> allByUniversityId = facultyRepo.findAllByUniversityId(universityId);
        return allByUniversityId;
    }

    //UPDATE
    @PutMapping("/{id}")
    public String editFaculty(@PathVariable Integer id, @RequestBody FacultyDto facultyDto) {
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
            facultyRepo.deleteById(id);
            return "Data deleted";
        } catch (Exception e) {
            return "Error in deleting";
        }
    }
}
