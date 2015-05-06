package com.android.main;

import java.util.ArrayList;
import java.util.List;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.asyncload.albumicon.ListItemInfo;
import com.android.asyncload.albumicon.ListItemInfoAdapter;
import com.android.beauty360.*;
import com.android.beauty360.point.MyPoints;
import com.android.download.HttpDownloader;

import com.android.info.AlbumInfo;
import com.android.info.CatagoryInfo;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.picview.PicViewer;
import com.android.utils.BtJsonParser;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;


public class TabHomeActivity extends ListActivity implements AdsMogoListener,PullDownListView.OnRefreshListioner {

	// mogo广告
	AdsMogoLayout adsMogoLayout;
	//积分墙广告
	MyPoints myPoints;
	MyHandler mHandler = new MyHandler();
	//MyUpdate myUpdate;

	RelativeLayout layout;
	TextView tv_front;// 需要移动的View

	TextView tv_bar_01;
	TextView tv_bar_02;
	TextView tv_bar_03;
	TextView tv_bar_04;

	String mCataName01, mCataName02, mCataName03, mCataName04;

	// 此页面的所有专辑信息
	//ArrayList<AlbumInfo> mArrayListAlbumInfo;
	// 此页面的每个专辑的图片和文字展示信息
	List<ListItemInfo> albumImaTxtArray;

	CatagoryInfo mCatagoryInfo = null;// 用户存放类别
	ListItemInfoAdapter adapter;
	// 标识本activity的list是否是子专辑list
	// boolean isSubAlbumList = false;

	// TextView tv_bar_science;
	// TextView tv_bar_more;

	//int avg_width = 0;// 用于记录平均每个标签的宽度，移动的时候需要

	ListView mListView = null;
	
	private ProgressDialog mBarDialog = null;//首次加载进度条
	
	private PullDownListView mPullDownView;//下拉刷新
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {// 下拉刷新
				mPullDownView.onRefreshComplete();//这里表示刷新处理完成后把上面的加载刷新界面隐藏
				if(mCatagoryInfo.getCurAlbumAmount()>=mCatagoryInfo.getAlbumAmount()){
					mPullDownView.setMore(false);//这里设置false表示没有更多加载
				}else{
					mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
				}		
				adapter.notifyDataSetChanged();
			} else if (msg.what == 1) {//加载更多
				mPullDownView.onLoadMoreComplete();//这里表示加载更多处理完成后把下面的加载更多界面（隐藏或者设置字样更多）
				if(mCatagoryInfo.getCurAlbumAmount()<mCatagoryInfo.getAlbumAmount())//判断当前list中已添加的数据是否小于最大值maxAount，是那么久显示更多否则不显示
					mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
				else
					mPullDownView.setMore(false);
				adapter.notifyDataSetChanged();	
			}else if (msg.what == 2) {//初始化第一页
				if (mBarDialog.isShowing()) {
					mBarDialog.cancel();
				}
				adapter = new ListItemInfoAdapter(TabHomeActivity.this,
						albumImaTxtArray, mListView,TabHomeActivity.this);
				adapter.notifyDataSetChanged();
				mListView.setAdapter(adapter);
				mListView.setOnItemClickListener(new ListItemClickListener());
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_home);
		// 积分广告初始化
		this.myPoints=new MyPoints(this,null);
		
		mBarDialog = new ProgressDialog(this);//初始化
		mBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mBarDialog.setMessage(this.getResources().getString(R.string.loading));
		
//		AppConnect.getInstance(this);
//		AppConnect.getInstance(this).getPoints(this);// 初始化积分
		// 设置广告
		// if(Constants.appInfo.getIsAddAds()){
		// 设置监听回调 其中包括 请求 展示 请求失败等事件的回调
		adsMogoLayout = ((AdsMogoLayout) this.findViewById(R.id.adsMogoView));
		adsMogoLayout.setAdsMogoListener(this);
		// }

