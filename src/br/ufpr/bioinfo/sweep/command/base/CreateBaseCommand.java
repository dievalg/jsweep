package br.ufpr.bioinfo.sweep.command.base;

import br.ufpr.bioinfo.sweep.SweepParameters;
import br.ufpr.bioinfo.sweep.command.SweepCommand;
import br.ufpr.bioinfo.sweep.core.SweepBase;
import br.ufpr.bioinfo.sweep.core.SweepBaseProjection;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;

/**
 *
 * @author Dieval Guizelini
 */
public class CreateBaseCommand extends SweepCommand {
    private int tamanhoBase=160000, tamanhoProj=600;
    private SweepBase base = null;
    
    public CreateBaseCommand() {
        
    }
    
    public CreateBaseCommand(SweepParameters params) {
        super(params);
    }

    @Override
    public boolean prepare() {
        // verifica os parâmetros para criar a base
        int tamAlfabeto = params.isAminoacidSequence() ? 20 : 4;
        SweepMask mask = params.getMask();
        int numBases = mask.getSizeOfSubset();
        if( numBases < 1 ) {
            numBases = 1;
        }
        this.tamanhoBase = (int)Math.pow(tamAlfabeto, numBases);
        this.tamanhoProj = params.getProjection();
        return( tamanhoBase>16 && tamanhoProj>20 );
    }

    @Override
    public boolean execute() {
        SweepBaseProjection proj = new SweepBaseProjection();
        this.base = proj.createRandomweepBase(tamanhoBase, tamanhoProj);
        return base != null && base.getBase() != null;
    }

    public SweepBase getBase() {
        return this.base;
    }
    
}