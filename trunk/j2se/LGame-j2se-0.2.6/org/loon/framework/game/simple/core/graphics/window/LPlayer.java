package org.loon.framework.game.simple.core.graphics.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Vector;

import mediaframe.mpeg4.audio.AudioPlayer;
import mediaframe.mpeg4.isofile.MP4Atom;
import mediaframe.mpeg4.isofile.MP4Descriptor;
import mediaframe.mpeg4.video.BitStream;
import mediaframe.mpeg4.video.MPEG4Decoder;
import mediaframe.mpeg4.video.VideoFrame;

import org.loon.framework.game.simple.GameManager;
import org.loon.framework.game.simple.action.sprite.WaitAnimation;
import org.loon.framework.game.simple.core.LSystem;
import org.loon.framework.game.simple.core.graphics.LContainer;
import org.loon.framework.game.simple.core.graphics.device.LGraphics;
import org.loon.framework.game.simple.core.graphics.window.achieve.IMpeg4;
import org.loon.framework.game.simple.core.timer.LTimer;
import org.loon.framework.game.simple.media.DataBuffer;
import org.loon.framework.game.simple.media.DataChannel;
import org.loon.framework.game.simple.media.DataSample;
import org.loon.framework.game.simple.media.DataStream;
import org.loon.framework.game.simple.utils.DateUtils;
import org.loon.framework.game.simple.utils.FileUtils;
import org.loon.framework.game.simple.utils.GraphicsUtils;
import org.loon.framework.game.simple.utils.http.ExternalDownload;
import org.loon.framework.game.simple.utils.http.States;
import org.loon.framework.game.simple.utils.http.WebClient;

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
public class LPlayer extends LContainer implements IMpeg4 {

	private static final long serialVersionUID = 1L;

	private LButton muteButton, playPauseButton, stopButton;

	private WaitAnimation wait;

	private Image movieScreen = null;

	private int[] pixels = null;

	private Font ERROR_FONT = new Font("Arial", 0, 12);

	public final static int ALPHA_MASK = (255 << 24);

	public final static int SHIFT_BITS = 16;

	private boolean isClick;

	public final static int R_CR_COEFF = (int) (1.402 * (1 << SHIFT_BITS));

	public final static int G_CR_COEFF = (int) (0.714 * (1 << SHIFT_BITS));

	public final static int G_CB_COEFF = (int) (0.344 * (1 << SHIFT_BITS));

	public final static int B_CB_COEFF = (int) (1.772 * (1 << SHIFT_BITS));

	private boolean complete;

	private long start_playing_time = 0;

	private int video_width;

	private int video_height;

	private boolean java2_platform = false;

	private int pixel_frame_width;

	private int pixel_frame_height;

	int memory_image_width;

	int memory_image_height;

	private int current_frame = 0;

	private long last_sleep_time = 0;

	private long autoStop;

	public final int CONNECTION_SPEED_UNKNOWN = 0;

	public final int CONNECTION_SPEED_24K = 1; // 1333 ms

	public final int CONNECTION_SPEED_44K = 2; // 727 ms

	public final int CONNECTION_SPEED_128K = 3; // 250 ms

	public final int CONNECTION_SPEED_256K = 4; // 125 ms

	public final int CONNECTION_SPEED_350K = 5; // 91 ms

	public final int CONNECTION_SPEED_500K = 6; // 64 ms

	public final int CONNECTION_SPEED_800K = 7; // 40 ms

	public final String[] CONNECTION_PARAMETERS = { "default_media",
			"24k_media", "44k_media", "128k_media", "256k_media", "350k_media",
			"500k_media", "800+_media" };

	public final String[] CONNECTION_TEXT = { "unknown", "24", "44", "128",
			"256", "350", "500", "800" };

	public final int[] CONNECTION_VALUES = { -1, 24, 44, 128, 256, 350, 500,
			800 };

	private Vector audioSamples = new Vector();

	private Vector videoSamples = new Vector();

	private boolean showButtons;

	public final String[] ERROR_MESSAGES = { null,
			"Unable to load the movie file",
			"The parameter \"default_media\" isn't specified",
			"Unsupported movie file format",
			"The license does not exist in the movie file", null, null, null,
			null, null, "MEDIAFRAME LOCKED!" };

	public final static int MANUAL_PLAYBACK = 0;

	public final static int ROLLOWER_TO_PLAY = 1;

	public final static int CLICK_TO_PLAY = 2;

	public final static int START_STATE = 0;

	public final static int READY_STATE = 1;

	public final static int PLAY_STATE = 2;

	public final static int PAUSE_STATE = 3;

	public final static int STOP_STATE = 4;

	public final static int REWIND_STATE = 5;

	public final static int ERROR_STATE = 6;

	public final static String[] STATE_TEXT = { "start", "ready", "playing",
			"paused", "finished", "finished", "error" };

	private volatile int player_state = START_STATE;

	private boolean buffering = true;

	private boolean firstLoop = true;

	private String fileName = null;

	private AudioPlayer audioPlayer = null;

	private MPEG4Decoder videoDecoder = null;

	private DataBuffer dataBuffer = null;

	private InputStream audioStream = null;

	private BitStream videoStream = null;

	private volatile int connection_speed = -1;

	private States states;

	private boolean download;

	private int movieTime = -1;

	private double video_rate = -1;