		initViews();// 初始化主界面
		this.showAlbumList(mCataName01);// 展示第一个类别的专辑列表
		// 判断是否需要升级
		//myUpdate.update();
		// if (Constants.appInfo != null && Constants.isShowUpdateDig == false
		// && Constants.appInfo.getIsNeedUpdate()) {
		// Constants.isShowUpdateDig = true;
		// Constants.myUpdate.update();
		// }
	}
	
	/**
	 * 
	 * @param cataName
	 */
	void showAlbumList(String cataName) {

		MyUtils.outLog("showAlbumList and cataName is:"+cataName);
		mListView = (ListView) this.getListView();

		for (int i = 0; i < Constants.mCatagorysInfo.size(); i++) {
			if (Constants.mCatagorysInfo.get(i).getCatagoryName() != null
					&& Constants.mCatagorysInfo.get(i).getCatagoryName()
							.equals(cataName)) {
				mCatagoryInfo = Constants.mCatagorysInfo.get(i);
			}
		}

		// 本activety包含的内容是否是子专辑
		// isSubAlbumList=getIntent().getExtras().getBoolean(Constants.IS_SUBALBUM_KEY);

		// getIntent()
		// .getSerializableExtra(Constants.LIST_ALBUM_IMAGE_ADN_TEXT_KEY);

		// 显示进度条，提示用户正在加载
		if (mCatagoryInfo.getmArrayListAlbumsInfo() == null
				|| mCatagoryInfo.getmArrayListAlbumsInfo().size() == 0) {
			mBarDialog.show();
		}
		
    	mPullDownView = (PullDownListView) findViewById(R.id.sreach_list);
		mPullDownView.setRefreshListioner(this);
		mListView = mPullDownView.mListView;
		if(mCatagoryInfo.getCurAlbumAmount()>=mCatagoryInfo.getAlbumAmount()){
			mPullDownView.setMore(false);//这里设置false表示没有更多加载
		}else{
			mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
		}		
		// 获取当前类别的专辑列表
		Thread getCurCataThread=new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				albumImaTxtArray = getAlbumList(mCatagoryInfo);// (List<ImageAndText>)
				
				Message mMessage = mHandler.obtainMessage(2);
				mHandler.sendMessage(mMessage);
			}			
		};
		getCurCataThread.start();
		
		//albumImaTxtArray = this.getAlbumList(mCatagoryInfo);// (List<ImageAndText>)
	    // 设置适配器
