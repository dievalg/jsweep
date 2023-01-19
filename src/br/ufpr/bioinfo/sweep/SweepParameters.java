package br.ufpr.bioinfo.sweep;

import br.ufpr.bioinfo.commons.utils.ArraysUtils;
import br.ufpr.bioinfo.sweep.core.SweepBase;
import br.ufpr.bioinfo.sweep.core.mask.SweepMask;
import br.ufpr.bioinfo.sweep.io.FeatureFile;

/**
 *
 * @author Dieval Guizelini
 */
public class SweepParameters {

    public final static int NUCLEOTIDE = 0;
    public final static int AMINOACID = 1;
    //
    // main parameters...
    //
    /** @value sequenceType kind of sequence */
    private int sequenceType = AMINOACID;
    /** @value projection length of vector projection */
    private int projection = 600;
    /** @value maskStr string with mask definition, use zeros and one, where one considers base and zero ignore base. */
    private String maskStr = "11011";
    /** @value maskStr object */
    private SweepMask mask = new SweepMask(maskStr);
    //
    // create base
    private boolean generateBase = false;
    //private java.io.File baseFilename = null;
    private FeatureFile baseFilename = null;
    private SweepBase base = null;
    //
    // outros
    /** @value inputFilename is default output filename */
    private java.util.List<FeatureFile> inputFileList = null;
    /** @value outputFilename is default output filename */
    private java.io.File outputFilename = null;
    /** @value exportBaseFiles is collections of parameters file to be exported */
    private java.util.List<FeatureFile> exportBaseFiles = new java.util.ArrayList<FeatureFile>();
    /** @value exportFiles is collections of parameters file to be exported */
    private java.util.List<FeatureFile> exportFiles = new java.util.ArrayList<FeatureFile>();
    /** @value distanceFiles files to save distances between sequences. */
    private java.util.List<FeatureFile> distanceFiles = new java.util.ArrayList<FeatureFile>();
    //
    private int ncbiGeneticCode = 11;

    public SweepParameters() {
    }

    public SweepParameters(String[] args) {
        parser(args);
    }

