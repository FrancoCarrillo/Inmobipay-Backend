package com.coderly.inmobipay.core.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity(name = "rol")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;

    private String Name;

    @ManyToMany(mappedBy = "roles")
    public Set<UserEntity> users = new HashSet<>();
}
