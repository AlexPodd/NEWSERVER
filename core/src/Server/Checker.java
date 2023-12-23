package Server;

import java.util.*;

public class Checker implements Map {
    private Hashtable<Integer, Boolean> receivedMessages;
    private int LastZapros;
    public Checker() {
        receivedMessages = new Hashtable<>();
        LastZapros = 0;
    }

    public int getLastZapros() {
        return LastZapros;
    }

    @Override
    public int size() {
       return receivedMessages.size();
    }

    @Override
    public boolean isEmpty() {
        return receivedMessages.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return receivedMessages.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return receivedMessages.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return receivedMessages.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        if((Integer)key > LastZapros){
            LastZapros = (Integer)key;
        }
        return receivedMessages.put((Integer) key, (Boolean) value);
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry> entrySet() {
        return null;
    }

}
