package com.android.info;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class UrlParams {

	public Context mContext;
	/**
	 * 
	 * @param mContext 
	 * @param reqType 请求类型
	 */
	public UrlParams(Context mContext) {
		super();
		this.mContext = mContext;
		this.paraInit();
	}

	// URL 参数名.
	//=================专用参数start===========
	public String BT_CATAGORY_ID="catagory_id";//类别id
	public String BT_ALBUM_ID="album_id";//专辑id
	public String BT_IMAGE_ID="image_id";//图片id
	public String BT_IMAGE_POS="image_pos";//图片在专辑中的位置
	
	//统一定义请求参数
	public String BT_UPDATE="update";//升级请求
	public String BT_START="bt_start";//开始请求
	public String BT_GET_CATAGORY="get_catagory";//获取分类信息请求
	public String BT_GET_ALBUMS="get_albums";//获取专辑信息请求
	public String BT_GET_SUB_ALBUMS="get_sub_albums";//获取子专辑信息请求

	public String BT_CUR_ALBUM_AMOUNT="cur_album_amount";//当前专辑数量，用于加载更多
	public String BT_IS_ALUMB_MORE="is_album_more";//用户是否加载完毕，显示更多选项
	public String BT_GET_IMAGE="get_image";//根据ID获取图片信息请求
	public String BT_GET_IMAGE_ARR="get_image_arr";//获取专辑的imageInfo类别，如果专辑中的imageInfo为空，则根据专辑ID获取imageInfo列表
	//public String BT_GET_IMAGE_NAMES="get_iamge_names";//获取专辑的ID获取所有的专辑名称
	public String BT_GET_ICON="get_icon";//获取专辑的图标；
	public String BT_VER_CTRL_NUM="ver_ctrl_num";//是否使用正式版本，主要是为了在安卓市场上发布所设置
	public String BT_AD_CTL_NUM="ad_ctrl_num";//是否展示广告，主要考虑到有些发布平台不允许有广告
	public String BT_DOWN_IMAGE="down_image";//下载图片
	public String BT_POINT_LOG="point_log";//积分获取日志；
	public String BT_PAY_INFO="pay_info";//充值记录；
	public String BT_USER_ID="bt_user_id";//用户id
	public String BT_USER_NAME="user_name";//用户名；
	public String BT_PAY_POINT="pay_point";//支付积分
	public String BT_PAY_ORDER_ID="pay_order_id";//支付订单id
	public String BT_PAY_GOODS_NAME="pay_goods_name";//支付商品名称
	public String BT_PAY_GOODS_DESC="pay_goods_desc";//支付商品描述
	public String BT_PAY_PRICE="pay_price";//支付金额	
	public String BT_UPDATE_POINT="update_point";//更新支付积分
	public String BT_PAY_NOTIFY="pay_notify";//支付通知
	public String GET_HELP_CONTENT = "get_help_content"; //获取帮助信息.

	//======================end==================
	
	//======================通用参数start==================
	public String REQ_TYPE = "req_type";//请求类型，比如启动，请求专辑，请求图片等等
	public String PHONE_NUM = "phone_num";//手机号码
	public String NETWORK_TYPE = "network_type";// 网络大类，比如GRPS，CDMA等
	public String CELL_ID = "cell_id";// 手机所在的小区基站的蜂窝ID，如没有获取到，值为-1
	public String LOC_ID = "loc_id";// 手机所在的小区ID，如没有获取到，值为-1
	public String LATITUDE = "latitude"; // 经度信息.如没有获取到，值为-1
	public String LONGITUDE = "longitude"; // 维度信息.如没有获取到，值为-1
	public String MCC = "mcc";// 运营商所属国家代码
	public String MNC = "mnc";// 运营商代码
	public String SPN = "spn";// 运营商的名称
	public String MAC_ADDRESS = "mac";// mac地址
	public String NET_TYPE = "net";// 访问的网络类型，比如CMNET，CMWAP
	public String DEVICE_SIM_IMSI = "imsi"; // 设备为手机时的SIM唯一识别码
	public String DEVICE_ID_NAME = "imei"; // 该设备的唯一ID (IMEI or
														// MEID).
	public String DEVICE_NAME = "device_name"; // 具体的设备名(iPod touch
															// 2G, iPhone
	// 3GS, ...)
	public String DEVICE_TYPE_NAME = "device_type"; // 平台类型
																	// (Android,
																	// iPhone,
	// iPad).
	public String DEVICE_OS_VERSION_NAME = "os_version"; // 操作系统版本.
	public String DEVICE_COUNTRY_CODE = "country_code"; // 国家代码.
	public String DEVICE_LANGUAGE = "language"; // 语言代码.
	public String PKG_NAME = "pkg_name"; // 包名.
	public String APP_VERSION_NAME = "app_version"; // 应用程序版本.
	public String DEVICE_SCREEN_WIDTH = "device_width"; // 布局大小.
	public String DEVICE_SCREEN_HEIGHT = "device_height"; // 布局大小.
	public String APP_VERSION_CODE = "app_version_code"; // 应用程序版本code.

	
	// static final String CONNECT_URL = Constants.SERVER_MAIN_URL;//访问的URL
	public String DEVICE_PLATFORM_TYPE = "android";//
	public String CURRENT_TIME = "at"; // 系统当前时间
	public String EMULATOR_DEVICE_ID = "emulatorDeviceId";
	//public String PREFERENCE = "appPrefrences";
	//public String REFERRAL_URL = "InstallReferral";

	//====================通用参数end==================
	// ==================专用参数值变量start=============
	private String cataId="";//类别ID
	private String albumId="";//专辑ID
	private String imageId="";//图片ID
	private String imagePos="";//图片ID
	private String verCtrlNum="";//版本控制代码
	private String adCtrlNum="";//广告控制代码
	private String 	help_content = "";//帮助信息
	// ==================专用参数值变量end=============
	
	//====================通用参数值变量start==================
	private String 	reqType = "";//请求类型，比如启动，请求专辑，请求图片等等
	//private String 	phoneNum = "";//手机号码
	private String deviceID = "";
	private String deviceName = "";
	private String deviceType = "";
	private String deviceOSVersion = "";
	private String deviceCountryCode = "";
	private String deviceLanguage = "";
	private String appVersionName = "";
	private int appVersionCode = 0;
	private String deviceScreenDensity = "";
	private int deviceScreenWidth = 0;
	private int deviceScreenHeight = 0;
	// private boolean isUpdate = true;
	// private static String area = null;
	private String simIMSI = "";
	private String netType = "";
	private String mac_address = "";
	private int cellId = -1;
	private int LocId = -1;
	private Double latitude = (double) -1;
	private Double longitude = (double) -1;
	private int mcc;// 运营商所属国家代码
	private int mnc;// 运营商代码
	private String spn;// 运营商的名称
	private int networkType = 0;// 网络名称：
	private String pkgName = "";//包名
	//====================通用参数值变量end==================

	public String getImagePos() {
		return imagePos;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getHelp_content() {
		return help_content;
	}

	public void setHelp_content(String help_content) {
		this.help_content = help_content;
	}

	public void setImagePos(String imagePos) {
		this.imagePos = imagePos;
	}
	
	public String getCataId() {
		return cataId;
	}

	public void setCataId(String cataId) {
		this.cataId = cataId;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}


	/**
	 * 获取网络类型
	 * 
	 * NETWORK_TYPE_1xRTT 7 NETWORK_TYPE_CDMA 4 NETWORK_TYPE_EDGE 2
	 * NETWORK_TYPE_EHRPD 14 NETWORK_TYPE_EVDO_0 5 (0x00000005)
	 * NETWORK_TYPE_EVDO_A 6 (0x00000006) NETWORK_TYPE_EVDO_B 12 (0x0000000c)
	 * NETWORK_TYPE_GPRS 1 (0x00000001) NETWORK_TYPE_HSDPA 8 (0x00000008)
	 * NETWORK_TYPE_HSPA 10 (0x0000000a) NETWORK_TYPE_HSUPA 9 (0x00000009)
	 * NETWORK_TYPE_IDEN 11 (0x0000000b) NETWORK_TYPE_LTE 13 (0x0000000d)
	 * NETWORK_TYPE_UMTS 3 (0x00000003) NETWORK_TYPE_UNKNOWN 0 (0x00000000)
	 * PHONE_TYPE_CDMA 2 (0x00000002) PHONE_TYPE_GSM 1 (0x00000001)
	 * PHONE_TYPE_NONE 0 (0x00000000) PHONE_TYPE_SIP 3 (0x00000003)
	 * SIM_STATE_ABSENT 1 (0x00000001) SIM_STATE_NETWORK_LOCKED 4 (0x00000004)
	 * SIM_STATE_PIN_REQUIRED 2 (0x00000002) SIM_STATE_PUK_REQUIRED 3
	 * (0x00000003) SIM_STATE_READY 5 (0x00000005) SIM_STATE_UNKNOWN 0
	 * 
	 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
	 */
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	public void setDeviceOSVersion(String deviceOSVersion) {
		this.deviceOSVersion = deviceOSVersion;
	}

	public String getDeviceCountryCode() {
		return deviceCountryCode;
	}

	public void setDeviceCountryCode(String deviceCountryCode) {
		this.deviceCountryCode = deviceCountryCode;
	}

	public String getDeviceLanguage() {
		return deviceLanguage;
	}

	public void setDeviceLanguage(String deviceLanguage) {
		this.deviceLanguage = deviceLanguage;
	}

	public int getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(int appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getDeviceScreenDensity() {
		return deviceScreenDensity;
	}

	public void setDeviceScreenDensity(String deviceScreenDensity) {
		this.deviceScreenDensity = deviceScreenDensity;
	}

	public int getDeviceScreenWidth() {
		return deviceScreenWidth;
	}

	public void setDeviceScreenWidth(int deviceScreenWidth) {
		Log.v(Constants.LOG_TAG, "deviceScreenWidth is:"+deviceScreenWidth);
		this.deviceScreenWidth = deviceScreenWidth;
	}

	public int getDeviceScreenHeight() {
		return deviceScreenHeight;
	}

	public void setDeviceScreenHeight(int deviceScreenHeight) {
		this.deviceScreenHeight = deviceScreenHeight;
	}

	public String getSimIMSI() {
		return simIMSI;
	}

	public void setSimIMSI(String simIMSI) {
		this.simIMSI = simIMSI;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getMac_address() {
		return mac_address;
	}

	public void setMac_address(String mac_address) {
		this.mac_address = mac_address;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public int getLocId() {
		return LocId;
	}

	public void setLocId(int locId) {
		LocId = locId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public String getSpn() {
		return spn;
	}

	public void setSpn(String spn) {
		this.spn = spn;
	}

	public int getNetworkType() {
		return networkType;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	public String getVerCtrlNum() {
		return verCtrlNum;
	}

	public void setVerCtrlNum(String verCtrlNum) {
		this.verCtrlNum = verCtrlNum;
	}

	public String getAdCtrlNum() {
		return adCtrlNum;
	}

	public void setAdCtrlNum(String adCtrlNum) {
		this.adCtrlNum = adCtrlNum;
	}
	
//	public String getPhoneNum() {
//		return phoneNum;
//	}
//
//	public void setPhoneNum(String phoneNum) {
//		this.phoneNum = phoneNum;
//	}
	void paraInit( ) {
		
		Log.v(Constants.LOG_TAG, "enter paraInit");
		try {
			PackageInfo packageInfo = mContext.getPackageManager()
					.getPackageInfo(mContext.getPackageName(), 0);
			// 初始化版本号
			this.appVersionCode = packageInfo.versionCode;
			// 初始化版本名称
			this.appVersionName = packageInfo.versionName;

			// 设备类型.
			this.setDeviceType(this.DEVICE_PLATFORM_TYPE);

			// 设备名.
			this.setDeviceName(android.os.Build.MODEL);

			// 操作系统版本.
			this.setDeviceOSVersion(android.os.Build.VERSION.RELEASE);

			// 国家和语言代码.
			this.setDeviceCountryCode(Locale.getDefault().getCountry());
			this.setDeviceLanguage(Locale.getDefault().getLanguage());
			
			//初始化包名
			this.setPkgName(mContext.getPackageName());

//			TelephonyManager phoneMgr = (TelephonyManager) mContext
//					.getSystemService(Context.TELEPHONY_SERVICE);
			// 获取SIM卡的IMSI码
			//this.setSimIMSI(phoneMgr.getSubscriberId());
			//获取手机号码
			//this.setPhoneNum(phoneMgr.getLine1Number());

			try {
				// 获取设备的物理地址
				WifiManager wifiManager = (WifiManager) mContext
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				this.setMac_address("mac"
						+ wifiInfo.getMacAddress().replaceAll(":", ""));
			} catch (Exception e2) {
				Log.v(Constants.LOG_TAG,
						"Permission.ACCESS_WIFI_STATE is not found or the device is Emulator, Please check it!");
			}
			// 获取网络类型
			try {
				ConnectivityManager connManager = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netWorkInfo = connManager.getActiveNetworkInfo();
				if (netWorkInfo != null
						&& !netWorkInfo.getTypeName().toLowerCase()
								.equals("mobile")) {
					this.setNetType(netWorkInfo.getTypeName().toLowerCase());
				} else {
					this.setNetType(netWorkInfo.getExtraInfo().toLowerCase());
				}
				Log.d(Constants.LOG_TAG, "The net is: " + this.getNetType());
			} catch (Exception e1) {
				Log.d(Constants.LOG_TAG, "e1.printStackTrace() is: " + e1.toString());
				e1.printStackTrace();
			}

//			SharedPreferences settings = mContext.getSharedPreferences(
//					this.PREFERENCE, 0);

			// 设备ID.
			TelephonyManager telephonyManager = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);

			if (telephonyManager != null) {
				this.setDeviceID(telephonyManager.getDeviceId());
				Log.v(Constants.LOG_TAG, "deviceID01 is:" + this.getDeviceID());
				// 设备ID是否为空?
				if (this.getDeviceID() == null
						|| this.getDeviceID().length() == 0) {

					// 设置设备ID为0 如果是空，生成一个模拟的设备ID.
					this.setDeviceID("0");
				}
				try {
					// 设备ID转为小写.
					this.setDeviceID(this.getDeviceID().toLowerCase());
					Integer devTag = Integer.parseInt(this.getDeviceID());
					// 设备ID是否0.
					if (devTag.intValue() == 0
							&& this.getMac_address().equals("")) {
						this.setDeviceID("aaa");
				/*		StringBuffer buff = new StringBuffer();
						buff.append("EMULATOR");
						String tempDeviceId = settings.getString(
								this.EMULATOR_DEVICE_ID, null);

						if (tempDeviceId != null && !tempDeviceId.equals("")) {
							this.setDeviceID(tempDeviceId);
						} else {
							this.setDeviceID("aaa");
							String constantChars = "aaa";
							for (int i = 0; i < 32; i++) {
								int randomChar = (int) (Math.random() * 100);
								int ch = randomChar % 30;
								buff.append(constantChars.charAt(ch));
							}
							this.setDeviceID(buff.toString().toLowerCase());
							SharedPreferences.Editor editor = settings.edit();
							editor.putString(this.EMULATOR_DEVICE_ID,
									this.getDeviceID());
							editor.commit();
						}*/
					}
					// IMEI 不存在，并且mac地址可获取时，自定义一个udid并进行固化存储
					else if (devTag.intValue() == 0
							&& this.getMac_address() != null
							&& !("".equals(this.getMac_address().trim()))) {

						this.setDeviceID(this.getMac_address());
					}
				} catch (NumberFormatException ex) {

				}

			} else {
				this.setDeviceID(null);
			}

			// 获取终端屏幕的大小
			try {
				DisplayMetrics metrics = new DisplayMetrics();
				WindowManager windowManager = (WindowManager) mContext
						.getSystemService(Context.WINDOW_SERVICE);
				windowManager.getDefaultDisplay().getMetrics(metrics);
			
				this.setDeviceScreenWidth(metrics.widthPixels);
				this.setDeviceScreenHeight(metrics.heightPixels);
			} catch (Exception e) {
				Log.v(Constants.LOG_TAG, e.toString());
			}

			// 获得GSM网络信息
			TelephonyManager tm = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
				GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
				this.setCellId(gcl.getCid());
				this.setLocId(gcl.getLac());
				Log.v(Constants.LOG_TAG, "cid is:" + this.getCellId());
				Log.v(Constants.LOG_TAG, "lac is:" + this.getLocId());
				// 运营商国家代码
				this.setMcc(Integer.valueOf(tm.getNetworkOperator().substring(
						0, 3)));
				Log.v(Constants.LOG_TAG, "mcc is:" + this.getMcc());

				// 运营商代码
				this.setMnc(Integer.valueOf(tm.getNetworkOperator().substring(
						3, 5)));
				Log.v(Constants.LOG_TAG, "mnc is:" + this.getMnc());

				// 返回移动网络运营商的名字(SPN)
				this.setSpn(tm.getNetworkOperatorName());
				Log.v(Constants.LOG_TAG, "strOprName is:" + this.getSpn());

				/**
				 * 获取网络类型
				 * 
				 * NETWORK_TYPE_1xRTT 7 NETWORK_TYPE_CDMA 4 NETWORK_TYPE_EDGE 2
				 * NETWORK_TYPE_EHRPD 14 NETWORK_TYPE_EVDO_0 5 (0x00000005)
				 * NETWORK_TYPE_EVDO_A 6 (0x00000006) NETWORK_TYPE_EVDO_B 12
				 * (0x0000000c) NETWORK_TYPE_GPRS 1 (0x00000001)
				 * NETWORK_TYPE_HSDPA 8 (0x00000008) NETWORK_TYPE_HSPA 10
				 * (0x0000000a) NETWORK_TYPE_HSUPA 9 (0x00000009)
				 * NETWORK_TYPE_IDEN 11 (0x0000000b) NETWORK_TYPE_LTE 13
				 * (0x0000000d) NETWORK_TYPE_UMTS 3 (0x00000003)
				 * NETWORK_TYPE_UNKNOWN 0 (0x00000000) PHONE_TYPE_CDMA 2
				 * (0x00000002) PHONE_TYPE_GSM 1 (0x00000001) PHONE_TYPE_NONE 0
				 * (0x00000000) PHONE_TYPE_SIP 3 (0x00000003) SIM_STATE_ABSENT 1
				 * (0x00000001) SIM_STATE_NETWORK_LOCKED 4 (0x00000004)
				 * SIM_STATE_PIN_REQUIRED 2 (0x00000002) SIM_STATE_PUK_REQUIRED
				 * 3 (0x00000003) SIM_STATE_READY 5 (0x00000005)
				 * SIM_STATE_UNKNOWN 0
				 * 
				 * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
				 */
				this.setNetworkType(tm.getNetworkType());
				Log.v(Constants.LOG_TAG, "strNetworkType is:" + this.getNetworkType());

			}

			// 获得GPS位置信息
			// 得到gps设备的访问
			LocationManager locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
			// 如果GPS处于打开状态
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
				// 设置gps定位配置
				Criteria criteria = new Criteria();
				// 设置显示精度
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);
				// 是否获得海拔数据
				criteria.setAltitudeRequired(false);
				// 是否获得方向数据
				criteria.setBearingRequired(false);
				// 是否允许运营商计费
				criteria.setCostAllowed(true);
				// 设置耗电程度
				criteria.setPowerRequirement(Criteria.POWER_LOW);

				// 获得服务供应商
				String provider = locationManager.getBestProvider(criteria,
						true);
				Log.v(Constants.LOG_TAG, "provider is:" + provider);

				// 获取上一个定位点
				Location location = locationManager
						.getLastKnownLocation(provider);
				Log.v(Constants.LOG_TAG, "location is:" + location);

				// 获得gps定位坐标信息
				this.setLatitude(location.getLatitude() * 1E6);// 纬度信息
				Log.v(Constants.LOG_TAG, "latitude is:" + this.getLatitude());

				this.setLongitude(location.getLongitude() * 1E6);// 经度信息
				Log.v(Constants.LOG_TAG, "longitude is:" + this.getLongitude());

				// 根据经纬度获得改点地址信息
				String msg = null;
				Geocoder gc = new Geocoder(mContext);
				List<Address> addresses = gc.getFromLocation(
						this.getLatitude(), this.getLongitude(), 1);
				if (addresses.size() > 0) {
					// 获得地址信息
					msg += "AddressLine:" + addresses.get(0).getAddressLine(0)
							+ "\n";
					// 获得国家名
					msg += "CountryName：" + addresses.get(0).getCountryName()
							+ "\n";
					msg += "Locality：" + addresses.get(0).getLocality() + "\n";
					msg += "FeatureName：" + addresses.get(0).getFeatureName();
				}
				Log.v(Constants.LOG_TAG, "msg is:" + msg);
			}
			//
			// Log.v(Constants.LOG_TAG, "locationManager is:"+locationManager);
			// Log.v(Constants.LOG_TAG,
			// "GPS_status is:"+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
			// Log.v(Constants.LOG_TAG,
			// "NETWORK_status is:"+locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));

			// Get the referral URL
//			String tempReferralURL = settings
//					.getString(this.REFERRAL_URL, null);
//			if (tempReferralURL != null && !tempReferralURL.equals("")) {
//
//			}
			// installURI = tempReferralURL;
		} catch (Exception e) {
			Log.v("PPK  flag01", e.toString());
		}
	}

	
	//获取icon的url参数
	public String getIconParam(String albumId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_ICON + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		//return urlParams.replaceAll(" ", "%20");
		return urlParams+getSuffixParam();
	}
	
	//获取image的url参数
	public String getImageParam(String albumId,int imagePos){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_IMAGE + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		urlParams += this.BT_IMAGE_POS + "=" + imagePos + "&";
		return urlParams+getSuffixParam();
	}
	
	//下载image的url参数
	public String getDownImageParam(String albumId,String imageId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_DOWN_IMAGE + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		urlParams += this.BT_IMAGE_ID + "=" + imageId + "&";
		return urlParams+getSuffixParam();
	}
	
	//根据类别ID获取专辑的url参数
	public String getSubAlbumsParam(String albumId,Integer subAlbumAmout){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_SUB_ALBUMS + "&";
		urlParams += this.BT_CUR_ALBUM_AMOUNT + "=" + subAlbumAmout + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId;
		return urlParams;
	}
		
	//根据类别ID获取专辑的url参数
	public String getAlbumsParam(String cataId,Integer curAlbumAmout){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_ALBUMS + "&";
		urlParams += this.BT_CUR_ALBUM_AMOUNT + "=" + curAlbumAmout + "&";
		urlParams += this.BT_CATAGORY_ID + "=" + cataId;
		return urlParams;
	}
	
	//根据类别ID获取专辑的url参数
	public String getAllAlbumsParam(String cataId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_ALBUMS + "&";
		urlParams += this.BT_CATAGORY_ID + "=" + cataId;
		return urlParams;
	}
	
	//获取所有类别的url参数
	public String getCataParam( ){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_CATAGORY;
		return urlParams;
	}
	
	//获取所有类别的url参数
	public String getStartParam(){
		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_START + "&";
		urlParams += this.BT_AD_CTL_NUM + "=" + Constants.AD_CTL_NUM + "&";
		urlParams += this.BT_VER_CTRL_NUM + "=" + Constants.REAL_VER_CTL_NUM + "&";

		return urlParams+this.getSuffixParam();
	}
	
	//获取支付通知参数
	public String getPayNotifyParam(PayInfo payInfo){		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_PAY_NOTIFY + "&";
		urlParams += this.BT_USER_NAME + "=" + payInfo.getUserName() + "&";
		urlParams += this.BT_USER_ID + "=" + payInfo.getUserId() + "&";
		urlParams += this.BT_PAY_ORDER_ID + "=" + payInfo.getOrderId() + "&";
		urlParams += this.BT_PAY_GOODS_NAME + "=" + payInfo.getGoodsName() + "&";
		urlParams += this.BT_PAY_GOODS_DESC + "=" + payInfo.getGoodsDesc() + "&";
		urlParams += this.BT_PAY_PRICE + "=" + payInfo.getPrice() + "&";
		return urlParams+this.getSuffixParam();
	}
	
	//获取支付积分更新参数
	public String getUpdatePayPointParam(int point){		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_UPDATE_POINT + "&";
		urlParams += this.BT_PAY_POINT + "=" + point + "&";
		return urlParams+this.getSuffixParam();
	}
	
	
	//根据album id获取所有图片的url参数
	public String getImageArrParam(String cataId, String albumId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_IMAGE_ARR + "&";
		urlParams += this.BT_CATAGORY_ID +"=" + cataId + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		return urlParams+this.getSuffixParam();
	}
	
/*	//根据album id获取本专辑下的所有图片名称每个一个名称以"|"分割
	public String getImageNamesByAlbumId(String albumId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_IMAGE_NAMES + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		return urlParams+this.getSuffixParam();
	}*/
	
	//升级的url参数
	public String getUpdateParam(){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_UPDATE + "&";
		return urlParams+this.getSuffixParam();
	}
	
	//获取帮助信息
	public String getHelpContentParam( ){		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.GET_HELP_CONTENT + "&";
		return urlParams+this.getSuffixParam();
	}
	
	//得到普通url参数
	public String getSuffixParam() {

		// 构建url参数.
		String urlParams = "";
/*		urlParams += this.REQ_TYPE + "=" + this.getReqType() + "&";

		if(this.getCataId()!= null &&this.getCataId().length()>0){
			urlParams += this.BT_CATAGORY_ID + "=" + this.getCataId() + "&";
		}
		if(this.getAlbumId()!= null &&this.getAlbumId().length()>0){
			urlParams += this.BT_ALBUM_ID + "=" + this.getAlbumId() + "&";
		}
		if(this.getImageId()!= null &&this.getImageId().length()>0){
			urlParams += this.BT_IMAGE_ID + "=" + this.getImageId() + "&";
		}
		if(this.getImagePos()!= null &&this.getImagePos().length()>0){
			urlParams += this.BT_IMAGE_POS + "=" + this.getImagePos() + "&";
		}
		if(this.getReqType().equals(this.BT_START)){//如果是请求应用版本信息，需要将版本控制和广告控制代码设置为参数
			urlParams += this.BT_AD_CTL_NUM + "=" + this.getAdCtrlNum() + "&";
			urlParams += this.BT_VER_CTRL_NUM + "=" + this.getVerCtrlNum() + "&";
		}*/		

		urlParams += this.DEVICE_ID_NAME + "=" + this.getDeviceID() + "&";
		urlParams += this.DEVICE_SIM_IMSI + "=" + this.getSimIMSI() + "&";
		urlParams += this.NET_TYPE + "=" + this.getNetType() + "&";
		urlParams += this.PKG_NAME + "=" + this.getPkgName()
				+ "&";
		urlParams += this.APP_VERSION_NAME + "=" + this.getAppVersionName()
				+ "&";
		urlParams += this.APP_VERSION_CODE + "=" + this.getAppVersionCode()
				+ "&";
		urlParams += this.DEVICE_NAME + "=" + this.getDeviceName() + "&";
		urlParams += this.DEVICE_TYPE_NAME + "=" + this.getDeviceType() + "&";
		urlParams += this.DEVICE_OS_VERSION_NAME + "="
				+ this.getDeviceOSVersion() + "&";
		urlParams += this.DEVICE_COUNTRY_CODE + "="
				+ this.getDeviceCountryCode() + "&";
		urlParams += this.DEVICE_LANGUAGE + "=" + this.getDeviceLanguage()
				+ "&";
		urlParams += this.CELL_ID + "=" + this.getCellId() + "&";
		urlParams += this.LOC_ID + "=" + this.getLocId() + "&";
		urlParams += this.LATITUDE + "=" + this.getLatitude() + "&";
		urlParams += this.LONGITUDE + "=" + this.getLongitude() + "&";
		urlParams += this.MCC + "=" + this.getMcc() + "&";
		urlParams += this.MNC + "=" + this.getMnc() + "&";
		urlParams += this.SPN + "=" + this.SPN + "&";
		urlParams += this.NETWORK_TYPE + "=" + this.getNetworkType() + "&";
		//urlParams += this.PHONE_NUM + "=" + this.getPhoneNum() + "&";
		
		if (this.getDeviceScreenWidth() > 0 && this.getDeviceScreenHeight() > 0) {
			urlParams += "&";
			urlParams += this.DEVICE_SCREEN_WIDTH + "="
					+ this.getDeviceScreenWidth() + "&";
			urlParams += this.DEVICE_SCREEN_HEIGHT + "="
					+ this.getDeviceScreenHeight();
		}
		return urlParams.replaceAll(" ", "%20");
	}

}
