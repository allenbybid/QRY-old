package bid.allenby.qry;

import android.app.*;
import android.os.*;
import android.net.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.database.sqlite.*;

public class InfoGet extends Activity 
{
	private String dbn="sys_data";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.loading);
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;
		if(currentapiVersion >= 19) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		Intent intent =getIntent();
		Uri uri =intent.getData();
		String auth=intent.getExtras().getString("auth");
		String nick=intent.getExtras().getString("nick");
		String uid=intent.getExtras().getString("uid");
		Toast.makeText(InfoGet.this,"👏欢迎，"+nick+"。",Toast.LENGTH_LONG).show();
		ContentValues values = new ContentValues();
		values.put("id",8);
		values.put("name",auth);
		DatabaseHelper dbHleper = new DatabaseHelper(InfoGet.this,dbn, 2);  
		SQLiteDatabase sqliteDatabase = dbHleper.getWritableDatabase();  
		sqliteDatabase.insert("sys", null, values); 
		sqliteDatabase.close();
		Intent intent2 = new Intent();
		intent2.setClass(InfoGet.this,meanc.class);
		startActivity(intent2);
		finish();
	}
}

