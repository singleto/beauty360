package com.android.asyncload.albumicon;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.android.download.HttpDownloader;
import com.android.info.Constants;
import com.android.utils.FileUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	//private BlockingQueue<Runnable> mThreadQueue;
	//private Stack<Runnable> threadStack;
	//private ThreadPoolExecutor mThreadPoolExecutor;
	private ImageMemoryCache memoryCache;// 内存缓存
    private ExecutorService executorService;
	public AsyncImageLoader() {
		memoryCache = new ImageMemoryCache();
	    executorService=Executors.newFixedThreadPool(2);

		// 线程池：最大50条，每次执行：1条，空闲线程结束的超时时间：180秒
/*		mThreadQueue = new LinkedBlockingQueue<Runnable>();
		mThreadPoolExecutor = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
				Constants.MAXIMUM_POOL_SIZE, Constants.KEEP_ALIVE,
				TimeUnit.SECONDS, mThreadQueue);// 线程池是静态变量，所有的异步任务都会放到这个线程池的工作线程内执行。
		Log.v(Constants.LOG_TAG, "=====new mThreadPoolExecutor ");*/
	}

	public Bitmap loadBitmap(final String imageDir, final String imagePath,
			final String imageUrl, final ImageCallback imageCallback) {
		//Bitmap mBitmap = null;
		Bitmap mBitmap = memoryCache.getBitmapFromCache(imageUrl);

		if (mBitmap != null) {
			return mBitmap;

		} else if (FileUtils.isFileExist(imagePath)) {

			/*
			 * BitmapFactory.Options options = new BitmapFactory.Options();
			 * options.inSampleSize = 4;
			 */
			mBitmap = BitmapFactory.decodeFile(imagePath);
			if (null != mBitmap) {
				return mBitmap;
			}
		}
		// 如果有缓存则使用缓存中的图片
		/*
		 * if(imageCache.containsKey(imageUrl)){ Log.v(Constants.LOG_TAG,
		 * "get image from SoftReference"); SoftReference<Bitmap> softReference
		 * = imageCache.get(imageUrl); mBitmap = softReference.get(); if(mBitmap
		 * != null) { return mBitmap; } }
		 */

		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Bitmap mBitmap = (Bitmap) message.obj;
				// 调用回调函数展示图片
				if (null != mBitmap) {
					imageCallback.imageLoaded(mBitmap, imageUrl);
				}
			}
		};
		// 用线程池来做下载图片的任务
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				Bitmap mBitmap = loadImageFromUrl(imageDir, imagePath, imageUrl);
				Message message = handler.obtainMessage(0, mBitmap);
				handler.sendMessage(message);
			}
		});

		return null;

	}

	// 网络图片先下载到本地cache目录保存，以imagUrl的图片文件名保存。如果有同名文件在cache目录就从本地加载
	/**
	 * 
	 * @param imageDir:本地图片存放目录
	 * @param imageFullPath：图片本地存放的全路径
	 * @param imageUrl：获取图片的url
	 * @return
	 */
	public Bitmap loadImageFromUrl(String imageDir, String imageFullPath,
			String imageUrl) {
		Bitmap mImage = null;
		boolean isNeedSave = false;
		//将路径转化为本地cache路径
		//String strCacheFilePath= FileUtils.strReplace(imagePath,".jpg",Constants.CACHE_SUFFIX);
		if (FileUtils.isFileExist(imageFullPath)) {			
			 //BitmapFactory.Options options = new BitmapFactory.Options();
			 //options.inSampleSize = 4;
			 
			mImage = BitmapFactory.decodeFile(imageFullPath);
			if (null == mImage) {
				mImage = HttpDownloader.downloadIcon(imageUrl);
				isNeedSave = true;
			}else{
				//更新cache文件的时间，以便根据修改时间清除缓存
	            FileUtils.updateFileTime(imageFullPath);
				return mImage;
			}
		} else {
			mImage = HttpDownloader.downloadIcon(imageUrl);
			isNeedSave = true;
		}

		/*
		 * if(null!=mImage){ Log.v(Constants.LOG_TAG,
		 * "put image to SoftReference"); imageCache.put(imageUrl, new
		 * SoftReference<Bitmap>(mImage)); }
		 */
		if (isNeedSave && null != mImage) {
			try {
				FileUtils.autoSaveImage(imageDir, imageFullPath, mImage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.v(Constants.LOG_TAG, "loadImageFromUrl:" + e);
			}
		}
		memoryCache.addBitmapToCache(imageUrl, mImage);
		return mImage;
	}
 
     public interface ImageCallback {
         public void imageLoaded(Bitmap imageBitmap, String imageUrl);
     }

}