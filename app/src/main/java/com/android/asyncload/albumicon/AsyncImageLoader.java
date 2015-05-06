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
	private ImageMemoryCache memoryCache;// �ڴ滺��
    private ExecutorService executorService;
	public AsyncImageLoader() {
		memoryCache = new ImageMemoryCache();
	    executorService=Executors.newFixedThreadPool(2);

		// �̳߳أ����50����ÿ��ִ�У�1���������߳̽����ĳ�ʱʱ�䣺180��
/*		mThreadQueue = new LinkedBlockingQueue<Runnable>();
		mThreadPoolExecutor = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
				Constants.MAXIMUM_POOL_SIZE, Constants.KEEP_ALIVE,
				TimeUnit.SECONDS, mThreadQueue);// �̳߳��Ǿ�̬���������е��첽���񶼻�ŵ�����̳߳صĹ����߳���ִ�С�
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
		// ����л�����ʹ�û����е�ͼƬ
		/*
		 * if(imageCache.containsKey(imageUrl)){ Log.v(Constants.LOG_TAG,
		 * "get image from SoftReference"); SoftReference<Bitmap> softReference
		 * = imageCache.get(imageUrl); mBitmap = softReference.get(); if(mBitmap
		 * != null) { return mBitmap; } }
		 */

		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				Bitmap mBitmap = (Bitmap) message.obj;
				// ���ûص�����չʾͼƬ
				if (null != mBitmap) {
					imageCallback.imageLoaded(mBitmap, imageUrl);
				}
			}
		};
		// ���̳߳���������ͼƬ������
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

	// ����ͼƬ�����ص�����cacheĿ¼���棬��imagUrl��ͼƬ�ļ������档�����ͬ���ļ���cacheĿ¼�ʹӱ��ؼ���
	/**
	 * 
	 * @param imageDir:����ͼƬ���Ŀ¼
	 * @param imageFullPath��ͼƬ���ش�ŵ�ȫ·��
	 * @param imageUrl����ȡͼƬ��url
	 * @return
	 */
	public Bitmap loadImageFromUrl(String imageDir, String imageFullPath,
			String imageUrl) {
		Bitmap mImage = null;
		boolean isNeedSave = false;
		//��·��ת��Ϊ����cache·��
		//String strCacheFilePath= FileUtils.strReplace(imagePath,".jpg",Constants.CACHE_SUFFIX);
		if (FileUtils.isFileExist(imageFullPath)) {			
			 //BitmapFactory.Options options = new BitmapFactory.Options();
			 //options.inSampleSize = 4;
			 
			mImage = BitmapFactory.decodeFile(imageFullPath);
			if (null == mImage) {
				mImage = HttpDownloader.downloadIcon(imageUrl);
				isNeedSave = true;
			}else{
				//����cache�ļ���ʱ�䣬�Ա�����޸�ʱ���������
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