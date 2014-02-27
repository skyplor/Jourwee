package com.algomized.android.jourwee.util;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.algomized.android.jourwee.auth.CKey;
import com.algomized.android.jourwee.auth.CacheResult;
import com.algomized.android.jourwee.auth.Cacheable;
import com.algomized.android.jourwee.auth.JourweeAuth;
import com.algomized.android.jourwee.model.User;

public class JourweeCache //extends SqliteCache
{
//    private static class JourweeSession extends SqliteSession
//    {
//
//        protected void saveInternal(Cacheable cacheable)
//        {
//            super.saveInternal(cacheable);
//            ((JourweeCache)cache).tryCacheActiveUser(cacheable);
//        }
//
//        protected Cacheable stageInternal(Cacheable cacheable)
//        {
//            ((JourweeCache)cache).tryCacheActiveUser(cacheable);
//            return super.stageInternal(cacheable);
//        }
//
//        public JourweeSession(JourweeCache jourweecache)
//        {
//            super(jourweecache);
//        }
//    }
//
//
//    private static final int VERSION = 1;
//    private final JourweeAuth auth;
//    private User mActiveUser;
//
//    public JourweeCache(Context context, JourweeAuth jourweeauth)
//    {
//        super(context, JourweeGson.newInstance(), new Class[] {
//            com/jourwee/ape/User, com/jourwee/ape/UserList, com/jourwee/ape/JourweeNearbyUser, com/jourwee/ape/JourweeNearbyUserList, com/jourwee/ape/JourweeMessage, com/jourwee/ape/JourweeMessageList, com/jourwee/ape/JourweeMessageThreadBase, com/jourwee/ape/JourweeMessageThreadList, com/jourwee/ape/JourweeFile, com/jourwee/ape/JourweeFeedItem, 
//            com/jourwee/ape/JourweeFeed, com/jourwee/ape/JourweeAsset, com/jourwee/ape/JourweeAssetBundle, com/jourwee/ape/JourweeAssetBundleList, com/jourwee/ape/JourweeAssetHistory, com/jourwee/ape/JourweePromotion, com/jourwee/ape/JourweeRelationshipChoices, com/jourwee/ape/JourweeDeactivationFeedback
//        });
//        auth = jourweeauth;
//    }
//
//    public void cleanMessageThreads()
//    {
//        delete(com/jourwee/ape/JourweeMessageThreadBase, "__cache_time__ = 0", null);
//    }
//
//    public void cleanMessages()
//    {
//        delete(com/jourwee/ape/JourweeMessage, "__cache_time__ = 0", null);
//    }
//
//    public void cleanShims(Pane pane)
//    {
//        deleteList(com/jourwee/ape/JourweeMessageList, com/jourwee/ape/JourweeMessage, null, pane);
//    }
//
//    public void clearActiveUser()
//    {
//        mActiveUser = null;
//    }
//
//    public int clearInboxUnreadCount(InboxId inboxid)
//    {
//        String s = "incoming=1 AND read=0";
//        String as[];
//        int i;
//        Iterator iterator;
//        if (!inboxid.isDefault())
//        {
//            s = (new StringBuilder("(url=? OR parent=?) AND ")).append(s).toString();
//            as = new String[2];
//            as[0] = inboxid.get();
//            as[1] = inboxid.get();
//        } else
//        {
//            as = new String[0];
//        }
//        i = 0;
//        iterator = query(com/jourwee/ape/JourweeMessageThreadBase, s, as).iterator();
//        do
//        {
//            if (!iterator.hasNext())
//            {
//                return i;
//            }
//            JourweeMessageThreadBase jourweemessagethreadbase = (JourweeMessageThreadBase)iterator.next();
//            if (jourweemessagethreadbase.getParent().equals(inboxid))
//            {
//                i += jourweemessagethreadbase.getUnreadCount();
//            }
//            jourweemessagethreadbase.setUnreadCount(0);
//            jourweemessagethreadbase.setRead(true);
//            save(jourweemessagethreadbase);
//        } while (true);
//    }
//
//    public void dispatchInfo(net.dhleong.ape.cache.ApeChangedListener.ChangeInfo changeinfo)
//    {
//        super.dispatchInfo(changeinfo);
//    }
//
//    public User getActiveUserIfCached()
//    {
//        if (mActiveUser != null && mActiveUser.slug.get().equals(auth.getSlug()))
//        {
//            return mActiveUser;
//        } else
//        {
//            mActiveUser = null;
//            return null;
//        }
//    }
//
//    protected int getCacheVersion()
//    {
//        return 1;
//    }
//
//    public List getInstalledAssetBundles()
//    {
//        String as[] = new String[1];
//        as[0] = (new StringBuilder(String.valueOf('"'))).append(BoolState.TRUE.name()).append('"').toString();
//        return query(com/jourwee/ape/JourweeAssetBundle, "installed = ?", as);
//    }
//
//    public User getOrLoadActiveUser()
//    {
//        User jourweeuser = getActiveUserIfCached();
//        if (jourweeuser != null)
//        {
//            return jourweeuser;
//        } else
//        {
//            return (User)load(com/jourwee/ape/User, Slug.ACTIVEUSER);
//        }
//    }
//
//    public int getUnreadIncomingMessageCount(InboxId inboxid)
//    {
//        String as[] = new String[1];
//        as[0] = inboxid.get();
//        return queryInt(com/jourwee/ape/JourweeMessageThreadBase, "unread_count", 0, "url = ? LIMIT 1", as);
//    }
//
//    public boolean hasUnreadIncoming(InboxId inboxid)
//    {
//        String as[] = new String[1];
//        as[0] = inboxid.getId();
//        int i = queryInt(com/jourwee/ape/JourweeMessage, "1 AS count", 0, "user = ? AND read=0 AND incoming=1 LIMIT 1", as);
//        boolean flag = false;
//        if (i != 0)
//        {
//            flag = true;
//        }
//        return flag;
//    }
//
//    public Cacheable loadForced(Class class1, CKey ckey)
//    {
//        return loadResult(class1, ckey, true).get();
//    }
//
//    public CacheResult loadResult(Class class1, CKey ckey)
//    {
//        CacheResult cacheresult;
//        if (ckey == Slug.ACTIVEUSER && auth != null && auth.getSlug() != null)
//        {
//            cacheresult = super.loadResult(class1, Slug.from(auth.getSlug()));
//        } else
//        {
//            cacheresult = super.loadResult(class1, ckey);
//        }
//        tryCacheActiveUser(cacheresult.get());
//        return cacheresult;
//    }
//
//    public void markIncomingAsRead(InboxId inboxid)
//    {
//        ContentValues contentvalues = new ContentValues();
//        contentvalues.put("read", Integer.valueOf(1));
//        String as[] = new String[2];
//        as[0] = inboxid.getId();
//        as[1] = JourweeMessageBase.Status.FAILED.name();
//        update(com/jourwee/ape/JourweeMessage, contentvalues, "user = ? AND incoming = 1 AND read = 0 AND status != ?", as);
//    }
//
//    public boolean markPriorOutgoingMessagesAsRead(InboxId inboxid, MUUID muuid)
//    {
//        SchemaParser schemaparser = SchemaParser.of(com/jourwee/ape/JourweeMessage);
//        String s = schemaparser.getTableName();
//        String s1 = schemaparser.getActualKeyName();
//        String s2 = (new StringBuilder("user = ? AND incoming=0 AND read=0 AND status != ? AND created <= (SELECT created FROM ")).append(s).append(" WHERE ").append(s1).append(" = ?)").toString();
//        ContentValues contentvalues = new ContentValues();
//        contentvalues.put("read", Integer.valueOf(1));
//        String as[] = new String[3];
//        as[0] = inboxid.getId();
//        as[1] = JourweeMessageBase.Status.FAILED.name();
//        as[2] = muuid.get();
//        update(com/jourwee/ape/JourweeMessage, contentvalues, s2, as);
//        return numAffected() > 0;
//    }
//
//    public volatile Session newSession()
//    {
//        return newSession();
//    }
//
//    public SqliteSession newSession()
//    {
//        ensureInitialized();
//        return new JourweeSession(this);
//    }
//
//    protected void onCreate(SQLiteDatabase sqlitedatabase)
//    {
//        super.onCreate(sqlitedatabase);
//        SchemaParser schemaparser = SchemaParser.of(com/jourwee/ape/JourweeMessageThreadBase);
//        SchemaParser schemaparser1 = SchemaParser.of(com/jourwee/ape/JourweeMessageList);
//        String s = (new StringBuilder(" BEGIN   UPDATE ")).append(schemaparser1.getTableName()).append("  SET ").append("__cache_time__").append(" = 0 ").append("  WHERE new.url = ").append(schemaparser1.getTableName()).append(".").append("key_group").append("  ;").append(" END ").toString();
//        sqlitedatabase.execSQL((new StringBuilder("CREATE TRIGGER IF NOT EXISTS invalidate_threads_new_message AFTER  UPDATE OF uuid ON ")).append(schemaparser.getTableName()).append(s).toString());
//        sqlitedatabase.execSQL((new StringBuilder("CREATE TRIGGER IF NOT EXISTS invalidate_threads_invalidated AFTER  UPDATE OF __cache_time__ ON ")).append(schemaparser.getTableName()).append(" WHEN new.").append("__cache_time__").append(" <= 0 ").append(s).toString());
//    }
//
//    protected void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
//    {
//        sqlitedatabase.execSQL("DROP TRIGGER IF EXISTS invalidate_threads_new_message");
//        sqlitedatabase.execSQL("DROP TRIGGER IF EXISTS invalidate_threads_invalidated");
//        super.onUpgrade(sqlitedatabase, i, j);
//    }
//
//    public void save(Cacheable cacheable)
//    {
//        tryCacheActiveUser(cacheable);
//        super.save(cacheable);
//    }
//
//    void tryCacheActiveUser(Object obj)
//    {
//        if ((obj instanceof User) && auth != null && auth.getSlug() != null && (auth.getSlug().equals(((User)obj).slug.get()) || auth.getSlug().equals(Slug.ACTIVEUSER)))
//        {
//            mActiveUser = (User)obj;
//        }
//    }
//
//    public void updateThreadReadCount(InboxId inboxid, int i)
//    {
//        String s = SchemaParser.of(com/jourwee/ape/JourweeMessageThreadBase).getTableName();
//        String s1 = (new StringBuilder("(url = ? OR url IN ( SELECT parent FROM ")).append(s).append(" WHERE url = ?").append(")) ").append("AND incoming=1 AND read=0").toString();
//        String as[] = new String[2];
//        as[0] = inboxid.get();
//        as[1] = inboxid.get();
//        List list = query(com/jourwee/ape/JourweeMessageThreadBase, s1, as);
//        SqliteSession sqlitesession = newSession();
//        Iterator iterator = list.iterator();
//        do
//        {
//            if (!iterator.hasNext())
//            {
//                sqlitesession.commit();
//                return;
//            }
//            JourweeMessageThreadBase jourweemessagethreadbase = (JourweeMessageThreadBase)iterator.next();
//            jourweemessagethreadbase.setUnreadCount(i + jourweemessagethreadbase.getUnreadCount());
//            jourweemessagethreadbase.setRead(true);
//            sqlitesession.attach(jourweemessagethreadbase.getUser());
//            sqlitesession.save(jourweemessagethreadbase);
//        } while (true);
//    }
}
