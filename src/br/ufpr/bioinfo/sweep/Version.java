package br.ufpr.bioinfo.sweep;

/**
 *
 * @author Dieval Guizelini
 */
public final class Version {
    private int major=0;
    private int minor=1;
    private int build=1;
    
    public Version() {
    }

    @Override
    public String toString() {
        return String.format(" version %d.%d.%d (alfa)",major,minor,build);
    }

    /**
     * @return the major
     */
    public int getMajor() {
        return major;
    }

    /**
     * @return the minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * @return the build
     */
    public int getBuild() {
        return build;
    }
    
    
    
}
