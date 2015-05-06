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
	static final int ERROR = -1;//for �ڴ����
	static ProgressDialog mProgressBarDialog = null;// for ͼƬ�������

	///=====================�������start============================
	/**
	 * �鿴��ǰ������״̬
	 * 
	 * @return
	 */
/*	public static boolean  checkNetwork(Context mContext) {
		// ʵ����ConnectivityManager
		ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ��õ�ǰ������Ϣ
		NetworkInfo info = manager.getActiveNetworkInfo();
		Log.v(Constants.LOG_TAG, "info.isConnected() is:"+info.isConnected());
		// �ж��Ƿ�����
		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}
*/	
	
	/** 
	* ������������ ��toast��ʾ 
	* 
	* @return 
	*/ 
	public static boolean checkNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// ��ǰ���粻����
			Toast.makeText(context, context.getResources().getString(R.string.no_network_tip),
					Toast.LENGTH_LONG).show();
			return false;
		}
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		if (!wifi) { // ��ʾʹ��wifi
			Toast.makeText(context,context.getResources().getString(R.string.wifi_suggest_tip),
					Toast.LENGTH_LONG).show();
		}
		return true;

	}
	///=====================�������end============================
	
	///=====================�ڴ����start============================
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
	///=====================�ڴ����end============================

	//======================����start============================
	
	/**
	 * ��ȡ��ǰ�汾�İ汾��
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
	 * �����˳�
	 */
	
	public static void quitApp() {
		//ɱ��Application   

		int nPid = android.os.Process.myPid();
		Log.v(Constants.LOG_TAG, "app pid is:"+nPid);
		android.os.Process.killProcess(nPid);
	}
	
	/**
	 * ����ͼƬռ�õ��ڴ�
	 * @param mBitmap
	 */
	public static void recycleBitmap(Bitmap mBitmap) {
		if (null!=mBitmap&&!mBitmap.isRecycled()) {
			mBitmap.recycle(); // ����ͼƬ��ռ���ڴ�
			System.gc(); // ����ϵͳ��ʱ����
		}
	}
	
	public static AlbumInfo getAlbumInfo(String cataId,String albumId){
		AlbumInfo mAlbumInfo=null;
		//��ʼ����������µ�ר������״̬
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
	//================����end============================
	
	
	//===============ͼƬ�������start============================
	/*
	 * �õ�ͼƬ�ֽ��� �����С
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
	 * ���ñ�ֽ
	 */
	public static void setBitmap(final Context mContext, final Bitmap bitmap) {

		mProgressBarDialog = new ProgressDialog(mContext);
		mProgressBarDialog.setMessage("�������ñ�ֽ�����Ժ�...");
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
						.setMessage("��ֽ���óɹ�����ӭ�������ͣ�")
						.setCancelable(true)
						.setPositiveButton("ȷ��",
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
	//===========ͼƬ�������end============================
 
	public static boolean exitDig(final Activity mContext) {
		// TODO Auto-generated method stub

			// For ���� ---------�ֶ���ȡ���͹��
			// if(Constants.appInfo.getIsAddAds()){
			// AppConnect.getInstance(this).getPushAd();
			// }
			
			System.out.println("Constants.urlParams=="+Constants.urlParams);
			AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(mContext);
			alertbBuilder
					.setTitle("�˳�����")
					.setMessage("��ȷ��Ҫ�뿪��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Log.v(Constants.LOG_TAG,
											"start finish autoUpdate");

									// �Զ�����--ͣ������
									if (Constants.myUpdate.getBinder() != null) {
										Constants.myUpdate.stopService();
										Log.v(Constants.LOG_TAG,
												"starting finish autoUpdate");
									}
									Constants.mySqliteDao.closeDb();//�ر����ݿ�
									Log.v(Constants.LOG_TAG,
											"finished autoUpdate");
									// â������ͷ���Դ
									// ��� adsMogoLayout ʵ�� ���������ڶ��̻߳�����Ƶ��̳߳�
									// �˷����벻Ҫ���׵��ã��������ʱ�䲻����������޷�ͳ�Ƽ���
									//new MyPoints(mContext,null).clear();
									AdsMogoLayout.clear();
									// ��� adsMogoLayout ʵ�� ���������ڶ��̻߳�����Ƶ��̳߳�
									// �˷����벻Ҫ���׵��ã��������ʱ�䲻����������޷�ͳ�Ƽ���
	//								Constants.adsMogoLayoutCode.clearThread();
									//AdOffer myOffer=new AdOffer(mContext,null);
									MyPoints myPoints=new MyPoints(mContext,null);
									myPoints.clear();
									//myOffer.clear();
									// if (adsMogoLayout != null) {
									// adsMogoLayout.clearThread();
									// }
									// for ���չ��
									// if(Constants.appInfo.getIsAddAds()){
									// }
									// �������Activity
//									Activity mActivity=(Activity)mContext;
//									mActivity.finish();
									
									MyUtils.quitApp();

								}
							})
					.setNegativeButton("ȡ��",
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
	 * ��־���
	 * @param logs ����־����
	 */
	public static void outLog(String logs){
		if(Constants.islog){
			Log.v(Constants.LOG_TAG, logs);
		}
		
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * @return
	 */
	public static String nowTime() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR)
				+ "-"
				+ formatTime(c.get(Calendar.MONTH) + 1)
				+ "-"
				+ // month��һ
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + " "
				+ formatTime(c.get(Calendar.HOUR_OF_DAY)) + ":"
				+ formatTime(c.get(Calendar.MINUTE)) + ":"
				+ formatTime(c.get(Calendar.SECOND)) + ":"
				+ formatTime(c.get(Calendar.MILLISECOND));

		return time;
	}
	
	/**
	 * ��ʽ��ʱ�亯����������nowTime
	 * @param t
	 * @return
	 */
	public static String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// ��Ԫ����� t>10ʱȡ ""+t
	}
	
	/**
	 * �洢���ݵ�SharedPreferences
	 * @param mContext ��������
	 * @param spfName ��SPF�ļ�������
	 * @param strKey ��keyֵ
	 * @param strValue ��valueֵ
	 */
	public static void putDataToSPF(Context mContext,String spfName,String strKey,String strValue){
		
		SharedPreferences sp = mContext.getSharedPreferences(spfName, Context.MODE_PRIVATE); 
		//��������            
		Editor editor = sp.edit();                                           
		editor.putString(strKey, strValue);
		editor.commit();
		outLog("SharedPreferences size is:"+sp.getAll().size());
	}
	
	/**
	 * ��SharedPreferences�У�����keyֵȡ����
	 * @param mContext
	 * @param spfName
	 * @param strKey
	 * @return
	 */
	public static String getDataFormSPF(Context mContext,String spfName,String strKey){
		String strRes;
		SharedPreferences sp = mContext.getSharedPreferences(spfName, Context.MODE_PRIVATE); 
		//��������            
		strRes=sp.getString(strKey, "none");
		return strRes;
	}	
	
	
	public static void showDlg(Context mContext,String strtitle,String msg) {
		Dialog tipsDialog = new AlertDialog.Builder(mContext)
				.setTitle(strtitle)
				.setMessage(msg)
				.setPositiveButton(mContext.getResources().getString(R.string.bt_ok),// ����ȷ����ť
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
	 * ��ʾ���ֲ���ĶԻ���
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
