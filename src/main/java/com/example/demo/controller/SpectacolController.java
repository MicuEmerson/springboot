package com.example.demo.controller;

import com.example.demo.service.SpectacolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/spectacol")
@RestController
public class SpectacolController {

    private SpectacolService spectacolService;

    @Autowired
    public SpectacolController(SpectacolService spectacolService) {
        this.spectacolService = spectacolService;
    }


}
