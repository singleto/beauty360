package com.android.info;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.android.beauty360.R;
import com.android.download.BatchDownloadThread;
import com.android.utils.MyUtils;

//��¼ר�����ص������Ϣ
public class DownloadInfo {
	
	private int downloadedAmout=0;//�����ص�ͼƬ������Ĭ��0
	private int curDownloadNum;//��ǰ�������ص�X��
	private BatchDownloadThread downThread=null;//��������߳�
	private int downStatus=Constants.NO_DOWNLOAD;
	private String mAlbumName;
	//private String albumDir;
	private String mAlbumId;
	private String categoryId;
	private String categoryName;
	private int picAmount;
	private String iconName;
	private String downLoadAmount;//��ר���ܵı���������
	private String commentAmount;
	private String clickAmount;
	private String praiseAmount;
	private String insTime;
	private String updTime;
	
	private ArrayList<ImageInfo> arrImageInfo=null; 
	///private ArrayList<File> arrImageFile=null;
	public DownloadInfo(String mAlbumId, String categoryId,
			int downloadedAmout) {
		super();
		this.downloadedAmout = downloadedAmout;
		this.mAlbumId = mAlbumId;
		this.categoryId = categoryId;

		for (int i = 0; i < Constants.mCatagorysInfo.size(); i++) {
			if (Constants.mCatagorysInfo.get(i).getId().equals(categoryId)) {
				AlbumInfo albumInfo = Constants.mCatagorysInfo.get(i)
						.getAlbumInfo(mAlbumId);
				this.categoryName = albumInfo.getCategoryName();
				// this.arrImageInfo=albumInfo.getArrImageInfo();
				this.mAlbumName = albumInfo.getmAlbumName();
				this.iconName = albumInfo.getIconName();
				this.picAmount=Integer.parseInt(albumInfo.getPicAmount());
				MyUtils.outLog("DownloadInfo this.picAmount is:"+this.picAmount);
			}
		}
	}
		
	public DownloadInfo() {
		super();
	}

