package fun.stgoder.live.stream.ctrl.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fun.stgoder.live.stream.core.Stream;
import fun.stgoder.live.stream.core.Streams;
import fun.stgoder.live.stream.core.exception.StreamException;
import fun.stgoder.live.stream.core.module.dto.StreamInfo;
import fun.stgoder.live.stream.model.Resp;

@RestController
@RequestMapping("/rest/stream")
public class StreamCtrl {

    @PostMapping("/{key}")
    public Resp streamAdd(@PathVariable("key") String key, @RequestParam("_addr") String _addr,
            @RequestParam("addr_") String addr_)
            throws org.bytedeco.javacv.FrameGrabber.Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        // rtsp://admin:a00000000@192.168.1.75:554/h264/ch3/main/av_stream
        // rtmp://117.50.18.97:10005/hls/75
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null) {
            stream = new Stream(_addr, addr_);
            Streams.INSTANCE.Put(key, stream);
        }
        return new Resp(200, "success", key);
    }

    @GetMapping("/info")
    public Resp streams() {
        return new Resp(200, "success", StreamInfo.build());
    }

    @PutMapping("/{key}/grabber")
    public Resp streamGrabberCtrl(@PathVariable("key") String key, @RequestParam("op") String op) throws Exception {
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        if ("start".equals(op)) {
            stream.startGrabber();
        } else if ("stop".equals(op)) {
            stream.stopGrabber();
        } else {
            throw new StreamException(400, "op not support");
        }
        return new Resp(200, op + " grabber success", stream.grabber().started());
    }

    @PutMapping("/{key}/pusher")
    public Resp streamPusherCtrl(@PathVariable("key") String key, @RequestParam("op") String op) throws Exception {
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        if ("start".equals(op)) {
            stream.startPusher();
        } else if ("stop".equals(op)) {
            stream.stopPusher();
        } else {
            throw new StreamException(400, "op not support");
        }
        return new Resp(200, op + " pusher success", stream.pusher().started());
    }

    @PostMapping("/{key}/recorder")
    public Resp streamRecoderAdd(@PathVariable("key") String key, @RequestParam("rkey") String rkey) throws Exception {
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        if (stream.recorder().recorders().containsKey(rkey))
            throw new StreamException(500, "recorder existed");
        stream.addRecorder(rkey);
        return new Resp(200, "success", key + "-" + rkey);
    }

    @GetMapping("/{key}/recorders")
    public Resp streamRecoderAll(@PathVariable("key") String key) throws StreamException {
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        return new Resp(200, "success", stream.recorder().recorders().keySet());
    }

    @DeleteMapping("/{key}/recorder/{rkey}")
    public Resp streamRecoderRemove(@PathVariable("key") String key, @PathVariable("rkey") String rkey)
            throws StreamException {
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        stream.removeRecorder(rkey);
        return new Resp(200, "success", key + "-" + rkey);
    }

    @DeleteMapping("/{key}")
    public Resp streamDel(@PathVariable("key") String key) throws Exception {
        if (!Streams.INSTANCE.contains(key))
            throw new StreamException(404, "stream not found");
        Streams.INSTANCE.remove(key);
        return new Resp(200, "success", key);
    }
}
