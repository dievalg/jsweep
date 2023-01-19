package br.ufpr.bioinfo.sweep.command;

import br.ufpr.bioinfo.sweep.SweepParameters;

/**
 * <p>
 * abstracts basic console operations and creates a simplified facade for
 * integration with other applications.</p>
 *
 * @author Dieval Guizelini
 * @version 1.2
 */
public abstract class SweepCommand {
    protected SweepParameters params=null;
    private int verboseMode = 0;   // 0 silently
    
    public SweepCommand() {
    }
    
    public SweepCommand(SweepParameters params) {
        this.params = params;
    }
    
    public abstract boolean prepare();
    
    public abstract boolean execute();
    
    public SweepParameters getParameters() {
        return this.params;
    }
    
}