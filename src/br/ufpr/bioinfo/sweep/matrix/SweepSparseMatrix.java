package br.ufpr.bioinfo.sweep.matrix;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dieval Guizelini
 */
public class SweepSparseMatrix<T, V extends Number> implements Serializable {

    private Map<T, V> values = null;

    public SweepSparseMatrix() {
        values = new HashMap<T, V>(600);  // tamanho médio das sequencias aa
    }

    public SweepSparseMatrix(int size) {
        values = new HashMap<T, V>(size);  // tamanho médio das sequencias aa
    }

    public void setValue(T pos, V value) {
        values.put(pos, value);
    }

    public V get(T pos) {
        return values.get(pos);
    }

    public java.util.List<T> getKeys() {
        java.util.List<T> keys = new java.util.ArrayList<T>(values.keySet());
        return keys;
    }

    public int size() {
        if (values == null) {
            return 0;
        }
        return values.size();
    }
}
