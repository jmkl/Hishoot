package dcsms.hishoot2.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import dcsms.hishoot2.R;
import dcsms.hishoot2.fb.Grup;
import dcsms.hishoot2.fb.GrupAraiadapter;
import dcsms.hishoot2.fb.KomenDialog;
import dcsms.hishoot2.fb.KomenDialog.Listener;

public class ShareDialog extends SherlockActivity implements OnClickListener {
	private final ArrayList<Grup> Group = new ArrayList<Grup>();
	private static final List<String> PERMISSIONS = Arrays.asList(
			"publish_actions", "publish_stream", "read_stream", "user_groups");
	private boolean pendingPublishReauthorization = false;
	private GrupAraiadapter adapter;

	String TAG = getClass().getSimpleName();
	private Button fb, show, share;
	private ImageView iv;
	private ListView lv;
	private String dirfile;
	private Button lb;
	Bitmap ok;
	Grup a;

	ProgressDialog pd;
	private UiLifecycleHelper uiHelper;

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.sharedialog);
		new SL(this, "Share It!!!","Connect with facebook");
		pd = new ProgressDialog(ShareDialog.this);
		pd.setMessage("Processing... Please Wait");
		pd.setCancelable(false);
		pd.setIndeterminate(true);

		Bundle exBundle = getIntent().getExtras();
		dirfile = exBundle.getString("File");
		fb = (Button) findViewById(R.id.share_fb);
		lb = (Button) findViewById(R.id.login);
		share = (Button) findViewById(R.id.share_apps);
		show = (Button) findViewById(R.id.share_show);
		iv = (ImageView) findViewById(R.id.hishoot_preview);
		ok = BitmapFactory.decodeFile(dirfile);
		lv = (ListView) findViewById(R.id.gruplist);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				a = new Grup();
				a = Group.get(pos);
				final KomenDialog k = new KomenDialog(ShareDialog.this);
				k.setListener(new Listener() {
					
					@Override
					public void onOK(String string) {
						Bundle params = new Bundle();
						params.putString("message", string);
						params.putParcelable("picture", ok);
						publishStory(params, "GRUPWALL", a.id);
						lv.setVisibility(View.GONE);
						
					}
					
					@Override
					public void onCancel() {
						
						
					}
				});
				k.show();
				

			}
		});

		updateView();

	}

	private void updateView() {
		final Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}
			onSessionStateChange(session, session.getState(), null);
		} else {
			lb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					session.openForRead(new Session.OpenRequest(
							ShareDialog.this).setCallback(callback));

				}
			});
		}
		iv.setImageBitmap(ok);
		fb.setOnClickListener(this);
		show.setOnClickListener(this);
		share.setOnClickListener(this);
		// uiHelper.onResume();

	}

	@Override
	public void onClick(View arg0) {
		Session session = Session.getActiveSession();
		if(arg0.getId()==R.id.share_apps){
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("image/*");
			i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(dirfile)));
			startActivity(Intent.createChooser(i, "Share with : "));
		}
		if (session != null && (session.isOpened() || session.isClosed())) {
			switch (arg0.getId()) {
			case R.id.share_fb:
				
				final KomenDialog k = new KomenDialog(ShareDialog.this);
				k.setListener(new Listener() {
					
					@Override
					public void onOK(String string) {
						
						Bundle p = new Bundle();
						p.putString("message", string);
						p.putParcelable("picture", ok);
						publishStory(p, "WALL", null);
						
					}
					
					@Override
					public void onCancel() {
						
					}
				});
				k.show();
				

				break;
			case R.id.share_show:
				Bundle p2 = new Bundle();
				publishStory(p2, "GRUP", null);
				break;

			}
		} else {
			session.openForRead(new Session.OpenRequest(ShareDialog.this)
					.setCallback(callback));
		}

	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingPublishReauthorization
				&& state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
			pendingPublishReauthorization = false;
		}
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			lb.setVisibility(View.GONE);
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
			lb.setVisibility(View.VISIBLE);
		}
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private void publishStory(Bundle post, String type, String grupid) {
		Session session = Session.getActiveSession();
		if (session != null) {


			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			PassData p = new PassData();
			p.session = session;
			p.string = type;
			p.post = post;
			p.gId = grupid;

			new doLoad().execute(p);

		}

	}

	private class PassData {
		Session session;
		String string;
		Bundle post;
		String gId;
	}

	private class doLoad extends AsyncTask<PassData, Void, Request> {

		@Override
		protected void onPostExecute(Request result) {
			RequestAsyncTask task = new RequestAsyncTask(result);
			task.execute();
			// pd.dismiss();
		}

		@Override
		protected void onPreExecute() {
			pd.show();
		}

		@Override
		protected Request doInBackground(PassData... arg0) {
			PassData tipe = arg0[0];
			Session session = tipe.session;
			String type = tipe.string;
			Bundle post = tipe.post;
			String grupid = tipe.gId;

			Request request = null;
			if (type.equals("WALL"))
				request = new Request(session, "me/photos", post,
						HttpMethod.POST, new OrdinaryCallBek());
			else if (type.equals("GRUP"))
				request = new Request(session, "me/groups", post,
						HttpMethod.GET, new GrupRikues());
			else if (type.equals("GRUPWALL"))
				request = new Request(session, grupid + "/photos", post,
						HttpMethod.POST, new OrdinaryCallBek());

			return request;
		}

	}

	class OrdinaryCallBek extends Request implements Request.Callback {

		@Override
		public void onCompleted(Response response) {
			pd.dismiss();
			JSONObject graphResponse = response.getGraphObject()
					.getInnerJSONObject();
			String postId = null;
			try {
				postId = graphResponse.getString("id");
			} catch (JSONException e) {
				Log.i(TAG, "JSON error " + e.getMessage());
			}
			FacebookRequestError error = response.getError();
			if (error != null) {
				Toast.makeText(ShareDialog.this, error.getErrorMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ShareDialog.this, "Success", Toast.LENGTH_LONG)
						.show();
				
			}
		}

	}

	class GrupRikues extends Request implements Request.Callback {

		@Override
		public void onCompleted(Response response) {
			pd.dismiss();
			JSONObject json = response.getGraphObject().getInnerJSONObject();

			try {

				JSONArray d = json.getJSONArray("data");

				int l = (d != null ? d.length() : 0);

				for (int i = 0; i < l; i++) {
					JSONObject o = d.getJSONObject(i);

					String namagrup = o.getString("name");
					String idgroup = o.getString("id");

					Grup f = new Grup();
					f.id = idgroup;
					f.name = namagrup;
					Group.add(f);
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter = new GrupAraiadapter(ShareDialog.this,
								R.layout.rowlayout, Group);
						lv.setVisibility(View.VISIBLE);
						lv.setAdapter(adapter);

					}
				});
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

	}
}
