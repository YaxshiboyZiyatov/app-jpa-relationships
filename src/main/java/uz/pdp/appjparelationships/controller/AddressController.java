package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.payload.AddressDTO;
import uz.pdp.appjparelationships.repository.AddressRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/getAll")
    public List<Address> getAllAddress() {
        List<Address> all = addressRepository.findAll();
        return all;
    }
    @GetMapping("/getAllById/{id}")
    public Address getById(@PathVariable Integer id){
        Optional<Address> byId = addressRepository.findById(id);
        if (byId.isPresent()){
            Address address = byId.get();
            return address;
        }
        return new Address();
    }
    @PostMapping("/addAddress/{id}")
    public String addAddress(@RequestBody AddressDTO addressDTO) {
        boolean exists = addressRepository.existsByCityAndDistrictAndStreet(
                addressDTO.getCity(), addressDTO.getDistrict(), addressDTO.getStreet());
        if (exists){
            return "Already exists";
        }
        Address address1=new Address();
        address1.setCity(addressDTO.getCity());
        address1.setDistrict(addressDTO.getDistrict());
        address1.setStreet(addressDTO.getStreet());
        addressRepository.save(address1);
        return "Added Success";
    }

    @PutMapping("/editAddress/{id}")
    public String editAddress(@PathVariable Integer id, @RequestBody AddressDTO addressDTO){
        Optional<Address> byId = addressRepository.findById(id);
        Address address1 = byId.get();
        boolean exists = addressRepository.existsByCityAndDistrictAndStreet(
                addressDTO.getCity(), addressDTO.getDistrict(), addressDTO.getStreet());
        if (exists){
            return "Already exists";
        }
        address1.setCity(addressDTO.getCity());
        address1.setDistrict(addressDTO.getDistrict());
        address1.setStreet(addressDTO.getStreet());
        addressRepository.save(address1);
        return "Added Success";
    }
    @DeleteMapping("deleted/{id}")
    public String deleteIdAddress(@PathVariable Integer id) {
        Optional<Address> optionalGroups = addressRepository.findById(id);
        if (!optionalGroups.isPresent()) {
            return "address not found!";
        }
        addressRepository.deleteById(id);
        return "address deleted!";

    }

}
