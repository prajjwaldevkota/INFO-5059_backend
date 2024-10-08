package com.info5059.casestudy.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin
@RestController
public class VendorController {
    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping("api/vendors")
    public ResponseEntity<Iterable<Vendor>> findAll() {
        Iterable<Vendor> vendors = vendorRepository.findAll();
        return new ResponseEntity<Iterable<Vendor>>(vendors, HttpStatus.OK);
    }

    @PutMapping("api/vendors")
    public ResponseEntity<Vendor> updateOne(@RequestBody Vendor vendor) {
        Vendor updatedVendor = vendorRepository.save(vendor);
        return new ResponseEntity<Vendor>(updatedVendor, HttpStatus.OK);
    }

    @PostMapping("/api/vendors")
    public ResponseEntity<Vendor> addOne(@RequestBody Vendor employee) {
        Vendor newEmployee = vendorRepository.save(employee);
        return new ResponseEntity<Vendor>(newEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/api/vendors/{id}")
    public ResponseEntity<Integer> deleteOne(@PathVariable long id) {
        return new ResponseEntity<Integer>(vendorRepository.deleteOne(id), HttpStatus.OK);
    }

}
