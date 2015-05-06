package com.android.beauty360.point;

import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.baidu.mobads.appoffers.OffersManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.android.beauty360.*;
import com.android.download.HttpDownloader;
import com.android.info.Constants;
import com.android.utils.MyUtils;
public class MyPoints implements UpdatePointsNotifier{
	Context  mContext=null;
	Handler mHandler=null;
	
	//int dianjinPoints=0;
	int baiduPoints=0;
//	static int wanpuPoints=0;

	
	public MyPoints(Context mContext,Handler mHandler) {
		super();
		this.mContext = mContext;
		this.mHandler=mHandler;
        //点金广告初始化
//		DianJinPlatform.initialize(mContext, 1010,"eb3de875023ff1db2077c13888a637a6", 1001);
//		DianJinPlatform.hideFloatView(mContext);
		//万普广告初始化
		AppConnect.getInstance(Constants.WAPS_ID, "WAPS", mContext);
		//AppConnect.getInstance("09f277ca386ee99cb4c910e09f562112", "WAPS", mContext);
//		DianJinPlatform.initialize(mContext, Constants.DIANJIN_APP_ID,
//				Constants.DIANJIN_APP_KEY,Constants.DIANJIN_CHANNEL_ID);
		this.updateAdPoints();
		//代码设置APPSID与计费名，下面的设置方法与配置文件中的设置是等价的。
		OffersManager.setAppSid(Constants.BAIDU_APP_SID);
		OffersManager.setAppSec(Constants.BAIDU_APP_SEC);
	}

