package org.loon.framework.android.game;

import org.loon.framework.android.game.action.map.shapes.RectBox;
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

	public void initScreen();

	public RectBox getScreenBox();

	public int getWidth();

	public int getHeight();

	public View getView();

	public Context getContext();

	public Window getWindow();

	public WindowManager getWindowManager();

	public void destroy();

}
