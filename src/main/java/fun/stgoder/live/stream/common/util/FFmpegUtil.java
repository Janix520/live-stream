package fun.stgoder.live.stream.common.util;

import java.io.File;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

public class FFmpegUtil {

    public static void main(String[] args) throws Exception {

        FrameGrabber grabber = FFmpegFrameGrabber
                .createDefault("rtsp://admin:a00000000@192.168.1.75:554/h264/ch1/main/av_stream");

        FrameRecorder recorder75 = recoder(new File("C:\\Users\\Administrator\\Desktop\\test\\75"));
        FrameRecorder recorder76 = recoder(new File("C:\\Users\\Administrator\\Desktop\\test\\76"));

        grabber.start();

        recorder75.start();
        recorder76.start();

        Frame f = null;
        while ((f = grabber.grabFrame()) != null) {
            recorder75.record(f);
            recorder76.record(f);
        }

        recorder75.stop();
        recorder76.stop();

        grabber.stop();
    }

    public static FrameRecorder recoder(File tmp) throws org.bytedeco.javacv.FrameRecorder.Exception {
        FrameRecorder recorder = FFmpegFrameRecorder.createDefault(tmp, 2560, 1440);
        recorder.setFormat("flv");
        // recorder.setVideoOption("crf", "25");
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setInterleaved(true);
        recorder.setVideoOption("preset", "ultrafast");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        recorder.setAudioChannels(1);
        // recorder.setAudioOption("crf", "0");
        recorder.setAudioQuality(0);
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        return recorder;
    }

}
