package com.android.beauty360;


import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import com.android.beauty360.R;
import com.android.data.MySqliteDao;
import com.android.download.HttpDownloader;
import com.android.info.AlbumInfo;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.info.UrlParams;
import com.android.main.MainActivity;
import com.android.utils.BtJsonParser;
import com.android.utils.MyUtils;

@SuppressLint("HandlerLeak")
public class WelcomeActivity extends Activity{

	private MyHandler myHandler = null;
	private Intent mIntent = null;
	
	Thread loadingThread=null;//启动界面的loading
	
	private LoadingView loadingView;//启动界面

	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {//冲数据库中获取配置信息
				Log.v(Constants.LOG_TAG, "从数据库中获取配置信息");
				Constants.mCatagorysInfo=Constants.mySqliteDao.getCatas();
				if(Constants.mCatagorysInfo==null||Constants.mCatagorysInfo.size()==0){
					WelcomeActivity.this.quitDig(WelcomeActivity.this);//如果数据库中也没有专辑信息数据，则退出
				}else{
					WelcomeActivity.this.goNextActivity();
				}
			}else if(msg.what == 1){
			    //如果没有普通提示信息也没有升级提示，则直接转向下一页面

				if (Constants.appInfo != null
						&& false == Constants.appInfo.getIsShowNormalTips()
								.booleanValue()) {
					WelcomeActivity.this.goNextActivity();
				}
				// 展示提示信息
				if (Constants.appInfo != null
						&& Constants.appInfo.getIsShowNormalTips()) {
					showTips(Constants.appInfo.getStrNormalTips().toString());
				}
			}/*else if(msg.what == 2){
				//如果读取失败，则提示用户连接服务器超时并退出
				WelcomeActivity.this.quitDig(WelcomeActivity.this);
			}*/
		}
		
	}
	
	
	/**
	 * 推出提示框
	 * @param mContext
	 */
	public void quitDig(Context mContext){
		AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(
				mContext);
		alertbBuilder
				.setMessage(this.getResources().getString(R.string.conn_timeout))
				.setPositiveButton(WelcomeActivity.this.getResources().getString(R.string.bt_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MyUtils.quitApp();
								WelcomeActivity.this.finish();
							}
						}).create();
		alertbBuilder.show();
	}
/*
 * 定义获取版本信息的线程
 */
/*	private class GetVerInfoThread extends Thread {

		@Override
		public void run() {
			if (true==getVerInfoSucc) {
				Message mUpdateMessage = mMyHandler.obtainMessage(1);
				mMyHandler.sendMessage(mUpdateMessage);
			}else{//获取版本信息失败
				Message mUpdateMessage = mMyHandler.obtainMessage(2);
				mMyHandler.sendMessage(mUpdateMessage);
			}
		}
	}*/
	
	// 广告相关
/*	static {
		if (1 == Constants.AD_TYPE) {
			LmMobEngine.init("41a22e0bcb1d825037dac04db887a566");
			Log.v(Constants.LOG_TAG, "1==Constants.AD_TYPE");
		} else if (2 == Constants.AD_TYPE) {
		} else if (3 == Constants.AD_TYPE) {
			 //AirAD.setGlobalParameter("Your APPID", false, "Your AdMob PublisherID",false);
			Log.v(Constants.LOG_TAG, "3==Constants.AD_TYPE");
		}
	}*/


	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		
		loadingView = (LoadingView)findViewById(R.id.loading_view);
		TextView tmpTextView = (TextView)findViewById(R.id.version_name);
		tmpTextView.setText(getResources().getString(R.string.version_name, MyUtils.getCurVerName(this)));
		
		Constants.mySqliteDao=new MySqliteDao(getApplicationContext());//初始化数据库操作类
		Constants.urlParams = new UrlParams(WelcomeActivity.this);// 初始化请求参数，创建URL请求的参数对象，包括IMEI，IMSI等

		myHandler = new MyHandler();		
		mIntent = new Intent();
		
		//万普广告初始化
