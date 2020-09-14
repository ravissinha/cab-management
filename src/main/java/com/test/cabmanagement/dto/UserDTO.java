package com.test.cabmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {

   // @NotNull(message ="user name can't be null")
    Long id;
    String userName;
    //@NotNull(message ="firstName can't be null")
    String firstName;
    //@NotNull(message ="lastName can't be null")
    String lastName;
    //@NotNull(message ="gender can't be null")
    String gender;
    //@NotNull(message ="email can't be null")
    String email;
    //@NotNull(message ="phoneNumber can't be null")
    Long phoneNumber;
    String password;

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