//		adapter = new ListItemInfoAdapter(this,
//				albumImaTxtArray, mListView,this);
//		adapter.notifyDataSetChanged();
//		mListView.setAdapter(adapter);
//		mListView.setOnItemClickListener(new ListItemClickListener());
		// mListView.setOnScrollListener(new ListItemScrollListener());
	}

	class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// 获取用户点击的图片专辑信息
			ArrayList<AlbumInfo> mArrayListAlbumInfo = mCatagoryInfo.getmArrayListAlbumsInfo();//获取当前类别下的专辑
			AlbumInfo mAlbumInfo = mArrayListAlbumInfo.get(arg2-1);
			MyUtils.outLog("clicked album name is:"+mAlbumInfo.getmAlbumName() +" and index is:"+arg2);
			Intent mIntent;// 定义intent
			if (mAlbumInfo.getArrSubAlbumInfo() != null
					&& mAlbumInfo.getArrSubAlbumInfo().size() > 0) {// 如果有子专辑，则转向子专辑列表
				// 获取当前专辑的子专辑列表

				mIntent = new Intent().setClass(TabHomeActivity.this,
						AlbumList.class);
				mIntent.putExtra(Constants.ALBUM_ID, mAlbumInfo.getId());
				mIntent.putExtra(Constants.CATA_ID, mAlbumInfo.getCategoryId());
				/* 设置intent内容，传递图片专辑信息到下一个activity */
//				Bundle mBundle = new Bundle();
//				// 设置每个页面中的子专辑图标和文字列表
//				mBundle.putParcelable(Constants.LIST_ALBUM_IMAGE_ADN_TEXT_KEY,
//						(Parcelable) subAlbumImaTxtArray);
//				
//				// 设置每个页面的所有子专辑信息
//				mBundle.putParcelable(Constants.ARR_ALBUMS_INFO_KEY,
//						(Parcelable) mAlbumInfo.getArrSubAlbumInfo());
//				// mBundle.putBoolean(Constants.IS_SUBALBUM_KEY, true);
//				mIntent.putExtras(mBundle);
				// TabMainActivity.this.startActivity(intent);

			} else {
				// 将专辑信息传到下一个activity
				//Bundle mBundle = new Bundle();
				//mBundle.putParcelable(Constants.ALBUM_INFO_KEY, mAlbumInfo);
				mIntent = new Intent().setClass(TabHomeActivity.this,
						PicViewer.class);
				mIntent.putExtra(Constants.CATA_ID,mAlbumInfo.getCategoryId());
				mIntent.putExtra(Constants.ALBUM_ID,mAlbumInfo.getId());

				//mIntent.putExtras(mBundle);
			}

			mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			TabHomeActivity.this.startActivity(mIntent);
		}

	}


	/**
	 * 初始化主界面
	 */
	private void initViews() {

		layout = (RelativeLayout) findViewById(R.id.cata_tab01);

		tv_bar_01 = (TextView) findViewById(R.id.tv_title_bar_01);
		tv_bar_02 = (TextView) findViewById(R.id.tv_title_bar_02);
		tv_bar_03 = (TextView) findViewById(R.id.tv_title_bar_03);
		tv_bar_04 = (TextView) findViewById(R.id.tv_title_bar_04);

		//if(Constants.mCatagorysInfo!=null){
		mCataName01 = Constants.mCatagorysInfo.get(0).getCatagoryName();
		mCataName02 = Constants.mCatagorysInfo.get(1).getCatagoryName();
		mCataName03 = Constants.mCatagorysInfo.get(2).getCatagoryName();
		mCataName04 = Constants.mCatagorysInfo.get(3).getCatagoryName();
//		}else{
//			MyUtils.outLog("TabHomeActivity initViews  Constants.mCatagorysInfo is:"+Constants.mCatagorysInfo);
//		}

		tv_bar_01.setText(mCataName01);
		tv_bar_02.setText(mCataName02);
		tv_bar_03.setText(mCataName03);
		tv_bar_04.setText(mCataName04);
		// tv_bar_science = (TextView) findViewById(R.id.tv_title_bar_science);
		// tv_bar_more = (TextView) findViewById(R.id.tv_title_bar_more);

		tv_bar_01.setOnClickListener(onClickListener);
		tv_bar_02.setOnClickListener(onClickListener);
		tv_bar_03.setOnClickListener(onClickListener);
		tv_bar_04.setOnClickListener(onClickListener);
		// tv_bar_science.setOnClickListener(onClickListener);
		// tv_bar_more.setOnClickListener(onClickListener);

		tv_front = new TextView(this);
		tv_front.setBackgroundResource(R.drawable.slidebar_1);
		// /tv_front.setBackgroundColor(Color.BLUE);
		tv_front.setTextColor(Color.WHITE);
		tv_front.setPadding(4, 0, 4, 0);
		tv_front.setText(mCataName01);
		tv_front.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		param.setMargins(0, 0, 0, 0);
		layout.addView(tv_front, param);
		// layout.addView(tv_front, param);
	}

	// 动态生成titlebar中的RelativeLayout
	// public RelativeLayout getTitleRL(String strTitle,int id){
	//
	// RelativeLayout resRL= new RelativeLayout(this);
	// RelativeLayout.LayoutParams param=new RelativeLayout.LayoutParams(
	// ViewGroup.LayoutParams.FILL_PARENT,
	// ViewGroup.LayoutParams.FILL_PARENT);
	// resRL.setPadding(5, 0, 5, 0);
	//
	// resRL.addView(this.getTitleTv(strTitle,id),param);
	// return resRL;
	// }

	// 动态生成title bar中的textView
	// private TextView getTitleTv(String strTitle,int id){
	// TextView tv=new TextView(this);
	// tv.setTextColor(Color.RED);
	// tv.setText(strTitle);
	// tv.setGravity(Gravity.CENTER);
	// tv.setId(id);
	// tv.setLayoutParams(new
	// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
	// tv_bar_zh.setOnClickListener(onClickListener);
	// return tv;
	// }

	private OnClickListener onClickListener = new OnClickListener() {
		RelativeLayout.LayoutParams param;
		@Override
		public void onClick(View v) {
			//avg_width = findViewById(R.id.cata_tab01).getWidth();
			param = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			param.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			param.setMargins(0, 0, 0, 0);
			switch (v.getId()) {
			case R.id.tv_title_bar_01:
				tv_front.setText(mCataName01);
				//MoveBg.moveFrontBg(tv_front, startX, 0, 0, 0);
				//startX = 0;
				// tv_front.setText(R.string.title_category_rhmn);
				layout.removeView(tv_front);
				layout=(RelativeLayout)findViewById(R.id.cata_tab01);
				layout.addView(tv_front, param);	
				break;
			case R.id.tv_title_bar_02:
				tv_front.setText(mCataName02);
				System.out.println("tv_front width is:" + tv_front.getWidth()
						+ " tv_front hight is:" + tv_front.getHeight());
				//MoveBg.moveFrontBg(tv_front, startX, avg_width, 0, 0);
				//startX = avg_width;
				// tv_front.setText(R.string.title_category_dlmn);
				layout.removeView(tv_front);
				layout=(RelativeLayout)findViewById(R.id.cata_tab02);
				layout.addView(tv_front, param);	

				break;
			case R.id.tv_title_bar_03:
				//MoveBg.moveFrontBg(tv_front, startX, avg_width * 2, 0, 0);
				//startX = avg_width * 2;
				// tv_front.setText(R.string.title_category_xfmn);
				tv_front.setText(mCataName03);
				layout.removeView(tv_front);
				layout=(RelativeLayout)findViewById(R.id.cata_tab03);
				layout.addView(tv_front, param);	
				break;
			case R.id.tv_title_bar_04:
				//MoveBg.moveFrontBg(tv_front, startX, avg_width * 3, 0, 0);
				//startX = avg_width * 3;
				// tv_front.setText(R.string.title_category_zh);
				tv_front.setText(mCataName04);
				layout.removeView(tv_front);
				layout=(RelativeLayout)findViewById(R.id.cata_tab04);
				layout.addView(tv_front, param);	
				break;
			// MoveBg.moveFrontBg(tv_front, startX, avg_width * 4, 0, 0);
			// startX = avg_width * 4;
			// tv_front.setText(R.string.title_news_category_science);
			// break;
			// case R.id.tv_title_bar_more:
			// MoveBg.moveFrontBg(tv_front, startX, avg_width * 5, 0, 0);
			// startX = avg_width * 5;
			// tv_front.setText(R.string.title_news_category_more);
			// break;

			default:
				break;
			}
			TabHomeActivity.this.showAlbumList(tv_front.getText().toString());
		}
	};

	/**
	 * 获取每个页面中的专辑图标和文字列表
	 * 
	 * @param mCatagoryInfo
	 * @return
	 */
	protected List<ListItemInfo> getMoreAlbumList(CatagoryInfo mCatagoryInfo) {
		
		AlbumInfo mAlbumInfo = null;
		ListItemInfo albumItem = null;
		String strAlbumIconUrl = null;
		String strAlbumIconDir = null;
		String strAlbumIconPath = null;
		String strInfo1 = null;
		String strInfo2 = null;
		// start add for download 2013-10-17
		String downButtonTxt = "";
		String downStatusTxt = "";		
		// end add
		//MyUtils.outLog("enter getMoreAlbumList and mCatagoryInfo.getCurAlbumAmount() is:"+mCatagoryInfo.getCurAlbumAmount());
		BtJsonParser jsonParser = new BtJsonParser();
		String strJson=HttpDownloader.getResFromServer(Constants.REQ_SERV+"?"+Constants.urlParams.getAlbumsParam(mCatagoryInfo.getId(),mCatagoryInfo.getCurAlbumAmount()));
		List<ListItemInfo> albumArray = new ArrayList<ListItemInfo>();
		ArrayList<AlbumInfo> mArrayListAlbumInfo = jsonParser.albumParser(strJson);
		mCatagoryInfo.addAlbums(mArrayListAlbumInfo);
		
		for (int i = 0; i < mArrayListAlbumInfo.size(); i++) {
			mAlbumInfo = mArrayListAlbumInfo.get(i);
			strAlbumIconDir = Constants.LOCAL_PATH + mAlbumInfo.getId() + "/";
			strAlbumIconPath = Constants.LOCAL_PATH + mAlbumInfo.getId()
					+ "/icon" + Constants.CACHE_SUFFIX;

			// String
			// strUrl=HttpDownloader.getResFromServer(Constants.REQ_SERV+"?"+Constants.urlParams.getParams());
			strAlbumIconUrl = Constants.REQ_SERV + "?"
					+ Constants.urlParams.getIconParam(mAlbumInfo.getId());

			// strAlbumIconUrl = Constants.PRE_URL + mAlbumInfo.getAlbumDir()
			// + "/" + mAlbumInfo.getIconName();
			if (mAlbumInfo.getArrSubAlbumInfo().size() > 0) {
				strInfo1 = this.getResources().getString(R.string.album_amount)
						+ "：" + mAlbumInfo.getPicAmount() + "  "
						+ this.getResources().getString(R.string.explore_pic)
						+ "：" + mAlbumInfo.getClickAmount() + "  "
						+ this.getResources().getString(R.string.praise_num)
						+ "：" + mAlbumInfo.getPraiseAmount();
				//add start for download 2013-11-17
				downButtonTxt="";
				downStatusTxt="";
				//add end
			} else {
				strInfo1 = this.getResources().getString(R.string.pic_amount)
						+ "：" + mAlbumInfo.getPicAmount() + "  "
						+ this.getResources().getString(R.string.explore_pic)
						+ "：" + mAlbumInfo.getClickAmount() + "  "
						+ this.getResources().getString(R.string.praise_num)
						+ "：" + mAlbumInfo.getPraiseAmount();
				//add start for download 2013-11-17
				downButtonTxt=DownloadInfo.getDownButtonTxt(mAlbumInfo.getId(),this);
				downStatusTxt=DownloadInfo.getDownStatusTxt(mAlbumInfo.getId(),this);
				//add end
			}
			strInfo2 = this.getResources().getString(R.string.download) + "："
					+ mAlbumInfo.getDownLoadAmount();
			// start modify for download 2013-10-05

			albumItem = new ListItemInfo(strAlbumIconDir, strAlbumIconPath,
					strAlbumIconUrl, mAlbumInfo.getmAlbumName(), strInfo1,
					strInfo2, downButtonTxt, downStatusTxt);
			albumItem.setAlbumId(mAlbumInfo.getId());
			albumItem.setCataId(mAlbumInfo.getCategoryId());
			// end modify
			albumArray.add(albumItem);
		}
		return albumArray;
	}

	/**
	 * 获取每个页面中的专辑图标和文字列表
	 * 
	 * @param mCatagoryInfo
	 * @return
	 */
	protected List<ListItemInfo> getAlbumList(CatagoryInfo mCatagoryInfo) {
		
		BtJsonParser jsonParser;
		List<ListItemInfo> albumArray=new ArrayList<ListItemInfo>();
		ArrayList<AlbumInfo> mArrayListAlbumInfo=null;
		
		AlbumInfo mAlbumInfo = null;
		ListItemInfo albumItem = null;
		String strAlbumIconUrl = null;
		String strAlbumIconDir = null;
		String strAlbumIconPath = null;
		String strInfo1 = null;
		String strInfo2 = null;
		// start add for download 2013-10-17
		String downButtonTxt = "";
		String downStatusTxt = "";		
		// end add

		//如果已加载过专辑，直接读取并展示

		if(mCatagoryInfo.getmArrayListAlbumsInfo()!=null && mCatagoryInfo.getmArrayListAlbumsInfo().size()>0){
			mArrayListAlbumInfo=mCatagoryInfo.getmArrayListAlbumsInfo();
		}else{//如果未加载过专辑，需要从服务器加载
			jsonParser = new BtJsonParser();
			String strJson=HttpDownloader.getResFromServer(Constants.REQ_SERV+"?"+Constants.urlParams.getAlbumsParam(mCatagoryInfo.getId(),mCatagoryInfo.getCurAlbumAmount()));
			mArrayListAlbumInfo = jsonParser.albumParser(strJson);
			mCatagoryInfo.addAlbums(mArrayListAlbumInfo);
		}

		
		for (int i = 0; i < mArrayListAlbumInfo.size(); i++) {
			mAlbumInfo = mArrayListAlbumInfo.get(i);
			strAlbumIconDir = Constants.LOCAL_PATH + mAlbumInfo.getId() + "/";
			strAlbumIconPath = Constants.LOCAL_PATH + mAlbumInfo.getId()
					+ "/icon" + Constants.CACHE_SUFFIX;

			// String
			// strUrl=HttpDownloader.getResFromServer(Constants.REQ_SERV+"?"+Constants.urlParams.getParams());
			strAlbumIconUrl = Constants.REQ_SERV + "?"
					+ Constants.urlParams.getIconParam(mAlbumInfo.getId());

			// strAlbumIconUrl = Constants.PRE_URL + mAlbumInfo.getAlbumDir()
			// + "/" + mAlbumInfo.getIconName();
			if (mAlbumInfo.getArrSubAlbumInfo().size() > 0) {
				strInfo1 = this.getResources().getString(R.string.album_amount)
						+ "：" + mAlbumInfo.getPicAmount() + "  "
						+ this.getResources().getString(R.string.explore_pic)
						+ "：" + mAlbumInfo.getClickAmount() + "  "
						+ this.getResources().getString(R.string.praise_num)
						+ "：" + mAlbumInfo.getPraiseAmount();
				//add start for download 2013-11-17
				downButtonTxt="";
				downStatusTxt="";
				//add end
			} else {
				strInfo1 = this.getResources().getString(R.string.pic_amount)
						+ "：" + mAlbumInfo.getPicAmount() + "  "
						+ this.getResources().getString(R.string.explore_pic)
						+ "：" + mAlbumInfo.getClickAmount() + "  "
						+ this.getResources().getString(R.string.praise_num)
						+ "：" + mAlbumInfo.getPraiseAmount();
				//add start for download 2013-11-17
				downButtonTxt=DownloadInfo.getDownButtonTxt(mAlbumInfo.getId(),this);
				downStatusTxt=DownloadInfo.getDownStatusTxt(mAlbumInfo.getId(),this);
				//add end
			}
			strInfo2 = this.getResources().getString(R.string.download) + "："
					+ mAlbumInfo.getDownLoadAmount();
			// start modify for download 2013-10-05

			albumItem = new ListItemInfo(strAlbumIconDir, strAlbumIconPath,
					strAlbumIconUrl, mAlbumInfo.getmAlbumName(), strInfo1,
					strInfo2, downButtonTxt, downStatusTxt);
			albumItem.setAlbumId(mAlbumInfo.getId());
			albumItem.setCataId(mAlbumInfo.getCategoryId());
			// end modify
			albumArray.add(albumItem);
		}
		return albumArray;
	}
	/**
	 * 重载getAlbumList，获取子专辑的列表信息
	 * @param mArrayListAlbumInfo
	 * @return
	 */
