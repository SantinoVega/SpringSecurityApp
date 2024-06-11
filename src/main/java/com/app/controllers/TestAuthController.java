package com.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/method")
@PreAuthorize("denyAll()")
public class TestAuthController {

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello World GET!");
    }

    @PostMapping("/post")
    @PreAuthorize("hasAnyAuthority('CREATE','UPDATE')")
    public ResponseEntity<String> post() {
        return ResponseEntity.ok("Hello World POST");
    }

    @PutMapping("/put")
    @PreAuthorize("hasAnyAuthority('CREATE', 'UPDATE', 'REFACTOR')")
    public ResponseEntity<String> put() {
        return ResponseEntity.ok("Hello World con PUT");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('CREATE','UPDATE','DELETE')")
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok("Hello World DELETE");
    }

    @PatchMapping("/patch")
    @PreAuthorize("hasAuthority('REFACTOR')")
    public ResponseEntity<String> patch() {
        return ResponseEntity.ok("Hello World PATCH");
    }
}
