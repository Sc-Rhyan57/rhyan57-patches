package app.revanced.extension.youtube.announcements;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Rhyan57AnnouncementHelper {

    private static final String PREFS_NAME = "rhyan57_patch_prefs";
    private static final String SHOWN_KEY = "rhyan57_announcement_shown";
    private static final String DISCORD_URL = "https://discord.gg/rhyan57";

    public static void showAnnouncementIfNeeded(Activity activity) {
        if (activity == null) return;

        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean alreadyShown = prefs.getBoolean(SHOWN_KEY, false);

        if (alreadyShown) return;

        activity.runOnUiThread(() -> showDialog(activity));
    }

    private static void showDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        int dp16 = dpToPx(activity, 16);
        int dp8 = dpToPx(activity, 8);
        int dp12 = dpToPx(activity, 12);
        int dp24 = dpToPx(activity, 24);

        root.setPadding(dp24, dp24, dp24, dp16);

        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setShape(GradientDrawable.RECTANGLE);
        bgDrawable.setCornerRadius(dpToPx(activity, 20));
        bgDrawable.setColor(Color.parseColor("#1A1A2E"));
        bgDrawable.setStroke(dpToPx(activity, 2), Color.parseColor("#E94560"));
        root.setBackground(bgDrawable);

        TextView emojiView = new TextView(activity);
        emojiView.setText("🧩");
        emojiView.setTextSize(36);
        emojiView.setGravity(Gravity.CENTER);
        emojiView.setPadding(0, 0, 0, dp8);
        root.addView(emojiView);

        TextView titleView = new TextView(activity);
        titleView.setText("patch By rhyan57");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.parseColor("#E94560"));
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(0, 0, 0, dp8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            titleView.setLetterSpacing(0.05f);
        }
        root.addView(titleView);

        View divider = new View(activity);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                dpToPx(activity, 60), dpToPx(activity, 2));
        dividerParams.gravity = Gravity.CENTER_HORIZONTAL;
        dividerParams.bottomMargin = dp12;
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.parseColor("#E94560"));
        root.addView(divider);

        TextView subtitleView = new TextView(activity);
        subtitleView.setText("YouTube ReVanced modificado com patches exclusivos.\nGmsCore • Download • Sem anúncios");
        subtitleView.setTextSize(13);
        subtitleView.setTextColor(Color.parseColor("#AAAACC"));
        subtitleView.setGravity(Gravity.CENTER);
        subtitleView.setPadding(0, 0, 0, dp16);
        root.addView(subtitleView);

        Button discordButton = new Button(activity);
        discordButton.setText("  Discord");
        discordButton.setTextColor(Color.WHITE);
        discordButton.setTextSize(14);
        GradientDrawable discordBg = new GradientDrawable();
        discordBg.setShape(GradientDrawable.RECTANGLE);
        discordBg.setCornerRadius(dpToPx(activity, 50));
        discordBg.setColor(Color.parseColor("#5865F2"));
        discordButton.setBackground(discordBg);
        LinearLayout.LayoutParams discordParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(activity, 44));
        discordParams.bottomMargin = dp8;
        discordButton.setLayoutParams(discordParams);
        discordButton.setPadding(dp16, 0, dp16, 0);
        discordButton.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DISCORD_URL));
                activity.startActivity(browserIntent);
            } catch (Exception ignored) {}
        });
        root.addView(discordButton);

        Button dontShowButton = new Button(activity);
        dontShowButton.setText("Não mostrar mais");
        dontShowButton.setTextColor(Color.parseColor("#AAAACC"));
        dontShowButton.setTextSize(13);
        GradientDrawable dontShowBg = new GradientDrawable();
        dontShowBg.setShape(GradientDrawable.RECTANGLE);
        dontShowBg.setCornerRadius(dpToPx(activity, 50));
        dontShowBg.setColor(Color.parseColor("#2A2A3E"));
        dontShowBg.setStroke(dpToPx(activity, 1), Color.parseColor("#444466"));
        dontShowButton.setBackground(dontShowBg);
        LinearLayout.LayoutParams dontShowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(activity, 40));
        dontShowParams.bottomMargin = dp8;
        dontShowButton.setLayoutParams(dontShowParams);
        dontShowButton.setOnClickListener(v -> {
            SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putBoolean(SHOWN_KEY, true).apply();
            dialog.dismiss();
        });
        root.addView(dontShowButton);

        Button closeButton = new Button(activity);
        closeButton.setText("Fechar");
        closeButton.setTextColor(Color.parseColor("#E94560"));
        closeButton.setTextSize(13);
        GradientDrawable closeBg = new GradientDrawable();
        closeBg.setShape(GradientDrawable.RECTANGLE);
        closeBg.setCornerRadius(dpToPx(activity, 50));
        closeBg.setColor(Color.TRANSPARENT);
        closeBg.setStroke(dpToPx(activity, 1), Color.parseColor("#E94560"));
        closeButton.setBackground(closeBg);
        LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(activity, 40));
        closeButton.setLayoutParams(closeParams);
        closeButton.setOnClickListener(v -> dialog.dismiss());
        root.addView(closeButton);

        dialog.setContentView(root);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.88f);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }

        try {
            dialog.show();
        } catch (Exception ignored) {}
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
