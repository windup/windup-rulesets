package com.test.project;

import static sun.misc.Unsafe.getUnsafe;

public class UnsafeRemovals {
    public static void main(String[] args) {
        Object obj = new Object();

        getUnsafe().getInt(0L);
        getUnsafe().getInt(obj, 1);
        getUnsafe().putInt(obj, 1, 1);
        getUnsafe().getObject(obj, 1);
        getUnsafe().putObject(obj, 1, obj);
        getUnsafe().getBoolean(obj, 1);
        getUnsafe().putBoolean(obj, 1, true);
        getUnsafe().getByte(obj, 1);
        getUnsafe().putByte(obj, 1, (byte) 0xFF);
        getUnsafe().getShort(obj, 1);
        getUnsafe().putShort(obj, 1, (byte) 0xFF);
        getUnsafe().getChar(obj, 1);
        getUnsafe().putChar(obj, 1, 'a');
        getUnsafe().getLong(obj, 1);
        getUnsafe().putLong(obj, 1, 0L);
        getUnsafe().getFloat(obj, 1);
        getUnsafe().putFloat(obj, 1, 0f);
        getUnsafe().getDouble(obj, 1);
        getUnsafe().putDouble(obj, 1, 2);
        getUnsafe().fieldOffset(null);
        getUnsafe().staticFieldBase(obj.getClass());
        getUnsafe().tryMonitorEnter(obj);
        getUnsafe().monitorExit(obj);
        getUnsafe().monitorEnter(obj);
    }
}
