package com.android.main;

//import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.android.asyncload.albumicon.ListItemInfo;
import com.android.asyncload.albumicon.ListItemInfoAdapterLocal;
import com.android.beauty360.*;
import com.android.beauty360.point.MyPoints;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.picview.LocalPicViewer;
import com.android.picview.PicViewer;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;

public class TabLocalActivity extends Activity implements AdsMogoListener {
	private ListView albumListView;

	//private AlbumInfo mAlbumInfo;
	ListItemInfoAdapterLocal adapter;
	AdsMogoLayout adsMogoLayout;//横幅广告定义
	MyPoints myPoints;//积分墙广告
	// 此页面的所有专辑信息
	//ArrayList<AlbumInfo> mArrayListAlbumInfo;
	// 此页面的每个专辑的图片和文字展示信息
	List<ListItemInfo> albumImaTxtArray;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_local);
		
		// 万普广告初始化
		this.myPoints=new MyPoints(this,null);
//		AppConnect.getInstance(this);
//		AppConnect.getInstance(this).getPoints(this);// 初始化积分

		// 设置广告
		// if(Constants.appInfo.getIsAddAds()){
		// 设置监听回调 其中包括 请求 展示 请求失败等事件的回调
		adsMogoLayout = ((AdsMogoLayout) this.findViewById(R.id.adsMogoView));
		adsMogoLayout.setAdsMogoListener(this);
		// addMogoAdView();
		// }
		albumListView = (ListView) findViewById(R.id.list_view_local);	
		
		MyUtils.outLog("TabLocalActivity  onCreate albumListView is:"+albumListView);
		
	}
	
	@Override
	protected void onDestroy() {
		// // TODO Auto-generated method stub
		// if(2==Constants.verInfo.getAdType()){
		// }
		//AppConnect.getInstance(TabLocalActivity.this).finalize();
		MyUtils.outLog("TabLocalActivity onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		/*
		 * List<Runnable> temp=Constants.executorService.shutdownNow();
		 * Log.v(Constants.LOG_TAG,
		 * "AlbumList onPause temp's size is:"+temp.size());
		 */
		MyUtils.outLog("TabLocalActivity onPause");

		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		MyUtils.outLog("TabLocalActivity onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		MyUtils.outLog("TabLocalActivity onResume ");

		// TODO Auto-generated method stub
		albumImaTxtArray = getDownloadedAlbumList();
		//albumImaTxtArray = getDownloadedAlbumList();
		MyUtils.outLog("albumImaTxtArray is:"+albumImaTxtArray);
		// 获取当前类别的专辑列表
		// albumImaTxtArray = (List<ListItemInfo>) getIntent()
		// .getParcelableExtra(Constants.LIST_ALBUM_IMAGE_ADN_TEXT_KEY);

		// 设置适配器
		adapter = new ListItemInfoAdapterLocal(this,
				albumImaTxtArray, albumListView);
		albumListView.setAdapter(adapter);
		albumListView.setOnItemClickListener(new ListItemClickListener());
		albumListView.setOnScrollListener(new ListItemScrollListener());
		adapter.notifyDataSetChanged();

		/*
		 * if(Constants.executorService.isShutdown()){ Constants.executorService
		 * = Executors.newFixedThreadPool(5); Log.v(Constants.LOG_TAG,
		 * "AlbumList onResume and Constants.executorService.isShutdown()"); }
		 */
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		MyUtils.outLog("TabLocalActivity onStart ");

		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		MyUtils.outLog("TabLocalActivity onStop ");
		super.onStop();
	}

	/**
	 * listview监听器
	 * 
	 * @author yuguangbao
	 * 
	 */
	class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ListItemInfo mListItemInfo = albumImaTxtArray.get(arg2);
			/**
			 * 如果专辑中的图片数为0，则直接返回
			 */
			DownloadInfo downInfo = Constants.hashMapDownloadInfo.get(mListItemInfo.getAlbumId());
			if(downInfo==null||downInfo.getArrImageInfo().size()==0){
				Toast.makeText(TabLocalActivity.this,TabLocalActivity.this.getResources().getString(R.string.no_pic_tip), Toast.LENGTH_SHORT).show();
				return;
			}
			Intent mIntent;

			// 将专辑信息传到下一个activity
			mIntent = new Intent().setClass(TabLocalActivity.this,
					LocalPicViewer.class);
			// mIntent.putExtras(mBundle);
			mIntent.putExtra(Constants.CATA_ID, mListItemInfo.getCataId());
			mIntent.putExtra(Constants.ALBUM_ID, mListItemInfo.getAlbumId());
			// AlbumList.this.startActivity(mIntent);

			mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			TabLocalActivity.this.startActivity(mIntent);
		}
	}

	class ListItemScrollListener implements OnScrollListener {
		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// loadImage.doTask();
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub
			// loadImage.doTask();
		}
	}

	protected List<ListItemInfo> getDownloadedAlbumList( ) {
		List<ListItemInfo> albumArray = new ArrayList<ListItemInfo>();
		//DownloadInfo mDownloadInfo = null;
		ListItemInfo albumItem = null;
		String strAlbumIconUrl = null;
		String strAlbumIconDir = null;
		String strAlbumIconPath = null;
		String strInfo1 = null;
		String strInfo2 = null;
//		Button downloadButton = null;
//		String downloadProgress = null;

		if(Constants.hashMapDownloadInfo!=null){
			
			HashMap<String, DownloadInfo> hashMap=Constants.hashMapDownloadInfo;
			Iterator<Entry<String, DownloadInfo>> iter = hashMap.entrySet().iterator();
			while (iter.hasNext()) {
				HashMap.Entry entry = (HashMap.Entry) iter.next();
				String strKey = (String) entry.getKey();
				DownloadInfo downInfo = (DownloadInfo) entry.getValue();
				downInfo.getImageInfoFromLocal();//初始化图片列表
				strAlbumIconDir = Constants.DOWNLOAD_PATH + downInfo.getmAlbumId() + "/";
				if (downInfo.getArrImageInfo().size() > 0) {
					strAlbumIconPath = strAlbumIconDir
							+ downInfo.getArrImageInfo().get(0).getImgName();
				} else {
					strAlbumIconPath = Constants.LOCAL_PATH
							+ downInfo.getmAlbumId() + "/icon"
							+ Constants.CACHE_SUFFIX;
				}
				// strAlbumIconUrl = Constants.PRE_URL + mAlbumInfo.getId()
				// + "/" + mAlbumInfo.getIconName();
				strAlbumIconUrl = Constants.REQ_SERV + "?"
						+ Constants.urlParams.getIconParam(downInfo.getmAlbumId());

				strInfo1 = getResources().getString(R.string.pic_amount) + "："
						+ downInfo.getDownloadedAmout() + "  "
						+ getResources().getString(R.string.explore_pic) + "："
						+ downInfo.getClickAmount() + "  "
						+ getResources().getString(R.string.praise_num) + "："
						+ downInfo.getPraiseAmount();
				strInfo2 = getResources().getString(R.string.download) + "："
						+ downInfo.getDownLoadAmount();

				// start modify for download 2013-10-05
				albumItem = new ListItemInfo(strAlbumIconDir, strAlbumIconPath,
						strAlbumIconUrl, downInfo.getmAlbumName(), strInfo1,
						strInfo2, "temp", "temp");
				albumItem.setAlbumId(downInfo.getmAlbumId());
				albumItem.setCataId(downInfo.getCategoryId());
				// end modify
				albumArray.add(albumItem);
			}
		
		}
		return albumArray;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			MyUtils.exitDig(TabLocalActivity.this);
			//this.finish();
		}
		// For 万普 ---------手动获取推送广告
