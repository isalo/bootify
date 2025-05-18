package com.cykor.hub.base.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationSocialRequest {

    @NotNull
    private String code;

}
