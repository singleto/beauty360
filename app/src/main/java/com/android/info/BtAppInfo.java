package com.android.info;

public class BtAppInfo {
	Integer awardPoints=0;
	Integer curVerCode=0;
	String strVerName="1.0";
	String strNormalTips="";
	String strUpdateTips="";
	Boolean isShowNormalTips=true;
	Boolean isNeedUpdate=false;
	Boolean isShowRealVer=true;
	Boolean isAddAds=true;
	String newVerAddr="";
	Integer payPoints=0;

	public String getNewVerAddr() {
		return newVerAddr;
	}
	public void setNewVerAddr(String newVerAddr) {
		this.newVerAddr = newVerAddr;
	}
	public Boolean getIsAddAds() {
		return isAddAds;
	}
	public void setIsAddAds(Boolean isAddAds) {
		this.isAddAds = isAddAds;
	}
	public Integer getAwardPoints() {
		return awardPoints;
	}
	public void setAwardPoints(Integer awardPoints) {
		this.awardPoints = awardPoints;
	}
	public Integer getCurVerCode() {
		return curVerCode;
	}
	public void setCurVerCode(Integer curVerCode) {
		this.curVerCode = curVerCode;
	}
	public String getStrVerName() {
		return strVerName;
	}
	public void setStrVerName(String strVerName) {
		this.strVerName = strVerName;
	}
	public String getStrNormalTips() {
		return strNormalTips;
	}
	public void setStrNormalTips(String strNormalTips) {
		this.strNormalTips = strNormalTips;
	}
	public String getStrUpdateTips() {
		return strUpdateTips;
	}
	public void setStrUpdateTips(String strUpdateTips) {
		this.strUpdateTips = strUpdateTips;
	}
	public Boolean getIsShowNormalTips() {
		return isShowNormalTips;
	}
	public void setIsShowNormalTips(Boolean isShowNormalTips) {
		this.isShowNormalTips = isShowNormalTips;
	}
	public Boolean getIsNeedUpdate() {
		return isNeedUpdate;
	}
	public void setIsNeedUpdate(Boolean isNeedUpdate) {
		this.isNeedUpdate = isNeedUpdate;
	}
	public Boolean getIsShowRealVer() {
		return isShowRealVer;
	}
	public void setIsShowRealVer(Boolean isShowRealVer) {
		this.isShowRealVer = isShowRealVer;
	}
	
	public Integer getPayPoints() {
		return payPoints;
	}
	public void setPayPoints(Integer payPoints) {
		this.payPoints = payPoints;
	}
	@Override
	public String toString() {
		return "BtAppInfo [awardPoints=" + awardPoints + ", curVerCode="
				+ curVerCode + ", strVerName=" + strVerName
				+ ", strNormalTips=" + strNormalTips + ", strUpdateTips="
				+ strUpdateTips + ", isShowNormalTips=" + isShowNormalTips
				+ ", isNeedUpdate=" + isNeedUpdate + ", isShowRealVer="
				+ isShowRealVer + ", isAddAds=" + isAddAds + "]";
	}
	
	
}