//		 if(Constants.appInfo.getIsAddAds()){
//		AppConnect.getInstance(this).getPushAd();
//		 }
//
//		 AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
//		 alertbBuilder
//		 .setTitle("退出提醒")
//		 .setMessage("你确定要离开吗？")
//		 .setPositiveButton("确定",
//		 new DialogInterface.OnClickListener() {
//		
//		 @Override
//		 public void onClick(DialogInterface dialog,
//		 int which) {
//		 MyUtils.outLog("start finish autoUpdate");
//		
//		 //自动升级--停掉服务
//		 if(TabLocalActivity.this.myUpdate.getBinder()!=null){
//			 TabLocalActivity.this.myUpdate.stopService();
//		 }
//
//		// 芒果广告释放资源
//		// 清除 adsMogoLayout 实例 所产生用于多线程缓冲机制的线程池
//		// 此方法请不要轻易调用，如果调用时间不当，会造成无法统计计数
//		 if (adsMogoLayout != null) {
//		 adsMogoLayout.clearThread();
//		 }
//		 //for 万普广告
//		 if(Constants.appInfo.getIsAddAds()){
//		 }
//		// 结束这个Activity
//
//		 MyUtils.quitApp();
//		 }
//		 })
//		 .setNegativeButton("取消",
//		 new DialogInterface.OnClickListener() {		
//		 @Override
//		 public void onClick(DialogInterface dialog,
//		 int which) {
//		 dialog.cancel();	
//		 }
//		 }).create();
//		 alertbBuilder.show();
//		 return true;
//		 }
//		TabLocalActivity.this.finish();
		return super.onKeyDown(keyCode, event);

	}



	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// startActivity(new Intent(this, translucentButton.class));
			// overridePendingTransition(R.anim.fade, R.anim.hold);
		}
		return super.onKeyUp(keyCode, event);
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
			Toast.makeText(TabLocalActivity.this,
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
	public void onCloseMogoDialog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailedReceiveAd() {
		// TODO Auto-generated method stub

	}

	@Override
	public Class getCustomEvemtPlatformAdapterClass(
			AdsMogoCustomEventPlatformEnum arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClickAd(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRealClickAd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveAd(ViewGroup arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestAd(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCloseAd() {
		// TODO Auto-generated method stub
		return false;
	}
}