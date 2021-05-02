package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.*;
import uz.pdp.online.appjparelationships.payload.UniversityDto;
import uz.pdp.online.appjparelationships.repository.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UniversityController {
    @Autowired // beanni chaqirish uchun
    UniversityRepository universityRepository; // BEAN
    @Autowired
    AddressRepository addressRepository;// BEAN
    @Autowired
    FacultyRepo facultyRepo;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    StudentRepo studentRepo;

    // READ
    @RequestMapping(value = "/university", method = RequestMethod.GET)
    public Page<University> getUniversities(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page,15);
        Page<University> universityPage = universityRepository.findAll(pageable);
        return universityPage;
    }

    // READ by ID
    @RequestMapping(value = "/university/{id}", method = RequestMethod.GET)
    public University getUniversity(@PathVariable Integer id) {
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isPresent()) {
            University university = universityOptional.get();
            return university;
        }
        return new University();
    }

    // CREATE
    @RequestMapping(value = "/university", method = RequestMethod.POST)
    public String addUniversity(@RequestBody UniversityDto universityDto) {
        // YANGI ADDRESS OCHDIK
        Address address = new Address();
        address.setCity(universityDto.getCity());
        address.setDistrict(universityDto.getDistrict());
        address.setStreet(universityDto.getStreet());
        // YASAB OLGAN ADDRESSIMIZNI DBGA SAQLADIK VA U BIZGA ADDRESSNI BERDI
        Address savedAddress = addressRepository.save(address);

        // YANGI UNIVERSITET YASAB OLDIK
        University university = new University();
        // SAQLANGAN ADDRESSNI UNIVERSITETGA SAQLADIK
        university.setName(universityDto.getName());

        university.setAddress(savedAddress);
        universityRepository.save(university);
        return "University added";
    }

    // UPDATE
    @RequestMapping(value = "/university/{id}", method = RequestMethod.PUT)
    public String editUniversity(@PathVariable Integer id, @RequestBody UniversityDto universityDto) {
        boolean exists = universityRepository.existsByNameAndAddressIdAndIdNot(universityDto.getName(), universityDto.getAddressId(), id);
        if (exists)
            return "This university already exist";
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isPresent()) {
            University editingUniversity = universityOptional.get();
            Address address = editingUniversity.getAddress();
            address.setCity(universityDto.getCity());
            address.setDistrict(universityDto.getDistrict());
            address.setStreet(universityDto.getStreet());
            editingUniversity.setName(universityDto.getName());
            editingUniversity.setAddress(address);
            addressRepository.save(address);
            universityRepository.save(editingUniversity);
            return "Data changed!";
        }
        return "Data not found!";
    }

    // DELETE
    @RequestMapping(value = "/university/{id}", method = RequestMethod.DELETE)
    public String deleteUniversity(@PathVariable Integer id) {
        List<Student> allByGroup_facultyId_universityId = studentRepo.findAllByGroup_FacultyId_UniversityId(id);
        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(id);
        List<Faculty> allByUniversityId = facultyRepo.findAllByUniversity_Id(id);
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isPresent()) {
            studentRepo.deleteAll(allByGroup_facultyId_universityId);
            groupRepository.deleteAll(allByFaculty_universityId);
            facultyRepo.deleteAll(allByUniversityId);
            universityRepository.delete(universityOptional.get());
            return "Data deleted!";
        }
        return "Data not found!";
    }
}
