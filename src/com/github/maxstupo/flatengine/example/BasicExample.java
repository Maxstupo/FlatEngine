package com.github.maxstupo.flatengine.example;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.Window;
import com.github.maxstupo.flatengine.gameloop.AbstractGameloop;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class BasicExample {

    public static enum State {
        MAINMENU,
        OPTIONS,
        CREDITS,
        INGAME
    }

    public static void main(String[] args) {

        JFlatLog logger = JFlatLog.get(); // Log to console mode.
        logger.setLogLevel(JFlatLog.LEVEL_FINE); // Display ALL log messages.

        AbstractGameloop gameloop = new BasicGameloop(60); // Create a gameloop that runs at 60 FPS & 60 UPS.
        Engine<State> engine = new Engine<>(gameloop, logger); // Create the game engine.

        // Register each gamestate.
        engine.registerState(new GamestateMainmenu(engine, State.MAINMENU));
        engine.registerState(new GamestateOptions(engine, State.OPTIONS));
        engine.registerState(new GamestateCredits(engine, State.CREDITS));
        engine.registerState(new GamestateIngame(engine, State.INGAME));

        engine.switchTo(State.MAINMENU); // Start on the main menu.

        // Create the window for the game engine.
        Window.get().create("Basic example of FlatEngine", 600, 400, true, Window.EXIT_ON_CLOSE, engine);

        engine.start(); // Start the game loop
    }

}
