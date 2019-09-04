package com.yuejian.meet.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yuejian.meet.utils.ViewInject;

/**
 * An EditText, which notifies when something was cut/copied/pasted inside it.
 * 
 * @author Lukas Knuth
 * @version 1.0
 */
@SuppressLint("NewApi")
public class CustomEditText extends EditText implements
		MenuItem.OnMenuItemClickListener {

	private static final int ID_SELECTION_MODE = android.R.id.selectTextMode;
	// Selection context mode
	private static final int ID_SELECT_ALL = android.R.id.selectAll;
	private static final int ID_CUT = android.R.id.cut;
	private static final int ID_COPY = android.R.id.copy;
	private static final int ID_PASTE = android.R.id.paste;

	private final Context mContext;
	private OnBackPressedListener onBackPressedListener;

	/*
	 * Just the constructors to create a new EditText...
	 */
	public CustomEditText(Context context) {
		super(context);
		this.mContext = context;
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
		this.onBackPressedListener = onBackPressedListener;
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if (mContext!= null) {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

			if (imm.isActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (onBackPressedListener != null){
					onBackPressedListener.onBackPressed();
				}
			}
		}

		return super.dispatchKeyEventPreIme(event);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		// menu.add(0, ID_PASTE, 0, "粘贴").setOnMenuItemClickListener(this);
		// menu.add(0, ID_CUT, 1, "剪切").setOnMenuItemClickListener(this);
		// menu.add(0, ID_COPY, 1, "复制").setOnMenuItemClickListener(this);
		// menu.add(0, ID_SELECT_ALL, 1,
		// "全�?").setOnMenuItemClickListener(this);
		super.onCreateContextMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		return onTextContextMenuItem(item.getItemId());
	}

	@Override
	public boolean onTextContextMenuItem(int id) {
		// Do your thing:
		boolean consumed = super.onTextContextMenuItem(id);
		// React:
		switch (id) {
		case android.R.id.cut:
			onTextCut();
			break;
		case android.R.id.paste:
			onTextPaste();
			break;
		case android.R.id.copy:
			onTextCopy();
		}
		return consumed;
	}

	/**
	 * Text was cut from this EditText.
	 */
	public void onTextCut() {
		ViewInject.toast(mContext, "Cut!" );
	}

	/**
	 * Text was copied from this EditText.
	 */
	public void onTextCopy() {
		ViewInject.toast(mContext, "Copy!" );
	}

	/**
	 * Text was pasted into the EditText.
	 */
	public void onTextPaste() {
		ViewInject.toast(mContext, "Paste!" );
	}

	public interface OnBackPressedListener{
		public void onBackPressed();
	}
}
