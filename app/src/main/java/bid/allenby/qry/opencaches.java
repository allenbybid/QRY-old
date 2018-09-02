package bid.allenby.qry;
import java.io.*;
import android.content.*;
import android.net.*;
import android.app.Activity;
import android.support.v4.widget.*;
import com.jaeger.library.*;
import android.support.v7.app.ActionBarActivity;
import android.os.*;
public class opencaches extends ActionBarActivity 
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏 
        setContentView(R.layout.opencaches);
		StatusBarUtil.setTransparent(opencaches.this);
		File filesDir = getExternalFilesDir("");
		openAssignFolder(filesDir.toString());
		finish();
	}
	private void openAssignFolder(String path){
        File file = new File(path);
        if(null==file || !file.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        try {
            startActivity(intent);
//            startActivity(Intent.createChooser(intent,"选择浏览工具"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
