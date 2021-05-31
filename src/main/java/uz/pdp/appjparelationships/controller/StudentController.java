package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDTO;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;


    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 3);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId,
                                                  @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Student> facultyId1 = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return facultyId1;
    }


    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId,
                                                @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Student> facultyId1 = studentRepository.findAllByGroup_Id(groupId, pageable);
        return facultyId1;
    }

    @GetMapping("/getAllStudent")
    public List<Student> getStudent() {
        List<Student> all = studentRepository.findAll();
        return all;
    }

    @GetMapping("/getAllById/{id}")
    public Student getById(@PathVariable Integer id) {
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isPresent()) {
            Student student = byId.get();
            return student;
        }
        return new Student();
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestBody StudentDTO studentDTO) {
        boolean exists = studentRepository.existsByFirstNameAndLastNameAndGroupId(
                studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getGroupId());
        if (exists) {
            return "This group such student exists";
        }
        List<Subject> subjectList = new ArrayList<>();
        Integer[] subjectList1 = studentDTO.getSubjectList();
        for (Integer integer : subjectList1) {
            Subject subject = subjectRepository.findById(integer).get();
            subjectList.add(subject);
        }
        Address address = new Address();
        address.setCity(studentDTO.getCity());
        address.setDistrict(studentDTO.getDistrict());
        address.setStreet(studentDTO.getStreet());
        Address savedAddress = addressRepository.save(address);

        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());

        Optional<Group> byId = groupRepository.findById(studentDTO.getGroupId());
        if (!byId.isPresent()) {
            return "Not found group Id";
        }
        student.setGroup(byId.get());
        student.setAddress(savedAddress);
        student.setSubjects(subjectList);
        studentRepository.save(student);
        return "saved";

    }

    @PutMapping("editStudent/{id}")
    public String deleteById(@PathVariable Integer id, @RequestBody StudentDTO studentDTO) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()) return "Not found group ID";

        if (!optionalStudent.isPresent()){
            boolean exists = studentRepository.existsByFirstNameAndLastNameAndGroupId(
                    studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getGroupId());
            if (exists) {
                return "This group such student exists";
            }
            Student student = optionalStudent.get();

            Address address = student.getAddress();
            address.setCity(studentDTO.getCity());
            address.setDistrict(studentDTO.getDistrict());
            address.setStreet(studentDTO.getStreet());
            Address savedAddress = addressRepository.save(address);

            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());

            List<Subject> subjectList = new ArrayList<>();
            Integer[] subjectList1 = studentDTO.getSubjectList();
            for (Integer integer : subjectList1) {
                Subject subject = subjectRepository.findById(integer).get();
                subjectList.add(subject);
            }

            student.setAddress(savedAddress);
            student.setSubjects(subjectList);
            studentRepository.save(student);
            return "saved";

        }
        return "Student Not Found";
    }
    @DeleteMapping("/deleteStudent/{id}")
    public String deleteById(@PathVariable Integer id){

        if (!addressRepository.existsById(id))
            return "Not";

        studentRepository.deleteById(id);
        return "deleted";
    }
}
