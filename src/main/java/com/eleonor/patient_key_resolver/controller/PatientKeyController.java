package com.eleonor.patient_key_resolver.controller;

import com.googlecode.objectify.Key;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient/resolve-key")
@CrossOrigin(origins = "*")
public class PatientKeyController {

    @GetMapping(value = "/{pk}", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> resolvePatientKey(@PathVariable("pk") String pk) {
        if (pk == null || pk.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Key<?> key = Key.create(pk);
            Long id = key.getId();
            if (id == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(id.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
