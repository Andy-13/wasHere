package com.example.yang.washere.Account;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.yang.washere.R;

import java.util.Calendar;
import java.util.Date;

public class BirthdayPickerFragment extends DialogFragment implements OnClickListener {

    private DatePicker mDatePicker;
    private Button mCancelBtn;
    private Button mAcceptBtn;

    private Date mMinDate;
    private Date mMaxDate;
    private Date mSelDate;
    private OnDatePickerClickListener mOnDatePickerClickListener;

    public void setOnDatePickerClickListener(OnDatePickerClickListener l) {
        mOnDatePickerClickListener = l;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDatePickerDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(R.layout.fragment_birthday_picker);
        dialog.setCanceledOnTouchOutside(true);

        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        mDatePicker = (DatePicker) dialog.findViewById(R.id.dialog_dashboard_date_datePicker);
        mCancelBtn = (Button) dialog.findViewById(R.id.dialog_dashboard_date_cancel);
        mAcceptBtn = (Button) dialog.findViewById(R.id.dialog_dashboard_date_accept);

        if (mSelDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mSelDate);
            mDatePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
            mDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        if (mMinDate != null) {
            mDatePicker.setMinDate(mMinDate.getTime());
        }

        if (mMaxDate != null) {
            mDatePicker.setMaxDate(mMaxDate.getTime());
        }

        mCancelBtn.setOnClickListener(this);
        mAcceptBtn.setOnClickListener(this);

        return dialog;
    }

    public void setSelectedDate(Date date) {
        mSelDate = date;
    }

    public void setMinDate(Date minDate) {
        mMinDate = minDate;
    }

    public void setMaxDate(Date maxDate) {
        mMaxDate = maxDate;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mOnDatePickerClickListener != null) {
            if (id == R.id.dialog_dashboard_date_cancel) {
                mOnDatePickerClickListener.onCancelClick();
            } else if (id == R.id.dialog_dashboard_date_accept) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int dayOfMonth = mDatePicker.getDayOfMonth();
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                mOnDatePickerClickListener.onAcceptClick(cal.getTime());
            }
        }
    }

    public interface OnDatePickerClickListener {
        public void onCancelClick();

        public void onAcceptClick(Date date);
    }

}
