package br.ufpr.bioinfo.sweep.io;

/**
 *
 * @author Dieval Guizelini
 */
public class FeatureFile {
    public final static int UNKNOW = 0;
    public final static int INPUT = 1;
    public final static int OUTPUT = 2;
    public final static int INOUT = 3;
    //
    public final static int ALL = 0;
    public final static int ONE_SEQUENCE = 1;
    public final static int MERGE_SEQUENCES = 2;
    
    //
    private java.io.File filename;
    private int kindOfStream = UNKNOW;
    private String format = null;
    
    public FeatureFile() {
    }
    
    public FeatureFile(java.io.File file) {
        this.filename = file;
    }

    public FeatureFile(java.io.File file, String fmt, int kind) {
        this.filename = file;
        this.format = fmt;
        this.kindOfStream = kind;
    }

    /**
     * @return the filename
     */
    public java.io.File getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(java.io.File filename) {
        this.filename = filename;
    }

    /**
     * @return the kindOfStream
     */
    public int getKindOfStream() {
        return kindOfStream;
    }

    /**
     * @param kindOfStream the kindOfStream to set
     */
    public void setKindOfStream(int kindOfStream) {
        this.kindOfStream = kindOfStream;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
    public int getStrategyLoader() {
        return ALL;
    }
    
}
