package br.edu.infnet.CarlosAraujo.adapters.in.web.controller;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.mapper.UserDtoMapper;
import br.edu.infnet.CarlosAraujo.application.port.in.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "Users endpoints")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @PatchMapping("/me")
    @Operation(
            summary = "Update my own user data",
            description = "Updates the data for the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully Updated",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Email not found", content = @Content)
            }
    )
    public ResponseEntity<UserResponseDTO> updateUserDataByEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserDataUpdateDTO userDTO) {
        String userEmail = userDetails.getUsername();
        UserResponseDTO result = userDtoMapper.toResponseDto(
                userService.updateUserData(
                        userEmail,
                        userDtoMapper.toUserUpdateCommand(userDTO)
                )
        );
        return ResponseEntity.ok(result);
    }
}
