package dcsms.hishoot2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DrawView {
	public Bitmap DrawMe(Bitmap frame,int framex,int framey, Bitmap ss,int ssx,int ssy, Context context, int w, int h) {
		Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(b);
		canvas.drawBitmap(ss, ssx,ssy, null);
		canvas.drawBitmap(frame, framex,framey, null);		
		return b;

	}

}
