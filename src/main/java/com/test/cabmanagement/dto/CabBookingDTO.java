package com.test.cabmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CabBookingDTO {

    Long id;
    String userName;

    @Override
    public String toString() {
        return "CabBookingDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
