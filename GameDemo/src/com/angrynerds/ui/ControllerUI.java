package com.angrynerds.ui;

import com.angrynerds.game.screens.play.PlayScreen;
import com.angrynerds.input.IGameInputController;
import com.angrynerds.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;

/**
 * User: Franjo
 * Date: 07.11.13
 * Time: 16:05
 * Project: GameDemo
 */
public class ControllerUI implements IGameInputController {

    private ArrayList<MyButton> touchOrder;
    private MyButtonListener listener;
    private Touchpad touchpad;
    private MyButton midButton;
    private MyButton topButton;
    private MyButton downButton;
    private MyButton leftButton;
    private MyButton rightButton;
    private MyButton[] buttons = new MyButton[5];
    private Boolean isJumping =false;

    private Stage stage;

    public ControllerUI() {
        init();
    }

    private void init() {
        //listener = new MyButtonListener();+


        // start touchpad init
        Skin skin = new Skin();
        skin.add("joystick_bg", new Texture("data/buttons/button_256/joystick_bg.png"));
        skin.add("joystick_knob", new Texture("data/buttons/button_256/joystick_knob.png"));
        skin.add("mid_up", new Texture("data/buttons/button_256/button_norm.png"));
        skin.add("mid_down", new Texture("data/buttons/button_256/button_pushed.png"));
        skin.add("top_up", new Texture("data/buttons/button_256/button_oben_norm.png"));
        skin.add("top_down", new Texture("data/buttons/button_256/button_oben_pushed.png"));

        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        Drawable joystickBG = skin.getDrawable("joystick_bg");
        Drawable joystickKnob = skin.getDrawable("joystick_knob");
        style.background = joystickBG;
        style.knob = joystickKnob;

        touchpad = new Touchpad(10, style);
        touchpad.setBounds(15, 100, 100, 100);
//		touchpad.addListener(listener);
        //end touchpad init


        //start button init
        listener = new MyButtonListener();

        midButton = new MyButton(skin.getDrawable("mid_up"), skin.getDrawable("mid_down"));
//        midButton.setPosition(Constants.VIEWPORT_WIDTH - 30, midButton.getHeight() + 15);
        midButton.setSize(75,75);
        midButton.setPosition(Constants.VIEWPORT_WIDTH - midButton.getWidth(), midButton.getHeight());

//        midButton.setBounds(Constants.VIEWPORT_WIDTH - midButton.getWidth() * midButton.getScaleX(), midButton.getHeight(), 75, 75);
        midButton.addListener(listener);

        topButton = new MyButton(skin.getDrawable("top_up"), skin.getDrawable("top_down"));
        topButton.setBounds(250, 200, 100, 100);
        topButton.addListener(listener);
        // end button init

        buttons[0] = midButton;
        buttons[1] = topButton;

        touchOrder = new ArrayList<MyButton>();

        stage = new Stage(800,480,true,PlayScreen.getBatch());

        Gdx.input.setInputProcessor(stage);
//        stage = new Stage(800,480,true,PlayScreen.getBatch());
        stage.addActor(touchpad);
        stage.addActor(midButton);
        stage.addActor(topButton);
    }

    @Override
    public float get_stickX() {
        return touchpad.getKnobPercentX();
    }

    @Override
    public float get_stickY() {
        return touchpad.getKnobPercentY();
    }

    @Override
    public boolean get_isA() {
        if(isJumping) {
            isJumping = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean get_isB() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


    class MyButtonListener extends DragListener {


        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer,
                            int button) {
            System.out.println("touchup");
            for (MyButton but : buttons) {
                if (but != null)
                    but.setOver(false);
                MyButton.overCounter = 0;
            }
            if (touchOrder.size() == 2) {
                if (touchOrder.get(0).equals(midButton)) {
                    if (touchOrder.get(1).equals(topButton)) {
                        System.out.println("jumpjump");
                        isJumping = true;
                    }
                }
            }
            touchOrder.clear();
            super.touchUp(event, x, y, pointer, button);
        }


        @Override
        public void enter(InputEvent event, float x, float y, int pointer,
                          Actor fromActor) {
            MyButton b;
            b = (MyButton) event.getTarget();
            if (!b.getOver()) {
                b.setOver(true);
                touchOrder.add(b);
            }

            super.enter(event, x, y, pointer, fromActor);
        }
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

    public Button getMidButton() {
        return midButton;
    }

    public Button getTopButton() {
        return topButton;
    }

    public Button getDownButton() {
        return downButton;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public Button getRightButton() {
        return rightButton;
    }
}
