package dcsms.hishoot2;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.crop.CropImage;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import dcsms.hishoot2.skinmanager.GetResources;
import dcsms.hishoot2.skinmanager.SkinChooser;
import dcsms.hishoot2.skinmanager.SkinDescription;
import dcsms.hishoot2.skinmanager.Utils;
import dcsms.hishoot2.util.BmpProc;
import dcsms.hishoot2.util.CekDensiti;
import dcsms.hishoot2.util.DrawView;
import dcsms.hishoot2.util.HiPref;
import dcsms.hishoot2.util.Resize;
import dcsms.hishoot2.util.SL;
import dcsms.hishoot2.util.Save;
import dcsms.hishoot2.util.ShareDialog;

public class Hello extends SherlockActivity implements OnClickListener {
	private HiPref pref;
	private ImageView load;
	private int tinggi, lebar;
	private ImageView iv_prev;
	private int TL, TT, BL, BT;
	private String WTF = "watepak";
	private Button ss1, ss2, wall, save;
	private int KODE_SS1 = 1, KODE_SS2 = 2, KODE_WALL = 3,KODE_CROP=4;
	private String IMG_1 = "watepak", IMG_2 = "watepak", IMG_WALL = "watepak";
	private ProgressDialog dialog;
	private SkinDescription desc;
	private Bitmap effect;
	private int effect_type = 666;
	private Resize resize;
	boolean onsave = false;
	private int wall_lebar = 300, wall_tinggi = 300;
	static {
		System.loadLibrary("photoprocessing");
	}

	public native String dafuq();