//      AppConnect.getInstance(this);
//		Log.v(Constants.LOG_TAG, "初始化万普广告");
        
        initLoadingImages();
        
        this.loadingThread=new Thread()
        {
            @Override
            public void run()
            {
            	loadingView.startAnim();
            }
        };
        loadingThread.start();
        
        
		if (MyUtils.checkNetwork(WelcomeActivity.this)) {

			new Thread() {
				@Override
				public void run() {
					WelcomeActivity.this.BtInit();
				}
			}.start();
		}else{
			Message mAlertMessage = myHandler.obtainMessage(0);
			myHandler.sendMessage(mAlertMessage);
		}
	}

	private void initLoadingImages()
	{
	    int[] imageIds = new int[6];
	    imageIds[0] = R.drawable.loader_frame_1;
	    imageIds[1] = R.drawable.loader_frame_2;
	    imageIds[2] = R.drawable.loader_frame_3;
	    imageIds[3] = R.drawable.loader_frame_4;
	    imageIds[4] = R.drawable.loader_frame_5;
	    imageIds[5] = R.drawable.loader_frame_6;
	    
	    loadingView.setImageIds(imageIds);
	}
	/**
	 * 获取xml文件中图片专辑信息
	 * 
	 * @return
	 */
/*	public boolean getXmlInfo() {
		String resultStr = null;
		// HttpDownloader hd = new HttpDownloader();
		ProgressDialog pDialog = new ProgressDialog(WelcomeActivity.this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Loading….");
		pDialog.show();
		Log.v(Constants.LOG_TAG, "pDialog is:"+pDialog);
		//String resultStr = FileUtils.readFileSdcardFile(Constants.LOCAL_PATH+Constants.CONFIG_FILE);
		if (NetWorkUtils.checkNetwork(this)) {//如果网络正常，从服务器上读取配置文件
			resultStr = HttpDownloader.downloadFile(this,
					Constants.CATEGORYS_INFO_URL, "GB2312");
			
			 * if(null==resultStr &&
			 * FileUtils.isFileExist(Constants.LOCAL_PATH+Constants
			 * .CONFIG_FILE)){ Log.v(Constants.LOG_TAG, "read json from sd");
			 * resultStr =
			 * FileUtils.readFileSdcardFile(Constants.LOCAL_PATH+Constants
			 * .CONFIG_FILE); }
			 
		}
		//Log.v(Constants.LOG_TAG, resultStr);
		if(null!=resultStr && resultStr.contains("verCode")){//如果网络正常且读配置文件成功
			new Thread(){
				@Override
				public void run() {
					FileUtils.writeFile2SDFromInput("/", Constants.CONFIG_FILE, HttpDownloader
							.getInputStreamForTest(Constants.CATEGORYS_INFO_URL));						
					// TODO Auto-generated method stub
				}					
			}.start();
		}else if(FileUtils.isFileExist(Constants.LOCAL_PATH//如果网络不正常或者读服务器的配置文件失败，且本地有配置文件
					+ Constants.CONFIG_FILE)){
				resultStr = FileUtils.readFileSdcardFile(Constants.LOCAL_PATH
						+ Constants.CONFIG_FILE);
		}
		
		//pDialog.dismiss();
		//健壮性处理，如果读取文件失败，提示连接服务器超时
		if(null==resultStr || !resultStr.contains("verCode")){
			quitAlert(Constants.CONN_TIMEOUT);
		}
		
		try {
			// 创建一个SAXParserFactory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			MyContentHandler mHandler = new MyContentHandler();
			// 为XMLReader设置内容处理器
			reader.setContentHandler(mHandler);
			// 开始解析文件
			//reader.parse(new InputSource(new StringReader(resultStr)));
			resultStr =resultStr.substring(resultStr.indexOf("{"),resultStr.lastIndexOf("}")+1);
			JsonParser jsonParser=new JsonParser();
			Constants.mCategorysInfo=jsonParser.catagorysParser(resultStr);						
			//Constants.mCategorysInfo=mHandler.getCategorysInfo();
			Constants.verInfo=jsonParser.versionInfoParser(resultStr);
			///==========for 安卓市场

			if(Constants.appInfo.getIsShowRealVer()==false){
				Constants.PRE_URL = "http://www.meitwo.com/adApp/";
				String tempStr = HttpDownloader.downloadFile(this,
						Constants.PRE_URL+"360beauty.json","GB2312");
				//Log.v(Constants.LOG_TAG, "tempStr is;"+tempStr);
				JsonParser jsonParserTemp=new JsonParser();
				Constants.mCategorysInfo=jsonParserTemp.catagorysParser(tempStr);						
				//Constants.mCategorysInfo=mHandler.getCategorysInfo();
				Constants.verInfo=jsonParserTemp.versionInfoParser(resultStr);


				// 开始解析adApp文件
				reader.parse(new InputSource(new StringReader(tempStr)));

				Constants.mCategorysInfo=mHandler.getCategorysInfo();
				//Constants.verInfo=mHandler.getMyVerInfo();
				//Constants.verInfo.adType=0;
			}
			///======================
			
			
		    ///for awards points to access 360beauty first
			//AppConnect.getInstance(this).spendPoints(1930, this);
			//AppConnect.getInstance(this).awardPoints(100, this);

		    AppConnect.getInstance(this).getPoints(this);

		    //========================
		} catch (Exception e) {
			//e.printStackTrace();
			Log.v(Constants.LOG_TAG, ""+e.toString());
			return false;
		}
		if(null==Constants.mCategorysInfo || null==Constants.verInfo){
			return false;
		}else{			
			if (Constants.verInfo.getStrTips().equals("")) {
				isNeedTips = false;
			} else {
				// 显示Tips对话框
				strTipContent = HttpDownloader.downloadFile(this,
						Constants.PRE_URL + Constants.verInfo.getStrTips(), "GB2312");
			}
			return true;
		}
	}*/	


	
	/**
	 * 根据数据库中存储的专辑下载信息，批量初始化专辑的下载状态，递归处理
	 * @param arrAlbum 
	 */
	private void initAlbumsDownStatus(ArrayList<AlbumInfo> arrAlbum){
		AlbumInfo albumInfo = null;
		for (int i = 0; i < arrAlbum.size(); i++) {			
			albumInfo = (AlbumInfo) arrAlbum.get(i);
			if (albumInfo.getArrSubAlbumInfo() != null) {
				initAlbumsDownStatus(albumInfo.getArrSubAlbumInfo());
			}
			DownloadInfo downInfo= Constants.hashMapDownloadInfo.get(albumInfo.getId());
			if (downInfo!=null) {
				downInfo.initStatus(albumInfo);
			}
		}
	}
	
	/**
	 * 初始化
	 */
	private void BtInit() {

		//if (MyUtils.checkNetwork(WelcomeActivity.this)) {
			BtJsonParser jsonParser = new BtJsonParser();// 生成json解析对象，用于解析请求的json串
			
			// 获取应用信息，包括tips，是否升级等
			String strJson = HttpDownloader.getResFromServer(Constants.REQ_SERV + "?"
					+ Constants.urlParams.getStartParam());
			
			Log.v(Constants.LOG_TAG, "welcom :strJson is:"+strJson);
			//如果服务无响应，或者返回的信息为空，提示用户连接超时，稍后再试
			if (strJson == null || strJson.equals("")) {
				Message mAlertMessage = myHandler.obtainMessage(0);
				myHandler.sendMessage(mAlertMessage);
			} else {
				Constants.appInfo = jsonParser.parseAppInfo(strJson);
				// 初始化类别信息
				strJson = HttpDownloader.getResFromServer(Constants.REQ_SERV
						+ "?" + Constants.urlParams.getCataParam());
				Constants.mCatagorysInfo = jsonParser.parseCata(strJson);// 解析请求的json串，并生成catagory数组
				Log.v(Constants.LOG_TAG, Constants.urlParams.getCataParam());

				// 初始化每个类别的专辑信息
//				for (int i = 0; i < Constants.mCatagorysInfo.size(); i++) {
//					Constants.mCatagorysInfo.get(i).initAlbums();
//				}
				// 将配置信息重新初始化到数据库
				//Constants.mySqliteDao.initCatas(Constants.mCatagorysInfo);				
				//分派提示信息任务
				Message mAlertMessage = myHandler.obtainMessage(1);
				myHandler.sendMessage(mAlertMessage);
			}			

//		}else{
//			Message mAlertMessage = myHandler.obtainMessage(0);
//			myHandler.sendMessage(mAlertMessage);
//		}
		
/*		if(Constants.mCatagorysInfo==null || Constants.mCatagorysInfo.size()==0){
			Message mAlertMessage = myHandler.obtainMessage(2);
			myHandler.sendMessage(mAlertMessage);
		}*/
		
		Constants.hashMapDownloadInfo=Constants.mySqliteDao.getDownInfo();
		MyUtils.outLog("Constants.hashMapDownloadInfo is:"+Constants.hashMapDownloadInfo);
		if(Constants.hashMapDownloadInfo==null){
			Constants.hashMapDownloadInfo=new HashMap<String, DownloadInfo>();
		}

		//初始化所有类别下的专辑下载状态
		if(Constants.mCatagorysInfo!=null){
			for(int i=0;i<Constants.mCatagorysInfo.size();i++){
				ArrayList<AlbumInfo> arrAlbum=Constants.mCatagorysInfo.get(i).getmArrayListAlbumsInfo();
				if(arrAlbum!=null){
					this.initAlbumsDownStatus(arrAlbum);
				}
			}
		}

		
		//Constants.myUpdate= new MyUpdate(WelcomeActivity.this);//初始化自动升级类
		
		if(Constants.appInfo!=null){
			Constants.PAY_POINT=Constants.appInfo.getPayPoints();
		}
		
		//new MyPoints(this,null).getAdPoints();//初始化广告积分
		
		//myOffer=new AdOffer(WelcomeActivity.this,null);//初始化积分墙
		//如果用户的积分等于0，则奖励部分计费
//		if (myOffer.getPoints() <= 0 && Constants.appInfo != null) {
//			myOffer.addPoints(Constants.appInfo.getAwardPoints());
//		}
		// pBar = new ProgressDialog(WelcomeActivity.this);
	}

