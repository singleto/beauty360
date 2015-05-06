package com.android.main;



import android.app.Activity;
import android.content.res.Configuration;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.beauty360.*;
import com.android.info.Constants;
import com.android.beauty360.point.MyPoints;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;


/*  Android实现圆角ListView示例*/


public class TabMyCenterActivity extends Activity implements  OnClickListener {
	
//	 private CornerListView pointListView = null;
//	 private CornerListView settingListView = null;
	MyPoints myPoints;//定义积分墙广告
	// private View mPointLoading;
	 private TextView mPointValue=null;

//	class PointHandler extends Handler {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			if (msg.what == 20) {
//				MyUtils.outLog("update point value textview");
//				Bundle mBundle = (Bundle) msg.obj;
//				int pointAmount = (int) mBundle.getInt("pointAmount");
//				if (mPointValue != null) {
//					mPointValue.setText(pointAmount + "");
//				}
//				super.handleMessage(msg);
//			}
//		}
//	}
	 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mycenter);
		this.myPoints=new MyPoints(this,null);
		View tmpView;
//		View tmpView = this.findViewById(R.id.index);
//		tmpView.setOnClickListener(new MyCenterListener());
//		
//		tmpView = this.findViewById(R.id.image_quality);
//		tmpView.setOnClickListener(new MyCenterListener());
//		
//		tmpView = this.findViewById(R.id.image_move_v);
//		tmpView.setOnClickListener(new MyCenterListener());
		
		tmpView = this.findViewById(R.id.clear_cache);
		tmpView.setOnClickListener(this);
		
//		tmpView = this.findViewById(R.id.update_check);
//		tmpView.setOnClickListener(new MyCenterListener());
		
		tmpView = this.findViewById(R.id.feedback);
		tmpView.setOnClickListener(this);
		
//		tmpView = this.findViewById(R.id.help_content);
//		tmpView.setOnClickListener(this);
//		tmpView = this.findViewById(R.id.about);
//		tmpView.setOnClickListener(new MyCenterListener());
		
		tmpView = this.findViewById(R.id.point);
		//mPointLoading = this.findViewById(R.id.point_loading);
		mPointValue = (TextView)this.findViewById(R.id.point_value);
		tmpView.setOnClickListener(this);
		//AppConnect.getInstance(this).getPoints(this);
		
		tmpView = this.findViewById(R.id.app_offer_wall);
		tmpView.setOnClickListener(this);
		
		
		//Constants.myUpdate.update();//判断是否需要升级
		
//		tmpView = this.findViewById(R.id.pay_point);
//		tmpView.setOnClickListener(this);
		
//		pointListView = (CornerListView)findViewById(R.id.list_point);
//		settingListView = (CornerListView)findViewById(R.id.list_setting);
//
//		getDataSource1();
//		getDataSource2();
//		SimpleAdapter pointAdapter = new SimpleAdapter(getApplicationContext(), pioint_map_list,R.layout.mycenter_list_item_1, 
//				new String[] { "item" },new int[] { R.id.item_title });
//		pointListView.setAdapter(pointAdapter);
//		pointListView.setOnItemClickListener(new OnItemClickListener() {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			if (arg2 == 0) {
//				System.out.println("0");
//			}else if(arg2 == 1){
//				System.out.println("1");
//			}else if(arg2 == 2){
//				System.out.println("2");
//			}
//			
//		}
//		});
//		
//        
//        SimpleAdapter settingAdapter = new SimpleAdapter(getApplicationContext(), setting_map_list, R.layout.mycenter_list_item_2,
//        		new String[]{"text","img"}, new int[]{R.id.setting_list_item_text,R.id.setting_list_item_arrow});
//        settingListView.setAdapter(settingAdapter);
//        settingListView.setOnItemClickListener(new OnItemClickListener() {
//    		@Override
//    		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//    				long arg3) {
//    			if (arg2 == 0) {
//    				System.out.println("3");
//    			}else if(arg2 == 1){
//    				System.out.println("4");
//    			}
//    			
//    			
//    		}
//    		});
	//	cornerListView2.setOnItemClickListener(new OnItemListSelectedListener());

	}

