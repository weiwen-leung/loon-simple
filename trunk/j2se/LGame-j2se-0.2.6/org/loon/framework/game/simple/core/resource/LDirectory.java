package org.loon.framework.game.simple.core.resource;

import java.io.File;
import java.io.IOException;
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
public class LDirectory extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = 783797829281799240L;

	public LDirectory() {
		super("");
	}

	public LDirectory(String filePath) {
		super(filePath);
	}

	public LDirectory(File file) {
		this(file.getPath());
	}

	public LDirectory(String path, String name) {
		super(path, name);
	}

	public LDirectory(File dir, String name) {
		super(dir, name);
	}

	public static long getAllDirSizeK(String path) {
		return getAllDirSizeK(new File(path));
	}

	public static long getAllDirSizeK(File dir) {
		return getAllDirSizeK(dir, 0L);
	}

	public List getFileList() throws IOException {
		return LDirectory.getAllFiles(this);
	}

	public List getFileList(final String ext) throws IOException {
		return LDirectory.getAllFiles(this, ext);
	}

	public static List getAllFiles(final File path) throws IOException {
		List ret = new ArrayList(10);
		String[] listFile = path.list();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				File tempfile = new File(path + LSystem.FS + listFile[i]);
				if (tempfile.isDirectory()) {
					List arr = getAllFiles(tempfile);
					ret.addAll(arr);
					arr.clear();
					arr = null;
				} else {
					ret.add(tempfile.getAbsolutePath());

				}
			}
		}
		return ret;
	}

	public static List getAllFiles(final String path, final String ext)
			throws IOException {
		return getAllFiles(new File(path), ext);
	}

	public static List getAllFiles(final File path, final String ext)
			throws IOException {
		String nowExt = ext.startsWith(".") && ext.length() > 1 ? ext
				.substring(1, ext.length()) : ext;
		List list = new ArrayList(10);
		String[] listFile = path.list();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				File tempfile = new File(path + LSystem.FS + listFile[i]);
				if (tempfile.isDirectory()) {
					List arr = getAllFiles(tempfile, nowExt);
					list.addAll(arr);
					arr.clear();
					arr = null;
				} else {
					if (getExtension(tempfile.getAbsolutePath())
							.equalsIgnoreCase(nowExt))
						list.add(tempfile.getAbsolutePath());
				}
			}
		}
		return list;
	}

	private static long getAllDirSizeK(File dir, long nowSize) {
		File files[] = dir.listFiles();
		if (files == null)
			return nowSize;
		long dirSize = 0L;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			long size = file.length();
			if (file.isDirectory())
				size = getDirFileSize(file);
			dirSize += size;
		}
		nowSize = (nowSize + dirSize) / 1000L;
		return nowSize;
	}
	
	public static long getKB(final File file) {
		long size = file.length();
		size /= 1024L;
		if (size == 0L) {
			size = 1L;
		}
		return size;
	}
	
	public static String getFileName(final String name) {
		File file = new File(name);
		String temp = file.getAbsolutePath();
		int index = temp.lastIndexOf(LSystem.FS);
		if (index == -1) {
			return "";
		} else {
			return temp.substring(index + 1);
		}
	}
	
	public long getDirSizeKB() {
		return getDirSizeKB(this).longValue();
	}

	public static Long getDirSizeKB(File dir) {
		long size = getDirFileSize(dir);
		return new Long(size / 1000L);
	}

	public static long getDirFileSize(File dir) {
		File files[] = dir.listFiles();
		long dirSize = 0L;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			long size = file.length();
			if (file.isDirectory()) {
				size = getDirFileSize(file);
			}
			dirSize += size;
		}
		return dirSize;
	}

	public static String getAbsoluteFilePath(String path, String name) {
		return new File(path, name).getPath();
	}

	public static String getExtension(String name) {
		int index = name.lastIndexOf(".");
		if (index == -1) {
			return "";
		} else {
			return name.substring(index + 1);
		}
	}

	public static void deleteFiles(final String path, final String ext)
			throws IOException {
		deleteFiles(new File(path), ext);
	}

	public static void deleteFiles(final File path, final String ext)
			throws IOException {
		String nowExt = ext.startsWith(".") && ext.length() > 1 ? ext
				.substring(1, ext.length()) : ext;
		String[] listFile = path.list();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				File tempfile = new File(path + LSystem.FS + listFile[i]);
				if (tempfile.isDirectory()) {
					deleteFiles(tempfile, nowExt);
				} else {
					if (getExtension(tempfile.getAbsolutePath())
							.equalsIgnoreCase(nowExt))
						tempfile.delete();
				}
			}
		}
	}

	public boolean deleteAll() {
		if (isFile()) {
			return delete();
		} else {
			return deleteAll(this);
		}
	}

	public boolean deleteAll(final String ext) {
		try {
			LDirectory.deleteFiles(this, ext);
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	private boolean deleteAll(File dir) {
		String fileNames[] = dir.list();
		if (fileNames == null) {
			return false;
		}
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
}