//	class WelAnimationInListener implements AnimationListener {
//		@Override
//		public void onAnimationStart(Animation animation) {
//
//		}
//
//		@Override
//		public void onAnimationEnd(Animation animation) {
//			BtInit();//初始化相关参数
//			mIntent = new Intent();
//			WelcomeActivity.this.goNextActivity();
/*			pBar.setTitle("下载新版本");
			pBar.setMessage("请稍候...");
			pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pBar.show();
			getVerInfoSucc=WelcomeActivity.this.getXmlInfo();
			//pBar.dismiss();
			new GetVerInfoThread().start();

			if (false == isNeedUpdate && false == isNeedTips) {
				mMyHandler.postDelayed(new Runnable() {
					public void run() {
						 Create an Intent that will start the Main Activity. 
						goNextActivity();
					}
				}, 2000);
			}
			//标识动画已完成	
			isAnimComplete=true;*/
//		}
//
//		@Override
//		public void onAnimationRepeat(Animation animation) {
//
//		}
//	}

//	class WelAnimationOutListener implements AnimationListener {
//		@Override
//		public void onAnimationStart(Animation animation) {
//
//		}
//
//		@Override
//		public void onAnimationEnd(Animation animation) {
//			MyUtils.quitApp();
//			WelcomeActivity.this.finish();
//		}
//
//		@Override
//		public void onAnimationRepeat(Animation arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//	}

	// ========update version

