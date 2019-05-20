package fun.stgoder.live.stream.ctrl.view;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fun.stgoder.live.stream.core.Stream;
import fun.stgoder.live.stream.core.Streams;
import fun.stgoder.live.stream.core.exception.StreamException;
import fun.stgoder.live.stream.core.module.dto.StreamInfo;

@Controller
public class IndexView {

    @GetMapping({ "/", "" })
    public ModelAndView indexView(ModelAndView mv) {
        mv.setViewName("index");
        StreamInfo streamInfo = StreamInfo.build();
        mv.addObject("streamInfo", streamInfo);
        return mv;
    }

    @PostMapping("/stream/add")
    public ModelAndView streamAdd(@RequestParam("key") String key, @RequestParam("_addr") String _addr,
            @RequestParam("addr_") String addr_, ModelAndView mv)
            throws org.bytedeco.javacv.FrameGrabber.Exception, org.bytedeco.javacv.FrameRecorder.Exception {
        mv.setViewName("redirect:/");
        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null) {
            stream = new Stream(_addr, addr_);
            Streams.INSTANCE.Put(key, stream);
        }
        return mv;
    }

    @PostMapping("/stream/del")
    public ModelAndView streamDel(@RequestParam("key") String key, ModelAndView mv)
            throws StreamException, IOException {
        mv.setViewName("redirect:/");
        if (!Streams.INSTANCE.contains(key))
            throw new StreamException(404, "stream not found");
        Streams.INSTANCE.remove(key);
        return mv;
    }

    @PostMapping("/stream/grabber")
    public ModelAndView streamGrabberCtrl(@RequestParam("key") String key, @RequestParam("op") String op,
            ModelAndView mv) throws StreamException, org.bytedeco.javacv.FrameGrabber.Exception {
        mv.setViewName("redirect:/");

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

        return mv;
    }

    @PostMapping("/stream/pusher")
    public ModelAndView streamPusherCtrl(@RequestParam("key") String key, @RequestParam("op") String op,
            ModelAndView mv) throws org.bytedeco.javacv.FrameRecorder.Exception, StreamException {
        mv.setViewName("redirect:/");

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

        return mv;
    }

    @PostMapping("/stream/recorder/add")
    public ModelAndView streamRecoderAdd(@RequestParam("key") String key, @RequestParam("rkey") String rkey,
            ModelAndView mv) throws StreamException, org.bytedeco.javacv.FrameRecorder.Exception {
        mv.setViewName("redirect:/");

        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        if (stream.recorder().recorders().containsKey(rkey))
            throw new StreamException(500, "recorder existed");
        stream.addRecorder(rkey);

        return mv;
    }

    @PostMapping("/stream/recorder/del")
    public ModelAndView streamRecoderDel(@RequestParam("key") String key, @RequestParam("rkey") String rkey,
            ModelAndView mv) throws StreamException, org.bytedeco.javacv.FrameRecorder.Exception {
        mv.setViewName("redirect:/");

        Stream stream = Streams.INSTANCE.get(key);
        if (stream == null)
            throw new StreamException(404, "stream not found");
        stream.removeRecorder(rkey);

        return mv;
    }
}
