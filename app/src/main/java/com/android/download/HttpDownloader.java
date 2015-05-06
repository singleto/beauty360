package com.android.download;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import com.android.utils.MyUtils;
import com.android.info.Constants;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
public class HttpDownloader {

/*	*//**
	 * ����URL�����ļ�,ǰ��������ļ����е��������ı�,�����ķ���ֵ�����ı����е�����
	 * 1.����һ��URL����
	 * 2.ͨ��URL����,����һ��HttpURLConnection����
	 * 3.�õ�InputStream
	 * 4.��InputStream���ж�ȡ����
	 * @param urlStr:�����ļ���ַ
	 * @param path:ָ�����ص�SD���ϵ��ļ�Ŀ¼
	 * @return ���浽SD�����ļ�·��
	 *//*
	public static String downloadFile(String urlStr,String path){
		
		int start = urlStr.lastIndexOf("/");
		int end = urlStr.length();
		String fileName = urlStr.substring(start,end);//��ȡ�ļ�����Ϊ���ص�SD���ϵ��ļ���
		
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			urlConn.connect();//һ��Ҫ���ϣ�����urlConn.getInputStream()����
			urlConn.setConnectTimeout(6000);
			InputStream inputStream = urlConn.getInputStream();
			File resultFile = FileUtils.write2SDFromInput(path, fileName, inputStream);
			
			if(resultFile == null){
					return null;
			}
			return resultFile.getAbsolutePath();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
				if (null != urlConn)
					urlConn.disconnect();
		}
		return null;
	}
*/
	
	// ��װ��һ������������һ����ַURL�ַ���������InputStream����
/*	public static InputStream getInputStreamFromUrl(Activity mActivity,
			String urlStr) throws MalformedURLException, IOException {
		URL url = new URL(urlStr);
		Log.v(Constants.LOG_TAG, "start opengConnection");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		//HttpURLConnection urlConn = getUrlConn(mActivity, urlStr);

		// urlConn.setConnectTimeout(5 * 1000);// �������ӳ�ʱ
		// if (urlConn.getResponseCode() != 200)
		// throw new RuntimeException("����urlʧ��");
		HttpEntity mEntity = null;
		try {
			mEntity = getEntity(urlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStream inputStream = urlConn.getInputStream();
		//InputStream inputStream = mEntity.getContent();
		return inputStream;
	}
*/
	// ��װ��һ������������һ����ַURL�ַ���������String����
	public static String getResFromServer(String urlStr) {
		
		MyUtils.outLog("request url is:"+urlStr);
		String strRes = "";//���صĽ��
		String readLine = null;
		InputStream inputStream;
		InputStreamReader isr;
		BufferedReader bufferReader;
		
		try {

			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setConnectTimeout(5 * 1000);// �������ӳ�ʱ
			// urlConn.setReadTimeout(10*1000);//���ö�ȡ��ʱ

			if (urlConn.getResponseCode() != 200) {
				Log.d(Constants.LOG_TAG,
						"getStringInputStreamFromUrl strUrl is:" + url);
				return null;
			}
			inputStream = urlConn.getInputStream();
			isr = new InputStreamReader(inputStream,"UTF-8");
			bufferReader = new BufferedReader(isr);
			while ((readLine = bufferReader.readLine()) != null) {
				strRes += readLine;
			}

			isr.close();
			urlConn.disconnect();
		} catch (Exception e) {
			Log.v(Constants.LOG_TAG, e.toString());
		} 
		Log.v(Constants.LOG_TAG, strRes);
		return strRes;
	}
	/**
	 * ����URL�����ļ���ǰ��������ļ����е��������ı��������ķ���ֵ�����ļ����е����� 1.����һ��URL����
	 * 2.ͨ��URL���󣬴���һ��HttpURLConnection���� 3.�õ�InputStram 4.��InputStream���ж�ȡ����
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String downloadFile(Activity mActivity, String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// ����һ��URL����
			
/*			 * URL url = new URL(urlStr); // ����һ��Http���� HttpURLConnection
			 * urlConn = (HttpURLConnection) url .openConnection();
			 * urlConn.setConnectTimeout(5* 1000);//�������ӳ�ʱ if
			 * (urlConn.getResponseCode() != 200) throw new
			 * RuntimeException("����urlʧ��"); // ʹ��IO����ȡ���� buffer = new
			 * BufferedReader(new InputStreamReader(urlConn .getInputStream()));*/
			 
			InputStream mInputStream= getInputStreamFromUrl(urlStr);
			if(null==mInputStream){
				return null;
			}
			
