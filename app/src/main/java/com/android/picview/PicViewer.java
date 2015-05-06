package com.android.picview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.android.download.HttpDownloader;
import com.android.info.AlbumInfo;
import com.android.info.Constants;
import com.android.beauty360.R;
import com.android.beauty360.point.MyPoints;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.*;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
public class PicViewer extends Activity implements OnGestureListener,AdsMogoListener {

	/*
	 * 利用多点触控来控制ImageView中图像的放大与缩小 手指控制图片移动
	 */

	private MyImageView imageView;
	private Integer intPicPositon;
	public static final int NEW_NAME1 = 1;
	public static final int NEW_NUM1 = 2;

	private AlbumInfo albumInfo = null;
	//private String strPreUrl = null;
	private GestureDetector mygesture = null;
	private MyHandler myHander = new MyHandler();
	// 初始化Bar所用到的变量
	LayoutInflater mInflater = null;
	View mLayout = null;
	private PopupWindow popupWindow;
	private Integer popWinLocHig;
	private ProgressDialog mBarDialog = null;
	//String preDLMsg="正在下载，请稍候";
	// =============================
	//定义图片切换动画
	TranslateAnimation mTransFromRight=null;
	TranslateAnimation mTransFromLeft=null;
	
	// alter dialog
	AlertDialog.Builder mAlertDialogBuilder = null;

	//For recycle 
	Bitmap lastBitmap=null;
	Bitmap curBitmap= null;
	
	//标识是否正在删除缓存
	static boolean  isDeleting=false;
	
	//自动播放标识	
	boolean isRunning=true;
	boolean isStop=false;
	
	//芒果广告
	AdsMogoLayout adsMogoLayoutCode;
	MyPoints myPoints;
	//test free mem
	//	private final static float TARGET_HEAP_UTILIZATION = 0.75f; 
	//	private final static int CWJ_HEAP_SIZE = 6* 1024* 1024 ;    
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_viewer);
		mAlertDialogBuilder = new AlertDialog.Builder(this);
		//Log.v(Constants.LOG_TAG, "enter pic view.onCreate().");
		this.myPoints=new MyPoints(this,null);
		initVar();
		initPopuptWindow();
		//设置广告
		//if(Constants.appInfo.getIsAddAds()){
		addMogoAdView();
		
		//}
		// 设置背景
		imageView.setBackgroundColor(getResources().getColor(R.color.cream));
		this.showImage(this.getIntPicPositon(),Constants.NORMAL);
	}
	
	//设置广告
	void addMogoAdView(){
		
		// 构造方法，设置快速模式
		adsMogoLayoutCode = new AdsMogoLayout(this,
				 Constants.MOGO_ID);



		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告出现的位置(悬浮于底部)
		params.bottomMargin = 0;
		// 添加广告状态监听
		// 设置监听回调 其中包括 请求 展示 请求失败等事件的回调
		adsMogoLayoutCode.setAdsMogoListener(this);
		params.gravity = Gravity.BOTTOM;
		addContentView(adsMogoLayoutCode, params);
	}
	

