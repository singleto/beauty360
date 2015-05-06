package com.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import com.adsmogo.adview.AdsMogoLayout;
import com.android.beauty360.R;
import com.android.beauty360.point.MyPoints;
import com.android.info.AlbumInfo;
import com.android.info.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MyUtils {
	static final int ERROR = -1;//for 内存相关
	static ProgressDialog mProgressBarDialog = null;// for 图片处理相关

	///=====================网络相关start============================
	/**
	 * 查看当前的网络状态
	 * 
	 * @return
	 */
/*	public static boolean  checkNetwork(Context mContext) {
		// 实例化ConnectivityManager
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获得当前网络信息
		NetworkInfo info = manager.getActiveNetworkInfo();
		Log.v(Constants.LOG_TAG, "info.isConnected() is:"+info.isConnected());
		// 判断是否连接
		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}
*/	
	
	/** 
	* 检验网络连接 并toast提示 
	* 
	* @return 
	*/ 
	public static boolean checkNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// 当前网络不可用
			Toast.makeText(context, context.getResources().getString(R.string.no_network_tip),
					Toast.LENGTH_LONG).show();
			return false;
		}
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		if (!wifi) { // 提示使用wifi
			Toast.makeText(context,context.getResources().getString(R.string.wifi_suggest_tip),
					Toast.LENGTH_LONG).show();
		}
		return true;

	}
	///=====================网络相关end============================
	
	///=====================内存相关start============================
	static public boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	static public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	static public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	static public long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	static public long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	static public String formatSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KiB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MiB";
				size /= 1024;
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

/*	private static void curExternalStorageInfo() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		long availableBlocks = stat.getAvailableBlocks();
	}*/

