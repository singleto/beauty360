package com.android.asyncload.albumicon;

import java.util.List;

import com.android.asyncload.albumicon.AsyncImageLoader.ImageCallback;
import com.android.beauty360.R;
import com.android.utils.MyUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListItemInfoAdapterLocal extends ArrayAdapter<ListItemInfo> {

	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
	//private List<View> mViewCache = new ArrayList<View>();
	private LayoutInflater inflater;

	//private LoadImage loadImage;

	// View对象的缓存
	// private Map<Integer,View> rowViews = new HashMap<Integer,View>();
	// private List<View> mViewCache = new ArrayList<View>();

	public ListItemInfoAdapterLocal(Activity activity,
			List<ListItemInfo> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
		inflater = activity.getLayoutInflater();
//		if(Constants.mySqliteDao==null){
//			Constants.mySqliteDao = new MySqliteDao(mContext);
//		}
		//this.loadImage = loadImage;
	}

	public View getView(int position, View convertView, ViewGroup parent) {		
		View rowView = convertView;
		ViewCacheLocal viewCacheLocal;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.album_item_view_local, null);
			viewCacheLocal = new ViewCacheLocal(rowView);
			rowView.setTag(viewCacheLocal);

		} else {
			viewCacheLocal = (ViewCacheLocal) rowView.getTag();
		}
		ListItemInfo imageAndText = getItem(position);

		// Load the image and set it on the ImageView
		String imageDir = imageAndText.getImageDir();
		String imagePath = imageAndText.getImagePath();
		String imageUrl = imageAndText.getImageUrl();

		ImageView imageView = viewCacheLocal.getImageView();
		imageView.setTag(imageUrl);
		
		// Set the text on the TextView
		viewCacheLocal.getTvTitleView().setText(imageAndText.getStrTitle());
	
		MyUtils.outLog("downBt tag id is:"+imageAndText.getAlbumId());
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
}
