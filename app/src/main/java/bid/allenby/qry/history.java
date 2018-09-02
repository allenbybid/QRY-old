package bid.allenby.qry;

import android.support.v4.widget.*;
import com.jaeger.library.*;
import android.database.sqlite.*;
import android.database.*;
import android.support.v7.app.ActionBarActivity;
import android.app.*;
import android.os.*;
import android.R.*;
import java.io.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import android.widget.AdapterView.*;
import android.util.*;
import java.util.regex.*;
import java.net.*;
import com.google.gson.*;
public class history extends ActionBarActivity 
{
	private String dbn="sys_data";
    private HistoryList adapter;
	private ListView listView;
	private List<HashMap<String, Object>> list;
	//m
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
		System.out.println("H1");
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏 
        setContentView(R.layout.history);
		list = new ArrayList<HashMap<String,Object>>();
		StatusBarUtil.setTransparent(history.this);
		System.out.println("H2");
		DatabaseHelper dbHelper = new DatabaseHelper(history.this,dbn, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor3 = db.rawQuery("select count(*) from cache", null);
		cursor3.moveToNext();
		System.out.println("H3");
		Long kclis=cursor3.getLong(0);
		System.out.println(kclis);
		if(kclis>=1){
		TextView count=(TextView) findViewById(R.id.count);
		count.setText("共"+kclis+"条");
		//System.out.println(kclis);
		//Cursor c = db.rawQuery("select count(*) from cache", null);
		Cursor cursor = db.rawQuery("select * from cache", null);
		String ffs="";
		list.clear();
		while (cursor.moveToNext()) {
			int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
			String yuri = cursor.getString(1);//获取第二列的值
			String ytitle = cursor.getString(2);//获取第三列的值
			String kuri = cursor.getString(3);
			String kuri2=Base64.encodeToString(kuri.getBytes(), Base64.DEFAULT);
			//System.out.println(id);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id",id);
			map.put("yurl",yuri);
			map.put("kurl",kuri);
			map.put("ytitle",ytitle);
			map.put("titl",id+"."+ytitle);
			map.put("desc","输入:"+yuri+"\n输出:"+kuri2);
			/*map.put("date", );
			map.put("url", foos[j].link);
			map.put("ideam", foos[j].id);*/
			list.add(map);
			//System.out.println("ytitle");
		}
		listView = (ListView) findViewById(R.id.list);
		adapter = new HistoryList(getLayoutInflater(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new listViewOnItemClickListener());
		cursor.close();
		db.close(); 							 
		 
		/*Cursor c2 = db.rawQuery("select * from cache", null);
		List<Person> persons = new ArrayList<Person>();
		while(c.moveToNext()){
			Person2 p = new Person(c2.getInt(0),c2.getString(1),c2.getString(2),getString(3));
			persons.add(p);
		}*/
		}else{
		TextView tip=(TextView) findViewById(R.id.tips);
		tip.setText("你还没有过播放记录，请尽快开始吧。");
		}
	}
	public class listViewOnItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
								int position, long id)
		{
			ListView listView = (ListView)parent;
			HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
			
			/*Intent intent = new Intent();
			intent.setClass(history.this, show.class);
			//intent.putExtra("newsid", map.get("ideam"));
			//intent.putExtra("newstitle", map.get("title"));

			startActivity(intent);*/
			//finish();
			ContentValues values = new ContentValues();
			values.put("id", 1);
			values.put("yurl",map.get("yurl"));
			values.put("kurl",map.get("kurl"));
			values.put("ytitle",map.get("ytitle"));
			DatabaseHelper dbHleper = new DatabaseHelper(history.this,dbn, 2);  
			SQLiteDatabase sqliteDatabase = dbHleper.getWritableDatabase();  
			sqliteDatabase.insert("uridata", null, values); 
			sqliteDatabase.close();
			Intent intent = new Intent();
			intent.setClass(history.this,show.class);
			startActivity(intent);
			//finish();
		}
	}
	public void icoas(View v) {
		switch (v.getId()) {
			case R.id.del1:
			LayoutInflater factory = LayoutInflater.from(history.this);//提示框  
            final View view = factory.inflate(R.layout.alertdel1, null);//这里必须是final的  
            final EditText edit=(EditText)view.findViewById(R.id.delidc);//获得输入框对象  
            edit.setHint("");//输入框默认值  
            new AlertDialog.Builder(history.this)  
                .setTitle("请输入要删除内容的ID号")//提示框标题  
                .setView(view)  
                .setPositiveButton("确定",//提示框的两个按钮  
                    new android.content.DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog,int which) {  
                    //事件处理  
                        String delidc= edit.getText().toString();
						if(delidc.equals("")){
						Toast.makeText(getApplicationContext(), "ID不能为空", Toast.LENGTH_SHORT).show();
							}else{
						if(isInteger(delidc)){
					    DatabaseHelper dbHelper = new DatabaseHelper(history.this,dbn, 2);
						SQLiteDatabase db = dbHelper.getReadableDatabase();
						String  DELETE_DATA = "DELETE FROM cache WHERE id="+delidc;
						db.execSQL(DELETE_DATA);
						db.close();
							Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
							finish();
							Intent intent = new Intent(history.this,history.class);
							startActivity(intent);
						}else{
						Toast.makeText(getApplicationContext(), "ID仅能为单个且为数字。", Toast.LENGTH_SHORT).show();
						}
						}
                    }
             }).setNegativeButton("取消", null).create().show();
				break;
				case R.id.sharerl:
				LayoutInflater factory2 = LayoutInflater.from(history.this);//提示框  
				final View view2 = factory2.inflate(R.layout.alertdel1, null);//这里必须是final的  
				final EditText edit2=(EditText)view2.findViewById(R.id.delidc);//获得输入框对象  
				edit2.setHint("");//输入框默认值  
				new AlertDialog.Builder(history.this)  
					.setTitle("请输入要分享内容的ID号")//提示框标题  
					.setView(view2)  
					.setPositiveButton("确定",//提示框的两个按钮  
                    new android.content.DialogInterface.OnClickListener() {  
						@Override  
						public void onClick(DialogInterface dialog,int which) {  
							//事件处理  
							String delidc= edit2.getText().toString();
							if(delidc.equals("")){
								Toast.makeText(getApplicationContext(), "ID不能为空", Toast.LENGTH_SHORT).show();
							}else{
								if(isInteger(delidc)){
									int idi=0;
									String ytitli=null;
									String kuri=null;
									String yuri=null;
									System.out.println("H3");
									DatabaseHelper dbHelper = new DatabaseHelper(history.this,dbn, 2);
									SQLiteDatabase db = dbHelper.getReadableDatabase();
									Cursor cursor =db.query("cache", new String[] { "id","ytitle","yurl","kurl"}, "id=?", new String[] { delidc }, null, null, null);
									while (cursor.moveToNext()) {  
									idi= cursor.getInt(cursor.getColumnIndex("id"));  
									ytitli=cursor.getString(cursor.getColumnIndex("ytitle"));
									kuri= cursor.getString(cursor.getColumnIndex("kurl"));
									yuri = cursor.getString(cursor.getColumnIndex("yurl"));
									}
									System.out.println(kuri);
									db.close();
									if(kuri!=null&&kuri!=""){
										if(isTrueURL2(kuri)==false){
											Toast.makeText(getApplicationContext(), "该内容无法分享。", Toast.LENGTH_SHORT).show();
										}else{
										Long time2=System.currentTimeMillis();
										
										Map<String,Object> map = new HashMap<String,Object>();
										map.put("soureurl",yuri);
										map.put("souretitle",ytitli);
										map.put("recoureurl",kuri);
										map.put("lastmaketime",time2.toString());
											Gson gson = new Gson();
											String rl1=gson.toJson(map);
											System.out.println("H5");
											ZipUtils zipu=new ZipUtils();
											String rl2=zipu.gzip(rl1);
											//String rl3=zipu.zip(rl2);
											//String rl4=new String(Base64.decode(rl3.getBytes(), Base64.DEFAULT));
											String rlink=URLEncoder.encode(rl2);
											System.out.println(rlink);
											ContentValues value2 = new ContentValues();
											value2.put("id",4);
											value2.put("name",rlink);
											DatabaseHelper dbHleper2 = new DatabaseHelper(history.this,dbn, 2);  
											SQLiteDatabase sqliteDatabase2 = dbHleper2.getWritableDatabase();  
											sqliteDatabase2.insert("sys", null, value2); 
											sqliteDatabase2.close();
											Toast.makeText(getApplicationContext(), "🙌R链生成完成。", Toast.LENGTH_SHORT).show();
											AlertDialog.Builder builder = new AlertDialog.Builder(history.this);
											builder.setIcon(R.drawable.ic_launcher);

											builder.setTitle("确认");

											builder.setMessage("R链:\n"+rlink+"\nP.S.R链一般情况可存活12小时，分享过于古老的视频可能无法正常使用。");

											builder.setPositiveButton("复制", new DialogInterface.OnClickListener()
												{
													@Override
													public void onClick(DialogInterface dialog, int which)
													{
														ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
														// 将文本内容放到系统剪贴板里。
														String id = null;  
														String name = null;  

														DatabaseHelper dbHelper = new DatabaseHelper(history.this, dbn, 2);  

														SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

														Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
																								 "name" }, "id=?", new String[] { "4" }, null, null, null);  

														while (cursor.moveToNext())
														{  
															id = cursor.getString(cursor.getColumnIndex("id"));  
															name = cursor.getString(cursor.getColumnIndex("name"));  
														}
														sqliteDatabase.close();
														cm.setText(name);
														Toast.makeText(getApplicationContext(), "😃R链已复制，快去分享吧！", Toast.LENGTH_SHORT).show();
													}
												});
											builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
												{
													@Override
													public void onClick(DialogInterface dialog, int which)
													{

													}
												});

											builder.show();
									
									}
									}else{
										Toast.makeText(getApplicationContext(), "ID号不存在。", Toast.LENGTH_SHORT).show();
									}
									}
									}
									}
					}).setNegativeButton("取消", null).create().show();
					break;
					case R.id.delall:
				AlertDialog.Builder builder = new AlertDialog.Builder(history.this);

				builder.setIcon(R.drawable.ic_launcher);

				builder.setTitle("确认");

				builder.setMessage("确认清空历史记录？");

				builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							DatabaseHelper dbHelper = new DatabaseHelper(history.this,dbn, 2);
							SQLiteDatabase db = dbHelper.getReadableDatabase();
							db.execSQL("DELETE FROM cache");
							//db.execSQL("create table cache(id INTEGER PRIMARY KEY AUTOINCREMENT,yurl TEXT,ytitle TEXT,kurl TEXT)");
							db.close();
							Toast.makeText(getApplicationContext(), "清空完成", Toast.LENGTH_SHORT).show();
							finish();
							Intent intent = new Intent(history.this,history.class);
							startActivity(intent);
						}
						});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
						}
					});

				builder.show();
					break;
				}
				}
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
	public static boolean isInteger(String str) {    
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
		return pattern.matcher(str).matches();    
	}  
}
