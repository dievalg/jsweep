package br.ufpr.bioinfo.sweep.io;

import br.ufpr.bioinfo.sweep.core.SweepBase;
import java.io.Serializable;

/**
 *
 * @author Dieval Guizelini
 */
public class BaseRecord implements Serializable {
    private byte[] magic = new byte[]{'b','s','w','p'};
    private int version = 1;
    private int app = 2;
    private long data = 0L;
    private int sizeSpace = 160000;
    private int projectionSize = 600;
    private double[][] values;
    
    public BaseRecord() {
    }
    
    public BaseRecord(SweepBase base) {
        setBase(base);
    }
    
    public final void setBase(SweepBase base) {
        this.sizeSpace = base.getSpaceSize();
        this.projectionSize = base.getProjectionSize();
        this.values = base.getBase();
        java.util.Date now = new java.util.Date();
        this.data = now.getTime();
    }

    /**
     * @return the magic
     */
    public byte[] getMagic() {
        return magic;
    }

    /**
     * @param magic the magic to set
     */
    public void setMagic(byte[] magic) {
        this.magic = magic;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the app
     */
    public int getApp() {
        return app;
    }

    /**
     * @param app the app to set
     */
    public void setApp(int app) {
        this.app = app;
    }

    /**
     * @return the data
     */
    public long getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(long data) {
        this.data = data;
    }

    /**
     * @return the sizeSpace
     */
    public int getSizeSpace() {
        return sizeSpace;
    }

    /**
     * @param sizeSpace the sizeSpace to set
     */
    public void setSizeSpace(int sizeSpace) {
        this.sizeSpace = sizeSpace;
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
     * @return the values
     */
    public double[][] getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(double[][] values) {
        this.values = values;
    }
    
}
