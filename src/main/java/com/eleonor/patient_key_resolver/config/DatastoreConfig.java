package com.eleonor.patient_key_resolver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

@Configuration
public class DatastoreConfig {

    private static final Logger logger = Logger.getLogger(DatastoreConfig.class.getName());

    @Value("${google.cloud.credentials.filepath}")
    private String credentialsPath;

    @Bean
    public Datastore datastore() throws IOException {
        try {
            System.out.println("Firestore init 1");
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            DatastoreOptions datastoreOptions = DatastoreOptions.newBuilder()
                    .setCredentials(credentials)
                    .build();

            return datastoreOptions.getService();

        } catch (Exception ex) {
            System.out.println("Firestore init 2");
            logger.info("Intentando cargar credenciales desde el classpath: " + credentialsPath);
            Resource resource = new ClassPathResource(credentialsPath);

            if (!resource.exists()) {
                String mensaje = "No se pudo encontrar el archivo de credenciales en: " + credentialsPath;
                logger.severe(mensaje);
                throw new IOException(mensaje);
            }

            logger.info("Archivo de credenciales encontrado, configurando Datastore...");
            GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

            DatastoreOptions datastoreOptions = DatastoreOptions.newBuilder()
                    .setCredentials(credentials)
                    .build();

            logger.info("Datastore configurado exitosamente");
            return datastoreOptions.getService();
        }
    }
}
