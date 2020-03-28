package tools;

import java.util.Vector;

public class Map<KEYS, VALUES> {
    private Vector<Pair<KEYS, VALUES>> map;

    public Map() {
        map = new Vector<>();
    }

    public boolean containsKey(KEYS key) {
        for (int i = 0; i < map.size(); ++i) {
            if (key == map.get(i).getKey()) return true;
        }
        return false;
    }

    public boolean put(KEYS key, VALUES value) {
        if (containsKey(key)) return false;
        map.add(new Pair<KEYS, VALUES>(key, value));
        return true;
    }

    public VALUES getValue(KEYS key) {
        for (Pair<KEYS, VALUES> x: map) {
            if (key == x.getKey()) return x.getValue();
        }
        return null;
    }

    public Pair<KEYS, VALUES> getPair(int index) {
        if (index < 0 || index >= map.size()) return null;
        return map.get(index);
    }

    @Override
    public String toString() {
        String result = "";
        for (Pair<KEYS, VALUES> x: map) {
            result += x.getKey().toString() + ": " + x.getValue().toString() + "\n";
        }
        return result;
    }
}
