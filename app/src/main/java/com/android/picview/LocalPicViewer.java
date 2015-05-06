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
	 * ���ö�㴥��������ImageView��ͼ��ķŴ�����С ��ָ����ͼƬ�ƶ�
	 */

	private MyImageView imageView;
	private Integer intPicPositon;
	public static final int NEW_NAME1 = 1;
	public static final int NEW_NUM1 = 2;

	DownloadInfo downInfo = null;
	private GestureDetector mygesture = null;
	// ��ʼ��Bar���õ��ı���
	LayoutInflater mInflater = null;
	View mLayout = null;
	private PopupWindow popupWindow;
	private Integer popWinLocHig;
	private ProgressDialog mBarDialog = null;
	// =============================
	// ����ͼƬ�л�����
	TranslateAnimation mTransFromRight = null;
	TranslateAnimation mTransFromLeft = null;

	// alter dialog
	AlertDialog.Builder mAlertDialogBuilder = null;

	// For recycle
	Bitmap lastBitmap = null;
	Bitmap curBitmap = null;

	// ��ʶ�Ƿ�����ɾ������
	static boolean isDeleting = false;

	// �Զ����ű�ʶ
	boolean isRunning = true;
	boolean isStop = false;

	// â�����
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
		// ���ù��
		// if(Constants.appInfo.getIsAddAds()){
		addMogoAdView();
		// }
		// ���ñ���
		imageView.setBackgroundColor(getResources().getColor(R.color.cream));
		this.showImage(this.getIntPicPositon(), Constants.NORMAL);
	}

	// ���ù��
	void addMogoAdView() {

		// ���췽�������ÿ���ģʽ
		adsMogoLayoutCode = new AdsMogoLayout(this, Constants.MOGO_ID);
		this.myPoints=new MyPoints(this,null);
		/*------------------------------------------------------------*/
		// ͨ��Code��ʽ��ӹ���� �����Ľṹ����(�����ο�)
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
		// ���ù����ֵ�λ��(�����ڵײ�)
		params.bottomMargin = 0;
		// ��ӹ��״̬����
		// ���ü����ص� ���а��� ���� չʾ ����ʧ�ܵ��¼��Ļص�
		adsMogoLayoutCode.setAdsMogoListener(this);
		params.gravity = Gravity.BOTTOM;
		addContentView(adsMogoLayoutCode, params);
	}

	/**
	 * ��ʼ������
	 */
	public void initVar() {
		// imageView = new MyImageView();
		// start����imageView������
		imageView = (MyImageView) this.findViewById(R.id.myImageView);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setAdjustViewBounds(true);
		// end

		// loader = new AsyncImageLoader();
		mygesture = new GestureDetector(this);
		this.setIntPicPositon(0);
		// ��ʼ�����ȶԻ���
		mBarDialog = new ProgressDialog(LocalPicViewer.this);
		mBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// ��ȡ��һ��activity�����ר����Ϣ

		String albumId = getIntent().getStringExtra(Constants.ALBUM_ID);
		downInfo = Constants.hashMapDownloadInfo.get(albumId);
		//downInfo.getImageInfoFromLocal();

		// ��ʾ��һ��ͼƬ�Ķ�������
		mTransFromRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mTransFromRight.setInterpolator(AnimationUtils.loadInterpolator(this,
				android.R.anim.accelerate_decelerate_interpolator));
		mTransFromRight.setDuration(500);
		// ��ʾ��һ��ͼƬ�Ķ�������
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
	 * * ����PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub

		View popupWindow_view = getLayoutInflater().inflate( // ��ȡ�Զ��岼���ļ�pic_util_popwin.xml����ͼ
				R.layout.popwin_netpic, null, false);

		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);// ����PopupWindowʵ��
		// ���ö���Ч��
		popupWindow.setAnimationStyle(R.style.PopupAnimation);

		ImageButton btStoreImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_store_image); // pic_util_popwin.xml��ͼ����Ŀؼ�
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
		 * .findViewById(R.id.bt_app_recommend); // pic_util_popwin.xml��ͼ����Ŀؼ�
		 * TextView tvAppRecommend = (TextView) popupWindow_view
		 * .findViewById(R.id.net_tx_app_recommend);
		 * 
		 * ImageButton btCommentsImage = (ImageButton) popupWindow_view
		 * .findViewById(R.id.bt_comments); TextView tvComments = (TextView)
		 * popupWindow_view .findViewById(R.id.net_tx_comments);
		 */

		ImageButton btLastImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_last_pic); // pic_util_popwin.xml��ͼ����Ŀؼ�
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
			// ��ʾ�Ƽ�Ӧ���б�Offer��.
			myPoints.showGetPointDlg();