	private int movieLength = -1;

	private int movieDuration = -1;

	private int video_size = -1;

	private int playback_mode = CLICK_TO_PLAY;

	private boolean autoplay = false;

	public boolean audio_enabled = true;

	private boolean playback_loop = false;

	private String sBufferSize = "20%";

	public boolean smooth_video = true;

	public boolean licenseIsCorrect = true;

	private int error_code = 0;

	private int volume = 100;

	public boolean mute;

	private LTimer timer;

	private int audioHeaderSize = 0;

	private long audioHeaderOffset = 0;

	private volatile Thread movieParseThread = null;

	public LPlayer() {
		this(0, 0, GameManager.getSystemHandler().getWidth(), GameManager
				.getSystemHandler().getHeight());
		this.hideButtons();
		this.setLocked(true);
		this.isFull = true;
		this.showButtons = false;
	}

	public LPlayer(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.licenseIsCorrect = true;
		this.smooth_video = true;
		this.audio_enabled = true;
		this.showButtons = true;
		this.playback_loop = false;
		this.playback_mode = MANUAL_PLAYBACK;
		this.playersetvolume("100%");
		if (audio_enabled && (!AudioPlayer.isSoundEnabled())) {
			audio_enabled = false;
			mute = true;
		}
		try {
			Class.forName("java.awt.Graphics2D");
			java2_platform = true;
		} catch (Exception ex) {
		}
		this.autoStop = -1;
		this.setBackground(Color.BLACK);
		this.wait = new WaitAnimation(width, height);
		this.wait.white();
		this.timer = new LTimer(100);
		this.addButtons();
		this.setElastic(false);
		this.customRendering = true;
		this.setLayer(100);
		this.setLocked(true);
	}

	public void load(String fileName) {
		load(fileName, false);
	}

