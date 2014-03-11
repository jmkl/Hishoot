package dcsms.hishoot2.skinmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import dcsms.hishoot2.R;
import dcsms.hishoot2.util.CekDensiti;
import dcsms.hishoot2.util.HiPref;
import dcsms.hishoot2.util.SL;

public class SkinChooser extends SherlockActivity {
	private String TAG = getClass().getSimpleName();
	private ViewPager pager;
	private List<String> paket = new ArrayList<String>();
	private HiPref pref;
	private Button apply,reset;
	CekDensiti c;
	int mydensiti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skinchooser);
		new SL(this, "Choose ur Template","choose then apply");
		c=new CekDensiti(this);
		mydensiti=c.getDensitiType();
		 pref = new HiPref(SkinChooser.this);
		pager = (ViewPager) findViewById(R.id.viewpejer);	
		apply=(Button)findViewById(R.id.apply_button);
		reset=(Button)findViewById(R.id.reset);
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {					
				pref.setCurrentSkin(pos);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			
			@Override
			public void onPageScrollStateChanged(int pos) {				
			}
		});

		PackageManager pm = getPackageManager();
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory("dcsms.hishoot.SKINTEMPLATE");
		List<ResolveInfo> apps = pm.queryIntentActivities(i, 0);
		for (ResolveInfo skin : apps) {

			ActivityInfo ai = skin.activityInfo;
			String pkg = ai.packageName;
			if(getSkinInfo(pkg)==mydensiti)
				paket.add(pkg);

		}
		SkinAdapter adapter = new SkinAdapter(SkinChooser.this, paket);
		pager.setAdapter(adapter);
		pager.setCurrentItem(pref.getCurrentSkin(this));
		
		apply.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View madafaka) {
				int now = pager.getCurrentItem();
				String skin = paket.get(now);
				pref.cPaketName(skin);
				
				finish();
				
				
			}
		});
		reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pref.resetSkin();
				finish();
				
			}
		});

	}
	private int getSkinInfo(String pkg) {

		int type = -1;
		try {
			Context c = this.createPackageContext(pkg, 0);
			AssetManager am = c.getAssets();
			InputStream is = am.open("keterangan.xml");
			SkinDescription k = new SkinDescription();
			k.getKeterangan(is);
			type=k.getDensType();
			is.close();
			

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return type;
	}

}
