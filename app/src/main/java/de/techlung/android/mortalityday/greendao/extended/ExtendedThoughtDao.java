package de.techlung.android.mortalityday.greendao.extended;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.greendao.generated.ThoughtDao;
import de.techlung.android.mortalityday.thoughts.ThoughtManager;

public class ExtendedThoughtDao {
    ThoughtDao thoughtDao;

    public ExtendedThoughtDao(ThoughtDao thoughtDao) {
        this.thoughtDao = thoughtDao;
    }

    public Thought getThought(String key) {
        QueryBuilder<Thought> queryBuilder = thoughtDao.queryBuilder();
        queryBuilder.where(ThoughtDao.Properties.Key.eq(key));
        return queryBuilder.unique();
    }
    public List<Thought> getAllThoughts() {
        QueryBuilder<Thought> queryBuilder = thoughtDao.queryBuilder();
        return queryBuilder.list();
    }

    public long getCount() {
        QueryBuilder<Thought> queryBuilder = thoughtDao.queryBuilder();
        return queryBuilder.count();
    }


    public void insertOrReplace(Thought thought) {
        thoughtDao.insertOrReplace(thought);
        updateThoughtManager();
    }

    public void deleteByKey(String thoughtKey) {
        Thought thought = getThought(thoughtKey);
        if (thought != null) {
            thoughtDao.delete(thought);
            updateThoughtManager();
        }
    }

    public void deleteAll() {
        thoughtDao.deleteAll();
        updateThoughtManager();
    }

    private void updateThoughtManager() {
        ThoughtManager.processLocalThoughtsChanged();
    }
}
