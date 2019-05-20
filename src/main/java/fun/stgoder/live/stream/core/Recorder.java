package fun.stgoder.live.stream.core;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;

import fun.stgoder.live.stream.common.Constants;
import fun.stgoder.live.stream.core.module.FrameRecorderWrapper;

public class Recorder implements Closeable {

    private Stream stream;

    private ConcurrentHashMap<String, FrameRecorderWrapper> recorders;

    public Recorder(Stream stream) {
        this.stream = stream;
        recorders = new ConcurrentHashMap<>();
    }

    public void record(Frame f) {
        Enumeration<FrameRecorderWrapper> elements = recorders.elements();
        while (elements.hasMoreElements()) {
            try {
                FrameRecorderWrapper wrapper = elements.nextElement();
                FrameRecorder recorder = wrapper.recorder();
                long startTime = wrapper.startTime();
                recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));
                recorder.record(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Recorder add(String key) throws org.bytedeco.javacv.FrameRecorder.Exception {
        if (recorders.containsKey(key))
            return this;
        FrameRecorder recorder = FFmpegFrameRecorder
                .createDefault(new File(Constants.VOD_RECORD_PATH + File.separator + key), 640, 360);
        recorder.setFormat("flv");
        recorder.setVideoOption("crf", "25");
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setInterleaved(true);
        recorder.setVideoOption("preset", "ultrafast");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        // recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);

        /*
         * recorder.setAudioChannels(1); recorder.setAudioOption("crf", "0");
         * recorder.setAudioQuality(0); recorder.setAudioBitrate(192000);
         * recorder.setSampleRate(44100);
         * recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
         */

        recorder.start();
        recorders.putIfAbsent(key, new FrameRecorderWrapper(recorder, System.currentTimeMillis()));

        return this;
    }

    public Recorder remove(String key) {
        FrameRecorderWrapper frameRecorderWrapper = recorders.get(key);
        if (frameRecorderWrapper != null) {
            try {
                FrameRecorder recorder = frameRecorderWrapper.recorder();
                recorders.remove(key);
                Thread.sleep(1000);
                recorder.close();
            } catch (org.bytedeco.javacv.FrameRecorder.Exception | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Stream stream() {
        return stream;
    }

    public Map<String, FrameRecorderWrapper> recorders() {
        return recorders;
    }

    @Override
    public void close() throws IOException {
        // copy to tmp map
        Map<String, FrameRecorderWrapper> tmp = new HashMap<>();
        tmp.putAll(recorders);

        // clear recorders
        recorders.clear();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // close each in tmp
        Iterator<FrameRecorderWrapper> iterator = tmp.values().iterator();
        while (iterator.hasNext()) {
            FrameRecorderWrapper frw = iterator.next();
            try {
                if (frw != null)
                    frw.recorder().close();
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
    }
}
