package com.example.demo.service;

import com.example.demo.domain.Spectacol;
import com.example.demo.domain.SpectacolData;
import com.example.demo.repository.ManagerRepository;
import com.example.demo.repository.SpectacolDataRepository;
import com.example.demo.repository.SpectacolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class SpectacolService {

    private SpectacolRepository spectacolRepository;
    private SpectacolDataRepository spectacolDataRepository;

    @Autowired
    public SpectacolService(SpectacolRepository spectacolRepository, SpectacolDataRepository spectacolDataRepository){
        this.spectacolRepository = spectacolRepository;
        this.spectacolDataRepository = spectacolDataRepository;
    }

    // ~~~~~~~~~~~~~~~~~~~~~     SPECTACOL      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public List<Spectacol> getAllSpectacole(){
        return spectacolRepository.findAll();
    }

    public Spectacol getSpectacol(Long id){
        return spectacolRepository.findById(id).get();
    }

    public Spectacol addSpectacol(Spectacol spectacol){
        return spectacolRepository.save(spectacol);
    }

    public void deleteSpectacol(Long id){
        spectacolRepository.deleteById(id);
    }

    @Transactional
    public Spectacol updateSpectacol(Spectacol spectacol){
        spectacolRepository.deleteById(spectacol.getId());
        return spectacolRepository.save(spectacol);
    }

    // ~~~~~~~~~~~~~~~~~~~~~     SPECTACOLDATA     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public List<SpectacolData> getAllSpectacoleData(){
        return spectacolDataRepository.findAll();
    }

    public SpectacolData getSpectacolData(Long id){
        return spectacolDataRepository.findById(id).get();
    }

    public SpectacolData addSpectacolData(SpectacolData spectacolData){
        return spectacolDataRepository.save(spectacolData);
    }

    public void deleteSpectacolData(Long id){
        spectacolDataRepository.deleteById(id);
    }

    @Transactional
    public SpectacolData updateSpectacol(SpectacolData spectacolData){
        spectacolDataRepository.deleteById(spectacolData.getId());
        return spectacolDataRepository.save(spectacolData);
    }

}
