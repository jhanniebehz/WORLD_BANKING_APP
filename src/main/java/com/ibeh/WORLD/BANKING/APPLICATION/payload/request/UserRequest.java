package com.ibeh.WORLD.BANKING.APPLICATION.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "First Name must not be blank")
    @Size(min = 2, max =125, message = "First Name must be atleast 2 charcters long")
    private String firstName;

    private String lastName;

    private String otherName;

    private String gender;

    private String email;

    private String password;

    private String address;

    private String stateOfOrigin;

    private String BVN;

    private String phoneNumber;

    private String pin;

}
