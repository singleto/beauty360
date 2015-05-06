package com.android.asyncload.albumicon;

//import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItemInfo implements Parcelable{
	//private static final long serialVersionUID = 1L;
	    private String imageUrl;
	    private String imageDir;
	    private String imagePath;
	    private String strTitle;
	    private String strInfo1;
	    private String strInfo2;
	    private String cataId;
	    private String albumId;
	    private String downButtonTxt;
	    private String downloadProgress;
	    
	public ListItemInfo(String imageDir, String imagePath, String imageUrl,
			String strTitle, String strInfo1, String strInfo2,
			String downButtonTxt, String downloadProgress) {
		this.imageUrl = imageUrl;
		this.imageDir = imageDir;
		this.imagePath = imagePath;
		this.strTitle = strTitle;
		this.strInfo1 = strInfo1;
		this.strInfo2 = strInfo2;
		this.downButtonTxt=downButtonTxt;
		this.downloadProgress=downloadProgress;
	}

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		public String getImageDir() {
			return imageDir;
		}

		public void setImageDir(String imageDir) {
			this.imageDir = imageDir;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getStrTitle() {
			return strTitle;
		}

		public void setStrTitle(String strTitle) {
			this.strTitle = strTitle;
		}

		public String getStrInfo1() {
			return strInfo1;
		}

		public void setStrInfo1(String strInfo1) {
			this.strInfo1 = strInfo1;
		}

		public String getStrInfo2() {
			return strInfo2;
		}

		public void setStrInfo2(String strInfo2) {
			this.strInfo2 = strInfo2;
		}


		public String getDownButtonTxt() {
			return downButtonTxt;
		}

		public void setDownButtonTxt(String downButtonTxt) {
			this.downButtonTxt = downButtonTxt;
		}

		public String getDownloadProgress() {
			return downloadProgress;
		}

		public void setDownloadProgress(String downloadProgress) {
			this.downloadProgress = downloadProgress;
		}

		public String getCataId() {
			return cataId;
		}

		public void setCataId(String cataId) {
			this.cataId = cataId;
		}

		public String getAlbumId() {
			return albumId;
		}

		public void setAlbumId(String albumId) {
			this.albumId = albumId;
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

		@Override
		public String toString() {
			return "ListItemInfo [imageUrl=" + imageUrl + ", imageDir="
					+ imageDir + ", imagePath=" + imagePath + ", strTitle="
					+ strTitle + ", strInfo1=" + strInfo1 + ", strInfo2="
					+ strInfo2 + ", cataId=" + cataId + ", albumId=" + albumId
					+ ", downButtonTxt=" + downButtonTxt
					+ ", downloadProgress=" + downloadProgress + "]";
		}

}
