package mj.wt.wtapp.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import mj.wt.wtapp.bean.DaoMaster;
import mj.wt.wtapp.bean.DaoSession;
import mj.wt.wtapp.bean.SongDao;
import mj.wt.wtapp.bean.Song;

/**
 * Created by wantao on 2017/3/10.
 */

public class SongDbManager {
    private final static String dbName = "song_db";
    private static SongDbManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public SongDbManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static SongDbManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SongDbManager.class) {
                if (mInstance == null) {
                    mInstance = new SongDbManager(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    /*
     *插入单一数据
     */
    public void insetSong(Song song)
    {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SongDao songDao = daoSession.getSongDao();
        songDao.insert(song);
    }
    /**
     * 插入集合
     */
    public void insertSongList(List<Song> songList) {
        if (songList == null || songList.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SongDao songDao = daoSession.getSongDao();
        songDao.insertInTx(songList);
    }
    /**
     * 更新一条记录
     */
    public void updateSong(Song song) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SongDao songDao = daoSession.getSongDao();
        songDao.update(song);
    }

    /**
     * 查询用户列表
     */
    public List<Song> querySongList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SongDao userDao = daoSession.getSongDao();
        QueryBuilder<Song> qb = userDao.queryBuilder();
        List<Song> list = qb.list();
        return list;
    }
    /**
     * 根据id 查询用户
     */
    public Song querySong(long  id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SongDao songDao = daoSession.getSongDao();
        Song song = songDao.load(id);
        return  song;
    }

    /**
     * 删除一条记录
     */
    public void deleteSong(Song song) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        SongDao userDao = daoSession.getSongDao();
        userDao.delete(song);
    }

}