package dcsms.hishoot2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.drawable.NinePatchDrawable;

public class BmpProc {

	public static Bitmap getNine(int fromx,int fromy,int id, int x, int y, Context context, Options opts) {
		Bitmap out = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(out);
		
		NinePatchDrawable bg =  (NinePatchDrawable) context.getResources().getDrawable(id);
		if (bg != null) {
		  bg.setBounds(0, 0, x, y);
		  bg.draw(c);
		  }

		
		return out;

	}
}
