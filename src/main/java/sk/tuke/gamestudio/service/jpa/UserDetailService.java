package sk.tuke.gamestudio.service.jpa;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.PlayerService;

@AllArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final PlayerService playerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!playerService.playerExists(username))
            throw new UsernameNotFoundException("User not found");
        Player player = playerService.getPlayer(username);

        return User.builder()
                .username(player.getNickname())
                .password(player.getPassword())
                .roles("USER")
                .build();
    }
}