    public final void parser(String[] args) {
        //java.util.List<SweepCommand> cmdList = new java.util.ArrayList<SweepCommand>();
        //
        this.generateBase = true;

        for (int i = 0; i < args.length; i++) {
            // parametros globais
            if ("-t".equals(args[i]) || "-seqtype".equals(args[i])) {
                i++;
                if ("aa".equalsIgnoreCase(args[i])) {
                    this.sequenceType = AMINOACID;
                } else {
                    this.sequenceType = NUCLEOTIDE;
                }

            } else if ("-p".equals(args[i]) || "-projection".equals(args[i])) {
                i++;
                int value = Integer.parseInt(args[i]);
                if (value > 19 && value < 3001) {
                    this.projection = value;
                } else if (value < 20) {
                    this.projection = 20;
                    System.out.printf("WARN: minimum projection length %d\n", this.projection);
                } else if (value > 3000) {
                    this.projection = 3000;
                    System.out.printf("WARN: maximum projection length %d\n", this.projection);
                }

            } else if ("-m".equals(args[i]) || "-mask".equals(args[i])) {
                i++;
                String seedmaskStr = args[i];
                if (!seedmaskStr.matches("[01]+")) {
                    System.out.println("-mask requires mask with only ones and zeros symbols...");
                    System.exit(-1);
                }
                this.maskStr = seedmaskStr;
                this.mask = new SweepMask(seedmaskStr);

            } else if ("-gencode".equals(args[i])) {
                i++;
                int code = Integer.parseInt(args[i]);
                if (!setNcbiGeneticCode(code)) {
                    System.out.println("Invalid NCBI Genetic Codes");
                    System.exit(-1);
                }

                //
                //  para criar da base
            } else if ("-cb".equals(args[i]) || "-createbase".equals(args[i])) {
                this.generateBase = true;
                i++;
                if (i < args.length && !args[i].startsWith("-")) {
                    String fmt = "bin";
                    if (ArraysUtils.contain(new String[]{args[i]}, new String[]{"bin", "txt", "js", "json"})) {
                        fmt = args[i];
                        i++;
                    }
                    baseFilename = new FeatureFile(new java.io.File(args[i]), fmt, FeatureFile.OUTPUT);
                }

                //
                //  para carregar a base
            } else if ("-ib".equals(args[i]) || "-b".equals(args[i]) || "-inputbase".equals(args[i]) || "-base".equals(args[i]) ) {
                this.generateBase = false;
                i++;
                if (i < args.length && !args[i].startsWith("-")) {
                    String fmt = "bin";
                    if (ArraysUtils.contain(new String[]{args[i]}, new String[]{"bin", "txt", "js", "json"})) {
                        fmt = args[i];
                        i++;
                    }
                    java.io.File arq = new java.io.File(args[i]);
                    if( arq.exists() && arq.isFile() ) {
                        baseFilename = new FeatureFile(arq, fmt, FeatureFile.INPUT);
                    } else {
                        System.out.printf("unknow base file: %s\n",args[i]);
                    }
                    
                }

            } else if ("-expb".equals(args[i]) || "-eb".equals(args[i])) {
                i++;
                FeatureFile aux = new FeatureFile();
                aux.setKindOfStream(FeatureFile.OUTPUT);
                aux.setFormat("txt");
                if (i + 1 < args.length
                        && ArraysUtils.contain(new String[]{args[i]}, new String[]{"bin", "txt", "js", "json"})) {
                    aux.setFormat(args[i]);
                    i++;
                }
                aux.setFilename(new java.io.File(args[i]));
                exportBaseFiles.add(aux);

            } else if ("-exp".equals(args[i])) {
                i++;
                FeatureFile aux = new FeatureFile(null, "txt", FeatureFile.OUTPUT);
                if (i + 1 < args.length
                        && ArraysUtils.contain(new String[]{args[i]}, new String[]{"bin", "txt", "js", "json"})) {
                    aux.setFormat(args[i]);
                    i++;
                }
                aux.setFilename(new java.io.File(args[i]));
                exportFiles.add(aux);

                //
                //  input padrão
            } else if ("-i".equals(args[i]) || "-input".equals(args[i])) {
                i++;
                /* @TODO: tratar multiplos arquivos, pensar em uso de coringas ou virgulas, ou multiplos -i */
 /*String fmt = "fasta";
                if (i + 1 < args.length 
                        && ArraysUtils.contain(new String[]{args[i]}, new String[]{"fas", "fasta", "fq", "sam"})) {
                    
                }*/
                java.io.File arq = new java.io.File(args[i]);
                if (arq.exists() && arq.isFile()) {
                    FeatureFile ff = new FeatureFile(arq, "fasta", FeatureFile.INPUT);
                    if (inputFileList == null) {
                        inputFileList = new java.util.ArrayList<FeatureFile>();
                    }
                    inputFileList.add(ff);
                }

                //
                //  output padrão
            } else if ("-d".equals(args[i]) || "-dist".equals(args[i])) {
                i++;
                FeatureFile aux = new FeatureFile(null, "txt", FeatureFile.OUTPUT);
                if (i + 1 < args.length
                        && ArraysUtils.contain(new String[]{args[i]}, new String[]{"txt", "js", "json"})) {
                    aux.setFormat(args[i]);
                    i++;
                }
                aux.setFilename(new java.io.File(args[i]));
                if (distanceFiles == null) {
                    distanceFiles = new java.util.ArrayList<FeatureFile>();
                }
                distanceFiles.add(aux);

                //
                //  output padrão
            } else if ("-o".equals(args[i]) || "-out".equals(args[i])) {
                i++;
                if (this.generateBase && this.baseFilename == null) {
                    FeatureFile aux = new FeatureFile(new java.io.File(args[i]), "bin", FeatureFile.OUTPUT);
                    baseFilename = aux;
                }
                this.outputFilename = new java.io.File(args[i]);

                // output base
            } else if ("-ob".equals(args[i]) || "-outbase".equals(args[i])) {
                i++;
                FeatureFile aux = new FeatureFile();
                aux.setKindOfStream(FeatureFile.OUTPUT);
                if (i + 1 < args.length
                        && ArraysUtils.contain(new String[]{args[i]}, new String[]{"bin", "txt", "js", "json"})) {
                    aux.setFormat(args[i]);
                    i++;
                }
                aux.setFilename(new java.io.File(args[i]));
                this.baseFilename = aux;
            }
        }
    }

