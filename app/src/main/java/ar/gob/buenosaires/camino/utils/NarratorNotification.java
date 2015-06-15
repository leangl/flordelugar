package ar.gob.buenosaires.camino.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import ar.gob.buenosaires.camino.R;
import ar.gob.buenosaires.camino.activities.MainActivity;
import ar.gob.buenosaires.camino.model.Venue;
import ar.gob.buenosaires.camino.services.NarratorService;

public class NarratorNotification {

    public static final int NOTIFICATION_ID = 3562;

    public static Notification notify(Context ctx, Info info) {

        Intent openAppIntent = new Intent(ctx, MainActivity.class);
        openAppIntent.putExtra(MainActivity.INTENT_SHOW_VENUE_EXTRA, info.venue);
        PendingIntent openAppPendignIntent = PendingIntent.getActivity(ctx, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopServiceIntent = new Intent(ctx, NarratorService.class);
        stopServiceIntent.setAction(NarratorService.ACTION_TOGGLE);
        PendingIntent stopServicePendingIntent = PendingIntent.getService(ctx, 0, stopServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.notification);
        remoteViews.setImageViewResource(R.id.notification_image, info.venue.getImageResId());
        remoteViews.setTextViewText(R.id.notification_title, info.venue.name);
        remoteViews.setTextViewText(R.id.notification_text, info.venue.description);
        remoteViews.setOnClickPendingIntent(R.id.toggle, stopServicePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.stat_main_content, openAppPendignIntent);
        remoteViews.setImageViewResource(R.id.toggle, info.icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            remoteViews.setTextColor(R.id.notification_text, ctx.getResources().getColor(R.color.transparent_light_black));
            remoteViews.setTextColor(R.id.notification_title, ctx.getResources().getColor(android.R.color.widget_edittext_dark));
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setContentTitle(info.venue.name)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(openAppPendignIntent)
                .setTicker(info.venue.name)
                .setOnlyAlertOnce(true)
                .setContent(remoteViews)
                .setContentIntent(openAppPendignIntent);

        Notification n = builder.build();
        n.flags |= Notification.FLAG_ONGOING_EVENT;
        n.contentView = remoteViews; // Fixes bug in support library

        final NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, n);

        return n;
    }

    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }

    public static void waiting(Context ctx, Venue nextVenue) {
        Intent openAppIntent = new Intent(ctx, MainActivity.class);
        openAppIntent.putExtra(MainActivity.INTENT_SHOW_VENUE_MAP_EXTRA, nextVenue);
        PendingIntent openAppPendignIntent = PendingIntent.getActivity(ctx, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = new Notification.Builder(ctx)
                .setContentTitle("Esperando próximo punto")
                .setContentText("Aprox. a " + nextVenue.distanceToCurrentLocation() + " metros de " + nextVenue.name)
                .setSmallIcon(nextVenue.getImageResId())
                .setOngoing(true)
                .setContentIntent(openAppPendignIntent)
                .build();

        final NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_ID, n);
    }

    public static class Info {
        public final Venue venue;
        public final int icon;

        public Info(Venue v, int i) {
            venue = v;
            icon = i;
        }
    }
}
