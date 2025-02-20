package com.example.bff_aws.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class BffController {

    private final RestTemplate restTemplate;

    @Value("${microservice.pacientes.base.url}")
    private String pacientesServiceUrl;

    @Value("${microservice.reservas.base.url}")
    private String reservasServiceUrl;

    public BffController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // GET: Obtener todos los pacientes
    @GetMapping("/pacientes")
    public ResponseEntity<String> getPacientes() {
        String url = pacientesServiceUrl;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pacientes")
    public ResponseEntity<String> createPaciente(@RequestBody String paciente) {
        String url = pacientesServiceUrl;

        // Crear la solicitud con encabezados correctos
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Establecer Content-Type a JSON
        HttpEntity<String> request = new HttpEntity<>(paciente, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error del cliente: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error del servidor: " + e.getMessage());
        }
    }




    @GetMapping("/pacientes/{id}")
    public String getPacienteById(@PathVariable Long id) {
        String url = pacientesServiceUrl + "/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    @PutMapping("/pacientes/{id}")
    public ResponseEntity<String> updatePaciente(@PathVariable Long id, @RequestBody String paciente) {
        String url = pacientesServiceUrl + "/" + id;

        // Crear la solicitud con encabezados correctos
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Establecer Content-Type a JSON
        HttpEntity<String> request = new HttpEntity<>(paciente, headers);

        try {
            // Enviar la solicitud PUT
            restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            return ResponseEntity.ok("Paciente actualizado con ID: " + id);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error del cliente: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error del servidor: " + e.getMessage());
        }
    }


    @DeleteMapping("/pacientes/{id}")
    public String deletePaciente(@PathVariable Long id) {
        String url = pacientesServiceUrl + "/" + id;
        restTemplate.delete(url);
        return "Paciente eliminado con ID: " + id;
    }




    @PostMapping("/reservas/horaMedica")
    public ResponseEntity<String> reservarCita(@RequestBody String reservaJson) {
        String url = reservasServiceUrl + "/horaMedica";
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(reservaJson, headers);
    
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error del cliente: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error del servidor: " + e.getMessage());
        }
    }
    
    }
