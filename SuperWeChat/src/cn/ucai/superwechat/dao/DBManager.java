package cn.ucai.superwechat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.superwechat.domain.User;


/**
 * Created by Administrator on 2017/3/21.
 */

public class DBManager {
    private static DBManager dbManager = new DBManager();
    private DBOpenHelper dbOpenHelper;

    void onInit(Context context) {
        dbOpenHelper = new DBOpenHelper(context);

    }

    public static synchronized DBManager getInstance() {
        return dbManager;
    }

    public synchronized void closeDB() {
        if (dbOpenHelper != null) {
            dbOpenHelper.closeDB();

        }
    }

    public synchronized boolean saveUser(User user) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMUserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMUserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMAvatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMAvatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMAvatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMAvatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMAvatarLastUpdateTime());

        if (db.isOpen()) {
            return db.replace(UserDao.USER_TABLE_NAME, null, values) != -1;
        }
        return false;
    }

    public synchronized User getUser(String username) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME + " where " +
                UserDao.USER_COLUMN_NAME + " =?";
        User user = null;
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if(cursor.moveToNext()){
            user = new User();
            user.setMUserName(username);
            user.setMUserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMAvatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMAvatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMAvatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMAvatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMAvatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return user;

        }
        return null;
    }

    public synchronized boolean updateUser(User user) {
        int result = -1;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String sql = UserDao.USER_COLUMN_NAME + "=?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDao.USER_COLUMN_NICK, user.getMUserNick());

        if (db.isOpen()) {
            result = db.update(UserDao.USER_TABLE_NAME, contentValues, sql, new String[]{user.getMUserName()});
        }
        return result > 0;
    }
}
