package com.android.info;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.beauty360.point.MyPoints;
import com.android.data.MySqliteDao;
import com.android.update.MyUpdate;


import android.os.Environment;

public class Constants {

	public static String REQ_SERV = "http://360bt.meitwo.com/start";
	//public static String REQ_SERV = "http://10.0.2.2:8080/beauty360/start";
	//public static String WEBVIEW_URL="http://192.168.0.148:8080/ppk/android?";
	
//	public static final String CONFIG_FILE = "360beauty.json";
//	public static final String CATEGORYS_INFO_URL = PRE_URL	+ CONFIG_FILE;
	public static final String CATEGORYS_INFO_KEY = "categorysInfo";
	//public static final String LIST_ALBUM_IMAGE_ADN_TEXT_KEY = "listAlbumImageAndText";
	//public static final String ARR_ALBUMS_INFO_KEY = "arrAlbumsInfo";
	public static final String CATA_ID = "cataId";
	public static final String ALBUM_ID = "albumId";
	//public static final String ALBUM_INFO_KEY = "albumInfo";
	//public static final String IS_SUBALBUM_KEY = "isSubAlbum";
	
	//================================
	public static final String CATAGORY_INFO_KEY = "catagroyInfo";

	//================================
	
	
	public static final String LOG_TAG = "调试信息：";
	public static final int FLING_MIN_DISTANCE = 50;// 移动最小距离
	public static final int FLING_MIN_VELOCITY = 400;// 移动最大速度
	public static final String LOCAL_PATH = Environment
			.getExternalStorageDirectory() + "/360beauty/";
	public static final String DOWNLOAD_PATH = Environment
			.getExternalStorageDirectory() + "/360beauty/download/";	
	public static String UPDATE_SAVE_NAME="360beauty.apk";
	public static boolean SD_INSERTED = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); 
	//public static final String DOWNLOADED_MSG = "图片已下载到本地，欢迎继续欣赏!";
	public static final String REAL_VER_CTL_NUM="1";//是否使用正式版本，主要是为了在安卓市场上发布所设置
	public static final String AD_CTL_NUM="1";//是否展示广告，主要考虑到有些发布平台不允许有广告
	//public static ArrayList<CategoryInfo> mCategorysInfo = new ArrayList<CategoryInfo>();
	//public static MyVersionInfo  verInfo= null;//new MyVersionInfo();
	
	
	public static UrlParams  urlParams= null;//需要在启动的时候初始化;
	public static MyUpdate myUpdate = null;// 定义初始化自动升级类
	public static BtAppInfo  appInfo= null;//需要在启动的时候获取;
	public static MyPoints myPoints = null;// 定义积分相关的类
	public static ArrayList<CatagoryInfo> mCatagorysInfo = null;//服务器上所有类别及其包含的专辑
//	public static ArrayList<CatagoryInfo> mLocalCatagorysInfo = null;//服务器上所有类别及其包含的专辑

//=======================================
	
	//public static boolean isShowUpdateDig=false;//是否展示过update dialog
	// 动画相关
	public static final Integer ANIM_LOC_IMAGE_DUAR = 600;

	// 广告相关
	public static final String MOGO_ID = "1080a45a6bbf4229806da0be43ccb561";
	public static final String WAPS_ID ="153fdf364dc7acda462e8080ed099589"; 
	public static final String BAIDU_APP_SID="d58e49b3";
	public static final String BAIDU_APP_SEC="d58e49b3";
	public static final int DIANJIN_APP_ID=43422;
	public static final int DIANJIN_CHANNEL_ID=1001;
	public static final String DIANJIN_APP_KEY="675fc1bd561767b37c14cd49ecaf78d0";
	//public static AdsMogoLayout adsMogoLayoutCode;
	//public static final String MOGO_ID = "4d702bc9889f4a0a9e417e492eb1e35e";//for test
	public static final int POINT_PRICE=400;
	public static int wanpuPoints=0;
	//public static int PAY_POINT_AMOUNT=0;
	//public static final String CONN_TIMEOUT = "链接服务器超时，请您稍后再试，感谢您使用360美女！";

	//定义图片显示方式
	public static final Integer NORMAL=0;
	public static final Integer SHOW_LAST=1;
	public static final Integer SHOW_NEXT=2;
		
	//线程池相关	
	public static final int CORE_POOL_SIZE =3;//1个核心工作线程   
	public static final int MAXIMUM_POOL_SIZE = 60;//最多128个工作线程   
	public static final int KEEP_ALIVE = 1;//空闲线程的超时时间为1秒   
	public static final int SD_FREE_SIZE = 2;
	//public static ExecutorService executorService=Executors.newFixedThreadPool(5);
	//缓存文件的后缀名
	public static final String CACHE_SUFFIX=".cache";
	//add start for download 2013-11-08
	public static final int NO_DOWNLOAD=0;
	public static final int DOWNLOADING=1;
	public static final int DOWNLOAD_PAUSE=2;
	public static final int DOWNLOAD_NO_DONE=3;
	public static final int DOWNLOAD_DONE=4;
	public static final String PRE_DOWN_BT_TAG="http://www.meitwo.com/bt";
	public static final String PRE_DOWN_STA_TAG="http://www.meitwo.com/sta";

	public static HashMap<String, DownloadInfo> hashMapDownloadInfo=null;
	public static MySqliteDao mySqliteDao=null;//数据库操作类
	//0：未下载过；1：正在下载；2：已下载过，但未下载完；3：下载完成
	//public static String SERVER_ADDRESS="360mt.meitwo.com";//socket 的ip地址
	public static String SERVER_ADDRESS = "10.0.2.2";
	public static int SERVER_PORT=8888;//socket的端口
	public static final String GET_IMAGE_LIST="<#GET_IMAGE_LIST#>";
	public static final String GET_IMAGE="<#GET_IMAGE#>";
	//add end 
	
	public static int PAY_POINT=0;

	
	public static boolean islog=true;


}
