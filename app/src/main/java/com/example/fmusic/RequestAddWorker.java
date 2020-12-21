package com.example.fmusic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class RequestAddWorker extends Worker {
    public RequestAddWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {

        Music m = MyData.get_music(getInputData().getString("ID"));
        Map<String, Object> new_fav_mus = new HashMap<>();
        new_fav_mus.put("id_man", MyData.my_id);
        new_fav_mus.put("id_music", m.id);
        Task<DocumentReference> task = MyData.db.collection("fav_music")
                .add(new_fav_mus);
        synchronized (task)
        {
            task.notifyAll();
        }
        m.fav = true;
        MyData.my_music.add(m);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        String channelID = "music_notify";

        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(channelID, "Music", importance);

        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(getApplicationContext(), channelID)
                .setContentTitle("Трэк" + " " + m.track + " Добавлен в избранное")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .build();

        notificationManager.notify(42, notification);

        return Result.success();
    }
}
