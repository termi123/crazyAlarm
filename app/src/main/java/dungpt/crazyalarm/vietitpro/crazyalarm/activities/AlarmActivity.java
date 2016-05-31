package dungpt.crazyalarm.vietitpro.crazyalarm.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dungpt.crazyalarm.vietitpro.crazyalarm.AlarmReceiver;
import dungpt.crazyalarm.vietitpro.crazyalarm.R;
import dungpt.crazyalarm.vietitpro.crazyalarm.asyntask.GetPlayListTask;
import dungpt.crazyalarm.vietitpro.crazyalarm.utils.Constant;

public class AlarmActivity extends AppCompatActivity
        implements
        TimePickerDialog.OnTimeSetListener {

    private static final String TAG = AlarmActivity.class.getSimpleName();
    private static AlarmActivity inst;
    private TextView alarmTextView;
    private Calendar mCalendar;
    private int mAlarmHour;
    private int mAlarmMinutes;
    private String mSelectedSong;
    private ArrayList<HashMap<String, String>> mSongsListData = new ArrayList<>();

    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getPlayList();
        setUpToolbar();
        setUpTimeView();
        setUpRepeatView(null);
        setUpSelectRingtoneView(null);
        setUpVibrateView();
        setUpTurnOffAlarmView(null);
        setUpSetAlarmButton();
        showTimePickerDialog();
    }

    private void getPlayList() {
        new GetPlayListTask() {
            @Override
            public void getPlayListSuccess(ArrayList<HashMap<String, String>> songsListData) {
                mSongsListData = songsListData;
            }
        };
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpTimeView() {
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        LinearLayout llSetTime = (LinearLayout) findViewById(R.id.ll_time);
        if (llSetTime == null) {
            Log.i(TAG, "llSetTime == null");
            return;
        }
        llSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    private void setUpRepeatView(String listDaysRepeat) {
        LinearLayout llSetTime = (LinearLayout) findViewById(R.id.ll_repeat);
        if (llSetTime == null) {
            Log.i(TAG, "llSetTime == null");
            return;
        }
        llSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDaysRepeatDialog();
            }
        });
        if (listDaysRepeat == null || listDaysRepeat.isEmpty()) {
            return;
        }
        TextView repeatText = (TextView) findViewById(R.id.repeatText);
        if (repeatText == null) {
            Log.i(TAG, "repeatText == null");
            return;
        }
        repeatText.setText(listDaysRepeat);
    }

    ArrayList<Integer> selectedItems = new ArrayList<>();

    private void showDaysRepeatDialog() {
        AlertDialog dialog;
        final CharSequence[] items = getResources().getStringArray(R.array.days_array);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final boolean[] primitives = new boolean[items.length];
        for (int i = 0; i < items.length; i++) {
            primitives[i] = selectedItems.contains(i);
        }
        View customTitleView = getLayoutInflater().inflate(R.layout.alert_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title)).setText(getText(R.string.select_day));
        builder.setCustomTitle(customTitleView);
        builder.setMultiChoiceItems(items, primitives,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(indexSelected);
                        } else if (selectedItems.contains(indexSelected)) {
                            selectedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ListView list = ((AlertDialog) dialog).getListView();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < list.getCount(); i++) {
                            boolean checked = list.isItemChecked(i);
                            if (checked) {
                                if (stringBuilder.length() > 0) stringBuilder.append(",");
                                stringBuilder.append(list.getItemAtPosition(i));
                            }
                        }
                        setUpRepeatView(stringBuilder.toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        dialog = builder.create();
        dialog.show();
    }

    private void setUpSelectRingtoneView(HashMap fileInfo) {
        LinearLayout llRingTone = (LinearLayout) findViewById(R.id.ll_ring);
        if (llRingTone == null) {
            Log.i(TAG, "llRingTone == null");
            return;
        }
        llRingTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectRingToneDialog();
            }
        });
        TextView ringText = (TextView) findViewById(R.id.ringText);
        if (ringText == null
                || fileInfo == null
                || TextUtils.isEmpty(fileInfo.get(Constant.SONG_TITLE).toString())
                || TextUtils.isEmpty(fileInfo.get(Constant.SONG_PATH).toString())) {
            Log.i(TAG, "ringText == null");
            return;
        }
        ringText.setText(fileInfo.get(Constant.SONG_TITLE).toString());
        mSelectedSong = fileInfo.get(Constant.SONG_PATH).toString();
    }

    private void showSelectRingToneDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customTitleView = getLayoutInflater().inflate(R.layout.alert_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title)).setText(R.string.select_ringtone);
        builder.setCustomTitle(customTitleView);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                mSongsListData, R.layout.ringtone_list_item,
                new String[]{Constant.SONG_TITLE}, new int[]{R.id.songTitle});

        builder.setSingleChoiceItems(simpleAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView list = ((AlertDialog) dialog).getListView();
                setUpSelectRingtoneView(((HashMap<String, String>)
                        list.getItemAtPosition(which)));
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void setUpVibrateView() {
        RelativeLayout llVibrate = (RelativeLayout) findViewById(R.id.ll_vibrate);
        if (llVibrate == null) {
            Log.i(TAG, "llVibrate == null");
            return;
        }
        final CheckBox cbVibrate = (CheckBox) findViewById(R.id.cb_vibrate);

        llVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbVibrate == null) {
                    Log.i(TAG, "cbVibrate == null");
                    return;
                }
                cbVibrate.setChecked(!cbVibrate.isChecked());
            }
        });
    }

    private void setUpTurnOffAlarmView(String turnOff) {
        LinearLayout llTurnOffRingTone = (LinearLayout) findViewById(R.id.ll_turn_off);
        if (llTurnOffRingTone == null) {
            Log.i(TAG, "llTurnOffRingTone == null");
            return;
        }
        llTurnOffRingTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectOffAlarmMethodDialog();
            }
        });
        TextView turnOffMethod = (TextView) findViewById(R.id.turnOffText);
        if (turnOffMethod == null || TextUtils.isEmpty(turnOff)) {
            Log.i(TAG, "turnOffMethod == null");
            return;
        }
        turnOffMethod.setText(turnOff);
    }

    private void showSelectOffAlarmMethodDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customTitleView = getLayoutInflater().inflate(R.layout.alert_title, null);
        ((TextView) customTitleView.findViewById(R.id.dialog_title)).setText(R.string.turn_off_method);
        builder.setCustomTitle(customTitleView);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                getAllMethod(), R.layout.ringtone_list_item,
                new String[]{Constant.METHOD, Constant.ICON},
                new int[]{R.id.songTitle, R.id.iv_method});
        builder.setSingleChoiceItems(simpleAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private List<HashMap<String, String>> getAllMethod() {
        int[] listIcon = new int[]{R.drawable.ic_add_a_photo, R.drawable.ic_calc};
        String[] listMethod = getResources().getStringArray(R.array.turn_off_method);
        ArrayList<HashMap<String, String>> storageInfo = new ArrayList<>();
        for (int i = 0; i < listIcon.length; i++) {
            HashMap<String, String> datum = new HashMap<>();
            datum.put(Constant.ICON, Integer.toString(listIcon[i]));
            datum.put(Constant.METHOD, listMethod[i]);
            storageInfo.add(datum);
        }
        return storageInfo;
    }

    private void setUpSetAlarmButton() {
        Button mSetAlertButton = (Button) findViewById(R.id.btn_set_alarm);
        if (mSetAlertButton == null) {
            Log.i(TAG, "mSetAlertButton == null");
            return;
        }
        mSetAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
    }

    private void showTimePickerDialog() {
        mCalendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AlarmActivity.this,
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.vibrate(true);
        tpd.dismissOnPause(false);
        tpd.enableSeconds(false);
        tpd.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        tpd.setTitle(getString(R.string.time_picker));
        tpd.setCancelable(false);
        tpd.setTimeInterval(1, 1, 10);
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(AlarmActivity.this, R.string.alarm_not_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        tpd.show(getFragmentManager(), Constant.EMPTY_STRING);
    }

    public void setAlarm() {
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        myIntent.putExtra(Constant.SONGNAME, mSelectedSong);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(AlarmActivity.this,
                (int) System.currentTimeMillis(), myIntent, 0);
        Log.d(TAG, "Alarm On");
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, mAlarmHour);
        mCalendar.set(Calendar.MINUTE, mAlarmMinutes);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                mCalendar.getTimeInMillis(), mPendingIntent);
        //mAlarmManager.cancel(mPendingIntent);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mAlarmHour = hourOfDay;
        mAlarmMinutes = minute;
        String hourString = hourOfDay < 10 ? Constant.ZERO + hourOfDay : Constant.EMPTY_STRING + hourOfDay;
        String minuteString = minute < 10 ? Constant.ZERO + minute : Constant.EMPTY_STRING + minute;
        alarmTextView.setText(String.format(getString(R.string.time_format), hourString, minuteString));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(AlarmActivity.this, getString(R.string.alarm_not_saved), Toast.LENGTH_SHORT).show();
        finish();
    }
}
