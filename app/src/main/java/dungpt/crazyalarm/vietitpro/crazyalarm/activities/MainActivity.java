package dungpt.crazyalarm.vietitpro.crazyalarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dungpt.crazyalarm.vietitpro.crazyalarm.obj.AlarmItem;
import dungpt.crazyalarm.vietitpro.crazyalarm.adapter.AlarmAdapter;
import dungpt.crazyalarm.vietitpro.crazyalarm.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Button btnAddAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpButtonAddAlarm();
        setUpFloatingButton();
        setUpRecyclerView();
        setUpSwipeForRecyclerView();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpButtonAddAlarm() {
        btnAddAlarm = (Button) findViewById(R.id.btn_add_alarm);
        if (btnAddAlarm == null) {
            Log.i(TAG, "fab == null");
            return;
        }
        btnAddAlarm.setOnClickListener(this);
    }

    private void setUpFloatingButton() {
        FloatingActionButton fabImage = (FloatingActionButton) findViewById(R.id.fab_alarm_image);
        if (fabImage == null) {
            Log.i(TAG, "fab == null");
            return;
        }
        fabImage.setOnClickListener(this);

        FloatingActionButton fabCalc = (FloatingActionButton) findViewById(R.id.fab_alarm_calc);
        if (fabCalc == null) {
            Log.i(TAG, "fab == null");
            return;
        }
        fabCalc.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        if (mRecyclerView == null) {
            Log.i(TAG, "mRecyclerView == null");
            return;
        }
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AlarmAdapter(getAlarmList());
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<AlarmItem> getAlarmList() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");
        String time = dateformat.format(c.getTime());

        List<AlarmItem> items = new ArrayList<>();
        items.add(new AlarmItem(time, "Sunday", R.drawable.ic_add_a_photo, true));
        items.add(new AlarmItem(time, "Sunday", R.drawable.ic_calc, false));
        return items;
    }

    private void setUpSwipeForRecyclerView() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback
            (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            ((AlarmAdapter) mAdapter).getAlarmList().remove(viewHolder.getLayoutPosition());
            mAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
        }
    };

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_add_alarm:
                goToAddAlarmScreen();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.fab_alarm_image:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.fab_alarm_calc:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            default:
                break;
        }

    }

    private void goToAddAlarmScreen() {
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(intent);
//        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
    }
}
