package com.algomized.android.jourwee.util;

import android.content.Context;

import com.algomized.android.jourwee.Auth.Cacheable;
import com.algomized.android.jourwee.Auth.JourweeAuth;
import com.algomized.android.jourwee.model.User;

public class JourweeCache extends SqliteCache
{
    private static class MinusSession extends SqliteSession
    {

        protected void saveInternal(Cacheable cacheable)
        {
            super.saveInternal(cacheable);
            ((JourweeCache)cache).tryCacheActiveUser(cacheable);
        }

        protected Cacheable stageInternal(Cacheable cacheable)
        {
            ((JourweeCache)cache).tryCacheActiveUser(cacheable);
            return super.stageInternal(cacheable);
        }

        public MinusSession(JourweeCache minuscache)
        {
            super(minuscache);
        }
    }


    private static final int VERSION = 1;
    private final JourweeAuth auth;
    private User mActiveUser;

    public JourweeCache(Context context, JourweeAuth minusauth)
    {
        super(context, MinusGson.newInstance(), new Class[] {
            com/minus/ape/MinusUser, com/minus/ape/MinusUserList, com/minus/ape/MinusNearbyUser, com/minus/ape/MinusNearbyUserList, com/minus/ape/MinusMessage, com/minus/ape/MinusMessageList, com/minus/ape/MinusMessageThreadBase, com/minus/ape/MinusMessageThreadList, com/minus/ape/MinusFile, com/minus/ape/MinusFeedItem, 
            com/minus/ape/MinusFeed, com/minus/ape/MinusAsset, com/minus/ape/MinusAssetBundle, com/minus/ape/MinusAssetBundleList, com/minus/ape/MinusAssetHistory, com/minus/ape/MinusPromotion, com/minus/ape/MinusRelationshipChoices, com/minus/ape/MinusDeactivationFeedback
        });
        auth = minusauth;
    }

    public void cleanMessageThreads()
    {
        delete(com/minus/ape/MinusMessageThreadBase, "__cache_time__ = 0", null);
    }

    public void cleanMessages()
    {
        delete(com/minus/ape/MinusMessage, "__cache_time__ = 0", null);
    }

    public void cleanShims(Pane pane)
    {
        deleteList(com/minus/ape/MinusMessageList, com/minus/ape/MinusMessage, null, pane);
    }

    public void clearActiveUser()
    {
        mActiveUser = null;
    }

    public int clearInboxUnreadCount(InboxId inboxid)
    {
        String s = "incoming=1 AND read=0";
        String as[];
        int i;
        Iterator iterator;
        if (!inboxid.isDefault())
        {
            s = (new StringBuilder("(url=? OR parent=?) AND ")).append(s).toString();
            as = new String[2];
            as[0] = inboxid.get();
            as[1] = inboxid.get();
        } else
        {
            as = new String[0];
        }
        i = 0;
        iterator = query(com/minus/ape/MinusMessageThreadBase, s, as).iterator();
        do
        {
            if (!iterator.hasNext())
            {
                return i;
            }
            MinusMessageThreadBase minusmessagethreadbase = (MinusMessageThreadBase)iterator.next();
            if (minusmessagethreadbase.getParent().equals(inboxid))
            {
                i += minusmessagethreadbase.getUnreadCount();
            }
            minusmessagethreadbase.setUnreadCount(0);
            minusmessagethreadbase.setRead(true);
            save(minusmessagethreadbase);
        } while (true);
    }

    public void dispatchInfo(net.dhleong.ape.cache.ApeChangedListener.ChangeInfo changeinfo)
    {
        super.dispatchInfo(changeinfo);
    }

    public MinusUser getActiveUserIfCached()
    {
        if (mActiveUser != null && mActiveUser.slug.get().equals(auth.getSlug()))
        {
            return mActiveUser;
        } else
        {
            mActiveUser = null;
            return null;
        }
    }

    protected int getCacheVersion()
    {
        return 1;
    }

    public List getInstalledAssetBundles()
    {
        String as[] = new String[1];
        as[0] = (new StringBuilder(String.valueOf('"'))).append(BoolState.TRUE.name()).append('"').toString();
        return query(com/minus/ape/MinusAssetBundle, "installed = ?", as);
    }

    public MinusUser getOrLoadActiveUser()
    {
        MinusUser minususer = getActiveUserIfCached();
        if (minususer != null)
        {
            return minususer;
        } else
        {
            return (MinusUser)load(com/minus/ape/MinusUser, Slug.ACTIVEUSER);
        }
    }

    public int getUnreadIncomingMessageCount(InboxId inboxid)
    {
        String as[] = new String[1];
        as[0] = inboxid.get();
        return queryInt(com/minus/ape/MinusMessageThreadBase, "unread_count", 0, "url = ? LIMIT 1", as);
    }

    public boolean hasUnreadIncoming(InboxId inboxid)
    {
        String as[] = new String[1];
        as[0] = inboxid.getId();
        int i = queryInt(com/minus/ape/MinusMessage, "1 AS count", 0, "user = ? AND read=0 AND incoming=1 LIMIT 1", as);
        boolean flag = false;
        if (i != 0)
        {
            flag = true;
        }
        return flag;
    }

