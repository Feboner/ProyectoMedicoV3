package com.medico.auth.controller;

import com.medico.auth.dto.AuthResponse;
import com.medico.auth.dto.LoginRequest;
import com.medico.auth.dto.RegisterRequest;
import com.medico.auth.model.Usuario;
import com.medico.auth.service.JwtService;
import com.medico.auth.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints de login y registro")
public class AuthController {
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y retorna JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());
        
        Usuario usuario = usuarioService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, null, "Credenciales inválidas"));
        }

        String token = jwtService.generateToken(usuario.getUsername(), usuario.getId());
        log.info("Login exitoso para usuario: {}", usuario.getUsername());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .userId(usuario.getId())
                .message("Login exitoso")
                .build());
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Usuario o email ya existe")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());
        
        try {
            Usuario usuario = usuarioService.registrar(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getNombre(),
                    request.getApellido()
            );

            String token = jwtService.generateToken(usuario.getUsername(), usuario.getId());
            log.info("Usuario registrado exitosamente: {}", usuario.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AuthResponse.builder()
                            .token(token)
                            .username(usuario.getUsername())
                            .email(usuario.getEmail())
                            .userId(usuario.getId())
                            .message("Usuario registrado exitosamente")
                            .build());
        } catch (IllegalArgumentException e) {
            log.warn("Error registrando usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, null, null, e.getMessage()));
        }
    }

    @GetMapping("/validate/{token}")
    @Operation(summary = "Validar JWT", description = "Valida un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido"),
            @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    public ResponseEntity<Boolean> validateToken(@PathVariable String token) {
        boolean isValid = jwtService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
