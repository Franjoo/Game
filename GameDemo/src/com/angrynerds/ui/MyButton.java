package com.angrynerds.ui;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MyButton extends Button{

	public static int overCounter = 0;
	private Boolean over = false;
	private HashMap<String, Integer> touchedOrder;
	
	
	public MyButton(Drawable up, Drawable down) {
		super(up, down);

//		addListener(new MyButtonListener());
	}
	
	public void setOver(Boolean b){
		this.over = b;
	}
	
	public Boolean getOver(){
		return over;
	}
	
	
	
	
//	class MyButtonListener extends InputListener{
//
//		@Override
//		public void enter(InputEvent event, float x, float y, int pointer,
//				Actor fromActor) {
//			if(!over){
//				over = true;
//				overCounter ++;
//				MyButton b = (MyButton) event.getTarget();
//				b.toggle();
//				System.out.println("overCounter =" + overCounter);
//				super.enter(event, x, y, pointer, fromActor);
//			}
//		}
//
//		@Override
//		public void touchUp(InputEvent event, float x, float y, int pointer,
//				int button) {
//			System.out.println("touchup");
//			overCounter = 0;
//			over = false;
//			super.touchUp(event, x, y, pointer, button);
//		}
//
//		@Override
//		public boolean touchDown(InputEvent event, float x, float y,
//				int pointer, int button) {
//			System.out.println("down");
//			return super.touchDown(event, x, y, pointer, button);
//		}
//		
//		
//		
//		
//	}

}