//	protected List<ListItemInfo> getAlbumList(
//			ArrayList<AlbumInfo> mArrayListAlbumInfo) {
//		List<ListItemInfo> albumArray = new ArrayList<ListItemInfo>();
//		AlbumInfo mAlbumInfo = null;
//		ListItemInfo albumItem = null;
//		String strAlbumIconUrl = null;
//		String strAlbumIconDir = null;
//		String strAlbumIconPath = null;
//		String strInfo1 = null;
//		String strInfo2 = null;
//		//add start for download 2013-11-17
//		String downButtonTxt="";
//		String downStatusTxt="";
//		//add end
//		for (int i = 0; i < mArrayListAlbumInfo.size(); i++) {
//			mAlbumInfo = mArrayListAlbumInfo.get(i);
//			strAlbumIconDir = Constants.LOCAL_PATH + mAlbumInfo.getId() + "/";
//			strAlbumIconPath = Constants.LOCAL_PATH + mAlbumInfo.getId()
//					+ "/icon" + Constants.CACHE_SUFFIX;
//			// strAlbumIconUrl = Constants.PRE_URL + mAlbumInfo.getId()
//			// + "/" + mAlbumInfo.getIconName();
//			strAlbumIconUrl = Constants.REQ_SERV + "?"
//					+ Constants.urlParams.getIconParam(mAlbumInfo.getId());
//			if (mAlbumInfo.getArrSubAlbumInfo().size() > 0) {
//				strInfo1 = this.getResources().getString(R.string.album_amount)
//						+ "：" + mAlbumInfo.getPicAmount() + "  "
//						+ getResources().getString(R.string.explore_pic) + "："
//						+ mAlbumInfo.getClickAmount() + "  "
//						+ getResources().getString(R.string.praise_num) + "："
//						+ mAlbumInfo.getPraiseAmount();
//				//add start for download 2013-11-17
//				downButtonTxt="";
//				downStatusTxt="";
//				//add end
//			} else {
//				strInfo1 = getResources().getString(R.string.pic_amount) + "："
//						+ mAlbumInfo.getPicAmount() + "  "
//						+ getResources().getString(R.string.explore_pic) + "："
//						+ mAlbumInfo.getClickAmount() + "  "
//						+ getResources().getString(R.string.praise_num) + "："
//						+ mAlbumInfo.getPraiseAmount();
//				//add start for download 2013-11-17
//				downButtonTxt=DownloadInfo.getDownButtonTxt(mAlbumInfo.getId(),this);
//				downStatusTxt=DownloadInfo.getDownStatusTxt(mAlbumInfo.getId(),this);
//				//add end				
//			}
//
//			strInfo2 = getResources().getString(R.string.download) + "："
//					+ mAlbumInfo.getDownLoadAmount();
//			// start modify for download 2013-10-05			
//			albumItem = new ListItemInfo(strAlbumIconDir, strAlbumIconPath,
//					strAlbumIconUrl, mAlbumInfo.getmAlbumName(), strInfo1,
//					strInfo2, downButtonTxt,downStatusTxt);
//			albumItem.setAlbumId(mAlbumInfo.getId());
//			albumItem.setCataId(mAlbumInfo.getCategoryId());
//			// end modify
//			albumArray.add(albumItem);
//		}
//		return albumArray;
//	}

	


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
	public boolean onCloseAd() {
		// TODO Auto-generated method stub
		return false;
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// TabMainActivity.this.finish();
			MyUtils.exitDig(TabHomeActivity.this);
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
			Toast.makeText(this,
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyUtils.outLog("TabHomeActivity onDestroy");

		super.onDestroy();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		Thread refreshThread=new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				albumImaTxtArray.clear();
				mCatagoryInfo.getmArrayListAlbumsInfo().clear();
				mCatagoryInfo.setCurAlbumAmount(0);
				List<ListItemInfo> arrAlbumInfo=TabHomeActivity.this.getAlbumList(mCatagoryInfo);
				for(int i=0;i<arrAlbumInfo.size();i++){
					albumImaTxtArray.add(arrAlbumInfo.get(i));
				}
				
				Message mMessage = mHandler.obtainMessage(0);
				mHandler.sendMessage(mMessage);
			}			
		};
		refreshThread.start();
