package redeneural.Rede;

import java.util.ArrayList;
import redeneural.DataSet.DataSet;

public class Rede {
    private ArrayList<Double> logErro;
    private ArrayList<CamadaOculta> rede;
    private ArrayList<String> tpSaidas;
    private ArrayList<Double>[] vetorLog;
    private int[][] mConfusao;
    private double redeErro;
    private double totalErroK;
    private int ientradas, icamadas, isaida, ineuronioOculto;
    
    public void exibeRede() {
        String text = "";
        int i = 0;
        for (CamadaOculta co : rede) {
            text += "Camada: " + i + "\n";
            text += co.exibePeso() + "\n";
            i++;
        }

        System.out.println(text);
    }

    public String exibeMatriz(){
        String exibe = "\t";
        double total = 0 ;
        double acerto = 0;
        double porc;
        for (int i = 0; i < isaida; i++) {
            exibe += tpSaidas.get(i)+"\t";
        }
        exibe+="\n";
        for (int i = 0; i < isaida; i++) {
            exibe+=tpSaidas.get(i)+"\t";
            for (int j = 0; j < isaida; j++) {
                if(i==j)
                    acerto+=mConfusao[i][j];
                
                exibe += mConfusao[i][j]+ "\t";
                total +=mConfusao[i][j];
            }
            exibe+="\n";
        }
        porc = (acerto/total)*100;
        totalErroK += porc;
        exibe+="\nacurácia da rede";
        exibe+=String.format(": %.3f", porc);
        exibe+=" %";
        
        
        for (int i = 0; i < isaida; i++) {
            total = 0;
            acerto = 0;
            for (int j = 0; j < isaida; j++) {
                if(i==j)
                    acerto+=mConfusao[i][j];
                
                total +=mConfusao[i][j];
            }
            
            if(total!=0)
                porc = (acerto/total)*100;
            else{
                porc = 0.0;
            }
            exibe+="\nacurácia de "+tpSaidas.get(i);
            exibe+=String.format(": %.3f", porc);
            exibe+=" %";

        }
        exibe+=String.format("\nErro da rede: %.6f", redeErro);
        return exibe;
    }
    
    public void iniciar(int ientradas, int icamadas, int ineuronioOculto, int saida) {
        logErro = new ArrayList();
        this.icamadas = icamadas + 1;
        this.ientradas = ientradas;
        this.isaida = saida;
        this.ineuronioOculto = ineuronioOculto;

        rede = new ArrayList();
        CamadaOculta co = new CamadaOculta(ientradas, ineuronioOculto);
        rede.add(co);

        for (int i = 1; i < this.icamadas - 1; i++) {
            co = new CamadaOculta(ineuronioOculto, ineuronioOculto);
            rede.add(co);
        }

        co = new CamadaOculta(ineuronioOculto, isaida);
        rede.add(co);
    }
    
    public void iniciar_Kfould_log(){
        vetorLog = new ArrayList[4];
        
        for (int i = 0; i < 4; i++) {
            vetorLog[i] = new ArrayList();
        }
    }
    
    
    public void iniciarTeste(){
        mConfusao = new int[isaida][isaida];
    }
    
    public void iniciarLog(){
        logErro = new ArrayList();
    }
    //----------------------------iniciar-----------------------------------
    
    public void setLog_k(int i, ArrayList<Double> lista){
        vetorLog[i] = lista;
    }
    
    public ArrayList<Double> getLog(int i){
        return vetorLog[i];
    }
    
    
    public int getIcamadas() {
        return icamadas;
    }

    public void setIcamadas(int icamadas) {
        this.icamadas = icamadas;
    }

    public int getIentradas() {
        return ientradas;
    }

    public void setIentradas(int ientradas) {
        this.ientradas = ientradas;
    }

    public int getIsaida() {
        return isaida;
    }

    public void setIsaida(int isaida) {
        this.isaida = isaida;
    }

    public double getRedeErro() {
        return redeErro;
    }

    public void setRedeErro(double redeErro) {
        this.redeErro = redeErro;
    }

    public ArrayList<Double> getLogErro() {
        return logErro;
    }

    public ArrayList<CamadaOculta> getRede() {
        return rede;
    }

    public void setRede(ArrayList<CamadaOculta> rede) {
        this.rede = rede;
    }

    public ArrayList<String> getTpSaidas() {
        return tpSaidas;
    }

    public void setTpSaidas(ArrayList<String> tpSaidas) {
        this.tpSaidas = tpSaidas;
    }

