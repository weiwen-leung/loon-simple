package org.loon.framework.game.simple.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;


/**
 * Copyright 2008
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
 class OSInfoImpl implements OSInfo{

	private long totalMemory;

	private long freeMemory;

	private long maxMemory;

	private String osName;

	private int totalThread;

	final static private int CPUTIME = 30;

	final static private int PERCENT = 100;

	final static private int FLAG = 10;

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}



	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public String getOSName() {
		return osName;
	}

	public void setOSName(String osName) {
		this.osName = osName;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}


	public int getTotalThread() {
		return totalThread;
	}

	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}


	public double getCpuRatio() {
		double cpuRatio = 0;
		if (LSystem.OS_NAME.startsWith("windows")) {
			cpuRatio = getCpuRatioForWindows();
		} else if (LSystem.OS_NAME.startsWith("linux")) {
			cpuRatio = getCpuRatioForLinux();
		}
		return cpuRatio;
	}

	final static private String substring(String src, int startidx, int endidx) {
		byte[] b = src.getBytes();
		StringBuffer sbr = new StringBuffer();
		for (int i = startidx; i <= endidx; i++) {
			sbr.append((char) b[i]);
		}
		return sbr.toString();
	}

	/**
	 * 获得linux  cpu占用率
	 * 
	 * @return
	 */
	final static private double getCpuRatioForLinux() {
		try {
			File file = new File("/proc/stat");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			StringTokenizer token = new StringTokenizer(br.readLine());
			token.nextToken();
			int user = Integer.parseInt(token.nextToken());
			int nice = Integer.parseInt(token.nextToken());
			int system = Integer.parseInt(token.nextToken());
			int idle = Integer.parseInt(token.nextToken());
			Thread.sleep(1000);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			token = new StringTokenizer(br.readLine());
			token.nextToken();
			int user2 = Integer.parseInt(token.nextToken());
			int nice2 = Integer.parseInt(token.nextToken());
			int sys2 = Integer.parseInt(token.nextToken());
			int idle2 = Integer.parseInt(token.nextToken());
			return (double) ((user2 + sys2 + nice2) - (user + system + nice))
					/ (float) ((user2 + nice2 + sys2 + idle2) - (user + nice
							+ system + idle));
		} catch (Exception ex) {
			return (double) 0.0;
		}

	}

	/**
	 * 获得windows cpu占用率
	 * 
	 * @return
	 */
	final static private double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir")
					+ "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
					+ "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";

			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return Double.parseDouble(String.valueOf(
						PERCENT * (busytime) / (busytime + idletime)));
			} else {
				return 0.0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0.0;
		}
	}

	final static private long[] readCpu(final Process proc) {
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FLAG) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}

				String caption = substring(line, capidx, cmdidx - 1).trim();
				String cmd = substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				if (caption.equals("System Idle Process")
						|| caption.equals("System")) {
					idletime += Long.valueOf(
							substring(line, kmtidx, rocidx - 1).trim())
							.longValue();
					idletime += Long.valueOf(
							substring(line, umtidx, wocidx - 1).trim())
							.longValue();
					continue;
				}

				kneltime += Long.valueOf(
						substring(line, kmtidx, rocidx - 1).trim()).longValue();
				usertime += Long.valueOf(
						substring(line, umtidx, wocidx - 1).trim()).longValue();
			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getAddressIP() {
		String ip = "127.0.0.1";
		try {
			InetAddress address = InetAddress.getLocalHost();
			InetAddress[] all = InetAddress.getAllByName(address.getHostName());
			for (int i = 0; i < all.length; i++) {
				String temp = null;
				temp = (ip = all[i].getHostAddress().toString());
				if (!temp.startsWith("127.0") && !temp.startsWith("169.254")) {
					return ip;
				}
			}
		} catch (UnknownHostException e) {

		}
		return ip;
	}

	final private long getDiskForLinuxInfo(final String dirPath) {
		try {
			String dir = dirPath.startsWith("/") ? dirPath : "/"+dirPath;
			long space = -1;
			Process process;
			Runtime run = Runtime.getRuntime();
			String osName = System.getProperty("os.name").toLowerCase();
			String command = "";
			if (osName.startsWith("linux")) {
				command = "df -k " + dir;
			}
			process = run.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String freeSpace = "", line;
			while ((line = in.readLine()) != null) {
				if (line.length() > 0) {
					freeSpace = line;
				}
			}
			if (freeSpace == null || freeSpace.length() == 0) {
				return -1;
			}
			process.destroy();
			freeSpace = freeSpace.trim().replaceAll("\\\\", "\\/");
			String[] results = freeSpace.split(" ");
			for (int i = results.length - 1; i > 0; i--) {
				try {
					space = Long.parseLong(results[i]);
					return space;
				} catch (NumberFormatException ex) {
					continue;
				}
			}
		} catch (IOException e) {
			return -1;
		}
		return -1;
	}

	/**
	 * 获得windows下指定地址硬盘空间大小
	 * 
	 * @param dirPath
	 * @return
	 */
	final private long getDiskForWindowsInfo(String dirPath) {
		try {
			long space = -1;
			Process process;
			Runtime run = Runtime.getRuntime();
			String osName = LSystem.OS_NAME;
			String command = "";
			if (osName.startsWith("windows") && osName.indexOf("98") == -1) {
				command = "cmd.exe /c dir " + dirPath;
			} else if (osName.startsWith("windows")
					&& osName.indexOf("98") != -1) {
				command = "command.com /c dir " + dirPath;
			}
			process = run.exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String freeSpace = "", line;
			while ((line = in.readLine()) != null) {
				freeSpace = line;
			}
			if (freeSpace == null) {
				return -1;
			}
			process.destroy();
			freeSpace = freeSpace.trim();
			freeSpace = freeSpace.replaceAll("\\\\\\\\\\\\\\\\.", "");
			freeSpace = freeSpace.replaceAll(",", "");
			String[] results = freeSpace.split(" ");
			for (int i = 1; i < results.length; i++) {
				try {
					space = Long.parseLong(results[i]);
					return space;
				} catch (NumberFormatException ex) {
					continue;
				}
			}
			return space;
		} catch (IOException e) {
			return -1;
		}
	}

	public long getObjectDisk(final String dirPath) {
		String path = dirPath;
		int index = path.indexOf(LSystem.FS);
		path = path.substring(0, index);
		String osName = LSystem.OS_NAME;
		if (osName.startsWith("windows")) {
			return getDiskForWindowsInfo(path);
		} else if (osName.startsWith("linux")) {
			return getDiskForLinuxInfo(path);
		} else {
			throw new RuntimeException("Lack of access to hard drive size!");
		}
	}

}
