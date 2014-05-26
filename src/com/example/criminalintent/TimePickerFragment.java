package com.example.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.criminalintent.R.id;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME =
			"com.example.criminalintent.time";
	
	private Date mDate;
	
	public static TimePickerFragment newInstance(Date date){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, date);
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private void sendResult(int resultCode){
		if(getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME, mDate);
		
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, i);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date) getArguments().getSerializable(EXTRA_TIME);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_time, null);
		
		TimePicker timePicker = (TimePicker) v.findViewById(id.dialog_time_timePicker);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker arg0, int hour, int minute) {
				mDate = new GregorianCalendar(year, month, day, hour, minute).getTime();
				getArguments().putSerializable(EXTRA_TIME, mDate);
				
			}
		});
		
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.time_picker_title)
			.setPositiveButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sendResult(Activity.RESULT_OK);
					
				}
			})
			.create();
	}

}
