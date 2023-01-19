package br.ufpr.bioinfo.sweep.core;

import br.ufpr.bioinfo.sweep.SweepVector;

/**
 *
 * @author Dieval Guizelini
 */
public abstract class DoubleSweepVector<T> implements SweepVector<T> {
    /** @value id external identifier. */
    private T id;
    
    /** @value values is the high-dimensional vector projected into low-dimensionality.*/
    private double[] values = null;
    
    public DoubleSweepVector() {
        
    }
    
    @Override
    public double[] getDoubleValues() {
        return this.values;
    }
    
    @Override
    public void setDoubleValues(double[] values) {
        this.values = values;
    }
    
    @Override
    public T getID() {
        return this.id;
    }
    
    @Override
    public void setID(T value) {
        this.id = value;
    }
    
}