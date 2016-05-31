package dungpt.crazyalarm.vietitpro.crazyalarm.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dungpt.crazyalarm.vietitpro.crazyalarm.obj.AlarmItem;
import dungpt.crazyalarm.vietitpro.crazyalarm.R;
import dungpt.crazyalarm.vietitpro.crazyalarm.utils.ui.TintableImageView;

/**
 * Created by DungPT17 on 2016/05/17.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<AlarmItem> mAlarms;

    public AlarmAdapter(List<AlarmItem> alarms) {
        this.mAlarms = alarms;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alarm_item, viewGroup, false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder alarmViewHolder, int position) {
        alarmViewHolder.tvTime.setText(mAlarms.get(position).getTime());
        alarmViewHolder.tvDate.setText(mAlarms.get(position).getDate());
        alarmViewHolder.ivType.setImageResource(mAlarms.get(position).getType());
        alarmViewHolder.ivAlarm.setSelected(mAlarms.get(position).isEnable());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public List<AlarmItem> getAlarmList() {
        return mAlarms;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView tvTime;
        TextView tvDate;
        ImageView ivType;
        TintableImageView ivAlarm;

        AlarmViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            ivType = (TintableImageView) itemView.findViewById(R.id.img_alarm_type);
            ivType.setSelected(true);
            ivAlarm = (TintableImageView) itemView.findViewById(R.id.iv_alarm);
            ivAlarm.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TintableImageView current =
                    (TintableImageView) v.findViewById(R.id.iv_alarm);
            if (current == null) {
                return;
            }
            current.setSelected(!current.isSelected());
        }
    }
}
