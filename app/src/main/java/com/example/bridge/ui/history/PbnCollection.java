package com.example.bridge.ui.history;

import com.example.bridge.ui.game.GameActivity;
public class PbnCollection {
    private GameActivity gameActivity;

    private Pbn pbn;
    private Pbn pbnNoSystem;
    private Pbn pbnNatC;
    private Pbn pbnWj2025Simple;
    private Pbn pbnWj2025;
    private Pbn pbnLCStandard;

    public PbnCollection(GameActivity gameActivity) {
        this.gameActivity = gameActivity;

        this.pbn = new Pbn(gameActivity, "Current");
        this.pbnNoSystem = new Pbn(gameActivity, "NoSystem");
        this.pbnNatC = new Pbn(gameActivity, "NatC");
        this.pbnWj2025Simple = new Pbn(gameActivity, "Wj2025Simple");
        this.pbnWj2025 = new Pbn(gameActivity, "Wj2025");
        this.pbnLCStandard = new Pbn(gameActivity, "LCStandard");
    }

    public void initAllPbn() {

    }
    public GameActivity getGameActivity() {
        return gameActivity;
    }
    public Pbn getPbnNoSystem() {
        return pbnNoSystem;
    }
    public Pbn getPbnNatC() {
        return pbnNatC;
    }
    public Pbn getPbnWj2025Simple() {
        return pbnWj2025Simple;
    }
    public Pbn getPbnWj2025() {
        return pbnWj2025;
    }
    public Pbn getPbnLCStandard() {
        return pbnLCStandard;
    }
    public Pbn getPbn() {
        return pbn;
    }


}

