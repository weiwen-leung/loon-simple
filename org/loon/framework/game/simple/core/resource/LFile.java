package org.loon.framework.game.simple.core.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.loon.framework.game.simple.core.LSystem;

/**
 * 
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
public class LFile extends LDirectory {

	private static final long serialVersionUID = -536428100013291223L;

	private long lastModified;

	private long size;

	public static final int EOF = -1;

	public LFile() {
		super("");
	}

	public LFile(String filePath) {
		super(filePath);
		lastModified = lastModified();
		size = length();
	}

	public LFile(File file) {
		this(file.getPath());
		lastModified = lastModified();
		size = length();
	}

	public LFile(String path, String name) {
		super(path, name);
		lastModified = lastModified();
		size = length();
	}

	public LFile(File dir, String name) {
		super(dir, name);
		lastModified = lastModified();
		size = length();
	}

	public static boolean mkdirs(String path) {
		File dir = new File(path);
		if (!dir.exists()){
			return dir.mkdirs();
		}
		else{
			return false;
		}
	}

	public static String getName(String name, String ext) {
		return name + "." + ext;
	}

	public static String getNoExtensionName(String name) {
		if (name.indexOf(".") == -1)
			return name;
		else
			return name.substring(0, name.lastIndexOf(getExtension(name)) - 1);
	}

	public static String getExtension(String name) {
		int index = name.lastIndexOf(".");
		if (index == -1){
			return "";
		}
		else{
			return name.substring(index + 1);
		}
	}

	public LFile[] listSortFiles() {
		File files[] = listFiles();
		if (files == null){
			return null;
		}
		List fileList = new ArrayList(10);
		List dirList = new ArrayList(10);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				dirList.add(new LFile(file));
			} else {
				fileList.add(new LFile(file));
			}
		}

		dirList.addAll(fileList);
		return (LFile[]) dirList.toArray(new LFile[dirList.size()]);
	}

	public String getKB() {
		long nsize = size / 1000L;
		if (nsize == 0L){
			nsize = 1L;
		}
		return String.valueOf(size);
	}

	public String getWindowsName() {
		String name = getName();
		int index = name.lastIndexOf("\\");
		if (index == -1){
			return getNoExtensionName(name);
		}
		else{
			return getNoExtensionName(name.substring(index + 1));
		}
	}

	public boolean isModified() {
		File file = new File(getPath());
		long thisTime = file.lastModified();
		if (thisTime > lastModified) {
			lastModified = thisTime;
			return true;
		} else {
			return false;
		}
	}

	public long getTimeStamp() {
		return lastModified;
	}

	public String getNoExtensionName() {
		return getNoExtensionName(getName());
	}

	public String getExtension() {
		return getExtension(getName());
	}

	public boolean renameTo(String name) {
		return super.renameTo(new File(getParentFile(), name));
	}

	public LFile copy(String path) throws IOException {
		return copy(path, getName());
	}

	public LFile copy(String path, String name) throws IOException {
		return copy(((File) (new LFile(path, name))));
	}

	public LFile copy(File path, String name) throws IOException {
		return copy(((File) (new LFile(path, name))));
	}

	public LFile copy(File store) throws IOException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(this));
			outputFile(is, store);
		} finally {
			if (is != null){
				is.close();
			}
		}
		return new LFile(store);
	}

	public LFile copyFile(File store) throws IOException {
		String encode = LSystem.encoding;
		try {
			FileInputStream ins = new FileInputStream(this);
			FileOutputStream outs = new FileOutputStream(store);
			InputStreamReader in = new InputStreamReader(ins, encode);
			OutputStreamWriter out = new OutputStreamWriter(outs, encode);
			int contents;
			while ((contents = in.read()) != -1){
				out.write(contents);
			}
			in.close();
			out.close();
		} catch (IOException ioexception) {
		}
		return new LFile(store);
	}

	public LFile move(String path) throws IOException {
		return move(path, getName());
	}

	public LFile move(String path, String fileName) throws IOException {
		return move(((File) (new LFile(path, fileName))));
	}

	public LFile move(File path, String fileName) throws IOException {
		return move(((File) (new LFile(path, fileName))));
	}

	public LFile move(File file) throws IOException {
		LFile store = copy(file);
		delete();
		return store;
	}

	public LFile moveWindows(File path, String fileName) throws IOException {
		return moveWindows(((File) (new LFile(path, fileName))));
	}

	public LFile moveWindows(File file) throws IOException {
		LFile store = copyFile(file);
		delete();
		return store;
	}

	public boolean deleteAll() {
		if (isFile()) {
			return delete();
		} else {
			return deleteAll((File) this);
		}
	}

	private boolean deleteAll(File dir) {
		String fileNames[] = dir.list();
		if (fileNames == null)
			return false;
		for (int i = 0; i < fileNames.length; i++) {
			File file = new File(dir, fileNames[i]);
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				deleteAll(file);
			}
		}
		return dir.delete();
	}

	public static void outputFile(InputStream stream, File outputFile)
			throws IOException {
		DataOutputStream unZipileData = null;
		BufferedInputStream bis = null;
		try {
			unZipileData = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(outputFile)));
			bis = new BufferedInputStream(stream);
			byte buf[] = new byte[1024];
			int c;
			while ((c = bis.read(buf, 0, 1024)) != -1){
				unZipileData.write(buf, 0, c);
			}
		} finally {
			try {
				if (unZipileData != null){
					unZipileData.close();
				}
			} catch (IOException ioexception) {
			}
			try {
				if (bis != null)
					bis.close();
			} catch (IOException ioexception1) {
			}
		}
		return;
	}

}
