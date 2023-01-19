package br.ufpr.bioinfo.sweep.core;

import java.util.Arrays;

/**
 *
 * @author Dieval Guizelini
 */
public class SweepBase {
    private int spaceSize = 160000; // 20^2 * 20^^2 = 400 x 400 = 160.000
    private int projectionSize = 600;
    private double[][] base = null;   // matriz de 160k x 600 
    //
    private java.io.File baseFilename = null;
    
    public SweepBase() {
    }
    
    public SweepBase(int size, int proj) {
        this.spaceSize = size;
        this.projectionSize = proj;
    }

    /**
     * @return the spaceSize
     */
    public int getSpaceSize() {
        return spaceSize;
    }

    /**
     * @param spaceSize the spaceSize to set
     */
    public void setSpaceSize(int spaceSize) {
        this.spaceSize = spaceSize;
    }

    /**
     * @return the projectionSize
     */
    public int getProjectionSize() {
        return projectionSize;
    }

    /**
     * @param projectionSize the projectionSize to set
     */
    public void setProjectionSize(int projectionSize) {
        this.projectionSize = projectionSize;
    }

    /**
     * @return the base
     */
    public double[][] getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(double[][] base) {
        this.base = base;
    }

    /**
     * @return the baseFilename
     */
    public java.io.File getBaseFilename() {
        return baseFilename;
    }

    /**
     * @param baseFilename the baseFilename to set
     */
    public void setBaseFilename(java.io.File baseFilename) {
        this.baseFilename = baseFilename;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.spaceSize;
        hash = 37 * hash + this.projectionSize;
        hash = 37 * hash + Arrays.deepHashCode(this.base);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SweepBase other = (SweepBase) obj;
        if (this.spaceSize != other.spaceSize) {
            return false;
        }
        if (this.projectionSize != other.projectionSize) {
            return false;
        }
        return Arrays.deepEquals(this.base, other.base);
    }
}