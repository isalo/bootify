package com.cykor.hub.base.rest;

import com.cykor.hub.base.model.CongregationDTO;
import com.cykor.hub.base.service.CongregationService;
import com.cykor.hub.base.util.UserRoles;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/congregations", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRoles.ROLE_ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class CongregationResource {

    private final CongregationService congregationService;

    public CongregationResource(final CongregationService congregationService) {
        this.congregationService = congregationService;
    }

    @GetMapping
    public ResponseEntity<List<CongregationDTO>> getAllCongregations() {
        return ResponseEntity.ok(congregationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CongregationDTO> getCongregation(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(congregationService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCongregation(
            @RequestBody @Valid final CongregationDTO congregationDTO) {
        final UUID createdId = congregationService.create(congregationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCongregation(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CongregationDTO congregationDTO) {
        congregationService.update(id, congregationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCongregation(@PathVariable(name = "id") final UUID id) {
        congregationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
