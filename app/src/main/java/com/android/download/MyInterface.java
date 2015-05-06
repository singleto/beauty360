package com.android.download;

import com.android.info.DownloadInfo;


public interface MyInterface {
	/**
	 * 
	 * @param btMsg下载按钮的文字
	 * @param tvMsg下载状态字符串
	 * @param btFlag 下载按钮的状态，失效或者有效，根据下载的状态进行设定
	 */
	public void updateStatus(DownloadInfo downInfo);
	public void showToast(String strToast);
}
