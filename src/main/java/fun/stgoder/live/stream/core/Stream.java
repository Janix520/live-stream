package fun.stgoder.live.stream.core;

import java.io.Closeable;
import java.io.IOException;

import org.bytedeco.ffmpeg.global.avutil;

public class Stream implements Closeable {

    private String _addr;

    private String addr_;

    private Grabber grabber;

    private Pusher pusher;

    private Recorder recorder;

    public Stream(String _addr, String addr_)
            throws org.bytedeco.javacv.FrameGrabber.Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        this._addr = _addr;
        this.addr_ = addr_;
        this.grabber = new Grabber(this);
        this.pusher = new Pusher(this);
        this.recorder = new Recorder(this);
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
    }

    public Stream startGrabber() throws org.bytedeco.javacv.FrameGrabber.Exception {
        grabber.startGrabbing(this);
        return this;
    }

    public Stream stopGrabber() throws org.bytedeco.javacv.FrameGrabber.Exception {
        grabber.stop();
        return this;
    }

    public Stream startPusher() throws org.bytedeco.javacv.FrameRecorder.Exception {
        pusher.start();
        return this;
    }

    public Stream stopPusher() throws org.bytedeco.javacv.FrameRecorder.Exception {
        pusher.stop();
        return this;
    }

    public Stream addRecorder(String key) throws org.bytedeco.javacv.FrameRecorder.Exception {
        this.recorder.add(key);
        return this;
    }

    public Stream removeRecorder(String key) {
        this.recorder.remove(key);
        return this;
    }

    public String _addr() {
        return _addr;
    }

    public String addr_() {
        return addr_;
    }

    public Grabber grabber() {
        return grabber;
    }

    public Pusher pusher() {
        return pusher;
    }

    public Recorder recorder() {
        return recorder;
    }

    @Override
    public void close() throws IOException {
        grabber.close();
        pusher.close();
        recorder.close();
    }
}
