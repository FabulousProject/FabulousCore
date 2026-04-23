package net.infumia.lock;

final class RedisScripts {

    static final String DELETE_IF_OWNED =
        "local output = {}\n" +
        "for _, key in ipairs(KEYS) do\n" +
        "    if redis.call('get', key) == ARGV[1] then\n" +
        "        if redis.call('del', key) then\n" +
        "            table.insert(output, key)\n" +
        "        end\n" +
        "    end\n" +
        "end\n" +
        "return output".trim();
    static final String RENEW_IF_OWNED =
        "local output = {}\n" +
        "for _, key in ipairs(KEYS) do\n" +
        "    if redis.call('get', key) == ARGV[1] then\n" +
        "        if redis.call('pexpire', key, ARGV[2]) then\n" +
        "            table.insert(output, key)\n" +
        "        end\n" +
        "    end\n" +
        "end\n" +
        "return output".trim();
    static final String ACQUIRE_OR_RENEW_IF_OWNED =
        "local output = {}\n" +
        "for _, key in ipairs(KEYS) do\n" +
        "    local result = redis.call('get', key)\n" +
        "    if result == false then\n" +
        "        if redis.call('set', key, ARGV[1], 'px', ARGV[2]) then\n" +
        "            table.insert(output, key)\n" +
        "        end\n" +
        "    elseif result == ARGV[1] then\n" +
        "        if redis.call('pexpire', key, ARGV[2]) then\n" +
        "            table.insert(output, key)\n" +
        "        end\n" +
        "    end\n" +
        "end\n" +
        "return output".trim();
    static final String ACQUIRE =
            "local output = {}\n" +
                    "for _, key in ipairs(KEYS) do\n" +
                    "    local current = redis.call('get', key)\n" +
                    "    if current ~= 'CONSUMED' then\n" +
                    "        if redis.call('set', key, ARGV[1], 'NX', 'PX', ARGV[2]) ~= false then\n" +
                    "            table.insert(output, key)\n" +
                    "        end\n" +
                    "    end\n" +
                    "end\n" +
                    "return output";

    static final String CONSUME_IF_OWNED =
            "if redis.call('get', KEYS[1]) == ARGV[1] then\n" +
                    "    return redis.call('set', KEYS[1], 'CONSUMED', 'PX', ARGV[2])\n" +  // kısa TTL yeterli
                    "end\n" +
                    "return nil";


    private RedisScripts() {
        throw new IllegalStateException("Utility class");
    }
}
