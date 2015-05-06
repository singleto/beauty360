package com.android.download;

import java.io.File;
import java.io.IOException;

import com.android.beauty360.R;
import com.android.info.AlbumInfo;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.info.ImageInfo;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class BatchDownloadThread extends Thread {

	// add start for thread control method about suspend and resume at
	// 2013-11-12
	private boolean suspend = false;
	private String control = new String("Thread Control"); // 只是需要一个对象而已，这个对象没有实际意义
	private int i = 0;

	public void setSuspend(boolean suspend) {
		if (!suspend) {
			synchronized (control) {
				control.notifyAll();
			}
		}
		this.suspend = suspend;
	}

	public boolean isSuspend() {
		return this.suspend;
	}
	// add end
	private MyInterface myInterface;
	// private MySocket mySocket = null;
	private Context mContext = null;
	private String mAlbumId = null;
	private String mCataId = null;
	private AlbumInfo mAlbumInfo = null;
	// private Bitmap mBitmap = null;
	private DownloadInfo downInfo = null;
	Bitmap mBitmap;

	public BatchDownloadThread(MyInterface myInterface, DownloadInfo downInfo,
			Context mContext) {
		super();
		this.myInterface = myInterface;
		this.downInfo = downInfo;
		this.mAlbumId = downInfo.getmAlbumId();
		this.mCataId = downInfo.getCategoryId();
		this.mContext = mContext;
	}

	final Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				int i = (int) msg.arg1;
				MyUtils.outLog("BatchDownloadThread what is:0 and i is:"+i);
				String sInfoFormat = mContext.getResources().getString(R.string.downloading);
				String sFinalInfo = String.format(sInfoFormat, i, i);

				myInterface.updateStatus(downInfo);
			} else if (msg.what == 1) {// 下载图片完成
				// 更改按钮的状态和按钮的提示信息
				// 修改提示信息

				// String
				// strButtonText=mContext.getResources().getString(R.string.down_over);
				// String strStatusText=
				// String.format(mContext.getResources().getString(
				// R.string.downloading),(int)msg.arg1);
					myInterface.updateStatus(downInfo);
					MyUtils.outLog("BatchDownloadThread what is:1");

			} else if (msg.what == 2) {// 下载完单张图片，但还未完成整个专辑的下载
				// 更改按钮的状态和按钮的提示信息
				// 修改提示信息
				// String
				// strButtonText=mContext.getResources().getString(R.string.pause_download);
				// String strStatusText=
				// String.format(mContext.getResources().getString(
				// R.string.downloading),(int)msg.arg1);
				// myInterface.updateStatus(downInfo);

				// System.out.println("what is:2");

			} else if (msg.what == 5) {// 空间不够了
				MyUtils.outLog("BatchDownloadThread what is:5");
				myInterface.showToast(mContext.getResources().getString(R.string.lack_of_space));
				// 提示空间不足的信息
			}
		}
	};

	@Override
	public void run() {

		MyUtils.outLog("download info is:"+downInfo.toString());
		if (Constants.mCatagorysInfo == null) {// 如果为空则无法继续，直接返回
			return;//
		} else {// 否则

			for (int i = 0; i < Constants.mCatagorysInfo.size(); i++) {// 遍历Constants.mCatagorysInfo，
																		// 获取专辑对象
				if (Constants.mCatagorysInfo.get(i).getId().equals(mCataId)) {// 如果类别ID等于当前专辑隶属的id，则取出
					mAlbumInfo = Constants.mCatagorysInfo.get(i).getAlbumInfo(
							mAlbumId);
					break;
				}
			}
		}
		mAlbumInfo.getArrImageFormServer();// 从服务器获取专辑下的所有图片对象信息
		if (mAlbumInfo.getArrImageInfo() != null) {// 针对每个图片对象，下载图片
			for (int i = 0; i < mAlbumInfo.getArrImageInfo().size(); i++) {
				// add start for thread control method about suspend and resume
				// at 2013-11-12
				synchronized (control) {
					if (suspend) {
						try {
							control.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				// add end
				// 如果空间不够，则给出提示信息，并退出线程
				if (!Constants.SD_INSERTED
						|| Constants.SD_FREE_SIZE > FileUtils.freeSpaceOnSd()) {
					Message message = myHandler.obtainMessage();
					message.what = 5;
					myHandler.sendMessage(message);
				}

				ImageInfo mImageInfo = mAlbumInfo.getArrImageInfo().get(i);
				File fImage = new File(Constants.DOWNLOAD_PATH
						+ this.mAlbumInfo.getId() + "/"
						+ mImageInfo.getImgName());

				// 更新下载状态

				if (fImage.exists()) {// 如果文件已经存在，则继续
					MyUtils.outLog("image " + mImageInfo.getImgName()
							+ " exists");
					downInfo.setDownloadedAmout(i + 1);
					Message message = myHandler.obtainMessage();
					message.what = 1;
					myHandler.sendMessage(message);
					continue;
				}

				// MyUtils.putDataToSPF(mContext, "temp",
				// this.mAlbumInfo.getId(),
				// this.mAlbumInfo.getmAlbumName());//将albumID和name存到spf文件中

				String strUrl = Constants.REQ_SERV
						+ "?"
						+ Constants.urlParams.getDownImageParam(
								mImageInfo.getAlbumId(), mImageInfo.getImgId());
				mBitmap = HttpDownloader.loadImageFromUrl(strUrl,
						myHandler);

				// 将下载的图片存储到本地
				if (null != mBitmap) {// 如果图片
					try {
						boolean saveRes = FileUtils.autoSaveImage(
								Constants.DOWNLOAD_PATH
										+ this.mAlbumInfo.getId(),
								Constants.DOWNLOAD_PATH
										+ this.mAlbumInfo.getId() + "/"
										+ mImageInfo.getImgName(), mBitmap);
						if (saveRes) {// 如果保存成功
							// 更新下载状态
							downInfo.setDownloadedAmout(i + 1);
							Message message = myHandler.obtainMessage();
							message.what = 1;
							myHandler.sendMessage(message);
						}
					} catch (IOException e) {
						Log.v(Constants.LOG_TAG, "" + e.toString());
					}
					// 释放图片内存
					MyUtils.recycleBitmap(mBitmap);
				}

				// 暂停一会
				try {
					sleep(200);// 暂停一会，避免对服务造成压力
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}

}