/*	void addDianRuAdView(){
		final AdSpace space = new AdSpace(this);
		space.setInterval(30);	//设置广告刷新时间
		space.setType(0);		//设置广告类型，此处其值为0
		space.setKeyword("game");//设置关键字
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告出现的位置(悬浮于底部)
		params.bottomMargin = 0;
		// 添加广告状态监听
		params.gravity = Gravity.BOTTOM;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.bottomMargin=0;
		//mainLayout.addView(space, lp);
		this.addContentView(space, params);		
	}
*/	
	/**
	 * 初始化变量
	 */
	public void initVar() {
		// imageView = new MyImageView();
		// start设置imageView的属性
		imageView = (MyImageView) this.findViewById(R.id.myImageView);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setAdjustViewBounds(true);
		// end

		//loader = new AsyncImageLoader();
		mygesture = new GestureDetector(this);
		this.setIntPicPositon(0);
		// 初始化进度对话框
		mBarDialog = new ProgressDialog(PicViewer.this);
		mBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 获取上一个activity传入的专辑信息
//		albumInfo = (AlbumInfo) getIntent().getParcelableExtra(
//				Constants.ALBUM_INFO_KEY);
		albumInfo = MyUtils.getAlbumInfo(
				getIntent().getStringExtra(Constants.CATA_ID), getIntent()
						.getStringExtra(Constants.ALBUM_ID));
		// 显示下一张图片的动画设置
		mTransFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mTransFromRight.setInterpolator(AnimationUtils.loadInterpolator(this,
				 android.R.anim.accelerate_decelerate_interpolator));
		mTransFromRight.setDuration(500);
		// 显示上一张图片的动画设置
		mTransFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mTransFromLeft.setInterpolator(AnimationUtils.loadInterpolator(this,
				 android.R.anim.accelerate_decelerate_interpolator));
		mTransFromLeft.setDuration(500);
		// set picture's pre url
		//this.setStrPreUrl(Constants.PRE_URL + albumInfo.getId() + "/");
		//for auto play
		
	}
	/**
	 * * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub

		View popupWindow_view = getLayoutInflater().inflate( // 获取自定义布局文件pic_util_popwin.xml的视图
				R.layout.popwin_netpic, null, false);

		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);// 创建PopupWindow实例
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		ImageButton btStoreImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_store_image); // pic_util_popwin.xml视图里面的控件
		TextView txStoreImage = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_save);
		ImageButton btSetupBitmap = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_setup_bitmap);
		TextView txSetupBitmap = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_setup_bitmap);

/*		ImageButton btAppRecommendImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_app_recommend); // pic_util_popwin.xml视图里面的控件
		TextView tvAppRecommend = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_app_recommend);

		ImageButton btCommentsImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_comments);
		TextView tvComments = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_comments);*/
		
		ImageButton btLastImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_last_pic); // pic_util_popwin.xml视图里面的控件
		TextView txLastImage = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_last);

		ImageButton btNextImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_next_pic);
		TextView txNextImage = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_next);
		popWinLocHig = (Integer) (PicViewer.this.getWindowManager()
				.getDefaultDisplay().getHeight() / 4);

		btStoreImage.setOnClickListener(new SaveImageListener());
		txStoreImage.setOnClickListener(new SaveImageListener());
		btLastImage.setOnClickListener(new ShowLastListener());
		txLastImage.setOnClickListener(new ShowLastListener());
		btNextImage.setOnClickListener(new ShowNextListener());
		txNextImage.setOnClickListener(new ShowNextListener());

