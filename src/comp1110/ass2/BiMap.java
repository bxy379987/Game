package comp1110.ass2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class BiMap<K, V> {
    private Map<K, V> forwardMap = new HashMap<>();
    private Map<V, K> reverseMap = new HashMap<>();
    public void put(K key, V value) {
        forwardMap.put(key, value);
        reverseMap.put(value, key);
    }

    public Optional<Object> get(Object key) {
        if (forwardMap.containsKey(key)) return Optional.of(forwardMap.get(key));
        if (reverseMap.containsKey(key)) return Optional.of(reverseMap.get(key));
        return Optional.empty();
    }

    public Optional<Object> remove(Object key) {
        if (forwardMap.containsKey(key)) return Optional.of(forwardMap.remove(key));
        if (reverseMap.containsKey(key)) return Optional.of(reverseMap.remove(key));
        return Optional.empty();
    }

    public void clear() {
        forwardMap.clear();
        reverseMap.clear();
    }
}
