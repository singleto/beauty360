package com.android.info;

public class PayInfo {
	String userId = "";
	String userName = "";

	// 应用或游戏商自定义的支付订单(每条支付订单数据不可相同)
	String orderId = "";
	// 用户标识
	String imei = "";
	// 支付商品名称
	String goodsName ="";// 
	// 支付金额
	int price = 0;
	// 支付时间
	String time = "";
	// 支付描述
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
