package de.techlung.android.mortalityday.greendao.extended;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.techlung.android.mortalityday.greendao.generated.Thought;
import de.techlung.android.mortalityday.greendao.generated.ThoughtDao;

public class ExtendedThoughtDao {
    ThoughtDao thoughtDao;

    public ExtendedThoughtDao(ThoughtDao thoughtDao) {
        this.thoughtDao = thoughtDao;
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
    }

    public void deleteAll() {
        thoughtDao.deleteAll();
    }
}