/*	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				//pBar.cancel();
			} else if (msg.what == 1) {// 下载完版本信息后判断是否需要升级
				// pBar.cancel();
				judgeUpdate();
			} else if (msg.what == 2) {// 若获取版本信息失败，程序退出
				// pBar.cancel();
				AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(
						WelcomeActivity.this);
				alertbBuilder
						.setMessage(Constants.CONN_TIMEOUT)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										animationWelOut.setAnimationListener(new WelAnimationOutListener());
										mWelcomeImage.startAnimation(animationWelOut);
									}
								}).create();
				alertbBuilder.show();
			}
			super.handleMessage(msg);
		}
	}
*/	
/*	public void judgeUpdate() {

		int vercode = OtherUtils.getCurVerCode(WelcomeActivity.this);
		// 如果服务器版本高于当前版本，则升级，否则不需要升级，并置不需要升级的标志
		if (Constants.verInfo.getNewVerCode() > vercode) {
			doNewVersionUpdate();
		} else {
			isNeedUpdate = false;
		}
		// 如果有提示文件名，则需要提示。
		if (isNeedTips) {
			// 显示Tips对话框
			Dialog tipsDialog = new AlertDialog.Builder(WelcomeActivity.this)
					.setTitle("温馨提示").setMessage(strTipContent.toString())
					.setPositiveButton("确定",// 设置确定按钮
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 如果不需要升级，直接转向专辑页面
									if (false == isNeedUpdate) {
										dialog.cancel();
										WelcomeActivity.this.goNextActivity();

									} else {
										dialog.cancel();
									}
								}
							}).create();
			tipsDialog.show();
		}
		if (!isNeedUpdate && !isNeedTips) {
			// 不需要升级，且不需要提示，动画显示完后，直接转向专辑页面
			mMyHandler.postDelayed(new Runnable() {
				public void run() {
					 Create an Intent that will start the Main Activity. 
					goNextActivity();
				}
			}, 100);

		}
	}*/

	/*	private boolean getServerVerCode() {
		try {
			String strVerjson = HttpDownloader
					.downloadFile(this,Constants.VERSION_INFO_URL);
			JSONArray array = new JSONArray(strVerjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);

				try {
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
					strTips = obj.getString("tips");
				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			} else {
				newVerCode = -1;
				newVerName = "";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
*/

	/**
	 * 获取当前版本的版本名称
	 * 
	 * @param context
	 * @return
	 */
