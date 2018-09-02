package bid.allenby.qry;
import java.io.*;
import android.content.*;
import android.net.*;
import android.app.Activity;
import android.support.v4.widget.*;
import com.jaeger.library.*;
import android.support.v7.app.ActionBarActivity;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.support.v7.widget.*;
import java.net.*;
import android.database.sqlite.*;
import android.database.*;
public class settings extends ActionBarActivity 
{
private Spinner spinner;
private Spinner spinner2;
private String dbn="sys_data";
private ArrayAdapter<String> adapter;
private ArrayAdapter<String> adapter2;
private static final String[] vplayer={"VP","DP"};
private static final String[] down={"DM","FD"};
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏 
        setContentView(R.layout.settings);
		System.out.println("s1");
		StatusBarUtil.setTransparent(settings.this);
		spinner=(Spinner)findViewById(R.id.setplay);//将可选内容与ArrayAdapter连接起来
adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,vplayer);//设置下拉列表的风格
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//将adapter 添加到spinner中
spinner.setAdapter(adapter);//添加事件Spinner事件监听
		//Toast.makeText(getApplicationContext(), seadata(6), Toast.LENGTH_SHORT).show();
spinner.setSelection(Integer.parseInt(seadata(6)),true);
		//为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
		System.out.println("s3");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String ccmm=Integer.toString(position);
					if(ccmm.equals(seadata(6))==false){
					setdata(6,ccmm);
					Toast.makeText(getApplicationContext(), "😊设置成功。", Toast.LENGTH_SHORT).show();
					//alert.setText("您当前选择的是："+adapter.getItem(position));
				}
}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
					Toast.makeText(getApplicationContext(), "😁请选择使用的播放器!", Toast.LENGTH_SHORT).show();

				}
			});
		spinner2=(Spinner)findViewById(R.id.setdown);//将可选内容与ArrayAdapter连接起来
		adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,down);//设置下拉列表的风格
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//将adapter 添加到spinner中
		spinner2.setAdapter(adapter2);//添加事件Spinner事件监听
		//为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
		spinner2.setSelection(Integer.parseInt(seadata(7)),true);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String ccmm=Integer.toString(position);
					if(ccmm.equals(seadata(7))==false){
					setdata(7,ccmm);
					Toast.makeText(getApplicationContext(), "😊设置成功。", Toast.LENGTH_SHORT).show();
					}
					//alert.setText("您当前选择的是："+adapter2.getItem(position));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
					Toast.makeText(getApplicationContext(), "😁请选择使用的下载器!", Toast.LENGTH_SHORT).show();

				}
			});
			}
			public void setdata(int id,String name){
				ContentValues values = new ContentValues();
				values.put("id",id);  
				values.put("name",name);  
				// 创建DatabaseHelper对象  
				DatabaseHelper dbHleper = new DatabaseHelper(settings.this, dbn, 2);  
				// 得到一个可写的SQLiteDatabase对象  
				SQLiteDatabase sqliteDatabase = dbHleper.getWritableDatabase();  
				// 调用insert方法，就可以将数据插入到数据库当中  
				// 第一个参数:表名称  
				// 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值  
				// 第三个参数：ContentValues对象  
				sqliteDatabase.insert("sys", null, values); 
				sqliteDatabase.close();
			}
			public String seadata(int id){
				String id2 = null;  
				String name = null;  

				DatabaseHelper dbHelper = new DatabaseHelper(settings.this,dbn,2);  

				SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

				Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
														 "name" }, "id="+id,null, null, null, null);  

				while (cursor.moveToNext()) {  
					id2 = cursor.getString(cursor.getColumnIndex("id"));  
					name = cursor.getString(cursor.getColumnIndex("name"));  
				}
				System.out.println(name);
				sqliteDatabase.close();
				if(name==null){
					name="0";
				}
				System.out.println(name);
				return name;
			}
}