    //
    // indicação de ação a ser realizada
    public boolean isCreateBase() {
        return this.generateBase;
    }

    public boolean hasBaseOutputFile() {
        return this.baseFilename != null
                || (this.outputFilename != null && this.generateBase);
    }

    //
    // arquivos
    public FeatureFile getBaseFilename() {
        return this.baseFilename;
    }

    public java.io.File getOutputFilename() {
        return this.outputFilename;
    }

    //
    //  -- gets e sets...
    //
    public int getSequenceType() {
        return this.sequenceType;
    }

    public boolean isAminoacidSequence() {
        return this.sequenceType == AMINOACID;
    }

    public int getProjection() {
        return this.projection;
    }

    public String getMaskStr() {
        return this.maskStr;
    }
    
    public void setMask(SweepMask mask) {
        this.mask = mask;
        this.maskStr = mask.toString();
    }

    public SweepMask getMask() {
        return this.mask;
    }

    //
    // global data
    public void setBase(SweepBase base) {
        this.base = base;
    }

    public SweepBase getBase() {
        return this.base;
    }

    public java.util.List<FeatureFile> getInputFileList() {
        return this.inputFileList;
    }

    public java.util.List<FeatureFile> getExportBaseFiles() {
        return this.exportBaseFiles;
    }

    public java.util.List<FeatureFile> getExportFiles() {
        return this.exportFiles;
    }

    public java.util.List<FeatureFile> getDistanceFiles() {
        return this.distanceFiles;
    }

    /**
     * @return the ncbiGeneticCode
     */
    public int getNcbiGeneticCode() {
        return ncbiGeneticCode;
    }

    /**
     * @param code the ncbiGeneticCode to set
     * @return true if code is valid.
     *
     * @see https://www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi Codes may be: 
     * <ol>
     * <li>1. The Standard Code</li>
     * <li>2. The Vertebrate Mitochondrial Code</li>
     * <li>3. The Yeast Mitochondrial Code </li>
     * <li>4. The Mold, Protozoan, and Coelenterate Mitochondrial Code and the Mycoplasma/Spiroplasma Code </li>
     * <li>5. The Invertebrate Mitochondrial Code </li>
     * <li>6. The Ciliate, Dasycladacean and Hexamita Nuclear Code </li>
     * <li>9. The Echinoderm and Flatworm Mitochondrial Code </li>
     * <li>10. The Euplotid Nuclear Code </li>
     * <li>11. The Bacterial, Archaeal and Plant Plastid Code </li>
     * <li>12. The Alternative Yeast Nuclear Code </li>
     * <li>13. The Ascidian Mitochondrial Code </li>
     * <li>14. The Alternative Flatworm Mitochondrial Code </li>
     * <li>16. Chlorophycean Mitochondrial Code </li>
     * <li>21. Trematode Mitochondrial Code </li>
     * <li>22. Scenedesmus obliquus Mitochondrial Code </li>
     * <li>23. Thraustochytrium Mitochondrial Code </li>
     * <li>24. Rhabdopleuridae Mitochondrial Code </li>
     * <li>25. Candidate Division SR1 and Gracilibacteria Code </li>
     * <li>26. Pachysolen tannophilus Nuclear Code </li>
     * <li>27. Karyorelict Nuclear Code </li>
     * <li>28. Condylostoma Nuclear Code </li>
     * <li>29. Mesodinium Nuclear Code </li>
     * <li>30. Peritrich Nuclear Code </li>
     * <li>31. Blastocrithidia Nuclear Code </li>
     * <li>33. Cephalodiscidae Mitochondrial UAA-Tyr Code</li>
     * </ol>
     */
    public boolean setNcbiGeneticCode(int code) {
        switch (code) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 33:
                this.ncbiGeneticCode = code;
                break;
            default:
                System.out.println("WARN: Invalid NCBI Genetic Code: " + code);
                return false;
        }
        return true;
    }


}
