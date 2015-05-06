package com.android.utils;

import com.android.info.CatagoryInfo;
import com.android.info.Constants;

import android.content.Context;
import android.os.AsyncTask;

public class LoadCataAndAlbumToDbTask extends AsyncTask<Void, Void, Void>{

	Context mContext;
	
	public LoadCataAndAlbumToDbTask(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		//
		Constants.mySqliteDao.InitCataAndAlbumTables();
		MyUtils.outLog("enter LoadCataAndAlbumToDbTask doInBackground");
		// �����ר����Ϣ���³�ʼ�������ݿ�
		for (int i = 0; i < Constants.mCatagorysInfo.size(); i++) {
			CatagoryInfo cataInfo = Constants.mCatagorysInfo.get(i);
			Constants.mySqliteDao.insertCata(cataInfo);
			if (cataInfo.getmArrayListAlbumsInfo() != null)
				Constants.mySqliteDao.insertAlbums(Constants.mCatagorysInfo
						.get(i).getAllAlbums(), "0");

		}
		
		//Constants.mySqliteDao.initCatas(Constants.mCatagorysInfo);
		// FileUtils.removeCache(Constants.LOCAL_PATH, (float) 1);
		// Toast.makeText(mContext,mContext.getResources().getString(R.string.del_cache_res),
		// Toast.LENGTH_SHORT).show();
		return null;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

//	private void deleteFile(File dir){
//		if(!dir.exists()){
//			return;
//		}
//		if(dir.isFile()){
//			dir.delete();
//			if(BuildConfig.DEBUG){
//				Log.d(TAG, "delete:" + dir.getPath());
//			}
//			return;
//		}
//		if(dir.isDirectory()){
//			File[] subFiles = dir.listFiles();
//			for(File subFile : subFiles){
//				deleteFile(subFile);
//			}
//		}
//	}
//	
//	@Override
//	protected Void doInBackground(Void... params) {
//		try{
//			//webview H5����
//			File cacheDir = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE);
//			deleteFile(cacheDir);
//			//webviewҳ����Դ ����
//			cacheDir = getActivity().getCacheDir();
//			deleteFile(cacheDir);
//			getActivity().deleteDatabase("webview.db");
//			getActivity().deleteDatabase("webviewCache.db");
//			//bitmapfun����
//			cacheDir = ImageDiskCache.getImageDiskCacheDir(getActivity());
//			deleteFile(cacheDir);
//		}catch(Exception e){
//			if(BuildConfig.DEBUG){
//				e.printStackTrace();
//			}
//		}
//		
//		return null;
//	}
//
//	@Override
//	protected void onPreExecute() {
//		if(mPd == null){
//			mPd = new ProgressDialog(getActivity(), R.style.Theme_Dialog_NoTitle);
//			mPd.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_large));
//			mPd.setCancelable(false);
//		}
//		if(!mPd.isShowing()){
//			mPd.setMessage(getActivity().getResources().getString(R.string.cleaning));
//			mPd.show();
//		}
//	}
//
//	@Override
//	protected void onPostExecute(Void result) {
//		if(mPd != null && mPd.isShowing()){
//			mPd.dismiss();
//		}
//		Toast.makeText(getActivity(), R.string.op_success, Toast.LENGTH_SHORT).show();
//	}
	
}

