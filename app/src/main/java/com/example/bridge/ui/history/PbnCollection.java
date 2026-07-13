package com.example.bridge.ui.history;

import com.example.bridge.ui.game.GameActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
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
        pbn.initNewGame();
        pbnNatC.initNewGame();
        //pbnNoSystem.initNewGame();
        //pbnWj2025Simple.initNewGame();
        //pbnWj2025.initNewGame();
        //pbnLCStandard.initNewGame();

        gameActivity.getGameController().calculateAndSetTheBestContract();
        pbn.setContract(gameActivity.getGameController().getCurrentContract(), "South");


        pbnNatC.todoBiding();
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

    public String generateJsonExport() {
        try {
            JSONArray jsonArray = new JSONArray();
            List<Pbn> allPbns = new ArrayList<>();
            allPbns.add(pbn);
            allPbns.add(pbnNatC);
            allPbns.add(pbnNoSystem);
            allPbns.add(pbnWj2025Simple);
            allPbns.add(pbnWj2025);
            allPbns.add(pbnLCStandard);

            for (Pbn p : allPbns) {
                if (p != null) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("system", p.getBoard()); 
                    jsonObj.put("data", p.toJsonObject()); // Teraz przekazujemy obiekt, nie String
                    jsonArray.put(jsonObj);
                }
            }
            return jsonArray.toString(4);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}

