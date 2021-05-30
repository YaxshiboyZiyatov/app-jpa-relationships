package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

    //VAZIRLIK UCHUN
    //READ
    @GetMapping
    public List<Group> getGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }


    //UNIVERSITET MAS'UL XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId) {
        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(universityId);
        List<Group> groupsByUniversityId = groupRepository.getGroupsByUniversityId(universityId);
        List<Group> groupsByUniversityIdNative = groupRepository.getGroupsByUniversityIdNative(universityId);
        return allByFaculty_universityId;
    }

    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }
        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }
    @PutMapping("/editGroup/{id}")
    public String editById(@PathVariable Integer id, @RequestBody GroupDto groupDto){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        Group group = optionalGroup.get();

        if (!optionalGroup.isPresent()) return "Not found Group ID";
        if (!optionalFaculty.isPresent()) return "Not found Faculty ID";

        group.setName(groupDto.getName());

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";

    }
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Integer id){
        Optional<Group> byId = groupRepository.findById(id);
        if (byId.isPresent()){
            groupRepository.deleteById(id);
            return "deleted";
        }
        return "not found group id";
    }


}
