package dungpt.crazyalarm.vietitpro.crazyalarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by DungPT17 on 2016/05/16.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
//            String mSelectedSong = intent.getStringExtra("songname");
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
            ringtone.play();

//            MediaPlayer mMediaPlayer = new MediaPlayer();
//            mMediaPlayer = MediaPlayer.create(context, Uri.parse(mSelectedSong));
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setLooping(true);
//            mMediaPlayer.start();

            //this will send a notification message
            ComponentName comp = new ComponentName(context.getPackageName(),
                    AlarmService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
}
