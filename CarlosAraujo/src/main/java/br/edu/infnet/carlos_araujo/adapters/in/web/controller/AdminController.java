package br.edu.infnet.carlos_araujo.adapters.in.web.controller;

import br.edu.infnet.carlos_araujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.UserRoleUpdateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.mapper.UserDtoMapper;
import br.edu.infnet.carlos_araujo.application.port.in.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")

@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin endpoints")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserDtoMapper userDtoMapper;

    @DeleteMapping("users/{userEmail}")
    @Operation(
            summary = "Remove an existing user",
            description = "Receives an existing user email and removes it from system.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User successfully Removed",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Email not found", content = @Content)
            }
    )
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String userEmail) {
        adminService.deleteUserByEmail(userEmail);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("users/{userEmail}")
    @Operation(
            summary = "Update an existing user",
            description = "Receives an existing user email and update it from system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully Updated",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Email not found", content = @Content)
            }
    )
    public ResponseEntity<UserResponseDTO> updateUserDataByEmail(@PathVariable String userEmail, @Valid @RequestBody UserDataUpdateDTO userDTO) {
        UserResponseDTO result = userDtoMapper.toResponseDto(
                adminService.updateUserData(
                        userEmail,
                        userDtoMapper.toUserUpdateCommand(userDTO)
                )
        );
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{userEmail}/role")
    @Operation(
            summary = "Change a user's role",
            description = "Updates the role of a specific user (e.g., from ROLE_USER to ROLE_ADMIN).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User role successfully updated"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid role provided")
            }
    )
    public ResponseEntity<UserResponseDTO> changeUserRole(
            @PathVariable String userEmail,
            @Valid @RequestBody UserRoleUpdateDTO roleUpdateDTO
    ) {
        UserResponseDTO result = userDtoMapper.toResponseDto(adminService.changeUserRole(userEmail, roleUpdateDTO.newRole()));
        return ResponseEntity.ok(result);
    }
}
