package br.ufpr.bioinfo.sweep.core;

import br.ufpr.bioinfo.sweep.SweepVector;
import br.ufpr.bioinfo.sweep.matrix.SweepSparseMatrix;

/**
 *
 * @author Dieval Guizelini
 */
public class SparseSweepVector <T> implements SweepVector<T> {
    /** @value id external identifier. */
    private T id;
    
    /** @value values is the high-dimensional vector projected into low-dimensionality.*/
    private SweepSparseMatrix<Integer,Double> values;
    //
    private int size=600;
    
    public SparseSweepVector() {
        
    }
    
    @Override
    public double[] getDoubleValues() {
        double[] result = new double[size];
        for(Integer k : values.getKeys() ) {
            if( k>=0 && k<size ) {
                result[k] = values.get(k);
            }
        }
        return result;
    }
    
    @Override
    public void setDoubleValues(double[] values) {
        this.values = new SweepSparseMatrix<Integer,Double>(values.length);
        int contar=0;
        for(int i=0 ; i<values.length ; i++ ) {
            if( values[i] > 0.00001 && values[i] < -0.00001 ) {
                this.values.setValue(i, values[i]);
                contar++;
            }
        }
        if( contar>(size*0.5) ) {
            if( id != null ) {
                System.out.println("WARN: SparceMatrix used in dense matrix. ID="+id.toString());
            } else {
                System.out.println("WARN: SparceMatrix used in dense matrix.");
            }
        }
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