package bid.allenby.qry;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tone.library.DownloadDB;
import com.tone.library.DownloadInfo;
import com.tone.library.DownloadListener;
import com.tone.library.DownloadManager;

import java.util.ArrayList;
import com.jaeger.library.*;
import android.database.sqlite.*;
import android.database.*;
import android.widget.*;
import android.content.*;
import com.tone.library.*;

public class caches2 extends AppCompatActivity {
	private String dbn="sys_data";
	private String dbd="filedownloader.db";
	private String dbdt="downloadinfo";
    Adapter adapter;
    ArrayList<DownloadInfo> downloads = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caches2);
		StatusBarUtil.setTransparent(caches2.this);
		String id = null;  
		String ytitle = null;  
		String kurl=null;
		DatabaseHelper dbHelper = new DatabaseHelper(caches2.this,dbn,2);  

		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

		Cursor cursor = sqliteDatabase.query("uridata", new String[] { "id",  
												 "yurl","kurl","ytitle"}, "id=?", new String[] { "1" }, null, null, null);  
		while (cursor.moveToNext()) {  
			id = cursor.getString(cursor.getColumnIndex("id"));  
			ytitle = cursor.getString(cursor.getColumnIndex("ytitle"));
			kurl = cursor.getString(cursor.getColumnIndex("kurl"));
		}
		sqliteDatabase.close();
		int fr;
		if(kurl.equals("QRY://ERROR")){
			fr=1;
			Toast.makeText(getApplicationContext(), "😳该内容不支持被缓存", Toast.LENGTH_SHORT).show();
			/*final Intent it = new Intent(caches.this,meanc.class); //你要转向的Activity   
			 Timer timer = new Timer();  
			 TimerTask task = new TimerTask() {  
			 @Override  
			 public void run() {   
			 startActivity(it); //执行 
			 finish();
			 }  
			 }; 
			 timer.schedule(task,2000); //2秒后*/
			//以上BUG 无法跳转
			finish();
		}else{
			fr=0;
		}
		if(fr==0){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter =new Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        downloads = DownloadDB.getInstance(this).getAllDownLoadInfo();
        adapter.setDownloadInfos(downloads);
        recyclerView.setAdapter(adapter);
        DownloadManager.getInstance(this).setListener(new DownloadListener() {
            @Override
            public void onStart(DownloadInfo info) {
				System.out.println("C3");
				//Toast.makeText(getApplicationContext(), "😉缓存开始", Toast.LENGTH_SHORT).show();
                Log.d("caches2", "onStart: "+info.getFileName());
            }

            @Override
            public void onProgress(DownloadInfo info) {
                Log.d("caches2", "onProgress: "+info.getFileName()+"  progress==" +(int) (info.getCurrentSize()*100/info.getTotalSize())+"%");

                Message msg = hander.obtainMessage();
                msg.obj = info;
                msg.what=1;
                hander.sendMessage(msg);
            }

            @Override
            public void onStop(DownloadInfo info) {
				//Toast.makeText(getApplicationContext(), "😞缓存暂停", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(DownloadInfo info) {
				//Toast.makeText(getApplicationContext(), "😨缓存错误", Toast.LENGTH_SHORT).show();
                Log.d("caches2", "onError: "+info.getFileName());
            }

            @Override
            public void onSuccess(DownloadInfo info) {
				//Toast.makeText(getApplicationContext(), "😏缓存完成", Toast.LENGTH_SHORT).show();
                Log.d("caches2", "onSuccess: "+info.getFileName());
            }
        });
		}
    }


    private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            DownloadInfo info = (DownloadInfo) msg.obj;
            Log.d("caches2", "handleMessage: "+info.getFileName()+"  progress==" +(int) (info.getCurrentSize()*100/info.getTotalSize())+"%");

            if (adapter.getDownloadInfos().contains(info)) {
                int position = adapter.getDownloadInfos().indexOf(info);
                adapter.getDownloadInfos().get(position).setCurrentSize(info.getCurrentSize());
                adapter.notifyItemChanged(position);
            }else {
                adapter.addItem(info);
            }

        }
    };
	public void icoas(View v) {
		switch (v.getId()) {
			case R.id.delall2:
				DatabaseHelper dbHelper = new DatabaseHelper(caches2.this,dbd,2);
				SQLiteDatabase db = dbHelper.getReadableDatabase();
				db.execSQL("DELETE FROM "+dbdt);
				//db.execSQL("DROP TABLE IF EXISTS " +dbdt);
				//db.execSQL("create table cache(id INTEGER PRIMARY KEY AUTOINCREMENT,yurl TEXT,ytitle TEXT,kurl TEXT)");
				db.close();
				/*String id = null;  
				String ytitle = null;  
				String kurl=null;
				System.out.println("C1");
				DatabaseHelper dbHelper = new DatabaseHelper(caches2.this,dbn,2);  

				SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

				Cursor cursor = sqliteDatabase.query("uridata", new String[] { "id",  
														 "yurl","kurl","ytitle"}, "id=?", new String[] { "1" }, null, null, null);  
				while (cursor.moveToNext()) {  
					id = cursor.getString(cursor.getColumnIndex("id"));  
					ytitle = cursor.getString(cursor.getColumnIndex("ytitle"));
					kurl = cursor.getString(cursor.getColumnIndex("kurl"));
				}
				sqliteDatabase.close();*/
				System.out.println("C2");
				Toast.makeText(getApplicationContext(), "清空完成", Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(caches2.this,caches2.class);
				startActivity(intent);
				break;
				}
				}
	
    public void start(View v){
		String id = null;  
		String ytitle = null;  
		String kurl=null;
		System.out.println("C1");
		DatabaseHelper dbHelper = new DatabaseHelper(caches2.this,dbn,2);  

		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

		Cursor cursor = sqliteDatabase.query("uridata", new String[] { "id",  
												 "yurl","kurl","ytitle"}, "id=?", new String[] { "1" }, null, null, null);  
		while (cursor.moveToNext()) {  
			id = cursor.getString(cursor.getColumnIndex("id"));  
			ytitle = cursor.getString(cursor.getColumnIndex("ytitle"));
			kurl = cursor.getString(cursor.getColumnIndex("kurl"));
		}
		sqliteDatabase.close();
		System.out.println("C2");
        DownloadManager.getInstance(this).start(kurl);
        //DownloadManager.getInstance(this).start("http://www.xzcmvideo.cn//masvod/public/2016/09/21/20160921_1574c418010_r1.mp4");
        //DownloadManager.getInstance(this).start("http://www.xzcmvideo.cn//masvod/public/2016/09/08/20160908_15707ff550a_r1_1200k.mp4");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance(this).unbindService();
    }
}