	public String getmAlbumId() {
		return mAlbumId;
	}
	public void setmAlbumId(String mAlbumId) {
		this.mAlbumId = mAlbumId;
	}
	public BatchDownloadThread getDownThread() {
		return downThread;
	}
	public void setDownThread(BatchDownloadThread downThread) {
		this.downThread = downThread;
	}
	public int getDownloadedAmout() {
		return downloadedAmout;
	}
	public void setDownloadedAmout(int downloadedAmout) {
		this.downloadedAmout = downloadedAmout;
	}
	public String getmAlbumName() {
		return mAlbumName;
	}
	public void setmAlbumName(String mAlbumName) {
		this.mAlbumName = mAlbumName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getPicAmount() {
		return picAmount;
	}

	public void setPicAmount(int picAmount) {
		this.picAmount = picAmount;
	}

	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getDownLoadAmount() {
		return downLoadAmount;
	}
	public void setDownLoadAmount(String downLoadAmount) {
		this.downLoadAmount = downLoadAmount;
	}
	public String getCommentAmount() {
		return commentAmount;
	}
	public void setCommentAmount(String commentAmount) {
		this.commentAmount = commentAmount;
	}
	public String getClickAmount() {
		return clickAmount;
	}
	public void setClickAmount(String clickAmount) {
		this.clickAmount = clickAmount;
	}
	public String getPraiseAmount() {
		return praiseAmount;
	}
	public void setPraiseAmount(String praiseAmount) {
		this.praiseAmount = praiseAmount;
	}
	public ArrayList<ImageInfo> getArrImageInfo() {
		return arrImageInfo;
	}
	public void setArrImageInfo(ArrayList<ImageInfo> arrImageInfo) {
		this.arrImageInfo = arrImageInfo;
	}

	public int getCurDownloadNum() {
		return curDownloadNum;
	}
	public void setCurDownloadNum(int curDownloadNum) {
		this.curDownloadNum = curDownloadNum;
	}
	
	public String getInsTime() {
		return insTime;
	}
	public void setInsTime(String insTime) {
		this.insTime = insTime;
	}
	public String getUpdTime() {
		return updTime;
	}
	public void setUpdTime(String updTime) {
		this.updTime = updTime;
	}
	
	public int getDownStatus() {
		return downStatus;
	}

	public void setDownStatus(int downStatus) {
		this.downStatus = downStatus;
	}

	/**
	 * ��ȡ��ǰ���ذ�ťӦ��չʾ������
	 * @param mAlbumInfo
	 * @param mContext
	 * @return
	 */
	public static String getDownButtonTxt(String albumId,Context mContext){
		
		String downButtonTxt="";
		DownloadInfo downInfo=null;
		if(Constants.hashMapDownloadInfo!=null){
			downInfo=Constants.hashMapDownloadInfo.get(albumId);
		}
		if(downInfo!=null){			
			MyUtils.outLog("downInfo.downStatus is:"+downInfo.downStatus);
			if (downInfo.downStatus == Constants.DOWNLOAD_NO_DONE//���Ϊ�����������ͣ״̬����ť��ʾ��������
					|| downInfo.downStatus == Constants.DOWNLOAD_PAUSE) {
				downButtonTxt = mContext.getResources().getString(
						R.string.continue_download);
			} else if (downInfo.downStatus == Constants.DOWNLOAD_DONE) {//����Ѿ�������ɣ���ť��ʾ������ɡ�
				downButtonTxt = mContext.getResources().getString(
						R.string.down_over);
			} else if (downInfo.downStatus == Constants.DOWNLOADING) {//����������أ���ť��ʾ��ͣ
				downButtonTxt = mContext.getResources().getString(
						R.string.pause_download);
			} else {//��������¶���ʾ�����ء�������δ���ع������
				downButtonTxt = mContext.getResources()
						.getString(R.string.download);
			}
		}else{
			downButtonTxt = mContext.getResources()
					.getString(R.string.download);
		}
		return downButtonTxt;
	}
	
	/**
	 * ��ȡ��ǰ����״̬��ťӦ��չʾ������
	 * @param mAlbumInfo
	 * @param mContext
	 * @return
	 */
	public static String getDownStatusTxt(String albumId, Context mContext) {
		String downStatusTxt = "";
		DownloadInfo downInfo = null;
		if (Constants.hashMapDownloadInfo != null) {
			downInfo = Constants.hashMapDownloadInfo.get(albumId);
		}
		if(downInfo==null){
			downStatusTxt = String.format(mContext.getResources()
					.getString(R.string.downloaded), 0);	
		}else{
			downStatusTxt = String.format(mContext.getResources()
					.getString(R.string.downloaded), downInfo
					.getDownloadedAmout());	
		}

		return downStatusTxt;
	}
	/**
	 * ��ȡ����downInfo��״̬
	 * @param mAlbumInfo
	 * @return
	 */
	public int initStatus(AlbumInfo mAlbumInfo) {
		// ��������б�Ϊ�գ����ߴ�ר��û�����ع�
		int statusRes = Constants.NO_DOWNLOAD;
		if (this.getDownloadedAmout() == 0) {
			statusRes = Constants.NO_DOWNLOAD;
		} else if (Integer.parseInt(mAlbumInfo.getPicAmount()) <= this
				.getDownloadedAmout()) {
			statusRes = Constants.DOWNLOAD_DONE;
		} else if (Integer.parseInt(mAlbumInfo.getPicAmount()) > this
				.getDownloadedAmout()) {
			statusRes = Constants.DOWNLOAD_NO_DONE;
		}
		this.setDownStatus(statusRes);
		return statusRes;
	}
	/**
	 * ��ȡ����Ŀ¼�µ�����ͼƬ�ļ�
	 * @return
	 */
	public boolean getImageInfoFromLocal() {
		boolean bRes=true;
		this.arrImageInfo=new ArrayList<ImageInfo>();
		
		File mFile = new File(Constants.DOWNLOAD_PATH + this.mAlbumId);
		if (mFile == null || !mFile.exists() || mFile.isFile()) {
			bRes = false;
			return bRes;
		}

		File[] fileArray = mFile.listFiles();
		for (File f : fileArray) {
//			if(f.getName().equals(this.iconName)){
//				continue;
//			}
			ImageInfo imageInfo=new ImageInfo();
			imageInfo.setImgName(f.getName());
			imageInfo.setAlbumId(this.mAlbumId);
			imageInfo.setCataId(this.categoryId);
			this.arrImageInfo.add(imageInfo);			
		}
		return bRes;
	}

	@Override
	public String toString() {
		return "DownloadInfo [downloadedAmout=" + downloadedAmout
				+ ", curDownloadNum=" + curDownloadNum + ", downThread="
				+ downThread + ", downStatus=" + downStatus + ", mAlbumName="
				+ mAlbumName + ", mAlbumId=" + mAlbumId + ", categoryId="
				+ categoryId + ", categoryName=" + categoryName
				+ ", picAmount=" + picAmount + ", iconName=" + iconName
				+ ", downLoadAmount=" + downLoadAmount + ", commentAmount="
				+ commentAmount + ", clickAmount=" + clickAmount
				+ ", praiseAmount=" + praiseAmount + ", insTime=" + insTime
				+ ", updTime=" + updTime + ", arrImageInfo=" + arrImageInfo
				+ "]";
	}

}
