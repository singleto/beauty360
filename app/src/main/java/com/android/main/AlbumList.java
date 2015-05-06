package com.android.main;

//import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;
import com.android.asyncload.albumicon.ListItemInfo;
import com.android.asyncload.albumicon.ListItemInfoAdapter;
import com.android.beauty360.R;
import com.android.beauty360.point.MyPoints;
import com.android.info.AlbumInfo;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.picview.PicViewer;
import com.android.utils.FileUtils;
import com.android.utils.MyUtils;


public class AlbumList extends Activity implements AdsMogoListener{
	private ListView albumListView;
	
	private AlbumInfo mAlbumInfo;

	AdsMogoLayout adsMogoLayout;
	MyPoints myPoints;//�������ǽ
	// ��ҳ�������ר����Ϣ
	ArrayList<AlbumInfo> mArrayListAlbumInfo;
	// ��ҳ���ÿ��ר����ͼƬ������չʾ��Ϣ
	List<ListItemInfo> albumImaTxtArray;

	// MyUpdate myUpdate=new MyUpdate(this);//��ʼ���Զ�������

	// ��ʶ��activity��list�Ƿ�����ר��list
	// boolean isSubAlbumList=false;

	// private ExecutorService executorService=null;
	// private LoadImage loadImage = new LoadImage();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_list_view);

		//��ʼ������ǽ
		this.myPoints=new MyPoints(this,null);
		//myPoints.getAdPoints();
		// ���չ���ʼ��
//		AppConnect.getInstance(this);
//		AppConnect.getInstance(this).getPoints(this);// ��ʼ������

		// ���ù��
		// if(Constants.appInfo.getIsAddAds()){
		// ���ü����ص� ���а��� ���� չʾ ����ʧ�ܵ��¼��Ļص�
		adsMogoLayout = ((AdsMogoLayout) this.findViewById(R.id.adsMogoView));
		adsMogoLayout.setAdsMogoListener(this);
		// addMogoAdView();
		// }

		albumListView = (ListView) findViewById(R.id.list_view);

		// ��ȡ�û������ͼƬר����Ϣ
//		mArrayListAlbumInfo = (ArrayList<AlbumInfo>) getIntent()
//				.getParcelableExtra(Constants.ARR_ALBUMS_INFO_KEY);
		mAlbumInfo = MyUtils.getAlbumInfo(
				getIntent().getStringExtra(Constants.CATA_ID), getIntent()
						.getStringExtra(Constants.ALBUM_ID));
		mArrayListAlbumInfo=mAlbumInfo.getArrSubAlbumInfo();
		// ��activety�����������Ƿ�����ר��
		// isSubAlbumList=getIntent().getExtras().getBoolean(Constants.IS_SUBALBUM_KEY);
		albumImaTxtArray=getAlbumList(mAlbumInfo.getArrSubAlbumInfo());
		// ��ȡ��ǰ����ר���б�
//		albumImaTxtArray = (List<ListItemInfo>) getIntent()
//				.getParcelableExtra(Constants.LIST_ALBUM_IMAGE_ADN_TEXT_KEY);
		
		// ����������
		ListItemInfoAdapter adapter = new ListItemInfoAdapter(this,
				albumImaTxtArray, albumListView,this);
		albumListView.setAdapter(adapter);
		albumListView.setOnItemClickListener(new ListItemClickListener());
		albumListView.setOnScrollListener(new ListItemScrollListener());

