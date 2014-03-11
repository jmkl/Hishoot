package dcsms.hishoot2.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class HiPref {
	public SharedPreferences pref;
	private SharedPreferences.Editor editor;
	public String MYPREF = new Shit().PREF;
	private Shit s;

	public HiPref(Context context) {
		pref = context.getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
		s=new Shit();

	}

	public SharedPreferences getPref() {
		return pref;
	}

	public void PrefInt(String key, int value) {
		editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void PrefString(String key, String value) {
		editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void PrefBool(String key, Boolean value) {
		editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public int getHi(Activity a){
		int hi;
		if (!pref.contains(s.DEVICE_HI)) {
			hi = new CekDensiti(a).getHi();
			PrefInt(s.DEVICE_HI, hi);
		}else{
			hi = pref.getInt(s.DEVICE_HI, 320);
		}
		return hi; 
	}
	public int getWi(Activity a){
		int wi;
		if (!pref.contains(s.DEVICE_WIDTH)) {
			wi = new CekDensiti(a).getWid();
			PrefInt(s.DEVICE_WIDTH, wi);
		}else{
			wi = pref.getInt(s.DEVICE_WIDTH, 240);
		}
		return wi; 
	}
	public int getCurrentSkin(Activity a){
		int value = 0;
		if (pref.contains(s.CUR_SKIN)) {
			value = pref.getInt(s.CUR_SKIN, 0);
		}
		return value; 
	}
	public void resetSkin() {
		editor = pref.edit();
		editor.remove(s.CUR_SKIN);
		editor.remove(s.PAKET);
		editor.commit();
	}
	public void setCurrentSkin(int value) {
		editor = pref.edit();
		editor.putInt(s.CUR_SKIN, value);
		editor.commit();
	}
	public void cPaketName(String value) {
		editor = pref.edit();
		editor.putString(s.PAKET, value);
		editor.commit();
	}
	public String getPaketName() {
		String paket=null;
		if(pref.contains(s.PAKET)){
			paket = pref.getString(s.PAKET, null);
		}
		
		return paket;

	}
}