	/**
	 * 弹出获取积分的对话框
	 */
	public void showGetPointDlg(){
        final Builder builder = new AlertDialog.Builder(mContext);
		//builder.setIcon(R.drawable.icon);
		builder.setTitle(this.mContext.getResources().getString(
				R.string.get_points));
		String[] pointChannel = new String[] {
				mContext.getResources().getString(R.string.get_by_pay),
				mContext.getResources().getString(R.string.best_app_rec_01),
				mContext.getResources().getString(R.string.best_app_rec_02),
				mContext.getResources()
						.getString(R.string.get_point_by_tuangou)
				 };
		builder.setItems(pointChannel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch(which){
				case 0:
					Intent mIntent = new Intent().setClass(mContext, PayMainActivity.class);
					mContext.startActivity(mIntent);
					break;
				case 1:
					AppConnect.getInstance(mContext).showOffers(mContext);
					break;
				case 2:
					//DianJinPlatform.showOfferWall(mContext);
					OffersManager.showOffers(mContext);
					//AppConnect.getInstance(MainActivity.this).showFeedback();
					//AppConnect.getInstance(MainActivity.this).showTuanOffers(MainActivity.this);
					//消费虚拟货币.
					//AppConnect.getInstance(this).spendPoints(10, this);		
					//奖励虚拟货币
					//AppConnect.getInstance(this).awardPoints(10, this);
					break;
				case 3:
					AppConnect.getInstance(mContext).showTuanOffers(mContext);
					break;

/*				case 2:
					DianJinPlatform.showOfferWall(mContext);
					//OffersManager.subPoints(MainActivity.this, 20);
					//增加10积分
					//OffersManager.addPoints(mContext, 10);
					//int points=OffersManager.getPoints(mContext);
					//System.out.println("baidu points is:"+points);
					//打开积分墙
					
					break;*/
				}
			}
		});
		builder.create().show();
	}

	@Override
	public void getUpdatePoints(String currencyName, int pointTotal) {
		// TODO Auto-generated method stub
		//MyUtils.outLog(logs);
		
		Constants.wanpuPoints=pointTotal;	
		int pointAmount=this.baiduPoints/*+this.dianjinPoints*/+Constants.wanpuPoints+Constants.PAY_POINT;
		if (this.mHandler != null) {
			Bundle bundle = new Bundle();
			bundle.putInt("pointAmount", pointAmount);// 设置图片
			Message message = mHandler.obtainMessage(20, bundle);
			mHandler.sendMessage(message);
		}
		MyUtils.outLog("getUpdatePoints wanpuPoints is:"+pointTotal);
		MyUtils.outLog("getUpdatePoints baiduPoints is:"+baiduPoints);
		MyUtils.outLog("getUpdatePoints Constants.PAY_POINT is:"+Constants.PAY_POINT);

	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 更新积分
	 * @return
	 */
	public int updateAdPoints(){
		this.baiduPoints=OffersManager.getPoints(this.mContext);
		//this.dianjinPoints=(int)DianJinPlatform.getBalance(this.mContext);
		AppConnect.getInstance(mContext).getPoints(this);
		return (this.baiduPoints/*+this.dianjinPoints*/+Constants.wanpuPoints+Constants.PAY_POINT);		
	}
	
	/**
	 * 更新积分
	 * @return
	 */
	public int getPoints(){
		//this.baiduPoints=OffersManager.getPoints(this.mContext);
		//this.dianjinPoints=(int)DianJinPlatform.getBalance(this.mContext);
		//AppConnect.getInstance(mContext).getPoints(this);
		this.updateAdPoints();
		return (this.baiduPoints/*+this.dianjinPoints*/+Constants.wanpuPoints+Constants.PAY_POINT);		
	}
	
	/**
	 * 扣减积分
	 * @param points
	 * @return
	 */
	public boolean subPoints(int points) {
		MyUtils.outLog("subPoints points is:"+points);
		MyUtils.outLog("subPoints wanpuPoints is:"+Constants.wanpuPoints);
		MyUtils.outLog("subPoints baiduPoints is:"+this.baiduPoints);
		MyUtils.outLog("subPoints Constants.PAY_POINT is:"+Constants.PAY_POINT);
		
		boolean bRes=false;
		if(points>(this.baiduPoints+Constants.wanpuPoints/*+this.dianjinPoints*/+Constants.PAY_POINT)){
			MyUtils.outLog("subPoints 01");

			return bRes;
		}else if(points <= Constants.wanpuPoints) {
			MyUtils.outLog("subPoints 02");
			AppConnect.getInstance(this.mContext).spendPoints(points, this);
			Constants.wanpuPoints=Constants.wanpuPoints-points;
			bRes=true;
			
		}else if(points <= (Constants.wanpuPoints+this.baiduPoints)){
			MyUtils.outLog("subPoints 03");

			int temp = points-Constants.wanpuPoints;
			AppConnect.getInstance(this.mContext).spendPoints(Constants.wanpuPoints, this);//万普积分清零
			Constants.wanpuPoints=0;
			OffersManager.subPoints(mContext, temp);//百度积分减去剩余的
			this.baiduPoints=this.baiduPoints-temp;
			bRes=true;
			
		}/*else if(points <= (this.wanpuPoints+this.baiduPoints+this.dianjinPoints)){
			int temp = points-this.wanpuPoints;//总分减去万普
			AppConnect.getInstance(this.mContext).spendPoints(this.wanpuPoints, this);//万普积分清零
			this.wanpuPoints=0;
			temp = temp-this.baiduPoints;//除去万普之后的积分减去百度积分
			OffersManager.subPoints(mContext, this.baiduPoints);//百度积分清零
			this.baiduPoints=0;			
			DianJinPlatform.consume(mContext, temp, new DianJinConsumeListener());	
			dianjinPoints=dianjinPoints-temp;//修改点金积分	
			bRes=true;
			
		}else if(points <= (this.wanpuPoints+this.baiduPoints+this.dianjinPoints)){//全部清零
			AppConnect.getInstance(this.mContext).spendPoints(this.wanpuPoints, this);//万普积分清零
			this.wanpuPoints=0;
			OffersManager.subPoints(mContext, this.baiduPoints);//百度积分清零
			this.baiduPoints=0;			
			DianJinPlatform.consume(mContext, this.dianjinPoints, new DianJinConsumeListener() );	
			dianjinPoints=0;//修改点金积分
			bRes=true;
		}*/else if(points <= (Constants.wanpuPoints+this.baiduPoints/*+this.dianjinPoints*/+Constants.PAY_POINT)) {
			MyUtils.outLog("subPoints 04");

			int temp = points-Constants.wanpuPoints-this.baiduPoints/*-this.dianjinPoints*/;//总分减去万普
			AppConnect.getInstance(this.mContext).spendPoints(Constants.wanpuPoints, this);//万普积分清零
			Constants.wanpuPoints=0;
			OffersManager.subPoints(mContext, this.baiduPoints);//百度积分清零
			this.baiduPoints=0;			
/*			DianJinPlatform.consume(mContext, this.dianjinPoints, new DianJinConsumeListener() );	
			dianjinPoints=0;//修改点金积分	
*/			Constants.PAY_POINT=Constants.PAY_POINT-temp;//修改支付账户积分

			new Thread()
			{
			    @Override
			    public void run()
			    {
					String strRes = HttpDownloader.getResFromServer(Constants.REQ_SERV
							+ "?"
							+ Constants.urlParams
									.getUpdatePayPointParam(Constants.PAY_POINT));
			    }
			}.start();
//			String strRes = HttpDownloader.getResFromServer(Constants.REQ_SERV
//					+ "?"
//					+ Constants.urlParams
//							.getUpdatePayPointParam(Constants.PAY_POINT));
			bRes = true;
		}
		this.updateAdPoints();
		return bRes;
	}
	
	/**
	 * 程序退出时销毁广告
	 */
	public void clear(){
		AppConnect.getInstance(this.mContext).close();
		//DianJinPlatform.destory(this.mContext);
	}
	
	/**
	 * 增加积分
	 * @param points
	 */
	public void addPoints(int points){
		MyUtils.outLog("addPoints points is:"+points);
		MyUtils.outLog("addPoints wanpuPoints is:"+Constants.wanpuPoints);
		MyUtils.outLog("addPoints baiduPoints is:"+this.baiduPoints);
		MyUtils.outLog("addPoints Constants.PAY_POINT is:"+Constants.PAY_POINT);
		Constants.PAY_POINT=Constants.PAY_POINT+points;
//		String strRes= HttpDownloader.getResFromServer(Constants.REQ_SERV + "?"
//				+ Constants.urlParams.getUpdatePayPointParam(Constants.PAY_POINT));
		this.updateAdPoints();
//		MyUtils.outLog("addPoints server respones:"+strRes);
		//更新到服务器
	}
	/**
	 * 弹出反馈对话框
	 */
	public void showFeedback(){
		AppConnect.getInstance(this.mContext).showFeedback();
	}
	
//	class DianJinConsumeListener implements ConsumeListener{
//
//		@Override
//		public void onError(int errorCode, String errorMessage) {
//			// TODO Auto-generated method stub
//			switch (errorCode) {
//			case DianJinPlatform.DIANJIN_ERROR_ILLEGAL_CONSUNE:// 非法的消费金额
//				Toast.makeText(mContext, errorMessage,
//						Toast.LENGTH_SHORT).show();
//				break;
//			case DianJinPlatform.DIANJIN_ERROR_BALANCE_NO_ENOUGH:// 余额不足
//				Toast.makeText(mContext, errorMessage,
//						Toast.LENGTH_SHORT).show();
//			default:
//				break;
//			}
//
//		}
//
//		@Override
//		public void onSuccess() {
//			// TODO Auto-generated method stub
//			MyUtils.outLog("dianjin point consum success");
//		}
//	}
}
