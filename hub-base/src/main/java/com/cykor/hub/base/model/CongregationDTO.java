package com.cykor.hub.base.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CongregationDTO {

    private UUID id;

    @NotNull
    private Integer number;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String country;

    @Size(max = 255)
    private String state;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String address;

    private Double latitude;

    private Double longitude;

}
