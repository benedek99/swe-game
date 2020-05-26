package swegame.results;

import com.google.inject.persist.Transactional;
import util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {

    public GameResultDao() {
        super(GameResult.class);
    }

    /**
     * Returns the list of {@code n} players with the most wins.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} players with the most wins.
     */
    @Transactional
    public List<GameResult> findBest(int n) {
        return entityManager.createQuery("SELECT r FROM GameResult r where r.win=TRUE group by r.player order by count(r.win)", GameResult.class)
                .setMaxResults(n)
                .getResultList();
    }

}
