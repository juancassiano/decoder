package com.ead.authuser.dtos;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.ead.authuser.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public interface UserView{
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }
    
    private UUID userId;

    @NotBlank(message = "Username cannot be blank", groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters", groups = UserView.RegistrationPost.class)
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String username;
    
    @NotBlank(message = "Email cannot be blank", groups = UserView.RegistrationPost.class)
    @Email(message = "Email is invalid", groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;
    
    @NotBlank(message = "Password cannot be blank", groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters", groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;
   
    @NotBlank(message = "Old password cannot be blank", groups = UserView.PasswordPut.class)
    @Size(min = 6, max = 20, message = "Old password must be between 6 and 20 characters", groups = UserView.PasswordPut.class)
    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;
   
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;
    
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String phoneNumber;
    
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String cpf;
    
    @NotBlank(message = "Image URL cannot be blank", groups = UserView.ImagePut.class)
    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}