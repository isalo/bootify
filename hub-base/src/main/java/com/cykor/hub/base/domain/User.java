package com.cykor.hub.base.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String resetPasswordToken;

    @Column
    private OffsetDateTime resetPasswordTokenSentAt;

    @Column
    private String emailConfirmationToken;

    @Column
    private OffsetDateTime emailConfirmationTokenSentAt;

    @Column
    private Boolean emailConfirmed;

    @Column(length = 10)
    private String countryCode;

    @Column(length = 50)
    private String phone;

    @Column
    private String phoneConfirmationToken;

    @Column
    private OffsetDateTime phoneConfirmationTokenSentAt;

    @Column
    private Boolean phoneConfirmed;

    @Column
    private String profileImageUrl;

    @Column
    private Boolean isActive;

    @Column
    private Boolean isLocked;

    @ManyToMany
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private Set<LoginHistory> loginHistories;

    @ManyToMany
    @JoinTable(
            name = "UserCongregations",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "congregationId")
    )
    private Set<Congregation> congregations;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
