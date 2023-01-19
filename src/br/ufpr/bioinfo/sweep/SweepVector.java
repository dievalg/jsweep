package br.ufpr.bioinfo.sweep;

/**
 * SweepVector vector encoded biological sequence.
 * @param <T> where T is id type.
 * 
 * @author Dieval Guizelini
 */
public interface SweepVector<T> {
    
    T getID();
    
    void setID(T value);
    
    double[] getDoubleValues();
    
    void setDoubleValues(double[] values);
    
}
