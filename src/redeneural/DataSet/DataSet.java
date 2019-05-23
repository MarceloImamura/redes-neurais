package redeneural.DataSet;

import java.util.ArrayList;

public class DataSet {
    private ArrayList<Double> entrada;
    int saida;
    String s;
    public DataSet() {
        entrada = new ArrayList();
    }
    
    public void addEntrada(double entrada){
        this.entrada.add(entrada);
    }

    public DataSet(ArrayList<Double> entrada, int saida, String s) {
        this.entrada = entrada;
        this.saida = saida;
        this.s= s;
    }

    public void setSaida(int saida) {
        this.saida = saida;
    }

    public ArrayList<Double> getEntrada() {
        return entrada;
    }

    public int getSaida() {
        return saida;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
    
    
    
}
