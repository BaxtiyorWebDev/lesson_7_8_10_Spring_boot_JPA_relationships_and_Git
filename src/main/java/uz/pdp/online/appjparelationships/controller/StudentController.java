package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.Address;
import uz.pdp.online.appjparelationships.entity.Group;
import uz.pdp.online.appjparelationships.entity.Student;
import uz.pdp.online.appjparelationships.entity.Subject;
import uz.pdp.online.appjparelationships.payload.StudentDto;
import uz.pdp.online.appjparelationships.repository.AddressRepository;
import uz.pdp.online.appjparelationships.repository.GroupRepository;
import uz.pdp.online.appjparelationships.repository.StudentRepo;
import uz.pdp.online.appjparelationships.repository.SubjectRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    SubjectRepo subjectRepo;
    @Autowired
    GroupRepository groupRepository;

    @PostMapping
    public String add(@RequestBody StudentDto studentDto) {
        Student student = new Student();
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        List<Subject> allById = subjectRepo.findAllById(studentDto.getSubjectsId());
        if (optionalGroup.isPresent()) {
            Address address = new Address();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            address.setDistrict(studentDto.getDistrict());
            address.setCity(studentDto.getCity());
            address.setStreet(studentDto.getStreet());
            addressRepository.save(address);
            student.setGroup(optionalGroup.get());
            student.setSubjects(allById);
            student.setAddress(address);
            studentRepo.save(student);
            return "Data added";
        }
        return "Data not added";
    }

    //Vazirlik uchun
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {//kelgan parametrni ushlab olish
        //1-1=0     2-1=1       page=3-1=2      4-1=3
        //select * from student limit 10 offsett (0*10)
        //select * from student limit 10 offsett (1*10)
        //select * from student limit 10 offsett (2*10)
        //select * from student limit 10 offsett (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepo.findAll(pageable);
        return studentPage;
    }

    //Universitet uchun
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId, @RequestParam int page) {//kelgan parametrni ushlab olish
        //1-1=0     2-1=1       page=3-1=2      4-1=3
        //select * from student limit 10 offsett (0*10)
        //select * from student limit 10 offsett (1*10)
        //select * from student limit 10 offsett (2*10)
        //select * from student limit 10 offsett (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepo.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

//    //Universitet rahbari uchun
//    @GetMapping("byUniversityId/{id}")
//    public List<Student> studentListByUniversityId(@PathVariable Integer id) {
//        List<Student> allByGroup_facultyId_universityId = studentRepo.findAllByGroup_FacultyId_UniversityId(id);
//        return allByGroup_facultyId_universityId;
//    }

    //Fakultet rahbari uchun
    @GetMapping("/facultyId/{id}")
    public List<Student> studentListByFacultyId(@PathVariable Integer id) {
        List<Student> allByGroup_facultyId = studentRepo.findAllByGroup_FacultyId(id);
        return allByGroup_facultyId;
    }

    //Guruh rahbari uchun
    @GetMapping("/groupId/{id}")
    public List<Student> studentListByGroupId(@PathVariable Integer id) {
        List<Student> allByGroupId = studentRepo.findAllByGroupId(id);
        return allByGroupId;
    }

    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Student> optionalStudent = studentRepo.findById(id);
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (optionalStudent.isPresent() && optionalAddress.isPresent()) {
            Student editingStudent = optionalStudent.get();
            editingStudent.setFirstName(studentDto.getFirstName());
            editingStudent.setLastName(studentDto.getLastName());
            Address address = optionalAddress.get();
            address.setDistrict(studentDto.getDistrict());
            address.setCity(studentDto.getCity());
            address.setStreet(studentDto.getStreet());
            addressRepository.save(address);
            editingStudent.setAddress(address);
            studentRepo.save(editingStudent);
            return "Data edited";
        }
        return "Data not found";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        Optional<Student> optionalStudent = studentRepo.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            studentRepo.delete(optionalStudent.get());
            addressRepository.delete(student.getAddress());
            return "Data deleted";
        }
        return "Data not found";
    }
}
