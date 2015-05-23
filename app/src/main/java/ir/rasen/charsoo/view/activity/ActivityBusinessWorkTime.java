package ir.rasen.charsoo.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.helper.WorkTime;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.ButtonFont;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityBusinessWorkTime extends CharsooActivity implements View.OnClickListener, IWebserviceResponse {


    EditTextFont editTextTimeOpenHour, editTextTimeOpenMinute, editTextTimeCloseHour, editTextTimeCloseMinute;
    ButtonFont btnSat, btnSun, btnMon, btnTue, btnWed, btnThr, btnFri, btnSubmit;
    boolean sat, sun, mon, tue, wed, thr, fri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_work_time);
        setTitle(getResources().getString(R.string.work_time));
        boolean isEditting = false;
        try {
            isEditting = getIntent().getExtras().getBoolean(Params.IS_EDITTING);
        } catch (Exception e) {

        }

        (findViewById(R.id.btn_submit)).setOnClickListener(this);

        btnSat = (ButtonFont) findViewById(R.id.button_sat);
        btnSun = (ButtonFont) findViewById(R.id.button_sun);
        btnMon = (ButtonFont) findViewById(R.id.button_mon);
        btnTue = (ButtonFont) findViewById(R.id.button_tue);
        btnWed = (ButtonFont) findViewById(R.id.button_wed);
        btnThr = (ButtonFont) findViewById(R.id.button_thr);
        btnFri = (ButtonFont) findViewById(R.id.button_fri);

        btnSat.setOnClickListener(this);
        btnSun.setOnClickListener(this);
        btnMon.setOnClickListener(this);
        btnTue.setOnClickListener(this);
        btnWed.setOnClickListener(this);
        btnThr.setOnClickListener(this);
        btnFri.setOnClickListener(this);

        editTextTimeCloseHour = (EditTextFont) findViewById(R.id.edt_time_close_hour);
        editTextTimeCloseMinute = (EditTextFont) findViewById(R.id.edt_time_close_minute);
        editTextTimeOpenHour = (EditTextFont) findViewById(R.id.edt_time_open_hour);
        editTextTimeOpenMinute = (EditTextFont) findViewById(R.id.edt_time_open_minute);

        if (isEditting) {
            WorkTime workTime = ((MyApplication) getApplication()).workTime;
            if (workTime.getSat()) {
                btnSat.setBackgroundResource(R.drawable.selector_button_day_selected);
                sat = true;
            }
            if (workTime.getSun()) {
                btnSun.setBackgroundResource(R.drawable.selector_button_day_selected);
                sun = true;
            }
            if (workTime.getMon()) {
                btnMon.setBackgroundResource(R.drawable.selector_button_day_selected);
                mon = true;
            }
            if (workTime.getTue()) {
                btnTue.setBackgroundResource(R.drawable.selector_button_day_selected);
                tue = true;
            }
            if (workTime.getWed()) {
                btnWed.setBackgroundResource(R.drawable.selector_button_day_selected);
                wed = true;
            }
            if (workTime.getThr()) {
                btnThr.setBackgroundResource(R.drawable.selector_button_day_selected);
                thr = true;
            }
            if (workTime.getFri()) {
                btnFri.setBackgroundResource(R.drawable.selector_button_day_selected);
                fri = true;
            }

            editTextTimeCloseHour.setText(String.valueOf(workTime.time_close_hour));
            editTextTimeCloseMinute.setText(String.valueOf(workTime.time_close_minutes));
            editTextTimeOpenHour.setText(String.valueOf(workTime.time_open_hour));
            editTextTimeOpenMinute.setText(String.valueOf(workTime.time_open_minutes));

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sat:
                //if sat is chosen before
                if (sat) {
                    sat = false;
                    btnSat.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    sat = true;
                    btnSat.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.button_sun:
                //if sat is chosen before
                if (sun) {
                    sun = false;
                    btnSun.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    sun = true;
                    btnSun.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.button_mon:
                //if sat is chosen before
                if (mon) {
                    mon = false;
                    btnMon.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    mon = true;
                    btnMon.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.button_tue:
                //if sat is chosen before
                if (tue) {
                    tue = false;
                    btnTue.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    tue = true;
                    btnTue.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.button_wed:
                //if sat is chosen before
                if (wed) {
                    wed = false;
                    btnWed.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    wed = true;
                    btnWed.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.button_thr:
                //if sat is chosen before
                if (thr) {
                    thr = false;
                    btnThr.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    thr = true;
                    btnThr.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.button_fri:
                //if sat is chosen before
                if (fri) {
                    fri = false;
                    btnFri.setBackgroundResource(R.drawable.selector_button_day);
                } else {
                    fri = true;
                    btnFri.setBackgroundResource(R.drawable.selector_button_day_selected);
                }
                break;
            case R.id.btn_submit:
                //if user doesn't choose any days
                if (!(sat || sun || mon || tue || wed || thr || fri)) {
                    new DialogMessage(ActivityBusinessWorkTime.this, getString(R.string.err_choose_days)).show();
                    return;
                }

                if (!Validation.validateHour(ActivityBusinessWorkTime.this, editTextTimeOpenHour.getText().toString()).isValid()) {
                    editTextTimeOpenHour.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validateMinute(ActivityBusinessWorkTime.this, editTextTimeOpenMinute.getText().toString()).isValid()) {
                    editTextTimeOpenMinute.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validateHour(ActivityBusinessWorkTime.this, editTextTimeCloseHour.getText().toString()).isValid()) {
                    editTextTimeCloseHour.setError(Validation.getErrorMessage());
                    return;
                }

                if (!Validation.validateMinute(ActivityBusinessWorkTime.this, editTextTimeCloseMinute.getText().toString()).isValid()) {
                    editTextTimeCloseMinute.setError(Validation.getErrorMessage());
                    return;
                }


                int hourClose = Integer.valueOf(editTextTimeCloseHour.getText().toString());
                int hourOpen = Integer.valueOf(editTextTimeOpenHour.getText().toString());
                int minuteClose = Integer.valueOf(editTextTimeCloseMinute.getText().toString());
                int minuteOpen = Integer.valueOf(editTextTimeOpenMinute.getText().toString());

                if ((hourOpen > hourClose) || (hourClose == hourOpen && minuteOpen > minuteClose)) {
                    editTextTimeCloseHour.setError(getString(R.string.err_time_open_smaller_than_close));
                    return;
                }
                if (hourClose == hourOpen && minuteClose == minuteOpen) {
                    editTextTimeCloseHour.setError(getString(R.string.err_time_open_equal_close));
                    return;
                }

                WorkTime workTime = new WorkTime();
                workTime.setSat(sat);
                workTime.setSun(sun);
                workTime.setMon(mon);
                workTime.setTue(tue);
                workTime.setWed(wed);
                workTime.setThr(thr);
                workTime.setFri(fri);
                workTime.time_close_hour = hourClose;
                workTime.time_close_minutes = minuteClose;
                workTime.time_open_hour = hourOpen;
                workTime.time_open_minutes = minuteOpen;

                ((MyApplication) getApplication()).workTime = workTime;
                Intent i = getIntent();
                setResult(RESULT_OK, i);
                finish();
                break;
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResult(Object result) {

    }

    @Override
    public void getError(Integer errorCode) {

        new DialogMessage(ActivityBusinessWorkTime.this, ServerAnswer.getError(ActivityBusinessWorkTime.this, errorCode)).show();
    }
}
