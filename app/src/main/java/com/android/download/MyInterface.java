package com.android.download;

import com.android.info.DownloadInfo;


public interface MyInterface {
	/**
	 * 
	 * @param btMsg���ذ�ť������
	 * @param tvMsg����״̬�ַ���
	 * @param btFlag ���ذ�ť��״̬��ʧЧ������Ч���������ص�״̬�����趨
	 */
	public void updateStatus(DownloadInfo downInfo);
	public void showToast(String strToast);
}
