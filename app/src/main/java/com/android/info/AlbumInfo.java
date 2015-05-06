package com.android.info;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.download.HttpDownloader;
import com.android.utils.BtJsonParser;
import com.android.utils.MyUtils;

/**
 * @author yuguangbao
 *
 */
public class AlbumInfo implements Parcelable{

//	private static final long serialVersionUID = 1L;
	private String id;
	private String mAlbumName;
	//private String albumDir;	
	private String categoryId;
	private String categoryName;
	private String picAmount;
	private String iconName;
	private String downLoadAmount;
	private String commentAmount;
	private String clickAmount;
	private String praiseAmount;
	private boolean isMoreAlbum=false;

	//private int downStatus=Constants.NO_DOWNLOAD;
	private Integer curPicNo=0;
	private Integer curSubAlbumNo=0;
	private Integer subAlbumAmount=0;
	private ArrayList<ImageInfo> arrImageInfo=null; 
	private ArrayList<AlbumInfo> arrSubAlbumInfo=null;

	public boolean isMoreAlbum() {
		return isMoreAlbum;
	}

	public void setMoreAlbum(boolean isMoreAlbum) {
		this.isMoreAlbum = isMoreAlbum;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public Integer getSubAlbumAmount() {
		return subAlbumAmount;
	}

	public void setSubAlbumAmount(Integer subAlbumAmount) {
		this.subAlbumAmount = subAlbumAmount;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public ArrayList<ImageInfo> getArrImageInfo() {
		return arrImageInfo;
	}

	public Integer getCurSubAlbumNo() {
		return curSubAlbumNo;
	}

	public void setCurSubAlbumNo(Integer curSubAlbumNo) {
		this.curSubAlbumNo = curSubAlbumNo;
	}

	public void setArrImageInfo(ArrayList<ImageInfo> arrImageInfo) {
		this.arrImageInfo = arrImageInfo;
	}

	public AlbumInfo() {
		super();
		this.arrSubAlbumInfo=new ArrayList<AlbumInfo>();
	}

	public Integer getCurPicNo() {
		return curPicNo;
	}

	public void setCurPicNo(Integer curPicNo) {
		this.curPicNo = curPicNo;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

//	public int getDownStatus() {
//		return downStatus;
//	}
//
//	public void setDownStatus(int downStatus) {
//		this.downStatus = downStatus;
//	}

	/*	public String getAlbumDir() {
		return albumDir;
	}
	public void setAlbumDir(String albumDir) {
		this.albumDir = albumDir;
	}*/
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getIconName() {
		return iconName;
	}
	public String getmAlbumName() {
		return mAlbumName;
	}
	public void setmAlbumName(String mAlbumName) {
		this.mAlbumName = mAlbumName;
	}
	public String getPicAmount() {
		return picAmount;
	}
	public void setPicAmount(String picAmount) {
		this.picAmount = picAmount;
	}
	public String getCommentAmount() {
		return commentAmount;
	}
	public void setCommentAmount(String commentAmount) {
		this.commentAmount = commentAmount;
	}
	public String getDownLoadAmount() {
		return downLoadAmount;
	}
	public void setDownLoadAmount(String downLoadAmount) {
		this.downLoadAmount = downLoadAmount;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
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

	/**
	 * @return the arrSubAlbumInfo
	 */
	public ArrayList<AlbumInfo> getArrSubAlbumInfo() {
		return arrSubAlbumInfo;
	}

	/**
	 * @param arrSubAlbumInfo the arrSubAlbumInfo to set
	 */
	public void setArrSubAlbumInfo(ArrayList<AlbumInfo> arrSubAlbumInfo) {
		this.arrSubAlbumInfo = arrSubAlbumInfo;
	}
	
	/**
	 * ��ȡר���µ�����ͼƬ��Ϣ
	 */
	public void getArrImageFormServer() {
		BtJsonParser jsonParser = new BtJsonParser();// ����json�����������ڽ��������json��
		String strJson = HttpDownloader.getResFromServer(Constants.REQ_SERV
				+ "?"
				+ Constants.urlParams.getImageArrParam(this.categoryId,
						this.getId()));
		this.setArrImageInfo((jsonParser.imageInfoParser(strJson)));
		MyUtils.outLog("this.getArrImageInfo() is:" + this.getArrImageInfo()
				+ " and this.getArrImageInfo()'s size is:"
				+ this.getArrImageInfo().size());
		MyUtils.outLog("strJson is:" + strJson);

		// Constants.mCatagorysInfo=jsonParser.parseAlbum(strJson);
	}
	
	/**
	 * ����album id��ȡ��ר���µ�����ͼƬ����ÿ��һ��������"|"�ָ�
	 * @param albumId
	 * @return
	 */
/*	String getImageListByAlbumId(String albumId){
		//BtJsonParser jsonParser=new BtJsonParser();//����json�����������ڽ��������json��
		String strRes=HttpDownloader.getResFromServer(Constants.REQ_SERV+"?"+Constants.urlParams.getImageNamesByAlbumId(albumId));
		//this.setArrImageInfo((jsonParser.imageInfoParser(strJson)));
		//Constants.mCatagorysInfo=jsonParser.parseAlbum(strJson);
		return strRes;
	}*/
	
	
	/**
	 * ����̬���ص���ר�����ӵ�ר���б�ͬʱ�޸�ר��������
	 * @param arrAlbum
	 */	
	public void addSubAlbums(ArrayList<AlbumInfo> arrAlbum){
		if(this.arrSubAlbumInfo==null){
			this.arrSubAlbumInfo=new  ArrayList<AlbumInfo>();
		}
		for(int i=0;i<arrAlbum.size();i++){
			this.arrSubAlbumInfo.add(arrAlbum.get(i));
		}
		this.curSubAlbumNo=this.arrSubAlbumInfo.size();		
	}
	
	@Override
	public String toString() {
		return "AlbumInfo [id=" + id + ", mAlbumName=" + mAlbumName
				+ ", categoryName=" + categoryName
				+ ", picAmount=" + picAmount + ", iconName=" + iconName
				+ ", downLoadAmount=" + downLoadAmount + ", commentAmount="
				+ commentAmount + ", clickAmount=" + clickAmount
				+ ", praiseAmount=" + praiseAmount + ", arrSubAlbumInfo="
				+ arrSubAlbumInfo + "]";
	}

	/**
	 * ��ȡ��ǰ���ذ�ťӦ��չʾ������
	 * @param mAlbumInfo
	 * @param mContext
	 * @return
	 */
//	public String getDownButtonTxt(Context mContext){
//		
//		String downButtonTxt="";
//		if (this.downStatus == Constants.DOWNLOAD_NO_DONE//���Ϊ�����������ͣ״̬����ť��ʾ��������
//				|| this.downStatus == Constants.DOWNLOAD_PAUSE) {
//			downButtonTxt = mContext.getResources().getString(
//					R.string.continue_download);
//		} else if (this.downStatus == Constants.DOWNLOAD_DONE) {//����Ѿ�������ɣ���ť��ʾ������ɡ�
//			downButtonTxt = mContext.getResources().getString(
//					R.string.down_over);
//		} else if (this.downStatus == Constants.DOWNLOADING) {//����������أ���ť��ʾ��ͣ
//			downButtonTxt = mContext.getResources().getString(
//					R.string.pause_download);
//		} else {//��������¶���ʾ�����ء�������δ���ع������
//			downButtonTxt = mContext.getResources()
//					.getString(R.string.download);
//		}			
//		return downButtonTxt;
//	}
	
	/**
	 * ��ȡ��ǰ����״̬��ťӦ��չʾ������
	 * @param mAlbumInfo
	 * @param mContext
	 * @return
	 */
//	public String getDownStatusTxt(Context mContext){
//		String downStatusTxt="";
//		// ��������б�Ϊ�գ����ߴ�ר��û�����ع�
//		if (this.downStatus == Constants.DOWNLOAD_NO_DONE) {
//			downStatusTxt = String.format(
//					mContext.getResources().getString(R.string.downloaded), 0);
//		}else if(Constants.hashMapDownloadInfo.get(this.getId())!=null){// ������ص�ͼƬ�����ڵ���ר��ͼƬ��
//			downStatusTxt = String.format(
//					mContext.getResources().getString(R.string.downloaded),
//					Constants.hashMapDownloadInfo.get(this.getId())
//							.getDownloadedAmout());
//		}
//		return downStatusTxt;
//	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
