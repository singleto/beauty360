package com.android.picview;

import java.io.InputStream;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.info.ImageInfo;
import com.android.beauty360.R;
import com.android.beauty360.point.MyPoints;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.*;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
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


public class LocalPicViewer extends Activity implements OnGestureListener,
		AdsMogoListener {

	/*
	 * 利用多点触控来控制ImageView中图像的放大与缩小 手指控制图片移动
	 */

	private MyImageView imageView;
	private Integer intPicPositon;
	public static final int NEW_NAME1 = 1;
	public static final int NEW_NUM1 = 2;

	DownloadInfo downInfo = null;
	private GestureDetector mygesture = null;
	// 初始化Bar所用到的变量
	LayoutInflater mInflater = null;
	View mLayout = null;
	private PopupWindow popupWindow;
	private Integer popWinLocHig;
	private ProgressDialog mBarDialog = null;
	// =============================
	// 定义图片切换动画
	TranslateAnimation mTransFromRight = null;
	TranslateAnimation mTransFromLeft = null;

	// alter dialog
	AlertDialog.Builder mAlertDialogBuilder = null;

	// For recycle
	Bitmap lastBitmap = null;
	Bitmap curBitmap = null;

	// 标识是否正在删除缓存
	static boolean isDeleting = false;

	// 自动播放标识
	boolean isRunning = true;
	boolean isStop = false;

	// 芒果广告
	AdsMogoLayout adsMogoLayoutCode;
	MyPoints myPoints;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_viewer);
		mAlertDialogBuilder = new AlertDialog.Builder(this);
		// Log.v(Constants.LOG_TAG, "enter pic view.onCreate().");
		initVar();
		initPopuptWindow();
		// 设置广告
		// if(Constants.appInfo.getIsAddAds()){
		addMogoAdView();
		// }
		// 设置背景
		imageView.setBackgroundColor(getResources().getColor(R.color.cream));
		this.showImage(this.getIntPicPositon(), Constants.NORMAL);
	}

	// 设置广告
	void addMogoAdView() {

		// 构造方法，设置快速模式
		adsMogoLayoutCode = new AdsMogoLayout(this, Constants.MOGO_ID);
		this.myPoints=new MyPoints(this,null);
		/*------------------------------------------------------------*/
		// 通过Code方式添加广告条 本例的结构如下(仅供参考)
		// -RelativeLayout/(FILL_PARENT,FILL_PARENT)
		// |
		// +RelativeLayout/(FILL_PARENT,WRAP_CONTENT)
		// |
		// +AdsMogoLayout(FILL_PARENT,WRAP_CONTENT)
		// |
		// \
		// |
		// \
		/*------------------------------------------------------------*/
		/*
		 * RelativeLayout parentLayput = new RelativeLayout(this);
		 * RelativeLayout.LayoutParams parentLayputParams = new
		 * RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT,
		 * RelativeLayout.LayoutParams.FILL_PARENT); RelativeLayout.LayoutParams
		 * layoutParams = new RelativeLayout.LayoutParams(
		 * RelativeLayout.LayoutParams.FILL_PARENT,
		 * RelativeLayout.LayoutParams.WRAP_CONTENT);
		 * layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
		 * RelativeLayout.TRUE); parentLayput.addView(adsMogoLayoutCode,
		 * layoutParams);
		 * 
		 * this.addContentView(parentLayput, parentLayputParams);
		 */

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

		// loader = new AsyncImageLoader();
		mygesture = new GestureDetector(this);
		this.setIntPicPositon(0);
		// 初始化进度对话框
		mBarDialog = new ProgressDialog(LocalPicViewer.this);
		mBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 获取上一个activity传入的专辑信息

		String albumId = getIntent().getStringExtra(Constants.ALBUM_ID);
		downInfo = Constants.hashMapDownloadInfo.get(albumId);
		//downInfo.getImageInfoFromLocal();

		// 显示下一张图片的动画设置
		mTransFromRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mTransFromRight.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator));
		mTransFromRight.setDuration(500);
		// 显示上一张图片的动画设置
		mTransFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mTransFromLeft.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator));
		mTransFromLeft.setDuration(500);
		// set picture's pre url
		// this.setStrPreUrl(Constants.PRE_URL + albumInfo.getId() + "/");
		// for auto play

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
		txStoreImage.setVisibility(View.GONE);
		btStoreImage.setVisibility(View.GONE);

		ImageButton btSetupBitmap = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_setup_bitmap);
		TextView txSetupBitmap = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_setup_bitmap);

		/*
		 * ImageButton btAppRecommendImage = (ImageButton) popupWindow_view
		 * .findViewById(R.id.bt_app_recommend); // pic_util_popwin.xml视图里面的控件
		 * TextView tvAppRecommend = (TextView) popupWindow_view
		 * .findViewById(R.id.net_tx_app_recommend);
		 * 
		 * ImageButton btCommentsImage = (ImageButton) popupWindow_view
		 * .findViewById(R.id.bt_comments); TextView tvComments = (TextView)
		 * popupWindow_view .findViewById(R.id.net_tx_comments);
		 */

		ImageButton btLastImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_last_pic); // pic_util_popwin.xml视图里面的控件
		TextView txLastImage = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_last);

		ImageButton btNextImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_next_pic);
		TextView txNextImage = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_next);
		popWinLocHig = (Integer) (LocalPicViewer.this.getWindowManager()
				.getDefaultDisplay().getHeight() / 4);

		btLastImage.setOnClickListener(new ShowLastListener());
		txLastImage.setOnClickListener(new ShowLastListener());
		btNextImage.setOnClickListener(new ShowNextListener());
		txNextImage.setOnClickListener(new ShowNextListener());

		/*
		 * btAppRecommendImage.setOnClickListener(new AppRecommendListener());
		 * tvAppRecommend.setOnClickListener(new AppRecommendListener());
		 * btCommentsImage.setOnClickListener(new CommentsListener());
		 * tvComments.setOnClickListener(new CommentsListener());
		 */
		btSetupBitmap.setOnClickListener(new SetupBitmapListener());
		txSetupBitmap.setOnClickListener(new SetupBitmapListener());
	}

	class AppRecommendListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 显示推荐应用列表（Offer）.
			myPoints.showGetPointDlg();
