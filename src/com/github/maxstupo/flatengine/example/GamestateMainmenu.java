package com.github.maxstupo.flatengine.example;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.github.maxstupo.flatengine.Engine;
import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.Window;
import com.github.maxstupo.flatengine.example.BasicExample.State;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiNode;
import com.github.maxstupo.flatengine.gui.GuiText;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.states.AbstractGamestate;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GamestateMainmenu extends AbstractGamestate<State> implements IEventListener<GuiButton<State>, String, Integer> {

    private static final Vector2i BUTTON_SIZE = new Vector2i(100, 25);
    private static final int BUTTON_SPACING = 3;

    private final GuiNode<State> btnGroup = new GuiNode<>(this);

    private final GuiButton<State> btnPlay;
    private final GuiButton<State> btnOptions;
    private final GuiButton<State> btnCredits;
    private final GuiButton<State> btnExit;

    private final GuiText<State> title;

    public GamestateMainmenu(Engine<State> engine, State key) {
        super(engine, key);

        // Buttons
        btnPlay = new GuiButton<>(this, "Play", new Vector2i(0, 0), BUTTON_SIZE);
        btnOptions = new GuiButton<>(this, "Options", new Vector2i(0, BUTTON_SIZE.y + BUTTON_SPACING), BUTTON_SIZE);
        btnCredits = new GuiButton<>(this, "Credits", new Vector2i(0, (BUTTON_SIZE.y + BUTTON_SPACING) * 2), BUTTON_SIZE);
        btnExit = new GuiButton<>(this, "Exit", new Vector2i(0, (BUTTON_SIZE.y + BUTTON_SPACING) * 3), BUTTON_SIZE);

        btnPlay.addListener(this);
        btnOptions.addListener(this);
        btnCredits.addListener(this);
        btnExit.addListener(this);

        // Group buttons so we can move them all at once.
        btnGroup.addChild(btnPlay).addChild(btnOptions).addChild(btnCredits).addChild(btnExit);

        gui.addChild(btnGroup);

        // A title.
        title = new GuiText<>(this, new Vector2i(0, 0), "The mainmenu");
        title.setFont(new Font("", Font.BOLD, 25));
        gui.addChild(title);
    }

    @Override
    public void onEvent(GuiButton<State> executor, String actionItem, Integer action) {
        if (executor.equals(btnPlay)) {
            gsm.switchTo(State.INGAME);
        } else if (executor.equals(btnOptions)) {
            gsm.switchTo(State.OPTIONS);
        } else if (executor.equals(btnCredits)) {
            gsm.switchTo(State.CREDITS);
        } else if (executor.equals(btnExit)) {
            System.exit(0);
        }
    }

    @Override
    public void update(double delta) {
        // If the space bar is pressed.
        if (gsm.getEngine().getKeyboard().isKeyHeld(Keyboard.KEY_SPACE)) {
            title.setText("Spacebar held!");
        } else {
            title.setText("The mainmenu");
        }

        // If left mouse button is pressed.
        if (gsm.getEngine().getMouse().isMouseDown(Mouse.LEFT_CLICK)) {

            // Set mouse position in window title.
            Window.get().setTitle(gsm.getEngine().getMouse().getPosition().x + "," + gsm.getEngine().getMouse().getPosition().y);

        }
    }

    @Override
    public void render(Graphics2D g) {

    }

    @Override
    public void onActivated() {
        super.onActivated();
        onResize(gsm.getEngine().getWidth(), gsm.getEngine().getHeight());
    }

    @Override
    public void onResize(int width, int height) {
        super.onResize(width, height);

        // Recentre the title
        title.setLocalPosition(width / 2 - title.getBounds().width / 2, 0);

        // Recentre the buttons.
        Rectangle bounds = btnGroup.getBoundsTotal();
        btnGroup.setLocalPosition(width / 2 - bounds.width / 2, height / 2 - bounds.height / 2);
    }

}
