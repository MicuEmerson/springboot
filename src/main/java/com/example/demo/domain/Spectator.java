package com.example.demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Builder
public class Spectator {

    @Id
    private String nume;
    private String password;

    @OneToMany(mappedBy = "spectatorMapat")
    private Set<Rezervare> rezervari;
}
