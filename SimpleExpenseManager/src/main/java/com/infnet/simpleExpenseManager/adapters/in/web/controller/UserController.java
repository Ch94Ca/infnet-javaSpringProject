package com.infnet.simpleExpenseManager.adapters.in.web.controller;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.domain.user.User;
import com.infnet.simpleExpenseManager.application.port.in.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")

@Tag(name = "User", description = "Users endpoints")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

   @PostMapping("/create")
   @ResponseStatus(HttpStatus.CREATED)
   @Operation(
           summary = "Create an new user",
           description = "Receives new user data, validates it, and persists it to the system.",
           responses = {
                   @ApiResponse(responseCode = "201", description = "User successfully created",
                           content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                   @ApiResponse(responseCode = "400", description = "Invalid Data", content = @Content)
           }
   )
   public User createUser(@Valid @RequestBody UserCreateDTO userDTO) {
       return userService.createUser(userDTO);
   }
}
