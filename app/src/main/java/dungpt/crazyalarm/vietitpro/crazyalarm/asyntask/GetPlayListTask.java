package dungpt.crazyalarm.vietitpro.crazyalarm.asyntask;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import dungpt.crazyalarm.vietitpro.crazyalarm.utils.SongsManager;

/**
 * Created by te on 5/30/2016.
 */
public abstract class GetPlayListTask extends AsyncTask {
    private static final String TAG = GetPlayListTask.class.getSimpleName();
    private ArrayList<HashMap<String, String>> songsListData = new ArrayList<>();

    @Override
    protected Object doInBackground(Object[] params) {
        ArrayList<HashMap<String, String>> songsList = SongsManager.getPlayList();
        if (songsList.isEmpty()) {
            Log.i(TAG, "songsList: empty");
            return songsListData;
        }
        for (int i = 0; i < songsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = songsList.get(i);
            // adding HashList to ArrayList
            songsListData.add(song);
        }
        return songsListData;
    }

    @Override
    protected void onPostExecute(Object o) {
        getPlayListSuccess(songsListData);
    }

    public abstract void getPlayListSuccess(ArrayList<HashMap<String, String>> songsListData);
}
