package com.test.cabmanagement.service;

import com.test.cabmanagement.dto.CabBookingDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CabBookingServiceImpl implements CabBookingService{
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
    private static BlockingQueue<CabBookingDTO> blockingMaleQueue = new LinkedBlockingDeque();
    private static BlockingQueue<CabBookingDTO> blockingFemaleQueue = new LinkedBlockingDeque();
    private static Semaphore blockingMaleSemaphore = new Semaphore(-1,true);
    private static Semaphore blockingFemaleSemaphore = new Semaphore(-1,true);
    @Override
    public CabBookingDTO bookCab(CabBookingDTO cabBookingDTO) throws BrokenBarrierException, InterruptedException {
        if(cabBookingDTO.getUserName().equals("M"))
            blockingMaleQueue.add(cabBookingDTO);
        if(cabBookingDTO.getUserName().equals("F"))
            blockingFemaleQueue.add(cabBookingDTO);
        log.info("semaphore count- {} - {} queue male-{} female-{}",blockingMaleSemaphore.getQueueLength(),blockingFemaleSemaphore.getQueueLength()
                ,blockingMaleQueue.size(),blockingFemaleQueue.size());

        if((blockingMaleQueue.size() + blockingFemaleQueue.size())==4){
            Map<String, Long> maleCounts =blockingMaleQueue.stream().collect(Collectors.groupingBy(b -> b.getUserName(),Collectors.counting()));
            Map<String, Long> femaleCounts =blockingFemaleQueue.stream().collect(Collectors.groupingBy(b -> b.getUserName(),Collectors.counting()));
            boolean condition = ( maleCounts.get("M")==4) || (femaleCounts.get("F")==4)
                    || (femaleCounts.get("F") ==2 && maleCounts.get("M")==2);
            if(condition) {
                log.info("condition satisfied with 4");

                if(maleCounts.get("M")==4){
                    for (CabBookingDTO cabBookingDTOData : blockingMaleQueue) {
                        //log.info("size -{}", blockingQueue.size());
                        //log.info(cabBookingDTOData.getUserName());
                        //cyclic barrier for all 4 male or female or 2M -2F
                        //cyclicBarrier.await();
                        //log.info("booked");
                        //cyclic barrier
                        blockingMaleQueue.remove(cabBookingDTOData);
                        blockingMaleSemaphore.release();

                    }
                    blockingMaleSemaphore.release();
                }

                else if(femaleCounts.get("F")==4){
                    for (CabBookingDTO cabBookingDTOData : blockingFemaleQueue) {
                        //log.info("size -{}", blockingQueue.size());
                        //log.info(cabBookingDTOData.getUserName());
                        //cyclic barrier for all 4 male or female or 2M -2F
                        //cyclicBarrier.await();
                        //log.info("booked");
                        //cyclic barrier
                        blockingFemaleQueue.remove(cabBookingDTOData);
                        blockingFemaleSemaphore.release();

                    }
                    blockingFemaleSemaphore.release();
                }

                else if(maleCounts.get("M")==2 && femaleCounts.get("F")==2){

                    for(int i=0;i<2;i++){
                        blockingFemaleQueue.remove(blockingFemaleQueue.peek());
                        blockingFemaleSemaphore.release();
                    }

                    for(int i=0;i<2;i++){
                        blockingMaleQueue.remove(blockingMaleQueue.peek());
                        blockingMaleSemaphore.release();
                    }
                    blockingMaleSemaphore.release();
                    blockingFemaleSemaphore.release();
                }
            }

        }

        if((blockingMaleQueue.size() +  blockingFemaleQueue.size() ) > 4){
            log.info("condition satisfied > 4");
            if(cabBookingDTO.getUserName().equals("M")) {
                Map<String, Long> maleCounts =blockingMaleQueue.stream().collect(Collectors.groupingBy(b -> b.getUserName(),Collectors.counting()));
                Map<String, Long> femaleCounts =blockingFemaleQueue.stream().collect(Collectors.groupingBy(b -> b.getUserName(),Collectors.counting()));
                if(maleCounts.get(cabBookingDTO.getUserName())==4){
                    for(int i=0;i<4;i++){
                        blockingMaleQueue.remove(blockingMaleQueue.peek());
                        blockingMaleSemaphore.release();
                    }
                    blockingMaleSemaphore.release();
                }
                else{
                    for(int i=0;i<2;i++){
                        blockingMaleQueue.remove(blockingMaleQueue.peek());
                        blockingFemaleQueue.remove(blockingFemaleQueue.peek());
                        blockingMaleSemaphore.release();
                        blockingFemaleSemaphore.release();
                    }
                    blockingMaleSemaphore.release();
                    blockingFemaleSemaphore.release();
                }

            }

            if(cabBookingDTO.getUserName().equals("F")) {
                Map<String, Long> maleCounts =blockingMaleQueue.stream().collect(Collectors.groupingBy(b -> b.getUserName(),Collectors.counting()));
                Map<String, Long> femaleCounts =blockingFemaleQueue.stream().collect(Collectors.groupingBy(b -> b.getUserName(),Collectors.counting()));
                if(femaleCounts.get(cabBookingDTO.getUserName())==4){
                    for(int i=0;i<4;i++){
                        blockingFemaleQueue.remove(blockingFemaleQueue.peek());
                        blockingFemaleSemaphore.release();
                    }
                    blockingFemaleSemaphore.release();
                }
                else{
                    for(int i=0;i<2;i++){
                        blockingMaleQueue.remove(blockingMaleQueue.peek());
                        blockingFemaleQueue.remove(blockingFemaleQueue.peek());
                        blockingMaleSemaphore.release();
                        blockingFemaleSemaphore.release();
                    }
                    blockingMaleSemaphore.release();
                    blockingFemaleSemaphore.release();
                }

            }
            return null;
        }


        //cyclicBarrier.await();

        if(cabBookingDTO.getUserName().equals("M")) {
            log.info("calling semaphore aquire for M");
            blockingMaleSemaphore.acquire();
        }
        if(cabBookingDTO.getUserName().equals("F")) {
            log.info("calling semaphore aquire for F");
            blockingFemaleSemaphore.acquire();
        }
        log.info("semaphore count after- {} - {}",blockingMaleSemaphore.getQueueLength(),blockingFemaleSemaphore.getQueueLength());

        return null;
    }
}