	@Override
	protected void onResume() {
		super.onResume();
		onsave = false;
		setFungsi();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mykey", 1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindDrawables(findViewById(R.id.rootView));
		System.gc();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 123456, 2, "About")
				.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(1, 21, 1, "template").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Log.d("IMAGE", Integer.toString(item.getItemId()));
		if (item.getItemId() == 123456) {
			AlertDialog.Builder d = new AlertDialog.Builder(Hello.this);
			d.setCancelable(true);
			d.setTitle("ABOUT HiShoot 2");
			d.setMessage(R.string.about);
			d.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub

				}
			});
			d.show();
		}
		if (item.getItemId() == 21) {
			Intent i = new Intent();
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setClass(Hello.this, SkinChooser.class);
			startActivity(i);
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		new SL(this, "HiShoot2", "Hs for home screen, Hs for HiShoot");

		// Toast.makeText(this, dafuq(), Toast.LENGTH_SHORT).show();
		// Bitmap b = BitmapFactory.decodeResource(getResources(),
		// R.drawable.watermark);
		// byte[] data = null;
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// b.compress(Bitmap.CompressFormat.PNG, 100, baos);
		// data = baos.toByteArray();
		// String a = Base64.encodeToString(data, Base64.DEFAULT);
		//

		findView();

		pref = new HiPref(Hello.this);
		tinggi = pref.getHi(this);
		lebar = pref.getWi(this);

		setFungsi();
	}

	private void findView() {
		iv_prev = (ImageView) findViewById(R.id.iv_preview);
		ss1 = (Button) findViewById(R.id.btn_ss1);
		ss2 = (Button) findViewById(R.id.btn_ss2);
		wall = (Button) findViewById(R.id.btn_wall);
		save = (Button) findViewById(R.id.btn_save);
		load = (ImageView) findViewById(R.id.loading);
		load.setVisibility(View.GONE);

	}

	private void setFungsi() {
		ss1.setOnClickListener(this);
		ss2.setOnClickListener(this);
		wall.setOnClickListener(this);
		save.setOnClickListener(this);
		loading(true);
		try {
			new Tasking().execute();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		String chooser = getString(R.string.chooser);
		switch (v.getId()) {
		case R.id.btn_ss1:
			getImageChooser(String.format(chooser, getString(R.string.ss1)),
					KODE_SS1);
			break;
		case R.id.btn_ss2:
			getImageChooser(String.format(chooser, getString(R.string.ss2)),
					KODE_SS2);
			break;
		case R.id.btn_wall:
			getImageChooser(
					String.format(chooser, getString(R.string.wallpaper)),
					KODE_WALL);
			break;
		case R.id.btn_save:

			SavingShit();

			break;

		}

	}

	private void SavingShit() {
		onsave = true;
		setFungsi();

	}

	private class SavingTask extends AsyncTask<Bitmap, Integer, File> {
		private SavingTask() {
			dialog = new ProgressDialog(Hello.this);
			dialog.setCancelable(false);
			dialog.setMessage("Please Wait");
			dialog.show();
		}

		@Override
		protected void onPostExecute(File result) {
			if (result.exists()) {
				dialog.setMessage("SaveComplete");
				dialog.dismiss();
				String s = result.getPath();
				Log.d(getClass().getName(), s);
				Intent i = new Intent();
				i.putExtra("File", s);
				i.setClass(Hello.this, ShareDialog.class);
				startActivity(i);

			} else {
				dialog.setMessage("Failed");
				dialog.dismiss();
			}
		}

		@Override
		protected File doInBackground(Bitmap... bmp) {
			Save s = new Save(Hello.this);
			File f = s.SaveBitmap(bmp[0]);

			return f;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			onsave = false;
			if (requestCode == KODE_SS1) {
				Uri uri = data.getData();
				File f = new File(uri.getPath());

				if (!f.isFile()) {
					IMG_1 = getImagePath(uri);
				} else {
					IMG_1 = uri.getPath();
				}
				setFungsi();

			}

			else if (requestCode == KODE_SS2) {
				Uri uri = data.getData();
				File f = new File(uri.getPath());

				if (!f.isFile()) {
					IMG_2 = getImagePath(uri);
				} else {
					IMG_2 = uri.getPath();
				}
				setFungsi();

			}

			else if (requestCode == KODE_WALL) {
				Uri uri = data.getData();
				File f = new File(uri.getPath());
				String gw = null;
				if (!f.isFile()) {
					gw = getImagePath(uri);
				} else {
					gw = uri.getPath();
				}
				Intent i = new Intent(getApplicationContext(), CropImage.class);
				i.setData(Uri.fromFile(new File(gw)));
				 i.putExtra(CropImage.OUTY, wall_tinggi);
				 i.putExtra(CropImage.OUTX, wall_lebar);
				 i.putExtra(CropImage.ASPEKY, wall_tinggi);
				 i.putExtra(CropImage.ASPEKX, wall_lebar);
				 i.putExtra(CropImage.SCALE, true);				 
				 i.putExtra(CropImage.ISRETURN, true);
				 startActivityForResult(i, KODE_CROP);
				setFungsi();

			}
			else if (requestCode == KODE_CROP) {
				String img= data.getStringExtra("data");
				if(img.equals("watepak")){
					Toast.makeText(getApplicationContext(), "Wallpaper size is too Big", Toast.LENGTH_SHORT).show();
					IMG_WALL="watepak";
				}else{
					IMG_WALL=img;
				}
				setFungsi();
			}

		}
	}

	private void getImageChooser(String judul, int rekueskode) {
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("image/*");
		startActivityForResult(Intent.createChooser(i, judul), rekueskode);
	}

	private String getImagePath(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	private class Tasking extends AsyncTask<Bitmap, Bitmap, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			wall_lebar = result.getWidth();
			wall_tinggi = result.getHeight();
			if (onsave) {
				new SavingTask().execute(result);
			} else {
				Bitmap b = Resize.resizeImage(result, lebar, lebar);
				iv_prev.setImageBitmap(b);
				result.recycle();
			}

			loading(false);
		}

		@Override
		protected Bitmap doInBackground(Bitmap... bmp) {
			Bitmap bss1, bss2, bwall;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inDither = false; // Disable Dithering mode
			opts.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
			opts.inTempStorage = new byte[32 * 1024];
			if (IMG_1.equals(WTF)) {
				bss1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.img_default, opts);

			} else {
				bss1 = BitmapFactory.decodeFile(IMG_1, opts);
			}
			if (IMG_2.equals(WTF)) {
				bss2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.img_default, opts);
			} else {
				bss2 = BitmapFactory.decodeFile(IMG_2, opts);

			}
			if (IMG_WALL.equals(WTF)) {
				bwall = BitmapFactory.decodeResource(getResources(),
						R.drawable.img_default, opts);
			} else {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				bwall = BitmapFactory.decodeFile(IMG_WALL, options);

			}

			Bitmap shot1 = Bitmap.createScaledBitmap(bss1, lebar, tinggi, true);
			Bitmap shot2 = Bitmap.createScaledBitmap(bss2, lebar, tinggi, true);

			// 27 28 27 54
			TL = 26;
			TT = 27;
			BL = 27;
			BT = 54;

			Bitmap mixthem = null;
			Bitmap frame = null;

			CekDensiti cek = new CekDensiti(Hello.this);

			int deftotx = (int) (cek.getScale() * (TL + BL));
			int deftoty = (int) (cek.getScale() * (TT + BT));
			int topx = 0;
			int topy = 0;
			int totx = 0;
			int toty = 0;
			topx = (int) (TL);
			topy = (int) (TT);
			totx = (int) ((TL + BL));
			toty = (int) ((TT + BT));
			String skin = pref.getPaketName();
			if (skin != null) {
				Bitmap framefrom = new GetResources(Hello.this).getImage(skin,
						"skin", opts);

				Context c;
				InputStream is = null;
				try {
					c = createPackageContext(skin, 0);
					AssetManager am = c.getAssets();
					is = am.open("keterangan.xml");
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				desc = new SkinDescription();
				if (is != null) {

					desc.getKeterangan(is);
				}
				int tx, ty, bx, by;
				tx = desc.getTX();
				ty = desc.getTY();
				bx = desc.getBX();
				by = desc.getBY();
				if (tx > 0) {
					TL = tx;
				} else {
					TL = 1;
				}
				if (ty > 0) {
					TT = ty;
				} else {
					TT = 1;
				}
				if (bx > 0) {
					BL = bx;
				} else {
					BL = 1;
				}
				if (by > 0) {
					BT = by;
				} else {
					BT = 1;
				}
				topx = (int) (TL);
				topy = (int) (TT);
				totx = (int) ((TL + BL));
				toty = (int) ((TT + BT));

				frame = Bitmap.createScaledBitmap(framefrom, lebar + totx,
						tinggi + toty, true);

			} else {
				LOG(its(totx));
				LOG(its(toty));

				frame = BmpProc.getNine(0, 0, R.drawable.frame1, lebar + totx,
						tinggi + toty, Hello.this, opts);

			}

			Bitmap mix1 = new DrawView().DrawMe(frame, 0, 0, shot1, topx, topy,
					Hello.this, lebar + totx, tinggi + toty);

			Bitmap mix2 = new DrawView().DrawMe(frame, 0, 0, shot2, topx, topy,
					Hello.this, lebar + totx, tinggi + toty);
			mixthem = Bitmap.createBitmap(mix1.getWidth() * 2,
					mix1.getHeight(), Bitmap.Config.ARGB_8888);
			int maxwid = mixthem.getWidth(), maxhi = mixthem.getHeight();
			if (bwall.getHeight() < mixthem.getHeight()) {
				maxhi = mixthem.getHeight();
				maxwid = mixthem.getWidth() * mixthem.getWidth();
			}
			if (bwall.getWidth() < mixthem.getWidth()) {
				maxhi = mixthem.getHeight() * mixthem.getHeight();
				maxwid = mixthem.getWidth();
			}

			Bitmap wallfcuk = Resize.resizeImage(bwall, maxwid, maxhi);
			// Bitmap wm = BitmapFactory.decodeResource(getResources(),
			// R.drawable.watermark);
			Bitmap wm = BitmapFactory.decodeStream(new ByteArrayInputStream(
					Base64.decode(dafuq(), Base64.DEFAULT)));
			Canvas cc = new Canvas(mixthem);
			cc.drawBitmap(wallfcuk, 0, 0, null);
			cc.drawBitmap(mix1, 0, 0, null);
			cc.drawBitmap(mix2, mix1.getWidth(), 0, null);
			// cc.drawBitmap(wm, mix1.getWidth() - (wm.getWidth() / 2),
			// (mix1.getHeight() - wm.getHeight()), null);
			System.gc();
			return mixthem;

		}

	}

	private String its(Integer i) {
		return Integer.toString(i);
	}

	private void loading(Boolean run) {
		final AnimationDrawable anima = (AnimationDrawable) load.getDrawable();
		if (run) {
			load.setVisibility(View.VISIBLE);

			load.post(new Runnable() {

				@Override
				public void run() {
					anima.start();

				}
			});
		} else {
			load.setVisibility(View.GONE);
			anima.stop();
		}

	}

	private void LOG(String msg) {
		Log.d("OOM", msg);
	}

}