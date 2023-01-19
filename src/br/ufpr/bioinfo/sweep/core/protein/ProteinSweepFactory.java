package br.ufpr.bioinfo.sweep.core.protein;

import br.ufpr.bioinfo.sweep.SweepFactory;
import br.ufpr.bioinfo.sweep.SweepParameters;
import br.ufpr.bioinfo.sweep.core.SweepBase;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import br.ufpr.bioinfo.sweep.core.seed.SeedFactory;

/**
 *
 * @author Dieval Guizelini
 */
public class ProteinSweepFactory extends SweepFactory {

    public ProteinSweepFactory() {
        this.seedEncoder = ProteinSeedEncoder.getInstance();
        checkEncoder();
    }
    
    public ProteinSweepFactory(SweepParameters params) {
        super(params);
        this.seedEncoder = ProteinSeedEncoder.getInstance();
        checkEncoder();
    }

    private void checkEncoder() {
        if (this.seedEncoder == null || !(this.seedEncoder instanceof ProteinSeedEncoder)) {
            this.seedEncoder = new ProteinSeedEncoder();
        }
        String aa = new NCBIGeneticCode().getUniqueAminoacids(params.getNcbiGeneticCode());
        if (aa.contains("*")) {
            aa = aa.replaceAll("\\*","");
        }
        this.seedEncoder.setAlphabet(aa.getBytes());
    }

    @Override
    protected int numberOfRawCoords() {
        checkEncoder();
        if (this.mask == null) {
            this.mask = params.getMask();
        }
        int numOfSymbols = this.seedEncoder.getNumberOfSymbols();
        int tamEspaco = (int) Math.pow(numOfSymbols, mask.getSizeOfSubset());
        return tamEspaco;
    }

    @Override
    protected void reconfigure() {
        checkEncoder();
        if (this.mask == null) {
            this.mask = params.getMask(); //new SweepMask(DEFAULT_PROTEIN_MASK);
        }
        int numOfSymbols = this.seedEncoder.getNumberOfSymbols();
        int tamEspaco = (int) Math.pow(numOfSymbols, mask.getSizeOfSubset());
        SweepBase base = params.getBase();
        //
        if (base != null) {
            if (base.getSpaceSize() != tamEspaco || base.getProjectionSize() != this.projectionSize) {
                System.out.println("WARN: base-matrix reconfigured...");
                base = new SweepBase();
                base.setSpaceSize(tamEspaco);
                base.setProjectionSize(this.projectionSize);
            }
        } else {
            base = new SweepBase();
            base.setSpaceSize(tamEspaco);
            base.setProjectionSize(this.projectionSize);
        }

        //
        // verifica a compatibilidade das classes....
        // @TODO: precisa validar novamente a necessidade de ter seeds diferentes....
        /*if (!"br.ufpr.bioinfo.sweep.core.seed.BasicSeed".equals(this.seedClass.getName())
                && !"br.ufpr.bioinfo.sweep.core.seed.ProteinSeed".equals(this.seedClass.getName())) {
            this.seedClass = ProteinSeed.class;
        }*/
    }

    
    @Override
    protected SeedBuilder createAndConfigureSeedBuilder() {
        SeedBuilder seedBuilder = SeedFactory.getInstance(params);
        if( seedBuilder != null ) {
            return seedBuilder;
        }
        String maskStr = "11011";
        System.out.println("WARN: mask rewrited "+maskStr);
        mask = new SweepMask(maskStr);
        params.setMask(mask);
        seedBuilder = SeedFactory.getInstance(params);
        return seedBuilder;
    }
    
}