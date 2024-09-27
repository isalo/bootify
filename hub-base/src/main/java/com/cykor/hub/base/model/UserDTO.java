package com.cykor.hub.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
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

    private List<UUID> roles;

    private List<UUID> congregations;

}
