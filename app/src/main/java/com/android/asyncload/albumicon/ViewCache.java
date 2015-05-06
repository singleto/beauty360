package com.android.asyncload.albumicon;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.beauty360.R;
public class ViewCache {
	    private View baseView = null;
	    private TextView tvTitle = null;
	    private TextView tvInfo1 = null;
	    private TextView tvInfo2 = null;
	    private Button downloadButton = null;
	    private TextView downloadProgress = null;
	    private ImageView imageView;

	    public ViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public TextView getTvDownloadProgressView() {
	        if (downloadProgress == null) {
	        	downloadProgress = (TextView) baseView.findViewById(R.id.album_download_progress);
	        }
	        return downloadProgress;
	    }
	    
	    public Button getDownloadButton() {
	        if (downloadButton == null) {
	        	downloadButton = (Button) baseView.findViewById(R.id.album_download_bt);
	        }
	        return downloadButton;
	    }
	    
	    public TextView getTvTitleView() {
	        if (tvTitle == null) {
	        	tvTitle = (TextView) baseView.findViewById(R.id.title);
	        }
	        return tvTitle;
	    }
	    
	    public TextView getTvInfo1View() {
	        if (tvInfo1 == null) {
	        	tvInfo1 = (TextView) baseView.findViewById(R.id.info1);
	        }
	        return tvInfo1;
	    }
	    
	    public TextView getTvInfo2View() {
	        if (tvInfo2 == null) {
	        	tvInfo2 = (TextView) baseView.findViewById(R.id.info2);
	        }
	        return tvInfo2;
	    }
	    
	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.album_icon);
	        }
	        return imageView;
	    }

}