/*	private String formatSize(long size) {
		return Formatter.formatFileSize(this, size);
	}*/
	///=====================内存相关end============================

	//======================其他start============================
	
	/**
	 * 获取当前版本的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurVerCode(Context mContext) {
		int verCode = -1;
		try {
			verCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
		return verCode;
	}
	
	public static String getCurVerName(Context mContext) {
		String verName = "";
		try {
			verName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
			Log.v(Constants.LOG_TAG, "verCode is:"+verName);
		} catch (NameNotFoundException e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
		return verName;
	}

	/**
	 * 程序退出
	 */
	
	public static void quitApp() {
		//杀死Application   

		int nPid = android.os.Process.myPid();
		Log.v(Constants.LOG_TAG, "app pid is:"+nPid);
		android.os.Process.killProcess(nPid);
	}
	
	/**
	 * 回收图片占用的内存
	 * @param mBitmap
	 */
	public static void recycleBitmap(Bitmap mBitmap) {
		if (null!=mBitmap&&!mBitmap.isRecycled()) {
			mBitmap.recycle(); // 回收图片所占的内存
			System.gc(); // 提醒系统及时回收
		}
	}
	
	public static AlbumInfo getAlbumInfo(String cataId,String albumId){
		AlbumInfo mAlbumInfo=null;
		//初始化所有类别下的专辑下载状态
		if(Constants.mCatagorysInfo!=null){
			for(int i=0;i<Constants.mCatagorysInfo.size();i++){
				if(Constants.mCatagorysInfo.get(i).getId().equals(cataId)){
					mAlbumInfo=Constants.mCatagorysInfo.get(i).getAlbumInfo(albumId);
					break;
				}
			}
		}
		
		return mAlbumInfo;
		
	}
	//================其他end============================
	
	
	//===============图片处理相关start============================
	/*
	 * 得到图片字节流 数组大小
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}


	/**
	 * 设置壁纸
	 */
	public static void setBitmap(final Context mContext, final Bitmap bitmap) {

		mProgressBarDialog = new ProgressDialog(mContext);
		mProgressBarDialog.setMessage("正在设置壁纸，请稍候...");
		mProgressBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressBarDialog.show();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (mProgressBarDialog.isShowing()) {
					mProgressBarDialog.cancel();
				}
				AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(
						mContext)
						.setMessage("壁纸设置成功，欢迎继续欣赏！")
						.setCancelable(true)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog mAlert = mAlertDialogBuilder.create();
				mAlert.show();
			}
		};

		new Thread() {
			public void run() {
				try {
					mContext.setWallpaper(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message = handler.obtainMessage(0);
				handler.sendMessage(message);
			};
		}.start();
	}
	//===========图片处理相关end============================
 
	public static boolean exitDig(final Activity mContext) {
		// TODO Auto-generated method stub

			// For 万普 ---------手动获取推送广告
			// if(Constants.appInfo.getIsAddAds()){
			// AppConnect.getInstance(this).getPushAd();
			// }
			
			System.out.println("Constants.urlParams=="+Constants.urlParams);
			AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(mContext);
			alertbBuilder
					.setTitle("退出提醒")
					.setMessage("你确定要离开吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Log.v(Constants.LOG_TAG,
											"start finish autoUpdate");

									// 自动升级--停掉服务
									if (Constants.myUpdate.getBinder() != null) {
										Constants.myUpdate.stopService();
										Log.v(Constants.LOG_TAG,
												"starting finish autoUpdate");
									}
									Constants.mySqliteDao.closeDb();//关闭数据库
									Log.v(Constants.LOG_TAG,
											"finished autoUpdate");
									// 芒果广告释放资源
									// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
									// 此方法请不要轻易调用，如果调用时间不当，会造成无法统计计数
									//new MyPoints(mContext,null).clear();
									AdsMogoLayout.clear();
									// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
									// 此方法请不要轻易调用，如果调用时间不当，会造成无法统计计数
	//								Constants.adsMogoLayoutCode.clearThread();
									//AdOffer myOffer=new AdOffer(mContext,null);
									MyPoints myPoints=new MyPoints(mContext,null);
									myPoints.clear();
									//myOffer.clear();
									// if (adsMogoLayout != null) {
									// adsMogoLayout.clearThread();
									// }
									// for 万普广告
									// if(Constants.appInfo.getIsAddAds()){
									// }
									// 结束这个Activity
//									Activity mActivity=(Activity)mContext;
//									mActivity.finish();
									
									MyUtils.quitApp();

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							}).create();
			alertbBuilder.show();
			return true;
	}
	
	/**
	 * 日志输出
	 * @param logs ：日志内容
	 */
	public static void outLog(String logs){
		if(Constants.islog){
			Log.v(Constants.LOG_TAG, logs);
		}
		
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String nowTime() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR)
				+ "-"
				+ formatTime(c.get(Calendar.MONTH) + 1)
				+ "-"
				+ // month加一
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + " "
				+ formatTime(c.get(Calendar.HOUR_OF_DAY)) + ":"
				+ formatTime(c.get(Calendar.MINUTE)) + ":"
				+ formatTime(c.get(Calendar.SECOND)) + ":"
				+ formatTime(c.get(Calendar.MILLISECOND));

		return time;
	}
	
	/**
	 * 格式化时间函数，服务于nowTime
	 * @param t
	 * @return
	 */
	public static String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// 三元运算符 t>10时取 ""+t
	}
	
	/**
	 * 存储数据到SharedPreferences
	 * @param mContext ：上下文
	 * @param spfName ：SPF文件的名字
	 * @param strKey ：key值
	 * @param strValue ：value值
	 */
	public static void putDataToSPF(Context mContext,String spfName,String strKey,String strValue){
		
		SharedPreferences sp = mContext.getSharedPreferences(spfName, Context.MODE_PRIVATE); 
		//存入数据            
		Editor editor = sp.edit();                                           
		editor.putString(strKey, strValue);
		editor.commit();
		outLog("SharedPreferences size is:"+sp.getAll().size());
	}
	
	/**
	 * 从SharedPreferences中，根据key值取数据
	 * @param mContext
	 * @param spfName
	 * @param strKey
	 * @return
	 */
	public static String getDataFormSPF(Context mContext,String spfName,String strKey){
		String strRes;
		SharedPreferences sp = mContext.getSharedPreferences(spfName, Context.MODE_PRIVATE); 
		//存入数据            
		strRes=sp.getString(strKey, "none");
		return strRes;
	}	
	
	
	public static void showDlg(Context mContext,String strtitle,String msg) {
		Dialog tipsDialog = new AlertDialog.Builder(mContext)
				.setTitle(strtitle)
				.setMessage(msg)
				.setPositiveButton(mContext.getResources().getString(R.string.bt_ok),// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		tipsDialog.show();
	}
	
	/**
	 * 显示积分不足的对话框
	 * @param mContext
	 */
	public static void showPointLackDlg(Context mContext,
			final MyPoints myPoints) {
		AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(mContext);
		alertbBuilder
				.setTitle(
						mContext.getResources().getString(
								R.string.lack_points_tip))
				.setMessage(
						mContext.getResources().getString(
								R.string.get_points_tips))
				.setPositiveButton(
						mContext.getResources().getString(R.string.get_points),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								myPoints.showGetPointDlg();
								// AppConnect.getInstance(PicViewer.this).showOffers(PicViewer.this);
								dialog.cancel();
							}
						})
				.setNegativeButton(
						mContext.getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();

							}
						}).create();
		alertbBuilder.show();
	}

}
