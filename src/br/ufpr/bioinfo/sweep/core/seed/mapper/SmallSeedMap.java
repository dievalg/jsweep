package br.ufpr.bioinfo.sweep.core.seed.mapper;

import br.ufpr.bioinfo.sweep.core.SeedIndex;
import br.ufpr.bioinfo.sweep.core.seed.Seed;
import br.ufpr.bioinfo.sweep.core.seed.SeedBuilder;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T>
 * @author Dieval Guizelini
 */
public class SmallSeedMap<T extends Seed> implements SeedIndex { 
    
    private byte[] alphabet = null;
    private Integer sizes = null;
    private Integer maxValues = null;
    private Class<T> seedClass = null;
    //
    private java.util.List<T> seedList = null;
    private java.util.Map<Integer, Integer> seedMap = null;

    public SmallSeedMap(int[] sizes, byte[] alphabet, Class<T> clazz) {
        this.alphabet = alphabet;
        this.sizes = sizes[0];
        this.seedClass = clazz;
    }

    @Override
    public void validadeIndex(SeedBuilder builder) {
        if (this.seedList == null || this.seedMap == null) {
            createIndex(builder);
        }
    }

    @Override
    public int getIndex(Seed seed) {
        T seed2 = (T) seed;
        // o hashCode é usado apra criar o indice...
        Integer res = seedMap.get(seed2.hashCode());
        if( res != null ) {
            return res.intValue();
        } else {
            System.out.printf("Unknow seed %s\n",seed.toString() );
            return 0;
        }
    }

    @Override
    public Seed getSeed(int index) {
        return (Seed) this.seedList.get(index);
    }

    @Override
    public void createIndex(SeedBuilder builder) {
        createIndexInterno(0);
    }

    private void createIndexInterno(int index) {
        final byte[] conjunto = this.alphabet;
        final int len = (int)this.sizes;
        int conjuntoLen = conjunto.length;
        int numComb = (int) Math.pow(conjunto.length, len);
        if (numComb > 65536) {  // 16bits
            System.out.println("WAR: high number of combinations.");
        }
        java.util.Map<Integer, Integer> map2num = new java.util.HashMap<Integer, Integer>(numComb);
        java.util.List<T> keys = new java.util.ArrayList<T>(numComb);
        byte[] base = new byte[len];
        for (int i = 0; i < len; i++) {
            base[i] = conjunto[0];
        }
        for (int i = 0; i < numComb; i++) {
            int n = i;
            byte[] aux = new byte[len];
            for (int j = 0; j < len; j++) {
                aux[j] = conjunto[n % conjuntoLen];
                n /= conjuntoLen;
            }
            //Seed s = builder.getSeed(aux);
            try {
                T s = this.seedClass.newInstance();
                s.setValue(aux);
                keys.add(s);
            } catch (InstantiationException ex) {
                Logger.getLogger(SmallSeedMap.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(SmallSeedMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            T auxCmp = this.seedClass.newInstance();
            Collections.sort(keys, auxCmp.getSeedComparator());
            for (int i = 0; i < keys.size(); i++) {
                T k = (T) keys.get(i);
                //map2num.put(k.getIntValue(), i);
                map2num.put(k.hashCode(), i);
                //System.out.printf("%s\t%d\n", k.toString(), i);
            }
            this.seedList = keys;
            this.seedMap = map2num;
            this.maxValues = numComb;

        } catch (InstantiationException ex) {
            Logger.getLogger(SmallSeedMap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SmallSeedMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.printf("index %d  numCombinacoes %d seedList.size=%d seedMap.size=%d maxValues=%d\n",
                index, numComb,seedList.size(), seedMap.size(), numComb);
    }

    /*public static void main(String[] args) {
        //String seq = "AGAGTTTGATCCTGGCTCAGAGTGAACGCTGGCGGCGTGCCTAATACATGCAAGTCGAACGATGAAGCTT";
        SeedBuilder testeBuilder
                = new GenericSeedBuilder<BasicSeed>(new SweepMask("11111111"), BasicSeed.class);
        SmallSeedMap<BasicSeed> map = new SmallSeedMap<BasicSeed>(new int[]{8},
                NucleotideSeedEncoder.INSTANCE.getAlphabet(), BasicSeed.class);
        map.createIndex(testeBuilder);

        System.out.println("Todas as seeds geradas...");
        for (int i = 0; i < map.sizes.length; i++) {
            System.out.printf("\nLista %d  tamanho %d num de combinacoes %d\n\n", i + 1,
                    map.sizes[i], map.maxValues[i]);
            for (Seed s : map.seedList[i]) {
                System.out.printf("%2d\t%s\n", i++, new String(s.getValue()));
            }
        }
        // partindo do pressuposto que a geração da lista acima está correta...
        int numComb = map.maxValues[0];
        for (int i = 1; i < map.maxValues.length; i++) {
            numComb *= map.maxValues[i];
        }
        System.out.printf("Num. de combinações %d\n",numComb);
        /*int[] idx = new int[map.maxValues.length];
        for (int i = 0; i < map.maxValues.length; i++) {
            idx[i] = map.maxValues[i] - 1;
        }*/
 /*
        System.out.println("Verificando a recuperação da posição a partir da seed...");
        i = 0;
        for (Seed s : map.seedList[1]) {
            int idx = map.getIndex(s);
            String s1 = new String(s.getValue());
            String s2 = new String(map.getSeed(i).getValue());
            System.out.printf("%2d\t%s\t%2d\t%s\t\t%s\t%s\n", i, s1,
                    idx, i != idx ? "Erro" : "", s2, !s1.equals(s2) ? "Erro" : "");
            i++;
        }

        /*if (map.seedList1 == map.seedList2) {
            System.out.println("2a lista gerada...");
            i = 0;
            for (Seed s : map.seedList2) {
                System.out.printf("%2d\t%s\n", i++, new String(s.getValue()));
            }
            System.out.println("Verificando a recuperação da posição a partir da seed...");
            i = 0;
            for (Seed s : map.seedList2) {
                int idx = map.getIndex(s);
                String s1 = new String(s.getValue());
                String s2 = new String(map.getSeed(i).getValue());
                System.out.printf("%2d\t%s\t%2d\t%s\t\t%s\t%s\n", i, s1,
                        idx, i != idx ? "Erro" : "", s2, !s1.equals(s2) ? "Erro" : "");
                i++;
            }
        }* /
    }*/
}
