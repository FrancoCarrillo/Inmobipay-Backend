package com.coderly.inmobipay.core.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    public Set<UserEntity> users = new HashSet<>();
}
