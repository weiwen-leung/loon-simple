package org.loon.framework.javase.game.action.avg.command.lua;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.loon.framework.javase.game.core.LSystem;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class LUA_LIB {

	final static void loadLibrary(final String libName) {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				Class resource = LUA_LIB.class;
				URL LibURL = resource.getResource(libName);
				String libPath = (LibURL != null ? LibURL.getPath() : null);
				if (libPath != null) {
					// dll在包内无法直接读取
					if (libPath.startsWith("file:")) {
						libPath = (System.getProperty("user.dir") + LSystem.FS + libName)
								.intern();
						try {
							File file = new File(libPath);
							if (!file.exists()) {
								file.createNewFile();
								InputStream in = resource
										.getResourceAsStream(libName);
								OutputStream out = new FileOutputStream(file);
								byte buffer[] = new byte[1024 * 10];
								int charsRead;
								while ((charsRead = in.read(buffer)) != -1) {
									out.write(buffer, 0, charsRead);
									out.flush();
								}
								out.flush();
								in.close();
								out.close();
							}
						} catch (Exception ex) {
						}
						System.load(libPath);
					} else {
						System.load(libPath);
					}
				} else {
					System.loadLibrary(libName.replaceAll(".dll", ""));
				}
				return null;
			}
		});
	}

}