			buffer = new BufferedReader(new InputStreamReader(mInputStream,"GBK"));

			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			Log.v(Constants.LOG_TAG, ""+e.toString());
			return null;
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}


	
	public static String downloadFile(Activity mActivity, String urlStr,
			String encode) {
		StringBuffer sb = new StringBuffer();
		BufferedReader buffer = null;
		int ch = 0;
		try {
			buffer = new BufferedReader(new InputStreamReader(
					getInputStreamFromUrl(urlStr), encode));
			while ((ch = buffer.read()) > -1) {
				sb.append((char) ch);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


	public static HttpEntity getEntity(String strUrl) throws Exception {
		BasicHttpParams localBasicHttpParams = new BasicHttpParams();
		HttpVersion localHttpVersion = HttpVersion.HTTP_1_1;
		HttpProtocolParams.setVersion(localBasicHttpParams, localHttpVersion);
		// HttpProtocolParams
		// .setContentCharset(localBasicHttpParams, "ISO-8859-1");
		HttpProtocolParams.setUseExpectContinue(localBasicHttpParams, true);
		HttpConnectionParams.setConnectionTimeout(localBasicHttpParams,
				50 * 5000);
		HttpConnectionParams.setSoTimeout(localBasicHttpParams, 50 * 5000);
		HttpConnectionParams.setTcpNoDelay(localBasicHttpParams, true);
		SchemeRegistry localSchemeRegistry = new SchemeRegistry();
		PlainSocketFactory localPlainSocketFactory = PlainSocketFactory
				.getSocketFactory();
		Scheme localScheme1 = new Scheme("http", localPlainSocketFactory, 80);
		localSchemeRegistry.register(localScheme1);
		SSLSocketFactory localSSLSocketFactory = SSLSocketFactory
				.getSocketFactory();
		X509HostnameVerifier localX509HostnameVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
		localSSLSocketFactory.setHostnameVerifier(localX509HostnameVerifier);
		Scheme localScheme2 = new Scheme("https", localSSLSocketFactory, 443);
		localSchemeRegistry.register(localScheme2);
		ThreadSafeClientConnManager localThreadSafeClientConnManager = new ThreadSafeClientConnManager(
				localBasicHttpParams, localSchemeRegistry);
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(
				localThreadSafeClientConnManager, localBasicHttpParams);
		HttpClient localHttpClient = localDefaultHttpClient;
		HttpGet httpGet = new HttpGet(strUrl);
		httpGet.addHeader("Accept-Encoding", "gzip, deflate");
		HttpResponse response = localHttpClient.execute(httpGet);
		HttpEntity mEntity = response.getEntity();
		return mEntity;
	}
	
	
	 

	public static InputStream getInputStreamForTest(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream=null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			//urlConn.connect();//һ��Ҫ���ϣ�����urlConn.getInputStream()����
			//urlConn.setConnectTimeout(6000);
			inputStream = urlConn.getInputStream();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
/*		finally{
				if (null != urlConn)
					urlConn.disconnect();
		}*/
		return inputStream;
	}

	public static InputStream getInputStreamFromUrl(String url) {
		InputStream is=null;
		//String smthEncoding = "GBK";
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.4) Gecko/20091016 Firefox/3.5.4";
		DefaultHttpClient httpClient;
		//int threadNum;
		//ExecutorService execService;
		//boolean destroy;

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 10);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		httpClient = new DefaultHttpClient(cm, params);
		// ����
		 httpClient.setHttpRequestRetryHandler(new
		 DefaultHttpRequestRetryHandler(3, false));
		// ��ʱ����
		 httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,
		 5000);
		// httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
		// 10000);
		httpClient.getParams()
				.setParameter(ClientPNames.HANDLE_REDIRECTS, true);

		//threadNum = 10;
		//execService = Executors.newFixedThreadPool(threadNum);
		//destroy = false;

		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", userAgent);
		//httpget.addHeader("Accept-Encoding", "gzip, deflate");
		//String content;
		try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (IOException e) {
			Log.d(Constants.LOG_TAG, "strUrl is:"+url);
			Log.d(Constants.LOG_TAG, "get url failed,", e);
			//content = null;
			is=null;
		}
		//return content;
		return is;
	}

	/**
	 * ����URL������������ͼƬ
	 */
	public Bitmap downloadPic(String urlStr) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {

			is = getInputStreamFromUrl(urlStr);
			//��׳�Դ�����ȡ�ļ�ʧ�ܣ�����null
			if(null == is){
				return null;
			}

			// BufferedInputStream bis = new
			// BufferedInputStream(is);//����������װ��ӵ���ڲ�������BufferedInputStream
			/**
			 * ͨ���ı�ͼƬ�ֱ�������ֹ�������
			 */
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds=true;
			/*
			 * if (is == null){ throw new RuntimeException("stream is null");
			 * }else{
			 */
			try {
				byte[] data = MyUtils.readStream(is);
				if (data != null) {
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
				}
			} catch (Exception e) {
				Log.v(Constants.LOG_TAG, ""+e);
				return null;
			}

			// bitmap = BitmapFactory.decodeStream(is);
			// options.inJustDecodeBounds=false;
			// options.inSampleSize=1;
			// bitmap = BitmapFactory.decodeStream(is,null,options);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				Log.v(Constants.LOG_TAG, ""+e);
				return null;
			}
		}
		return bitmap;
	}

	/**
	 * ����URL������������ͼƬ
	 */
	public static Bitmap downloadIcon(String urlStr) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {

			Log.v(Constants.LOG_TAG, "downloadIcon urlStr is:"+urlStr);
			is = getInputStreamFromUrl(urlStr);
			
			//��׳����ǿ
			if(null==is){
				return null;
			}
			// BufferedInputStream bis = new
			// BufferedInputStream(is);//����������װ��ӵ���ڲ�������BufferedInputStream
			/**
			 * ͨ���ı�ͼƬ�ֱ�������ֹ�������
			 */
			BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
			try {
				byte[] data = MyUtils.readStream(is);
				//Log.v(Constants.LOG_TAG, "start get icon data.lenth is:"+data.length);
				//bitmap = BitmapFactory.decodeStream(is, null, options);

				if (data != null) {
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length,options);
//					Log.v(Constants.LOG_TAG, "start get icon bitmap is:"+bitmap);
				}
			} catch (Exception e) {
				Log.v(Constants.LOG_TAG, "downloadIcon :"+e);
				return null;			}

			// bitmap = BitmapFactory.decodeStream(is);
			// options.inJustDecodeBounds=false;
			// options.inSampleSize=1;
			// bitmap = BitmapFactory.decodeStream(is,null,options);
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				Log.v(Constants.LOG_TAG, "downloadIcon:"+e);
				return null;
			}
		}
		return bitmap;
	}
	
	/**
	  * �ж�Զ���ļ��Ƿ����
	  * 
	  * @param url
	  * @return true:�ļ����ڡ� false���ļ�������
	  */
	public static boolean fileIsExist(String URLstr) {
		boolean b = false;
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// HTTP_CONNECT_TIMEOUT
		HttpGet httpGet = new HttpGet(URLstr);
		try {
			Log.v(Constants.LOG_TAG, "00");

			HttpResponse response = httpClient.execute(httpGet);
			Log.v(Constants.LOG_TAG, "01");

			int code = response.getStatusLine().getStatusCode();
			Log.v(Constants.LOG_TAG, "02");

			if (code == 200)
				b = true;
		} catch (Exception e) {
			Log.v(Constants.LOG_TAG,
					"&&&&&&&&&&&&&&&&--fileIsExist()--&&&&&&&&&&&&="
							+ e.toString());
		}
		Log.v(Constants.LOG_TAG, "...fileIsExist() return= " + b);
		return b;
	}
	
	// �÷������ڸ���ͼƬ��URL��������������ͼƬ
	public static Bitmap loadImageFromUrl(String imageUrl,Handler myHandler) {
		int fileSize = 0;
		int downSize = 0;
		int progress = 0;//���ڱ�ʶ���ؽ��ȿ��еĽ��ȣ�ʹ��.�ĸ�����ʶ
		try {
			// ����ͼƬ��URL������ͼƬ��������һ��Bitmap����
			Bitmap bitmap = null;
			InputStream inStream = null;
			ByteArrayOutputStream outStream = null;
			try {

				HttpEntity entity = HttpDownloader.getEntity(imageUrl);
				// ��׳�Դ�����ȡ����ʧ�ܣ�����null
				if(null==entity){
					return null;
				}
				inStream = entity.getContent();

				fileSize = (int) entity.getContentLength();
				// ��׳�Դ�����ȡ�ļ�ʧ�ܣ�����null
				if (null == inStream) {
					return null;
				}
				try {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0;
					//int progress=0;//���ڱ�ʶ���ؽ��ȿ��еĽ��ȣ�ʹ�á���ʶ
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						downSize += len;

						//int num = (int) ((double) downSize / (double) fileSize * 100);

					//	if (num > progress + 1) {
							progress++;//���ȼ�һ
							//progress = num;
							Message message = myHandler.obtainMessage();
							message.what = 2;
							message.arg1 = progress;
							myHandler.sendMessage(message);
					//	}
					}
					outStream.flush();
					outStream.close();
					inStream.close();

					byte[] data = outStream.toByteArray();
					if (data != null) {
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
					}

				} catch (Exception e) {
					Log.v(Constants.LOG_TAG, "" + e);
					return null;
				}

			} finally {
				try {
					if (null != outStream)
						outStream.close();
					if (null != inStream)
						inStream.close();
				} catch (Exception e) {
					Log.v(Constants.LOG_TAG, "" + e);
					return null;
				}
			}
			return bitmap;
		} catch (Exception e) {
			//throw new RuntimeException(e);
			Log.v(Constants.LOG_TAG, "" + e);
			return null;
		}
	}

	

}
