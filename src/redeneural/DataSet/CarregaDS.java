/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redeneural.DataSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author marceloimamura
 */
public class CarregaDS {

    private ArrayList<DataSet> dsTeste;
    private ArrayList<DataSet> dataset;
    private ArrayList<String> tpSaidas;
    ArrayList<Double> menor;
    ArrayList<Double> maior;
    private int ientradas, isaida;

    public ArrayList<DataSet> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<DataSet> dataset) {
        this.dataset = dataset;
    }

    public ArrayList<DataSet> getDsTeste() {
        return dsTeste;
    }

    public void setDsTeste(ArrayList<DataSet> dsTeste) {
        this.dsTeste = dsTeste;
    }

    public int getIentradas() {
        return ientradas;
    }

    public int getIsaida() {
        return tpSaidas.size();
    }
    

    public void addTreino(DataSet ds, ArrayList<DataSet> lista) {
        Random random = new Random();
        if (lista.size() == 0) {
            lista.add(ds);
        } else {
            int index = random.nextInt(lista.size());
            lista.add(index, ds);
        }

    }

    public int addDatasetSaida(String saida) {
        boolean flag = true;
        int i = 0;

        for (; i < tpSaidas.size() && flag; i++) {
            if (tpSaidas.get(i).equals(saida)) {
                flag = false;
            }
        }

        if (flag) {
            tpSaidas.add(saida);
            return i;
        }
        return i-1;
    }

    public boolean buscaDatasetSaida(String saida) {
        boolean flag = true;
        int i = 0;

        for (; i < tpSaidas.size() && flag; i++) {
            if (tpSaidas.get(i).equals(saida)) {
                flag = false;
            }
        }

        return !flag;
    }

    public boolean carregaTreinamento(String dir) {
        String div = ";";
        String linha;
        String[] var;
        try {
            //ler dataset
            BufferedReader br = new BufferedReader(new FileReader(dir));

            linha = br.readLine();
            if (linha.contains(",")) {
                div = ",";
            }

            var = linha.split(div);
            ientradas = var.length - 1;

            menor = new ArrayList();
            maior = new ArrayList();

            tpSaidas = new ArrayList();
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());

        }

        dataset = verifica(dir);

        if (dataset == null) {
            return false;
        }
        return true;
    }
    
    public boolean carregaTeste(String dir) {
        dsTeste = verifica(dir);

        if (dsTeste == null) {
            return false;
        }
        return true;
    }

    public ArrayList<DataSet> verifica(String dir) {//verifica enquanto cria dataset

        ArrayList<DataSet> lista = new ArrayList();

        DataSet ds;
        double valor;
        String linha;
        String[] var;
        boolean flag = true;
        String div = ";";
        try {
            //ler dataset
            BufferedReader br = new BufferedReader(new FileReader(dir));

            linha = br.readLine();
            if (linha.contains(",")) {
                div = ",";
            }

            var = linha.split(div);
            while (br.ready() && flag) {

                linha = br.readLine();

                var = linha.split(div);

                //posui tam igual
                if (var.length == ientradas + 1) {
                    ds = new DataSet();

                    try {
                        for (int i = 0; i < var.length - 1; i++) {
                            valor = Double.parseDouble(var[i]);
                            
                            if(valor == 0.7545909849749584)
                                System.out.println("arroz");
                            if (maior.size() < ientradas) {
                                maior.add(valor);
                                menor.add(valor);
                            } else {
                                if (maior.get(i) < valor) {
                                    maior.set(i, valor);
                                }
                                if (menor.get(i) > valor) {
                                    menor.set(i, valor);
                                }
                            }
                            ds.addEntrada(valor);
                        }
                    } catch (Exception ex) {// nao é valor
                        System.out.println("Erro: " + ex.getMessage());
                        flag = false;
                        lista = null;
                    }

                    ds.setSaida(addDatasetSaida(var[var.length - 1]));
                    ds.setS(var[var.length - 1]);
                    addTreino(ds,lista);
                    lista.add(ds);
                } else {// nao possui tam
                    flag = false;
                    lista = null;
                    System.out.println("Falta valor !");
                }
            }

            br.close();
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());

        }

        return lista;
    }
    
    
    public void normalizar(){
        double v;
        for (DataSet ds : dataset) {
            for (int i = 0; i < ds.getEntrada().size(); i++) {
                v = ds.getEntrada().get(i);
                v = (v - menor.get(i))/(maior.get(i)-menor.get(i));
                if(v>1){
                    System.out.println("Erro: "+v+" Valor Entrada: "+ds.getEntrada().get(i)+" maior: "+maior.get(i)+" coluna: "+i);
                }
                ds.getEntrada().set(i, v);
            }
        }
        
        for (DataSet d : dsTeste) {
            for (int i = 0; i < d.getEntrada().size(); i++) {
                v = d.getEntrada().get(i);
                d.getEntrada().set(i, (v - menor.get(i)/(maior.get(i)-menor.get(i))));
                d.getEntrada().set(i, v);
            }
        }
    }
}
