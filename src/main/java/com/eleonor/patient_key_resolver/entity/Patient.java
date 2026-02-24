package com.eleonor.patient_key_resolver.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;
import com.overcome.entity.User;

@Subclass
public class Patient extends User {

    public Patient() {
        super();
    }

    @Override
    public Key<Patient> getKey() {
        return Key.create(Patient.class, getId());
    }
}
