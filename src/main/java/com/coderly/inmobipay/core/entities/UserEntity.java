package com.coderly.inmobipay.core.entities;

import javax.persistence.*;

import lombok.*;

import java.util.*;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String names;
    private String lastNames;
    private String email;
    private Integer age;
    private String dni;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<CreditEntity> credits;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_rol",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public Set<String> getRolName() {
        Set<String> rolesName = new HashSet<>();

        roles.forEach(role -> {
            rolesName.add(role.getName());
        });

        return rolesName;
    }
}
