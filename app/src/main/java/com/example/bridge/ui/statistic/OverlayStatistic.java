package com.example.bridge.ui.statistic;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bridge.R;
import com.example.bridge.core.StatsManager;
import com.example.bridge.ui.game.GameActivity;
import com.google.android.material.button.MaterialButton;

public class OverlayStatistic {

    private final GameActivity activity;
    private final View root;
    private final StatsManager statsManager;

    // Session TextViews
    private TextView tvSessionGamesJd, tvSessionGamesSp, tvSessionGamesMp;
    private TextView tvSessionDealsJd, tvSessionDealsSp, tvSessionDealsMp;
    private TextView tvSessionConcedesJd, tvSessionConcedesSp, tvSessionConcedesMp;
    private TextView tvSessionImpJd, tvSessionImpSp, tvSessionImpMp;
    private TextView tvSessionMaxImpJd, tvSessionMaxImpSp, tvSessionMaxImpMp;
    private TextView tvSessionWinsJd, tvSessionWinsSp, tvSessionWinsMp;

    // Global TextViews
    private TextView tvGlobalGamesJd, tvGlobalGamesSp, tvGlobalGamesMp;
    private TextView tvGlobalDealsJd, tvGlobalDealsSp, tvGlobalDealsMp;
    private TextView tvGlobalConcedesJd, tvGlobalConcedesSp, tvGlobalConcedesMp;
    private TextView tvGlobalImpJd, tvGlobalImpSp, tvGlobalImpMp;
    private TextView tvGlobalMaxImpJd, tvGlobalMaxImpSp, tvGlobalMaxImpMp;
    private TextView tvGlobalWinsJd, tvGlobalWinsSp, tvGlobalWinsMp;

    public OverlayStatistic(GameActivity activity) {
        this.activity = activity;
        this.root = activity.findViewById(R.id.statistic_overlay);
        this.statsManager = new StatsManager(activity);
        initViews();
        refresh();
    }

    private void initViews() {
        if (root == null) return;

        // Session
        tvSessionGamesJd = root.findViewById(R.id.tv_session_games_jd);
        tvSessionGamesSp = root.findViewById(R.id.tv_session_games_sp);
        tvSessionGamesMp = root.findViewById(R.id.tv_session_games_mp);

        tvSessionDealsJd = root.findViewById(R.id.tv_session_deals_jd);
        tvSessionDealsSp = root.findViewById(R.id.tv_session_deals_sp);
        tvSessionDealsMp = root.findViewById(R.id.tv_session_deals_mp);

        tvSessionConcedesJd = root.findViewById(R.id.tv_session_concedes_jd);
        tvSessionConcedesSp = root.findViewById(R.id.tv_session_concedes_sp);
        tvSessionConcedesMp = root.findViewById(R.id.tv_session_concedes_mp);

        tvSessionImpJd = root.findViewById(R.id.tv_session_imp_jd);
        tvSessionImpSp = root.findViewById(R.id.tv_session_imp_sp);
        tvSessionImpMp = root.findViewById(R.id.tv_session_imp_mp);

        tvSessionMaxImpJd = root.findViewById(R.id.tv_session_max_imp_jd);
        tvSessionMaxImpSp = root.findViewById(R.id.tv_session_max_imp_sp);
        tvSessionMaxImpMp = root.findViewById(R.id.tv_session_max_imp_mp);

        tvSessionWinsJd = root.findViewById(R.id.tv_session_wins_jd);
        tvSessionWinsSp = root.findViewById(R.id.tv_session_wins_sp);
        tvSessionWinsMp = root.findViewById(R.id.tv_session_wins_mp);

        // Global
        tvGlobalGamesJd = root.findViewById(R.id.tv_global_games_jd);
        tvGlobalGamesSp = root.findViewById(R.id.tv_global_games_sp);
        tvGlobalGamesMp = root.findViewById(R.id.tv_global_games_mp);

        tvGlobalDealsJd = root.findViewById(R.id.tv_global_deals_jd);
        tvGlobalDealsSp = root.findViewById(R.id.tv_global_deals_sp);
        tvGlobalDealsMp = root.findViewById(R.id.tv_global_deals_mp);

        tvGlobalConcedesJd = root.findViewById(R.id.tv_global_concedes_jd);
        tvGlobalConcedesSp = root.findViewById(R.id.tv_global_concedes_sp);
        tvGlobalConcedesMp = root.findViewById(R.id.tv_global_concedes_mp);

        tvGlobalImpJd = root.findViewById(R.id.tv_global_imp_jd);
        tvGlobalImpSp = root.findViewById(R.id.tv_global_imp_sp);
        tvGlobalImpMp = root.findViewById(R.id.tv_global_imp_mp);

        tvGlobalMaxImpJd = root.findViewById(R.id.tv_global_max_imp_jd);
        tvGlobalMaxImpSp = root.findViewById(R.id.tv_global_max_imp_sp);
        tvGlobalMaxImpMp = root.findViewById(R.id.tv_global_max_imp_mp);

        tvGlobalWinsJd = root.findViewById(R.id.tv_global_wins_jd);
        tvGlobalWinsSp = root.findViewById(R.id.tv_global_wins_sp);
        tvGlobalWinsMp = root.findViewById(R.id.tv_global_wins_mp);

        // Clear Buttons
        MaterialButton btnClearJustDeclare = root.findViewById(R.id.btn_clear_just_declare);
        MaterialButton btnClearSingleplayer = root.findViewById(R.id.btn_clear_singleplayer);
        MaterialButton btnClearMultiplayer = root.findViewById(R.id.btn_clear_multiplayer);

        if (btnClearJustDeclare != null) {
            btnClearJustDeclare.setOnClickListener(v -> confirmClearSessionStats("Just Declare"));
        }
        if (btnClearSingleplayer != null) {
            btnClearSingleplayer.setOnClickListener(v -> confirmClearSessionStats("Singleplayer"));
        }
        if (btnClearMultiplayer != null) {
            btnClearMultiplayer.setOnClickListener(v -> confirmClearSessionStats("Multiplayer"));
        }
    }

