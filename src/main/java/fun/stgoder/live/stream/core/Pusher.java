package fun.stgoder.live.stream.core;

import java.io.Closeable;
import java.io.IOException;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder;

public class Pusher implements Closeable {

    private Stream stream;

    private FrameRecorder pusher;

    private volatile boolean started;

    public Pusher(Stream stream) throws org.bytedeco.javacv.FrameRecorder.Exception {
        this.stream = stream;
    }

    public void start() {
        if (started)
            return;
        try {
            pusher = FFmpegFrameRecorder.createDefault(stream.addr_(), 640, 360);

            pusher.setInterleaved(true);
            pusher.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            pusher.setFormat("flv");
            pusher.setFrameRate(25);
            // pusher.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // yuv420p

            pusher.start();
            started = true;
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
            started = false;
        }
    }

    public void stop() {
        if (!started)
            return;
        try {
            started = false;
            Thread.sleep(1000);
            pusher.close();
        } catch (InterruptedException | org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }

    public void push(Frame f) throws org.bytedeco.javacv.FrameRecorder.Exception {
        if (started)
            pusher.record(f);
    }

    public Stream stream() {
        return stream;
    }

    public boolean started() {
        return started;
    }

    @Override
    public void close() throws IOException {
        if (pusher != null)
            pusher.close();
    }
}
