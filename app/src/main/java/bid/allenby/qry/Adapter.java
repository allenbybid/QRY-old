package bid.allenby.qry;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.R.*;
import com.tone.library.DownloadInfo;
import com.tone.library.DownloadTask;
import java.util.ArrayList;
import android.widget.*;
import com.tone.library.*;

/**
 * Created by zhaotong on 2016/9/29.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();

    private Context context;

    public void setDownloadInfos(ArrayList<DownloadInfo> downloadInfos) {
        this.downloadInfos = downloadInfos;
        notifyDataSetChanged();
    }

    public ArrayList<DownloadInfo> getDownloadInfos() {
        return downloadInfos;
    }

    public void addItem(DownloadInfo info){
        downloadInfos.add(info);
        notifyItemInserted(downloadInfos.size()-1);
    }

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_down, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final DownloadInfo info = downloadInfos.get(position);
        final Holder holder = (Holder) h;
        holder.file_name.setText(info.getFileName());
        int progress = (int) (info.getCurrentSize()*100/info.getTotalSize());
        Log.d("Adapter", "onBindViewHolder:  progress =="+progress);
        holder.progressbar.setProgress(progress);
        holder.progress.setText(progress+"%");
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DownloadTask.getInstance(context).isDownloading(info.getFileUrl())){
                    DownloadTask.getInstance(context).stopDownload(info);
                    holder.btn.setText("开始");
                }else {
                    DownloadTask.getInstance(context).startDownload(info);
                    holder.btn.setText("暂停");
                }
            }
        });
		holder.delzxs.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (DownloadTask.getInstance(context).isDownloading(info.getFileUrl())){
						DownloadTask.getInstance(context).stopDownload(info);
						DownloadTask.getInstance(context).deleteDownload(info);
						
						//holder.delzxs.setText("删除中");
					}else {
						DownloadTask.getInstance(context).startDownload(info);
						DownloadTask.getInstance(context).deleteDownload(info);
						//holder.delzxs.setText("删除中");
					}
				}
			});
        if (info.getCurrentSize()==info.getTotalSize()){
            holder.btn.setText("下载完成");
        }

    }

    @Override
    public int getItemCount() {
        return downloadInfos.size();
    }



    static class Holder extends RecyclerView.ViewHolder{

        private TextView file_name,progress;
		private Button btn,delzxs;
        private ProgressBar progressbar;
        public Holder(View itemView) {
            super(itemView);
            file_name = (TextView) itemView.findViewById(R.id.file_name);
            progress = (TextView) itemView.findViewById(R.id.progress);
            btn = (Button) itemView.findViewById(R.id.btn);
			delzxs=(Button) itemView.findViewById(R.id.delzxs);
            progressbar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }

}
