package com.android.beauty360.point;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.beauty360.*;
import com.android.download.HttpDownloader;
import com.android.info.Constants;
import com.android.info.PayInfo;
import com.android.utils.MyUtils;
import com.wanpu.pay.PayConnect;
import com.wanpu.pay.PayResultListener; 
public class PayMainActivity extends Activity {
	
	// 应用或游戏商自定义的支付订单(每条支付订单数据不可相同)
	String orderId = "";
	// 用户标识
	String deviceId = "";
	// 支付商品名称
	String goodsName ="";// 
	// 支付金额
	int price = 0;
	// 支付时间
	String time = "";
	// 支付描述
	String goodsDesc = "";
	// 应用或游戏商服务器端回调接口（无服务器可不填写）
	String notifyUrl = "";
	TextView detailsView;
	
	MyPoints myPoints;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pay_main);
		
		// 初始化统计器(必须调用)
        PayConnect.getInstance(Constants.WAPS_ID, "WAPS", this);        
      
        myPoints=new MyPoints(this,null);
        
		TextView goodsNameView = (TextView) this.findViewById(R.id.goodsName);
		goodsNameView.setText(this.getResources().getString(R.string.point));
		goodsName=getResources().getString(R.string.point);
		
		final EditText amountView = (EditText) this.findViewById(R.id.amount);

		detailsView = (TextView) this.findViewById(R.id.details);
		
		String amountStr = amountView.getText().toString();
		if (!"".equals(amountStr)) {
			price = Integer.valueOf(amountStr);
		}
		
		goodsDesc = price*Constants.POINT_PRICE+getResources().getString(R.string.point);
		detailsView.setText(goodsDesc);
		
		
		amountView.addTextChangedListener(new TextWatcher(){ 

            @Override 
            public void beforeTextChanged(CharSequence s, int start, int count, 
                    int after) { 
               	MyUtils.outLog("beforeTextChanged:" + s + "-" + start + "-" + count + "-" + after); 
                 
            } 
 
            @Override 
            public void onTextChanged(CharSequence s, int start, int before, 
                    int count) { 
            	MyUtils.outLog( "onTextChanged:" + s + "-" + "-" + start + "-" + before + "-" + count); 
        		String amountStr = s.toString(); 
        		if(amountStr.equals("")){
        			amountStr="0";
        		}
        		price = Integer.valueOf(amountStr);        		
        		goodsDesc = price*Constants.POINT_PRICE+getResources().getString(R.string.point);
        		detailsView.setText(goodsDesc);
        		//detailsView.setText(s); 
            }

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				MyUtils.outLog("afterTextChanged" ); 
			} 
             
        }); 	
		
		
		

		
		deviceId = PayConnect.getInstance(PayMainActivity.this).getDeviceId(PayMainActivity.this);
		
		Button submitButton = (Button) this.findViewById(R.id.submitBtn);
		
