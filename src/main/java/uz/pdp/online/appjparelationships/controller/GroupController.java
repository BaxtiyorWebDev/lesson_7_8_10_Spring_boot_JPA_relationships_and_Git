package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.Faculty;
import uz.pdp.online.appjparelationships.entity.Group;
import uz.pdp.online.appjparelationships.entity.Student;
import uz.pdp.online.appjparelationships.payload.GroupDto;
import uz.pdp.online.appjparelationships.repository.FacultyRepo;
import uz.pdp.online.appjparelationships.repository.GroupRepository;
import uz.pdp.online.appjparelationships.repository.StudentRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepo facultyRepo;
    @Autowired
    StudentRepo studentRepo;

    //READ VAZIRLIK UCHUN
    @GetMapping
    public Page<Group> groupList(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page,15);
        return groupRepository.findAll(pageable);
    }

    //UNIVERSITET MAS'UL XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public Page<Group> getGroupsByUniversityId(@PathVariable Integer universityId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(universityId, pageable);
//        List<Group> groupsByUniversityId = groupRepository.getGroupsByUniversityId(universityId);
//        List<Group> groupsByUniversityIdNative = groupRepository.getGroupsByUniversityIdNative(universityId);

        return allByFaculty_universityId;
    }

    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        Optional<Faculty> optionalFaculty = facultyRepo.findById(groupDto.getFacultyId());
        boolean exists = groupRepository.existsByNameAndFaculty_Id(groupDto.getName(), groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }
        if (exists) {
            return "This group is already exist";
        }
        group.setFaculty(optionalFaculty.get());
        groupRepository.save(group);
        return "Group added";
    }

    @PutMapping("/{id}")
    public String editGroup(@PathVariable Integer id, @RequestBody GroupDto groupDto) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        Optional<Faculty> optionalFaculty = facultyRepo.findById(groupDto.getFacultyId());
        boolean exists = groupRepository.existsByNameAndFaculty_IdAndIdNot(groupDto.getName(), groupDto.getFacultyId(), id);
        if (exists)
            return "This group already exist";
        if (optionalGroup.isPresent()&&optionalFaculty.isPresent()) {
            Group editingGroup = optionalGroup.get();
            editingGroup.setName(groupDto.getName());
            editingGroup.setFaculty(optionalFaculty.get());
            groupRepository.save(editingGroup);
            return "Data edited";
        }
        return "Data not found";
    }

    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable Integer id) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        List<Student> allByGroupId = studentRepo.findAllByGroupId(id);
        if (optionalGroup.isPresent()) {
            studentRepo.deleteAll(allByGroupId);
            groupRepository.delete(optionalGroup.get());
            return "Data deleted";
        }
        return "Data not found";
    }
}
