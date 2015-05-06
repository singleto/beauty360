package com.android.asyncload.albumicon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.beauty360.R;
public class ViewCacheLocal {
	    private View baseView = null;
	    private TextView tvTitle = null;
	    private ImageView imageView;

	    public ViewCacheLocal(View baseView) {
	        this.baseView = baseView;
	    }

	    public TextView getTvTitleView() {
	        if (tvTitle == null) {
	        	tvTitle = (TextView) baseView.findViewById(R.id.title_local);
	        }
	        return tvTitle;
	    }
	    
	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.album_icon_local);
	        }
	        return imageView;
	    }

}
