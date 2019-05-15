package redeneural.Rede;

import java.util.ArrayList;

public class DataSet {
    ArrayList<Double> entrada;
    int saida;

    public DataSet() {
        entrada = new ArrayList();
    }
    
    public void addEntrada(double entrada){
        this.entrada.add(entrada);
    }

    public DataSet(ArrayList<Double> entrada, int saida) {
        this.entrada = entrada;
        this.saida = saida;
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
    
    
    
}