    private void confirmClearSessionStats(String mode) {
        String message = activity.getString(R.string.clear_stats_confirm_message, mode);
        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(R.string.clear_stats_confirm_title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, (dialog, which) -> clearSessionStats(mode))
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void clearSessionStats(String mode) {
        statsManager.clearSessionStats(mode);
        Toast.makeText(activity, R.string.clear_stats_success, Toast.LENGTH_SHORT).show();
        refresh();
    }

    public void refresh() {
        if (root == null || statsManager == null) return;

        // Session Stats
        displayStats(statsManager.getSessionStatsJustDeclare(), tvSessionGamesJd, tvSessionDealsJd, tvSessionConcedesJd, tvSessionImpJd, tvSessionMaxImpJd, tvSessionWinsJd);
        displayStats(statsManager.getSessionStatsSingleplayer(), tvSessionGamesSp, tvSessionDealsSp, tvSessionConcedesSp, tvSessionImpSp, tvSessionMaxImpSp, tvSessionWinsSp);
        displayStats(statsManager.getSessionStatsMultiplayer(), tvSessionGamesMp, tvSessionDealsMp, tvSessionConcedesMp, tvSessionImpMp, tvSessionMaxImpMp, tvSessionWinsMp);

        // Global Stats
        displayStats(statsManager.getGlobalStatsJustDeclare(), tvGlobalGamesJd, tvGlobalDealsJd, tvGlobalConcedesJd, tvGlobalImpJd, tvGlobalMaxImpJd, tvGlobalWinsJd);
        displayStats(statsManager.getGlobalStatsSingleplayer(), tvGlobalGamesSp, tvGlobalDealsSp, tvGlobalConcedesSp, tvGlobalImpSp, tvGlobalMaxImpSp, tvGlobalWinsSp);
        displayStats(statsManager.getGlobalStatsMultiplayer(), tvGlobalGamesMp, tvGlobalDealsMp, tvGlobalConcedesMp, tvGlobalImpMp, tvGlobalMaxImpMp, tvGlobalWinsMp);
    }

    private void displayStats(
            StatsManager.ModeStats stats,
            TextView tvGames, TextView tvDeals, TextView tvConcedes,
            TextView tvImp, TextView tvMaxImp, TextView tvWins
    ) {
        if (tvGames != null) tvGames.setText(String.valueOf(stats.games));
        if (tvDeals != null) tvDeals.setText(String.valueOf(stats.deals));
        if (tvConcedes != null) tvConcedes.setText(String.valueOf(stats.concedes));
        if (tvImp != null) tvImp.setText(stats.getImpFormatted());
        if (tvMaxImp != null) tvMaxImp.setText(stats.getMaxImpFormatted());
        if (tvWins != null) tvWins.setText(stats.getWinsFormatted());
    }

    public void show() {
        if (root != null) {
            root.setVisibility(View.VISIBLE);
            refresh();
        }
    }

    public void hide() {
        if (root != null) {
            root.setVisibility(View.GONE);
        }
    }

    public boolean isVisible() {
        return root != null && root.getVisibility() == View.VISIBLE;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}