/*		btAppRecommendImage.setOnClickListener(new AppRecommendListener());
		tvAppRecommend.setOnClickListener(new AppRecommendListener());
		btCommentsImage.setOnClickListener(new CommentsListener());
		tvComments.setOnClickListener(new CommentsListener());*/
		btSetupBitmap.setOnClickListener(new SetupBitmapListener());
		txSetupBitmap.setOnClickListener(new SetupBitmapListener());
	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				
				if (mBarDialog.isShowing()) {
					mBarDialog.cancel();
				}
				//弹出下载完成后的提示框
				Toast toast = Toast.makeText(PicViewer.this,
						PicViewer.this.getResources().getString(R.string.downloaded_tip), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

/*				mAlertDialogBuilder
						.setMessage(Constants.DOWNLOADED_MSG)
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog mAlert = mAlertDialogBuilder.create();
				mAlert.show();
*/
			}else if(msg.what == 1){
				if (mBarDialog.isShowing()) {
					mBarDialog.cancel();
				}
				Log.v(Constants.LOG_TAG, "099");

				Bundle mBundle=(Bundle)msg.obj;	
				Bitmap mTempBitmap = (Bitmap) mBundle.getParcelable("bitmap");
				
				if(null== mTempBitmap){		//下载图片失败			
					//need mod id
					//Log.v(Constants.LOG_TAG, "null mImage is:"+mTempBitmap);
					imageView.setImageResource(R.drawable.default_image);
					MyUtils.recycleBitmap(curBitmap);
					InputStream is = PicViewer.this.getResources().openRawResource(R.drawable.default_image);
					curBitmap =BitmapFactory.decodeStream(is);
				}else{//下载图片成功
					Log.v(Constants.LOG_TAG, "mImage is:"+mTempBitmap);
					imageView.setImageBitmap(mTempBitmap);//设置将要展示的图片
					MyUtils.recycleBitmap(curBitmap);//回收当前的图片
					curBitmap=mTempBitmap;//将最新图片设置为当前图片变量，以便展示下一张图片时进行回收
				}
				//在标题栏中显示本张图片信息
				PicViewer.this.setTitle(mBundle.getString("curPicInfo"));
				//获取动画并设置
				Integer showMode= ((Integer) mBundle.get("showmode"));
				if(Constants.SHOW_LAST==showMode){
					imageView.startAnimation(PicViewer.this.mTransFromLeft);
				}else if(Constants.SHOW_NEXT==showMode){
					imageView.startAnimation(PicViewer.this.mTransFromRight);
				}
			}else if(msg.what == 2){
				//构造下载进度提示语
				//String downMsg="正在下载，请稍候";
				String downProgress="";
				int intProgress=msg.arg1%7;//loading最多7个点，取模计算展示的点数
				for(int i=0; i<=intProgress;i++){
					downProgress=downProgress+".";
				}
				//loading最多7个点，不足部分补充空格
				for(int i=0;i<(7-intProgress);i++){
					downProgress=downProgress+" ";
				}
				
				if (mBarDialog.isShowing()) {
					//mBarDialog.setProgress(msg.arg1);
					mBarDialog.setMessage(PicViewer.this.getResources().getString(R.string.downloading_tip)+downProgress);	//更新进度
				}
			}else if(msg.what == 3){
				MyUtils.showPointLackDlg(PicViewer.this,PicViewer.this.myPoints);
			}
			super.handleMessage(msg);
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}

		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			return super.equals(o);
		}

		@Override
		protected void finalize() throws Throwable {
			// TODO Auto-generated method stub
			super.finalize();
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}

	}

	class SaveImageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (false == mBarDialog.isShowing()) {
				mBarDialog.setMessage(PicViewer.this.getResources().getString(R.string.pic_storing)+"");
				mBarDialog.show(); 
			}
			new SaveImageThread().start();
		}
		
	}
	
	class AppRecommendListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 显示推荐应用列表（Offer）.
			PicViewer.this.myPoints.showGetPointDlg();
			//AppConnect.getInstance(PicViewer.this).showOffers(PicViewer.this);
		}
	}
	
	class CommentsListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//for 万普广告 ----用户反馈
			PicViewer.this.myPoints.showFeedback();
			//if(Constants.appInfo.getIsAddAds()){
			//AppConnect.getInstance(PicViewer.this).showFeedback();
			//}
		}
	}
	class ShowNextListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			PicViewer.this.showNextImage();
		}

	}

	class ShowLastListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			PicViewer.this.showLastImage();
		}

	}

	class SetupBitmapListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			Bitmap mBitmap = PicViewer.this.curBitmap;
			MyUtils.setBitmap(PicViewer.this, mBitmap);
		}
	}


	
	/*
	 * 定义保存图片的线程
	 */
	private class SaveImageThread extends Thread {
		//String strUrl = null;
		String localFileName = null;// 存放原文件名
		File fileCreated = null;

		public void init() {
/*			this.strUrl = PicViewer.this.getStrPreUrl() + "s"
					+ (PicViewer.this.getIntPicPositon() + 1) + ".jpg";
*/			localFileName = "s" + (PicViewer.this.getIntPicPositon() + 1)
					+ ".jpg";
		}

		@Override
		public void run() {
			init();
			try {
				fileCreated = FileUtils.saveImageToLocal(localFileName,
						PicViewer.this.curBitmap);
			} catch (IOException e) {
				//e.printStackTrace();
				Log.v(Constants.LOG_TAG, ""+e.toString());
			}
			if (null != fileCreated) {
				Message mAlertMessage = myHander.obtainMessage(0);
				myHander.sendMessage(mAlertMessage);
			}
		}
	}
	
    /** 
     * 加载内存卡图片 
     */  	
    public Bitmap loadImageFromSD(String path) {  
//            Options options=new Options();  
//            options.inSampleSize=2; 
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            FileUtils.updateFileTime(path);
            return bitmap;  
    }  

	/*
	 * //这里来监听屏幕触控时间
	 */
	// @Override
	public boolean onTouchEvent(MotionEvent event) {

		// 判定用户是否触摸到了图片如果是单点触摸则调用控制图片移动的方法 如果是2点触控则调用控制图片大小的方法
		if (event.getY() > imageView.getTop()
				&& event.getY() < imageView.getBottom()
				&& event.getX() > imageView.getLeft()
				&& event.getX() < imageView.getRight()) {
			if (event.getPointerCount() == 2) {
				imageView.scaleWithFinger(event);
			}else if (event.getPointerCount() == 1) {
				if (0 != imageView.getLeft() || 0 != imageView.getTop()
						|| imageView.getRight() > imageView.getWidth()
						|| imageView.getBottom() > imageView.getHeight()){
					imageView.moveWithFinger(event);
				}
				return this.mygesture.onTouchEvent(event);
			}
		}
		return true;
	}

	public void showImage(Integer intPosition, Integer showMode) {
		
		if(this.myPoints.getPoints()<=0){
			Message mAlertMessage = myHander.obtainMessage(3);
			myHander.sendMessage(mAlertMessage);
		}else{
			this.myPoints.subPoints(1);
		}		
//		this.myPoints.subPoints(1);
//		this.myOffer.refreshPoints();
//		if(this.myOffer.getPoints()<=0){
//			Message mAlertMessage = myHander.obtainMessage(3);
//			myHander.sendMessage(mAlertMessage);
//		}
		
//		AppConnect.getInstance(this).spendPoints(1, this);
//		AppConnect.getInstance(this).getPoints(this);
		//String strUrl = this.getStrPreUrl() + "s" + (intPosition + 1) + ".jpg";
		//String strUrl=HttpDownloader.getResFromServer(Constants.REQ_SERV+"?"+Constants.urlParams.getParams());
		String strUrl=Constants.REQ_SERV+"?"+Constants.urlParams.getImageParam(getAlbumInfo().getId(),intPosition);
		//String strUrl = this. + "s" + (intPosition + 1) + ".jpg";
		String imageFullPath = Constants.LOCAL_PATH
				+ this.getAlbumInfo().getId() + "/s" + intPosition
				+ Constants.CACHE_SUFFIX;
		String mDir = Constants.LOCAL_PATH + this.getAlbumInfo().getId()
				+ "/";
		loadImage(getAlbumInfo().getmAlbumName(), strUrl, mDir, imageFullPath,
				showMode, intPosition + 1,
				Integer.parseInt(this.albumInfo.getPicAmount()));
	}

	// /==============================================================
	// 构建手势探测器

	// 显示下一张图片
	public void showNextImage() {
		Integer intPosition = this.getIntPicPositon() + 1;

		// 循环显示，若当前页为最后页，则下一步需要显示第一页
		if (intPosition >= Integer.parseInt(this.getAlbumInfo().getPicAmount())) {
			this.setIntPicPositon(0);
			//Toast.makeText(this, "已到最后一张！", Toast.LENGTH_SHORT).show();
			this.showImage(this.getIntPicPositon(),Constants.SHOW_NEXT);
			//this.showImage(this.getIntPicPositon(),mTransFromRight);

		} else {
			this.showImage(intPosition,Constants.SHOW_NEXT);
			this.setIntPicPositon(intPosition);
		}
		
		//test free mem
/*		String strUrl = this.getStrPreUrl() + "s" + (intPosition-1) + ".jpg";
		loader.freeImage(strUrl);*/
	}

	// 显示上一张图片
	public void showLastImage() {
		Integer intPosition = this.getIntPicPositon() - 1;
		if (intPosition < 0) {
			// 循环显示，若当前页为第一页，则下一步需要显示最后一页
			this.setIntPicPositon(Integer.parseInt(this.getAlbumInfo()
					.getPicAmount()) - 1);
			//Toast.makeText(this, "已返回第一张！", Toast.LENGTH_SHORT).show();
			this.showImage(this.getIntPicPositon(),Constants.SHOW_LAST);
		} else {
			this.setIntPicPositon(intPosition);
			this.showImage(this.getIntPicPositon(),Constants.SHOW_LAST);
		}
	}

	// 这里来监听屏幕触控时间

	/*
	 * 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
	 */
	// 主要方法
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度（像素/秒）
		// velocityY：Y轴上的移动速度（像素/秒）

		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
		// 向有翻图片

		if (e1.getX() - e2.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			showNextImage();
		}
		// 向左翻图片
		if (e2.getX() - e1.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			showLastImage();
		}

		return false;
	}

	// 下面方法没用，但是这里必须实现
	/* 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发 */
	@Override
	public void onLongPress(MotionEvent e) {

	}

	/* 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/*
	 * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
	 * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	 */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	/* 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
			return false;
		} else {
			popupWindow.showAtLocation(imageView, Gravity.BOTTOM, 0, popWinLocHig);
		}
		// this.showPicUtilMenu();
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (null != this.popupWindow && this.popupWindow.isShowing()) {
				this.popupWindow.dismiss();
			}

			if (null != this.mBarDialog && this.mBarDialog.isShowing()) {
				this.mBarDialog.dismiss();
			}

			//结束此acivity
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 
	 */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_MENU) {
    		//startActivity(new Intent(this, translucentButton.class));
    		//overridePendingTransition(R.anim.menu_fade, R.anim.menu_hold);
    	}
    	return super.onKeyUp(keyCode, event);
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* 
         * add()方法的四个参数，依次是：
         *  1、组别，如果不分组的话就写Menu.NONE,
         2、Id，这个很重要，Android根据这个Id来确定不同的菜单
         * 3、顺序，那个菜单现在在前面由这个参数的大小决定
         * 4、文本，菜单的显示文本
         */
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, PicViewer.this.getResources().getString(R.string.get_points)).setIcon(
        android.R.drawable.ic_menu_share);
        // setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

        // android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, PicViewer.this.getResources().getString(R.string.del_cache)).setIcon(
        android.R.drawable.ic_menu_delete);
        menu.add(Menu.NONE, Menu.FIRST + 3, 3, PicViewer.this.getResources().getString(R.string.suggestion)).setIcon(
        android.R.drawable.ic_menu_edit);
