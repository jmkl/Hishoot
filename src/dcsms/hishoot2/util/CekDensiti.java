package dcsms.hishoot2.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class CekDensiti {
	private int wid;
	private int haig;
	private float scale;
	private int dd;

	public CekDensiti(Activity c) {
		DisplayMetrics dm = new DisplayMetrics();
		c.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int dpi = dm.densityDpi;
		// these will return the actual dpi horizontally and vertically
		if (dpi == DisplayMetrics.DENSITY_LOW)
			dd = 0;
		else if (dpi == DisplayMetrics.DENSITY_MEDIUM)
			dd = 1;
		else if (dpi == DisplayMetrics.DENSITY_HIGH)
			dd = 2;
		else if (dpi == DisplayMetrics.DENSITY_XHIGH)
			dd = 3;
		else if (dpi == DisplayMetrics.DENSITY_XXHIGH)
			dd = 4;
		else
			dd = 1;
		wid = dm.widthPixels;
		haig = dm.heightPixels;
		scale = c.getApplicationContext().getResources().getDisplayMetrics().density;

	}

	public int getWid() {
		return wid;
	}
	public int getDensitiType(){
		return dd;
	}

	public float getScale() {
		return scale;
	}

	public int getHi() {
		return haig;
	}

}
