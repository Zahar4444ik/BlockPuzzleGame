package sk.tuke.gamestudio.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.GamePrinter;
import sk.tuke.gamestudio.service.PlayerService;

@Service
@Transactional
public class PlayerServiceJPA implements PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder;

    public PlayerServiceJPA() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean register(String nickname, String password) {
        if (isNotValidParameter(nickname) || isNotValidParameter(password)) {
            GamePrinter.invalidInput();
            return false;
        }
        if (playerExists(nickname)){
            GamePrinter.playerWithNicknameAlreadyExists(nickname);
            return false;
        }

        String hashedPassword = passwordEncoder.encode(password);

        Player newPlayer = new Player(nickname, hashedPassword);
        entityManager.persist(newPlayer);
        return true;
    }

    @Override
    public boolean login(String nickname, String password) {
        if (isNotValidParameter(nickname) || isNotValidParameter(password)) {
            GamePrinter.invalidInput();
            return false;
        }
        if (!playerExists(nickname)) {
            GamePrinter.playerWithNicknameDoesNotExist(nickname);
            return false;
        }

        Player player;
        try {
            player = entityManager.createNamedQuery("Player.getPlayer", Player.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }

        return passwordEncoder.matches(password, player.getPassword());
    }

    @Override
    public boolean playerExists(String nickname) {
        try {
            entityManager.createNamedQuery(
                            "Player.getPlayer", Player.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Player getPlayer(String nickname) {
        try {
            return entityManager.createNamedQuery(
                            "Player.getPlayer", Player.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void updatePlayer(Player player) {
        if (!isNotValidPlayer(player)) {
            entityManager.merge(player);
        }
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Player.resetPlayers").executeUpdate();
    }

    private boolean isNotValidParameter(String parameter) {
        return parameter == null || parameter.isBlank();
    }

    private boolean isNotValidPlayer(Player player) {
        return player == null || isNotValidParameter(player.getNickname()) || isNotValidParameter(player.getPassword());
    }
}