package redeneural.Rede;

import java.util.ArrayList;
import java.util.Random;

public class Neuronio {
    private ArrayList<Double> sinapse;
    private double erro;
    private double net;

    public Neuronio(int i) {
        sinapse = new ArrayList();
        Random r = new Random();
        
        for (int j = 0; j < i; j++) {
            sinapse.add(r.nextDouble());
            
            //sinapse.add(Double.parseDouble(""+j));
        }
        
        net = erro = 0;
        
    }

    public ArrayList<Double> getSinapse() {
        return sinapse;
    }

    public double getErro() {
        return erro;
    }

    public void setErro(double erro) {
        this.erro = erro;
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }
    
    
    
    
}
