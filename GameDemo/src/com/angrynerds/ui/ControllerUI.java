package com.angrynerds.ui;

import com.angrynerds.game.screens.play.PlayScreen;
import com.angrynerds.input.UIButtonListener;
import com.angrynerds.util.C;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * User: Franjo
 * Date: 07.11.13
 * Time: 16:05
 * Project: GameDemo
 */
public class ControllerUI  {

    private UIButtonListener listener;

    private Touchpad touchpad;
    private MyButton midButton;
    private MyButton topButton;
    private MyButton botButton;
    private MyButton leftButton;
    private MyButton rightButton;
    private MyButton[] buttons = new MyButton[5];

    private Stage stage;

    public ControllerUI() {
        init();
    }

    private void init() {
        // start touchpad init
        Skin skin = new Skin();
        skin.add("joystick_bg", new Texture("data/buttons/button_256/joystick_bg.png"));
        skin.add("joystick_knob", new Texture("data/buttons/button_256/joystick_knob.png"));
        skin.add("mid_up", new Texture("data/buttons/button_256/button_norm.png"));
        skin.add("mid_down", new Texture("data/buttons/button_256/button_pushed.png"));
        skin.add("top_up", new Texture("data/buttons/button_256/button_oben_norm.png"));
        skin.add("top_down", new Texture("data/buttons/button_256/button_oben_pushed.png"));
        skin.add("right_up", new Texture("data/buttons/button_256/button_rechts_norm.png"));
        skin.add("right_down", new Texture("data/buttons/button_256/button_rechts_pushed.png"));
        skin.add("left_up", new Texture("data/buttons/button_256/button_links_norm.png"));
        skin.add("left_down", new Texture("data/buttons/button_256/button_links_pushed.png"));
        skin.add("bottom_up", new Texture("data/buttons/button_256/button_unten_norm.png"));
        skin.add("bottom_down", new Texture("data/buttons/button_256/button_unten_pushed.png"));


        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        Drawable joystickBG = skin.getDrawable("joystick_bg");
        Drawable joystickKnob = skin.getDrawable("joystick_knob");
        style.background = joystickBG;
        style.knob = joystickKnob;

        touchpad = new Touchpad(10, style);
        touchpad.setBounds(15, 75, 120, 120);
        //end touchpad init


        //start button init


        midButton = new MyButton(0, skin.getDrawable("mid_up"), skin.getDrawable("mid_down"));
        midButton.setBounds(C.VIEWPORT_WIDTH - midButton.getWidth()/1.5f, midButton.getHeight()/3, 100, 100);

//        midButton.setBounds(C.VIEWPORT_WIDTH - midButton.getWidth() * midButton.getScaleX(), midButton.getHeight(), 75, 75);

        topButton = new MyButton(1, skin.getDrawable("top_up"), skin.getDrawable("top_down"));
        topButton.setBounds(midButton.getX(), midButton.getY()+midButton.getHeight()/1.25f, 100, 70);

        rightButton = new MyButton(2, skin.getDrawable("right_up"), skin.getDrawable("right_down"));
        rightButton.setBounds(midButton.getX()+midButton.getWidth()/1.25f, midButton.getY()-midButton.getHeight()/15, 70, 100);

        botButton = new MyButton(3, skin.getDrawable("bottom_up"), skin.getDrawable("bottom_down"));
        botButton.setBounds(midButton.getX(), midButton.getY()-midButton.getHeight()/1.8f, 100, 70);

        leftButton = new MyButton(4, skin.getDrawable("left_up"), skin.getDrawable("left_down"));
        leftButton.setBounds(midButton.getX()-midButton.getWidth()/1.75f, midButton.getY()-midButton.getHeight()/15, 70, 100);
         // end button init

        listener = new UIButtonListener(this);
        topButton.addListener(listener);
        midButton.addListener(listener);
        rightButton.addListener(listener);
        botButton.addListener(listener);
        leftButton.addListener(listener);


        //fills button[]
        buttons[0] = midButton;
        buttons[1] = topButton;
        buttons[2] = rightButton;
        buttons[3] = botButton;
        buttons[4] = leftButton;

        stage = new Stage(800,480,true,PlayScreen.getBatch());

        Gdx.input.setInputProcessor(stage);

        // add buttons and touchpad to the stage
        stage.addActor(touchpad);
        stage.addActor(topButton);
        stage.addActor(rightButton);
        stage.addActor(botButton);
        stage.addActor(leftButton);
        stage.addActor(midButton);
    }


    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    public void render() {
        stage.draw();
    }


    // getters
    public MyButton[] getButtons() {
        return buttons;
    }

    public Touchpad getTouchpad() {
        return touchpad;
    }

    public MyButton getMidButton() {
        return midButton;
    }

    public MyButton getTopButton() {
        return topButton;
    }

    public MyButton getBotButton() {
        return botButton;
    }

    public MyButton getLeftButton() {
        return leftButton;
    }

    public MyButton getRightButton() {
        return rightButton;
    }

    public UIButtonListener getListener() {
        return listener;
    }
}
