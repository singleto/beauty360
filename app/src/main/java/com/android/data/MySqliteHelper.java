package com.android.data;


import com.android.utils.MyUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "bt360";
	public static final String TB_APP_INFO = "app_info";//Ӧ����Ϣ��
	public static final String TB_CATA = "bt_cata";//����
	public static final String TB_ALBUM = "bt_album";//ר����
	public static final String TB_DOWNLOAD_INFO = "bt_download_info";//������Ϣ��
	public static final String TB_SUB_ALBUM = "bt_sub_album";//��ר����
	public static final String TB_IMAGE = "bt_image";//ͼƬ��
	/**
	 * ���ݿ�汾��,Ĭ�ϴ�1��ʼ��2013/5/31����Ϊ2����Ҫ���ݿ����ʱ�����Ĵ�ֵ��
	 */
	private static final int DB_VERSION = 2;
	public MySqliteHelper(Context context) { 
		super(context, DB_NAME, null, DB_VERSION);
		//super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_APP_INFO + "(is_auto_save bit)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_CATA + "(cata_id integer primary key,cata_name varchar(20))");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_ALBUM + "(album_id varchar(5) primary key," +
						"cata_id varchar(5),alubm_name varchar(50)," +
						"pic_amount varchar(5),click_amount varchar(10)," +
						"comment_amount varchar(10),praise_amount varchar(10)," +
						"download_amount varchar(10),is_father varchar(2)," +
						"father_id varchar(5))");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_IMAGE + "(image_id varchar(5) primary key," +
						"album_id varchar(5),image_name varchar(50)," +
						"cata_id varchar(5),praise_amount varchar(10)," +
						"download_amount varchar(10),image_size)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_DOWNLOAD_INFO + "(album_id varchar(5) primary key," +
						"cata_id varchar(5),alubm_name varchar(50)," +
						"pic_amount varchar(5),click_amount varchar(10)," +
						"comment_amount varchar(10),praise_amount varchar(10)," +
						"download_amount varchar(10),is_father varchar(2)," +
						"father_id varchar(5),down_status integer," +
						"downloaded_amout integer,cur_download_num integer," +
						"ins_time varchar(30),upd_time varchar(30))");
		
		MyUtils.outLog("created sqlite table");

		//db.execSQL("create table user(id int,name varchar(20),phone varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//Log.v(Constants.LOG_TAG, "000001");

		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_DOWNLOAD_INFO + "(album_id varchar(5) primary key," +
						"cata_id varchar(5),alubm_name varchar(50)," +
						"pic_amount varchar(5),click_amount varchar(10)," +
						"comment_amount varchar(10),praise_amount varchar(10)," +
						"download_amount varchar(10),is_father varchar(2)," +
						"father_id varchar(5),down_status integer," +
						"downloaded_amout integer,cur_download_num integer," +
						"ins_time varchar(30),upd_time varchar(30))");
		
		MyUtils.outLog("update sqlite table");
	}

	/*	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}*/

}
