package com.android.info;

public class ImageInfo {
	String imgId;
	String imgName;
	String albumId;
	String cataId;
	String downAmount;
	String praiseAmount;
	
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getCataId() {
		return cataId;
	}
	public void setCataId(String cataId) {
		this.cataId = cataId;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	public String getDownAmount() {
		return downAmount;
	}
	public void setDownAmount(String downAmount) {
		this.downAmount = downAmount;
	}
	public String getPraiseAmount() {
		return praiseAmount;
	}
	public void setPraiseAmount(String praiseAmount) {
		this.praiseAmount = praiseAmount;
	}

	@Override
	public String toString() {
		return "ImageInfo [imgId=" + imgId + ", imgName=" + imgName
				+ ", albumId=" + albumId + ", cataId=" + cataId
				+ ", downAmount=" + downAmount + ", praiseAmount="
				+ praiseAmount + "]";
	}	
}
