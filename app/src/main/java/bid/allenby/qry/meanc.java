package bid.allenby.qry;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.app.ActionBarActivity;
import android.app.*;
import android.os.*;
import android.R.*;
import java.io.*;
import android.widget.*;
import android.view.*;
import android.content.*;
//import com.mashape.unirest.http.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import android.webkit.*;
import android.database.sqlite.*;
import android.database.*;
import java.util.regex.*;
import org.apache.http.*;
import org.apache.http.util.*;
import android.net.*;
import java.net.*;
import android.support.v4.widget.*;
import com.jaeger.library.*;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import android.widget.AdapterView.*;
import android.widget.Toolbar.*;
import android.util.*;
import android.content.pm.*;
import me.shenfan.updateapp.*;

public class meanc extends ActionBarActivity 
{
private TextView uri;
private TextView rlcon;
private Button submit;
private int ifco,idc;
private JsonBean json;
private Button rlsub;
private FloatingActionButton topCenterButton;
private FloatingActionMenu topCenterMenu;
private String dbn="sys_data";
	public static final int SUCCESS= 1;
	private Handler handler3 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SUCCESS:
					String mss=(String) msg.obj;
					if(mss.indexOf("true")!=-1){
						JsonBean4 json= new Gson().fromJson(mss,JsonBean4.class);
						Toast.makeText(meanc.this,"👏欢迎，"+json.nickname+"。", Toast.LENGTH_SHORT).show();
						if(json.canuse.equals("0")){
							Toast.makeText(meanc.this,"😜每日播放次数已达上限！", Toast.LENGTH_SHORT).show();
						}else{
							ProgressBar iuccess=(ProgressBar) findViewById(R.id.iuccess);
							System.out.println("1");
							Toast.makeText(meanc.this,"😊今日播放次数剩余:"+json.canuse+"次。", Toast.LENGTH_SHORT).show();
							uri=(TextView) findViewById(R.id.uri);
							String url;
							url=uri.getText().toString();
							
							System.out.println("1");
							int base;
							if(iskuURL(url)){
								base=2;
							}else if(isqyURL(url)){
								base=1;
							}else{
								base=0;
							}
							System.out.println(base);
							if(base!=0){
								//System.out.println(urj);
								//Toast.makeText(meanc.this,urj), Toast.LENGTH_SHORT).show();
								iuccess.setVisibility(View.VISIBLE);
								String urk;
								if(base==1){
									url=qyURL(url);
									urk="http://app.ck921.com/qiyi.php?url="+url;

								}else if(base==2){
									url=kuURL(url);
									urk="http://app.ck921.com/youku.php?url="+url;

								}else{
									urk="http://www.allenby.bid/";
									url="";
								}
								System.out.println(urk);
								ContentValues values = new ContentValues();
								values.put("id", 1);  
								values.put("name",urk);  
								DatabaseHelper dbHleper = new DatabaseHelper(meanc.this,dbn, 2);  
								SQLiteDatabase sqliteDatabase = dbHleper.getWritableDatabase();  
								sqliteDatabase.insert("sys", null, values); 
								sqliteDatabase.close();
								System.out.println("2");
								sendRequestWithHttpClient();
								submit.setEnabled(false);
								rlsub.setEnabled(false);
								//iuccess.setVisibility(View.VISIBLE);
							}else{
								Toast.makeText(meanc.this,"您输入的地址不规范，请重新输入！", Toast.LENGTH_SHORT).show();
							//Toast.makeText(MainActivity.this,url, Toast.LENGTH_SHORT).show();       
						}
						}
					}else{
						if(!isAvilible(getApplicationContext(),"bid.allenby.login")){
						Toast.makeText(meanc.this,"😲请先安装稀树草原。", Toast.LENGTH_SHORT).show();
						}else{
						ProgressBar iuccess=(ProgressBar) findViewById(R.id.iuccess);
						Toast.makeText(meanc.this,"😲登录已过期！", Toast.LENGTH_SHORT).show();
						//exitlo(null);
						submit.setEnabled(true);
						Button login=(Button) findViewById(R.id.login);
						iuccess.setVisibility(View.INVISIBLE);
						login.setVisibility(View.VISIBLE);
						}
					}
					break;
			}
		}
	};

	
	/**
	 * 检查手机上是否安装了指定的软件
	 * @param context
	 * @param packageName
	 * @return
	 */
	public boolean isAvilible(Context context, String packageName) {
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
	public void exitlo(View v){
		DatabaseHelper dbHelper = new DatabaseHelper(meanc.this,dbn, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String whereClause =  "id=?";  
		String[] whereArgs = new String[] {String.valueOf(8)};  
		db.delete("sys",whereClause, whereArgs);
		db.close();
	}
	    private Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	           super.handleMessage(msg);
			           switch (msg.what) {
					             case SUCCESS:
							   ProgressBar iuccess=(ProgressBar) findViewById(R.id.iuccess);
							   iuccess.setVisibility(View.GONE);
					           String response = (String) msg.obj;
							   System.out.println("6");
							   
							   int ife4=response.indexOf("<html>");
							   int ife5=response.indexOf("<xml>");
							   if(ife5!=-1 || ife4!=-1){
								   ifco=1;
								   idc=1;
								   json=null;
							   }else{
								   ifco=0;
							   json= new Gson().fromJson(response, JsonBean.class);
							   int ife2=json.mp4.indexOf(".taobao.com/");
							   int ife3=json.mp4.indexOf(".qq.com/");
							   
							   if(ife2!=-1 || ife3!=-1||ifco==1){
								   idc=1;
							   }else{
								   idc=0;
							   }
							   }
							   System.out.println(idc);
							   if(idc==1){
								   Toast.makeText(meanc.this,"😲服务器休眠中，请稍后再试。", Toast.LENGTH_SHORT).show();
								   //Toast.makeText(meanc.this,"😲加载休眠短片中...", Toast.LENGTH_SHORT).show();
							   }
							   ContentValues values = new ContentValues();
							   values.put("id", 1);
							   uri=(TextView) findViewById(R.id.uri);
							   String yul=uri.getText().toString();
							   values.put("yurl",yul);
							   
							   if(idc==0){
							   values.put("kurl",json.mp4);
							   values.put("ytitle",json.code);
							   }else{
							   //values.put("kurl","https://tv6.byr.cn/hls/dnhd.m3u8");
							   values.put("kurl","QRY://ERROR");
							   values.put("ytitle","休眠短片");
							   }
							   DatabaseHelper dbHleper = new DatabaseHelper(meanc.this,dbn, 2);  
							   SQLiteDatabase sqliteDatabase = dbHleper.getWritableDatabase();  
							   sqliteDatabase.insert("uridata", null, values); 
							   sqliteDatabase.close();
							   if(idc==0){
							   ContentValues value2 = new ContentValues();
							   value2.put("ytitle",json.code);
							   value2.put("kurl",json.mp4);
							   value2.put("yurl",yul);
							   DatabaseHelper dbHleper2 = new DatabaseHelper(meanc.this,dbn, 2);  
							   SQLiteDatabase sqliteDatabase2 = dbHleper2.getWritableDatabase();  
							   sqliteDatabase2.insert("cache", null, value2); 
							   sqliteDatabase2.close();
							   }
							   Intent intent = new Intent();
							   intent.setClass(meanc.this,show.class);
							   submit=(Button) findViewById(R.id.submit);
							   rlsub=(Button) findViewById(R.id.rlsub);
					           submit.setEnabled(true);
							   rlsub.setEnabled(true);
							   System.out.println("7");
							   startActivity(intent);
							   //finish();
									 break;
					          }            
		         }
		
	     };
	private Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					Toast.makeText(meanc.this,"您输入的地址不规范，请重新输入！", Toast.LENGTH_SHORT).show();
					final Intent it = new Intent(meanc.this,MainActivity.class); //你要转向的Activity   
					Intent intent = new Intent();
					intent.setClass(meanc.this,meanc.class);
					System.out.println("7");
					startActivity(intent);
					finish();
					break;
			}            
		}

	};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.meanc);
		TextView main=(TextView) findViewById(R.id.khide);
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;
		/*if(currentapiVersion >= 19) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}*/
		StatusBarUtil.setTransparent(meanc.this);
        ////////////////////////////////////////////////////////
		
        // Set up the large red button on the top center side
        // With custom button and content sizes and margins
        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        topCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_red_selector)
                .setPosition(FloatingActionButton.POSITION_TOP_CENTER)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder tCSubBuilder = new SubActionButton.Builder(this);
        tCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        SubActionButton.Builder tCRedBuilder = new SubActionButton.Builder(this);
        tCRedBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_red_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);

        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        tCSubBuilder.setLayoutParams(blueParams);
        tCRedBuilder.setLayoutParams(blueParams);

        ImageView tcIcon1 = new ImageView(this);
        ImageView tcIcon2 = new ImageView(this);
        ImageView tcIcon3 = new ImageView(this);
        ImageView tcIcon4 = new ImageView(this);
        ImageView tcIcon5 = new ImageView(this);
        ImageView tcIcon6 = new ImageView(this);

        tcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_history));
        tcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_about));
        tcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting));
        tcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_update));
        tcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.ic_cache));
        tcIcon6.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_cancel));

        SubActionButton tcSub1 = tCSubBuilder.setContentView(tcIcon1, blueContentParams).build();
        SubActionButton tcSub2 = tCSubBuilder.setContentView(tcIcon2, blueContentParams).build();
        SubActionButton tcSub3 = tCSubBuilder.setContentView(tcIcon3, blueContentParams).build();
        SubActionButton tcSub4 = tCSubBuilder.setContentView(tcIcon4, blueContentParams).build();
        SubActionButton tcSub5 = tCSubBuilder.setContentView(tcIcon5, blueContentParams).build();
        SubActionButton tcSub6 = tCRedBuilder.setContentView(tcIcon6, blueContentParams).build();


        // Build another menu with custom options
        topCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(tcSub2, tcSub2.getLayoutParams().width, tcSub2.getLayoutParams().height)
                .addSubActionView(tcSub3, tcSub3.getLayoutParams().width, tcSub3.getLayoutParams().height)
                .addSubActionView(tcSub4, tcSub4.getLayoutParams().width, tcSub4.getLayoutParams().height)
                .addSubActionView(tcSub5, tcSub5.getLayoutParams().width, tcSub5.getLayoutParams().height)
                .addSubActionView(tcSub6, tcSub6.getLayoutParams().width, tcSub6.getLayoutParams().height)
                .setRadius(redActionMenuRadius)
                .setStartAngle(0)
                .setEndAngle(180)
                .attachTo(topCenterButton)
                .build();

        topCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
            }
        });
		tcSub1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(meanc.this, "历史", Toast.LENGTH_SHORT).show();

					Intent intent = new Intent();
					intent.setClass(meanc.this,history.class);
					startActivity(intent);
				}
			});
        tcSub2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(meanc.this, "关于", Toast.LENGTH_SHORT).show();

					Intent intent = new Intent();
					intent.setClass(meanc.this,about.class);
					startActivity(intent);
				}
			});
		// set listeners seperately
        tcSub3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(meanc.this, "设置", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(meanc.this,settings.class);
					startActivity(intent);
				}
			});
        tcSub4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(meanc.this, "刷新", Toast.LENGTH_SHORT).show();
				}
			});
		tcSub5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(meanc.this, "缓存", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(meanc.this,opencaches.class);
					startActivity(intent);
				}
			});
		tcSub6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					topCenterMenu.close(true);
				}
			});
		submit=(Button) findViewById(R.id.submit);
		rlsub=(Button) findViewById(R.id.rlsub);
    }
	public void subnna(View v) {
		switch (v.getId()) {
			case R.id.submit:
				ProgressBar iuccess=(ProgressBar) findViewById(R.id.iuccess);
				iuccess.setVisibility(View.VISIBLE);
				submit.setEnabled(false);
				String id2 = null;  
				String name = null;  

				DatabaseHelper dbHelper = new DatabaseHelper(meanc.this,dbn,2);  

				SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

				Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
														 "name" }, "id=8",null, null, null, null);  

				while (cursor.moveToNext()) {  
					id2 = cursor.getString(cursor.getColumnIndex("id"));  
					name = cursor.getString(cursor.getColumnIndex("name"));  
				}
				sqliteDatabase.close();

				if(name==null){
					submit.setEnabled(true);
					Toast.makeText(meanc.this,"😢未登录,请先登录！", Toast.LENGTH_SHORT).show();
					Button login=(Button) findViewById(R.id.login);
					iuccess.setVisibility(View.INVISIBLE);
					login.setVisibility(View.VISIBLE);
				}else{
					auth2();
				}

				//Toast.makeText(MainActivity.this,url, Toast.LENGTH_SHORT).show();
				break;        
			case R.id.login:
				/*uri=(TextView) findViewById(R.id.uri);
				 String url=uri.getText().toString();*/
				Intent intent=new Intent("bid.login.MYACTION",Uri.parse("ablogin://abqry"));
				//intent.putExtra("v1",url);
				startActivity(intent);
				//startActivityForResult(intent,1);
				finish();
				break;  	
         case R.id.rlsub:
			    rlcon=(TextView) findViewById(R.id.rlcon);
				String base6=rlcon.getText().toString();
				//String kurlf=new String(Base64.decode(base6.getBytes(), Base64.DEFAULT));
				String kur3=URLDecoder.decode(base6);
				String kurb3=kur3.replaceAll(" ","+");
				System.out.println(kurb3);
				ZipUtils zcns=new ZipUtils();
				//String kur2=zcns.unzip(kurb3);
				//System.out.println(kur2);
				String kuco=zcns.gunzip(kurb3);
				System.out.println(kuco);
				if(kuco!=null&&kuco.indexOf("sourceurl")!=-1&&kuco.indexOf("recoureurl")!=-1&&kuco.indexOf("sourcetitle")!=-1){
				JsonBean2 json= new Gson().fromJson(kuco, JsonBean2.class);
				System.out.println(json.sourcetitle);
				if(isTrueURL2(json.recoureurl)){
					ContentValues values = new ContentValues();
					values.put("id", 1);
					values.put("yurl",json.sourceurl);
					values.put("kurl",json.recoureurl);
					values.put("ytitle","R链_"+json.sourcetitle);
					DatabaseHelper dbHleper = new DatabaseHelper(meanc.this,dbn, 2);  
					SQLiteDatabase sqliteDatabase3 = dbHleper.getWritableDatabase();  
					sqliteDatabase3.insert("uridata", null, values); 
					sqliteDatabase3.close();
					submit.setEnabled(false);
					rlsub.setEnabled(false);
					Intent intent2 = new Intent();
					intent2.setClass(meanc.this,show.class);
					System.out.println("Rlink");
					startActivity(intent2);
					submit.setEnabled(true);
					rlsub.setEnabled(true);
					}else{
					Toast.makeText(meanc.this, "非标准R链，请重新输入！", Toast.LENGTH_SHORT).show();
				}
					//finish();
				}else{
				Toast.makeText(meanc.this,"非标准R链，请重新输入！", Toast.LENGTH_SHORT).show();
				}
			 break;
			default:
				break;
		}
	}
	/**
	 * 验证是否是爱奇艺URL
	 * @author YOLANDA
	 * @param url
	 * @return
	 */
	public static boolean isqyURL(String url){
		String regex = "^(https?)://(m|www).iqiyi.com/v_[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
		Pattern patt = Pattern. compile(regex);
		Matcher matcher = patt.matcher(url);
		return matcher.matches();
	}
	public static String qyURL(String url){
		String[] pxi=url.split("v_");
	    String[] pxa=pxi[pxi.length-1].split(".html");
		String pxb="http://www.iqiyi.com/v_"+pxa[0]+".html";
		return pxb;
	}
	
	/**
	 * 验证是否是优酷URL
	 * @author YOLANDA
	 * @param url
	 * @return
	 */
	public static boolean iskuURL(String url){
		String regex = "^(https?)://m.youku.com/video/id_[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
		Pattern patt = Pattern. compile(regex);
		Matcher matcher = patt.matcher(url);
		return matcher.matches();
	}
	public static String kuURL(String url){
		String[] pxi=url.split("id_");
	    String[] pxa=pxi[pxi.length-1].split(".html");
		String pxb="http://v.youku.com/v_show/id_"+pxa[0]+".html";
		return pxb;
	}
	/*http://m.youku.com/video/id_XMjYwOTQ2Nzg1Mg==.html?spm=a2h12.8251612.2408645.1

	 http://v.youku.com/v_show/id_XMjYwOTQ2Nzg1Mg==.html?from=s7.8-1.1
*/
	/**
	 * 验证是否是URL
	 * @author YOLANDA
	 * @param url
	 * @return
	 */
	public static boolean isTrueURL2(String url){
		String regex = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
		Pattern patt = Pattern. compile(regex);
		Matcher matcher = patt.matcher(url);
		return matcher.matches();
	}
	public String seadata(int id){
		String id2 = null;  
		String name = null;  

		DatabaseHelper dbHelper = new DatabaseHelper(meanc.this,dbn,2);  

		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

		Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
												 "name" }, "id="+id,null, null, null, null);  

		while (cursor.moveToNext()) {  
			id2 = cursor.getString(cursor.getColumnIndex("id"));  
			name = cursor.getString(cursor.getColumnIndex("name"));  
		}
		sqliteDatabase.close();
		return name;
	}
	private void sendRequestWithHttpClient() {
		        new Thread(new Runnable() {
				            @Override
			             public void run() {
							 String id = null;  
							 String name = null;  

							 DatabaseHelper dbHelper = new DatabaseHelper(meanc.this,dbn,2);  

							 SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

							 Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
																	  "name" }, "id=?", new String[] { "1" }, null, null, null);  

							 while (cursor.moveToNext()) {  
								 id = cursor.getString(cursor.getColumnIndex("id"));  
								 name = cursor.getString(cursor.getColumnIndex("name"));  
							 }
							 sqliteDatabase.close();
							 System.out.println("3");
				                //用HttpClient发送请求，分为五步
				                 //第一步：创建HttpClient对象
				                HttpClient httpCient = new DefaultHttpClient();
				               //第二步：创建代表请求的对象,参数是访问的服务器地址
				                HttpGet httpGet = new HttpGet(name);
				             
					                try {
					                     //第三步：执行请求，获取服务器发还的相应对象
					                    HttpResponse httpResponse = httpCient.execute(httpGet);
					                //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
					                     if (httpResponse.getStatusLine().getStatusCode() == 200) {
						                         //第五步：从相应对象当中取出数据，放到entity当中
						                         HttpEntity entity = httpResponse.getEntity();
						                         String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
							                        System.out.println("4");
							                        //在子线程中将Message对象发出去
					                        Message message = new Message();
						                         message.what = SUCCESS;
							                        message.obj = response.toString();
													int ife2=response.indexOf("解析失败");
													System.out.println("4.1");
													System.out.println(ife2);
													
													if(ife2!=-1){
													Message message2=new Message();
												    message2.what=1;
												    handler2.sendMessage(message2);
													}else{
                                                     System.out.println("5");
							                         handler.sendMessage(message);
											        }
							                    }
						                     
						                 } catch (Exception e) {
						                  // TODO Auto-generated catch block
					                    e.printStackTrace();
						                 }
					             
					             }
	   }).start();//这个start()方法不要忘记了               
}   
	
	private void auth2() {
		new Thread(new Runnable() {
				@Override
				public void run() {
					String id2= null;  
					String name2 = null;  

					DatabaseHelper dbHelper3 = new DatabaseHelper(meanc.this,dbn,2);  

					SQLiteDatabase sqliteDatabase3 = dbHelper3.getReadableDatabase();  

					Cursor cursor2 = sqliteDatabase3.query("sys", new String[] { "id",  
															   "name" }, "id=?", new String[] { "1" }, null, null, null);  

					while (cursor2.moveToNext()) {  
						id2= cursor2.getString(cursor2.getColumnIndex("id"));  
						name2= cursor2.getString(cursor2.getColumnIndex("name"));  
					}
					sqliteDatabase3.close();
					String id = null;  
					String name = null;  

					DatabaseHelper dbHelper = new DatabaseHelper(meanc.this,dbn,2);  

					SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

					Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
															 "name" }, "id=?", new String[] { "8" }, null, null, null);  

					while (cursor.moveToNext()) {  
						id = cursor.getString(cursor.getColumnIndex("id"));  
						name = cursor.getString(cursor.getColumnIndex("name"));  
					}
					sqliteDatabase.close();
					System.out.println("3");
					//用HttpClient发送请求，分为五步
					//第一步：创建HttpClient对象
					HttpClient httpCient2 = new DefaultHttpClient();
					//第二步：创建代表请求的对象,参数是访问的服务器地址
					HttpGet httpGet2 = new HttpGet("http://o.allenby.bid/php/api.php?action=auth&auth="+name+"&jurl="+name2);
					try {
						//第三步：执行请求，获取服务器发还的相应对象
						HttpResponse httpResponse2= httpCient2.execute(httpGet2);
						//第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
						if (httpResponse2.getStatusLine().getStatusCode() == 200) {
							//第五步：从相应对象当中取出数据，放到entity当中
							HttpEntity entity = httpResponse2.getEntity();
							String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
							System.out.println("4");
							//在子线程中将Message对象发出去
							Message message2 = new Message();
							message2.what = SUCCESS;
							message2.obj = response.toString();
							handler3.sendMessage(message2);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();//这个start()方法不要忘记了     
	}   
}
