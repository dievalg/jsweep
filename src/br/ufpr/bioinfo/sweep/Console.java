package br.ufpr.bioinfo.sweep;

import br.ufpr.bioinfo.commons.utils.ArraysUtils;
import br.ufpr.bioinfo.commons.utils.FileUtils;
import br.ufpr.bioinfo.fasta.Fasta;
import br.ufpr.bioinfo.sweep.core.SweepBase;
import br.ufpr.bioinfo.sweep.core.SweepBaseProjection;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.io.FeatureFile;
import br.ufpr.bioinfo.sweep.io.IOBase;
import br.ufpr.bioinfo.sweep.io.SequenceLoader;
import java.util.List;

/**
 *
 * -t nt -p 100 -m 11011 -cb -o D:\projetos\sweep\nt_m11011_p100.bswp -expb txt D:\projetos\sweep\nt_m11011_p100.txt -expb json D:\projetos\sweep\nt_m11011_p100.json


-t aa -p 100 -m 11011 -cb -o D:\projetos\sweep\aa_m11011_p100.bswp -expb txt D:\projetos\sweep\aa_m11011_p100.txt -expb json D:\projetos\sweep\aa_m11011_p100.json


-t nt -p 100 -m 11011 -cb D:\projetos\sweep\nt_m11011_p100.bswp 
-i D:\projetos\sweep\t1ss_2vec.fas -o D:\projetos\sweep\t1ss_2.swp -exp txt D:\projetos\sweep\t1ss_2vec.txt

 * 
 * 
 * @author Dieval Guizelini
 */
public class Console {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean silently = false;
        if (args.length < 1 || ArraysUtils.contain(new String[]{"-?", "/?", "/help", "-help"}, args)) {
            showApp();
            usage();
            System.exit(0);
        }
        silently = ArraysUtils.contain(new String[]{"-s", "/s", "/silently"}, args);
        if (!silently) {
            showApp();
        }
        SweepParameters params = new SweepParameters(args);
        //
        // para executar o sweep é necessário ter uma base de projeção
        if( params.isCreateBase() && params.hasBaseOutputFile() ) {
            createBase(params);
        } else {
            loadBase(params);
        }
        //
        // cria os vetores para representar as sequências
        if( params.getInputFileList() != null && params.getInputFileList().size()>0 ) {
            SweepFactory fac = SweepFactory.getInstance(params);
            java.util.List<SweepVector<String>> vetores = new java.util.ArrayList<SweepVector<String>>();
            
            //
            java.util.List<FeatureFile> inputList = params.getInputFileList();
            for(FeatureFile ff : inputList ) {
                if( ff.getStrategyLoader() == FeatureFile.ALL ) {
                    List<Fasta> fastasList = SequenceLoader.load(ff.getFilename());
                    for(Fasta f : fastasList ) {
                        System.out.println(f.getHeader());
                    }
                    List<SweepVector<String>> auxList = fac.create(fastasList);
                    vetores.addAll(auxList);
                }
            }
        }
    }

    private static void showApp() {
        System.out.print("jSWeeP tools                       ");
        System.out.println(new Version().toString());
        System.out.println("Author: Dieval Guizelini\n\n");
    }

    private static void usage() {
        System.out.println("jSWeeP ");
        System.out.println("1) To create projection base.");
        System.out.println("   -t nt -p 100 -m 110011 -cb -o base_nt_m110011_p100.bswp");
        System.out.println("   -cb [fmt] [filename] | -ob [fmt] [filename]");
        System.out.println("   where fmt: bin (bswp format) txt json txt/js");
    }
    
    private static SweepBase createBase(SweepParameters params) {
        // verifica os parâmetros para criar a base
        int tamAlfabeto = params.isAminoacidSequence() ? 20 : 4;
        SweepMask mask = params.getMask();
        int numBases = mask.getSizeOfSubset();
        if( numBases < 1 ) {
            numBases = 1;
        }
        int tamanhoBase = (int)Math.pow(tamAlfabeto, numBases);
        int tamanhoProj = params.getProjection();
        SweepBaseProjection proj = new SweepBaseProjection();
        SweepBase base = proj.createRandomweepBase(tamanhoBase, tamanhoProj);
        if( params.getBaseFilename() != null ) {
            FeatureFile arq = params.getBaseFilename();
            IOBase.save(arq.getFilename(), base);
        }
        if( params.getExportBaseFiles() != null && params.getExportBaseFiles().size()>0 ) {
            for(FeatureFile f : params.getExportBaseFiles() ) {
                if( "txt".equals(f.getFormat()) ) {
                    IOBase.exportText(f.getFilename(), base);
                } else if( "json".equals(f.getFormat()) || "js".equals(f.getFormat()) ) {
                    IOBase.exportJSON(f.getFilename(), base);
                } else if( "txt/json".equals(f.getFormat()) || "txt/js".equals(f.getFormat()) ) {
                    String prefix = FileUtils.extractFilename(f.getFilename().getAbsolutePath());
                    IOBase.exportJSON(new java.io.File(prefix+".json"), base);
                    IOBase.exportText(new java.io.File(prefix+".txt"), base);
                }
            }
        }
        if( base != null && base.getBase() != null ) {
            params.setBase(base);
            return base;
        }
        
        return null;
    }
    
    private static SweepBase loadBase(SweepParameters params) {
        if( params.getBaseFilename() == null || params.getBaseFilename().getFilename() == null ) {
            return null;
        }
        SweepBase base = IOBase.load(params.getBaseFilename().getFilename());
        params.setBase(base);
        return base;
    }

}