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

	//static ArrayList<File> mFileList;//���ڱ���Ŀ¼�µ������ļ�

	
	public FileUtils() {

	}
	/**
	 * ��������ϵĿ��пռ�
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
	 * ��SD���ϴ����ļ�
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
	 * ɾ���ļ�
	 * 
	 * @param fileName
	 */
	public void deleteFile(String fileName) {
		File file = new File(Constants.LOCAL_PATH + fileName);
		file.delete();
	}

	/**
	 * ��SD���Ĺ���Ŀ¼�´���Ŀ¼
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
	 * ��SD���ϴ�������Ŀ¼
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
	 * �ж�SD���ϵ��ļ��л��ļ��Ƿ����
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * ��������ͼƬ���ļ������ɱ��ش洢���ļ��������ļ��Ѵ��ڣ� ����������������+"_"+Դ�ļ������Է�ֹ�������˸���ͼƬ�� ��ԭ���ı���ͼƬ����
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
	 * ��һ��InputStream���������д�뵽SD����
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
	 * ��һ��InputStream���������д�뵽SD����
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
	 * ��һ���ַ������浽�ļ���
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

	//��SD�е��ļ�  
	public static String readFileSdcardFile(String fileName) {
		String res = "";
		
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			fin.close();
			res = EncodingUtils.getString(buffer,"GB2312");
			//res = new String(buffer, "GB2312");//֧��˫�ֽ��ַ�
		    Log.v(Constants.LOG_TAG, "res is:"+res);
		    //myApp.setCharNumofString(res.length());//�洢���ַ���
		} catch (Exception e) {
			Log.v(Constants.LOG_TAG, ""+e.toString());
		}
		return res;
	}

	/*
	 * �Զ�����ͼƬ
	 */
	public static boolean autoSaveImage(String fileDir, String fileUrl,
			Bitmap mImage) throws IOException {
		
		boolean bRes=false;
		FileOutputStream mOutPutStream = null;
		File mFile = null;
		File mFileDir = null;//new File(fileDir);

		if (/*false == Constants.verInfo.autoSave ||*/ null == mImage) {// �������û�������Զ����棬����image==null,��ֱ�ӷ���
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
	 * �滻�ַ���ͨ�÷��� Դ�ִ���Ҫ�滻Դ�ִ�,�滻Ϊ��Ŀ���ִ�
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
	 * ����ͼƬ������,�������ʧ�ܷ���null�����򷵻ش�����File
	 * 
	 * @param fileName
	 * @param mImage
	 * @throws IOException
	 */
	public static File saveImageToLocal(String fileName, Bitmap mImage)
			throws IOException {
		File mFile = null;
		// ����Դ�ļ������ɱ����ļ�������ҪĿ���Ƿ�ֹ�ļ����������Ǳ����ļ�
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
	 * ɾ��һ��Ŀ¼�������Ƿǿ�Ŀ¼�� 
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
	            delDir(file);// �ݹ�   
	        }  
	    }  
	    dir.delete();  
	    return true;  
	} 
	

/**
 * ����Ŀ¼�µ������ļ������ô˺���֮ǰ����Ҫ������һ��ȫ�ֱ���
 * @param file    //&& false == f.getName().contains("icon")
 * @return
 */
	public static ArrayList<File> getFiles(File dir){
		ArrayList<File> mFileList =  new ArrayList<File>(); ;//���ڱ���Ŀ¼�µ������ļ�
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
				for(int i =0;i<mTempFileList.size();i++){//�����ص��ļ����ӵ�
					mFileList.add(mTempFileList.get(i));
				}
			}
		}
		return mFileList;
	}
	
	/**  
	 * * �޸��ļ�������޸�ʱ��  
	 * * @param dir  
	 * * @param fileName  
	 * */ 
	public static void updateFileTime(String fileSdUrl){     
		File file = new File(fileSdUrl);
		long newModifiedTime =System.currentTimeMillis();   
		file.setLastModified(newModifiedTime); 
	}

	/**  
	 * ����洢Ŀ¼�µ��ļ���С�����ļ��ܴ�С���ڹ涨��CACHE_SIZE����sdcardʣ��ռ�С��
	 * FREE_SD_SPACE_NEEDED_TO_CACHE�Ĺ涨  
	 * ��ôɾ��40%���û�б�ʹ�õ��ļ�  
	 * @param dirPath  
	 * @param filename  
	 * float delScale ����the proportion of all the cathe files
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
			List<File> fileList=getFile(f);//��������ϼ��ص�Adapter�� ��ListView����ʾ������
		}
	//�ݹ��ѯSD����.mp3�ļ� ����һ��File���

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
	 * * ɾ�������ļ�  
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
 * * TODO �����ļ�������޸�ʱ��������� *  
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

