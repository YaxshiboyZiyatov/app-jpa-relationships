package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    //CREATE
    @PostMapping("/add")
    public String addSubject(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }

    //READ
//    @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public List<Subject> getSubjects() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList;
    }
    @GetMapping("/getById/{id} ")
    public Subject getByIdSubject(@PathVariable Integer id) {
        Optional<Subject> optionalAddress = subjectRepository.findById(id);
        if (optionalAddress.isPresent()){
            Subject subject = optionalAddress.get();
            return subject;
        }
        return new Subject();

    }
    @PutMapping("/editById/{id}")
    public String editSubject(@PathVariable Integer id,@RequestBody Subject subject){
        Optional<Subject> byId = subjectRepository.findById(id);
        Subject subject1 = byId.get();
        if (!byId.isPresent())
            return "Not found subject";

        if (subjectRepository.existsByName(subject.getName()))
            return "already exist";
        subject1.setName(subject.getName());
        subjectRepository.save(subject1);
        return "Added Saccess";
    }


    @DeleteMapping("deleteSubject/{id}")
    public String deleteIdSubject(@PathVariable Integer id) {
        Optional<Subject> optionalGroups = subjectRepository.findById(id);
        if (optionalGroups.isPresent()) {
            subjectRepository.deleteById(id);
            return "Subject deleted!";
        }
        return "not found!";

    }


}
