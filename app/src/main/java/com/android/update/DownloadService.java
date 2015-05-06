package com.android.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import com.android.beauty360.R;
//import com.android.download.HttpDownloader;
import com.android.info.Constants;
import com.android.info.UrlParams;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadService extends Service {
	private static final int NOTIFY_DOW_ID = 0;
	private static final int NOTIFY_OK_ID = 1;

	private Context mContext = this;
	private int progress;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private DownloadBinder binder = new DownloadBinder();
	private String updateUrl="";
	//private String updateSaveName="360beauty.apk";
	
	private int fileSize; // 文件大小
	private int readSize; // 读取长度
	private int downSize; // 已下载大小
	private File downFile; // 下载的文件

	public enum versionInfoField {
		filename, filetype, version, description
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 更新进度
				// RemoteViews contentView = mNotification.contentView;
				mNotification.contentView.setTextViewText(R.id.downloadRate,
						msg.arg1 + "%");
				mNotification.contentView.setProgressBar(R.id.downloadProgress,
						100, msg.arg1, false);

				// 更新UI
				mNotificationManager.notify(NOTIFY_DOW_ID, mNotification);

				break;
			case 1:
				mNotificationManager.cancel(NOTIFY_DOW_ID);
				createNotification(NOTIFY_OK_ID);

				/* 打开文件进行安装 */
				openFile(downFile);
				break;
			case 2:
				mNotificationManager.cancel(NOTIFY_DOW_ID);
				break;
			}
		};
	};

	private Handler handMessage = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(
						mContext,
						DownloadService.this.getResources().getString(
								R.string.connect_serv_fail_tip),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(
						mContext,
						DownloadService.this.getResources().getString(
								R.string.connect_serv_fail_tip),
						Toast.LENGTH_SHORT).show();
				break;
			}

			handler.sendEmptyMessage(2);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		//this.updateSaveName=this.getResources().getString(R.string.app_name) + ".apk";
		if(Constants.urlParams!=null){
			this.updateUrl=Constants.REQ_SERV + "?"+ Constants.urlParams.getUpdateParam();
		}else{
			Constants.urlParams=new UrlParams(this);
			this.updateUrl=Constants.REQ_SERV + "?"+ Constants.urlParams.getUpdateParam();
		}
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// 返回自定义的DownloadBinder实例
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 创建通知
	 */
	private void createNotification(int notifyId) {

		//String updateSaveName = this.getResources().getString(
//				R.string.update_start_download)
//				+ this.getResources().getString(R.string.app_name) + ".apk";

		switch (notifyId) {
		case NOTIFY_DOW_ID:
			int icon = android.R.drawable.stat_sys_download;// R.drawable.save;
			CharSequence tickerText = this.getResources().getString(
					R.string.update_start_download)
					+ Constants.UPDATE_SAVE_NAME.substring(0,
							Constants.UPDATE_SAVE_NAME.lastIndexOf("."));
			long when = System.currentTimeMillis();
			mNotification = new Notification(icon, tickerText, when);

			// 放置在"正在运行"栏目中
			mNotification.flags = Notification.FLAG_ONGOING_EVENT;

			RemoteViews contentView = new RemoteViews(
					mContext.getPackageName(),
					R.layout.download_notification_layout);
			// System.out.println("versionInfo is:"+versionInfo);
			/*
			 * contentView.setTextViewText(R.id.updateFileName, "正在下载：" +
			 * versionInfo.get(versionInfoField.filename.toString()) + "." +
			 * versionInfo.get(versionInfoField.filetype.toString()));
			 */
			contentView.setTextViewText(
					R.id.updateFileName,
					this.getResources().getString(R.string.update_downloading)
							+ Constants.UPDATE_SAVE_NAME.substring(0,
									Constants.UPDATE_SAVE_NAME.lastIndexOf(".")));

			// 指定个性化视图
			mNotification.contentView = contentView;

			// Intent intent = new Intent(this, WelcomeActivity.class);
			Intent intent = new Intent();// 用户在通知栏中点击后，什么都不做
			PendingIntent contentIntent = PendingIntent.getActivity(mContext,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// 指定内容意图
			mNotification.contentIntent = contentIntent;

			break;
		case NOTIFY_OK_ID:
			int icon2 = android.R.drawable.stat_sys_download;// R.drawable.save;
			CharSequence tickerText2 = this.getResources().getString(
					R.string.update_download_done);
			long when2 = System.currentTimeMillis();
			mNotification = new Notification(icon2, tickerText2, when2);

			// 放置在"通知形"栏目中
			mNotification.flags = Notification.FLAG_AUTO_CANCEL;
			PendingIntent contentInten2 = PendingIntent.getActivity(mContext,
					0, new Intent(), 0);
			mNotification.setLatestEventInfo(
					mContext,
					this.getResources()
							.getString(R.string.update_download_done),
					this.getResources().getString(
							R.string.update_file_download_done), contentInten2);
			stopSelf();// 停掉服务自身
			Toast.makeText(
					DownloadService.this,
					this.getResources()
							.getString(R.string.update_download_done),
					Toast.LENGTH_SHORT).show();

			break;
		}

		// 最后别忘了通知一下,否则不会更新
		mNotificationManager.notify(notifyId, mNotification);
	}

	/**
	 * 下载模块
	 */
	private void startDownload(String strUrl) {
		
		// 初始化数据
		fileSize = 0;
		readSize = 0;
		downSize = 0;
		progress = 0;

		InputStream is = null;
		FileOutputStream fos = null;

		try {
			HttpEntity entity = getEntity(strUrl);
			// InputStream is=HttpDownloader.getInputStreamFromUrl(strUrl);
			// long length = entity.getContentLength();
			if (null != entity) {
				is = entity.getContent();
				fileSize = (int) entity.getContentLength();
			}
			// URL myURL = new URL(dowUrl); //取得URL
			// URLConnection conn = myURL.openConnection(); //建立联机
			// conn.connect();
			// fileSize = conn.getContentLength(); //获取文件长度
			// is = conn.getInputStream(); //InputStream 下载文件

			if (is != null) {
				// 建立临时文件
				// downFile = File.createTempFile("beauty360","apk");
				File downFile = new File(
						Environment.getExternalStorageDirectory(),
						Constants.UPDATE_SAVE_NAME);
				fos = new FileOutputStream(downFile);
				byte buf[] = new byte[1024 * 1024];
				while ((readSize = is.read(buf)) > 0) {
					fos.write(buf, 0, readSize);
					downSize += readSize;
					sendMessage(0);
				}
				// 将文件写入临时盘
				fos.flush();
				handler.sendEmptyMessage(1);
			}
		} catch (MalformedURLException e) {
			handMessage.sendEmptyMessage(0);
		} catch (IOException e) {
			handMessage.sendEmptyMessage(1);
		} catch (Exception e) {
			handMessage.sendEmptyMessage(0);
		} finally {
			try {
				if (null != fos)
					fos.close();
				if (null != is)
					is.close();
			} catch (IOException e) {
				// e.printStackTrace();
				Log.v(Constants.LOG_TAG, "" + e.toString());
			}
		}
	}

	public HttpEntity getEntity(String strUrl) throws Exception {
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

	public void sendMessage(int what) {
		int num = (int) ((double) downSize / (double) fileSize * 100);

		if (num > progress + 1) {
			progress = num;

			Message msg0 = handler.obtainMessage();
			msg0.what = what;
			msg0.arg1 = progress;
			handler.sendMessage(msg0);
		}
	}

	// 在手机上打开文件的method
	private void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Constants.UPDATE_SAVE_NAME)),
				"application/vnd.android.package-archive");

		startActivity(intent);

		/*
		 * //调用getMIMEType()来取得MimeType String type = getMIMEType(f);
		 * //设定intent的file与MimeType intent.setDataAndType(Uri.fromFile(f),type);
		 * startActivity(intent);
		 */
	}

	// 判断文件MimeType的method
	/*
	 * private String getMIMEType(File f) { String type = ""; String fName =
	 * f.getName(); // 取得扩展名 String
	 * end=fName.substring(fName.lastIndexOf(".")+1,
	 * fName.length()).toLowerCase();
	 * 
	 * // 按扩展名的类型决定MimeType
	 * if(end.equals("m4a")||end.equals("mp3")||end.equals("mid"
	 * )||end.equals("xmf")||end.equals("ogg")||end.equals("wav")) { type =
	 * "audio"; } else if(end.equals("3gp")||end.equals("mp4")) { type =
	 * "video"; } else
	 * if(end.equals("jpg")||end.equals("gif")||end.equals("png")
	 * ||end.equals("jpeg")||end.equals("bmp")) { type = "image"; } else
	 * if(end.equals("apk")) { // android.permission.INSTALL_PACKAGES type =
	 * "application/vnd.android.package-archive"; } else { type="*"; }
	 * //如果无法直接打开，就跳出软件清单给使用者选择 if(!end.equals("apk")) { type += "/*"; }
	 * 
	 * return type; }
	 */
	/*
	 * //SAX解析resourceUrl 页面的内容 public Map<String, String> getXMLElements(String
	 * resourceUrl) throws MalformedURLException, IOException, Exception {
	 * //获取XML URL url = new URL(resourceUrl); InputSource is = new
	 * InputSource(url.openStream()); is.setEncoding("UTF-8");
	 * 
	 * //解析XML SAXParserFactory spf = SAXParserFactory.newInstance(); SAXParser
	 * saxParser = spf.newSAXParser(); //创建解析器 ParsingXMLElements handler = new
	 * ParsingXMLElements(); saxParser.parse(is, handler); return
	 * handler.getElement(); }
	 */

	/**
	 * DownloadBinder中定义了一些实用的方法
	 * 
	 * @author user
	 */
	public class DownloadBinder extends Binder {
		/**
		 * 开始
		 */
		public void start() {
			new Thread() {
				public void run() {
					createNotification(NOTIFY_DOW_ID); // 创建通知
					startDownload(updateUrl);//
					// startDownload(Constants.verInfo.getStrUpdateUrl());

				};
			}.start();
		}

		/**
		 * 获取进度
		 * 
		 * @return
		 */
		/*
		 * public int getProgress() { return progress; }
		 */

		/*        *//**
		 * 检查当前系统是否是最新版本
		 * 
		 * @return 1：当前最新版本 0：发现新的版本可更新 -1：检测新版本失败
		 */
		/*
		 * public int isNewVersion() { try { versionInfo =
		 * getXMLElements(serverUrl + xmlName); pastVersion =
		 * getPackageManager().getPackageInfo(mContext.getPackageName(),
		 * 0).versionName;
		 * 
		 * return null == versionInfo || null == versionInfo.get("version") ||
		 * null == pastVersion ||
		 * pastVersion.equals(versionInfo.get(versionInfoField
		 * .version.toString())) ? 1 : 0; } catch (NameNotFoundException e) {
		 * e.printStackTrace(); } catch (MalformedURLException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace();
		 * handMessage.sendEmptyMessage(0); } catch (Exception e) {
		 * e.printStackTrace(); }
		 * 
		 * return -1; }
		 */

		/**
		 * 获取最新版本的信息
		 * 
		 * @return
		 */
		/*
		 * public Map<String, String> getVersionInfo() { return versionInfo; }
		 */
	}
}
