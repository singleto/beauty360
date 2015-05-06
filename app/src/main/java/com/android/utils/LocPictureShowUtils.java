/*
 * 此类是读取图片辅助类
 */
package com.android.utils;

import java.io.File;

import com.android.info.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LocPictureShowUtils {
	private String[] filesName = null;// 存储每张图片的路径

	public LocPictureShowUtils() {
		try {
			File file = new File(Constants.LOCAL_PATH);
			filesName = file.list();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			filesName = null;
		}
	}

	public int count() {
		if (filesName != null) {
			return filesName.length;
		}
		return 0;
	}

	public String[] getFilesName() {
		return filesName;
	}

	public Bitmap getImageAt(int index) {
		String path = Constants.LOCAL_PATH;
		if (index > filesName.length) {
			return null;
		}
		path += filesName[index];
		if (getImageFile(path)) {

			/*
			 * BitmapFactory.Options opts = new BitmapFactory.Options();
			 * opts.inJustDecodeBounds = true; BitmapFactory.decodeFile(path,
			 * opts); opts.inSampleSize = computeSampleSize(opts, -1, 128 *
			 * 128); opts.inJustDecodeBounds = false; try { Bitmap bt =
			 * BitmapFactory.decodeFile(path, opts); return bt; } catch
			 * (OutOfMemoryError err) { err.printStackTrace(); }
			 */
			Bitmap bt = BitmapFactory.decodeFile(path);
			return bt;
		}
		return null;

	}

	private boolean getImageFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}
}
