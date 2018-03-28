package com.github.maxstupo.flatengine.hgui;

import java.awt.event.KeyEvent;

import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.input.IKeyListener;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * @author Maxstupo
 *
 */
public class GuiTextBox extends GuiContainer implements IKeyListener {

    public static interface ITypeFilter {

        boolean filterCharacter(char character);
    }

    public static final ITypeFilter DEFAULT_FILTER = new ITypeFilter() {

        @Override
        public boolean filterCharacter(char character) {
            return Character.isLetterOrDigit(character) || Character.isWhitespace(character);
        }
    };

    private final GuiLabel label;
    private final GuiLabel input;

    private boolean inputFocused = false;
    private int cursor = 0, cursorNext = 0;
    private int ticks = 0;
    private int cursorSpeed = 15;
    private char cursorChar = '|';

    private ITypeFilter typingFilter = DEFAULT_FILTER;
    private int characterLimit = -1;

    public GuiTextBox(AbstractScreen screen, String labelText, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        this.label = new GuiLabel(screen, 0, 0, -1, -1, labelText);
        this.input = new GuiLabel(screen, 0, 0, 1, 1, "Text");
        this.label.setOutlineColor(null).setBackgroundColor(null);
        setOutlineColor(null).setBackgroundColor(null);
        screen.getKeyboard().addListener(this);
        this.input.getTextNode().setAlignment(Alignment.MIDDLE_LEFT);
        this.input.setOutlineColor(null).setBackgroundColor(null);
        this.add(label);
        this.add(input);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {

        if (input.isMouseClicked(Mouse.LEFT_CLICK)) {
            if (!inputFocused)
                cursor = cursorNext = getText().length();
            else
                updateCursorAnimation(false);
            inputFocused = !inputFocused;

        } else if (!isMouseOver() && getMouse().isMouseDown(Mouse.LEFT_CLICK)) {
            if (inputFocused)
                updateCursorAnimation(false);
            inputFocused = false;

        }

        if (inputFocused) {
            ticks++;
            if (ticks > cursorSpeed) {
                ticks = 0;
                updateCursorAnimation(true);
            }
        }

        return super.update(delta, shouldHandleInput) && isMouseOver();
    }

    private void updateCursorAnimation(boolean allowAdd) {
        String cursorStr = getText().substring(Math.min(getText().length(), cursor), Math.min(getText().length(), cursor + 1));
        if (cursorStr.equalsIgnoreCase(Character.toString(cursorChar))) {
            setText(getText().substring(0, cursor) + getText().substring(cursor + 1, getText().length()));
            cursor = cursorNext;
        } else if (allowAdd) {
            setText(getText().substring(0, cursor) + Character.toString(cursorChar) + getText().substring(cursor, getText().length()));
        }
    }

    @Override
    protected void onChildNodeChange(AbstractNode instigator) {
        super.onChildNodeChange(instigator);

        if (instigator.equals(label) || instigator.equals(input)) {
            if (input.isAutoSizeWidth()) {
                setWidth(label.getWidth() + input.getWidth());
            } else {
                input.setWidth(getWidth() - label.getWidth());
            }

            if (input.isAutoSizeHeight()) {
                setHeight(label.getHeight());
            } else {
                input.setHeight(getHeight());
            }

            input.setLocalPositionX(label.getWidth());
            label.setLocalPositionY(getHeight() / 2 - label.getHeight() / 2);
        }
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        screen.getKeyboard().removeListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!inputFocused)
            return;

        if (e.getKeyCode() == Keyboard.KEY_LEFT) {
            moveCursorLeft();
        } else if (e.getKeyCode() == Keyboard.KEY_RIGHT) {
            moveCursorRight();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            backspace();
        } else {
            type(e.getKeyChar());
        }
    }

    public GuiTextBox type(char c) {
        if (typingFilter != null) {
            if (!typingFilter.filterCharacter(c))
                return this;
        }

        String cursorStr = getText().substring(Math.min(getText().length(), this.cursor), Math.min(getText().length(), this.cursor + 1));
        int offset = cursorStr.equalsIgnoreCase(Character.toString(cursorChar)) ? 1 : 0;
        if (characterLimit > 0 && getText().length() - offset >= characterLimit)
            return this;

        setText(getText().substring(0, cursor) + c + getText().substring(cursor, getText().length()));

        cursor++;
        cursorNext = cursor;
        return this;
    }

    public GuiTextBox backspace() {
        if (cursor > 0)
            setText(getText().substring(0, cursor - 1) + getText().substring(cursor, getText().length()));
        cursor--;
        if (cursor < 0)
            cursor = 0;
        cursorNext = cursor;
        return this;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public GuiTextBox moveCursorRight() {
        return setCursor(getCursor() + 1);
    }

    public GuiTextBox moveCursorLeft() {
        return setCursor(getCursor() - 1);
    }

    public GuiTextBox setCursor(int cursor) {
        this.cursorNext = cursor;
        if (this.cursorNext < 0)
            this.cursorNext = 0;

        String cursorStr = getText().substring(Math.min(getText().length(), this.cursor), Math.min(getText().length(), this.cursor + 1));
        int offset = cursorStr.equalsIgnoreCase(Character.toString(cursorChar)) ? 1 : 0;
        if (this.cursorNext >= getText().length() - offset)
            this.cursorNext = getText().length() - offset;
        return this;
    }

    public GuiTextBox setText(String text) {
        input.getTextNode().setText(text);
        setCursor(getCursor());
        return this;
    }

    public GuiTextBox setInputFocused() {
        return setInputFocused(true);
    }

    public GuiTextBox setInputFocused(boolean inputSelected) {
        this.inputFocused = inputSelected;
        return this;
    }

    public GuiTextBox setCursorSpeed(int cursorSpeed) {
        this.cursorSpeed = cursorSpeed;
        return this;
    }

    public void setCursorChar(char cursorChar) {
        this.cursorChar = cursorChar;
    }

    public int getCursor() {
        return cursorNext;
    }

    public String getText() {
        return input.getTextNode().getText();
    }

    public GuiLabel getLabel() {
        return label;
    }

    public GuiLabel getInput() {
        return input;
    }

    public boolean isInputFocused() {
        return inputFocused;
    }

    public int getCursorSpeed() {
        return cursorSpeed;
    }

    public char getCursorChar() {
        return cursorChar;
    }

    public ITypeFilter getTypingFilter() {
        return typingFilter;
    }

    public GuiTextBox setTypingFilter(ITypeFilter typingFilter) {
        this.typingFilter = typingFilter;
        return this;
    }

    public int getCharacterLimit() {
        return characterLimit;
    }

    public GuiTextBox setCharacterLimit(int characterLimit) {
        this.characterLimit = characterLimit;
        return this;
    }

}
