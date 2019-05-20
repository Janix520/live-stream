package fun.stgoder.live.stream.core.module.dto;

import java.util.List;

public class Stream {

    private String key;

    private String _addr;

    private String addr_;

    private String http;

    private Grabber grabber;

    private Pusher pusher;

    private List<Recorder> recorders;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Grabber getGrabber() {
        return grabber;
    }

    public void setGrabber(Grabber grabber) {
        this.grabber = grabber;
    }

    public Pusher getPusher() {
        return pusher;
    }

    public void setPusher(Pusher pusher) {
        this.pusher = pusher;
    }

    public List<Recorder> getRecorders() {
        return recorders;
    }

    public void setRecorders(List<Recorder> recorders) {
        this.recorders = recorders;
    }

    public String get_addr() {
        return _addr;
    }

    public void set_addr(String _addr) {
        this._addr = _addr;
    }

    public String getAddr_() {
        return addr_;
    }

    public void setAddr_(String addr_) {
        this.addr_ = addr_;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

}
