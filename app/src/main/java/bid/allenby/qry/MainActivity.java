package bid.allenby.qry;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import android.R.string;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.*;
import android.app.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import android.webkit.*;
import android.widget.*;
import android.content.*;
import android.database.sqlite.*;
import android.database.*;
import android.view.*;
import android.content.pm.*;
import android.os.*;
import java.io.*;
import org.apache.http.*;
import java.util.jar.*;
import android.app.*;
import android.net.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import me.shenfan.updateapp.UpdateService;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar proccess3;
	private String dbn="sys_data";
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILURE = 1;
	private Handler mHandler = null;
	private Thread httpClientThread;
	private String updateurl="http://o.allenby.bid/api.php?ac=qry_update";
    public static final int WRITE_EXTERNAL_STORAGE_REQ_CODE =10 ;


	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏 
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.main);
        TextView main=(TextView) findViewById(R.id.khide2);
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		proccess3 = (ProgressBar) findViewById(R.id.proccess3);
		TextView ver1=(TextView) findViewById(R.id.ver1);
		String veapp=getAppVersionName(this);
		ver1.setText(veapp);
		ContentValues values = new ContentValues();
		values.put("id", 2);  
		values.put("name", veapp);  
		// 创建DatabaseHelper对象  
		DatabaseHelper dbHleper = new DatabaseHelper(MainActivity.this, dbn, 2);  
		// 得到一个可写的SQLiteDatabase对象  
		SQLiteDatabase sqliteDatabase = dbHleper.getWritableDatabase();  
		// 调用insert方法，就可以将数据插入到数据库当中  
		// 第一个参数:表名称  
		// 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值  
		// 第三个参数：ContentValues对象  
		sqliteDatabase.insert("sys", null, values); 
		sqliteDatabase.close();
        if(!isAvilible(this,"bid.allenby.login")){
			Toast.makeText(getApplicationContext(), "😊首次安装，依赖软件下载中",Toast.LENGTH_SHORT).show();
			UpdateService.Builder.create("http://o.allenby.bid/down/xscy.apk").setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
				.setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL).setStoreDir("QRY/update").build(MainActivity.this);
		}
		httpClientThread = new Thread(httpClientRunnable);
		httpClientThread.start();
		mHandler = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{

				switch (msg.what)
				{
					case MSG_SUCCESS:
						String id = null;  
						String name = null;  

						DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, dbn, 2);  

						SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

						Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
																 "name" }, "id=?", new String[] { "2" }, null, null, null);  

						while (cursor.moveToNext())
						{  
							id = cursor.getString(cursor.getColumnIndex("id"));  
							name = cursor.getString(cursor.getColumnIndex("name"));  
						}
						sqliteDatabase.close();
						//Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();	
						JsonBean3 foos3 = new Gson().fromJson((String)msg.obj, JsonBean3.class);
						proccess3.setVisibility(View.GONE);
						if (foos3.value.equals(name) == false)
						{
							String titl="新版本" + foos3.value;
							String con=foos3.value2;
							String iurl=foos3.value3;
							ContentValues values = new ContentValues();
							values.put("id", 3);  
							values.put("name", iurl);  

							DatabaseHelper dbHleper = new DatabaseHelper(MainActivity.this, dbn, 2);  

							SQLiteDatabase sqliteDatabase2= dbHleper.getWritableDatabase();  

							sqliteDatabase2.insert("sys", null, values); 
							sqliteDatabase2.close();

							AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

							builder.setIcon(R.drawable.ic_update);

							builder.setTitle(titl);

							builder.setMessage(con);

							builder.setPositiveButton("更新", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										String id = null;  
										String name = null;  

										DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, dbn, 2);  

										SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

										Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
																				 "name" }, "id=?", new String[] { "3" }, null, null, null);  

										while (cursor.moveToNext())
										{  
											id = cursor.getString(cursor.getColumnIndex("id"));  
											name = cursor.getString(cursor.getColumnIndex("name"));  
										}
										sqliteDatabase.close();
										/*Toast.makeText(MainActivity.this, "正在跳转", Toast.LENGTH_SHORT).show();
										 Intent intent= new Intent();       
										 intent.setAction("android.intent.action.VIEW");   
										 Uri url = Uri.parse(name);  
										 intent.setData(url); 
										 startActivity(intent);*/
										Toast.makeText(getApplicationContext(), "安装包下载中，请稍后。", Toast.LENGTH_SHORT).show();
										UpdateService.Builder.create(name).setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
											.setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL).setStoreDir("QRY/update").build(MainActivity.this);
										//finish();
									}
								});

							builder.setNegativeButton("退出", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										finish();
									}
								});

							builder.show();
						}
						else
						{
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, meanc.class);
							startActivity(intent);
							finish();
						}
						break;
					case MSG_FAILURE:
						Toast.makeText(getApplicationContext(), "未接入互联网", Toast.LENGTH_SHORT).show();
						finish();
					default:
						break;
				}
			}

		};
    }

	public String getInnerSDCardPath()
	{  
        return Environment.getExternalStorageDirectory().getPath();  
    }

	public static String getAppVersionName(Context context)
	{
		String versionName = "";
		int versioncode=1;
		try
		{

			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0)
			{
				return "";
			}
		}
		catch (Exception e)
		{

		}
		return versionName;
	}
	Runnable httpClientRunnable = new Runnable() {

		@Override
		public void run()
		{

			httpClientWebData();

		}
	};
	/**
	 * 检查手机上是否安装了指定的软件
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		List<String> packageNames = new ArrayList<String>();

		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}
	protected void httpClientWebData()
	{
     	DefaultHttpClient httpClinet = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(updateurl);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try
		{
			String content = httpClinet.execute(httpGet, responseHandler);
			mHandler.obtainMessage(MSG_SUCCESS, content).sendToTarget();
		}
		catch (ClientProtocolException e)
		{

			e.printStackTrace();
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
	}

}