		// //�ж��Ƿ���Ҫ����
		// if(Constants.appInfo!=null && Constants.isShowUpdateDig==false &&
		// Constants.appInfo.getIsNeedUpdate()){
		// Constants.isShowUpdateDig=true;
		// myUpdate.update();
		// }
		// Log.v(Constants.LOG_TAG,
		// "Constants.binder.isBinderAlive() is:"+Constants.binder.isBinderAlive());

	}

	// ���ù��
	/*
	 * void addMogoAdView(){ AdsMogoLayout adMogoLayoutCode = new
	 * AdsMogoLayout(this,
	 * Constants.MOGO_ID,AdsMogoTargeting.GETINFO_FULLSCREEN_AD);
	 * FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
	 * FrameLayout.LayoutParams.FILL_PARENT,
	 * FrameLayout.LayoutParams.WRAP_CONTENT); // ���ù����ֵ�λ��(�����ڵײ�)
	 * params.bottomMargin = 0; // ��ӹ��״̬����
	 * adMogoLayoutCode.setAdsMogoListener(this); params.gravity =
	 * Gravity.BOTTOM; addContentView(adMogoLayoutCode, params); }
	 *//*
		 * void addDianRuAdView(){ final AdSpace space = new AdSpace(this);
		 * space.setInterval(20); //���ù��ˢ��ʱ�� space.setType(0); //���ù�����ͣ��˴���ֵΪ0
		 * space.setKeyword("game");//���ùؼ��� FrameLayout.LayoutParams params =
		 * new FrameLayout.LayoutParams( FrameLayout.LayoutParams.FILL_PARENT,
		 * FrameLayout.LayoutParams.WRAP_CONTENT); // ���ù����ֵ�λ��(�����ڵײ�)
		 * params.bottomMargin = 0; // ��ӹ��״̬���� params.gravity = Gravity.BOTTOM;
		 * RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		 * android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		 * android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		 * lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 * lp.addRule(RelativeLayout.CENTER_VERTICAL); lp.bottomMargin=0;
		 * //mainLayout.addView(space, lp); this.addContentView(space, params);
		 * }
		 */
	@Override
	protected void onDestroy() {
		// // TODO Auto-generated method stub
		// if(2==Constants.verInfo.getAdType()){
		// }
//		AppConnect.getInstance(AlbumList.this).finalize();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		/*
		 * List<Runnable> temp=Constants.executorService.shutdownNow();
		 * Log.v(Constants.LOG_TAG,
		 * "AlbumList onPause temp's size is:"+temp.size());
		 */super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * listview������
	 * 
	 * @author yuguangbao
	 * 
	 */
	class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// protected void onListItemClick(ListView l, View v, int position,
			// long id) {
			// TODO Auto-generated method stub
			// CategoryInfo mCategoryInfo = (CategoryInfo) getIntent()
			// .getSerializableExtra(Constants.CATEGORYS_INFO_KEY);
			// ArrayList<AlbumInfo> mArrayListAlbumInfo = mCategoryInfo
			// .getmArrayListAlbumsInfo();
			AlbumInfo mAlbumInfo = mArrayListAlbumInfo.get(arg2);
			Intent mIntent;
			if (mAlbumInfo.getArrSubAlbumInfo() != null
					&& mAlbumInfo.getArrSubAlbumInfo().size() > 0) {// �������ר������ת����ר���б�
				// ��ȡ��ǰר������ר���б�
				List<ListItemInfo> subAlbumImaTxtArray = getAlbumList(mAlbumInfo
						.getArrSubAlbumInfo());
				mIntent = new Intent()
						.setClass(AlbumList.this, AlbumList.class);
				
				/* ����intent���ݣ�����ͼƬר����Ϣ����һ��activity */
				mIntent.putExtra(Constants.ALBUM_ID, mAlbumInfo.getId());
				mIntent.putExtra(Constants.CATA_ID, mAlbumInfo.getCategoryId());
				
//				Bundle mBundle = new Bundle();
//				// ����ÿ��ҳ���е���ר��ͼ��������б�
//				mBundle.putParcelable(Constants.LIST_ALBUM_IMAGE_ADN_TEXT_KEY,
//						(Parcelable) subAlbumImaTxtArray);
//				// ����ÿ��ҳ���������ר����Ϣ
//				mBundle.putParcelable(Constants.ARR_ALBUMS_INFO_KEY,
//						(Parcelable) mAlbumInfo.getArrSubAlbumInfo());
//				// mBundle.putBoolean(Constants.IS_SUBALBUM_KEY, true);
//				mIntent.putExtras(mBundle);
				// AlbumList.this.startActivity(mIntent);

			} else {
				// ��ר����Ϣ������һ��activity
//				Bundle mBundle = new Bundle();
//				mBundle.putParcelable(Constants.ALBUM_INFO_KEY, mAlbumInfo);
				mIntent = new Intent()
						.setClass(AlbumList.this, PicViewer.class);
				//mIntent.putExtras(mBundle);
				mIntent.putExtra(Constants.CATA_ID,mAlbumInfo.getCategoryId());
				mIntent.putExtra(Constants.ALBUM_ID,mAlbumInfo.getId());
				// AlbumList.this.startActivity(mIntent);
			}
			mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			AlbumList.this.startActivity(mIntent);
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

	protected List<ListItemInfo> getAlbumList(
			ArrayList<AlbumInfo> mArrayListAlbumInfo) {
		List<ListItemInfo> albumArray = new ArrayList<ListItemInfo>();
		AlbumInfo mAlbumInfo = null;
		ListItemInfo albumItem = null;
		String strAlbumIconUrl = null;
		String strAlbumIconDir = null;
		String strAlbumIconPath = null;
		String strInfo1 = null;
		String strInfo2 = null;
		//add start for download 2013-11-17
		String downButtonTxt="";
		String downStatusTxt="";
		//add end

		for (int i = 0; i < mArrayListAlbumInfo.size(); i++) {
			mAlbumInfo = mArrayListAlbumInfo.get(i);
			strAlbumIconDir = Constants.LOCAL_PATH + mAlbumInfo.getId() + "/";
			strAlbumIconPath = Constants.LOCAL_PATH + mAlbumInfo.getId()
					+ "/icon" + Constants.CACHE_SUFFIX;
			// strAlbumIconUrl = Constants.PRE_URL + mAlbumInfo.getId()
			// + "/" + mAlbumInfo.getIconName();
			strAlbumIconUrl = Constants.REQ_SERV + "?"
					+ Constants.urlParams.getIconParam(mAlbumInfo.getId());
			if (mAlbumInfo.getArrSubAlbumInfo().size() > 0) {
				strInfo1 = getResources().getString(R.string.album_amount)
						+ "��" + mAlbumInfo.getPicAmount() + "  "
						+ this.getResources().getString(R.string.explore_pic)
						+ "��" + mAlbumInfo.getClickAmount() + "  "
						+ getResources().getString(R.string.praise_num) + "��"
						+ mAlbumInfo.getPraiseAmount();
				//add start for download 2013-11-17
				downButtonTxt="";
				downStatusTxt="";
				//add end
			} else {
				strInfo1 = getResources().getString(R.string.pic_amount) + "��"
						+ mAlbumInfo.getPicAmount() + "  "
						+ getResources().getString(R.string.explore_pic) + "��"
						+ mAlbumInfo.getClickAmount() + "  "
						+ getResources().getString(R.string.praise_num) + "��"
						+ mAlbumInfo.getPraiseAmount();
				//add start for download 2013-11-17
				downButtonTxt=DownloadInfo.getDownButtonTxt(mAlbumInfo.getId(),this);
				downStatusTxt=DownloadInfo.getDownStatusTxt(mAlbumInfo.getId(),this);
				//add end	
			}

			strInfo2 = getResources().getString(R.string.download) + "��"
					+ mAlbumInfo.getDownLoadAmount();

			// start modify for download 2013-10-05
			albumItem = new ListItemInfo(strAlbumIconDir, strAlbumIconPath,
					strAlbumIconUrl, mAlbumInfo.getmAlbumName(), strInfo1,
					strInfo2, downButtonTxt,downStatusTxt);
			albumItem.setAlbumId(mAlbumInfo.getId());
			albumItem.setCataId(mAlbumInfo.getCategoryId());
			// end modify
			albumArray.add(albumItem);
		}
		return albumArray;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			 //this.finish();
		 }
		 //For ���� ---------�ֶ���ȡ���͹��
		 //if(Constants.appInfo.getIsAddAds()){
		 //AppConnect.getInstance(this).getPushAd();
		 //}
		
//		 AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
//		 alertbBuilder
//		 .setTitle("�˳�����")
//		 .setMessage("��ȷ��Ҫ�뿪��")
//		 .setPositiveButton("ȷ��",
//		 new DialogInterface.OnClickListener() {
//		
//		 @Override
//		 public void onClick(DialogInterface dialog,
//		 int which) {
//		 Log.v(Constants.LOG_TAG, "start finish autoUpdate");
//		
//		 //�Զ�����--ͣ������
//		 if(AlbumList.this.myUpdate.getBinder()!=null){
//		 AlbumList.this.myUpdate.stopService();
//		 }
		
		 // â������ͷ���Դ
		 // ��� adsMogoLayout ʵ�� ���������ڶ��̻߳�����Ƶ��̳߳�
		 // �˷����벻Ҫ���׵��ã��������ʱ�䲻����������޷�ͳ�Ƽ���
		 // if (adsMogoLayout != null) {
		 // adsMogoLayout.clearThread();
		 // }
		 // for ���չ��
		 //if(Constants.appInfo.getIsAddAds()){
		 //}
		 // �������Activity

		 //MyUtils.quitApp();		
//		 }
//		 })
//		 .setNegativeButton("ȡ��",
//		 new DialogInterface.OnClickListener() {
//		
//		 @Override
//		 public void onClick(DialogInterface dialog,
//		 int which) {
//		 dialog.cancel();
//		
//		 }
//		 }).create();
//		 alertbBuilder.show();
//		 return true;
//		 }
//		AlbumList.this.finish();
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
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
		 * add()�������ĸ������������ǣ� 1��������������Ļ���дMenu.NONE,
		 * 2��Id���������Ҫ��Android�������Id��ȷ����ͬ�Ĳ˵� 3��˳���Ǹ��˵�������ǰ������������Ĵ�С����
		 * 4���ı����˵�����ʾ�ı�
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1,
				this.getResources().getString(R.string.get_points)).setIcon(
				android.R.drawable.ic_menu_share);
		// setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ���ͼ�꣬ͬѧ������һ��,��

		// android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��
		menu.add(Menu.NONE, Menu.FIRST + 2, 2,
				this.getResources().getString(R.string.del_cache)).setIcon(
				android.R.drawable.ic_menu_delete);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3,
				this.getResources().getString(R.string.suggestion)).setIcon(
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
			// ��ʾ�Ƽ�Ӧ���б�Offer��
			this.myPoints.showGetPointDlg();
			//AppConnect.getInstance(this).showOffers(this);
			break;
		case Menu.FIRST + 2:
			FileUtils.removeCache(Constants.LOCAL_PATH, (float) 1);
			Toast.makeText(AlbumList.this,
					this.getResources().getString(R.string.del_cache_res),
					Toast.LENGTH_SHORT).show();
			break;
		case Menu.FIRST + 3:
			this.myPoints.showFeedback();
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

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * menu.add(Menu.NONE, Menu.FIRST + 1, 1,
	 * "����").setIcon(android.R.drawable.ic_menu_info_details
	 * );//ic_menu_help;ic_menu_info_details if(this.isSubAlbumList){
	 * menu.add(Menu
	 * .NONE,Menu.FIRST+2,2,"����").setIcon(android.R.drawable.ic_menu_revert);//
	 * }else{
	 * menu.add(Menu.NONE,Menu.FIRST+2,2,"�˳�").setIcon(android.R.drawable.
	 * ic_menu_close_clear_cancel); } return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * switch(item.getItemId() - Menu.FIRST) { case 1:
	 * 
	 * break; case 2: long now = SystemClock.uptimeMillis(); long n =
	 * System.currentTimeMillis(); //Log.d("Tiger", "Intent.ACTION_SOFT_" +
	 * keyCode+ "_PRESSED   0=" + n); //try { KeyEvent down = new KeyEvent(now,
	 * now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0); //KeyEvent up = new
	 * KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK, 0);
	 * //IWindowManager wm =
	 * IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
	 * //wm.injectKeyEvent(down, false); //wm.injectKeyEvent(up, false);
	 * 
	 * //KeyEvent event=new
	 * KeyEvent(0,0,KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK
	 * ,0,KeyEvent.KEYCODE_BACK); onKeyDown(KeyEvent.KEYCODE_BACK, down); break;
	 * } return super.onOptionsItemSelected(item); }
	 */

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