package com.annimon.ownlang.modules.functional;

import com.annimon.ownlang.lib.Function;
import com.annimon.ownlang.lib.FunctionValue;
import com.annimon.ownlang.lib.Value;
import com.annimon.ownlang.modules.Module;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aNNiMON
 */
public final class functional implements Module {

    @Override
    public Map<String, Value> constants() {
        return Map.of("IDENTITY", new FunctionValue(args -> args[0]));
    }

    @Override
    public Map<String, Function> functions() {
        final var result = new HashMap<String, Function>(15);
        result.put("foreach", new functional_foreach());
        result.put("map", new functional_map());
        result.put("flatmap", new functional_flatmap());
        result.put("reduce", new functional_reduce());
        result.put("filter", new functional_filter(false));
        result.put("sortby", new functional_sortby());
        result.put("takewhile", new functional_filter(true));
        result.put("dropwhile", new functional_dropwhile());

        result.put("chain", new functional_chain());
        result.put("stream", new functional_stream());
        result.put("combine", new functional_combine());
        return result;
    }
}
