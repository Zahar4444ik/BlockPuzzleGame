package sk.tuke.gamestudio.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterDTO {
    private String nickname;
    private String password;
}
