package de.techlung.android.mortalityday.greendao.extended;

import de.greenrobot.dao.query.QueryBuilder;
import de.techlung.android.mortalityday.greendao.generated.ThoughtMeta;
import de.techlung.android.mortalityday.greendao.generated.ThoughtMetaDao;

public class ExtendedThoughtMetaDao {
    ThoughtMetaDao thoughtMetaDao;

    public ExtendedThoughtMetaDao(ThoughtMetaDao thoughtMetaDao) {
        this.thoughtMetaDao = thoughtMetaDao;
    }

    public ThoughtMeta getThoughtMeta(String key) {
        QueryBuilder<ThoughtMeta> queryBuilder = thoughtMetaDao.queryBuilder();
        queryBuilder.where(ThoughtMetaDao.Properties.Key.eq(key));
        return queryBuilder.unique();
    }

    public void insertOrReplace(ThoughtMeta thoughtMeta) {
        thoughtMetaDao.insertOrReplace(thoughtMeta);
    }

    public void deleteAll() {
        thoughtMetaDao.deleteAll();
    }
}
