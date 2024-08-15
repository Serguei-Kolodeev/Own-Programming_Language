package com.annimon.ownlang.modules.okhttp;

import com.annimon.ownlang.exceptions.OwnLangRuntimeException;
import com.annimon.ownlang.lib.*;
import okhttp3.*;
import java.util.concurrent.TimeUnit;

public class HttpClientBuilderValue extends MapValue {

    private final OkHttpClient.Builder builder;

    public HttpClientBuilderValue(OkHttpClient.Builder builder) {
        super(6);
        this.builder = builder;
        init();
    }

    private void init() {
        set("callTimeout", timeout(builder::callTimeout));
        set("connectTimeout", timeout(builder::connectTimeout));
        set("readTimeout", timeout(builder::readTimeout));
        set("writeTimeout", timeout(builder::writeTimeout));
        set("retryOnConnectionFailure", args -> {
            builder.retryOnConnectionFailure(args[0].asInt() != 0);
            return this;
        });
        set("build", args -> new HttpClientValue(builder.build()));
    }

    private FunctionValue timeout(Timeout timeout) {
        return new FunctionValue(args -> {
            Arguments.check(2, args.length);
            long duration = args[0].type() == Types.NUMBER
                    ? ((NumberValue) args[0]).asLong()
                    : args[0].asInt();
            TimeUnit unit = switch (args[1].asString().toLowerCase()) {
                case "millis", "milliseconds" -> TimeUnit.MILLISECONDS;
                case "seconds" -> TimeUnit.SECONDS;
                case "minutes" -> TimeUnit.MINUTES;
                case "hours" -> TimeUnit.HOURS;
                case "days" -> TimeUnit.DAYS;
                default -> throw new OwnLangRuntimeException("Unknown unit type");
            };
            timeout.timeout(duration, unit);
            return this;
        });
    }

    interface Timeout {
        void timeout(long duration, TimeUnit unit);
    }
}