    public Cacheable loadForced(Class class1, CKey ckey)
    {
        return loadResult(class1, ckey, true).get();
    }

    public CacheResult loadResult(Class class1, CKey ckey)
    {
        CacheResult cacheresult;
        if (ckey == Slug.ACTIVEUSER && auth != null && auth.getSlug() != null)
        {
            cacheresult = super.loadResult(class1, Slug.from(auth.getSlug()));
        } else
        {
            cacheresult = super.loadResult(class1, ckey);
        }
        tryCacheActiveUser(cacheresult.get());
        return cacheresult;
    }

    public void markIncomingAsRead(InboxId inboxid)
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("read", Integer.valueOf(1));
        String as[] = new String[2];
        as[0] = inboxid.getId();
        as[1] = MinusMessageBase.Status.FAILED.name();
        update(com/minus/ape/MinusMessage, contentvalues, "user = ? AND incoming = 1 AND read = 0 AND status != ?", as);
    }

    public boolean markPriorOutgoingMessagesAsRead(InboxId inboxid, MUUID muuid)
    {
        SchemaParser schemaparser = SchemaParser.of(com/minus/ape/MinusMessage);
        String s = schemaparser.getTableName();
        String s1 = schemaparser.getActualKeyName();
        String s2 = (new StringBuilder("user = ? AND incoming=0 AND read=0 AND status != ? AND created <= (SELECT created FROM ")).append(s).append(" WHERE ").append(s1).append(" = ?)").toString();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("read", Integer.valueOf(1));
        String as[] = new String[3];
        as[0] = inboxid.getId();
        as[1] = MinusMessageBase.Status.FAILED.name();
        as[2] = muuid.get();
        update(com/minus/ape/MinusMessage, contentvalues, s2, as);
        return numAffected() > 0;
    }

    public volatile Session newSession()
    {
        return newSession();
    }

    public SqliteSession newSession()
    {
        ensureInitialized();
        return new MinusSession(this);
    }

    protected void onCreate(SQLiteDatabase sqlitedatabase)
    {
        super.onCreate(sqlitedatabase);
        SchemaParser schemaparser = SchemaParser.of(com/minus/ape/MinusMessageThreadBase);
        SchemaParser schemaparser1 = SchemaParser.of(com/minus/ape/MinusMessageList);
        String s = (new StringBuilder(" BEGIN   UPDATE ")).append(schemaparser1.getTableName()).append("  SET ").append("__cache_time__").append(" = 0 ").append("  WHERE new.url = ").append(schemaparser1.getTableName()).append(".").append("key_group").append("  ;").append(" END ").toString();
        sqlitedatabase.execSQL((new StringBuilder("CREATE TRIGGER IF NOT EXISTS invalidate_threads_new_message AFTER  UPDATE OF uuid ON ")).append(schemaparser.getTableName()).append(s).toString());
        sqlitedatabase.execSQL((new StringBuilder("CREATE TRIGGER IF NOT EXISTS invalidate_threads_invalidated AFTER  UPDATE OF __cache_time__ ON ")).append(schemaparser.getTableName()).append(" WHEN new.").append("__cache_time__").append(" <= 0 ").append(s).toString());
    }

    protected void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
    {
        sqlitedatabase.execSQL("DROP TRIGGER IF EXISTS invalidate_threads_new_message");
        sqlitedatabase.execSQL("DROP TRIGGER IF EXISTS invalidate_threads_invalidated");
        super.onUpgrade(sqlitedatabase, i, j);
    }

    public void save(Cacheable cacheable)
    {
        tryCacheActiveUser(cacheable);
        super.save(cacheable);
    }

    void tryCacheActiveUser(Object obj)
    {
        if ((obj instanceof MinusUser) && auth != null && auth.getSlug() != null && (auth.getSlug().equals(((MinusUser)obj).slug.get()) || auth.getSlug().equals(Slug.ACTIVEUSER)))
        {
            mActiveUser = (MinusUser)obj;
        }
    }

    public void updateThreadReadCount(InboxId inboxid, int i)
    {
        String s = SchemaParser.of(com/minus/ape/MinusMessageThreadBase).getTableName();
        String s1 = (new StringBuilder("(url = ? OR url IN ( SELECT parent FROM ")).append(s).append(" WHERE url = ?").append(")) ").append("AND incoming=1 AND read=0").toString();
        String as[] = new String[2];
        as[0] = inboxid.get();
        as[1] = inboxid.get();
        List list = query(com/minus/ape/MinusMessageThreadBase, s1, as);
        SqliteSession sqlitesession = newSession();
        Iterator iterator = list.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                sqlitesession.commit();
                return;
            }
            MinusMessageThreadBase minusmessagethreadbase = (MinusMessageThreadBase)iterator.next();
            minusmessagethreadbase.setUnreadCount(i + minusmessagethreadbase.getUnreadCount());
            minusmessagethreadbase.setRead(true);
            sqlitesession.attach(minusmessagethreadbase.getUser());
            sqlitesession.save(minusmessagethreadbase);
        } while (true);
    }
}
