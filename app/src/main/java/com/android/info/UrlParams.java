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
	 * @param reqType ��������
	 */
	public UrlParams(Context mContext) {
		super();
		this.mContext = mContext;
		this.paraInit();
	}

	// URL ������.
	//=================ר�ò���start===========
	public String BT_CATAGORY_ID="catagory_id";//���id
	public String BT_ALBUM_ID="album_id";//ר��id
	public String BT_IMAGE_ID="image_id";//ͼƬid
	public String BT_IMAGE_POS="image_pos";//ͼƬ��ר���е�λ��
	
	//ͳһ�����������
	public String BT_UPDATE="update";//��������
	public String BT_START="bt_start";//��ʼ����
	public String BT_GET_CATAGORY="get_catagory";//��ȡ������Ϣ����
	public String BT_GET_ALBUMS="get_albums";//��ȡר����Ϣ����
	public String BT_GET_SUB_ALBUMS="get_sub_albums";//��ȡ��ר����Ϣ����

	public String BT_CUR_ALBUM_AMOUNT="cur_album_amount";//��ǰר�����������ڼ��ظ���
	public String BT_IS_ALUMB_MORE="is_album_more";//�û��Ƿ������ϣ���ʾ����ѡ��
	public String BT_GET_IMAGE="get_image";//����ID��ȡͼƬ��Ϣ����
	public String BT_GET_IMAGE_ARR="get_image_arr";//��ȡר����imageInfo������ר���е�imageInfoΪ�գ������ר��ID��ȡimageInfo�б�
	//public String BT_GET_IMAGE_NAMES="get_iamge_names";//��ȡר����ID��ȡ���е�ר������
	public String BT_GET_ICON="get_icon";//��ȡר����ͼ�ꣻ
	public String BT_VER_CTRL_NUM="ver_ctrl_num";//�Ƿ�ʹ����ʽ�汾����Ҫ��Ϊ���ڰ�׿�г��Ϸ���������
	public String BT_AD_CTL_NUM="ad_ctrl_num";//�Ƿ�չʾ��棬��Ҫ���ǵ���Щ����ƽ̨�������й��
	public String BT_DOWN_IMAGE="down_image";//����ͼƬ
	public String BT_POINT_LOG="point_log";//���ֻ�ȡ��־��
	public String BT_PAY_INFO="pay_info";//��ֵ��¼��
	public String BT_USER_ID="bt_user_id";//�û�id
	public String BT_USER_NAME="user_name";//�û�����
	public String BT_PAY_POINT="pay_point";//֧������
	public String BT_PAY_ORDER_ID="pay_order_id";//֧������id
	public String BT_PAY_GOODS_NAME="pay_goods_name";//֧����Ʒ����
	public String BT_PAY_GOODS_DESC="pay_goods_desc";//֧����Ʒ����
	public String BT_PAY_PRICE="pay_price";//֧�����	
	public String BT_UPDATE_POINT="update_point";//����֧������
	public String BT_PAY_NOTIFY="pay_notify";//֧��֪ͨ
	public String GET_HELP_CONTENT = "get_help_content"; //��ȡ������Ϣ.

	//======================end==================
	
	//======================ͨ�ò���start==================
	public String REQ_TYPE = "req_type";//�������ͣ���������������ר��������ͼƬ�ȵ�
	public String PHONE_NUM = "phone_num";//�ֻ�����
	public String NETWORK_TYPE = "network_type";// ������࣬����GRPS��CDMA��
	public String CELL_ID = "cell_id";// �ֻ����ڵ�С����վ�ķ���ID����û�л�ȡ����ֵΪ-1
	public String LOC_ID = "loc_id";// �ֻ����ڵ�С��ID����û�л�ȡ����ֵΪ-1
	public String LATITUDE = "latitude"; // ������Ϣ.��û�л�ȡ����ֵΪ-1
	public String LONGITUDE = "longitude"; // ά����Ϣ.��û�л�ȡ����ֵΪ-1
	public String MCC = "mcc";// ��Ӫ���������Ҵ���
	public String MNC = "mnc";// ��Ӫ�̴���
	public String SPN = "spn";// ��Ӫ�̵�����
	public String MAC_ADDRESS = "mac";// mac��ַ
	public String NET_TYPE = "net";// ���ʵ��������ͣ�����CMNET��CMWAP
	public String DEVICE_SIM_IMSI = "imsi"; // �豸Ϊ�ֻ�ʱ��SIMΨһʶ����
	public String DEVICE_ID_NAME = "imei"; // ���豸��ΨһID (IMEI or
														// MEID).
	public String DEVICE_NAME = "device_name"; // ������豸��(iPod touch
															// 2G, iPhone
	// 3GS, ...)
	public String DEVICE_TYPE_NAME = "device_type"; // ƽ̨����
																	// (Android,
																	// iPhone,
	// iPad).
	public String DEVICE_OS_VERSION_NAME = "os_version"; // ����ϵͳ�汾.
	public String DEVICE_COUNTRY_CODE = "country_code"; // ���Ҵ���.
	public String DEVICE_LANGUAGE = "language"; // ���Դ���.
	public String PKG_NAME = "pkg_name"; // ����.
	public String APP_VERSION_NAME = "app_version"; // Ӧ�ó���汾.
	public String DEVICE_SCREEN_WIDTH = "device_width"; // ���ִ�С.
	public String DEVICE_SCREEN_HEIGHT = "device_height"; // ���ִ�С.
	public String APP_VERSION_CODE = "app_version_code"; // Ӧ�ó���汾code.

	
	// static final String CONNECT_URL = Constants.SERVER_MAIN_URL;//���ʵ�URL
	public String DEVICE_PLATFORM_TYPE = "android";//
	public String CURRENT_TIME = "at"; // ϵͳ��ǰʱ��
	public String EMULATOR_DEVICE_ID = "emulatorDeviceId";
	//public String PREFERENCE = "appPrefrences";
	//public String REFERRAL_URL = "InstallReferral";

	//====================ͨ�ò���end==================
	// ==================ר�ò���ֵ����start=============
	private String cataId="";//���ID
	private String albumId="";//ר��ID
	private String imageId="";//ͼƬID
	private String imagePos="";//ͼƬID
	private String verCtrlNum="";//�汾���ƴ���
	private String adCtrlNum="";//�����ƴ���
	private String 	help_content = "";//������Ϣ
	// ==================ר�ò���ֵ����end=============
	
	//====================ͨ�ò���ֵ����start==================
	private String 	reqType = "";//�������ͣ���������������ר��������ͼƬ�ȵ�
	//private String 	phoneNum = "";//�ֻ�����
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
	private int mcc;// ��Ӫ���������Ҵ���
	private int mnc;// ��Ӫ�̴���
	private String spn;// ��Ӫ�̵�����
	private int networkType = 0;// �������ƣ�
	private String pkgName = "";//����
	//====================ͨ�ò���ֵ����end==================

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
	 * ��ȡ��������
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
	 * ���й�����ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO
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
			// ��ʼ���汾��
			this.appVersionCode = packageInfo.versionCode;
			// ��ʼ���汾����
			this.appVersionName = packageInfo.versionName;

			// �豸����.
			this.setDeviceType(this.DEVICE_PLATFORM_TYPE);

			// �豸��.
			this.setDeviceName(android.os.Build.MODEL);

			// ����ϵͳ�汾.
			this.setDeviceOSVersion(android.os.Build.VERSION.RELEASE);

			// ���Һ����Դ���.
			this.setDeviceCountryCode(Locale.getDefault().getCountry());
			this.setDeviceLanguage(Locale.getDefault().getLanguage());
			
			//��ʼ������
			this.setPkgName(mContext.getPackageName());