//			AppConnect.getInstance(LocalPicViewer.this).showOffers(
//					LocalPicViewer.this);
		}
	}

	class CommentsListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// for 万普广告 ----用户反馈
			myPoints.showFeedback();
			// if(Constants.appInfo.getIsAddAds()){
			//AppConnect.getInstance(LocalPicViewer.this).showFeedback();
			// }
		}
	}

	class ShowNextListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LocalPicViewer.this.showNextImage();
		}

	}

	class ShowLastListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LocalPicViewer.this.showLastImage();
		}

	}

	class SetupBitmapListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			Bitmap mBitmap = LocalPicViewer.this.curBitmap;
			MyUtils.setBitmap(LocalPicViewer.this, mBitmap);
		}
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
			} else if (event.getPointerCount() == 1) {
				if (0 != imageView.getLeft() || 0 != imageView.getTop()
						|| imageView.getRight() > imageView.getWidth()
						|| imageView.getBottom() > imageView.getHeight()) {
					imageView.moveWithFinger(event);
				}
				return this.mygesture.onTouchEvent(event);
			}
		}
		return true;
	}

	public void showImage(Integer intPosition, Integer showMode) {

		ImageInfo imageInfo = this.downInfo.getArrImageInfo().get(intPosition);
		String imageFullPath = Constants.DOWNLOAD_PATH + imageInfo.getAlbumId()
				+ "/" + imageInfo.getImgName();

		Bitmap mTempBitmap = BitmapFactory.decodeFile(imageFullPath);
		FileUtils.updateFileTime(imageFullPath);

		if (null == mTempBitmap) { // 加载图片失败
			// need mod id
			imageView.setImageResource(R.drawable.default_image);
			MyUtils.recycleBitmap(curBitmap);
			InputStream is = LocalPicViewer.this.getResources()
					.openRawResource(R.drawable.default_image);
			curBitmap = BitmapFactory.decodeStream(is);
		} else {// 加载图片成功
			MyUtils.outLog("mImage is:" + mTempBitmap);
			imageView.setImageBitmap(mTempBitmap);// 设置将要展示的图片
			MyUtils.recycleBitmap(curBitmap);// 回收当前的图片
			curBitmap = mTempBitmap;// 将最新图片设置为当前图片变量，以便展示下一张图片时进行回收
		}

		String curPicInfo = (intPosition + 1) + "/"
				+ this.downInfo.getArrImageInfo().size() + "   "
				+ this.downInfo.getmAlbumName();
		// 在标题栏中显示本张图片信息
		LocalPicViewer.this.setTitle(curPicInfo);
		// 获取动画并设置
		if (Constants.SHOW_LAST == showMode) {
			imageView.startAnimation(LocalPicViewer.this.mTransFromLeft);
		} else if (Constants.SHOW_NEXT == showMode) {
			imageView.startAnimation(LocalPicViewer.this.mTransFromRight);
		}

	}

	// /==============================================================
	// 构建手势探测器

	// 显示下一张图片
	public void showNextImage() {
		Integer intPosition = this.getIntPicPositon() + 1;

		// 循环显示，若当前页为最后页，则下一步需要显示第一页
		if (intPosition >= this.downInfo.getArrImageInfo().size()) {
			this.setIntPicPositon(0);
			// Toast.makeText(this, "已到最后一张！", Toast.LENGTH_SHORT).show();
			this.showImage(this.getIntPicPositon(), Constants.SHOW_NEXT);
			// this.showImage(this.getIntPicPositon(),mTransFromRight);

		} else {
			this.showImage(intPosition, Constants.SHOW_NEXT);
			this.setIntPicPositon(intPosition);
		}

		// test free mem
		/*
		 * String strUrl = this.getStrPreUrl() + "s" + (intPosition-1) + ".jpg";
		 * loader.freeImage(strUrl);
		 */
	}

	// 显示上一张图片
	public void showLastImage() {
		Integer intPosition = this.getIntPicPositon() - 1;
		if (intPosition < 0) {
			// 循环显示，若当前页为第一页，则下一步需要显示最后一页
			this.setIntPicPositon(this.downInfo.getArrImageInfo().size() - 1);
			// Toast.makeText(this, "已返回第一张！", Toast.LENGTH_SHORT).show();
			this.showImage(this.getIntPicPositon(), Constants.SHOW_LAST);
		} else {
			this.setIntPicPositon(intPosition);
			this.showImage(this.getIntPicPositon(), Constants.SHOW_LAST);
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
			popupWindow.showAtLocation(imageView, Gravity.BOTTOM, 0,
					popWinLocHig);
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

			// 结束此acivity
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// startActivity(new Intent(this, translucentButton.class));
			// overridePendingTransition(R.anim.menu_fade, R.anim.menu_hold);
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
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, this.getResources().getString(R.string.get_points)).setIcon(
				android.R.drawable.ic_menu_share);
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以

		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, this.getResources().getString(R.string.del_cache)).setIcon(
				android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3, this.getResources().getString(R.string.suggestion)).setIcon(
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
			Toast.makeText(LocalPicViewer.this, this.getResources().getString(R.string.del_cache_res),
					Toast.LENGTH_SHORT).show();
			break;
		case Menu.FIRST + 3:
			myPoints.showFeedback();
			//AppConnect.getInstance(this).showFeedback();
			break;
		}
		return false;
	}

	// 发消息线程
	class AutoRemoveCatch implements Runnable {
		public AutoRemoveCatch() {
		}

		@Override
		public void run() {
			LocalPicViewer.isDeleting = true;
			FileUtils.removeCache(Constants.LOCAL_PATH, (float) 0.4);
			LocalPicViewer.isDeleting = false;
		}
	}

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