/*        menu.add(Menu.NONE, Menu.FIRST + 5, 4, "关于").setIcon(
        android.R.drawable.ic_menu_info_details);*/

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
			FileUtils.removeCache(Constants.LOCAL_PATH,(float) 1);
    		Toast.makeText(PicViewer.this, PicViewer.this.getResources().getString(R.string.del_cache_res), Toast.LENGTH_SHORT).show();       	
        	break;
        case Menu.FIRST + 3:
        	myPoints.showFeedback();
			//AppConnect.getInstance(this).showFeedback();
            break;
        }
        return false;
    }

    
	// 实现图片的异步加载
    /**
     * 
     * @param albumName:专辑名称
     * @param imageUrl：获取图片的URL
     * @param mDir：图片缓存到本地的目录
     * @param imageFullPath：图片在本地保存的全路径
     * @param showMode：显示模式，下一张还是上一张
     * @param curPos：标识第几张图片，显示在标题栏中
     * @param picsAmount：本专辑图片的总数量
     */
	public void loadImage(final String albumName,final String imageUrl, final String mDir,
			final String imageFullPath, final Integer showMode, final Integer curPos,
			final Integer picsAmount) {
		//将传入的路径修改为本地cache的路径
		//final String strCacheFilePath= FileUtils.strReplace(imagePath,".jpg",Constants.CACHE_SUFFIX);
		// 查询本地目录，查看当前需要下载的图片是否已经存在于本地目录当中
		// 新开辟一个线程，该线程用于进行图片的下载
		if (false == mBarDialog.isShowing()
				&& false == FileUtils.isFileExist(imageFullPath)) {
			mBarDialog.setMessage(this.getResources().getString(R.string.downloading_tip)+"");			
			mBarDialog.show();
		}   	        	
		new Thread() {
			public void run() {
				Bundle bundle = new Bundle();
				Bitmap mImage = null;
				// boolean needSave=true;
				boolean isNeedSave = false;
				if (FileUtils.isFileExist(imageFullPath)) {
					mImage = loadImageFromSD(imageFullPath);
					if (null == mImage) {
						mImage = HttpDownloader.loadImageFromUrl(imageUrl,myHander);
						isNeedSave = true;
					}
				} else {
					mImage = HttpDownloader.loadImageFromUrl(imageUrl,myHander);
					isNeedSave = true;
				}
				bundle.putParcelable("bitmap", mImage);//设置图片
				bundle.putInt("showmode", showMode);// 定义动画方向，0为无动画，1为上一张方向，2为下一张方向
				//设置本张图片信息，以便在标题栏中显示
				String curPicInfo=curPos+"/"+picsAmount+"   "+albumName;
				bundle.putString("curPicInfo", curPicInfo);

				
				//将下载的图片存储到本地
				if (isNeedSave && null != mImage) {
					try {
						FileUtils.autoSaveImage(mDir, imageFullPath, mImage);
					} catch (IOException e) {
						Log.v(Constants.LOG_TAG, ""+e.toString());
					}
				}
				Message message = myHander.obtainMessage(1, bundle);
				myHander.sendMessage(message);	

			};
		}.start();
		
		//如果剩余空间小于指定的值，则根据缓存图片的最后修改时间进行删除操作
		if (Constants.SD_INSERTED && false == FileUtils.isFileExist(imageFullPath)
				&& Constants.SD_FREE_SIZE > FileUtils.freeSpaceOnSd()
				&& !PicViewer.isDeleting) {
			new Thread(new AutoRemoveCatch()).start();
/*			new Thread() {
				public void run() {
					PicViewer.isDeleting = true;
					FileUtils.removeCache(Constants.LOCAL_PATH,(float) 0.4);
					PicViewer.isDeleting = false;
				}
			}.start();*/
		}
	}

	// 发消息线程
	class AutoRemoveCatch implements Runnable {
		public AutoRemoveCatch() {
		}

		@Override
		public void run() {
			PicViewer.isDeleting = true;
			FileUtils.removeCache(Constants.LOCAL_PATH,(float) 0.4);
			PicViewer.isDeleting = false;
		}
	}
	
