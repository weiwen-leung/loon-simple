package org.loon.framework.android.game;

import java.util.LinkedList;

import org.loon.framework.android.game.action.map.shapes.RectBox;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.geom.Dimension;
import org.loon.framework.android.game.media.sound.AssetsSoundManager;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public interface IHandler {

	/**
	 * 返回AssetsSoundManager
	 * 
	 * @return
	 */
	public AssetsSoundManager getAssetsSound();

	public Screen getCurrentScreen();

	public void runFirstScreen();

	public void runLastScreen();

	public void runIndexScreen(int index);

	public void runPreviousScreen();

	public void runNextScreen();

	public void addScreen(Screen screen);

	public LinkedList<Screen> getScreens();

	public int getScreenCount();

	public void initScreen();

	public boolean isScale();

	public Dimension getScreenDimension();

	public RectBox getScreenBox();

	public int getWidth();

	public int getHeight();

	public int getMaxWidth();

	public int getMaxHeight();

	public View getView();

	public Context getContext();

	public Window getWindow();

	public WindowManager getWindowManager();

	public void destroy();

}
