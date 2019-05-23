/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redeneural.Rede;

/**
 *
 * @author marceloimamura
 */
public class Func_ativacao {
    
    public double linear(double net) {
        return net / 10.0;
    }
    
    public double logistica(double net) {
        return 1.0 / (1 + Math.pow(Math.E, -net));
    }

    public double hiper(double net) {
        return (1 - Math.pow(Math.E, -2 * net)) / (1 + Math.pow(Math.E, -2 * net));
    }

    public double funcao_ativacao(double total, int tp_act) {
        if (tp_act == 0) {
            return linear(total);
        } else if (tp_act == 1) {
            return logistica(total);
        } else {
            return hiper(total);
        }
    }
    
    
    public double funcao_ativacao_der(double net, double total, int tp_act) {
        if (tp_act == 0) {
            return total * 0.1;
        } else if (tp_act == 1) {
            return total * (net * (1 - net));
        } else {
            return total * (1 - Math.pow(net, 2));
        }
    }
}