//			TelephonyManager phoneMgr = (TelephonyManager) mContext
//					.getSystemService(Context.TELEPHONY_SERVICE);
			// ��ȡSIM����IMSI��
			//this.setSimIMSI(phoneMgr.getSubscriberId());
			//��ȡ�ֻ�����
			//this.setPhoneNum(phoneMgr.getLine1Number());

			try {
				// ��ȡ�豸�������ַ
				WifiManager wifiManager = (WifiManager) mContext
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				this.setMac_address("mac"
						+ wifiInfo.getMacAddress().replaceAll(":", ""));
			} catch (Exception e2) {
				Log.v(Constants.LOG_TAG,
						"Permission.ACCESS_WIFI_STATE is not found or the device is Emulator, Please check it!");
			}
			// ��ȡ��������
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

			// �豸ID.
			TelephonyManager telephonyManager = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);

			if (telephonyManager != null) {
				this.setDeviceID(telephonyManager.getDeviceId());
				Log.v(Constants.LOG_TAG, "deviceID01 is:" + this.getDeviceID());
				// �豸ID�Ƿ�Ϊ��?
				if (this.getDeviceID() == null
						|| this.getDeviceID().length() == 0) {

					// �����豸IDΪ0 ����ǿգ�����һ��ģ����豸ID.
					this.setDeviceID("0");
				}
				try {
					// �豸IDתΪСд.
					this.setDeviceID(this.getDeviceID().toLowerCase());
					Integer devTag = Integer.parseInt(this.getDeviceID());
					// �豸ID�Ƿ�0.
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
					// IMEI �����ڣ�����mac��ַ�ɻ�ȡʱ���Զ���һ��udid�����й̻��洢
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

			// ��ȡ�ն���Ļ�Ĵ�С
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

			// ���GSM������Ϣ
			TelephonyManager tm = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
				GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
				this.setCellId(gcl.getCid());
				this.setLocId(gcl.getLac());
				Log.v(Constants.LOG_TAG, "cid is:" + this.getCellId());
				Log.v(Constants.LOG_TAG, "lac is:" + this.getLocId());
				// ��Ӫ�̹��Ҵ���
				this.setMcc(Integer.valueOf(tm.getNetworkOperator().substring(
						0, 3)));
				Log.v(Constants.LOG_TAG, "mcc is:" + this.getMcc());

				// ��Ӫ�̴���
				this.setMnc(Integer.valueOf(tm.getNetworkOperator().substring(
						3, 5)));
				Log.v(Constants.LOG_TAG, "mnc is:" + this.getMnc());

				// �����ƶ�������Ӫ�̵�����(SPN)
				this.setSpn(tm.getNetworkOperatorName());
				Log.v(Constants.LOG_TAG, "strOprName is:" + this.getSpn());

				/**
				 * ��ȡ��������
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
				 * ���й�����ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO
				 */
				this.setNetworkType(tm.getNetworkType());
				Log.v(Constants.LOG_TAG, "strNetworkType is:" + this.getNetworkType());

			}

			// ���GPSλ����Ϣ
			// �õ�gps�豸�ķ���
			LocationManager locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
			// ���GPS���ڴ�״̬
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
				// ����gps��λ����
				Criteria criteria = new Criteria();
				// ������ʾ����
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);
				// �Ƿ��ú�������
				criteria.setAltitudeRequired(false);
				// �Ƿ��÷�������
				criteria.setBearingRequired(false);
				// �Ƿ�������Ӫ�̼Ʒ�
				criteria.setCostAllowed(true);
				// ���úĵ�̶�
				criteria.setPowerRequirement(Criteria.POWER_LOW);

				// ��÷���Ӧ��
				String provider = locationManager.getBestProvider(criteria,
						true);
				Log.v(Constants.LOG_TAG, "provider is:" + provider);

				// ��ȡ��һ����λ��
				Location location = locationManager
						.getLastKnownLocation(provider);
				Log.v(Constants.LOG_TAG, "location is:" + location);

				// ���gps��λ������Ϣ
				this.setLatitude(location.getLatitude() * 1E6);// γ����Ϣ
				Log.v(Constants.LOG_TAG, "latitude is:" + this.getLatitude());

				this.setLongitude(location.getLongitude() * 1E6);// ������Ϣ
				Log.v(Constants.LOG_TAG, "longitude is:" + this.getLongitude());

				// ���ݾ�γ�Ȼ�øĵ��ַ��Ϣ
				String msg = null;
				Geocoder gc = new Geocoder(mContext);
				List<Address> addresses = gc.getFromLocation(
						this.getLatitude(), this.getLongitude(), 1);
				if (addresses.size() > 0) {
					// ��õ�ַ��Ϣ
					msg += "AddressLine:" + addresses.get(0).getAddressLine(0)
							+ "\n";
					// ��ù�����
					msg += "CountryName��" + addresses.get(0).getCountryName()
							+ "\n";
					msg += "Locality��" + addresses.get(0).getLocality() + "\n";
					msg += "FeatureName��" + addresses.get(0).getFeatureName();
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

	
	//��ȡicon��url����
	public String getIconParam(String albumId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_ICON + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		//return urlParams.replaceAll(" ", "%20");
		return urlParams+getSuffixParam();
	}
	
	//��ȡimage��url����
	public String getImageParam(String albumId,int imagePos){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_IMAGE + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		urlParams += this.BT_IMAGE_POS + "=" + imagePos + "&";
		return urlParams+getSuffixParam();
	}
	
	//����image��url����
	public String getDownImageParam(String albumId,String imageId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_DOWN_IMAGE + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		urlParams += this.BT_IMAGE_ID + "=" + imageId + "&";
		return urlParams+getSuffixParam();
	}
	
	//�������ID��ȡר����url����
	public String getSubAlbumsParam(String albumId,Integer subAlbumAmout){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_SUB_ALBUMS + "&";
		urlParams += this.BT_CUR_ALBUM_AMOUNT + "=" + subAlbumAmout + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId;
		return urlParams;
	}
		
	//�������ID��ȡר����url����
	public String getAlbumsParam(String cataId,Integer curAlbumAmout){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_ALBUMS + "&";
		urlParams += this.BT_CUR_ALBUM_AMOUNT + "=" + curAlbumAmout + "&";
		urlParams += this.BT_CATAGORY_ID + "=" + cataId;
		return urlParams;
	}
	
	//�������ID��ȡר����url����
	public String getAllAlbumsParam(String cataId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_ALBUMS + "&";
		urlParams += this.BT_CATAGORY_ID + "=" + cataId;
		return urlParams;
	}
	
	//��ȡ��������url����
	public String getCataParam( ){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_CATAGORY;
		return urlParams;
	}
	
	//��ȡ��������url����
	public String getStartParam(){
		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_START + "&";
		urlParams += this.BT_AD_CTL_NUM + "=" + Constants.AD_CTL_NUM + "&";
		urlParams += this.BT_VER_CTRL_NUM + "=" + Constants.REAL_VER_CTL_NUM + "&";

		return urlParams+this.getSuffixParam();
	}
	
	//��ȡ֧��֪ͨ����
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
	
	//��ȡ֧�����ָ��²���
	public String getUpdatePayPointParam(int point){		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_UPDATE_POINT + "&";
		urlParams += this.BT_PAY_POINT + "=" + point + "&";
		return urlParams+this.getSuffixParam();
	}
	
	
	//����album id��ȡ����ͼƬ��url����
	public String getImageArrParam(String cataId, String albumId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_IMAGE_ARR + "&";
		urlParams += this.BT_CATAGORY_ID +"=" + cataId + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		return urlParams+this.getSuffixParam();
	}
	
/*	//����album id��ȡ��ר���µ�����ͼƬ����ÿ��һ��������"|"�ָ�
	public String getImageNamesByAlbumId(String albumId){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_GET_IMAGE_NAMES + "&";
		urlParams += this.BT_ALBUM_ID + "=" + albumId + "&";
		return urlParams+this.getSuffixParam();
	}*/
	
	//������url����
	public String getUpdateParam(){
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.BT_UPDATE + "&";
		return urlParams+this.getSuffixParam();
	}
	
	//��ȡ������Ϣ
	public String getHelpContentParam( ){		
		String urlParams = "";
		urlParams += this.REQ_TYPE + "=" + this.GET_HELP_CONTENT + "&";
		return urlParams+this.getSuffixParam();
	}
	
	//�õ���ͨurl����
	public String getSuffixParam() {

		// ����url����.
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
		if(this.getReqType().equals(this.BT_START)){//���������Ӧ�ð汾��Ϣ����Ҫ���汾���ƺ͹����ƴ�������Ϊ����
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
