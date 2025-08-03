package br.edu.infnet.carlos_araujo.adapters.in.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for password change request")
public class PasswordChangeDTO {

    @NotBlank(message = "Current password is required")
    @JsonProperty("current_password")
    @Schema(description = "Current password", example = "oldPassword123")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    @JsonProperty("new_password")
    @Schema(description = "New password", example = "newPassword456")
    private String newPassword;

    @NotBlank(message = "Password confirmation is required")
    @JsonProperty("confirm_password")
    @Schema(description = "Confirmation of new password", example = "newPassword456")
    private String confirmPassword;
}
