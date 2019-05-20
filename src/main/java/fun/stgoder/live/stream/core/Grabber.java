package fun.stgoder.live.stream.core;

import java.io.Closeable;
import java.io.IOException;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

public class Grabber implements Closeable {

    private Stream stream;

    private FrameGrabber grabber;

    private GrabbingThread gt;

    private volatile boolean started;

    public Grabber(Stream stream) throws org.bytedeco.javacv.FrameGrabber.Exception {
        this.stream = stream;
        grabber = FFmpegFrameGrabber.createDefault(stream._addr());
        grabber.setOption("rtsp_transport", "tcp");
        // grabber.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);

        gt = new GrabbingThread(stream);
        gt.start();
    }

    public void startGrabbing(Stream stream) {
        if (started)
            return;
        try {
            grabber.start();
            started = true;
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
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
            grabber.stop();
        } catch (org.bytedeco.javacv.FrameGrabber.Exception | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Stream stream() {
        return stream;
    }

    public FrameGrabber fg() {
        return grabber;
    }

    public boolean started() {
        return started;
    }

    @Override
    public void close() throws IOException {
        if (!gt.isInterrupted())
            gt.interrupt();
        if (grabber != null)
            grabber.close();
        started = false;
    }

    public static void main(String[] args) throws java.lang.Exception {
        FFmpegFrameGrabber default1 = FFmpegFrameGrabber
                .createDefault("rtsp://admin:a00000000@192.168.1.75:554/h264/ch3/main/av_stream");
        default1.start();
        default1.stop();
        default1.start();
        default1.stop();
    }
}

class GrabbingThread extends Thread {

    private Stream stream;

    public GrabbingThread(Stream stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            // System.out.println(2);
            try {
                FrameGrabber fg = stream.grabber().fg();
                Frame f = null;
                while (stream.grabber().started() && (f = fg.grabFrame()) != null) {
                    // System.out.println(1);
                    stream.pusher().push(f);
                    stream.recorder().record(f);
                }
            } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                e.printStackTrace();
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(3);
    }
}
