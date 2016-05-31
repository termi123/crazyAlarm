package dungpt.crazyalarm.vietitpro.crazyalarm.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DungPT17 on 2016/05/18.
 */
public class SongsManager {

    public final static String MEDIA_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/";
    private static ArrayList<HashMap<String, String>> songsList = new ArrayList<>();

    public static ArrayList<HashMap<String, String>> getPlayList() {
        if (!TextUtils.isEmpty(MEDIA_PATH)) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        return songsList;
    }

    private static void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
    }

    private static void addSongToList(File song) {
        String mp3Pattern = ".mp3";
        if (song.getName().endsWith(mp3Pattern)) {
            HashMap<String, String> songMap = new HashMap<>();
            songMap.put("songTitle",
                    song.getName().substring(0, (song.getName().length() - 4)));
            songMap.put("songPath", song.getPath());
            // Adding each song to SongList
            songsList.add(songMap);
        }
    }
}
