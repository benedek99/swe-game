package swegame.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {
    /**
     * Constructor for thew class.
     */
    public GameResultDao() {
        super(GameResult.class);
    }

    /**
     * Returns the list of the last {@code n} games.
     *
     * @param n the maximum number of results to be returned
     * @return the list of the last {@code n} games.
     */
    @Transactional
    public List<GameResult> findLast(int n) {
        return entityManager.createQuery("SELECT r FROM GameResult r ORDER BY  r.created DESC", GameResult.class)
                .setMaxResults(n)
                .getResultList();
    }

}
