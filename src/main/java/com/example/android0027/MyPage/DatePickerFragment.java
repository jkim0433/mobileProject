package com.example.android0027.MyPage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText edtNumber;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void setEditText(EditText edtNumber) {
        this.edtNumber = edtNumber;
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // 사용자로부터 입력받은 날짜를 전달받아 활용하는 코드 작성을 합니다.
        String msg = year + "/" + (month+1) + "/" + day;

        edtNumber.setText(msg);
    }
}
