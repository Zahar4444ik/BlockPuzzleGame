package sk.tuke.gamestudio.server.webservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.DTO.auth.LoginDTO;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.DTO.auth.RegisterDTO;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.PlayerService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("api/player")
public class PlayerServiceRest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("{nickname}")
    public Player getPlayer(@PathVariable String nickname) {
        return playerService.getPlayer(nickname);
    }

    @PostMapping("/register")
    public boolean register(@RequestBody RegisterDTO registerRequest) {
        return playerService.register(registerRequest.getNickname(), registerRequest.getPassword());
    }

    @PostMapping("/login")
    public boolean login(@RequestBody LoginDTO loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getNickname(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession(true)  // Create session if not exists
                    .setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession(false); // Get current session, but don't create a new one
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate(); // Invalidate the session
        }
        SecurityContextHolder.clearContext(); // Clear security context
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/authenticated")
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
        return isAuthenticated;
    }

    @PutMapping("/update")
    public void updatePlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = new Player(playerDTO.getNickname(), playerDTO.getPassword());
        player.setGamesPlayed(playerDTO.getGamesPlayed());
        player.setLastPlayed(player.getLastPlayed());
        player.setScore(playerDTO.getScore());
        this.playerService.updatePlayer(player);
    }

    @GetMapping("/current-nickname")
    public String getCurrentNickname() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName(); // Returns the nickname (principal name)
        }
        return null;
    }
}
