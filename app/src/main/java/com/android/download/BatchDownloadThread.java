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
	private String control = new String("Thread Control"); // ֻ����Ҫһ��������ѣ��������û��ʵ������
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
			} else if (msg.what == 1) {// ����ͼƬ���
				// ���İ�ť��״̬�Ͱ�ť����ʾ��Ϣ
				// �޸���ʾ��Ϣ

				// String
				// strButtonText=mContext.getResources().getString(R.string.down_over);
				// String strStatusText=
				// String.format(mContext.getResources().getString(
				// R.string.downloading),(int)msg.arg1);
					myInterface.updateStatus(downInfo);
					MyUtils.outLog("BatchDownloadThread what is:1");

			} else if (msg.what == 2) {// �����굥��ͼƬ������δ�������ר��������
				// ���İ�ť��״̬�Ͱ�ť����ʾ��Ϣ
				// �޸���ʾ��Ϣ
				// String
				// strButtonText=mContext.getResources().getString(R.string.pause_download);
				// String strStatusText=
				// String.format(mContext.getResources().getString(
				// R.string.downloading),(int)msg.arg1);
				// myInterface.updateStatus(downInfo);

				// System.out.println("what is:2");

			} else if (msg.what == 5) {// �ռ䲻����
				MyUtils.outLog("BatchDownloadThread what is:5");
				myInterface.showToast(mContext.getResources().getString(R.string.lack_of_space));
				// ��ʾ�ռ䲻�����Ϣ
			}
		}
	};

	@Override
	public void run() {

		MyUtils.outLog("download info is:"+downInfo.toString());
		if (Constants.mCatagorysInfo == null) {// ���Ϊ�����޷�������ֱ�ӷ���
			return;//
		} else {// ����

			for (int i = 0; i < Constants.mCatagorysInfo.size(); i++) {// ����Constants.mCatagorysInfo��
																		// ��ȡר������
				if (Constants.mCatagorysInfo.get(i).getId().equals(mCataId)) {// ������ID���ڵ�ǰר��������id����ȡ��
					mAlbumInfo = Constants.mCatagorysInfo.get(i).getAlbumInfo(
							mAlbumId);
					break;
				}
			}
		}
		mAlbumInfo.getArrImageFormServer();// �ӷ�������ȡר���µ�����ͼƬ������Ϣ
		if (mAlbumInfo.getArrImageInfo() != null) {// ���ÿ��ͼƬ��������ͼƬ
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
				// ����ռ䲻�����������ʾ��Ϣ�����˳��߳�
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

				// ��������״̬

				if (fImage.exists()) {// ����ļ��Ѿ����ڣ������
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
				// this.mAlbumInfo.getmAlbumName());//��albumID��name�浽spf�ļ���

				String strUrl = Constants.REQ_SERV
						+ "?"
						+ Constants.urlParams.getDownImageParam(
								mImageInfo.getAlbumId(), mImageInfo.getImgId());
				mBitmap = HttpDownloader.loadImageFromUrl(strUrl,
						myHandler);

				// �����ص�ͼƬ�洢������
				if (null != mBitmap) {// ���ͼƬ
					try {
						boolean saveRes = FileUtils.autoSaveImage(
								Constants.DOWNLOAD_PATH
										+ this.mAlbumInfo.getId(),
								Constants.DOWNLOAD_PATH
										+ this.mAlbumInfo.getId() + "/"
										+ mImageInfo.getImgName(), mBitmap);
						if (saveRes) {// �������ɹ�
							// ��������״̬
							downInfo.setDownloadedAmout(i + 1);
							Message message = myHandler.obtainMessage();
							message.what = 1;
							myHandler.sendMessage(message);
						}
					} catch (IOException e) {
						Log.v(Constants.LOG_TAG, "" + e.toString());
					}
					// �ͷ�ͼƬ�ڴ�
					MyUtils.recycleBitmap(mBitmap);
				}

				// ��ͣһ��
				try {
					sleep(200);// ��ͣһ�ᣬ����Է������ѹ��
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}

}
