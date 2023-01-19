package br.ufpr.bioinfo.sweep.core.nucleotide;

import br.ufpr.bioinfo.sweep.SweepFactory;
import br.ufpr.bioinfo.sweep.SweepParameters;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import br.ufpr.bioinfo.sweep.core.seed.SeedFactory;

/**
 *
 * @author Dieval Guizelini
 */
public class NucleotideSweepFactory extends SweepFactory {

    public NucleotideSweepFactory() {
        this.seedEncoder = new NucleotideSeedEncoder();
    }
    
    public NucleotideSweepFactory(SweepParameters params) {
        super(params);
        this.seedEncoder = new NucleotideSeedEncoder();
        //this.seedMap = new 
    }
    
    @Override
    protected void reconfigure() {
    }

    @Override
    protected SeedBuilder createAndConfigureSeedBuilder() {
        SeedBuilder seedBuilder = SeedFactory.getInstance(params);
        if( seedBuilder != null ) {
            return seedBuilder;
        }
        String maskStr = "11011101111";
        System.out.println("WARN: mask rewrited "+maskStr);
        mask = new SweepMask(maskStr);
        params.setMask(mask);
        seedBuilder = SeedFactory.getInstance(params);
        return seedBuilder;
    }

    @Override
    protected int numberOfRawCoords() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
