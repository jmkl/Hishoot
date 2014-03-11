package dcsms.hishoot2.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import dcsms.hishoot2.R;

public class DcsmsButton extends Button {

	public DcsmsButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public DcsmsButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (isInEditMode() == false) {
			if (attrs.getStyleAttribute() == 0) {

				this.setBackgroundResource(R.drawable.next_btn);
				this.setTextColor(getResources().getColor(
						R.color.dcsms_hishoot__text_color));
				this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
						.getDimension(R.dimen.dcsms_hishoot__text_size));
				this.setPadding(
						getResources().getDimensionPixelSize(
								R.dimen.dcsms_hishoot__padding_left),
						getResources().getDimensionPixelSize(
								R.dimen.dcsms_hishoot__padding_top),
						getResources().getDimensionPixelSize(
								R.dimen.dcsms_hishoot__padding_right),
						getResources().getDimensionPixelSize(
								R.dimen.dcsms_hishoot__padding_bottom));

				this.setGravity(Gravity.CENTER);
			}
			parseAttributes(attrs);
		}
	}

	public DcsmsButton(Context context) {
		super(context);

	}

	private void parseAttributes(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.dcsms_button__view);
		a.recycle();

	}
}
