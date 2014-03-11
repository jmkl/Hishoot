package dcsms.hishoot2.skinmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class GetResources {
	private Bitmap bmp;
	private NinePatchDrawable nbmp;
	private PackageManager pm;

	public GetResources(Context context) {
		pm = context.getPackageManager();

	}
	public NinePatchDrawable getNineImage(String pkgName, String namaPng) {
		String themePack = pkgName;
		Resources themeResources = null;
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}
		nbmp = loadNineDraw(themeResources, themePack, namaPng);


		return nbmp;
	}
	public Bitmap getImage(String pkgName, String namaPng,Options opts) {
		String themePack = pkgName;
		Resources themeResources = null;
		try {
			themeResources = pm.getResourcesForApplication(themePack);
		} catch (NameNotFoundException e) {
		} catch (NullPointerException e) {
		}
		Drawable d = loadThemeResource(themeResources, themePack, namaPng);
		bmp = ((BitmapDrawable) d).getBitmap();


		return bmp;
	}

	public static Drawable loadThemeResource(Resources res, String pkg,
			String item_name) {
		Drawable d = null;
		if (res != null) {
			int resource_id = res.getIdentifier(item_name, "drawable", pkg);
			if (resource_id != 0) {
				try {
					d = res.getDrawable(resource_id);
				} catch (Resources.NotFoundException e) {
				}
			}
		}
		return d;
	}
	public static NinePatchDrawable loadNineDraw(Resources res, String pkg,
			String item_name) {
		NinePatchDrawable d = null;
		if (res != null) {
			int resource_id = res.getIdentifier(item_name, "drawable", pkg);
			if (resource_id != 0) {
				try {
					d = (NinePatchDrawable)res.getDrawable(resource_id);
				} catch (Resources.NotFoundException e) {
				}
			}
		}
		return d;
	}
	
}
