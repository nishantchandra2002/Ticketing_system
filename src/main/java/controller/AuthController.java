package controller;


import dto.*;
import service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CreateUserRequest req) {
        Set<String> roles = req.getRoles() == null || req.getRoles().isEmpty() ? Set.of("USER") : req.getRoles();
        String res = authService.register(req.getUsername(), req.getPassword(), roles);
        return ResponseEntity.ok(res);
    }
}
