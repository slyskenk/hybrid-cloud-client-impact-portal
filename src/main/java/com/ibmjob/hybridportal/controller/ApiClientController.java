package com.ibmjob.hybridportal.controller;

import com.ibmjob.hybridportal.domain.ClientProfile;
import com.ibmjob.hybridportal.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ApiClientController {

    private static final Logger log = LoggerFactory.getLogger(ApiClientController.class);

    private final ClientService clientService;

    public ApiClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientProfile> list(@RequestParam(required = false) String q) {
        return clientService.search(q);
    }

    @GetMapping("/{id}")
    public ClientProfile one(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientProfile create(@Valid @RequestBody ClientProfile clientProfile) {
        log.info("Creating client profile name={}", clientProfile.getName());
        return clientService.save(clientProfile);
    }
}
