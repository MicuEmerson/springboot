package com.example.demo.service;

import com.example.demo.domain.Rezervare;
import com.example.demo.domain.SpectacolData;
import com.example.demo.repository.RezervareRepository;
import com.example.demo.repository.SpectacolDataRepository;
import com.example.demo.repository.SpectatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class RezervareService {

    private SpectatorRepository spectatorRepository;
    private RezervareRepository rezervareRepository;
    private SpectacolDataRepository spectacolDataRepository;

    @Autowired
    public RezervareService(SpectatorRepository spectatorRepository, RezervareRepository rezervareRepository, SpectacolDataRepository spectacolDataRepository){
        this.spectatorRepository = spectatorRepository;
        this.rezervareRepository = rezervareRepository;
        this.spectacolDataRepository = spectacolDataRepository;
    }

    /**
     * Apelam un query facut de noi (getSpectacolDataByDate) si ne poate returna doar un singur spectacol pentru ca
     * nu se poate juca acelasi spectacol la aceeasi ora.
     * Cu spectacolul obtinut ne putem lua lista lui de rezervari.
     *
     * @param date data la care se joaca spectacolul cu id-ul dat
     * @param idSpectacol id-ul spectacolului
     * @return lista de rezervari care corespunde spectacolului din data respectiva
     */
    public Set<Rezervare> getReservationsByDateAndSpecId(Long date, Long idSpectacol){
        SpectacolData spectacolData = spectacolDataRepository.getSpectacolDataByDate(date, idSpectacol); // poate da null
        return spectacolData.getRezervari();
    }

    public Set<Rezervare> getReservationBySpectatorId(String idSpectator){
        return spectatorRepository.findById(idSpectator).get().getRezervari();
    }

    public Rezervare addRezervare(Rezervare rezervare){
        return rezervareRepository.save(rezervare);
    }

    public void deleteReservareById(Long idRezervare){
        rezervareRepository.deleteById(idRezervare);
    }
}