//		mHandler.postDelayed(new Runnable() {
//			
//			public void run() {
//				albumImaTxtArray.clear();
//				mCatagoryInfo.getmArrayListAlbumsInfo().clear();
//				mCatagoryInfo.setCurAlbumAmount(0);
//				List<ListItemInfo> arrAlbumInfo=TabHomeActivity.this.getAlbumList(mCatagoryInfo);
//				for(int i=0;i<arrAlbumInfo.size();i++){
//					albumImaTxtArray.add(arrAlbumInfo.get(i));
//				}
//
//				mPullDownView.onRefreshComplete();//这里表示刷新处理完成后把上面的加载刷新界面隐藏
//				if(mCatagoryInfo.getCurAlbumAmount()>=mCatagoryInfo.getAlbumAmount()){
//					mPullDownView.setMore(false);//这里设置false表示没有更多加载
//				}else{
//					mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
//				}		
//				adapter.notifyDataSetChanged();
//			}
//		}, 10);
		
		MyUtils.outLog("onRefresh");

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		Thread loadMoreThread=new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				List<ListItemInfo> arrAlbumInfo=TabHomeActivity.this.getMoreAlbumList(mCatagoryInfo);
				int n=albumImaTxtArray.size()+arrAlbumInfo.size();
				MyUtils.outLog("albumImaTxtArray.size() is:"+albumImaTxtArray.size()+" n is:"+n+" arrAlbumInfo.size() is:"+arrAlbumInfo.size());

				for(int i=0;i<arrAlbumInfo.size();i++){
					albumImaTxtArray.add(arrAlbumInfo.get(i));
				}
				
				Message mMessage = mHandler.obtainMessage(1);
				mHandler.sendMessage(mMessage);
			}			
		};
		loadMoreThread.start();
