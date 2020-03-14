package ru.itmo.java;

public class HashTable {
    private class Entry {
        private Object key, value;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        Object getKey() {
            return key;
        }

        Object getValue() {
            return value;
        }
    }

    private int size, count;
    private Entry[] array;
    private double loadFactor;

    HashTable(int initialCapacity) {
        this(initialCapacity, 0.5);
    }

    HashTable(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.size = initialCapacity;
        this.array = new Entry[initialCapacity];
        this.count = 0;
    }

    private int getHashIndex(Object object) {
        return (object.hashCode() % size + size) % size;
    }

    private int getNextHashIndex(int index) {
        return (index + 12345) % size;
    }

    private void optimizeTable() {
        if (count == (int) (loadFactor * size)) {
            Entry[] oldarray = array;
            this.array = new Entry[size * 2];
            this.size = size * 2;
            this.count = 0;

            for (Entry entry : oldarray) {
                if (entry != null && entry.getKey() != null) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private int getIndex(Object key) {
        int index = getHashIndex(key);
        while (array[index] != null && (array[index].getKey() == null || !array[index].getKey().equals(key))) {
            index = getNextHashIndex(index);
        }
        return index;
    }

    private Object getKeyUsingIndex(int index) {
        if (array[index] == null) {
            return null;
        }
        return array[index].getKey();
    }

    private Object getValueUsingIndex(int index) {
        if (array[index] == null) {
            return null;
        }
        return array[index].getValue();
    }

    private Object putInIndex(int index, Object key, Object value) {
        if (array[index] == null) {
            int tmpindex = getHashIndex(key);
            while (array[tmpindex] != null && array[tmpindex].getKey() != null) {
                tmpindex = getNextHashIndex(tmpindex);
            }
            index = tmpindex;
        }

        Object prevValue = getValueUsingIndex(index);
        if (getKeyUsingIndex(index) == null) {
            count += 1;
        }
        array[index] = new Entry(key, value);
        return prevValue;
    }

    private Object removeInIndex(int index) {
        if (array[index] == null) {
            return null;
        }

        Object prevValue = getValueUsingIndex(index);
        if (array[index].getKey() != null) {
            count -= 1;
        }
        array[index] = new Entry(null, null);
        return prevValue;
    }


    Object put(Object key, Object value) {
        optimizeTable();
        return putInIndex(getIndex(key), key, value);
    }

    Object get(Object key) {
        return getValueUsingIndex(getIndex(key));
    }

    Object remove(Object key) {
        optimizeTable();
        return removeInIndex(getIndex(key));
    }

    int size() {
        return count;
    }
}