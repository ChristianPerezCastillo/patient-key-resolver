package com.eleonor.patient_key_resolver.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.googlecode.objectify.Key;

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


    /**
     * Payload esperado por BigQuery Remote Functions:
     * {
     *   "calls": [
     *     ["<pk1>"],
     *     ["<pk2>"]
     *   ]
     * }
     *
     * Respuesta esperada:
     * {
     *   "replies": [12345, 67890]
     * }
     */
    @PostMapping(
            value = "/resolve-keys",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BatchResponse> resolvePatientKeys(@RequestBody BatchRequest request) {

        if (request == null || request.getCalls() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Long> replies = new ArrayList<>(request.getCalls().size());

        for (List<String> callArgs : request.getCalls()) {
            // Cada "call" es una lista con los args de la función remota.
            // Para 1 argumento: ["pk"]
            Long resolved = null;

            try {
                String pk = (callArgs != null && !callArgs.isEmpty()) ? callArgs.get(0) : null;

                if (pk == null || pk.isBlank()) {
                    // Mantén null para esta fila
                    resolved = null;
                } else {
                    Key<?> key = Key.create(pk);
                    Long id = key.getId();
                    resolved = id; // puede ser null si no es un id numérico
                }
            } catch (Exception ignored) {
                // Si hay error de parseo, devolvemos null para esta fila
                resolved = null;
            }

            replies.add(resolved);
        }

        return ResponseEntity.ok(new BatchResponse(replies));
    }

    // ===== DTOs =====

    public static class BatchRequest {
        private List<List<String>> calls;

        public List<List<String>> getCalls() {
            return calls;
        }

        public void setCalls(List<List<String>> calls) {
            this.calls = calls;
        }
    }

    public static class BatchResponse {
        private List<Long> replies;

        public BatchResponse() {}

        public BatchResponse(List<Long> replies) {
            this.replies = replies;
        }

        public List<Long> getReplies() {
            return replies;
        }

        public void setReplies(List<Long> replies) {
            this.replies = replies;
        }
    }
}
