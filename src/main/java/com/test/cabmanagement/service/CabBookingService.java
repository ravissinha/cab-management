package com.test.cabmanagement.service;

import com.test.cabmanagement.dto.CabBookingDTO;

import java.util.concurrent.BrokenBarrierException;

public interface CabBookingService {

    CabBookingDTO bookCab(CabBookingDTO cabBookingDTO) throws BrokenBarrierException, InterruptedException;

}
