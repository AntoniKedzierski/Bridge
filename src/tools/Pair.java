package tools;

public class Pair<KEY_TYPE, VALUE_TYPE> {
    private final KEY_TYPE key;
    private final VALUE_TYPE value;

    public Pair(KEY_TYPE key, VALUE_TYPE value) {
        this.key = key;
        this.value = value;
    }

    public KEY_TYPE getKey() { return key; }
    public VALUE_TYPE getValue() { return value; }
}
