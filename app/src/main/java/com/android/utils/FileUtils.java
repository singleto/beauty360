package com.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.http.util.EncodingUtils;

import com.android.info.Constants;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtils {

	//static ArrayList<File> mFileList;//用于遍历目录下的所有文件

	
	public FileUtils() {

	}
	/**
	 * 计算磁盘上的空闲空间
	 */
	public static int MB = 1024 * 1024;
	public static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) throws IOException {
		File mFile = creatSDDir();
		if (null != mFile) {
			mFile = new File(Constants.LOCAL_PATH + fileName);
			mFile.createNewFile();
		}
		return mFile;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */
	public void deleteFile(String fileName) {
		File file = new File(Constants.LOCAL_PATH + fileName);
		file.delete();
	}

	/**
	 * 在SD卡的工作目录下创建目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(Constants.LOCAL_PATH + dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 在SD卡上创建工作目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir() {
		File dir = new File(Constants.LOCAL_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹或文件是否存在
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 根据网络图片的文件名生成本地存储的文件名—若文件已存在， 则重新命名：数字+"_"+源文件名，以防止服务器端更新图片后， 将原来的本地图片覆盖
	 */
	public static String createFileName(String fileName) {
		int i = 1;
		while (isFileExist(Constants.LOCAL_PATH+i + "_" + fileName)) {
			i++;
		}
		;
		return i + "_" + fileName;
	}

/*	*//**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 *//*
	public static File write2SDFromInput(String path,String fileName,InputStream input){
		
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			while((input.read(buffer)) != -1){
				System.out.println("buffer is:"+buffer.toString());
				output.write(buffer);
			}
			output.flush();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(null != output){
					output.close();
				}
				if(null != input){
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
*/
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
/*	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		int i=0;//for input stream
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			Log.v(Constants.LOG_TAG, "Write to SD");
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((i=input.read(buffer)) != -1) {
				output.write(buffer,0,i);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
*/	
	public static File writeFile2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;

		//int i=0;//for input stream
		OutputStream output = null;
		try {
			
            creatSDDir(path);   
            file = creatSDFile(path + fileName);   
            output = new FileOutputStream(file);  

            byte[] buffer = new byte[8*1024];
            int i=0,a=0;
            while((i=input.read(buffer)) != -1){   
            	a=a+i;
                output.write(buffer,0,i); 

            }   
            output.flush();   			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将一个字符串保存到文件中
	 * @param path
	 * @param fileName
	 * @param write_str
	 */
	public static void writeFileSdcardFile(String path, String fileName, String write_str) {
		//File file = null;
		FileOutputStream output = null;

		try {
			creatSDDir(path);
			//file = creatSDFile(path + fileName);
			//output = new FileOutputStream(file);
			output = new FileOutputStream(path + fileName);
			byte[] bytes = write_str.getBytes();
			output.write(bytes);
			output.close();
		} catch (Exception e) {
			Log.v(Constants.LOG_TAG, ""+e.toString());
		}
	}

	//读SD中的文件  
	public static String readFileSdcardFile(String fileName) {
		String res = "";
		
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			fin.close();
			res = EncodingUtils.getString(buffer,"GB2312");
			//res = new String(buffer, "GB2312");//支持双字节字符
		    Log.v(Constants.LOG_TAG, "res is:"+res);
		    //myApp.setCharNumofString(res.length());//存储总字符数
		} catch (Exception e) {
			Log.v(Constants.LOG_TAG, ""+e.toString());
		}
		return res;
	}

	/*
	 * 自动保存图片
	 */
	public static boolean autoSaveImage(String fileDir, String fileUrl,
			Bitmap mImage) throws IOException {
		
		boolean bRes=false;
		FileOutputStream mOutPutStream = null;
		File mFile = null;
		File mFileDir = null;//new File(fileDir);

		if (/*false == Constants.verInfo.autoSave ||*/ null == mImage) {// 如果程序没有设置自动保存，或者image==null,则直接返回
			return false;
		}


		// String strCacheFileUrl=
		// FileUtils.strReplace(fileUrl,".jpg",Constants.CACHE_SUFFIX);

		if (!Constants.SD_INSERTED
				|| Constants.SD_FREE_SIZE > FileUtils.freeSpaceOnSd()) {
			return false;
		}
		mFileDir = new File(fileDir);

		if (mFileDir == null || !mFileDir.exists() || mFileDir.isFile()) {
			mFileDir.mkdirs();
		}
		mFile = new File(fileUrl);
		mFile.createNewFile();

		try {
			mOutPutStream = new FileOutputStream(mFile);
		} catch (FileNotFoundException e) {
			bRes=false;
			e.printStackTrace();
		}

		if (null != mImage) {
			mImage.compress(Bitmap.CompressFormat.JPEG, 100, mOutPutStream);
			mOutPutStream.flush();
			mOutPutStream.close();
			bRes=true;

		}
		return bRes;
	}
	
	/**
	 * 替换字符的通用方法 源字串，要替换源字串,替换为的目的字串
	 * */
/*	public static String strReplace(String s, String org, String ob) {
		String newString = "";
		int first = 0;
		while (s.indexOf(org) != -1) {
			first = s.indexOf(org);
			if (first != s.length()) {
				newString = newString + s.substring(0, first) + ob;
				s = s.substring(first + org.length(), s.length());
			}
		}
		newString = newString + s;
		return newString;
	}*/

	/**
	 * 保存图片到本地,如果保存失败返回null，否则返回创建的File
	 * 
	 * @param fileName
	 * @param mImage
	 * @throws IOException
	 */
	public static File saveImageToLocal(String fileName, Bitmap mImage)
			throws IOException {
		File mFile = null;
		// 根据源文件名生成本地文件名，主要目的是防止文件重名，覆盖本地文件
		String localFileName = createFileName(fileName);

		mFile = creatSDFile(localFileName);
		FileOutputStream mOutPutStream = null;
		try {
			mOutPutStream = new FileOutputStream(mFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		mImage.compress(Bitmap.CompressFormat.JPEG, 100, mOutPutStream);
		try {
			mOutPutStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mFile;
	}

	/** 
	 * 删除一个目录（可以是非空目录） 
	 *  
	 * @param dir 
	 */  
	public static boolean delDir(File dir) {  
	    if (dir == null || !dir.exists() || dir.isFile()) {  
	        return false;  
	    }  
	    for (File file : dir.listFiles()) {  
	        if (file.isFile()) {  
	            file.delete();  
	        } else if (file.isDirectory()) {  
	            delDir(file);// 递归   
	        }  
	    }  
	    dir.delete();  
	    return true;  
	} 
	

/**
 * 遍历目录下的所有文件，调用此函数之前，需要先生成一个全局变量
 * @param file    //&& false == f.getName().contains("icon")
 * @return
 */
	public static ArrayList<File> getFiles(File dir){
		ArrayList<File> mFileList =  new ArrayList<File>(); ;//用于遍历目录下的所有文件
	    if (dir == null || !dir.exists() || dir.isFile()) {  
	        return null;  
	    } 
		File[] fileArray =dir.listFiles();
	    MyUtils.outLog(" fileArray size  is:"+fileArray.length);

		for (File f : fileArray) {
		    MyUtils.outLog(" f.getParent() is:"+f.getParent());
		    MyUtils.outLog(" f is file:"+f.isFile());
			if (f.isFile()) {
				mFileList.add(f);
				MyUtils.outLog(" mFileList size  is:"+mFileList.size());
			} else {
				ArrayList<File> mTempFileList=getFiles(f);
				for(int i =0;i<mTempFileList.size();i++){//将返回的文件增加到
					mFileList.add(mTempFileList.get(i));
				}
			}
		}
		return mFileList;
	}
	
	/**  
	 * * 修改文件的最后修改时间  
	 * * @param dir  
	 * * @param fileName  
	 * */ 
	public static void updateFileTime(String fileSdUrl){     
		File file = new File(fileSdUrl);
		long newModifiedTime =System.currentTimeMillis();   
		file.setLastModified(newModifiedTime); 
	}

	/**  
	 * 计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于
	 * FREE_SD_SPACE_NEEDED_TO_CACHE的规定  
	 * 那么删除40%最近没有被使用的文件  
	 * @param dirPath  
	 * @param filename  
	 * float delScale ——the proportion of all the cathe files
	 */ 
	public static void removeCache(String dirPath,float delScale ) {
		File dir = new File(dirPath);
		// File[] files = dir.listFiles();
		//ArrayList<File> mFileList = new ArrayList<File>();
		ArrayList<File> mArrFiles = getFiles(dir);
		
		if (mArrFiles == null || mArrFiles.size()==0) {
			return;
		}
		
		File[] files = mArrFiles.toArray(new File[mArrFiles.size()]);
		int removeFactor = (int) (delScale * files.length);
		Arrays.sort(files, new FileLastModifSort());
		for (int i = 0; i < removeFactor; i++) {
			if(files[i].renameTo(files[i])&&(!files[i].getPath().contains(Constants.DOWNLOAD_PATH))){   
				files[i].delete();
			}else{
				MyUtils.outLog("files[i] can't be delete path is:"+files[i].getPath());
			}
		}
	}

	/*
	public class Mp extends Activity {
		List<File> mFileList;
		public void onCreate(Bundle savedInstanceState) {
			mFileList=new ArrayList<File>();
			File f = new File("mnt/sdcard/");
			List<File> fileList=getFile(f);//将这个集合加载到Adapter里 在ListView里显示就行了
		}
	//递归查询SD卡上.mp3文件 返回一个File结合

	public List<File> getFile(File file){
		File[] fileArray =file.listFiles();
		for (File f : fileArray) {
			if(f.isFile()){
				if(".mp3".equalsIgnoreCase(getFileEx(f))){
					mFileList.add(f);
				}
			}else{
				getFile(f);
			}
		}
		return mFileList;
	}
	*/
	
	/**  
	 * * 删除过期文件  
	 * * @param dirPath  
	 * * @param filename  
	 * */ 
/*	public static void removeExpiredCache(String dirPath, String filename) {  
		File file = new File(dirPath,filename);      
		if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) {          
			Log.v(Constants.LOG_TAG, "Clear some expiredcache files ");          
			file.delete();     
		}  
	}*/ 
}
/**  
 * * TODO 根据文件的最后修改时间进行排序 *  
 * */ 
class FileLastModifSort implements Comparator<File> {
	public int compare(File arg0, File arg1) {
		if (arg0.lastModified() > arg1.lastModified()) {
			return 1;
		} else if (arg0.lastModified() == arg1.lastModified()) {
			return 0;
		} else {
			return -1;
		}
	}
}