/*	void autoPlay(){
		isStop = false;
		if (!isRunning) {
			isRunning = true;
			if (false == mBarDialog.isShowing()
					
					&& false == FileUtils.isFileExist(strCacheFilePath)) {
				mBarDialog.setMessage("正在下载，请稍候... 0%");			
				mBarDialog.show();
			}
			while (count <= psu.count() && !isStop) {
				handler.sendEmptyMessage(0);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
			}
		}

	}
*/	// 启动自动播放
/*	private void autoPlay() {
		isStop = false;
		new Thread(new AutoPlay()).start();
	}

	// 发消息线程
	class AutoPlay implements Runnable {
		private int count = 0;

		public AutoPlay() {
		}

		@Override
		public void run() {
			if (!isRunning) {
				isRunning = true;
				while (count <= psu.count() && !isStop) {
					handler.sendEmptyMessage(0);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
				}
			}

		}

	}
	
	class AutoPlayListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (false == isRunning) {
				btAutoPlay.setImageResource(R.drawable.pause);
				autoPlay();
			} else {
				isRunning = false;
				isStop = true;
				btAutoPlay.setImageResource(R.drawable.auto_play);
				// imageView.setImageBitmap(psu.getImageAt(currentNum));
				loadImageFromSD(currentNum, imageView, null);

			}

		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isRunning) {
				isRunning = false;
				isStop = true;
				this.btAutoPlay.setImageResource(R.drawable.auto_play);
				// imageView.setImageBitmap(psu.getImageAt(currentNum));
				loadImageFromSD(currentNum, imageView, null);
//				AnimationDrawable temp=(AnimationDrawable)imageView.getBackground();
//				temp.stop();
			}
			break;
		}
		*/
		
	// ========================

