package dcsms.hishoot2.util;

import android.graphics.drawable.NinePatchDrawable;

import com.actionbarsherlock.app.SherlockActivity;

import dcsms.hishoot2.R;

public class SL {
	public SL(SherlockActivity s,String title,String sub){
		s.getSupportActionBar().setLogo(R.drawable.ic_launcher);
		NinePatchDrawable bg =  (NinePatchDrawable) s.getResources().getDrawable(R.drawable.head);
		s.getSupportActionBar().setBackgroundDrawable(bg);
		s.getSupportActionBar().setTitle(title);
		s.getSupportActionBar().setSubtitle(sub);
	}

}
