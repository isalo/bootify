package com.cykor.hub.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    @UserEmailUnique(message = "{registration.register.taken}")
    private String email;

    @NotNull
    @Size(max = 72)
    private String password;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String resetPasswordToken;

    private OffsetDateTime resetPasswordTokenSentAt;

    @Size(max = 255)
    private String emailConfirmationToken;

    private OffsetDateTime emailConfirmationTokenSentAt;

    private Boolean emailConfirmed;

    @Size(max = 10)
    private String countryCode;

    @Size(max = 50)
    private String phone;

    @Size(max = 255)
    private String phoneConfirmationToken;

    private OffsetDateTime phoneConfirmationTokenSentAt;

    private Boolean phoneConfirmed;

    @Size(max = 255)
    private String profileImageUrl;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isLocked")
    private Boolean isLocked;

}
