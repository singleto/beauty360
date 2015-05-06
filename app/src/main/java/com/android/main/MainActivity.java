package com.android.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import com.android.beauty360.*;
import com.android.info.Constants;
import com.android.update.MyUpdate;
import com.android.utils.LoadCataAndAlbumToDbTask;
import com.android.utils.MyUtils;


public class MainActivity extends TabActivity {
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	RadioButton radioButton;
	//MyPoints myPoints;
//	RelativeLayout bottom_layout;
//	ImageView img;
	//MyUpdate myUpdate;
	int startLeft;
//	class PointHandler extends Handler {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			if (msg.what == 20) {
//				MyUtils.outLog("MainActivity update point ");
//				Bundle mBundle = (Bundle) msg.obj;
//				int pointAmount = (int) mBundle.getInt("pointAmount");
//				if (pointAmount == 0) {
//					myPoints.addPoints(Constants.appInfo.getAwardPoints());
//				}
//				super.handleMessage(msg);
//			}
//		}
//	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bt_main);
//		bottom_layout = (RelativeLayout) findViewById(R.id.layout_bottom);
		//积分墙广告初始化
		//myPoints = new MyPoints(this,null);
		MyUtils.outLog("===============add point=====================");
		//myPoints.updateAdPoints();
		
		Constants.myUpdate= new MyUpdate(this);//初始化自动升级类
		Constants.myUpdate.update();
		new LoadCataAndAlbumToDbTask(this).execute();//将类别和专辑更新到数据库
		//myPoints.addPoints(30);
//		if (myPoints.getPoints()+Constants.PAY_POINT <= 0 && Constants.appInfo != null) {
//			myPoints.addPoints(Constants.appInfo.getAwardPoints());
//		}
		//AppConnect.getInstance(this);
		Log.v(Constants.LOG_TAG, "初始化积分墙广告");
		
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		MyUtils.outLog("radioGroup is:"+radioGroup);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
		
		tabHost = getTabHost();
		tabHost.addTab(tabHost
				.newTabSpec("home")
				.setIndicator(getResources().getString(R.string.online_album_text),
						getResources().getDrawable(R.drawable.icon_main_home))
				.setContent(new Intent(this, TabHomeActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("local")
				.setIndicator(getResources().getString(R.string.local_album),
						getResources().getDrawable(R.drawable.icon_main_local))
				.setContent(new Intent(this, TabLocalActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("mycenter")
				.setIndicator(getResources().getString(R.string.mycenter_text),
						getResources().getDrawable(R.drawable.icon_main_my_info))
				.setContent(new Intent(this, TabMyCenterActivity.class)));
		
//		tabHost.setCurrentTab(0);
		radioButton = (RadioButton) findViewById(R.id.radio_home);
		radioButton.setChecked(true);
		//this.tabHost.setCurrentTabByTag("home");
		// tabHost.addTab(tabHost.newTabSpec("follow").setIndicator("Follow").setContent(new
		// Intent(this, TabFollowActivity.class)));
		// tabHost.addTab(tabHost.newTabSpec("vote").setIndicator("Vote").setContent(new
		// Intent(this, TabVoteActivity.class)));

		

//		img = new ImageView(this);
//		img.setImageResource(R.drawable.tab_front_bg);
//		bottom_layout.addView(img);
		
		
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyUtils.outLog("MainActivity onDestroy");
		super.onDestroy();
	}


	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		MyUtils.outLog("MainActivity onResume");
		// TODO Auto-generated method stub
		super.onResume();
	}



	/**
	 * 
	 */
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_home:
				MainActivity.this.tabHost.setCurrentTabByTag("home");
				break;
			case R.id.radio_local:
				MainActivity.this.tabHost.setCurrentTabByTag("local");
				break;
			case R.id.radio_mycenter:
				MainActivity.this.tabHost.setCurrentTabByTag("mycenter");
				break;
			default:
				break;
			}
		}
	};
}