/*	// 该方法用于根据图片的URL，从网络上下载图片
	protected Bitmap loadImageFromUrl(String imageUrl) {
		int fileSize = 0;
		int downSize = 0;
		int progress = 0;//用于标识下载进度框中的进度，使用.的个数标识
		try {
			// 根据图片的URL，下载图片，并生成一个Bitmap对象
			Bitmap bitmap = null;
			InputStream inStream = null;
			ByteArrayOutputStream outStream = null;
			try {

				HttpEntity entity = HttpDownloader.getEntity(imageUrl);
				// 健壮性处理，获取链接失败，返回null
				if(null==entity){
					return null;
				}
				inStream = entity.getContent();

				fileSize = (int) entity.getContentLength();
				// 健壮性处理，读取文件失败，返回null
				if (null == inStream) {
					return null;
				}
				try {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					//int progress=0;//用于标识下载进度框中的进度，使用。标识
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						downSize += len;

						//int num = (int) ((double) downSize / (double) fileSize * 100);

					//	if (num > progress + 1) {
							progress++;//进度加一
							//progress = num;
							Message message = myHander.obtainMessage();
							message.what = 2;
							message.arg1 = progress;
							myHander.sendMessage(message);
					//	}
					}
					outStream.flush();
					outStream.close();
					inStream.close();

					byte[] data = outStream.toByteArray();
					if (data != null) {
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
					}
					Log.v(Constants.LOG_TAG, "recived size is:"+data.length);

				} catch (Exception e) {
					Log.v(Constants.LOG_TAG, "" + e);
					return null;
				}

			} finally {
				try {
					if (null != outStream)
						outStream.close();
					if (null != inStream)
						inStream.close();
				} catch (Exception e) {
					Log.v(Constants.LOG_TAG, "" + e);
					return null;
				}
			}
			return bitmap;
		} catch (Exception e) {
			//throw new RuntimeException(e);
			Log.v(Constants.LOG_TAG, "" + e);
			return null;
		}
	}
*/	
	
//====================================
	
	public AlbumInfo getAlbumInfo() {
		return albumInfo;
	}

	public void setAlbumInfo(AlbumInfo albumInfo) {
		this.albumInfo = albumInfo;
	}

//	public String getStrPreUrl() {
//		return strPreUrl;
//	}
//
//	public void setStrPreUrl(String strPreUrl) {
//		this.strPreUrl = strPreUrl;
//	}

	public Integer getIntPicPositon() {
		return intPicPositon;
	}

	public void setIntPicPositon(Integer intPicPositon) {
		this.intPicPositon = intPicPositon;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}



//	@Override
//	public void getUpdatePoints(String currencyName, int pointTotal) {
//		// TODO Auto-generated method stub
//		MyUtils.outLog("===========pointTotal is:"+pointTotal);
//		if(pointTotal==0){
//			Message mAlertMessage = myHander.obtainMessage(3);
//			myHander.sendMessage(mAlertMessage);
//		}
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


}
