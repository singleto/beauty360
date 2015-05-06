package com.android.info;

import java.util.ArrayList;

import com.android.download.HttpDownloader;
import com.android.utils.BtJsonParser;
import com.android.utils.MyUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class CatagoryInfo implements Parcelable{
	
	//private static final long serialVersionUID = 1L;

	private String id;
	private String catagoryName;
	private Bitmap mPic;
	private Integer albumAmount=0;
	private Integer curAlbumAmount=0;
	private boolean isMoreAlbum=false;
	private ArrayList<AlbumInfo> mArrayListAlbumsInfo=null;//���ר������

	public boolean isMoreAlbum() {
		return isMoreAlbum;
	}
	public void setMoreAlbum(boolean isMoreAlbum) {
		this.isMoreAlbum = isMoreAlbum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getAlbumAmount() {
		return albumAmount;
	}
	public void setAlbumAmount(Integer albumAmount) {
		this.albumAmount = albumAmount;
	}
	public Integer getCurAlbumAmount() {
		return curAlbumAmount;
	}
	public void setCurAlbumAmount(Integer curAlbumAmount) {
		this.curAlbumAmount = curAlbumAmount;
	}
	public String getCatagoryName() {
		return catagoryName;
	}
	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}
	public Bitmap getmPic() {
		return mPic;
	}
	public void setmPic(Bitmap mPic) {
		this.mPic = mPic;
	}
	public ArrayList<AlbumInfo> getmArrayListAlbumsInfo() {
		return mArrayListAlbumsInfo;
	}
	public void setmArrayListAlbumsInfo(ArrayList<AlbumInfo> mArrayListAlbumsInfo) {
		this.mArrayListAlbumsInfo = mArrayListAlbumsInfo;
	}
	/**
	 * �ӷ������˻�ȡר����Ϣ
	 */
	public ArrayList<AlbumInfo> getAllAlbums() {
		ArrayList<AlbumInfo> mArrayListAlbumsInfo = null;
		BtJsonParser jsonParser = new BtJsonParser();// ����json�����������ڽ��������json��
		String strJson = HttpDownloader.getResFromServer(Constants.REQ_SERV
				+ "?"
				+ Constants.urlParams.getAllAlbumsParam(this.getId()));
		mArrayListAlbumsInfo = jsonParser.albumParser(strJson);
		MyUtils.outLog("enter getAllAlbums mArrayListAlbumsInfo is:"+mArrayListAlbumsInfo);
		return mArrayListAlbumsInfo;
		// Constants.mCatagorysInfo=jsonParser.parseAlbum(strJson);
	}
	/**
	 * ����̬���ص�ר�����ӵ�ר���б�ͬʱ�޸�ר��������
	 * @param arrAlbum
	 */	
	public void addAlbums(ArrayList<AlbumInfo> arrAlbum){
		if(this.mArrayListAlbumsInfo==null){
			this.mArrayListAlbumsInfo=new  ArrayList<AlbumInfo>();
		}
		for(int i=0;i<arrAlbum.size();i++){
			this.mArrayListAlbumsInfo.add(arrAlbum.get(i));
		}
		this.curAlbumAmount=this.mArrayListAlbumsInfo.size();		
	}
	/**
	 * ����albumId��ȡalbum����
	 * @param albumId
	 */
	public AlbumInfo getAlbumInfo (String albumId){
		
		AlbumInfo resAlbumInfo=null;
		if(this.getmArrayListAlbumsInfo()!=null){
			resAlbumInfo = getFromArrAlbum(this.getmArrayListAlbumsInfo(),albumId);
		}
		return resAlbumInfo;		
	}
	
	/**
	 * ������getAlbumInfo�������ݹ������ǰ��album����
	 * @param arrAlbum
	 * @param albumId
	 * @return
	 */
	public AlbumInfo getFromArrAlbum(ArrayList<AlbumInfo> arrAlbum,
			String albumId) {
		AlbumInfo resAlbumInfo = null;
		for (int i = 0; i < arrAlbum.size(); i++) {			
			resAlbumInfo = (AlbumInfo) arrAlbum.get(i);
			//����ҵ�ֱ�ӷ��أ����жϼӵ������Ҫ���ǵ�������ר���ĸ�ר��������ֻ��Ҫ������if��伴�ɡ�
			if (resAlbumInfo!=null && resAlbumInfo.getId().equals(albumId)) {
				return resAlbumInfo;
			}
			//�������ר�����ݹ��ѯ
			if (resAlbumInfo.getArrSubAlbumInfo() != null&& resAlbumInfo.getArrSubAlbumInfo().size()>0) {
				resAlbumInfo=getFromArrAlbum(resAlbumInfo.getArrSubAlbumInfo(), albumId);
				if (resAlbumInfo!=null && resAlbumInfo.getId().equals(albumId)) {
					return resAlbumInfo;
				}
			}
		}
		return resAlbumInfo;
	}	
		

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