    public double getTotalErroK() {
        return totalErroK;
    }

    public void setTotalErroK(double totalErroK) {
        this.totalErroK = totalErroK;
    }
    

    //------------------------BUSCA ETC...-----------------------
    public double[] arrayTovetor(ArrayList<Double> lista) {
        double[] vetor = new double[lista.size()];

        for (int i = 0; i < lista.size(); i++) {
            vetor[i] = lista.get(i);
        }

        return vetor;
    }

    //-----------------------------REDE--------------------------
    public void calcular_erro_rede() {
        CamadaOculta oc;
        double total = 0;
        oc = rede.get(icamadas - 1);//pegar o ultimo

        for (double d : oc.getErro()) {
            total += Math.pow(d, 2);
        }
        redeErro = total / 2;
    }

    public void testarRede(double teste[], int tp_act) {
        double entrada[];
        CamadaOculta oc;
        oc = rede.get(0);

        oc.calcularNet(teste, tp_act);
        entrada = oc.getSaida();

        for (int i = 1; i < icamadas; i++) {
            oc = rede.get(i);
            oc.calcularNet(entrada, tp_act);
            entrada = oc.getSaida();
        }
    }

    //perone
    public void calcular_erros(double[] entrada, int tp_act, int saida) {
        CamadaOculta oc, antOc;
        double[] antEntrada;
        oc = rede.get(icamadas - 1);//pegar o ultimo

        oc.calcularErroSaida(saida, tp_act);

        for (int i = icamadas - 2; i >= 0; i--) {
            antOc = rede.get(i);
            antEntrada = antOc.getSaida();
            antOc.setErro(oc.calcularErro(antEntrada, tp_act));

            antOc = oc;
        }
    }

    public void atualizar_pesos(double[] entrada, double txApredizagem) {
        CamadaOculta oc, antOc;
        for (int i = icamadas - 1; i >= 1; i--) {
            oc = rede.get(i);
            antOc = rede.get(i - 1);
            oc.atualizarPeso(antOc.getSaida(), txApredizagem);
        }

        oc = rede.get(0);
        oc.atualizarPeso(entrada, txApredizagem);
    }

    public void treinar(ArrayList<DataSet> ds, ArrayList<DataSet> dsTeste, int tp_act, double txApredizagem) {
        double[] entrada;
        for (DataSet dataSet : ds) {
            entrada = arrayTovetor(dataSet.getEntrada());
            testarRede(entrada, tp_act);
            calcular_erros(entrada, tp_act, dataSet.getSaida());
            atualizar_pesos(entrada, txApredizagem);

        }
        calcular_erro_rede();

        logErro.add(redeErro);

    }
    
    
    public int maior_saida(){
        CamadaOculta oc;
        double total = 0;
        int i = 0;
        oc = rede.get(icamadas - 1);//pegar o ultimo
        
        total = oc.getSaida()[0];
        
        for (int j = 1; j < oc.getSaida().length; j++) {
            if(oc.getSaida()[j]>total){
                i = j;
                total = oc.getSaida()[j];
            }
        }
        
        return i;
    }
    
    public void testar(DataSet ds,int tp_act){
        int saida, desejado;
        desejado = ds.getSaida();
        double[] entrada= arrayTovetor(ds.getEntrada());
        testarRede(entrada, tp_act);
        saida = maior_saida();
        mConfusao[desejado][saida]++;
        
    }
    
    public ArrayList<DataSet> juntarDS(ArrayList<DataSet> ds, ArrayList<DataSet> teste){
        ArrayList<DataSet> lista = new ArrayList();
        lista.addAll(ds);
        if(teste!=null)
            lista.addAll(teste);
        
        return lista;
    }
    
    public ArrayList<DataSet> getDS_K(int i,ArrayList<DataSet> ds){
        int div = ds.size()/4;
        int ini = i * div;
        int fim = ini + div;
        
        ArrayList<DataSet> lista = new ArrayList();
        
        for (int j = 0; j < ds.size(); j++) {
            if(j<ini || j>=fim){
                lista.add(ds.get(j));
            }
        }
        return lista;
    }
    
    public ArrayList<DataSet> getTeste_K(int i,ArrayList<DataSet> ds){
        int div = ds.size()/4;
        int ini = i * div;
        int fim = ini + div;
        
        ArrayList<DataSet> lista = new ArrayList();
        
        for (int j = 0; j < ds.size(); j++) {
            if(j>=ini && j<fim){
                lista.add(ds.get(j));
            }
        }
        return lista;
    }
    
    
}
