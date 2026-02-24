package com.eleonor.patient_key_resolver.config;

import com.google.cloud.datastore.Datastore;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.eleonor.patient_key_resolver.entity.Patient;
import com.overcome.entity.User;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectifyConfig {

    @Bean
    public ObjectifyFactory objectifyFactory(Datastore datastore) {
        ObjectifyFactory factory = new ObjectifyFactory(datastore);
        ObjectifyService.init(factory);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Patient.class);
        return factory;
    }

    @Bean
    public FilterRegistrationBean<ObjectifyFilter> objectifyFilter() {
        FilterRegistrationBean<ObjectifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ObjectifyFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