//			AppConnect.getInstance(LocalPicViewer.this).showOffers(
//					LocalPicViewer.this);
		}
	}

	class CommentsListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// for ���չ�� ----�û�����
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
	 * //������������Ļ����ʱ��
	 */
	// @Override
	public boolean onTouchEvent(MotionEvent event) {

		// �ж��û��Ƿ�������ͼƬ����ǵ��㴥������ÿ���ͼƬ�ƶ��ķ��� �����2�㴥������ÿ���ͼƬ��С�ķ���
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

		if (null == mTempBitmap) { // ����ͼƬʧ��
			// need mod id
			imageView.setImageResource(R.drawable.default_image);
			MyUtils.recycleBitmap(curBitmap);
			InputStream is = LocalPicViewer.this.getResources()
					.openRawResource(R.drawable.default_image);
			curBitmap = BitmapFactory.decodeStream(is);
		} else {// ����ͼƬ�ɹ�
			MyUtils.outLog("mImage is:" + mTempBitmap);
			imageView.setImageBitmap(mTempBitmap);// ���ý�Ҫչʾ��ͼƬ
			MyUtils.recycleBitmap(curBitmap);// ���յ�ǰ��ͼƬ
			curBitmap = mTempBitmap;// ������ͼƬ����Ϊ��ǰͼƬ�������Ա�չʾ��һ��ͼƬʱ���л���
		}

		String curPicInfo = (intPosition + 1) + "/"
				+ this.downInfo.getArrImageInfo().size() + "   "
				+ this.downInfo.getmAlbumName();
		// �ڱ���������ʾ����ͼƬ��Ϣ
		LocalPicViewer.this.setTitle(curPicInfo);
		// ��ȡ����������
		if (Constants.SHOW_LAST == showMode) {
			imageView.startAnimation(LocalPicViewer.this.mTransFromLeft);
		} else if (Constants.SHOW_NEXT == showMode) {
			imageView.startAnimation(LocalPicViewer.this.mTransFromRight);
		}

	}

	// /==============================================================
	// ��������̽����

	// ��ʾ��һ��ͼƬ
	public void showNextImage() {
		Integer intPosition = this.getIntPicPositon() + 1;

		// ѭ����ʾ������ǰҳΪ���ҳ������һ����Ҫ��ʾ��һҳ
		if (intPosition >= this.downInfo.getArrImageInfo().size()) {
			this.setIntPicPositon(0);
			// Toast.makeText(this, "�ѵ����һ�ţ�", Toast.LENGTH_SHORT).show();
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

	// ��ʾ��һ��ͼƬ
	public void showLastImage() {
		Integer intPosition = this.getIntPicPositon() - 1;
		if (intPosition < 0) {
			// ѭ����ʾ������ǰҳΪ��һҳ������һ����Ҫ��ʾ���һҳ
			this.setIntPicPositon(this.downInfo.getArrImageInfo().size() - 1);
			// Toast.makeText(this, "�ѷ��ص�һ�ţ�", Toast.LENGTH_SHORT).show();
			this.showImage(this.getIntPicPositon(), Constants.SHOW_LAST);
		} else {
			this.setIntPicPositon(intPosition);
			this.showImage(this.getIntPicPositon(), Constants.SHOW_LAST);
		}
	}

	// ������������Ļ����ʱ��

	/*
	 * �û����´������������ƶ����ɿ�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE, 1��ACTION_UP����
	 */
	// ��Ҫ����
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// e1����1��ACTION_DOWN MotionEvent
		// e2�����һ��ACTION_MOVE MotionEvent
		// velocityX��X���ϵ��ƶ��ٶȣ�����/�룩
		// velocityY��Y���ϵ��ƶ��ٶȣ�����/�룩

		// X�������λ�ƴ���FLING_MIN_DISTANCE�����ƶ��ٶȴ���FLING_MIN_VELOCITY������/��
		// ���з�ͼƬ

		if (e1.getX() - e2.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			showNextImage();
		}
		// ����ͼƬ
		if (e2.getX() - e1.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			showLastImage();
		}

		return false;
	}

	// ���淽��û�ã������������ʵ��
	/* �û��������������ɶ��MotionEvent ACTION_DOWN���� */
	@Override
	public void onLongPress(MotionEvent e) {

	}

	/* �û����´����������϶�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE���� */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/*
	 * �û��ᴥ����������δ�ɿ����϶�����һ��1��MotionEvent ACTION_DOWN����
	 * ע���onDown()������ǿ������û���ɿ������϶���״̬
	 */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	/* �û����ᴥ���������ɿ�����һ��1��MotionEvent ACTION_UP���� */
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

			// ������acivity
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
		 * add()�������ĸ������������ǣ� 1��������������Ļ���дMenu.NONE,
		 * 2��Id���������Ҫ��Android�������Id��ȷ����ͬ�Ĳ˵� 3��˳���Ǹ��˵�������ǰ������������Ĵ�С����
		 * 4���ı����˵�����ʾ�ı�
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, this.getResources().getString(R.string.get_points)).setIcon(
				android.R.drawable.ic_menu_share);
		// setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ���ͼ�꣬ͬѧ������һ��,��

		// android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, this.getResources().getString(R.string.del_cache)).setIcon(
				android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3, this.getResources().getString(R.string.suggestion)).setIcon(
				android.R.drawable.ic_menu_edit);
		/*
		 * menu.add(Menu.NONE, Menu.FIRST + 5, 4, "����").setIcon(
		 * android.R.drawable.ic_menu_info_details);
		 */

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			// ��ʾ�Ƽ�Ӧ���б�Offer��.
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

	// ����Ϣ�߳�
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
