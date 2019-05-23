package redeneural.Rede;

import java.util.Random;

public class CamadaOculta {

    private double[][] peso;
    private double[] erro, net, saida;
    private int entrada, ineuronios;
    private Func_ativacao func_ati;

    public CamadaOculta(int entrada, int ineuronios) {
        func_ati = new Func_ativacao();
        this.entrada = entrada;
        this.ineuronios = ineuronios;
        peso = new double[ineuronios][];
        erro = new double[ineuronios];
        net = new double[ineuronios];
        saida = new double[ineuronios];

        Random randon = new Random();
        double aux = 0;

        for (int i = 0; i < ineuronios; i++) {
            peso[i] = new double[entrada];
            for (int j = 0; j < entrada; j++) {
                String v = String.format("%.3f", randon.nextDouble());
                v = v.replace(",", ".");
                peso[i][j] = Double.parseDouble(v);
                //aux++;
            }
        }

    }
    //--------------------------GET----------------------------------------------

    public double[][] getPeso() {
        return peso;
    }

    public void setPeso(double[][] peso) {
        this.peso = peso;
    }

    public double[] getErro() {
        return erro;
    }

    public void setErro(double[] erro) {
        this.erro = erro;
    }

    public double[] getNet() {
        return net;
    }

    public void setNet(double[] net) {
        this.net = net;
    }

    public double[] getSaida() {
        return saida;
    }

    public void setSaida(double[] saida) {
        this.saida = saida;
    }

    public int getEntrada() {
        return entrada;
    }

    public void setEntrada(int entrada) {
        this.entrada = entrada;
    }

    public int getIneuronios() {
        return ineuronios;
    }

    public void setIneuronios(int ineuronios) {
        this.ineuronios = ineuronios;
    }

    public Func_ativacao getFunc_ati() {
        return func_ati;
    }

    public void setFunc_ati(Func_ativacao func_ati) {
        this.func_ati = func_ati;
    }

    //--------------------------CALCULOS----------------------------------------------
    public void calcularNet(double[] entrada, int tp_act) {
        double total;
        for (int i = 0; i < ineuronios; i++) {
            total = 0;
            for (int j = 0; j < this.entrada; j++) {
                total += peso[i][j] * entrada[j];
                //System.out.println("N: "+i+" J: "+j+" "+peso[i][j] + " * " + entrada[j]);
            }
            net[i] = total;
        }

        for (int i = 0; i < ineuronios; i++) {
            saida[i] = func_ati.funcao_ativacao(net[i], tp_act);
        }
    }

    public double[][] transposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public double[] calcularErro(double[] antEntrada, int tp_act) {
        double[] erroAnt = new double[this.entrada];
        double[][] pesoT = transposeMatrix(this.peso);
        double total;
        for (int i = 0; i < this.entrada; i++) {
            total = 0;
            for (int j = 0; j < ineuronios; j++) {
                //System.out.println("2 += " + pesoT[i][j] + " * " + erro[j]);
                total += func_ati.funcao_ativacao_der(antEntrada[i], pesoT[i][j] * erro[j], tp_act);
            }
            erroAnt[i] = total;
        }

        return erroAnt;
    }

    public void calcularErroSaida(int indexSaida, int tp_act) {
        double total;
        for (int i = 0; i < ineuronios; i++) {
            if (i == indexSaida) {
                total = 1 - saida[i];
            } else {
                total = 1 - saida[i];
            }
            erro[i] = func_ati.funcao_ativacao_der(saida[i], total, tp_act);
        }
    }

    public void atualizarPeso(double[] entrada, double txAprendizagem) {
        double[][] pesoT = transposeMatrix(this.peso);
        double resul;
        for (int i = 0; i < this.entrada; i++) {
            for (int j = 0; j < ineuronios; j++) {
                resul = pesoT[i][j] + (txAprendizagem * erro[j] * entrada[i]);
                //System.out.println(resul+"= "+"["+i+","+j+"]"+ pesoT[i][j]+ " + "+txAprendizagem+" * "+erro[j]+" * "+ entrada[i]);
                peso[j][i] = resul;
            }
        }
    }

    public String exibePeso() {
        String text = "";
        for (int i = 0; i < ineuronios; i++) {
            text += "Camada: " + i;
            for (int j = 0; j < entrada; j++) {
                text += "\tV" + peso[i][j];
            }
            text += "\n";
        }

        return text;
    }

}
