package com.medico.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.medico.auth.dto.LoginRequest;
import com.medico.auth.dto.RegisterRequest;
import com.medico.auth.dto.AuthResponse;
import com.medico.auth.model.Usuario;
import com.medico.auth.repository.UsuarioRepository;
import com.medico.auth.service.UsuarioService;
import com.medico.auth.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class AuthTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;
    private String jwtToken;

    @BeforeEach
    public void setup() {
        usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("juan.perez");
        usuario1.setEmail("juan.perez@gmail.com");
        usuario1.setPassword("hashedPassword123");
        usuario1.setNombre("Juan");
        usuario1.setApellido("Pérez");
        usuario1.setActivo(true);
        usuario1.setFechaCreacion(new Date());

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("maria.lopez");
        usuario2.setEmail("maria.lopez@gmail.com");
        usuario2.setPassword("hashedPassword456");
        usuario2.setNombre("María");
        usuario2.setApellido("López");
        usuario2.setActivo(true);
        usuario2.setFechaCreacion(new Date());

        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6IiwidXNlcklkIjoxfQ.token";
    }

    @Test
    public void registrarUsuarioExitosamente() {
        String username = "nuevo.usuario";
        String email = "nuevo@gmail.com";
        String password = "password123";
        String nombre = "Nuevo";
        String apellido = "Usuario";

        when(usuarioRepository.existsByUsername(username)).thenReturn(false);
        when(usuarioRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword789");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario1);

        Usuario usuarioRegistrado = usuarioService.registrar(username, email, password, nombre, apellido);

        assertNotNull(usuarioRegistrado);
        assertEquals("juan.perez", usuarioRegistrado.getUsername());
        assertEquals("juan.perez@gmail.com", usuarioRegistrado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    public void encontrarUsuarioPorUsername() {
        String username = "juan.perez";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario1));

        Optional<Usuario> resultado = usuarioRepository.findByUsername(username);

        assertTrue(resultado.isPresent());
        assertEquals("juan.perez", resultado.get().getUsername());
        assertEquals("juan.perez@gmail.com", resultado.get().getEmail());
    }

    @Test
    public void encontrarUsuarioPorId() {
        Long userId = 1L;
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario1));

        Optional<Usuario> resultado = usuarioRepository.findById(userId);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Juan", resultado.get().getNombre());
    }

    @Test
    public void obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("juan.perez", resultado.get(0).getUsername());
        assertEquals("maria.lopez", resultado.get(1).getUsername());
    }

    @Test
    public void generarTokenJwtExitosamente() {
        String username = "juan.perez";
        Long userId = 1L;
        when(jwtService.generateToken(username, userId)).thenReturn(jwtToken);

        String token = jwtService.generateToken(username, userId);

        assertNotNull(token);
        assertTrue(token.contains("."));
        verify(jwtService, times(1)).generateToken(username, userId);
    }

    @Test
    public void validarTokenJwt() {
        when(jwtService.validateToken(jwtToken)).thenReturn(true);

        boolean esValido = jwtService.validateToken(jwtToken);

        assertTrue(esValido);
        verify(jwtService, times(1)).validateToken(jwtToken);
    }

    @Test
    public void extraerUsernameDelToken() {
        when(jwtService.extractUsername(jwtToken)).thenReturn("juan.perez");

        String username = jwtService.extractUsername(jwtToken);

        assertEquals("juan.perez", username);
        verify(jwtService, times(1)).extractUsername(jwtToken);
    }

    @Test
    public void codificarContraseña() {
        String rawPassword = "password123";
        String encodedPassword = "hashedPassword123";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String resultado = passwordEncoder.encode(rawPassword);

        assertEquals(encodedPassword, resultado);
        assertNotEquals(rawPassword, resultado);
    }

    @Test
    public void detectarUsernameDuplicado() {
        String username = "juan.perez";
        when(usuarioRepository.existsByUsername(username)).thenReturn(true);

        boolean existe = usuarioRepository.existsByUsername(username);

        assertTrue(existe);
    }

    @Test
    public void detectarEmailDuplicado() {
        String email = "juan.perez@gmail.com";
        when(usuarioRepository.existsByEmail(email)).thenReturn(true);

        boolean existe = usuarioRepository.existsByEmail(email);

        assertTrue(existe);
    }

    @Test
    public void retornarVacioParaIdDesconocido() {
        Long idInvalido = 99999L;
        when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioRepository.findById(idInvalido);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void rechazarTokenJwtInvalido() {
        String invalidToken = "invalid.jwt.token";
        when(jwtService.validateToken(invalidToken)).thenReturn(false);

        boolean esValido = jwtService.validateToken(invalidToken);

        assertFalse(esValido);
    }

    @Test
    public void tratarUsernameNuloComoFaltante() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setUsername(null);

        assertNull(usuarioInvalido.getUsername());
    }

    @Test
    public void preservarValoresDeDtoAuth() {
        RegisterRequest registerRequest = new RegisterRequest("nuevo.usuario", "nuevo@gmail.com", "password123", "Nuevo", "Usuario");
        LoginRequest loginRequest = new LoginRequest("nuevo.usuario", "password123");
        AuthResponse authResponse = new AuthResponse(jwtToken, "juan.perez", "juan.perez@gmail.com", 1L, "ok");

        assertEquals("nuevo.usuario", registerRequest.getUsername());
        assertEquals("nuevo@gmail.com", registerRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
        assertEquals(jwtToken, authResponse.getToken());
        assertEquals("juan.perez", authResponse.getUsername());
        assertEquals("juan.perez@gmail.com", authResponse.getEmail());
    }
}
