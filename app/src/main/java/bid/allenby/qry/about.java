package bid.allenby.qry;

import android.support.v4.widget.*;
import com.jaeger.library.*;
import android.support.v7.app.ActionBarActivity;
import android.app.*;
import android.os.*;
import android.R.*;
import java.io.*;
import android.widget.*;
import android.view.*;
import android.content.*;
public class about extends ActionBarActivity 
{
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏 
        setContentView(R.layout.about);
		StatusBarUtil.setTransparent(about.this);
}
}
