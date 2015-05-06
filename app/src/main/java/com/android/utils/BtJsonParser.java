package com.android.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.info.AlbumInfo;
import com.android.info.BtAppInfo;
import com.android.info.CatagoryInfo;
import com.android.info.Constants;
import com.android.info.ImageInfo;

public class BtJsonParser {

	/**
	 * 解析服务器端返回的类别信息json串
	 * @param strJson
	 * @return
	 */
	public ArrayList<CatagoryInfo> parseCata(String strJson) {
		ArrayList<CatagoryInfo> resArrCata = new ArrayList<CatagoryInfo>();
		try {
			JSONArray cataArr = new JSONArray(strJson);
			
			if(cataArr!=null){
				for(int i=0; i<cataArr.length(); i++){ 
					JSONObject jsonObj=cataArr.getJSONObject(i);
					CatagoryInfo cataInfo=new CatagoryInfo();
					cataInfo.setId(jsonObj.getString("id"));
					cataInfo.setCatagoryName(jsonObj.getString("name"));
					cataInfo.setAlbumAmount(Integer.parseInt(jsonObj.getString("album_amount")));
					resArrCata.add(cataInfo);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v(Constants.LOG_TAG, e.toString());
		}
		return resArrCata;
	}
	/**
	 * 解析应用版本字符串
	 * @param strJson
	 * @return
	 */
	public BtAppInfo parseAppInfo(String strJson) {
		BtAppInfo resAppInfo = new BtAppInfo();
		try {
			JSONObject appInfoObj = new JSONObject(strJson);

			if (appInfoObj != null) {
				resAppInfo.setAwardPoints(appInfoObj.getInt("awoard_points"));
				resAppInfo.setIsAddAds(appInfoObj.getBoolean("add_ads"));
				resAppInfo.setIsShowRealVer(appInfoObj
						.getBoolean("show_real_ver"));

				resAppInfo.setStrVerName(appInfoObj.getString("ver_name"));
				resAppInfo.setNewVerAddr(appInfoObj.getString("new_ver_addr"));

				if (appInfoObj.getString("update_tips").length() > 0) {
					resAppInfo.setStrUpdateTips(appInfoObj
							.getString("update_tips"));
					resAppInfo.setIsNeedUpdate(true);
				} else {
					resAppInfo.setStrUpdateTips("");
					resAppInfo.setIsNeedUpdate(false);
				}

				if (appInfoObj.getString("mormal_tips").length() > 0) {
					resAppInfo.setStrNormalTips(appInfoObj
							.getString("mormal_tips"));
					resAppInfo.setIsShowNormalTips(true);
				} else {
					resAppInfo.setIsShowNormalTips(false);
				}
				
				if (appInfoObj.getString("pay_point")!=null &&appInfoObj.getString("pay_point").length() > 0) {
					resAppInfo.setPayPoints(appInfoObj.getInt("pay_point"));
				} 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v(Constants.LOG_TAG, e.toString());
		}
		return resAppInfo;
	}

	
	/**
	 * 解析专辑信息
	 * @param jsonStr
	 * @return
	 * @throws JSONException 
	 */
	public  ArrayList<AlbumInfo> albumParser(String strJson){
		ArrayList<AlbumInfo> resArr=new ArrayList<AlbumInfo>();	
		if(strJson==null){
			return null;
		}
		try {
			JSONArray jsonAlbums = new JSONArray(strJson);

			// if there are subAlbums,then parser sub albums
			if (jsonAlbums != null) {
				for (int i = 0; i < jsonAlbums.length(); i++) {
					JSONObject jsonObj = jsonAlbums.getJSONObject(i);
					AlbumInfo mAlbumInfo = new AlbumInfo();
					mAlbumInfo.setId(jsonObj.optString("id"));
					mAlbumInfo.setmAlbumName(jsonObj.optString("name"));
					mAlbumInfo.setPicAmount(jsonObj.optString("image_num"));
					mAlbumInfo.setCategoryId(jsonObj.optString("cata_id"));
					// mAlbumInfo.setCommentAmount(jsonObj.optString("albumCommentAmount"));
					// mAlbumInfo.setAlbumDir(jsonObj.optString("albumDir"));
					mAlbumInfo.setIconName(jsonObj.optString("icon_name"));
					mAlbumInfo.setDownLoadAmount(jsonObj
							.optString("download_num"));
					mAlbumInfo.setPraiseAmount(jsonObj.optString("praise_num"));
					mAlbumInfo.setClickAmount(jsonObj.optString("click_num"));
					mAlbumInfo.setSubAlbumAmount(Integer.parseInt(jsonObj.optString("sub_album_amount")));
					String jsonSubAlbums = jsonObj.optString("sub_albums");
					if (jsonSubAlbums!=null && jsonSubAlbums.length()>0) {
						mAlbumInfo.setArrSubAlbumInfo(albumParser(jsonSubAlbums));
					}
					resArr.add(mAlbumInfo);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v(Constants.LOG_TAG, e.toString());
		}
		return resArr;
	}
	/**
	 * 解析服务器端返回的imageInfo json串
	 * @param strJson
	 * @return
	 */
	public  ArrayList<ImageInfo> imageInfoParser(String strJson){
		ArrayList<ImageInfo> resArr=new ArrayList<ImageInfo>();	
		if(strJson==null){
			return null;
		}
		
		try {
			JSONArray jsonImageInfo = new JSONArray(strJson);
			// if there are subAlbums,then parser sub albums
			if (jsonImageInfo != null) {
				for (int i = 0; i < jsonImageInfo.length(); i++) {
					JSONObject jsonObj = jsonImageInfo.getJSONObject(i);
					ImageInfo mImageInfo = new ImageInfo();
					mImageInfo.setImgId(jsonObj.optString("id"));
					mImageInfo.setImgName(jsonObj.optString("name"));
					mImageInfo.setAlbumId(jsonObj.optString("album_id"));
					//mImageInfo.setCataId(jsonObj.optString("cata_id"));
					resArr.add(mImageInfo);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v(Constants.LOG_TAG, e.toString());
		}
		return resArr;
	}
}
