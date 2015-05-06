package com.android.update;

import com.android.beauty360.R;
import com.android.info.Constants;
import com.android.utils.MyUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class MyUpdate {
	
	//相关常量
	//public String updateSaveName = "";
	//public static String PUBLISH_CHANNEL = "default";
	//public static String PACKAGE_NAME = "com.android.beauty360";
	public String updateDlgTitle="";
	//public String updateUrl="";//Constants.REQ_SERV+"?"+Constants.urlParams.getUpdateParam();//确保参数已经初始化
	private boolean isBind = false ;//标识是否绑定service
	//public static boolean isShowUpdateDig=false;//是否展示过update dialog

	Context mContext;

	public MyUpdate(Context mContext) {
		super();
		this.mContext = mContext;
		this.updateDlgTitle=mContext.getResources().getString(R.string.update_dlg_title);

	}

	// 自动升级相关
	public DownloadService.DownloadBinder binder=null;
	public ServiceConnection conn=null;
	public Intent downSevIntent = null;
	//public boolean hasShowUpdateDig = false;

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public DownloadService.DownloadBinder getBinder() {
		return binder;
	}

	public void setBinder(DownloadService.DownloadBinder binder) {
		this.binder = binder;
	}

	public ServiceConnection getConn() {
		return conn;
	}

	public void setConn(ServiceConnection conn) {
		this.conn = conn;
	}

	public Intent getDownSevIntent() {
		return downSevIntent;
	}

	public void setDownSevIntent(Intent downSevIntent) {
		this.downSevIntent = downSevIntent;
	}



	public String getUpdateDlgTitle() {
		return updateDlgTitle;
	}

	public void setUpdateDlgTitle(String updateDlgTitle) {
		this.updateDlgTitle = updateDlgTitle;
	}

	public void update() {

		if (Constants.appInfo != null 
				&& Constants.appInfo.getIsNeedUpdate()) {
		
			// 判断是否需要升级
			conn = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName name,
						IBinder service) {
					binder = (DownloadService.DownloadBinder) service;
				}

				@Override
				public void onServiceDisconnected(ComponentName name) {
					Log.v(Constants.LOG_TAG, "onServiceDisconnected");

				}
			};

			Intent intent = new Intent(mContext, DownloadService.class);
			this.mContext.startService(intent); // 如果先调用startService,则在多个服务绑定对象调用unbindService后服务仍不会被销毁
			isBind = mContext.bindService(
					intent, conn, Context.BIND_AUTO_CREATE);
			doNewVersionUpdate();
		}
	}

	// 弹出自动升级窗口
	private void doNewVersionUpdate( ) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.appInfo.getStrUpdateTips());
		Dialog dialog = new AlertDialog.Builder(mContext)
				.setCancelable(false)
				.setTitle(this.updateDlgTitle)
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton(this.mContext.getResources().getString(R.string.update),// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								binder.start();// 启动升级服务
								dialog.cancel();
							}
						})
				.setNegativeButton(this.mContext.getResources().getString(R.string.no_update_now),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) { // 点击"取消"按钮之后退出程序
								dialog.cancel();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}
	
	public void stopService() {
		MyUtils.outLog("stopService 001");
		if (isBind==true && this.getBinder() != null) {
			MyUtils.outLog("stopService 002");

			mContext.unbindService(conn);
			isBind=false;
			Intent downSevIntent = new Intent();
			downSevIntent.setClass(mContext, DownloadService.class);
			mContext.stopService(downSevIntent);
		}
	}
}
