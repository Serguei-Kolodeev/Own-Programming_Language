package com.annimon.ownlang.lib;

import com.annimon.ownlang.exceptions.UnknownPropertyException;
import java.util.Objects;

/**
 *
 * @author aNNiMON
 */
public final class StringValue implements Value {
    
    public static final StringValue EMPTY = new StringValue("");
    
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public Value access(Value propertyValue) {
        final String prop = propertyValue.asString();
        return switch (prop) {
            // Properties
            case "length" -> NumberValue.of(length());
            case "lower" -> new StringValue(value.toLowerCase());
            case "upper" -> new StringValue(value.toUpperCase());
            case "chars" -> {
                final Value[] chars = new Value[length()];
                int i = 0;
                for (char ch : value.toCharArray()) {
                    chars[i++] = NumberValue.of(ch);
                }
                yield new ArrayValue(chars);
            }

            // Functions
            case "trim" -> Converters.voidToString(value::trim);
            case "startsWith" -> new FunctionValue(args -> {
                    Arguments.checkOrOr(1, 2, args.length);
                    int offset = (args.length == 2) ? args[1].asInt() : 0;
                    return NumberValue.fromBoolean(value.startsWith(args[0].asString(), offset));
                });
            case "endsWith" -> Converters.stringToBoolean(value::endsWith);
            case "matches" -> Converters.stringToBoolean(value::matches);
            case "contains" -> Converters.stringToBoolean(value::contains);
            case "equalsIgnoreCase" -> Converters.stringToBoolean(value::equalsIgnoreCase);
            case "isEmpty" -> Converters.voidToBoolean(value::isEmpty);

            default -> {
                if (Functions.isExists(prop)) {
                    final Function f = Functions.get(prop);
                    yield new FunctionValue(args -> {
                        final Value[] newArgs = new Value[args.length + 1];
                        newArgs[0] = this;
                        System.arraycopy(args, 0, newArgs, 1, args.length);
                        return f.execute(newArgs);
                    });
                }
                throw new UnknownPropertyException(prop);
            }
        };
    }

    public int length() {
        return value.length();
    }
    
    @Override
    public int type() {
        return Types.STRING;
    }

    @Override
    public Object raw() {
        return value;
    }
    
    @Override
    public int asInt() {
        return Integer.parseInt(value);
    }
    
    @Override
    public double asNumber() {
        return Double.parseDouble(value);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final StringValue other = (StringValue) obj;
        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public int compareTo(Value o) {
        if (o.type() == Types.STRING) {
            return value.compareTo(((StringValue) o).value);
        }
        return asString().compareTo(o.asString());
    }
    
    @Override
    public String toString() {
        return asString();
    }
}
