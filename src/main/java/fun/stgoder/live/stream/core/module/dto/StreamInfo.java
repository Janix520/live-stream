package fun.stgoder.live.stream.core.module.dto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fun.stgoder.live.stream.core.Streams;
import fun.stgoder.live.stream.core.module.FrameRecorderWrapper;

public class StreamInfo {

    private List<Stream> streams;

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public static StreamInfo build() {
        StreamInfo info = new StreamInfo();
        List<Stream> list = new ArrayList<>();

        Map<String, fun.stgoder.live.stream.core.Stream> streams = Streams.INSTANCE.getAll();
        for (String skey : streams.keySet()) {
            fun.stgoder.live.stream.core.Stream stream = streams.get(skey);
            if (stream != null) {
                Stream item = new Stream();
                item.setKey(skey);
                item.set_addr(stream._addr());
                item.setAddr_(stream.addr_());
                item.setHttp(rtmpToHttp(stream.addr_()));

                Grabber grabber = new Grabber();
                grabber.setStarted(stream.grabber().started());
                item.setGrabber(grabber);

                Pusher pusher = new Pusher();
                pusher.setStarted(stream.pusher().started());
                item.setPusher(pusher);

                List<Recorder> recorders = new ArrayList<>();
                Map<String, FrameRecorderWrapper> rds = stream.recorder().recorders();
                for (String rkey : rds.keySet()) {
                    FrameRecorderWrapper frw = rds.get(rkey);
                    Recorder recorder = new Recorder();
                    recorder.setKey(rkey);
                    recorder.setStartTime(frw.startTime());
                    recorders.add(recorder);
                }
                item.setRecorders(recorders);

                list.add(item);
            }
        }

        info.setStreams(list);
        return info;
    }

    private static String rtmpToHttp(String rtmp) {
        rtmp = rtmp.replace("rtmp", "http");
        try {
            URL r = new URL(rtmp);
            String host = r.getHost();
            int port = 10001;
            String path = r.getPath();
            String query = r.getQuery();
            String h = "http://" + host + ":" + port + path + ".m3u8";
            if (StringUtils.isNotBlank(query))
                h += "?" + query;
            return h;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
