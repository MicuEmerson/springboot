package com.example.demo.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class SpectacolData extends BaseEntity<Long>{

    private Float pret;
    private Date data;

    @ManyToOne
    private Spectacol spectacolMapat;

    @OneToMany(mappedBy = "spectacolDataMapat")
    private Set<Rezervare> rezervari;
}
