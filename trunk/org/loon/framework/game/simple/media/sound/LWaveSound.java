package org.loon.framework.game.simple.media.sound;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.Mixer;

import org.loon.framework.game.simple.core.resource.Resources;

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

public class LWaveSound extends Thread implements LineListener, Sound {

	private Clip clip;

	private static boolean available;

	private static boolean volumeSupported;

	private static Mixer mixer;

	private static final int UNINITIALIZED = 0;

	private static final int INITIALIZING = 1;

	private static final int INITIALIZED = 2;

	private boolean isRunning;

	private int volume;

	private static int rendererStatus = UNINITIALIZED;

	public LWaveSound() {

		if (rendererStatus == UNINITIALIZED) {
			rendererStatus = INITIALIZING;
		}
		setSoundVolume(Sound.defaultMaxVolume);

	}

	public boolean isAvailable() {
		if (rendererStatus != INITIALIZED) {
			int i = 0;
			while (rendererStatus != INITIALIZED && i++ < 50) {
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
				}
			}
			if (rendererStatus != INITIALIZED) {
				rendererStatus = INITIALIZED;
				available = false;
			}
		}
		return available;
	}

	public boolean isStopped() {
		return isRunning;
	}

	public void playSound(String fileName) {
		playSound(Resources.getResourceToInputStream(fileName));
	}

	public void playSound(final InputStream in) {
		try {
			stopSound();
			if (this.clip != null) {
				this.clip.drain();
				this.clip.close();
			}

			AudioInputStream ain = AudioSystem.getAudioInputStream(in);
			AudioFormat format = ain.getFormat();

			if ((format.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (format.getEncoding() == AudioFormat.Encoding.ALAW)) {

				AudioFormat temp = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(),
						format.getSampleSizeInBits() * 2, format.getChannels(),
						format.getFrameSize() * 2, format.getFrameRate(), true);
				ain = AudioSystem.getAudioInputStream(temp, ain);
				format = temp;
			}

			DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat(),
					((int) ain.getFrameLength() * format.getFrameSize()));
			if (mixer != null) {
				this.clip = (Clip) mixer.getLine(info);
			} else {
				this.clip = (Clip) AudioSystem.getLine(info);
			}
			this.clip.addLineListener(this);
			this.clip.open(ain);
			this.setSoundVolume(volume);
			this.clip.start();
			this.isRunning = true;
			this.start();
		} catch (Exception e) {
		}
	}

	public void interrupt() {
		isRunning = false;
	}

	public void stopSound() {
		interrupt();
		if (this.clip != null) {
			this.clip.stop();
			this.clip.setMicrosecondPosition(0);
			this.clip.removeLineListener(this);
		}
	}

	public void update(LineEvent e) {
		if (e.getType() == LineEvent.Type.STOP) {
			this.clip.stop();
			this.clip.setMicrosecondPosition(0);
			this.clip.removeLineListener(this);
		}
	}

	public void setSoundVolume(int volume) {
		this.volume = volume;
		if (this.clip == null) {
			return;
		}
		Control.Type vol1 = FloatControl.Type.VOLUME, vol2 = FloatControl.Type.MASTER_GAIN;
		FloatControl c = (FloatControl) clip
				.getControl(FloatControl.Type.MASTER_GAIN);
		float min = c.getMinimum();
		float v = volume * (c.getMaximum() - min) / 100f + min;
		if (this.clip.isControlSupported(vol1)) {
			FloatControl volumeControl = (FloatControl) this.clip
					.getControl(vol1);
			volumeControl.setValue(v);
		} else if (this.clip.isControlSupported(vol2)) {
			FloatControl gainControl = (FloatControl) this.clip
					.getControl(vol2);
			gainControl.setValue(v);
		}
	}

	public void run() {
		while (isRunning) {
			do {
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
				}
			} while (clip != null && clip.isRunning());
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
			}
		}
		stopSound();
	}

	public boolean isVolumeSupported() {
		return volumeSupported;
	}

}
