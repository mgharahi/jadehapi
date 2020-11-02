package ir.jadeh.api.services;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class Redis {

    private static final String uri = "redis://localhost?timeout=3600s";
    private static final long ttl = 120000;

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> syncCommands;

    public Redis() {
        redisClient = RedisClient.create(uri);
        connection = redisClient.connect();
        syncCommands = connection.sync();
    }

    public void shutdown() {
        connection.close();
        redisClient.shutdown();
    }

    public void set(String key, String value, long ttl) {

        SetArgs setArgs = new SetArgs();
        setArgs.px(ttl);

        syncCommands.set(key, value, setArgs);

    }

    public void set(String key, String value) {
        set(key, value, ttl);
    }

    public String get(String key) {
        return syncCommands.get(key);
    }

    public void setAndShutdown(String key, String value) {
        set(key, value);
        shutdown();
    }


    public String getAndShutdown(String key) {
        String value = get(key);
        shutdown();
        return value;
    }
}
