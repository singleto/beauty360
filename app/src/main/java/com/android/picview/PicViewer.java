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
	 * ���ö�㴥��������ImageView��ͼ��ķŴ�����С ��ָ����ͼƬ�ƶ�
	 */

	private MyImageView imageView;
	private Integer intPicPositon;
	public static final int NEW_NAME1 = 1;
	public static final int NEW_NUM1 = 2;

	private AlbumInfo albumInfo = null;
	//private String strPreUrl = null;
	private GestureDetector mygesture = null;
	private MyHandler myHander = new MyHandler();
	// ��ʼ��Bar���õ��ı���
	LayoutInflater mInflater = null;
	View mLayout = null;
	private PopupWindow popupWindow;
	private Integer popWinLocHig;
	private ProgressDialog mBarDialog = null;
	//String preDLMsg="�������أ����Ժ�";
	// =============================
	//����ͼƬ�л�����
	TranslateAnimation mTransFromRight=null;
	TranslateAnimation mTransFromLeft=null;
	
	// alter dialog
	AlertDialog.Builder mAlertDialogBuilder = null;

	//For recycle 
	Bitmap lastBitmap=null;
	Bitmap curBitmap= null;
	
	//��ʶ�Ƿ�����ɾ������
	static boolean  isDeleting=false;
	
	//�Զ����ű�ʶ	
	boolean isRunning=true;
	boolean isStop=false;
	
	//â�����
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
		//���ù��
		//if(Constants.appInfo.getIsAddAds()){
		addMogoAdView();
		
		//}
		// ���ñ���
		imageView.setBackgroundColor(getResources().getColor(R.color.cream));
		this.showImage(this.getIntPicPositon(),Constants.NORMAL);
	}
	
	//���ù��
	void addMogoAdView(){
		
		// ���췽�������ÿ���ģʽ
		adsMogoLayoutCode = new AdsMogoLayout(this,
				 Constants.MOGO_ID);



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
	

/*	void addDianRuAdView(){
		final AdSpace space = new AdSpace(this);
		space.setInterval(30);	//���ù��ˢ��ʱ��
		space.setType(0);		//���ù�����ͣ��˴���ֵΪ0
		space.setKeyword("game");//���ùؼ���
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// ���ù����ֵ�λ��(�����ڵײ�)
		params.bottomMargin = 0;
		// ��ӹ��״̬����
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
	 * ��ʼ������
	 */
	public void initVar() {
		// imageView = new MyImageView();
		// start����imageView������
		imageView = (MyImageView) this.findViewById(R.id.myImageView);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setAdjustViewBounds(true);
		// end

		//loader = new AsyncImageLoader();
		mygesture = new GestureDetector(this);
		this.setIntPicPositon(0);
		// ��ʼ�����ȶԻ���
		mBarDialog = new ProgressDialog(PicViewer.this);
		mBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// ��ȡ��һ��activity�����ר����Ϣ
//		albumInfo = (AlbumInfo) getIntent().getParcelableExtra(
//				Constants.ALBUM_INFO_KEY);
		albumInfo = MyUtils.getAlbumInfo(
				getIntent().getStringExtra(Constants.CATA_ID), getIntent()
						.getStringExtra(Constants.ALBUM_ID));
		// ��ʾ��һ��ͼƬ�Ķ�������
		mTransFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mTransFromRight.setInterpolator(AnimationUtils.loadInterpolator(this,
				 android.R.anim.accelerate_decelerate_interpolator));
		mTransFromRight.setDuration(500);
		// ��ʾ��һ��ͼƬ�Ķ�������
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
		ImageButton btSetupBitmap = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_setup_bitmap);
		TextView txSetupBitmap = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_setup_bitmap);

