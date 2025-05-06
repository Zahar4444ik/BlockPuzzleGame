package sk.tuke.gamestudio.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginDTO {
    private String nickname;
    private String password;
}
