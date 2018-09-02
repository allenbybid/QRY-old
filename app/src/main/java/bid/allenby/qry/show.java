package bid.allenby.qry;

import android.os.*;
import java.io.*;
import android.widget.*;
import android.app.*;
import android.view.*;
import android.content.*;
import java.util.*;
import android.webkit.*;
import android.database.sqlite.*;
import android.database.*;
import org.apache.commons.codec.*;
import android.content.pm.*;
import android.view.View.*;
import android.net.*;
import android.widget.SearchView.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.afollestad.materialdialogs.MaterialDialog;
import android.support.annotation.*;

public class show extends AppCompatActivity implements EasyVideoCallback{
	private EasyVideoPlayer player;
	private String dbn="sys_data";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.player);
		String id = null;  
		String ytitle = null;  
        String kurl=null;
		DatabaseHelper dbHelper = new DatabaseHelper(show.this,dbn,2);  

		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

		Cursor cursor = sqliteDatabase.query("uridata", new String[] { "id",  
												 "yurl","kurl","ytitle"}, "id=?", new String[] { "1" }, null, null, null);  
		while (cursor.moveToNext()) {  
		id = cursor.getString(cursor.getColumnIndex("id"));  
		ytitle = cursor.getString(cursor.getColumnIndex("ytitle"));
	    kurl = cursor.getString(cursor.getColumnIndex("kurl"));
		}
		sqliteDatabase.close();
		setTitle(ytitle);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	player = (EasyVideoPlayer) findViewById(R.id.player);
        if(kurl.equals("QRY://ERROR")){
			kurl="https://t.allenby.tk/uploads/video/server_error.mp4";
		}
		player.setSource(Uri.parse(kurl));
        assert player != null;
        player.setCallback(this);
        // All further configuration is done from the XML layout.
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        Log.d("EVP", "onPreparing()");
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        Log.d("EVP", "onPrepared()");
    }

    @Override
    public void onBuffering(int percent) {
        Log.d("EVP", "onBuffering(): " + percent + "%");
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Log.d("EVP", "onError(): " + e.getMessage());
        new MaterialDialog.Builder(this)
                .title(R.string.error)
                .content(e.getMessage())
                .positiveText(android.R.string.ok)
                .show();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        Log.d("EVP", "onCompletion()");
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
		String id = null;  
		String ytitle = null;  
        String kurl=null;
		DatabaseHelper dbHelper = new DatabaseHelper(show.this,dbn,2);  

		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

		Cursor cursor = sqliteDatabase.query("uridata", new String[] { "id",  
												 "yurl","kurl","ytitle"}, "id=?", new String[] { "1" }, null, null, null);  
		while (cursor.moveToNext()) {  
			id = cursor.getString(cursor.getColumnIndex("id"));  
			ytitle = cursor.getString(cursor.getColumnIndex("ytitle"));
			kurl = cursor.getString(cursor.getColumnIndex("kurl"));
		}
		sqliteDatabase.close();
		if(kurl.equals("QRY://ERROR")){
			kurl="https://t.allenby.tk/uploads/video/server_error.mp4";
			Toast.makeText(this, "😲不支持被缓存的类型。", Toast.LENGTH_SHORT).show();
		}else{
		
		Intent intent = new Intent();
		if(seadata(7).equals("0")){
		intent.setClass(show.this,caches2.class);
		}else{
		intent.setClass(show.this,caches.class);
		}
		startActivity(intent);
		//finish(); 
		}
    }

    @Override
    public void onClickVideoFrame(EasyVideoPlayer player) {
        Toast.makeText(this, "💎💎SYH💎💎", Toast.LENGTH_SHORT).show();

    }
	public String seadata(int id){
		String id2 = null;  
		String name = null;  

		DatabaseHelper dbHelper = new DatabaseHelper(show.this,dbn,2);  

		SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

		Cursor cursor = sqliteDatabase.query("sys", new String[] { "id",  
												 "name" }, "id="+id,null, null, null, null);  

		while (cursor.moveToNext()) {  
			id2 = cursor.getString(cursor.getColumnIndex("id"));  
			name = cursor.getString(cursor.getColumnIndex("name"));  
		}
		sqliteDatabase.close();
		if(name==null){
			name="0";
		}
		return name;
	}
}