/*	public static String getCurVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					Constants.PACKAGE_NAME, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}*/

	/**
	 * 获取当前版本的应用名称
	 * 
	 * @param context
	 * @return
	 */
/*	public static String getCurAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}
*/

	public void showTips(String tips) {
		Dialog tipsDialog = new AlertDialog.Builder(WelcomeActivity.this)
				.setTitle(WelcomeActivity.this.getResources().getString(R.string.kindly_reminder))
				.setMessage(tips)
				.setPositiveButton(WelcomeActivity.this.getResources().getString(R.string.bt_ok),// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								WelcomeActivity.this.goNextActivity();
							}
						}).create();
		tipsDialog.show();
	}
	

/*
 * 转向下一个activity
 */
	void goNextActivity(){
//		WelcomeActivity.this.mIntent.setClass(
//				WelcomeActivity.this,
//				PicCatagoryTab.class);
//		if(this.loadingThread!=null){
//			this.loadingThread.stop();
//		}
		WelcomeActivity.this.mIntent.setClass(
				WelcomeActivity.this,
				MainActivity.class);
		WelcomeActivity.this.mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//FLAG_ACTIVITY_REORDER_TO_FRONT
		WelcomeActivity.this.startActivity(mIntent);
//		if (this.loadingThread != null) {
//			this.loadingThread.stop();
//			MyUtils.outLog("stop loaddingThread");
//		}
		WelcomeActivity.this.finish();
	}
	
/*	void downFile(final String strUrl) {
		Log.v(Constants.LOG_TAG, "UPDATE_URL is:"+strUrl);
		pBar.show();
		new Thread() {
			public void run() {
				try {
					//response = client.execute(get);
					HttpEntity entity = HttpDownloader.getEntity(strUrl);
					//InputStream is=HttpDownloader.getInputStreamFromUrl(strUrl);
					//long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								Constants.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					downFinish();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
*/
/*	void downFinish() {
		mMyHandler.post(new Runnable() {
			public void run() {
				Message mMessage = mMyHandler.obtainMessage(0);
				mMyHandler.sendMessage(mMessage);
				update();
				//openFile();
			}
		});
	}*/

/*	void update() {

//		Uri packageURI = Uri.parse("package:com.android.beauty360");
//		Intent mIntent = new Intent(Intent.ACTION_DELETE,packageURI);
//		startActivity(mIntent);
		
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Constants.UPDATE_SAVENAME)),
		                                "application/vnd.android.package-archive");

		startActivity(intent);
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(new File(Environment
//				.getExternalStorageDirectory(), Constants.UPDATE_SAVENAME)),
//				"application/vnd.android.package-archive");
//		startActivity(intent);
	}*/
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//mySqliteDao.closeDb();//关闭数据库		
        super.onDestroy();
	}

/*	private void openFile() {
		// TODO Auto-generated method stub
		// Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Constants.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}*/

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MyUtils.quitApp();
			this.finish();
		}

		return super.onKeyDown(keyCode, event);
	}
	
/*	class SaveFile extends Thread {
		String strPath;
		String fileName;
		InputStream mInStream;

		public SaveFile(String strPath, String fileName, InputStream mInStream) {
			super();
			this.strPath = strPath;
			this.fileName = fileName;
			this.mInStream = mInStream;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//Log.v(Constants.LOG_TAG, "save file");
			FileUtils.writeFile2SDFromInput(strPath, fileName, mInStream);
			//Log.v(Constants.LOG_TAG, "strPath is:"+strPath+",fileName is:"+",mInStream" +mInStream);

			// super.run();
		}
	}*/
}

