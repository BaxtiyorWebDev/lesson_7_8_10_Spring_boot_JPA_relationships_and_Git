package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.Subject;
import uz.pdp.online.appjparelationships.repository.SubjectRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {

    @Autowired
    SubjectRepo subjectRepo;


    //CREATE
    @RequestMapping(method = RequestMethod.POST)
    public String add(@RequestBody Subject subject) {
        boolean existsByName = subjectRepo.existsByName(subject.getName());
        if (existsByName) {
            return "This subject already exist";
        }
        subjectRepo.save(subject);
        return "Subject added";
    }

    //GET
    @GetMapping
    public List<Subject> getSubjects() {
        List<Subject> subjectList = subjectRepo.findAll();
        return subjectList;
    }

     //GET
    @GetMapping(value = "/{id}")
    public Subject get(@PathVariable Integer id) {
        Optional<Subject> optionalSubject = subjectRepo.findById(id);
        if (optionalSubject.isPresent()) {
            return optionalSubject.get();
        }
        return new Subject();
    }

    //UPDATE
    @PutMapping(value = "/{id}")
    public String edit(@PathVariable Integer id,@RequestBody Subject subject) {
        Optional<Subject> optionalSubject = subjectRepo.findById(id);
        if (optionalSubject.isPresent()) {
            Subject editingSubject = optionalSubject.get();
            editingSubject.setName(subject.getName());
            subjectRepo.save(editingSubject);
            return "Data edited";
        }
        return "Data not found";
    }

    //DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Subject> optionalSubject = subjectRepo.findById(id);
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subjectRepo.delete(subject);
            return "Data deleted";
        }
        return "Data not found";
    }
}
