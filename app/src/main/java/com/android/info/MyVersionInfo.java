package com.android.info;


public class MyVersionInfo {
	private int newVerCode = -1;
	private String newVerName = "";
	private String appName = "";
	private String apkName = "";
	private String strTips = "";
	private String strUpdateUrl = "";
	private String strUpdateTips = "";

	//广告相关	
	//public Integer adType = 0;
	public Integer adCtr = 0;
	public Integer awardPoints = 0;
	public Integer getAwardPoints() {
		return awardPoints;
	}
	public void setAwardPoints(Integer awardPoints) {
		this.awardPoints = awardPoints;
	}
	//自动保存标识
	public boolean autoSave = true;	
	//for 安卓市场
	public Integer realVerCtr = 0;

	public Integer getAdCtr() {
		return adCtr;
	}
	public void setAdCtr(Integer adCtr) {
		this.adCtr = adCtr;
	}
	public Integer getRealVerCtr() {
		return realVerCtr;
	}
	public void setRealVerCtr(Integer realVerCtr) {
		this.realVerCtr = realVerCtr;
	}
	
	public boolean isAutoSave() {
		return autoSave;
	}
	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public int getNewVerCode() {
		return newVerCode;
	}
	public void setNewVerCode(int newVerCode) {
		this.newVerCode = newVerCode;
	}
	public String getNewVerName() {
		return newVerName;
	}
	public void setNewVerName(String newVerName) {
		this.newVerName = newVerName;
	}
	public String getStrTips() {
		return strTips;
	}
	public void setStrTips(String strTips) {
		this.strTips = strTips;
	}
	public String getStrUpdateUrl() {
		return strUpdateUrl;
	}
	public void setStrUpdateUrl(String strUpdateUrl) {
		this.strUpdateUrl = strUpdateUrl;
	}
	public String getStrUpdateTips() {
		return strUpdateTips;
	}
	public void setStrUpdateTips(String strUpdateTips) {
		this.strUpdateTips = strUpdateTips;
	}
}
