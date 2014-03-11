package dcsms.hishoot2.util;

import android.content.Context;
import android.widget.Toast;

public class ToastAlert {
	public ToastAlert(Context context, String message, Boolean toastlong) {
		if (toastlong)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

	}
}
