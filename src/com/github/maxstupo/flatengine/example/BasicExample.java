package com.github.maxstupo.flatengine.example;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.AbstractGameloop;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class BasicExample {

    public static void main(String[] args) {

        JFlatLog logger = JFlatLog.get(); // Log to console mode.
        logger.setLogLevel(JFlatLog.LEVEL_FINE); // Display ALL log messages.

        AbstractGameloop gameloop = new BasicGameloop(60); // Create a gameloop that runs at 60 FPS & 60 UPS.
        FlatEngine engine = new FlatEngine(gameloop, logger); // Create the game engine.

        // Register each gamestate.
        engine.registerScreen(new GamestateMainmenu(engine, "mainmenu"));
        engine.registerScreen(new GamestateOptions(engine, "options"));
        engine.registerScreen(new GamestateCredits(engine, "credits"));
        engine.registerScreen(new GamestateIngame(engine, "ingame"));

        engine.switchTo("mainmenu"); // Start on the main menu.

        // Create the window for the game engine.
        engine.createWindow("Basic example of FlatEngine", 600, 400, true, FlatEngine.EXIT_ON_CLOSE);

        engine.start(); // Start the game loop
    }

}