//		mHandler.postDelayed(new Runnable() {
//			
//			public void run() {
//				List<ListItemInfo> arrAlbumInfo=TabHomeActivity.this.getMoreAlbumList(mCatagoryInfo);
//				int n=albumImaTxtArray.size()+arrAlbumInfo.size();
//				MyUtils.outLog("albumImaTxtArray.size() is:"+albumImaTxtArray.size()+" n is:"+n+" arrAlbumInfo.size() is:"+arrAlbumInfo.size());
//
//				for(int i=0;i<arrAlbumInfo.size();i++){
//					albumImaTxtArray.add(arrAlbumInfo.get(i));
//				}
//				mPullDownView.onLoadMoreComplete();//这里表示加载更多处理完成后把下面的加载更多界面（隐藏或者设置字样更多）
//				if(mCatagoryInfo.getCurAlbumAmount()<mCatagoryInfo.getAlbumAmount())//判断当前list中已添加的数据是否小于最大值maxAount，是那么久显示更多否则不显示
//					mPullDownView.setMore(true);//这里设置true表示还有更多加载，设置为false底部将不显示更多
//				else
//					mPullDownView.setMore(false);
//				adapter.notifyDataSetChanged();	
//			}
//		}, 10);
		MyUtils.outLog("onLoadMore");
	}
	
}
