package com.test.cabmanagement.controller;

import com.test.cabmanagement.service.CabBookingService;
import com.test.cabmanagement.dto.CabBookingDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BrokenBarrierException;

@RestController
@RequestMapping(value = "/book")
@Log4j2
public class BookingController {

    @Autowired
    private CabBookingService cabBookingService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody CabBookingDTO cabBookingDTO) throws BrokenBarrierException, InterruptedException {
        return new ResponseEntity<>(cabBookingService.bookCab(cabBookingDTO), HttpStatus.CREATED);
    }
}