/*		ImageButton btAppRecommendImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_app_recommend); // pic_util_popwin.xml��ͼ����Ŀؼ�
		TextView tvAppRecommend = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_app_recommend);

		ImageButton btCommentsImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_comments);
		TextView tvComments = (TextView) popupWindow_view
				.findViewById(R.id.net_tx_comments);*/
		
		ImageButton btLastImage = (ImageButton) popupWindow_view
				.findViewById(R.id.bt_last_pic); // pic_util_popwin.xml��ͼ����Ŀؼ�
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
				//����������ɺ����ʾ��
				Toast toast = Toast.makeText(PicViewer.this,
						PicViewer.this.getResources().getString(R.string.downloaded_tip), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

/*				mAlertDialogBuilder
						.setMessage(Constants.DOWNLOADED_MSG)
						.setCancelable(false)
						.setPositiveButton("ȷ��",
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
				
				if(null== mTempBitmap){		//����ͼƬʧ��			
					//need mod id
					//Log.v(Constants.LOG_TAG, "null mImage is:"+mTempBitmap);
					imageView.setImageResource(R.drawable.default_image);
					MyUtils.recycleBitmap(curBitmap);
					InputStream is = PicViewer.this.getResources().openRawResource(R.drawable.default_image);
					curBitmap =BitmapFactory.decodeStream(is);
				}else{//����ͼƬ�ɹ�
					Log.v(Constants.LOG_TAG, "mImage is:"+mTempBitmap);
					imageView.setImageBitmap(mTempBitmap);//���ý�Ҫչʾ��ͼƬ
					MyUtils.recycleBitmap(curBitmap);//���յ�ǰ��ͼƬ
					curBitmap=mTempBitmap;//������ͼƬ����Ϊ��ǰͼƬ�������Ա�չʾ��һ��ͼƬʱ���л���
				}
				//�ڱ���������ʾ����ͼƬ��Ϣ
				PicViewer.this.setTitle(mBundle.getString("curPicInfo"));
				//��ȡ����������
				Integer showMode= ((Integer) mBundle.get("showmode"));
				if(Constants.SHOW_LAST==showMode){
					imageView.startAnimation(PicViewer.this.mTransFromLeft);
				}else if(Constants.SHOW_NEXT==showMode){
					imageView.startAnimation(PicViewer.this.mTransFromRight);
				}
			}else if(msg.what == 2){
				//�������ؽ�����ʾ��
				//String downMsg="�������أ����Ժ�";
				String downProgress="";
				int intProgress=msg.arg1%7;//loading���7���㣬ȡģ����չʾ�ĵ���
				for(int i=0; i<=intProgress;i++){
					downProgress=downProgress+".";
				}
				//loading���7���㣬���㲿�ֲ���ո�
				for(int i=0;i<(7-intProgress);i++){
					downProgress=downProgress+" ";
				}
				
				if (mBarDialog.isShowing()) {
					//mBarDialog.setProgress(msg.arg1);
					mBarDialog.setMessage(PicViewer.this.getResources().getString(R.string.downloading_tip)+downProgress);	//���½���
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
			// ��ʾ�Ƽ�Ӧ���б�Offer��.
			PicViewer.this.myPoints.showGetPointDlg();
			//AppConnect.getInstance(PicViewer.this).showOffers(PicViewer.this);
		}
	}
	
	class CommentsListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//for ���չ�� ----�û�����
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
	 * ���屣��ͼƬ���߳�
	 */
	private class SaveImageThread extends Thread {
		//String strUrl = null;
		String localFileName = null;// ���ԭ�ļ���
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
     * �����ڴ濨ͼƬ 
     */  	
    public Bitmap loadImageFromSD(String path) {  
//            Options options=new Options();  
//            options.inSampleSize=2; 
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            FileUtils.updateFileTime(path);
            return bitmap;  
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
	// ��������̽����

	// ��ʾ��һ��ͼƬ
	public void showNextImage() {
		Integer intPosition = this.getIntPicPositon() + 1;

		// ѭ����ʾ������ǰҳΪ���ҳ������һ����Ҫ��ʾ��һҳ
		if (intPosition >= Integer.parseInt(this.getAlbumInfo().getPicAmount())) {
			this.setIntPicPositon(0);
			//Toast.makeText(this, "�ѵ����һ�ţ�", Toast.LENGTH_SHORT).show();
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

	// ��ʾ��һ��ͼƬ
	public void showLastImage() {
		Integer intPosition = this.getIntPicPositon() - 1;
		if (intPosition < 0) {
			// ѭ����ʾ������ǰҳΪ��һҳ������һ����Ҫ��ʾ���һҳ
			this.setIntPicPositon(Integer.parseInt(this.getAlbumInfo()
					.getPicAmount()) - 1);
			//Toast.makeText(this, "�ѷ��ص�һ�ţ�", Toast.LENGTH_SHORT).show();
			this.showImage(this.getIntPicPositon(),Constants.SHOW_LAST);
		} else {
			this.setIntPicPositon(intPosition);
			this.showImage(this.getIntPicPositon(),Constants.SHOW_LAST);
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

			//������acivity
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
         * add()�������ĸ������������ǣ�
         *  1��������������Ļ���дMenu.NONE,
         2��Id���������Ҫ��Android�������Id��ȷ����ͬ�Ĳ˵�
         * 3��˳���Ǹ��˵�������ǰ������������Ĵ�С����
         * 4���ı����˵�����ʾ�ı�
         */
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, PicViewer.this.getResources().getString(R.string.get_points)).setIcon(
        android.R.drawable.ic_menu_share);
        // setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ���ͼ�꣬ͬѧ������һ��,��

        // android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, PicViewer.this.getResources().getString(R.string.del_cache)).setIcon(
        android.R.drawable.ic_menu_delete);
        menu.add(Menu.NONE, Menu.FIRST + 3, 3, PicViewer.this.getResources().getString(R.string.suggestion)).setIcon(
        android.R.drawable.ic_menu_edit);
/*        menu.add(Menu.NONE, Menu.FIRST + 5, 4, "����").setIcon(
        android.R.drawable.ic_menu_info_details);*/

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

    
	// ʵ��ͼƬ���첽����
    /**
     * 
     * @param albumName:ר������
     * @param imageUrl����ȡͼƬ��URL
     * @param mDir��ͼƬ���浽���ص�Ŀ¼
     * @param imageFullPath��ͼƬ�ڱ��ر����ȫ·��
     * @param showMode����ʾģʽ����һ�Ż�����һ��
     * @param curPos����ʶ�ڼ���ͼƬ����ʾ�ڱ�������
     * @param picsAmount����ר��ͼƬ��������
     */
	public void loadImage(final String albumName,final String imageUrl, final String mDir,
			final String imageFullPath, final Integer showMode, final Integer curPos,
			final Integer picsAmount) {
		//�������·���޸�Ϊ����cache��·��
		//final String strCacheFilePath= FileUtils.strReplace(imagePath,".jpg",Constants.CACHE_SUFFIX);
		// ��ѯ����Ŀ¼���鿴��ǰ��Ҫ���ص�ͼƬ�Ƿ��Ѿ������ڱ���Ŀ¼����
		// �¿���һ���̣߳����߳����ڽ���ͼƬ������
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
				bundle.putParcelable("bitmap", mImage);//����ͼƬ
				bundle.putInt("showmode", showMode);// ���嶯������0Ϊ�޶�����1Ϊ��һ�ŷ���2Ϊ��һ�ŷ���
				//���ñ���ͼƬ��Ϣ���Ա��ڱ���������ʾ
				String curPicInfo=curPos+"/"+picsAmount+"   "+albumName;
				bundle.putString("curPicInfo", curPicInfo);

				
				//�����ص�ͼƬ�洢������
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
		
		//���ʣ��ռ�С��ָ����ֵ������ݻ���ͼƬ������޸�ʱ�����ɾ������
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

	// ����Ϣ�߳�
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
				mBarDialog.setMessage("�������أ����Ժ�... 0%");			
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
*/	// �����Զ�����
/*	private void autoPlay() {
		isStop = false;
		new Thread(new AutoPlay()).start();
	}

	// ����Ϣ�߳�
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

/*	// �÷������ڸ���ͼƬ��URL��������������ͼƬ
	protected Bitmap loadImageFromUrl(String imageUrl) {
		int fileSize = 0;
		int downSize = 0;
		int progress = 0;//���ڱ�ʶ���ؽ��ȿ��еĽ��ȣ�ʹ��.�ĸ�����ʶ
		try {
			// ����ͼƬ��URL������ͼƬ��������һ��Bitmap����
			Bitmap bitmap = null;
			InputStream inStream = null;
			ByteArrayOutputStream outStream = null;
			try {

				HttpEntity entity = HttpDownloader.getEntity(imageUrl);
				// ��׳�Դ�����ȡ����ʧ�ܣ�����null
				if(null==entity){
					return null;
				}
				inStream = entity.getContent();

				fileSize = (int) entity.getContentLength();
				// ��׳�Դ�����ȡ�ļ�ʧ�ܣ�����null
				if (null == inStream) {
					return null;
				}
				try {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					//int progress=0;//���ڱ�ʶ���ؽ��ȿ��еĽ��ȣ�ʹ�á���ʶ
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						downSize += len;

						//int num = (int) ((double) downSize / (double) fileSize * 100);

					//	if (num > progress + 1) {
							progress++;//���ȼ�һ
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
