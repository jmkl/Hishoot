package dcsms.hishoot2.skinmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import dcsms.hishoot2.R;

public class SkinAdapter extends PagerAdapter {
	private List<String> namapaket = new ArrayList<String>();
	private Context context;
	private GetResources getRes;

	public SkinAdapter(Context context, List<String> pkgName) {
		this.context = context;
		namapaket = pkgName;
		getRes = new GetResources(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return namapaket.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == ((View) obj);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		Bitmap a = null;
		LayoutInflater inflater = (LayoutInflater) container.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither = false; // Disable Dithering mode
		opts.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		opts.inInputShareable = true; // Which kind of reference will be used to
										// recover the Bitmap data after being
										// clear, when it will be used in the
										// future
		opts.inTempStorage = new byte[32 * 1024];
		opts.inSampleSize=4;

		View view = inflater.inflate(R.layout.preview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.preview_image);
		TextView tvView = (TextView) view.findViewById(R.id.description);
		String des = null;
		try {
			des = getSkinInfo(namapaket.get(position));
			a = getRes.getImage(namapaket.get(position), "skin",opts);
		} catch (NullPointerException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (des != null) {

			tvView.setText(des);
		}
		if (a != null) {
			imageView.setImageBitmap(a);
		} else {
			Bitmap x = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.img_default, opts);
			imageView.setImageBitmap(x);
		}
		((ViewPager) container).addView(view, 0);
		return view;
	}

	private String getSkinInfo(String pkg) throws IOException,XmlPullParserException {

		String device = null;
		String author = null;
		try {
			Context c = context.createPackageContext(pkg, 0);
			AssetManager am = c.getAssets();
			InputStream is = am.open("keterangan.xml");
			SkinDescription k = new SkinDescription();
			k.getKeterangan(is);
			device = k.getDevice();
			author = k.getAuthor();
			is.close();

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Device : " + device + "\n" + "Author : " + author;
	}

}