	public void load(final String fileName, boolean inner) {
		String name = fileName.toLowerCase();
		if (name.startsWith("http://") || name.startsWith("ftp://")
				|| name.startsWith("https://")) {
			download = true;
		}
		if (!download || inner) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					innerLoad(fileName);
				}
			});
			thread.start();
			thread = null;
		} else {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					WebClient client = new WebClient(fileName);
					if (!client.exists()) {
						try {
							ExternalDownload externalDownload = client
									.getExternalDownload(5);
							LPlayer.this.states = externalDownload.getInfo()
									.getState();
							externalDownload.start();
							while (states.getPercent() != 100) {
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						download = false;
					}
					innerLoad(client.getFileName());
				}
			});
			thread.start();
			thread = null;
		}
	}

	private void innerLoad(String path) {
		if (player_state == REWIND_STATE) {
			return;
		}
		complete = true;
		error_code = 0;
		player_state = REWIND_STATE;
		update();
		close();
		LSystem.destroy();
		LSystem.gc(10);
		if (!audio_enabled) {
			audio_enabled = true;
			if (audio_enabled && (!AudioPlayer.isSoundEnabled())) {
				audio_enabled = false;
				mute = true;
			} else {
				mute = false;
			}
		}
		firstLoop = true;
		autoplay = false;
		Object[] res = FileUtils.readPath(LPlayer.this.fileName = path);
		if (res != null) {
			loadInputStream(res[0] != null ? (InputStream) res[0] : null,
					res[1] != null ? ((Integer) res[1]).intValue() : 0);
		}

		if (dataBuffer != null) {
			movieScreen = null;
			pixels = null;
			movieParseThread = Thread.currentThread();
			parseFile();
			if (movieParseThread == Thread.currentThread()) {
				if (error_code == 0) {
					playerstart();
				}
				movieParseThread = null;
			}
		}
		complete = false;
	}

	public void play(final String fileName) {
		if (!complete) {
			Thread thread = new Thread(new Runnable() {
				public void run() {
					load(fileName);
					while (buffering) {
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
						}
					}
					playerplay();
				}
			});
			thread.start();
			thread = null;
		}
	}

	public void finalize() {
		this.dispose();
	}

	public void draw(LGraphics g, int x, int y, int width, int height) {
		if (player_state == ERROR_STATE) {
			g.setColor(Color.black);
			g.fillRect(x, y, width, height);
			g.setColor(Color.white);
			g.setFont(ERROR_FONT);
			if (error_code == 10) {
				g.drawString(ERROR_MESSAGES[error_code], x + (width / 2 - 70),
						x + (height / 2 - 3));
			} else {
				g.drawString("ERROR: " + ERROR_MESSAGES[error_code], x + 10,
						y + 20);
			}
		} else if ((player_state == PLAY_STATE)
				|| (player_state == PAUSE_STATE)) {
			if (movieScreen != null) {
				if (java2_platform) {
					if (smooth_video) {
						g.drawImage(movieScreen, x, y, width, height);
					} else {
						g.drawImage(movieScreen, x, y);
					}
				} else {
					g.drawImage(movieScreen, x, y);
				}

			}
		}
	}

	final static private BufferedImage mute_up = GraphicsUtils
			.loadBufferedImage("system/image/mute_up.gif");

	final static private BufferedImage mute_down = GraphicsUtils
			.loadBufferedImage("system/image/mute_down.gif");

	final static private BufferedImage play_up = GraphicsUtils
			.loadBufferedImage("system/image/play_up.gif");

	final static private BufferedImage pause = GraphicsUtils
			.loadBufferedImage("system/image/pause.gif");

	final static private BufferedImage stop_up = GraphicsUtils
			.loadBufferedImage("system/image/stop_up.gif");

	final static private BufferedImage stop_down = GraphicsUtils
			.loadBufferedImage("system/image/stop_down.gif");

	final static private BufferedImage[] mute1 = new BufferedImage[] {
			GraphicsUtils.getGray(mute_up), mute_up };

	final static private BufferedImage[] mute2 = new BufferedImage[] {
			GraphicsUtils.getGray(mute_down), mute_down };

	final static private BufferedImage[] playPause1 = new BufferedImage[] {
			GraphicsUtils.getGray(play_up), play_up };

	final static private BufferedImage[] playPause2 = new BufferedImage[] {
			GraphicsUtils.getGray(pause), pause };

	final static private BufferedImage[] stop1 = new BufferedImage[] {
			GraphicsUtils.getGray(stop_up), stop_up };

	final static private BufferedImage[] stop2 = new BufferedImage[] {
			GraphicsUtils.getGray(stop_down), stop_down };

	private void addButtons() {
		if (!showButtons) {
			return;
		}
		int control_panel_x_coord = getScreenX() + 10;
		int control_panel_y_coord = getScreenY() + (getHeight() - 25);
		muteButton = new LButton(mute1, "", 17, 16, control_panel_x_coord,
				control_panel_y_coord) {
			private boolean flag;

			public void doClick() {
				if (!flag) {
					setImages(mute2);
					playersetmute(true);
				} else {
					setImages(mute1);
					playersetmute(false);
				}
				flag = !flag;
			}
		};
		playPauseButton = new LButton(playPause2, "", 17, 16,
				control_panel_x_coord + 17, control_panel_y_coord) {
			public void doClick() {
				if ((player_state == READY_STATE)
						|| (player_state == PAUSE_STATE)) {
					setImages(playPause1);
					playerplay();
					stopButton.setImages(stop1);
				} else {
					setImages(playPause2);
					playerpause();

				}

			}
		};
		stopButton = new LButton(stop1, "", 17, 16, control_panel_x_coord + 34,
				control_panel_y_coord) {
			public void doClick() {
				if (!(player_state == READY_STATE)
						|| (player_state == PAUSE_STATE)) {
					setImages(stop2);
					playerrewind();
				} else {
					setImages(stop1);
				}
			}
		};
		add(muteButton);
		add(playPauseButton);
		add(stopButton);
	}

	private void loadInputStream(InputStream is, int length) {
		movieTime = -1;
		try {
			boolean encryptedStream = false;
			movieLength = length;
			int bufferSize = parsePercentValue(movieLength, sBufferSize,
					movieLength / 5);
			if (bufferSize == 0) {
				bufferSize = movieLength / 5;
			}
			if (bufferSize > movieLength) {
				bufferSize = movieLength;
			}
			buffering = true;
			dataBuffer = new DataBuffer(this, is, movieLength, bufferSize,
					encryptedStream);
		} catch (Throwable ex) {
			error_code = 1;
			player_state = ERROR_STATE;
		}
	}

	public void parseFile() {
		int timeScale = -1;
		long duration = -1;
		long videoHeaderOffset = 0;
		int videoHeaderSize = 0;

		try {
			audioSamples.removeAllElements();
			videoSamples.removeAllElements();
			DataStream stream = new DataStream(dataBuffer);
			for (; ((audioSamples.size() == 0) && (videoSamples.size() == 0));) {
				MP4Atom atom = MP4Atom.createAtom(stream);
				if (atom.getType() == MP4Atom.MP4MovieAtomType) {
					MP4Atom movieAtom = atom;
					int j = 0;
					MP4Atom trackAtom = movieAtom.lookup(
							MP4Atom.MP4TrackAtomType, j++);
					while (trackAtom != null) {
						MP4Atom mediaAtomType = trackAtom.lookup(
								MP4Atom.MP4MediaAtomType, 0);
						if (mediaAtomType == null)
							throw new Exception(
									"The MP4MediaAtom does not exist");

						MP4Atom handlerAtom = mediaAtomType.lookup(
								MP4Atom.MP4HandlerAtomType, 0);
						if (handlerAtom == null)
							throw new Exception(
									"The MP4HandlerAtom does not exist");

						if (handlerAtom.getHandlerType() == MP4Atom
								.typeToInt("soun")
								|| handlerAtom.getHandlerType() == MP4Atom
										.typeToInt("vide")) {

							MP4Atom mediaInformationAtom = mediaAtomType
									.lookup(
											MP4Atom.MP4MediaInformationAtomType,
											0);
							if (mediaInformationAtom == null)
								throw new Exception(
										"The MP4MediaInformationAtom does not exist");

							MP4Atom sampleTableAtom = mediaInformationAtom
									.lookup(MP4Atom.MP4SampleTableAtomType, 0);
							if (sampleTableAtom == null)
								throw new Exception(
										"The MP4SampleTableAtom does not exist");

							MP4Atom sampleDescriptionAtom = sampleTableAtom
									.lookup(
											MP4Atom.MP4SampleDescriptionAtomType,
											0);
							if (sampleDescriptionAtom == null)
								throw new Exception(
										"The MP4SampleDescriptionAtom does not exist");

							if (handlerAtom.getHandlerType() == MP4Atom
									.typeToInt("vide")) {

								MP4Atom mediaHeaderAtom = mediaAtomType.lookup(
										MP4Atom.MP4MediaHeaderAtomType, 0);
								if (mediaHeaderAtom == null)
									throw new Exception(
											"The MP4MediaHeaderAtom does not exist");

								duration = mediaHeaderAtom.getDuration();
								timeScale = mediaHeaderAtom.getTimeScale();
								movieDuration = (int) (duration * 1000 / timeScale);

								MP4Atom visualSampleEntry = sampleDescriptionAtom
										.lookup(
												MP4Atom.MP4VisualSampleEntryAtomType,
												0);
								if (visualSampleEntry == null)
									throw new Exception(
											"The MP4VisualSampleEntryAtom does not exist");
								video_width = visualSampleEntry.getWidth();
								video_height = visualSampleEntry.getHeight();
								video_size = 0;
								videoHeaderOffset = videoHeaderSize = 0;

								MP4Atom esdAtom = visualSampleEntry.lookup(
										MP4Atom.MP4ESDAtomType, 0);
								if (esdAtom != null) {
									MP4Descriptor esd_descriptor = esdAtom
											.getEsd_descriptor();
									if (esd_descriptor != null) {
										MP4Descriptor decoderConfigDescriptor = esd_descriptor
												.lookup(
														MP4Descriptor.MP4DecoderConfigDescriptorTag,
														0);
										if (decoderConfigDescriptor != null) {
											MP4Descriptor decSpecificInfoDescriptor = decoderConfigDescriptor
													.lookup(
															MP4Descriptor.MP4DecSpecificInfoDescriptorTag,
															0);
											if (decSpecificInfoDescriptor != null) {
												videoHeaderOffset = decSpecificInfoDescriptor
														.getDecSpecificDataOffset();
												videoHeaderSize = decSpecificInfoDescriptor
														.getDecSpecificDataSize();
											}
										}
									}
								}
							} else {

								MP4Atom audioSampleEntry = sampleDescriptionAtom
										.lookup(
												MP4Atom.MP4AudioSampleEntryAtomType,
												0);
								if (audioSampleEntry == null)
									throw new Exception(
											"The MP4AudioSampleEntryAtom does not exist");

								audioHeaderOffset = audioHeaderSize = 0;

								MP4Atom esdAtom = audioSampleEntry.lookup(
										MP4Atom.MP4ESDAtomType, 0);
								if (esdAtom != null) {
									MP4Descriptor esd_descriptor = esdAtom
											.getEsd_descriptor();
									if (esd_descriptor != null) {
										MP4Descriptor decoderConfigDescriptor = esd_descriptor
												.lookup(
														MP4Descriptor.MP4DecoderConfigDescriptorTag,
														0);
										if (decoderConfigDescriptor != null) {
											MP4Descriptor decSpecificInfoDescriptor = decoderConfigDescriptor
													.lookup(
															MP4Descriptor.MP4DecSpecificInfoDescriptorTag,
															0);
											if (decSpecificInfoDescriptor != null) {
												audioHeaderOffset = decSpecificInfoDescriptor
														.getDecSpecificDataOffset();
												audioHeaderSize = decSpecificInfoDescriptor
														.getDecSpecificDataSize();
											}
										}
									}
								}
							}

							MP4Atom sampleToChunkAtom = sampleTableAtom.lookup(
									MP4Atom.MP4SampleToChunkAtomType, 0);
							if (sampleToChunkAtom == null)
								throw new Exception(
										"The MP4SampleToChunkAtom does not exist");

							MP4Atom sampleSizeAtom = sampleTableAtom.lookup(
									MP4Atom.MP4SampleSizeAtomType, 0);
							if (sampleSizeAtom == null) {
								sampleSizeAtom = sampleTableAtom
										.lookup(
												MP4Atom.MP4CompactSampleSizeAtomType,
												0);
							}
							if (sampleSizeAtom == null)
								throw new Exception(
										"The MP4SampleSizeAtom does not exist");

							MP4Atom chunkOffsetAtomType = sampleTableAtom
									.lookup(MP4Atom.MP4ChunkOffsetAtomType, 0);
							if (chunkOffsetAtomType == null) {
								chunkOffsetAtomType = sampleTableAtom.lookup(
										MP4Atom.MP4ChunkLargeOffsetAtomType, 0);
							}
							if (chunkOffsetAtomType == null)
								throw new Exception(
										"The MP4ChunkOffsetAtom does not exist");

							Vector records = sampleToChunkAtom.getRecords();
							int currentSample = 0;
							for (int i = 0; i < records.size(); i++) {
								MP4Atom.Record record = (MP4Atom.Record) records
										.elementAt(i);
								int maxChunkNumber = 0;
								if (i == records.size() - 1) {
									maxChunkNumber = chunkOffsetAtomType
											.getChunks().size();
								} else {
									MP4Atom.Record nextRecord = (MP4Atom.Record) records
											.elementAt(i + 1);
									maxChunkNumber = nextRecord.getFirstChunk() - 1;
								}
								for (int chunkNumber = record.getFirstChunk() - 1; chunkNumber < maxChunkNumber; chunkNumber++) {
									Long offset = (Long) chunkOffsetAtomType
											.getChunks().elementAt(chunkNumber);
									if (offset == null)
										throw new Exception(
												"Unable to find the chunk with the id = "
														+ record
																.getFirstChunk());
									long sampleOffset = offset.longValue();
									for (int num = 0; num < record
											.getSamplesPerChunk(); num++) {
										int size = sampleSizeAtom
												.getSampleSize();
										if (size == 0) {
											Integer iSize = (Integer) sampleSizeAtom
													.getSamples().elementAt(
															currentSample);
											if (iSize == null)
												throw new Exception(
														"Unable to find the sample with the id = "
																+ currentSample);
											size = iSize.intValue();
										}
										DataSample sample = new DataSample(
												sampleOffset, size);

										if (handlerAtom.getHandlerType() == MP4Atom
												.typeToInt("soun")) {
											audioSamples.addElement(sample);
										} else if (handlerAtom.getHandlerType() == MP4Atom
												.typeToInt("vide")) {
											videoSamples.addElement(sample);
											video_size += size;
										}
										sampleOffset += size;
										currentSample++;
									}
								}
							}
						}
						trackAtom = movieAtom.lookup(MP4Atom.MP4TrackAtomType,
								j++);
					}
				}
			}
		} catch (InterruptedException iex) {
			return;
		} catch (InterruptedIOException iox) {
			return;
		} catch (Exception ex) {
			error_code = 3;
			player_state = ERROR_STATE;
		}

		video_rate = (((double) videoSamples.size()) * ((double) timeScale) / (double) (duration));
		if ((videoHeaderSize > 0) && (videoHeaderOffset > 0)) {
			videoSamples.insertElementAt(new DataSample(videoHeaderOffset,
					videoHeaderSize), 0);
			video_size += videoHeaderSize;
		}
		if ((audioHeaderSize > 0) && (audioHeaderOffset > 0)) {
			audioSamples.insertElementAt(new DataSample(audioHeaderOffset,
					audioHeaderSize), 0);
		}
	}

	public void playerstart() {
		try {
			if (audio_enabled) {
				try {
					audioStream = new DataChannel(DataChannel.AUDIO_CHANNEL,
							dataBuffer, audioSamples);
					audioPlayer = AudioPlayer.createAudioPlayer(audioStream,
							audioHeaderSize);
					audioPlayer.setVolume(volume);
					audioPlayer.setMute(mute);
				} catch (Exception ex) {
					mute = true;
					audioPlayer = null;
					audio_enabled = false;
				}
			}
			videoStream = new BitStream(new DataChannel(
					DataChannel.VIDEO_CHANNEL, dataBuffer, videoSamples));
			videoDecoder = new MPEG4Decoder(this, videoStream, video_width,
					video_height, video_rate, video_size);
		} catch (Exception ex) {

		}
	}

	public void playerplay() {
		this.wait.setRunning(true);
		synchronized (this) {
			if (firstLoop) {
				autoplay = true;
				return;
			}
		}
		if ((buffering == true)
				|| ((player_state != READY_STATE) && (player_state != PAUSE_STATE))) {
			return;
		}
		if (playback_mode == ROLLOWER_TO_PLAY) {
			playback_mode = MANUAL_PLAYBACK;
		}
		System.gc();
		try {
			if (audioPlayer != null) {
				audioPlayer.play();
			}
			player_state = PLAY_STATE;
			update();
			synchronized (this) {
				notifyAll();
			}
			complete = false;
		} catch (Exception ex) {
		}
	}

	public void playerpause() {
		this.wait.setRunning(false);
		if ((buffering == true) || (player_state != PLAY_STATE)) {
			return;
		}
		try {
			player_state = PAUSE_STATE;
			update();
			if (audioPlayer != null) {
				audioPlayer.pause();
			}
		} catch (Exception ex) {

		}
	}

	private void playerstop() {
		this.wait.setRunning(false);
		if (audioPlayer != null) {
			try {
				audioPlayer.stop();
			} catch (Exception ex) {
			} finally {
				audioPlayer = null;
			}
		}
		if (videoDecoder != null) {
			try {
				videoDecoder.stop();
			} catch (Exception ex) {
			} finally {
				videoDecoder = null;
			}
		}
		movieTime = -1;
	}

	public void playerend() {
		this.wait.setRunning(false);
		movieTime = -1;
		if (!playback_loop) {
			player_state = STOP_STATE;
			update();
		}
		if (playback_loop) {
			autoplay = true;
			playerrewind();
		}
	}

	public void playerrewind() {
		this.wait.setRunning(false);
		if ((player_state == ERROR_STATE) || (player_state == REWIND_STATE)
				|| (player_state == START_STATE)
				|| (player_state == READY_STATE)) {
			return;
		}
		if (buffering && (player_state != START_STATE)) {
			buffering = false;
		}
		player_state = REWIND_STATE;
		update();
		playerstop();
		System.gc();
		firstLoop = true;
		playerstart();
	}

	public void startReBuffering() {
		buffering = true;
		update();
		if (player_state == PLAY_STATE) {
			if (audioPlayer != null) {
				audioPlayer.pause();
			}
		}
	}

	public void stopBuffering() throws InterruptedIOException {
		try {
			if (player_state == PLAY_STATE) {
				if (audioPlayer != null) {
					audioPlayer.play();
				}
			}
			buffering = false;
			update();
			synchronized (this) {
				notifyAll();
			}
			if ((player_state == READY_STATE) || (player_state == START_STATE)) {
				Thread.sleep(300);
			}
		} catch (InterruptedException iex) {
			throw new InterruptedIOException(iex.getMessage());
		}
	}

	public void stopReBuffering() throws InterruptedIOException {
		this.stopBuffering();
	}

	public void playersetmute(boolean mute) {
		if (audio_enabled) {
			this.mute = mute;
			update();
			if (audioPlayer != null) {
				audioPlayer.setMute(mute);
			}
		}
	}

	public void playersetvolume(String sValue) {
		if (audio_enabled) {
			try {
				volume = parsePercentValue(100, sValue, volume);
				if ((sValue != null) && (sValue.length() >= 1)
						&& (sValue.charAt(sValue.length() - 1) != '%')) {
					if (volume == Integer.parseInt(sValue)) {
						volume = (volume - 1) * 100 / 9;
					}
				}
				if (volume < 0) {
					volume = 0;
				} else if (volume > 100) {
					volume = 100;
				}
				if (audioPlayer != null) {
					audioPlayer.setVolume(volume);
				}
			} catch (Exception ex) {
			}
		}
	}

	public boolean getAudioState() {
		return mute;
	}

	public String getFileName() {
		return fileName == null ? "" : fileName;
	}

	public int getVolume() {
		return (int) ((float) volume * 9.f / 100.f + 1.5f);
	}

	public int getVolume(String type) {
		if ("%".equals(type)) {
			return volume;
		}
		return getVolume();
	}

	public int getVideoSize() {
		return movieLength;
	}

	public int getVideoLength() {
		return movieDuration;
	}

	public int getVideoTime() {
		return movieTime;
	}

	public int getConnectionSpeed() {
		return CONNECTION_VALUES[connection_speed >= 0 ? connection_speed : 0];
	}

	public String getState() {
		if (buffering && (player_state != ERROR_STATE)) {
			if ((player_state == START_STATE) || (player_state == READY_STATE)
					|| firstLoop) {
				return "buffering";
			} else {
				return "re-buffering";
			}
		}
		return STATE_TEXT[player_state];
	}

	public int get_errorcode() {
		return error_code;
	}

	public String get_errortext() {
		return ERROR_MESSAGES[error_code];
	}

	/**
	 * 解析图像桢(原mediaframe组件函数改写)
	 */
	public void nextFrame(VideoFrame videoFrame) throws InterruptedIOException {
		int width = videoFrame.getFrameWidth();
		int height = videoFrame.getFrameHeight();
		int buffer_width = videoFrame.getBufferWidth();
		int[][] y_cb_cr_pixels = videoFrame.getPixelData();
		if (pixels == null) {
			if ((width == -1) && (height == -1)) {
				width = 320;
				height = 240;
			} else if (width == -1) {
				width = (width * height) / height;
			} else if (height == -1) {
				height = (height * width) / width;
			}
		}
		if (java2_platform && smooth_video) {
			memory_image_width = pixel_frame_width = width;
			memory_image_height = pixel_frame_height = height;
		} else {
			memory_image_width = width;
			memory_image_height = height;
			pixel_frame_width = width > width ? width : width;
			pixel_frame_height = height > height ? height : height;
		}
		if (pixels == null) {
			int pixelSize = pixel_frame_width * pixel_frame_height;
			pixels = new int[pixelSize];
			movieScreen = GraphicsUtils.newAwtRGBImage(pixels, pixel_frame_width,
					pixel_frame_height, pixelSize);
		}
		long playingTime = System.currentTimeMillis() - start_playing_time;
		int i, j, y, cb, cr, r_cr, g_cr_cb, b_cb, r, g, b;
		int lumminance_index = 0;
		int chrominance_index = 0;
		int pixel_index = 0;
		// Y CR CB -> R G B transformation
		for (j = 0; j < height; j += 2) {
			for (i = 0; i < width; i++) {
				y = y_cb_cr_pixels[0][lumminance_index + i] << SHIFT_BITS;
				cb = y_cb_cr_pixels[1][chrominance_index + (i >> 1)] - 128;
				cr = y_cb_cr_pixels[2][chrominance_index + (i >> 1)] - 128;
				r_cr = R_CR_COEFF * cr;
				g_cr_cb = -G_CB_COEFF * cb - G_CR_COEFF * cr;
				b_cb = B_CB_COEFF * cb;
				r = -(y + r_cr) >> SHIFT_BITS;
				g = -(y + g_cr_cb) >> SHIFT_BITS;
				b = -(y + b_cb) >> SHIFT_BITS;
				r = -(r & (r >> 63)) - 255;
				g = -(g & (g >> 63)) - 255;
				b = -(b & (b >> 63)) - 255;
				pixels[pixel_index + i++] = ALPHA_MASK
						+ ((b & (b >> 63)) + 255)
						+ (((g & (g >> 63)) + 255) << 8)
						+ (((r & (r >> 63)) + 255) << 16);
				y = y_cb_cr_pixels[0][lumminance_index + i] << SHIFT_BITS;
				r = -(y + r_cr) >> SHIFT_BITS;
				g = -(y + g_cr_cb) >> SHIFT_BITS;
				b = -(y + b_cb) >> SHIFT_BITS;
				// r = g = b = - ((y >> SHIFT_BITS) + 128);
				r = -(r & (r >> 63)) - 255;
				g = -(g & (g >> 63)) - 255;
				b = -(b & (b >> 63)) - 255;
				pixels[pixel_index + i--] = ALPHA_MASK
						+ ((b & (b >> 63)) + 255)
						+ (((g & (g >> 63)) + 255) << 8)
						+ (((r & (r >> 63)) + 255) << 16);
				y = y_cb_cr_pixels[0][lumminance_index + buffer_width + i] << SHIFT_BITS;
				r = -(y + r_cr) >> SHIFT_BITS;
				g = -(y + g_cr_cb) >> SHIFT_BITS;
				b = -(y + b_cb) >> SHIFT_BITS;
				// r = g = b = - ((y >> SHIFT_BITS) + 128);
				r = -(r & (r >> 63)) - 255;
				g = -(g & (g >> 63)) - 255;
				b = -(b & (b >> 63)) - 255;
				pixels[pixel_index + pixel_frame_width + i++] = ALPHA_MASK
						+ ((b & (b >> 63)) + 255)
						+ (((g & (g >> 63)) + 255) << 8)
						+ (((r & (r >> 63)) + 255) << 16);
				y = y_cb_cr_pixels[0][lumminance_index + buffer_width + i] << SHIFT_BITS;
				r = -(y + r_cr) >> SHIFT_BITS;
				g = -(y + g_cr_cb) >> SHIFT_BITS;
				b = -(y + b_cb) >> SHIFT_BITS;
				// r = g = b = - ((y >> SHIFT_BITS) + 128);
				r = -(r & (r >> 63)) - 255;
				g = -(g & (g >> 63)) - 255;
				b = -(b & (b >> 63)) - 255;
				pixels[pixel_index + pixel_frame_width + i] = ALPHA_MASK
						+ ((b & (b >> 63)) + 255)
						+ (((g & (g >> 63)) + 255) << 8)
						+ (((r & (r >> 63)) + 255) << 16);
			}
			lumminance_index += buffer_width << 1;
			pixel_index += pixel_frame_width << 1;
			chrominance_index += buffer_width;
		}
		if (!(java2_platform && smooth_video)) {
			if (width > width) {
				int x_increment = (width << 8) / width;
				for (j = 0; j < height; j++) {
					int source_index = ((j * width + width - 1) << 8)
							+ (1 << (8 - 1)) + x_increment;
					int destination_index = (j + 1) * width - 1;
					int eight_pixels_loops = width >> 3;
					for (i = 0; i < eight_pixels_loops; i++) {
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
					}
					for (i = 0; i < (width & 7); i++) {
						pixels[destination_index--] = pixels[(source_index -= x_increment) >> 8];
					}
				}
			}
			if (height != height) {
				int y_increment = (height << 8) / height;
				int source_y, d_y_increment, destination_index = 0;
				if (height > height) {
					source_y = ((height - 1) << 8) + (1 << (8 - 1));
					destination_index = (height - 1) * width;
					d_y_increment = -width;
					y_increment = -y_increment;
				} else {
					y_increment = (height << 8) / height;
					source_y = 0;
					d_y_increment = +pixel_frame_width;
				}
				for (j = 0; j < (height - 1); j++) {
					System.arraycopy(pixels, (source_y >> 8)
							* pixel_frame_width, pixels, destination_index,
							pixel_frame_width);
					destination_index += d_y_increment;
					source_y += y_increment;
				}
			}
			if (width < width) {
				int x_increment = (width << 8) / width;
				for (j = 0; j < height; j++) {
					int source_index = ((j * pixel_frame_width) << 8)
							+ (1 << (8 - 1)) - x_increment;
					int destination_index = j * pixel_frame_width;
					int eight_pixels_loops = width >> 3;
					for (i = 0; i < eight_pixels_loops; i++) {
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
					}
					for (i = 0; i < (width & 7); i++) {
						pixels[destination_index++] = pixels[(source_index += x_increment) >> 8];
					}
				}
			}
		}
		movieTime = (int) videoFrame.getPlaying_time();
		try {
			synchronized (this) {
				if (firstLoop) {
					while (buffering) {
						wait();
					}
					player_state = READY_STATE;
					update();
					firstLoop = false;
					if (autoplay) {
						autoplay = false;
						playerplay();
					}
					last_sleep_time = start_playing_time = System
							.currentTimeMillis();
				}
				current_frame++;
				while ((player_state != PLAY_STATE) || buffering) {
					long start_pause_time = System.currentTimeMillis();
					wait();
					start_playing_time += System.currentTimeMillis()
							- start_pause_time;
					last_sleep_time = playingTime;
				}
			}
			long pauseTime = movieTime - playingTime;
			if (pauseTime > 15) {
				last_sleep_time = playingTime;
				Thread.sleep(pauseTime - 10);
			} else if ((playingTime - last_sleep_time) > 250) {
				last_sleep_time = playingTime;
				if ((audioPlayer != null) && (audioPlayer.isDecoding())) {
					Thread.sleep(10);
				}
			}
		} catch (InterruptedException iex) {
			throw new InterruptedIOException(iex.getMessage());
		}

	}

	/**
	 * 关闭当前播放资源
	 * 
	 * 
	 */
	public void close() {
		playerstop();
		audioSamples = new Vector();
		videoSamples = new Vector();
		connection_speed = movieTime = -1;
		video_rate = movieLength = movieDuration = video_size = -1;
		last_sleep_time = start_playing_time = 0;
		Thread workThread = movieParseThread;
		movieParseThread = null;
		if (workThread != null) {
			workThread.interrupt();
			try {
				workThread.join(2000);
			} catch (Exception ex) {
			}
			workThread = null;
		}
		try {
			if (dataBuffer != null) {
				dataBuffer.stop();
				dataBuffer.clear();
			}
		} catch (Exception ex) {
		} finally {
			dataBuffer = null;
			LSystem.gc();
		}

	}

	private void update() {
		if (playPauseButton != null) {
			playPauseButton.setEnabled((player_state == PLAY_STATE)
					|| (player_state == PAUSE_STATE)
					|| ((player_state == READY_STATE) && (autoplay == false)));
		}
		if (stopButton != null) {
			stopButton.setEnabled((player_state == PLAY_STATE)
					|| (player_state == PAUSE_STATE)
					|| (player_state == STOP_STATE));
		}
	}

	/**
	 * 解析数据百分比
	 * 
	 * @param base
	 * @param sValue
	 * @param defaultValue
	 * @return
	 */
	private int parsePercentValue(int base, String sValue, int defaultValue) {
		int value = defaultValue;
		if (sValue != null) {
			try {
				if (sValue.charAt(sValue.length() - 1) == '%') {
					value = base
							* Integer.parseInt(sValue.substring(0, sValue
									.length() - 1)) / 100;
				} else {
					value = Integer.parseInt(sValue);
				}
			} catch (Exception ex) {
			}
			if (value < 0) {
				value = -value;
			}
		}
		return value;
	}

	/**
	 * 获得文件已播放时长信息
	 * 
	 * @return
	 */
	public String getPlayedDurationInfo() {
		return DateUtils.toMillisInfoString(videoDecoder.getBaseTime() * 1000);
	}

	/**
	 * 获得文件时长信息
	 * 
	 * @return
	 */
	public String getPlayDurationInfo() {
		return DateUtils.toMillisInfoString(videoDecoder.getDuration() * 1000);
	}

	/**
	 * 文件时长
	 * 
	 * @return
	 */
	public int getPlayDuration() {
		return videoDecoder.getDuration();
	}

	/**
	 * 文件已播放时间
	 * 
	 * @return
	 */
	public int getPlayTimer() {
		return videoDecoder.getBaseTime();
	}

	/**
	 * 设定播放在指定秒后自动停止
	 * 
	 * @param ms
	 */
	public void setMinuteAutoStop(long ms) {
		autoStop = ms;
	}

	/**
	 * 处理点击事件（请重载实现）
	 * 
	 */
	public void doClick() {
	}

	public void hideButtons() {
		this.playPauseButton.setVisible(false);
		this.stopButton.setVisible(false);
		this.muteButton.setVisible(false);
	}

	public void showButtons() {
		this.playPauseButton.setVisible(true);
		this.stopButton.setVisible(true);
		this.muteButton.setVisible(true);
	}

	protected void processMouseClicked() {
		if (showButtons) {
			if (this.input.getMouseReleased() == MouseEvent.BUTTON3) {
				if (!isClick) {
					this.hideButtons();
				} else {
					this.showButtons();
				}
				isClick = !isClick;
			}
		}
		this.doClick();

	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.VK_ENTER) {
			this.doClick();
		}
	}

	protected void processMouseDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getMouseDX(), this.input.getMouseDY());
		}
	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			if (timer.action(elapsedTime)) {
				if (autoStop > 0 && player_state == PLAY_STATE) {
					if (autoStop == getPlayTimer()) {
						playPauseButton.setImages(playPause2);
						playerpause();
						autoStop = -1;
					}
				}
				if (player_state != PLAY_STATE && wait.isRunning()) {
					wait.next();
				}
			}
		}
	}

	private void drawDownload(LGraphics g, int x, int y) {
		if (states == null) {
			return;
		}
		Color oldColor = g.getColor();
		g.setColor(Color.WHITE);
		String mes = "Download in progress : " + states.getPercent() + " %";
		int fh = g.getFont().getSize(), nh;
		g.drawString(mes, x + 5, nh = y + fh + 5);
		g.drawString("Download in speed : " + +states.getKB() + " KB", x + 5,
				nh = nh + fh + 5);
		g.drawString("Download file size : "
				+ +FileUtils.getKB(states.getLenght()) + " KB", x + 5, nh = nh
				+ fh + 5);
		states.getLenght();
		g.setColor(oldColor);

	}

	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (visible) {
			if (player_state != PLAY_STATE && wait.isRunning()) {
				wait.draw(g, x, y, w, h);
			} else {
				draw(g, x, y, w, h);
				if (!download) {
					return;
				}
				drawDownload(g, x, y);
			}
		}
	}

	public void dispose() {
		this.close();
		super.dispose();
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isComplete() {
		return complete;
	}

	public boolean isShowButtons() {
		return showButtons;
	}

	public void setShowButtons(boolean showButtons) {
		this.showButtons = showButtons;
	}

	public String getUIName() {
		return "Player";
	}

}
