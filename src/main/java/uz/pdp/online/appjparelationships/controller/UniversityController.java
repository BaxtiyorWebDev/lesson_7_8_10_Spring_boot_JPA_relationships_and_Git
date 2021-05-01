package uz.pdp.online.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.appjparelationships.entity.Address;
import uz.pdp.online.appjparelationships.entity.University;
import uz.pdp.online.appjparelationships.payload.UniversityDto;
import uz.pdp.online.appjparelationships.repository.AddressRepository;
import uz.pdp.online.appjparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class UniversityController {
    @Autowired // beanni chaqirish uchun
    UniversityRepository universityRepository; // BEAN
    @Autowired
    AddressRepository addressRepository;// BEAN

    // READ
    @RequestMapping(value = "/university", method = RequestMethod.GET)
    public List<University> getUniversities() {
        List<University> universityList = universityRepository.findAll();
        return universityList;
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
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isPresent()) {
            University university = universityOptional.get();
            universityRepository.delete(university);
            return "Data deleted!";
        }
        return "Data not found!";
    }
}