//	public ArrayList<HashMap<String, String>> getDataSource1() {
//
//		pioint_map_list = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String> hmPointAmount = new HashMap<String, String>();
//		HashMap<String, String> hmGetPointByDownApp = new HashMap<String, String>();
//		HashMap<String, String> hmGetPointByPay = new HashMap<String, String>();
//
//		hmPointAmount.put("item", getResources().getString(R.string.point_amount));
//		hmGetPointByDownApp.put("item", getResources().getString(R.string.get_point_by_down_app));
//		hmGetPointByPay.put("item", getResources().getString(R.string.get_point_by_pay));
//
//		pioint_map_list.add(hmPointAmount);
//		pioint_map_list.add(hmGetPointByDownApp);
//		pioint_map_list.add(hmGetPointByPay);
//
//		return pioint_map_list;
//	}
//	  private List<Map<String, Object>>  getDataSource2() { 
//		  setting_map_list = new ArrayList<Map<String, Object>>(); 
//	  
//	        Map<String, Object> hmSetingMap = new HashMap<String, Object>(); 
//	        hmSetingMap.put("text", "代驾宝"); 
//	     //   map.put("info", "google 1"); 
//	        hmSetingMap.put("img", R.drawable.icon); 
//	        setting_map_list.add(hmSetingMap); 
//	  
//	        hmSetingMap = new HashMap<String, Object>(); 
//	        hmSetingMap.put("text", "健康宝"); 
//	      //  map.put("info", "google 2"); 
//	        hmSetingMap.put("img", R.drawable.icon); 
//	        setting_map_list.add(hmSetingMap); 
//
//	        return setting_map_list; 
//	    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MyUtils.exitDig(TabMyCenterActivity.this);
			//this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1,
				this.getResources().getString(R.string.get_points)).setIcon(
				android.R.drawable.ic_menu_share);
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
		menu.add(Menu.NONE, Menu.FIRST + 2, 2,
				this.getResources().getString(R.string.del_cache)).setIcon(
				android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3,
				this.getResources().getString(R.string.suggestion)).setIcon(
				android.R.drawable.ic_menu_edit);
		/*
		 * menu.add(Menu.NONE, Menu.FIRST + 5, 4, "关于").setIcon(
		 * android.R.drawable.ic_menu_info_details);
		 */

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			// 显示推荐应用列表（Offer）.
			this.myPoints.showGetPointDlg();
			//AppConnect.getInstance(this).showOffers(this);
			break;
		case Menu.FIRST + 2:
			FileUtils.removeCache(Constants.LOCAL_PATH, (float) 1);
			Toast.makeText(TabMyCenterActivity.this,
					this.getResources().getString(R.string.del_cache_res),
					Toast.LENGTH_SHORT).show();
			break;
		case Menu.FIRST + 3:
			myPoints.showFeedback();
			//AppConnect.getInstance(this).showFeedback();
			break;
		}
		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.point:
			mPointValue.setText(myPoints.getPoints()+"");
			//AppConnect.getInstance(this).getPoints(this);
			break;
		case R.id.app_offer_wall:
			this.myPoints.showGetPointDlg();
			//AppConnect.getInstance(this).showOffers(this);
			break;
//		case R.id.pay_point:
//			Intent mIntent = new Intent().setClass(this, PayMainActivity.class);
//			this.startActivity(mIntent);
			//AppConnect.getInstance(this).showOffers(this);
			//break;
