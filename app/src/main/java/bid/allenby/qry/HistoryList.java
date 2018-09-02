package bid.allenby.qry;

import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.*;

public class HistoryList extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HashMap<String, Object>> list;
	private int i = 0;
	public HistoryList(LayoutInflater inflater, 
			List<HashMap<String, Object>> listList) {
		// TODO Auto-generated constructor stub
		this.inflater = inflater;
		this.list = listList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
  
   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewholder = null;
		if(convertView == null){
			viewholder = new ViewHodler();
			convertView = inflater.inflate(R.layout.hist_list, null);
			viewholder.image = (ImageView) convertView.findViewById(R.id.iv_image);
			viewholder.title = (TextView) convertView.findViewById(R.id.iv_list_title);
			viewholder.des = (TextView) convertView.findViewById(R.id.iv_list_des);
			//viewholder.share=(Button) convertView.findViewById(R.id.share);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHodler)convertView.getTag();
		}
		viewholder.image.setBackgroundResource(R.drawable.logo_2);
		viewholder.title.setText(list.get(position).get("titl").toString());
		viewholder.des.setText(list.get(position).get("desc").toString());
		
		i++;
		//Log.i("list", "list getview " + i);
		return convertView;
	}
	
	public class ViewHodler{
		ImageView image;
		TextView title;
	    //Button share;
		TextView des;
		String yurl;
		String kurl;
		String ytitle;
	}
	
}
