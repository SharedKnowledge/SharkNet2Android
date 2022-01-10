package net.sharksystem.android.util;

import net.sharksystem.SharkException;

import java.util.HashMap;
import java.util.Map;

public class ObjectHolder {
    private static ObjectHolder instance = null;
    private Map<CharSequence, Object> memory = new HashMap<>();

    public static ObjectHolder getObjectHolder() {
        if(ObjectHolder.instance == null) {
            ObjectHolder.instance = new ObjectHolder();
        }

        return ObjectHolder.instance;
    }

    public void setObject(CharSequence key, Object object) {
        this.memory.put(key, object);
    }

    public Object getAndRemoveObject(CharSequence key) throws SharkException {
        Object retval = this.memory.get(key);

        if(retval == null) throw new SharkException("no object with this key");

        this.memory.remove(key);
        return retval;
    }
}