//		case R.id.index:
//			//选择首页
//			new AlertDialog.Builder(this)
//				.setTitle(R.string.please_select)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setSingleChoiceItems(R.array.index_values, getNameIndex(mIndexNames, mCurrentIndexName), new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						Config.Instance().setString(SETTING_INDEX, mIndexNames[which]);
//						mCurrentIndexName = mIndexNames[which];
//						mIndexTextView.setText(mIndexValues[getNameIndex(mIndexNames, mCurrentIndexName)]);
//						dialog.dismiss();
//					}
//				})
//				.setNegativeButton(android.R.string.cancel, null)
//				.show();
//			break;
//		case R.id.image_quality:
//			//选择首页
//			new AlertDialog.Builder(getActivity())
//				.setTitle(R.string.please_select)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setSingleChoiceItems(R.array.image_quality_values, getNameIndex(mImageQualityNames, mCurrentImageQualityName), new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						Config.Instance().setInt(SETTING_IMAGE_QUALITY, Integer.valueOf(mImageQualityNames[which]));
//						mCurrentImageQualityName = mImageQualityNames[which];
//						mImageQualityTextView.setText(mImageQualityValues[getNameIndex(mImageQualityNames, mCurrentImageQualityName)]);
//						dialog.dismiss();
//					}
//				})
//				.setNegativeButton(android.R.string.cancel, null)
//				.show();
//			break;
//		case R.id.image_move_v:
//			//选择首页
//			new AlertDialog.Builder(getActivity())
//				.setTitle(R.string.please_select)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setSingleChoiceItems(R.array.image_move_v_values, getNameIndex(mImageMoveVNames, mCurrentImageMoveVName), new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						Config.Instance().setInt(SETTING_IMAGE_MOVE_V, Integer.valueOf(mImageMoveVNames[which]));
//						mCurrentImageMoveVName = mImageMoveVNames[which];
//						mImageMoveVTextView.setText(mImageMoveVValues[getNameIndex(mImageMoveVNames, mCurrentImageMoveVName)]);
//						dialog.dismiss();
//					}
//				})
//				.setNegativeButton(android.R.string.cancel, null)
//				.show();
//			break;
		case R.id.clear_cache:
			//清空缓存
			FileUtils.removeCache(Constants.LOCAL_PATH, (float) 1);
			Toast.makeText(TabMyCenterActivity.this,
					this.getResources().getString(R.string.del_cache_res),
					Toast.LENGTH_SHORT).show();
			//new CacheClearTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		    //new CacheClearTask(this).execute();
			break;
//		case R.id.update_check:
//			//检查更新
//			//new ManualUpdateCheckTask(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, HttpExtParameter.getUrl(Constants.URL_UPDATE_CHECK));
//			new ManualUpdateCheckTask(getActivity()).execute(HttpExtParameter.getUrl(Constants.URL_UPDATE_CHECK));
//			break;
		case R.id.feedback:
			this.myPoints.showFeedback();
			//AppConnect.getInstance(this).showFeedback();
			break;
//		case R.id.help_content:
//			MyUtils.outLog("help content");
//			String helpContent = HttpDownloader.getResFromServer(Constants.REQ_SERV + "?"
//					+ Constants.urlParams.getHelpContentParam());
//			MyUtils.showDlg(this,this.getResources().getString(R.string.help), helpContent);
//			//AppConnect.getInstance(this).showFeedback();
//			break;
//		case R.id.about:
//			//选择首页
//			new AlertDialog.Builder(getActivity())
//				.setTitle(R.string.about)
//				.setMessage(R.string.about_content)
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setNegativeButton(android.R.string.ok, null)
//				.show();
//			break;
			default:
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mPointValue.setText(myPoints.getPoints()+"");
		MyUtils.outLog("points amount is:"+myPoints.getPoints());
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyUtils.outLog("TabMyCenterActivity onDestroy");

		super.onDestroy();
	}
	
} 


//class OnItemListSelectedListener implements OnItemClickListener {
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//			long arg3) {
//		if (arg2 == 0) {
//			System.out.println("0");
//		}else{
//			System.out.println("1");
//		}
//	}
//}
	
