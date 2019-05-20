package fun.stgoder.live.stream.core.module;

import org.bytedeco.javacv.FrameRecorder;

public class FrameRecorderWrapper {

    private long startTime;

    private FrameRecorder recorder;

    public FrameRecorderWrapper(FrameRecorder recorder, long startTime) {
        this.recorder = recorder;
        this.startTime = startTime;
    }

    public long startTime() {
        return startTime;
    }

    public FrameRecorder recorder() {
        return recorder;
    }
}
