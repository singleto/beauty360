package com.android.info;

public class PayInfo {
	String userId = "";
	String userName = "";

	// Ӧ�û���Ϸ���Զ����֧������(ÿ��֧���������ݲ�����ͬ)
	String orderId = "";
	// �û���ʶ
	String imei = "";
	// ֧����Ʒ����
	String goodsName ="";// 
	// ֧�����
	int price = 0;
	// ֧��ʱ��
	String time = "";
	// ֧������
	String goodsDesc = "";

	
	
	public PayInfo(String userId,String userName,String orderId, String imei, String goodsName, int price,
			String time, String goodsDesc) {
		super();
		this.orderId = orderId;
		this.imei = imei;
		this.goodsName = goodsName;
		this.price = price;
		this.time = time;
		this.goodsDesc = goodsDesc;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
