package com.javatutoriales.profiles.account.controllers.vos;

import javax.validation.constraints.NotBlank;

public record UserRequest(@NotBlank String firstName,
                          @NotBlank String lastName,
                          @NotBlank String username,
                          @NotBlank String password) {
}
