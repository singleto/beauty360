package com.android.data;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.info.AlbumInfo;
import com.android.info.CatagoryInfo;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.utils.MyUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 处理数据库的dao
 * 
 * @author pwp
 * 
 */
public class MySqliteDao {

	private Context mContext;
	private SQLiteDatabase sqlitedb;
	MySqliteHelper myOpenHelper;

	public static final String lock = "访问";
	
	public MySqliteDao(Context context) {
		this.mContext = context;
		myOpenHelper = new MySqliteHelper(context);
		sqlitedb = myOpenHelper.getWritableDatabase(); // 实例化数据库
	}

	// 初始化类别
	public void InitCataAndAlbumTables() {

		Cursor cur;
		// "drop table user";
		if (isTableExist(MySqliteHelper.TB_CATA)) {
			sqlitedb.delete(MySqliteHelper.TB_CATA, null, null);// 删除cata表中的所有数据
			cur = sqlitedb.rawQuery("SELECT count(*) FROM "
					+ MySqliteHelper.TB_CATA, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					// Log.v(Constants.LOG_TAG, MySqliteHelper.TB_CATA
					// + "'size is:" + cur.getInt(0));
				}
				cur.close();
			}
		}

		if (isTableExist(MySqliteHelper.TB_ALBUM)) {
			sqlitedb.delete(MySqliteHelper.TB_ALBUM, null, null);// 删除album表中的所有数据
			cur = sqlitedb.rawQuery("SELECT count(*) FROM "
					+ MySqliteHelper.TB_ALBUM, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					// Log.v(Constants.LOG_TAG, MySqliteHelper.TB_ALBUM
					// + "'size is:" + cur.getInt(0));
				}
				cur.close();
			}
		}
	}

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public boolean isTableExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		//SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = sqlitedb.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
				cursor.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/*
	 * 插入单条类别信息
	 */
	public long insertCata(CatagoryInfo cataInfo) {

		long insertRows = 0;
		ContentValues value = new ContentValues();
		value.put("cata_id", cataInfo.getId());
		value.put("cata_name", cataInfo.getCatagoryName());
		insertRows = sqlitedb.insert(MySqliteHelper.TB_CATA, null, value);
		// Log.v(Constants.LOG_TAG,
		// "insertCata successed and row num is:"+insertRows);
		return insertRows;
	}

	// 插入某个类别的专辑，或者某个专辑的子专辑
	public void insertAlbums(ArrayList<AlbumInfo> arrAlbums, String fatherId) {

		for (int i = 0; i < arrAlbums.size(); i++) {
			AlbumInfo albumInfo = arrAlbums.get(i);
			insertAlubm(albumInfo, fatherId);
			if (albumInfo.getArrSubAlbumInfo() != null
					&& albumInfo.getArrSubAlbumInfo().size() > 0) {
				insertAlbums(albumInfo.getArrSubAlbumInfo(), albumInfo.getId());
			} else {

			}
		}
	}

	// 插入单个专辑
	public long insertAlubm(AlbumInfo albumInfo, String fatherId) {
		long insertRows = 0;
		ContentValues value = new ContentValues();
		value.put("album_id", albumInfo.getId());
		value.put("cata_id", albumInfo.getCategoryId());
		value.put("alubm_name", albumInfo.getmAlbumName());
		value.put("pic_amount", albumInfo.getPicAmount());
		value.put("click_amount", albumInfo.getClickAmount());
		value.put("comment_amount", albumInfo.getCommentAmount());
		value.put("praise_amount", albumInfo.getPraiseAmount());
		value.put("download_amount", albumInfo.getDownLoadAmount());
		// Log.v(Constants.LOG_TAG,
		// "albumInfo's subAlbum is:"+albumInfo.getArrSubAlbumInfo());
		if (albumInfo.getArrSubAlbumInfo() != null
				&& albumInfo.getArrSubAlbumInfo().size() > 0) {
			// Log.v(Constants.LOG_TAG, "isfather");
			value.put("is_father", "1");
		} else {
			// Log.v(Constants.LOG_TAG, "not father");
			value.put("is_father", "0");
		}
		value.put("father_id", fatherId);

		// Log.v(Constants.LOG_TAG, "value's isfather is:"+value.toString());

		insertRows = sqlitedb.insert(MySqliteHelper.TB_ALBUM, null, value);
		// Log.v(Constants.LOG_TAG,
		// "insertAlubm successed and row num is:"+insertRows);
		return insertRows;
	}

	// 插入图片
	public void initImage() {
		sqlitedb.delete(MySqliteHelper.TB_IMAGE, null, null);// 删除album表中的所有数据

	}

	public ArrayList<CatagoryInfo> getCatas() {

		// Log.v(Constants.LOG_TAG, "enter  getCatas:");

		ArrayList<CatagoryInfo> arrRes = null;
		CatagoryInfo cataInfo = null;
		
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM "
				+ MySqliteHelper.TB_CATA, null);
		if (cur != null) {
			arrRes = new ArrayList<CatagoryInfo>();
			while (cur.moveToNext()) {
				cataInfo = new CatagoryInfo();
				cataInfo.setId(cur.getString(0));
				cataInfo.setCatagoryName(cur.getString(1));
				cataInfo.setmArrayListAlbumsInfo(getAlbumsByCataId(cur
						.getString(0)));
				// Log.v(Constants.LOG_TAG,
				// "cataInfo id  is:"+cataInfo.getId());
				arrRes.add(cataInfo);
			}
			cur.close();
		}

		return arrRes;
	}

	// 根据类别ID获取专辑
	public ArrayList<AlbumInfo> getAlbumsByCataId(String cataId) {
		// Log.v(Constants.LOG_TAG, "enter  getAlbumsByCataId:");
		ArrayList<AlbumInfo> arrRes = null;
		AlbumInfo albumInfo = null;
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM "
				+ MySqliteHelper.TB_ALBUM + " where cata_id=" + cataId, null);
		if (cur != null) {
			arrRes = new ArrayList<AlbumInfo>();
			while (cur.moveToNext()) {
				albumInfo = new AlbumInfo();
				albumInfo.setId(cur.getString(0));
				albumInfo.setCategoryId(cur.getString(1));
				albumInfo.setmAlbumName(cur.getString(2));
				albumInfo.setPicAmount(cur.getString(3));
				albumInfo.setClickAmount(cur.getString(4));
				albumInfo.setCommentAmount(cur.getString(5));
				albumInfo.setPraiseAmount(cur.getString(6));
				albumInfo.setDownLoadAmount(cur.getString(7));

				// 如果下面还有子专辑
				if (cur.getString(8).equals("1")) {
					albumInfo.setArrSubAlbumInfo(getSubAlbumsByFatherId(cur
							.getString(0)));
				}
				// 如果是类别的顶层，则增加
				if ("0".equals(cur.getString(9))) {
					arrRes.add(albumInfo);

				}
				// Log.v(Constants.LOG_TAG,
				// "albumInfo id is:"+albumInfo.getId()+"  and is_father is:"+cur.getString(8));

			}
			cur.close();
		}
		return arrRes;
	}


	/**
	 * 判断downLoad info的记录是否存在
	 * @param albumId 专辑ID
	 * @return
	 */
	public boolean isDownInfoExist(String albumId ){
		boolean bRes=false;
		
		if (albumId == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) from "+MySqliteHelper.TB_DOWNLOAD_INFO+" where album_id ='"
					+ albumId.trim() + "' ";
			cursor = sqlitedb.rawQuery(sql, null);
			if (cursor != null && cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					bRes = true;
				}
				cursor.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return bRes;		
	}
	
	// 下载信息插入
	public long insertDownInfo(DownloadInfo downInfo) {	
		
		long insertRows = 0;
		synchronized (lock) {
			ContentValues value = new ContentValues();
			value.put("cur_download_num", downInfo.getCurDownloadNum());
			value.put("downloaded_amout", downInfo.getDownloadedAmout());
			MyUtils.outLog("datetime() is:" + MyUtils.nowTime());
			value.put("upd_time", MyUtils.nowTime());
			value.put("album_id", downInfo.getmAlbumId());
			value.put("cata_id", downInfo.getCategoryId());
			value.put("alubm_name", downInfo.getmAlbumName());
			value.put("pic_amount", downInfo.getPicAmount());
			value.put("click_amount", downInfo.getClickAmount());
			value.put("down_status", downInfo.getDownStatus());
			value.put("comment_amount", downInfo.getCommentAmount());
			value.put("praise_amount", downInfo.getPraiseAmount());
			value.put("download_amount", downInfo.getDownLoadAmount());
			insertRows = sqlitedb.insert(MySqliteHelper.TB_DOWNLOAD_INFO, null,
					value);
		}
		return insertRows;
	}

	// 下载信息更新
	public int updateDownInfo(DownloadInfo downInfo) {
		int res=0;
		synchronized (lock) {
			ContentValues value = new ContentValues();
			value.put("cur_download_num", downInfo.getCurDownloadNum());
			value.put("downloaded_amout", downInfo.getDownloadedAmout());
			MyUtils.outLog("datetime() is:" + MyUtils.nowTime());
			value.put("upd_time", MyUtils.nowTime());
			value.put("album_id", downInfo.getmAlbumId());
			value.put("cata_id", downInfo.getCategoryId());
			value.put("alubm_name", downInfo.getmAlbumName());
			value.put("pic_amount", downInfo.getPicAmount());
			value.put("click_amount", downInfo.getClickAmount());
			value.put("down_status", downInfo.getDownStatus());
			value.put("comment_amount", downInfo.getCommentAmount());
			value.put("praise_amount", downInfo.getPraiseAmount());
			value.put("download_amount", downInfo.getDownLoadAmount());
			res = sqlitedb.update(MySqliteHelper.TB_DOWNLOAD_INFO, value,
					"album_id = ?", new String[] { downInfo.getmAlbumId() });
		}
		return res;
	}	
	
	/**
	 * 获取下载过的专辑信息
	 * @return
	 */
	public HashMap<String, DownloadInfo> getDownInfo() {
		HashMap<String, DownloadInfo> downInfoRes=null;
		DownloadInfo downInfo=null;	
		// Log.v(Constants.LOG_TAG, "enter  getAlbumsByCataId:");
		Cursor cur = sqlitedb.rawQuery("SELECT * FROM "
				+ MySqliteHelper.TB_DOWNLOAD_INFO, null);
		if (cur != null) {
			downInfoRes=new HashMap<String, DownloadInfo>();
			while (cur.moveToNext()) {
				downInfo = new DownloadInfo();
				downInfo.setmAlbumId(cur.getString(cur.getColumnIndex("album_id")));
				downInfo.setCategoryId(cur.getString(cur.getColumnIndex("cata_id")));
				downInfo.setmAlbumName(cur.getString(cur.getColumnIndex("alubm_name")));
				downInfo.setPicAmount(Integer.parseInt(cur.getString(cur.getColumnIndex("pic_amount"))));
				downInfo.setClickAmount(cur.getString(cur.getColumnIndex("click_amount")));
				downInfo.setCommentAmount(cur.getString(cur.getColumnIndex("comment_amount")));
				downInfo.setPraiseAmount(cur.getString(cur.getColumnIndex("praise_amount")));
				downInfo.setDownLoadAmount(cur.getString(cur.getColumnIndex("download_amount")));
				downInfo.setInsTime(cur.getString(cur.getColumnIndex("ins_time")));
				downInfo.setUpdTime(cur.getString(cur.getColumnIndex("upd_time")));
				downInfo.setDownloadedAmout(cur.getInt(cur.getColumnIndex("downloaded_amout")));
				downInfo.setCurDownloadNum(cur.getInt(cur.getColumnIndex("cur_download_num")));
				downInfo.setDownStatus(cur.getInt(cur.getColumnIndex("down_status")));
				if(downInfo.getDownloadedAmout()<downInfo.getPicAmount()){
					downInfo.setDownStatus(Constants.DOWNLOAD_NO_DONE);
				}else{
					downInfo.setDownStatus(Constants.DOWNLOAD_DONE);
				}
				downInfoRes.put(downInfo.getmAlbumId(), downInfo);
				
			}
			cur.close();
		}
		return downInfoRes;
	}

	
	
	// 根据专辑ID查询子专辑
	public ArrayList<AlbumInfo> getSubAlbumsByFatherId(String albumId) {
		// Log.v(Constants.LOG_TAG, "enter  getSubAlbumsByFatherId");
		ArrayList<AlbumInfo> arrRes = null;
		AlbumInfo albumInfo = null;
		Cursor cur = sqlitedb
				.rawQuery("SELECT * FROM " + MySqliteHelper.TB_ALBUM
						+ " where father_id=" + albumId, null);
		if (cur != null) {
			arrRes = new ArrayList<AlbumInfo>();
			while (cur.moveToNext()) {
				albumInfo = new AlbumInfo();
				albumInfo.setId(cur.getString(0));
				albumInfo.setCategoryId(cur.getString(1));
				albumInfo.setmAlbumName(cur.getString(2));
				albumInfo.setPicAmount(cur.getString(3));
				albumInfo.setClickAmount(cur.getString(4));
				albumInfo.setCommentAmount(cur.getString(5));
				albumInfo.setPraiseAmount(cur.getString(6));
				albumInfo.setDownLoadAmount(cur.getString(7));
				// 如果还有子专辑，则递归
				if (cur.getString(8).equals("1")) {
					albumInfo.setArrSubAlbumInfo(getSubAlbumsByFatherId(cur
							.getString(0)));
				}
				// 如果子专辑的父亲跟当前专辑ID相同，则添加
				if (albumId.equalsIgnoreCase(cur.getString(9))) {
					arrRes.add(albumInfo);
				}
				// Log.v(Constants.LOG_TAG,
				// "sub albumInfo id is:"+albumInfo.getId()+"  and is_father is:"+cur.getString(8));

			}
			cur.close();
		}
		return arrRes;
	}
	
	public void closeDb(){
		if(myOpenHelper!=null){
			myOpenHelper.close();
		}
		if(sqlitedb!=null){
			sqlitedb.close();
		}
	}
	/*
	 * public List<Contact> getContact(){ PhoneSqlite ps = new
	 * PhoneSqlite(context, "pwp", null, 1); SQLiteDatabase sqlite =
	 * ps.getReadableDatabase(); Cursor cursor = sqlite.query("contact", new
	 * String[]{"id","name","phone"}, null, null, null, null, null);
	 * List<Contact> list = new ArrayList<Contact>();
	 * while(cursor.moveToNext()){ Contact contact = new Contact();
	 * contact.setId(cursor.getString(cursor.getColumnIndex("id")));
	 * contact.setName(cursor.getString(cursor.getColumnIndex("name")));
	 * contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
	 * list.add(contact);
	 * System.out.println("id:------"+cursor.getString(cursor.
	 * getColumnIndex("id"))); } return list; }
	 */
	/*
	 * //插入数据 public void insertContact(){ MySqlite ps = new MySqlite(context);
	 * SQLiteDatabase sqlite = ps.getWritableDatabase(); ContentValues value =
	 * new ContentValues(); value.put("id", "1"); value.put("name", "张三");
	 * value.put("phone", "5556"); sqlite.insert("contact", null, value);
	 * value.put("id", "2"); value.put("name", "老师三"); value.put("phone",
	 * "5555"); sqlite.insert("contact", null, value); value.put("id", "3");
	 * value.put("name", "天天三"); value.put("phone", "5554");
	 * sqlite.insert("contact", null, value); value.put("id", "5");
	 * value.put("name", "高规格三"); value.put("phone", "5553");
	 * sqlite.insert("contact", null, value);
	 * //sqlite.execSQL("insert into contact(?,?,?)",new
	 * Object[]{"1","张三","5556"});
	 * 
	 * }
	 *//**
	 * 从数据库中获取用户信息
	 * 
	 * @return 如何获取到，则返回userInfo 对象，否则返回null
	 */
	/*
	 * public UserInfo getUserInfo() { UserInfo userInfo = null; MySqlite ps =
	 * new MySqlite(context); SQLiteDatabase sqlite = ps.getWritableDatabase();
	 * Cursor userInfoCursor = sqlite.rawQuery("SELECT * FROM " +
	 * MySqlite.TB_NAME, null); int id =
	 * userInfoCursor.getColumnIndex(UserInfo.ID); int name =
	 * userInfoCursor.getColumnIndex(UserInfo.NAME); int password =
	 * userInfoCursor.getColumnIndex(UserInfo.PASSWORD);
	 * System.out.println("id is:"+id+"  name is:"+name
	 * +"  password is:"+password); if (userInfoCursor.moveToFirst()) { userInfo
	 * = new UserInfo(userInfoCursor.getString(id),
	 * userInfoCursor.getString(name), userInfoCursor.getString(password));
	 * Log.v("PPK", "userInfo is:" + userInfo.toString()); }
	 * userInfoCursor.close(); sqlite.close(); ps.close(); return userInfo; }
	 * 
	 * public void addUserInfo(UserInfo userInfo){ MySqlite ps = new
	 * MySqlite(context); SQLiteDatabase sqlite = ps.getWritableDatabase();
	 * String sql; // 先判断user表中是否已经存在用户信息，若存在，则清空，让user表之保存1条记录 Cursor
	 * userInfoCursor = sqlite.rawQuery("SELECT * FROM " + MySqlite.TB_NAME,
	 * null); if (userInfoCursor.moveToFirst()) { Log.v("PPK",
	 * "exct delete==="); sqlite.execSQL("delete from "+MySqlite.TB_NAME); }
	 * //String tip; sql = "insert into " + MySqlite.TB_NAME + " (" +
	 * UserInfo.ID + ", " + UserInfo.NAME + ", " + UserInfo.PASSWORD + ") " +
	 * "values('"
	 * +userInfo.getStrId()+"','"+userInfo.getStrName()+"','"+userInfo.
	 * getStrPassword()+"');";
	 * 
	 * Log.v("PPK", "sql is:"+sql);
	 * 
	 * try { sqlite.execSQL(sql); } catch (SQLException e) { Log.v("PPK",
	 * e.toString()); }finally{ sqlite.close(); ps.close(); } }
	 */
}
