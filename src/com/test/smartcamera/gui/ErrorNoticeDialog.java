package com.test.smartcamera.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.test.smartcamera.R;

public class ErrorNoticeDialog extends DialogFragment
{
	protected int mMsgRes = 0;
	public void setMsg(int aMsgRes)
	{
		assert(aMsgRes != 0);
		mMsgRes = aMsgRes;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(mMsgRes);
		builder.setPositiveButton(R.string.dialog_ok,
				new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
