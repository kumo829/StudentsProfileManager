package com.javatutoriales.profiles.account.persistence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;


@Data
@Table("accounts")
@Builder
public class UserEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("created_on")
    private ZonedDateTime createdOn;

    @Column("disabled")
    private boolean disabled;

    @Column("disabled_on")
    private ZonedDateTime disabledOn;

    @Column("deleted")
    private boolean deleted;

    @Column("deleted_on")
    private ZonedDateTime deletedOn;
}
