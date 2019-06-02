package com.example.demo.controller;

import com.example.demo.domain.Rezervare;
import com.example.demo.domain.Spectacol;
import com.example.demo.domain.SpectacolData;
import com.example.demo.domain.Spectator;
import com.example.demo.dto.RezervareDto;
import com.example.demo.dto.SpectacolDto;
import com.example.demo.service.AuthService;
import com.example.demo.service.RezervareService;
import com.example.demo.service.SpectacolService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/rez")
@RestController
public class RezervareController {
    private RezervareService rezervareService;
    private SpectacolService spectacolService;
    private AuthService authService;

    //id = id-ul spectacolData, pentru fiecare spectacolData o sa avem o lista de emitatori connectati la clientii care
    //au deschis pagina unde este spectacolData-ul respectiv
    private final Map<Long, List<SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

    @Autowired
    public RezervareController(RezervareService rezervareService, SpectacolService spectacolService, AuthService authService) {
        this.rezervareService = rezervareService;
        this.spectacolService = spectacolService;
        this.authService = authService;
    }

    // ~~~~~~~~~~~~~~~~~~~~~     REZERVARI     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PostMapping(value = "/rezervareByDate")
    public ResponseEntity<List<RezervareDto>> getRezervariByDate(@RequestBody ObjectNode objectNode){

        Long data =  Long.valueOf(objectNode.get("data").asText());
        Long idSpectacol = Long.valueOf(objectNode.get("idSpectacol").asText());

        Set<Rezervare> rezervari = rezervareService.getReservationsByDateAndSpecId(data, idSpectacol);
        List<RezervareDto> rezervareDtos = new ArrayList<>();

        for (Rezervare rezervare : rezervari) {
            rezervareDtos.add(rezervareDtoConverter(rezervare));
        }

        return new ResponseEntity<>(rezervareDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/rezervare/{id}")
    public ResponseEntity<List<RezervareDto>> getRezervareBySpectatorId(@PathVariable String id){
        Set<Rezervare> rezervari = rezervareService.getReservationBySpectatorId(id);
        List<RezervareDto> rezervareDtos = new ArrayList<>();

        for (Rezervare rezervare : rezervari) {
            rezervareDtos.add(rezervareDtoConverter(rezervare));
        }

        return new ResponseEntity<>(rezervareDtos, HttpStatus.OK);
    }

    @PostMapping(value = "/rezervare")
    public ResponseEntity<RezervareDto> addRezervare(@RequestBody ObjectNode objectNode)  {

        Integer pozitieX =  Integer.valueOf(objectNode.get("pozitieX").asText());
        Integer pozitieY =  Integer.valueOf(objectNode.get("pozitieY").asText());
        Integer nrScaun =  Integer.valueOf(objectNode.get("nrScaun").asText());
        Long idSpectacolData =  Long.valueOf(objectNode.get("idSpectacolData").asText());
        String idSpectator = objectNode.get("idSpectator").asText();

        SpectacolData spectacolData = spectacolService.getSpectacolData(idSpectacolData);
        Spectator spectator = authService.getSpectatorById(idSpectator);

        Rezervare rezervare = rezervareService.addRezervare(Rezervare.builder()
                .pozitieX(pozitieX)
                .pozitieY(pozitieY)
                .nrScaun(nrScaun)
                .spectacolDataMapat(spectacolData)
                .spectatorMapat(spectator)
                .build());

        ////// trimitem la fiecare SseEmitter care apartine SpectacolData-ului cu id-ul dat, mesajul de rezervare noua
        if(sseEmitters.containsKey(idSpectacolData)) {
            List<SseEmitter> list = sseEmitters.get(idSpectacolData);
            try {
                for(SseEmitter sseEmitter : list){
                    sseEmitter.send(rezervareDtoConverter(rezervare), MediaType.APPLICATION_JSON);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


        return new ResponseEntity<>(rezervareDtoConverter(rezervare), HttpStatus.OK);
    }

    @DeleteMapping(value = "/rezervare/{id}")
    public ResponseEntity<String> deleteRezervareById(@PathVariable Long id){
        rezervareService.deleteReservareById(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    // ~~~~~~~~~~~~~~~~~~~~~     SSE     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /*Dam subscribe la un SseEmitter pentru SpectacolData-ul cu id-ul precizat
    * dupa ce am dat subscribe, mergem in functia addRezervare si o sa trimitem un mesaj cu ajutorul SseEmitterului
    * atunci cand se face o rezervare pe SpectacolData-ul nostru
    */
    @GetMapping(value = "/subscribe/{id}")
    public SseEmitter subscribe(@PathVariable Long id) {

        if(!sseEmitters.containsKey(id)) {
            sseEmitters.put(id, new ArrayList<>());
        }

        SseEmitter sseEmitter = new SseEmitter(60 * 10000L); // timeout dupa 10minute
        sseEmitters.get(id).add(sseEmitter);

        return sseEmitter;
    }


    // ~~~~~~~~~~~~~~~~~~~~~     CONVERTERS     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private RezervareDto rezervareDtoConverter(Rezervare rezervare){
        return RezervareDto.builder()
                .id(rezervare.getId())
                .pozitieX(rezervare.getPozitieX())
                .pozitieY(rezervare.getPozitieY())
                .nrScaun(rezervare.getNrScaun())
                .idSpectacolData(rezervare.getSpectacolDataMapat().getId())
                .idSpectator(rezervare.getSpectatorMapat().getNume())
                .build();
    }
}
