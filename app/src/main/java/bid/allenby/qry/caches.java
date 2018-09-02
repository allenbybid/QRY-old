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
import android.support.annotation.*;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;
import android.icu.text.*;
import com.jaeger.library.*;
import android.support.v7.app.AppCompatActivity;
public class caches extends AppCompatActivity{
	private String dbn="sys_data";
	private ThinDownloadManager downloadManager;
	private static final int DOWNLOAD_THREAD_POOL_SIZE =1;
	MyDownloadDownloadStatusListenerV1
	myDownloadStatusListener = new MyDownloadDownloadStatusListenerV1();
    int downloadId1;
	Button mStart;
    Button mCancel;
    Button mListFiles;
    TextView mProgress1Txt;
    ProgressBar mProgress1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
		StatusBarUtil.setTransparent(caches.this);
	setContentView(R.layout.caches);
		
		mStart = (Button) findViewById(R.id.start);
        mCancel = (Button) findViewById(R.id.cannel);
        mListFiles = (Button) findViewById(R.id.openf);
        mProgress1 = (ProgressBar) findViewById(R.id.progress);
        mProgress1Txt = (TextView) findViewById(R.id.progresstip);
		mProgress1.setMax(100);
        mProgress1.setProgress(0);
	String id = null;  
	String ytitle = null;  
	String kurl=null;
	DatabaseHelper dbHelper = new DatabaseHelper(caches.this,dbn,2);  

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
	Toast.makeText(getApplicationContext(), "😉缓存开始", Toast.LENGTH_SHORT).show();
		       
	downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
	RetryPolicy retryPolicy = new DefaultRetryPolicy();
	File filesDir = getExternalFilesDir("");
	Uri downloadUri = Uri.parse(kurl);
	Uri destinationUri = Uri.parse(filesDir+"/"+ytitle+".mp4");
	String fpath=filesDir+"/"+ytitle+".mp4";
	TextView fpatht=(TextView) findViewById(R.id.downpath);
	fpatht.setText("💎下载路径\n"+filesDir);
	final DownloadRequest downloadRequest1 = new DownloadRequest(downloadUri)
	.setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
	.setRetryPolicy(retryPolicy)
	.setDownloadContext(ytitle.toString())
	.setStatusListener(myDownloadStatusListener);
	if (downloadManager.query(downloadId1) == DownloadManager.STATUS_NOT_FOUND) {
		downloadId1 = downloadManager.add(downloadRequest1);
	}
		mStart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					downloadManager.cancel(downloadId1);
					downloadId1 = downloadManager.add(downloadRequest1);
				}
			});

        mCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					downloadManager.cancel(downloadId1);
				}
			});

        mListFiles.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					showInternalFilesDir();
				}
			});
		}
    }
	protected void onDestroy() {
        super.onDestroy();
        downloadManager.release();
    }
	private void showInternalFilesDir() {
        File internalFile = new File(getExternalFilesDir("").getPath());
        File files[] = internalFile.listFiles();
        String contentText = "";
        if( files.length == 0 ) {
            contentText = "😞没有找到相关缓存文件!";
        }

        for (File file : files) {
			String fdize=fcsize(file.length())+"M";
            contentText += file.getName()+" "+fdize+" \n\n ";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog internalCacheDialog = builder.create();
        LayoutInflater inflater = internalCacheDialog.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.file_list, null);
        TextView content = (TextView) dialogLayout.findViewById(R.id.filesList);
        content.setText(contentText);
        builder.setView(dialogLayout).setTitle("文件列表");
        builder.show();

    }
	public double fcsize(double a){
		double b=a/1024/1024;
		double c=((int)(b*100))/100.0;
		return c;
	}
	public class MyDownloadDownloadStatusListenerV1 implements DownloadStatusListenerV1 {

		@Override
		public void onDownloadComplete(DownloadRequest request) {
			final int id = request.getDownloadId();
			if (id == downloadId1) {
				mProgress1Txt.setText(request.getDownloadContext() + "\r\r完成");
				Toast.makeText(getApplicationContext(), "😏缓存完成", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onDownloadFailed(DownloadRequest request, int errorCode, String errorMessage) {
			final int id = request.getDownloadId();
			if (id == downloadId1) {
				mProgress1Txt.setText("😂缓存失败\r\r错误代码:"+errorCode);
				Toast.makeText(getApplicationContext(), "😥缓存错误!", Toast.LENGTH_SHORT).show();
				mProgress1.setProgress(0);
			}
		}
		@Override
		public void onProgress(DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
			int id = request.getDownloadId();

			if (id == downloadId1) {
				mProgress1Txt.setText(""+progress+"%"+"  "+getBytesDownloaded(progress,totalBytes));
                mProgress1.setProgress(progress);
				/*File filesDir = getExternalFilesDir("");
				String ytitle = null;  
				String kurl=null;
				DatabaseHelper dbHelper = new DatabaseHelper(caches.this,dbn,2);  

				SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();  

				Cursor cursor = sqliteDatabase.query("uridata", new String[] { "id",  
														 "yurl","kurl","ytitle"}, "id=?", new String[] { "1" }, null, null, null);  
				while (cursor.moveToNext()){
					ytitle = cursor.getString(cursor.getColumnIndex("ytitle"));
					kurl = cursor.getString(cursor.getColumnIndex("kurl"));
				}
				sqliteDatabase.close();*/
				//System.out.println("Download "+id+", "+progress+"%"+" ，"+getBytesDownloaded(progress,totalBytes));
				/*File f= new File(filesDir+"/"+ytitle+".mp4");
				if (f.exists() && f.isFile()){
					double sosize=fcsize(f.length());
					Toast.makeText(getApplicationContext(), "已缓存"+sosize+"M", Toast.LENGTH_SHORT).show();
				}*/
			}
		}
		private String getBytesDownloaded(int progress, long totalBytes) {
			//Greater than 1 MB
			long bytesCompleted = (progress * totalBytes)/100;
			if (totalBytes >= 1000000) {
				return (""+(String.format("%.1f", (float)bytesCompleted/1000000))+ "/"+ ( String.format("%.1f", (float)totalBytes/1000000)) + "M");
			} if (totalBytes >= 1000) {
				return (""+(String.format("%.1f", (float)bytesCompleted/1000))+ "/"+ ( String.format("%.1f", (float)totalBytes/1000)) + "K");

			} else {
				return ( ""+bytesCompleted+"/"+totalBytes );
			}
		}
    }
}
