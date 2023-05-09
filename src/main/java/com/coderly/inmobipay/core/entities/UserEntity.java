package com.coderly.inmobipay.core.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;
    private String username;
    private String names;
    private String lastNames;
    private String email;
    private Integer age;
    private String dni;
    private String password;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_rol",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public Set<String> getRolName(){
        Set<String> rolesName = new HashSet<>();

        roles.forEach(role -> {
            rolesName.add(role.getName());
        });

        return rolesName;
    }
}
