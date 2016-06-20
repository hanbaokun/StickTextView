package cn.rosen.sticktextview.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CommonUtils{
	
	/**
	 * 获取系统版本
	 */
	public static int version = android.os.Build.VERSION.SDK_INT;

	/**
	 * 获取屏幕宽度
	 * @param act
	 * @return
	 */
	public static int getDisplayWidth(Activity act) 
	{	
		return act.getWindowManager().getDefaultDisplay().getWidth();
	}
	/**
	 * 获取屏幕高度
	 * @param act
	 * @return
	 */
	public static int getDisplayHeight(Activity act) 
	{
		return act.getWindowManager().getDefaultDisplay().getHeight();
	}
	/**
	 * DisplayMetrics  dm = new DisplayMetrics();     
	 * getWindowManager().getDefaultDisplay().getMetrics(dm);     
	 * int screenWidth = dm.widthPixels;               
	 * int screenHeight = dm.heightPixels;
	 */
    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
	public static int getScreenWidth(Context context) { 
	 WindowManager manager = (WindowManager) context 
	         .getSystemService(Context.WINDOW_SERVICE); 
	 Display display = manager.getDefaultDisplay(); 
	 return display.getWidth(); 
	} 
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) { 
	 WindowManager manager = (WindowManager) context 
	         .getSystemService(Context.WINDOW_SERVICE); 
	 Display display = manager.getDefaultDisplay(); 
	 return display.getHeight(); 
	}
	/**
	 * 获取屏幕最小宽度（相对高度来说）
	 * @param act
	 * @return
	 */
	public static int getMinWidth(Activity act)
	{
		return Math.min(getDisplayWidth(act), getDisplayHeight(act));
	}
	
	/**
	 * 获取屏幕像素宽度
	 * @param context
	 * @return
	 */
	public static int getWidthPixels(Context context) 
	{		
		 DisplayMetrics dm = new DisplayMetrics();   
		 dm = context.getResources().getDisplayMetrics();   
		   		
		return dm.widthPixels;
	}
	/**
	 * 获取屏幕像素高度
	 * @param context
	 * @return
	 */
	public static int getHeightPixels(Context context) 
	{
		DisplayMetrics dm = new DisplayMetrics();   
		dm = context.getResources().getDisplayMetrics();   
		return dm.heightPixels;
	}
	/**
	 * 以dp形式获取屏幕像素高度
	 * @param context
	 * @return
	 */
	public static final int getHeightInDp(Context context) {
		final float height = getHeightPixels(context);
		int heightInDp = pxToDip(context, height);
		return heightInDp;
	}
	/**
	 * 以dp形式获取屏幕像素宽度
	 * @param context
	 * @return
	 */
	public static final int getWidthInDp(Context context) {
		final float width = getWidthPixels(context);
		int widthInDp = pxToDip(context, width);
		return widthInDp;
	}
	/**
	 * 获取屏幕密度
	 * @param activity
	 * @return
	 */
	public static float getScreenDensity(Activity activity){
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		return metric.density;
	}
	/**
	 * 获取根Activity
	 * @param activity
	 * @return
	 */
	public static Activity getRootContext(Activity activity) {
		while(activity.getParent() != null){
			activity = activity.getParent();
		}
		return activity;
	}
	/**
	 * 判断横竖屏  
	 * @param context
	 * @return true为横屏，false为竖屏
	 */
	public static boolean isOrientationLandscape(Context context)
	{
		return getWidthPixels(context) > getHeightPixels(context);
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dipToPx(Context context, float dpValue) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}
	/**
	 * 根据手机的分辨率从 px(像素)的单位 转成为 dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxToDip(Context context,float pxValue){
		final float density = context.getResources().getDisplayMetrics().density;
		return  (int)(pxValue/density +0.5f);
		
	}
	/**
	 * px转sp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * sp转px
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}
	
	/**
	 * 隐藏输入法
	 * @param act
	 */
	public static void hideInputMethod(Activity act) {
		if(act == null){
			return;
		}
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (act.getWindow() != null) {
				if (act.getWindow().getCurrentFocus() == null) {
					inputMethodManager.hideSoftInputFromWindow(null, 0);
				} else {
					inputMethodManager.hideSoftInputFromWindow(act.getWindow().getCurrentFocus().getWindowToken(), 0);
				}
			}
		} catch (Exception e) {
			PrintUtils.println(e);
		}
	}
	
	public static void hideInputMethod(Activity act, EditText etext) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(etext.getWindowToken(),0);
		} catch (Exception e) {
			PrintUtils.println(e);
		}
	}
	
	/**
	 * 显示输入法
	 * @param act
	 */
	public static void showInputMethod(Activity act){
		if(act == null){
			return;
		}
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (act.getWindow() != null && act.getWindow().getCurrentFocus() != null) {
				inputMethodManager.showSoftInputFromInputMethod(act.getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
			PrintUtils.println(e);
		}
	}
	
	/**
	 * 显示输入法(建议使用这个，这个更能确保弹出)
	 * @param act
	 * @param mEditView
	 */
	public static void showInputMethod(Activity act, View mEditView) {
		try {
			mEditView.requestFocus();
			InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mEditView, 0);
		} catch (Exception e) {
			PrintUtils.println(e);
		}
	}
	
	/**
	 * 隐藏标题栏（用于没有在配置里设置主题为Theme.Black.NoTitleBar）
	 * @param act
	 */
	public static void hideFeatureTitle(Activity act){
		act.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	/**
	 * 打电话，调用前先验证phone，同时检查是否有添加权限
	 * @param context
	 * @param phone
	 */
	public static void callNumber(Context context, String phone) {
		try {
			// 传入服务， parse（）解析号码
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
			// 通知activtity处理传入的call服务
			context.startActivity(intent);
		} catch (Exception ex) {
			PrintUtils.println(ex);
		}
	}
	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isGPSOpen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}
	/**
	 * 强制帮用户打开GPS
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			PrintUtils.println(e);
		}
	}
	/**
	 * 判断wifi是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiAvilable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiInfo != null) {
			return wifiInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 判断wifi是否处于连接
	 * @param context
	 * @return
	 */
	public static boolean isWiftConnected(Context context){
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifiInfo != null){
			return wifiInfo.isConnected();
		}
		return false;
	}
	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvilable(Context context) {
		if (context == null) {
			return false;
		}
		try {
			ConnectivityManager con = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(con == null){
				return false;
			}
			boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
			boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
			boolean isAvilable = con.getActiveNetworkInfo().isAvailable();
			if(wifi || internet || isAvilable){
				return true;
			}
//			if (connectivity != null) {
//				// 获取网络连接管理的对象
//				NetworkInfo info = connectivity.getActiveNetworkInfo();
//				if (info != null) {
//					// 判断当前网络是否已经连接
//					return info.isConnected()
//				}
//			}
		} catch (Exception e) {
			PrintUtils.println(e);
			return false;
		}
		return false;
	}
	/**
	 * 判断网络是否链接成功
	 * @param context
	 * @return
	 */
	public static boolean hasInternet(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		return false;
	}
	public static boolean exterStorageReady() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
	}
	/**
	 * 获取缓存路径
	 * @param context
	 * @return
	 */
	public static String getDiskCacheDir(Context context){
		String cachePath = null;
		try {
			if(exterStorageReady()){
				cachePath = context.getExternalCacheDir().getPath();
			}else{
				cachePath = context.getCacheDir().getPath();
			}
		} catch (Exception e) {
			cachePath = getCacheDir(context);
			e.printStackTrace();
		}
		return cachePath;
	}

	/**
	 * 获取文件目录
	 * @param context
	 * @return
	 */
	public static String getDiskFileDir(Context context){
		String filePath = null;
		try {
			if(exterStorageReady()){
				filePath = context.getExternalFilesDir(null).getPath();
			}else {
				filePath = context.getFilesDir().getPath();
			}
		} catch (Exception e) {
			filePath = getFileDir(context);
			e.printStackTrace();
		}
		return filePath;
	}
	/**
	 * 获取缓存目录
	 *
	 * @param context
	 * @return
	 */
	public static String getCacheDir(Context context) {
		return context == null ? "" : context.getCacheDir().getAbsolutePath();
	}

	/**
	 * 获取文件缓存目录
	 *
	 * @param context
	 * @return
	 */
	public static String getFileDir(Context context) {
		return context == null ? "" : context.getFilesDir().getAbsolutePath();
	}
	public static String getAvilablePath(Context context){
		if(exterStorageReady()){
			return getExCardPath();
		}else{
			return getFileDir(context);
		}
	}
	/**
	 * 获取扩展卡路径
	 */
	public static String getExCardPath() {
		if (exterStorageReady()){
			return Environment.getExternalStorageDirectory().toString();
		}
		return "";
	}
	
	/**
	 * 获取手机剩余空间
	 * @return
	 */
	public static long getRemainSaveSize() {
		//有sd卡
		if (exterStorageReady()) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			// 获取一个文件的存储大小
			long blockSize = statFs.getBlockSize();
			// 获取剩下可用的文件大小
			long blocks = statFs.getFreeBlocks();
			return blocks * blockSize;
		} 
		//木有sd卡
		else {
			String rootPath = Environment.getRootDirectory().getPath(); 
			StatFs statFs = new StatFs(rootPath); 
			// 获取一个文件的存储大小
			long blockSize = statFs.getBlockSize();
			// 获取剩下可用的文件大小
			long blocks = statFs.getFreeBlocks();
			return blocks * blockSize;
		}
	}
	/**
	 * 判断某应用是否安装
	 * @param context
	 * @param pagckageName
	 * @return
	 */
	public boolean isAppAvilible(Context context,String pagckageName){
		if(pagckageName == null || "".equals(pagckageName)){
			return false;
		}
		PackageManager packageManager = context.getPackageManager();
		//获取所有已安装包信息
		List<PackageInfo> pagckageInfos = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pagckageInfos.size(); i++) {
			if(pagckageInfos.get(i).packageName.equalsIgnoreCase(pagckageName)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 跳转到包名对应应用的启动页。
	 * @param context
	 * @param pagckageName
	 * @param activityName
	 */
	public static void goApplication(Context context,String pagckageName,String activityName){
		Intent it = new Intent();
		ComponentName cn = new ComponentName(pagckageName,activityName);
		it.setComponent(cn);
		context.startActivity(it);
	}
	/**
	 * 根据包名打开应用
	 * @param context
	 * @param pagckageName
	 */
	public static void goApplication(Context context,String pagckageName){
		PackageManager packageManager = context.getPackageManager();
		Intent it = packageManager.getLaunchIntentForPackage(pagckageName);
		context.startActivity(it);
	}
	/**
     * 用来判断服务是否运行.
     * @param context
     * @param className 判断的服务名字：包名+类名
     * @return true 在运行, false 不在运行
     */
    public static boolean isServiceRunning(Context context,String className) {  
    	if(context == null || TextUtils.isEmpty(className)){
    		return false;
    	}
    	
        boolean isRunning = false;
        ActivityManager activityManager = 
            (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList 
        = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        PrintUtils.println("service is running?==" + isRunning);
        return isRunning;
    }
    
    /**
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
     * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     * @param context
     * @param apkPath apk路径
     * @return 图标Drawable对象
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }
    /**
	 * 设置图像移动动画效果
	 * 
	 * @param v
	 * @param startX
	 * @param toX
	 * @param startY
	 * @param toY
	 */
	public static void setImageSlide(View v, int startX, int toX, int startY,
			int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
				toY);
		anim.setDuration(100);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
    /**
     * 获取字符串宽度
     * @param paint
     * @param str
     * @return
     */
    public static int getStringWidth(Paint paint, String str) {
		int iRet = 0;
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];
			paint.getTextWidths(str, widths);
			for (int j = 0; j < len; j++) {
				iRet += (int) Math.ceil(widths[j]);
			}
		}
		return iRet;
	}
    /**
     * 获取配置文件里的版本名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context)
	{
	    PackageManager packageManager = context.getPackageManager();  
	    //GetPackageName () is your current class package name, 0 stands for is to get version information  
	    PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionName;   
		} catch (NameNotFoundException e) {
			PrintUtils.println(e);
			return "";
		}
	}
    /**
     * 获取配置文件里的版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context)
	{
	    PackageManager packageManager = context.getPackageManager();  
	    //GetPackageName () is your current class package name, 0 stands for is to get version information  
	    PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionCode;   
		} catch (NameNotFoundException e) {
			PrintUtils.println(e);
			return 0;
		}
	}
    /**
     * 获取metaData数据集
     * @param context
     * @return
     */
    public static Bundle getMetaData(Context context){
		ApplicationInfo info;
		Bundle bundle;
		try {
			info = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			bundle = info.metaData;
			if(bundle == null){
				bundle = new Bundle();
			}
		} catch (NameNotFoundException e) {
			bundle = new Bundle();
			e.printStackTrace();
		}
		return bundle;
    }
    /**
     * 获取设备id（IMEI number）
     * @param context
     * @return
     */
    public static String getDeviceID(Context context)
	{
    	String id = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if(TextUtils.isEmpty(id)){
			id = "000000000000000";
		}
    	return id;
	}
    /**
	 * 获取设备的UA
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserAgent(Context context) {
		try {
			WebView webview = new WebView(context);
			webview.layout(0, 0, 0, 0);
			WebSettings settings = webview.getSettings();
			String ua = settings.getUserAgentString();
			return ua;
		} catch (Exception e) {
			PrintUtils.println(e);
		}
		return null;
	}
	/**
	 * 去设置默认浏览器
	 * @param context
	 */
	public static void setDefaultBrowser(Context context,String tip){
		Intent intent = new Intent();  
	    intent.setAction("android.intent.action.VIEW");  
	    intent.addCategory("android.intent.category.BROWSABLE");  
	    intent.setData(Uri.parse(tip));  
	    intent.setComponent(new ComponentName("android","com.android.internal.app.ResolverActivity"));  
	    context.startActivity(intent); 
	}
	/**
	 * 检查是否已经有了默认浏览器
	 * @param context
	 * @return
	 */
	public static String checkHasDefaultBrowser(Context context) { 
		Intent intent = new Intent(Intent.ACTION_VIEW);  
	    intent.setData(Uri.parse("http://www.baidu.com"));
	    PackageManager pm = context.getPackageManager();  
	    ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);  
	    return info.activityInfo.packageName; 
	}
	/**
	 * 去清除包名所对应的默认浏览器
	 * @param context
	 * @param packgeName
	 */
	public static void clearDefaultBrowser(Context context,String packgeName){
		Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+packgeName));  
		context.startActivity(intent);
	}
	/**
	 * 清除从默认浏览器中自己浏览器
	 * @param context
	 */
	public static void clearMyDefaultBrowser(Context context){
		 PackageManager pm = context.getPackageManager();  
		 pm.clearPackagePreferredActivities(context.getPackageName());  
	}
	/**
     * 调整显示窗体
     * @param act
     * @param alpha 0.4f变暗 1f恢复亮度
     */
    public static void ajustWindow(Activity act,float alpha){
    	 WindowManager.LayoutParams lp= act.getWindow().getAttributes();
    	 lp.alpha = alpha;
    	 act.getWindow().setAttributes(lp);
    }
    /**
	 * 复制文本
	 * @param context
	 * @param text
	 */
	@SuppressWarnings("deprecation")
	public static void copyText(Context context,String text){
		ClipboardManager cm =(ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(text);
	}
	
	/**
	 * 判断当前设置的语言
	 * @return
	 */
	public static boolean IsLanguageCN() {
		String language = Locale.getDefault().getLanguage();
		if (language.equalsIgnoreCase("zh") || language.equalsIgnoreCase("cn"))
			return true;
		return false;
	}
	
	/**
	 * 随机的数值(这是一串32位的十六进制的数字，它产生重复值的概率是16的32次方之一)
	 * @return
	 */
	public static String GetGUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 将url转为文件名
	 * @param url
	 * @return
	 */
//	public static String GetUrlName(String url) {
//		if(TextUtils.isEmpty(url)){
//			return GetGUID();
//		}
//
//		String name = URegex.Match(url.trim(), URegex.RegUrlName);
//		if (TextUtils.isEmpty(name)){
//			return FileMD5Utils.stringMD5(url);
//		}
//		return name;
//	}
	
	@SuppressWarnings("rawtypes")
	public static void Sort(List<Map> list, final String sortKey) {
		Sort(list, sortKey, true);
	}

	// 排序
	@SuppressWarnings("rawtypes")
	public static void Sort(List<Map> list, final String sortKey,
			final boolean IsAsc) {
		Comparator<Map> cp = new Comparator<Map>() {
			private final Collator collator = Collator.getInstance();

			@Override
			public int compare(Map map1, Map map2) {
				if (IsAsc)
					return collator.compare(map1.get(sortKey),
							map2.get(sortKey));
				else
					return collator.compare(map2.get(sortKey),
							map1.get(sortKey));
			}
		};
		Collections.sort(list, cp);
	}
	
	/**
	 * 某个APP是否有注册了一个明确的intent
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {
	    final PackageManager packageManager = context.getPackageManager();
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
	            PackageManager.GET_ACTIVITIES);
	    return list.size() > 0;
	}
	
	/**
	 * 获取透明度
	 * @param per
	 * @return
	 */
	public static int alphaTranslate(int per){
		return 255*per/100;
	}
	
	/**
	 * 从assets获取bitmap
	 * @param context
	 * @param path
	 * @return
	 */
	public static Bitmap createFromAsset(Context context,String path){
		if(!TextUtils.isEmpty(path)){
			try{
				InputStream is = context.getAssets().open(path);
				return BitmapFactory.decodeStream(is);
			}catch (Exception e){
			}
		}
		return null;
	}
	/**
	 * url编码
	 * @param url
	 * @return
	 */
	public static String urlEncode(String url){
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * url解码
	 * @param url
	 * @return
	 */
	public static String urlDecode(String url){
		try {
			url = URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * 把文件扫描进媒体库
	 * @param filename
	 * @param context
	 */
	public static void updateMedia(String filename,Context context)//filename是我们的文件全名，包括后缀哦
	{
		MediaScannerConnection.scanFile(context,
				new String[]{filename}, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.i("ExternalStorage", "Scanned " + path + ":");
						Log.i("ExternalStorage", "-> uri=" + uri);
					}
				});
	}
	/**
	 * 获取系统资源
	 * @param intent
	 * @param context
	 * @param type
	 * @return
	 */
	public static String getDataPath(Intent intent, Context context, String type){
		String filePath = null;
		Uri uri = intent.getData();
		String[] proj = null;
		boolean isTypeEmpty = TextUtils.isEmpty(type);
		if(isTypeEmpty){
			type = "_data";
		}
//		System.out.println("uri:"+uri);
		proj = new String[]{type};
		Cursor actualimagecursor = context.getContentResolver().query(uri, proj, null, null, null);
		if(actualimagecursor == null){
			filePath = uri.getPath();
		}else{
			actualimagecursor.moveToFirst();
			int actual_image_column_index = actualimagecursor
					.getColumnIndexOrThrow(type);
			System.out.println("actual_image_column_index:"+actual_image_column_index);
			filePath = actualimagecursor.getString(actual_image_column_index);
//			if(filePath == null){
//				ToastUtils.showShortMsg(context.getApplicationContext(),"路径存在问题，请确认选择方式是否存在问题！");
//			}
//			if(filePath == null){
//				filePath = uri.getPath();
//			}
//			System.out.println("filePath:"+filePath);
		}
		return filePath;
	}
}
