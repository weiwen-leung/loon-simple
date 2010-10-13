package org.loon.framework.android.game.action.avg;

import org.loon.framework.android.game.action.avg.command.Command;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.LMessage;
import org.loon.framework.android.game.core.graphics.window.LSelect;

/**
 * Copyright 2008 - 2010
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1.1
 */
public class AVGScript extends AVGScreen {

	/**
	 * 使用指定脚本，指定路径中图片为对话框
	 * 
	 * @param initscript
	 * @param initdialog
	 */
	public AVGScript(String initscript, String initdialog) {
		super(initscript, initdialog);
	}

	/**
	 * 使用指定脚本，指定对话框图片
	 * 
	 * @param initscript
	 * @param img
	 */
	public AVGScript(String initscript, LImage img) {
		super(initscript, img);
	}

	/**
	 * 使用指定脚本，默认对话框
	 * 
	 * @param initscript
	 */
	public AVGScript(String initscript) {
		super(initscript);
	}

	/**
	 * AVG底层绘图接口，允许直接在此绘制您所需要的画面(即使不使用底层绘图， 您也可以通过add方式增加组件或精灵到指定位置)
	 */
	public void drawScreen(LGraphics g) {
	}

	/**
	 * 仅在初始化时起作用，设定脚本命令参数，可于此处预设游戏中变量
	 */
	public void initCommandConfig(Command command) {

	}

	/**
	 * 仅在初始化时起作用，设定信息框参数，不填此项时，对应组件默认置于屏幕下方，自适应信息框图片
	 */
	public void initMessageConfig(LMessage message) {

	}

	/**
	 * 仅在初始化时起作用，设定选择框参数，不填此项时，对应组件默认置于屏幕下方，自适应选择框图片
	 */
	public void initSelectConfig(LSelect select) {

	}

	/**
	 * 当执行AVG脚本时触发此项，message中数据为脚本信息，若返回false， 则当前脚本将被此函数截取（此时默认解释器无效），用户可以自行解析
	 * 该行脚本数据。
	 */
	public boolean nextScript(String message) {
		return true;
	}

	/**
	 * 当画面中出现选择项，并且选中时触发此函数，message中数据为[select]命令信息，type为选中的选择项，索引由0开始
	 */
	public void onSelect(String message, int type) {
	}

	/**
	 * 当脚本执行[exit]命令时将调用此参数，可在此执行setScreen之类命令离开AVG窗体
	 */
	public void onExit() {
	}

}
