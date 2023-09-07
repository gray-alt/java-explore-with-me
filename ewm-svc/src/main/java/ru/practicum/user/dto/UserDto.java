package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
public class UserDto {
    private final Long id;
    @NotBlank
    @Length(min = 2, max = 250)
    private final String name;
    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    private final String email;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
