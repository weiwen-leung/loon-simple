package org.loon.framework.game.simple;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferStrategy;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;

import org.loon.framework.game.simple.core.IHandler;
import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.IScreen;
import org.loon.framework.game.simple.core.timer.LTimerContext;
import org.loon.framework.game.simple.core.timer.SystemTimer;
import org.loon.framework.game.simple.utils.GraphicsUtils;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1.2
 */
public class GameView extends GameCanvas implements IGameView, Runnable,
		InputMethodListener, InputMethodRequests {

	private static final long serialVersionUID = 1982278682597393958L;

	final static private Toolkit systemToolKit = GraphicsUtils.toolKit;

	private final static long MAX_INTERVAL = 1000L;

	private boolean start, isFPS;

	private String actionName;

	private AttributedString composedTextString;

	private AttributedCharacterIterator composedText;

	private TextHitInfo caret;

	final static private Attribute[] IM_ATTRIBUTES = { TextAttribute.INPUT_METHOD_HIGHLIGHT };

	final static private AttributedCharacterIterator EMPTY_TEXT = (new AttributedString(
			"")).getIterator();

	final static private Font fpsFont = GraphicsUtils.getFont("Dialog", 0, 20);

	final static private int fpsX = 5;

	final static private int fpsY = 20;

	private transient boolean running = true, showLogo = true;

	private transient int shake_tmp, num_tmp;

	private transient long maxFrames, before, curTime, startTime, offsetTime,
			curFPS, calcInterval;

	private transient double frameCount;

	private Dimension dimension;

	private IHandler handler;

	private Graphics2D canvasGraphics = null;

	private IScreen screen;

	private BufferStrategy bufferStrategy;

	private Thread mainLoop;

	private GameContext context;

	public GameView(IHandler handler) {
		format(handler);
	}

	/**
	 * 创建GameView初始设置
	 * 
	 * @param handler
	 */
	public void format(IHandler handler) {
		this.handler = handler;
		this.context = GameManager.getInstance().registerApp(this);
		this.setFPS(LSystem.DEFAULT_MAX_FPS);
		this.setBackground(Color.BLACK);
		this.dimension = new Dimension(handler.getWidth(), handler.getHeight());
		this.setSize(dimension);
		this.setIgnoreRepaint(true);
		this.addFocusListener(handler);
		this.addKeyListener(handler);
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);
		this.setIgnoreRepaint(true);
		this.enableInputMethods(true);
		this.addInputMethodListener(this);

	}

	/**
	 * GameView内部用计时器
	 * 
	 * @return
	 */
	private long innerClock() {
		long now = System.currentTimeMillis();
		long ret = now - before;
		before = now;
		return ret;
	}

	/**
	 * 创建初始的Graphics
	 */
	public boolean createBufferGraphics() {
		boolean result;
		int num = 0;
		do {
			result = true;
			try {
				this.createBufferStrategy(2);
			} catch (Exception e) {
				result = false;
				try {
					Thread.sleep(100);
				} catch (InterruptedException excp) {
				}
			}
			if (num++ > 3) {
				break;
			}
		} while (!result);
		if (!result) {
			throw new RuntimeException(
					"Create BufferStrategy is not available!");
		}
		while (this.bufferStrategy == null) {
			try {
				this.bufferStrategy = getBufferStrategy();
				this.canvasGraphics = (Graphics2D) bufferStrategy
						.getDrawGraphics();
			} catch (Exception e) {
			}
		}
		return true;
	}

	/**
	 * 销毁图形资源
	 * 
	 */
	public void destroy() {
		if (canvasGraphics != null) {
			canvasGraphics.dispose();
			canvasGraphics = null;
		}
		if (bufferStrategy != null) {
			bufferStrategy = null;
		}
		handler.dispose();
		GameManager.getInstance().unregisterApp(this);
		context = null;
		LSystem.destroy();
		LSystem.gc();
	}

	/**
	 * 清屏
	 * 
	 * @param g
	 */
	private void clearDraw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	/**
	 * 更新图形资源
	 * 
	 */
	private void update() {
		bufferStrategy.show();
		systemToolKit.sync();
		canvasGraphics.dispose();
		canvasGraphics = null;
	}

	/**
	 * GameView内部线程
	 * 
	 */
	public void run() {
		if (showLogo) {
			showLogo();
		}
		final LTimerContext timerContext = new LTimerContext();
		final SystemTimer timer = context.getTimer();
		timerContext.setTimeMillis(startTime = timer.getTimeMillis());
		do {
			if (!start) {
				Thread.yield();
				continue;
			}
			screen = handler.getScreen();
			do {
				canvasGraphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				if (!screen.next()) {
					this.update();
					continue;
				}
				shake_tmp = screen.getShakeNumber();
				if (shake_tmp > 0) {
					canvasGraphics.drawImage(screen.getBackground(), shake_tmp
							/ 2 - LSystem.random.nextInt(shake_tmp), shake_tmp
							/ 2 - LSystem.random.nextInt(shake_tmp), null);
				} else if (shake_tmp == -1) {
					canvasGraphics
							.drawImage(screen.getBackground(), 0, 0, null);
				}
				curTime = timer.getTimeMillis();
				timerContext.setTimeSinceLastUpdate(curTime
						- timerContext.getTimeMillis());
				timerContext.setSleepTimeMillis((offsetTime - timerContext
						.getTimeSinceLastUpdate())
						- timerContext.getOverSleepTimeMillis());
				if (timerContext.getSleepTimeMillis() > 0) {
					try {
						Thread.sleep(timerContext.getSleepTimeMillis());
					} catch (InterruptedException e) {
					}
					timerContext
							.setOverSleepTimeMillis((timer.getTimeMillis() - curTime)
									- timerContext.getSleepTimeMillis());
				} else {
					timerContext.setOverSleepTimeMillis(0L);
				}
				timerContext.setTimeMillis(timer.getTimeMillis());
				screen.runTimer(timerContext);
				screen.createUI(canvasGraphics);
				if (isFPS) {
					tickFrames();
					canvasGraphics.setFont(fpsFont);
					canvasGraphics.setColor(Color.white);
					GraphicsUtils.setAntialias(canvasGraphics, true);
					canvasGraphics.drawString("FPS:" + curFPS, fpsX, fpsY);
					GraphicsUtils.setAntialias(canvasGraphics, false);
				}
				this.update();
			} while (bufferStrategy.contentsLost());
			if (isFocusOwner()) {
				Thread.yield();
				continue;
			}
			GraphicsUtils.wait(30);
			LSystem.gc(1000, 1);
		} while (running);
		this.destroy();
	}

	/**
	 * 生成FPS数值
	 * 
	 */
	private void tickFrames() {
		frameCount++;
		calcInterval += offsetTime;
		if (calcInterval >= MAX_INTERVAL) {
			long timeNow = System.currentTimeMillis();
			long realElapsedTime = timeNow - startTime;
			curFPS = (long) ((frameCount / realElapsedTime) * MAX_INTERVAL);
			frameCount = 0L;
			calcInterval = 0L;
			startTime = timeNow;
		}
	}

	/**
	 * 返回BufferStrategy
	 */
	public BufferStrategy getCurrentBufferStrategy() {
		return bufferStrategy;
	}

	/**
	 * 框架Logo
	 * 
	 */
	public void showLogo() {
		try {
			Image logo = null;
			long elapsedTime;
			int cx = 0, cy = 0;
			double delay;
			try {
				logo = GraphicsUtils.loadImage("system/image/logo.png");
				cx = this.getWidth() / 2 - logo.getWidth(null) / 2;
				cy = this.getHeight() / 2 - logo.getHeight(null) / 2;
			} catch (Exception e) {
			}
			do {
				float alpha = 0.0f;
				boolean firstTime = true;
				elapsedTime = innerClock();
				while (alpha < 1.0f) {
					clearDraw(canvasGraphics);
					Composite old = canvasGraphics.getComposite();
					canvasGraphics.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, alpha));
					canvasGraphics.drawImage(logo, cx, cy, null);
					canvasGraphics.setComposite(old);
					if (firstTime) {
						firstTime = false;
					}
					elapsedTime = innerClock();
					delay = 0.00065 * elapsedTime;
					if (delay > 0.22) {
						delay = 0.22 + (delay / 6);
					}
					alpha += delay;
					bufferStrategy.show();
					systemToolKit.sync();
				}
				while (num_tmp < 3000) {
					num_tmp += innerClock();
					bufferStrategy.show();
					systemToolKit.sync();
				}
				alpha = 1.0f;
				while (alpha > 0.0f) {
					clearDraw(canvasGraphics);
					Composite old = canvasGraphics.getComposite();
					canvasGraphics.setComposite(AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, alpha));
					canvasGraphics.drawImage(logo, cx, cy, null);
					canvasGraphics.setComposite(old);
					elapsedTime = innerClock();
					delay = 0.00055 * elapsedTime;
					if (delay > 0.15) {
						delay = 0.15 + ((delay - 0.04) / 2);
					}
					alpha -= delay;
					bufferStrategy.show();
					systemToolKit.sync();
				}
				canvasGraphics.dispose();
				showLogo = false;
			} while (bufferStrategy.contentsLost());
		} catch (Throwable e) {
		}
	}

	protected void processEvent(AWTEvent e) {
		super.processEvent(e);
		if (e instanceof MouseEvent) {
			if (!isFocusOwner()) {
				requestFocus();
			}
		} else if (e instanceof KeyEvent) {
			if (!isFocusOwner()) {
				requestFocus();
			}
		}
	}

	public String getActionCommand() {
		return actionName;
	}

	public void setActionCommand(String name) {
		this.actionName = name;
	}

	public Thread getMainLoop() {
		return mainLoop;
	}

	public void mainLoop() {
		mainLoop = context.createThread(this);
		context.setAnimationThread(mainLoop);
		mainLoop.start();
	}

	public void mainStop() {
		this.mainLoop = null;
	}

	public void startPaint() {
		this.start = true;
	}

	public void endPaint() {
		this.start = false;
	}

	public void setFPS(long frames) {
		this.maxFrames = frames;
		this.offsetTime = (long) (1.0 / maxFrames * MAX_INTERVAL);
	}

	public long getMaxFPS() {
		return this.maxFrames;
	}

	public long getCurrentFPS() {
		return this.curFPS;
	}

	public void setShowFPS(boolean isFPS) {
		this.isFPS = isFPS;
	}

	public boolean isShowLogo() {
		return showLogo;
	}

	public void setShowLogo(boolean showLogo) {
		this.showLogo = showLogo;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * 返回获得的输入法请求
	 */
	public InputMethodRequests getInputMethodRequests() {
		return this;
	}

	/**
	 * 返回当前用户输入的文本数据
	 */
	public AttributedCharacterIterator getDisplayText() {
		if (composedText == null) {
			return super.getDisplayText();
		} else {
			return EMPTY_TEXT;
		}
	}

	/**
	 * 返回当前插入符所在位置
	 */
	public TextHitInfo getCaret() {
		if (composedText == null) {
			return super.getCaret();
		} else if (caret == null) {
			return null;
		} else {
			return caret.getOffsetHit(getCommittedTextLength());
		}
	}

	/**
	 * 触发通过输入法引起的文本输入
	 */
	public void inputMethodTextChanged(InputMethodEvent e) {
		int committedCharacterCount = e.getCommittedCharacterCount();
		AttributedCharacterIterator text = e.getText();
		composedText = null;
		char c;
		if (text != null) {
			int toCopy = committedCharacterCount;
			c = text.first();
			while (toCopy-- > 0) {
				insertCharacter(c);
				c = text.next();
			}
			handler.changeText(getCommittedString());
			if (text.getEndIndex()
					- (text.getBeginIndex() + committedCharacterCount) > 0) {
				composedTextString = new AttributedString(text, text
						.getBeginIndex()
						+ committedCharacterCount, text.getEndIndex(),
						IM_ATTRIBUTES);
				composedTextString.addAttribute(TextAttribute.FONT, getFont());
				composedText = composedTextString.getIterator();
			}
		}
		e.consume();
		invalidateTextLayout();
		caret = e.getCaret();
		clear();
	}

	/**
	 * 获得插入点修改后的位置
	 */
	public void caretPositionChanged(InputMethodEvent e) {
		caret = e.getCaret();
		e.consume();
	}

	/**
	 * 获得输入文本定位
	 */
	public Rectangle getTextLocation(TextHitInfo offset) {
		Rectangle rectangle;
		if (offset == null) {
			rectangle = getCaretRectangle();
		} else {
			TextHitInfo globalOffset = offset
					.getOffsetHit(getCommittedTextLength());
			rectangle = getCaretRectangle(globalOffset);
		}
		Point location = getLocationOnScreen();
		rectangle.translate(location.x, location.y);
		return rectangle;
	}

	/**
	 * 获得指定x,y的偏移坐标
	 */
	public TextHitInfo getLocationOffset(int x, int y) {
		Point location = getLocationOnScreen();
		Point textOrigin = getTextOrigin();
		x -= location.x + textOrigin.x;
		y -= location.y + textOrigin.y;
		TextLayout textLayout = getTextLayout();
		if (textLayout != null && textLayout.getBounds().contains(x, y)) {
			return textLayout.hitTestChar(x, y).getOffsetHit(
					-getCommittedTextLength());
		} else {
			return null;
		}
	}

	public int getInsertPositionOffset() {
		return getCommittedTextLength();
	}

	public AttributedCharacterIterator getCommittedText(int beginIndex,
			int endIndex, Attribute[] attributes) {
		return getCommittedText(beginIndex, endIndex);
	}

	public AttributedCharacterIterator cancelLatestCommittedText(
			Attribute[] attributes) {
		return null;
	}

	public AttributedCharacterIterator getSelectedText(Attribute[] attributes) {
		return EMPTY_TEXT;
	}

	public IHandler getHandler() {
		return handler;
	}

}
