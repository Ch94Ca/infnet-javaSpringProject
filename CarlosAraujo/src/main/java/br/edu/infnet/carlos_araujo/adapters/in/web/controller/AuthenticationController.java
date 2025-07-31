package br.edu.infnet.carlos_araujo.adapters.in.web.controller;

import br.edu.infnet.carlos_araujo.adapters.in.web.dto.UserCreateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.auth.AuthResponseDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.auth.LoginRequestDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.mapper.UserDtoMapper;
import br.edu.infnet.carlos_araujo.application.service.security.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserCreateDTO userCreateDto) {
        return ResponseEntity.ok(
                userDtoMapper.toResponseDto(
                        service.register(
                                userDtoMapper.toUserCreateCommand(userCreateDto)
                        )
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = service.authenticate(
                userDtoMapper.toLoginCommand(loginRequestDTO)
        );

        return ResponseEntity.ok(
                new AuthResponseDTO(token)
        );
    }
}
