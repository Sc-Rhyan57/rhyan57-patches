package app.revanced.extension.youtube.player.download;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class VideoDownloadHelper {

    private static boolean downloadButtonEnabled = true;

    public static void enableDownloadButton() {
        downloadButtonEnabled = true;
    }

    public static void setDownloadButtonVisible(boolean visible) {
        downloadButtonEnabled = visible;
    }

    public static boolean isDownloadButtonEnabled() {
        return downloadButtonEnabled;
    }

    public static void onDownloadButtonClicked(Activity activity, String videoId) {
        if (activity == null || videoId == null || videoId.isEmpty()) return;

        try {
            String[] downloadApps = {
                "com.github.libretube",
                "org.schabi.newpipe",
                "me.echeung.moemoekyun"
            };

            for (String pkg : downloadApps) {
                try {
                    Intent specificIntent = activity.getPackageManager()
                        .getLaunchIntentForPackage(pkg);
                    if (specificIntent != null) {
                        Intent downloadIntent = new Intent(Intent.ACTION_SEND);
                        downloadIntent.setType("text/plain");
                        downloadIntent.setPackage(pkg);
                        downloadIntent.putExtra(Intent.EXTRA_TEXT,
                            "https://www.youtube.com/watch?v=" + videoId);
                        activity.startActivity(downloadIntent);
                        return;
                    }
                } catch (Exception ignored) {}
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoId));
            Intent chooser = Intent.createChooser(intent, "Baixar vídeo");
            activity.startActivity(chooser);

        } catch (Exception e) {
            Toast.makeText(activity, "Download não disponível para este vídeo", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showDownloadOptions(Activity activity, String videoId, String videoTitle) {
        if (activity == null) return;

        String[] options = {"Baixar Vídeo (MP4)", "Baixar Áudio (MP3)", "Copiar link"};

        new android.app.AlertDialog.Builder(activity)
            .setTitle(videoTitle != null ? videoTitle : "Download")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                    case 1:
                        onDownloadButtonClicked(activity, videoId);
                        break;
                    case 2:
                        android.content.ClipboardManager clipboard =
                            (android.content.ClipboardManager) activity
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) {
                            android.content.ClipData clip = android.content.ClipData.newPlainText(
                                "YouTube URL",
                                "https://www.youtube.com/watch?v=" + videoId
                            );
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(activity, "Link copiado!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}
