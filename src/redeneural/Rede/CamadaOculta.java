package redeneural.Rede;

import java.util.ArrayList;

public class CamadaOculta {

    private ArrayList<Neuronio> camada;

    public CamadaOculta(int entrada, int quant) {
        camada = new ArrayList();
        Neuronio n;
        
        for (int i = 0; i < quant; i++) {
            n = new Neuronio(entrada);
            camada.add(n);
        }
    }

    public ArrayList<Neuronio> getCamada() {
        return camada;
    }

    
    
}