//		// 支付SDK版本
//		TextView sdkVersionView = (TextView) this.findViewById(R.id.sdkVersion);
//		sdkVersionView.setText(this.getResources().getString(R.string.pay_sdk_ver)+ PayConnect.LIBRARY_VERSION_NUMBER);
		
		submitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				try {
//					// 游戏商自定义支付订单号，保证该订单号的唯，建议在执行支付操作时才进行该订单号的生�?
			        orderId = System.currentTimeMillis() + "";
			        
					String strPoint = amountView.getText().toString();
					
					int checkRes=PayMainActivity.this.checkPointInputed(strPoint);//0:有效；-1：小于等于0；1：为空；2：小于1元；
					if(checkRes==-1){//如果
						MyUtils.showDlg(PayMainActivity.this, PayMainActivity.this.getResources().getString(R.string.kindly_reminder),PayMainActivity.this.getResources().getString( R.string.pay_less_than_0_tip));
						return;
					}else if(checkRes==1){
						MyUtils.showDlg(PayMainActivity.this, PayMainActivity.this.getResources().getString(R.string.kindly_reminder),PayMainActivity.this.getResources().getString( R.string.pay_less_than_1_tip));
						return;			
					}else if(checkRes==2){
						MyUtils.showDlg(PayMainActivity.this, PayMainActivity.this.getResources().getString(R.string.kindly_reminder),PayMainActivity.this.getResources().getString( R.string.input_blank_tip));
						return;
					}
					

				   price = Integer.valueOf(strPoint);
				   PayInfo payInfo=new PayInfo("","",orderId,deviceId,goodsName,price,time,goodsDesc);
			       notifyUrl=Constants.REQ_SERV + "?"+ Constants.urlParams.getPayNotifyParam(payInfo);//设置支付通知的参数

				   PayConnect.getInstance(PayMainActivity.this).pay(PayMainActivity.this, 
																	orderId, 
																	deviceId, 
																	price, 
																	goodsName, 
																	goodsDesc, 
																	notifyUrl,
																	new MyPayResultListener());
					
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
	
	/**
	 * 根据当前时间获取订单Id
	 * @return
	 */
	public static String getOrderId() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR)				
				+ MyUtils.formatTime(c.get(Calendar.MONTH) + 1)
				+ MyUtils.formatTime(c.get(Calendar.DAY_OF_MONTH)) 
				+ MyUtils.formatTime(c.get(Calendar.HOUR_OF_DAY))
				+ MyUtils.formatTime(c.get(Calendar.MINUTE))
				+ MyUtils.formatTime(c.get(Calendar.SECOND))
				+ MyUtils.formatTime(c.get(Calendar.MILLISECOND));
		return time;
	}
	
	/**
	 * @checkPointInputed
	 * @param strPoint
	 * @return0:有效；-1：小于等于0；1：为空；2：小于1元；
	 */
	public int checkPointInputed( String strPoint){
		int iRes=0;//0:有效；-1：小于等于0；1：为空；2：小于1元；
		int price = 0;
		if (!"".equals(strPoint)) {//如果不为空
			price = Integer.valueOf(strPoint);
		}else{//如果为空 返回1
			iRes=1;
			return iRes;
		}
		
		if(price<=0){//如果输入值小于0
			iRes=-1;
		}else if(price<1){//如果小于1元
			iRes=2;
		}		
		return iRes;	

		
		
	}

	
	@Override
	protected void onDestroy() {
		//以前版本的finalize()方法作废
		PayConnect.getInstance(this).close();
		super.onDestroy();
	}
	
	/**
	 * 自定义Listener实现PaySuccessListener，用于监听支付成�?
	 * @author Administrator
	 *
	 */
	private class MyPayResultListener implements PayResultListener{

		@Override
		public void onPayFinish(Context payViewContext, String orderId, int resultCode, String resultString, int payType,
				float amount, String goodsName) {
			// 可根据resultCode自行判断
			if(resultCode == 0){
				//赠送积分，并通知服务器
				myPoints.addPoints((int)amount*Constants.POINT_PRICE);
				new Thread()
				{
				    @Override
				    public void run()
				    {
						String strRes= HttpDownloader.getResFromServer(Constants.REQ_SERV + "?"
								+ Constants.urlParams.getUpdatePayPointParam(Constants.PAY_POINT));
				    }
				}.start();
//				String strRes= HttpDownloader.getResFromServer(Constants.REQ_SERV + "?"
//						+ Constants.urlParams.getUpdatePayPointParam(Constants.PAY_POINT));
				//MyUtils.outLog("addPoints server respones:"+strRes);
//				Constants.PAY_POINT=(Constants.PAY_POINT+((int)amount*Constants.POINT_PRICE));
//				String strRes= HttpDownloader.getResFromServer(Constants.REQ_SERV + "?"
//						+ Constants.urlParams.getUpdatePayPointParam((int)amount*Constants.POINT_PRICE));
				
				Toast.makeText(PayMainActivity.this, resultString + ":" + amount + ":", Toast.LENGTH_LONG).show();
				// 支付成功时关闭当前支付界面
				PayConnect.getInstance(PayMainActivity.this).closePayView(payViewContext);	
				
				// TODO 在客户端处理支付成功的操做

				
				// 未指定notifyUrl的情况下，交易成功后，必须发送回
				PayConnect.getInstance(PayMainActivity.this).confirm(orderId, payType);
			}else{
				Toast.makeText(PayMainActivity.this, resultString, Toast.LENGTH_LONG).show();
			}
		}
	}
	
}
