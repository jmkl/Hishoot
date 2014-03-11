package dcsms.hishoot2.fb;

import dcsms.hishoot2.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class KomenDialog extends Dialog {
	private Button btnok, btncancel;
	private EditText et;
	private Listener listener;

	public interface Listener {
		void onOK(String string);

		void onCancel();
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public KomenDialog(final Context context) {
		super(context);
		setCancelable(false);
		setTitle("Add Comment");
		setContentView(R.layout.addkomen);
		btnok = (Button) findViewById(R.id.button_ok);
		btncancel = (Button) findViewById(R.id.button_cancel);
		et = (EditText) findViewById(R.id.editText1);
		btncancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();

			}
		});
		btnok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String t = et.getText().toString();
				if (et.equals(null)) {
					Toast.makeText(context, "Type Something",
							Toast.LENGTH_SHORT).show();
				} else {
					listener.onOK(t);
					dismiss();
				}

			}
		});
	}

}
