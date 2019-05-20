package fun.stgoder.live.stream.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Streams {

    INSTANCE;

    private ConcurrentHashMap<String, Stream> streams;

    {
        streams = new ConcurrentHashMap<>();
    }

    public Stream Put(String key, Stream stream) {
        return streams.putIfAbsent(key, stream);
    }

    public Stream get(String key) {
        return streams.get(key);
    }

    public boolean contains(String key) {
        return streams.containsKey(key);
    }

    public void remove(String key) throws IOException {
        Stream stream = streams.get(key);
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw e;
            }
        }
        streams.remove(key);
    }

    public Map<String, Stream> getAll() {
        Map<String, Stream> tmp = new HashMap<>();
        tmp.putAll(streams);
        return tmp;
    }
}
