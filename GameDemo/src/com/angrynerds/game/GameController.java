package com.angrynerds.game;

import com.angrynerds.game.screens.MainMenuScreen;
import com.angrynerds.game.screens.PlayScreen;
import com.angrynerds.ui.ControllUI;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 22:10
 * Project: GameDemo
 */
public class GameController {

    // Screens

    public PlayScreen playScreen;
    private MainMenuScreen mainMenuScreen;

    public GameController() {
        init();
    }

    private void init() {
        playScreen = new PlayScreen(this);
    }

    public void update(float deltaTime) {
        playScreen.update(deltaTime);

    }
}
