package com.android.asyncload.albumicon;

import java.util.List;

import com.android.asyncload.albumicon.AsyncImageLoader.ImageCallback;
import com.android.beauty360.R;
import com.android.beauty360.point.MyPoints;
import com.android.download.BatchDownloadThread;
import com.android.download.MyInterface;
import com.android.info.Constants;
import com.android.info.DownloadInfo;
import com.android.utils.MyUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListItemInfoAdapter extends ArrayAdapter<ListItemInfo> {

	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
	//private List<View> mViewCache = new ArrayList<View>();
	private LayoutInflater inflater;
	private Context mContext;
	//private MySqliteDao mySqliteDao;//数据库操作类
	private DownloadInfo downInfo=null;
	MyPoints myPoints;
	//private LoadImage loadImage;

	// View对象的缓存
	// private Map<Integer,View> rowViews = new HashMap<Integer,View>();
	// private List<View> mViewCache = new ArrayList<View>();

	public ListItemInfoAdapter(Activity activity,
			List<ListItemInfo> imageAndTexts, ListView listView, Context mContext) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
		inflater = activity.getLayoutInflater();
		this.mContext=mContext;
		myPoints= new MyPoints(mContext,null);
//		if(Constants.mySqliteDao==null){
//			Constants.mySqliteDao = new MySqliteDao(mContext);
//		}
		//this.loadImage = loadImage;
	}

	public View getView(int position, View convertView, ViewGroup parent) {		
		View rowView = convertView;
		ViewCache viewCache;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.album_item_view, null);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);

		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		ListItemInfo imageAndText = getItem(position);

		// Load the image and set it on the ImageView
		String imageDir = imageAndText.getImageDir();
		String imagePath = imageAndText.getImagePath();
		String imageUrl = imageAndText.getImageUrl();

		ImageView imageView = viewCache.getImageView();
		imageView.setTag(imageUrl);
	
		// Set the text on the TextView
		viewCache.getTvTitleView().setText(imageAndText.getStrTitle());
		viewCache.getTvInfo1View().setText(imageAndText.getStrInfo1());
		viewCache.getTvInfo2View().setText(imageAndText.getStrInfo2());
		//MyUtils.outLog(" ListItemInfoAdapter getView imageAndText. is:"+imageAndText.toString());

		//start add for download 2013-10-05
		if (imageAndText.getDownButtonTxt().equals("")) {
			viewCache.getDownloadButton().setVisibility(View.GONE);
			viewCache.getTvDownloadProgressView().setVisibility(View.GONE);
		} else {
			viewCache.getDownloadButton().setVisibility(View.VISIBLE);
			viewCache.getTvDownloadProgressView().setVisibility(View.VISIBLE);
			viewCache.getDownloadButton().setText(
					DownloadInfo.getDownButtonTxt(imageAndText.getAlbumId(),
							mContext));
			viewCache.getTvDownloadProgressView().setText(
					DownloadInfo.getDownStatusTxt(imageAndText.getAlbumId(),
							mContext));
			viewCache.getDownloadButton().setTag(Constants.PRE_DOWN_BT_TAG+imageAndText.getAlbumId());
			viewCache.getTvDownloadProgressView().setTag(Constants.PRE_DOWN_STA_TAG+imageAndText.getAlbumId());

		}
		//MyUtils.outLog("viewCache.getDownloadButton() tag is:"+viewCache.getDownloadButton().getTag());
		//MyUtils.outLog("viewCache.getTvDownloadProgressView() is:"+viewCache.getTvDownloadProgressView().getTag());

		//MyUtils.outLog("downBt tag id is:"+imageAndText.getAlbumId());
		//MyUtils.outLog("imageAndText is:"+imageAndText.toString());
		viewCache.getDownloadButton().setOnClickListener(
				new ListViewButtonListener(imageAndText.getAlbumId(),imageAndText.getStrTitle(),
						imageAndText.getCataId(), position));
		if (Constants.hashMapDownloadInfo != null
				&& Constants.hashMapDownloadInfo.get(imageAndText.getAlbumId()) != null
				&& Constants.hashMapDownloadInfo.get(imageAndText.getAlbumId())
						.getDownStatus() == Constants.DOWNLOAD_DONE) {
			viewCache.getDownloadButton().setEnabled(false);
		} else {
			viewCache.getDownloadButton().setEnabled(true);
		}
		//end add
		
		// /Log.v(Constants.LOG_TAG, "imageUrl is:"+imageUrl);
		//loadImage.addTask(imageUrl,imageView,imageDir,imagePath);
		//loadImage.doTask();
		Bitmap cachedImage = asyncImageLoader.loadBitmap(imageDir, imagePath,
				imageUrl, new ImageCallback() {
					public void imageLoaded(Bitmap imageBitmap, String imageUrl) {
						ImageView imageViewByTag = (ImageView) listView
								.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							imageViewByTag.setImageBitmap(imageBitmap);
						}
					}
				});
		if (cachedImage == null) {
			imageView.setImageResource(R.drawable.app_icon);
		} else {
			imageView.setImageBitmap(cachedImage);
		}
		// rowViews.put(position, rowView);

		// 缓存view，防止刷新时重复加载
		// mViewCache.add(rowView);
		
		//add start for listview bg 2013-10-06
		rowView.setBackgroundResource(R.drawable.listitem_bk); //设置listview的背景
		//add end 
		return rowView;
	}

    class ListViewButtonListener implements OnClickListener {
        private int position;
        private String albumId;
        private String albumName;
        private String cataId;
        private TextView tvDownStauts;
        private Button btDownload;
        
        ListViewButtonListener(String albumId,String albumName,String cataId,int pos) {
            this.position = pos;
            this.albumId=albumId;
            this.cataId=cataId;
            this.albumName=albumName;
			}    
        
        
        //0：未下载过；1：正在下载；2：已下载过，但未下载完；3：下载完成；默认为下载过
        MyInterface myInterface= new MyInterface() {	
        	
			@Override
			public void showToast(String strToast) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, strToast,
					     Toast.LENGTH_SHORT).show();
			}

			@Override
			public void updateStatus(DownloadInfo updateDownInfo) {
				
				//MyUtils.outLog("downInfo album id is:"+updateDownInfo.getmAlbumId());
				//MyUtils.outLog(" downInfo.getDownloadedAmout() is:"+updateDownInfo.getDownloadedAmout()+" and downInfo.getPicAmount() is:"+updateDownInfo.getPicAmount());				

				btDownload = (Button)listView
						.findViewWithTag(Constants.PRE_DOWN_BT_TAG+updateDownInfo.getmAlbumId());
				tvDownStauts = (TextView)listView
						.findViewWithTag(Constants.PRE_DOWN_STA_TAG+updateDownInfo.getmAlbumId());
				//MyUtils.outLog(" btDownload is:"+btDownload+" and tvDownStauts is:"+tvDownStauts);
				if(btDownload==null || tvDownStauts==null){
					MyUtils.outLog("errr!!!!!!!!!!downInfo album id is:"+updateDownInfo.getmAlbumId());
					//return;
				}
				
				// TODO Auto-generated method stub					
				if(updateDownInfo.getDownloadedAmout()==updateDownInfo.getPicAmount()){//表示已经下载完了
					updateDownInfo.setDownStatus(Constants.DOWNLOAD_DONE);
//					strButtonText=mContext.getResources().getString(R.string.down_over);
//					strStatusText= String.format(mContext.getResources().getString(
//							R.string.downloaded),downInfo.getDownloadedAmout());
					if(tvDownStauts!=null&&btDownload!=null){
						tvDownStauts.setText(DownloadInfo.getDownStatusTxt(updateDownInfo.getmAlbumId(), mContext));
						btDownload.setText(DownloadInfo.getDownButtonTxt(updateDownInfo.getmAlbumId(), mContext));
						btDownload.setEnabled(false);
					}
				}else{
					updateDownInfo.setDownStatus(Constants.DOWNLOADING);
					//strButtonText=mContext.getResources().getString(R.string.pause_download);
//					strStatusText= String.format(mContext.getResources().getString(
//							R.string.downloading),downInfo.getDownloadedAmout());
					if(tvDownStauts!=null&&btDownload!=null){
						tvDownStauts.setText(DownloadInfo.getDownStatusTxt(updateDownInfo.getmAlbumId(), mContext));
						//btDownload.setText(strButtonText);
						btDownload.setEnabled(true);
					}
				}
				//如果下载表中没有此专辑，需要添加
				if(Constants.mySqliteDao.isDownInfoExist(albumId)==false){
					Constants.mySqliteDao.insertDownInfo(updateDownInfo);
				}else{
					Constants.mySqliteDao.updateDownInfo(updateDownInfo);
				}
			}
		};

		@Override
        public void onClick(View v) {
//			btDownload=null;
//			tvDownStauts=null;
			btDownload = (Button)listView
					.findViewWithTag(Constants.PRE_DOWN_BT_TAG+albumId);
			tvDownStauts = (TextView)listView
					.findViewWithTag(Constants.PRE_DOWN_STA_TAG+albumId);
        	//System.out.println("tvDownStauts is:"+tvDownStauts);
        	//System.out.println("btDownload is:"+btDownload);
        	
			if(null==tvDownStauts||btDownload==null){//错误判断，待处理。
				return;//需要补充提示信息
			}
			
			if(Constants.hashMapDownloadInfo==null){//健壮性处理
				return;//错误处理
			}
			
    		downInfo = Constants.hashMapDownloadInfo.get(albumId);//取出download info信息
    		//MyUtils.outLog("downInfo is:"+downInfo);
    		
    		if(downInfo==null){
    			downInfo = new DownloadInfo(albumId,cataId,0);
    			if ((myPoints.subPoints(downInfo.getPicAmount())) == false) {// 如果扣减积分失败，弹出提示，并返回
    				MyUtils.showPointLackDlg(mContext,myPoints);
    				return;
    			}
    			Constants.hashMapDownloadInfo.put(albumId, downInfo);// 存储到全局的下载信息表中
    			Constants.mySqliteDao.insertDownInfo(downInfo);// 插入数据库
    		}
    	

    		
    		//MyUtils.outLog("downInfo.getDownThread() id is:"+downInfo.getDownThread().getId());
    		
//    		if(downInfo.getDownStatus()==Constants.DOWNLOADING){//如果正在下载
//    			//将状态置为暂停
//    			
//    		}else if(downInfo.getDownStatus()==Constants.DOWNLOAD_PAUSE){//如果暂停
//    			
//    		}else if(downInfo.getDownStatus()==Constants.DOWNLOAD_DONE){//其他
//    			//什么也不处理
//    		}else{
//    			//新建下载闲扯
//    		}
    		
			//0：未下载过；1：正在下载；2：暂停；3：下载完成；默认为下载过
			if(btDownload.isEnabled()&&btDownload.getText().equals(mContext.getResources().getString(R.string.download))){//如果此专辑没有下载过
				//按钮设置为暂停				
				btDownload.setText(mContext.getResources().getString(R.string.pause_download));
				MyUtils.outLog("001downInfo.getDownThread() is:"+downInfo.getDownThread());

				if(downInfo.getDownThread()!=null){
					downInfo.getDownThread().setSuspend(false);
				}else{
					BatchDownloadThread downThread=new BatchDownloadThread(myInterface,downInfo,mContext);
					downThread.start();
					downInfo.setDownThread(downThread);
				}
				//MyUtils.outLog("001downInfo.getDownThread() id is:"+downInfo.getDownThread().getId());

//				//暂停当前专辑下载进程
//				if(Constants.hashMapDownloadInfo.get(albumId).getDownThread()!=null){
//					Constants.hashMapDownloadInfo.get(albumId).getDownThread().setSuspend(true);
//				}
				Constants.hashMapDownloadInfo.get(albumId).setDownStatus(Constants.DOWNLOADING);

			}else if(btDownload.isEnabled()&& btDownload.getText().equals(mContext.getResources().getString(R.string.continue_download))){//如果此专辑之前下载过，但未下载完成
				//按钮设置为暂停
				btDownload.setText(mContext.getResources().getString(R.string.pause_download));
				//MyUtils.outLog("002downInfo.getDownThread() is:"+downInfo.getDownThread());

				if(downInfo.getDownThread()!=null){
					downInfo.getDownThread().setSuspend(false);
				}else{
					BatchDownloadThread downThread=new BatchDownloadThread(myInterface,downInfo,mContext);
					downThread.start();
					downInfo.setDownThread(downThread);
				}
				//MyUtils.outLog("002downInfo.getDownThread() id is:"+downInfo.getDownThread().getId());

//				//暂停当前专辑下载进程
//				if(Constants.hashMapDownloadInfo.get(albumId).getDownThread()!=null){
//					Constants.hashMapDownloadInfo.get(albumId).getDownThread().setSuspend(true);
//				}
				Constants.hashMapDownloadInfo.get(albumId).setDownStatus(Constants.DOWNLOADING);
				//MyUtils.outLog("press pause button and DownStatus is:"+Constants.hashMapDownloadInfo.get(albumId).getDownStatus());

			}else if(btDownload.isEnabled()&& btDownload.getText().equals(mContext.getResources().getString(R.string.pause_download))){//处于暂停状态
				//按钮设置为继续
				btDownload.setText(mContext.getResources().getString(R.string.continue_download));
				//MyUtils.outLog("003downInfo.getDownThread() is:"+downInfo.getDownThread());

				downInfo.getDownThread().interrupt();
				if(downInfo.getDownThread()!=null){
					downInfo.getDownThread().setSuspend(true);
				}else{
					BatchDownloadThread downThread=new BatchDownloadThread(myInterface,downInfo,mContext);
					downThread.start();
					downInfo.setDownThread(downThread);
					downInfo.getDownThread().setSuspend(true);
				}
				//MyUtils.outLog("003downInfo.getDownThread() id is:"+downInfo.getDownThread().getId());
//				//继续当前专辑下载进程
//				if(Constants.hashMapDownloadInfo.get(albumId).getDownThread()!=null){
//					Constants.hashMapDownloadInfo.get(albumId).getDownThread().setSuspend(false);
//				}
				Constants.hashMapDownloadInfo.get(albumId).setDownStatus(Constants.DOWNLOAD_PAUSE);//
				MyUtils.outLog("press continue button and DownStatus is:"+Constants.hashMapDownloadInfo.get(albumId).getDownStatus());
			}
			
            //int vid=v.getId();
            System.out.println("clicked button pos:"+position);
            System.out.println("clicked button albumId :"+this.albumId);
            System.out.println("clicked alubmName is:"+this.albumName);
            System.out.println("button View text:"+btDownload.getText());
        }
    }
}
