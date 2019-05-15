package redeneural.Rede;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class Rede {

    ArrayList<Double> menor;
    ArrayList<Double> maior;

    private ArrayList<Double> logErro;
    private ArrayList<DataSet> dsTeste;
    private ArrayList<DataSet> dataset;
    private ArrayList<String> tpSaidas;
    private ArrayList<CamadaOculta> camadas;
    private int neuronio, icamadas, ientradas, isaida;
    private double redeErro;

    public void iniciar(int camadas) {
        icamadas = camadas;
        iniciar(ientradas, icamadas, isaida);
    }

    public void iniciar(int ientradas, int icamadas, int saida) {
        logErro = new ArrayList();
        this.icamadas = icamadas;
        this.ientradas = ientradas;
        this.isaida = saida;
        this.neuronio = (ientradas + saida) / 2;

        camadas = new ArrayList();

        CamadaOculta co = new CamadaOculta(ientradas, this.neuronio);
        camadas.add(co);

        for (int i = 1; i < this.icamadas; i++) {
            co = new CamadaOculta(this.neuronio, this.neuronio);
            camadas.add(co);
        }

        co = new CamadaOculta(this.neuronio, saida);
        camadas.add(co);
    }

    public ArrayList<DataSet> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<DataSet> dataset) {
        this.dataset = dataset;
    }

    public int getNeuronio() {
        return neuronio;
    }

    public void setNeuronio(int neuronio) {
        this.neuronio = neuronio;
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

    public double linear(double net) {
        return net / 10.0;
    }

    public double linear_der(double net) {
        return net * (1 / 10.0);
    }

    public double logistica(double net) {
        return 1.0 / (1 + Math.pow(Math.E, -net));
    }

    public double logistica_der(double net) {
        return net * (1 - net);
    }

    public double hiper(double net) {
        return (1 - Math.pow(Math.E, -2 * net)) / (1 + Math.pow(Math.E, -2 * net));
    }

    public double hiper_der(double net) {
        return 1 - (Math.pow(net, 2));
    }

    public double getErroRede() {
        double erro = 0;

        CamadaOculta oc = camadas.get(neuronio);

        for (Neuronio n : oc.getCamada()) {
            erro += Math.pow(n.getErro(), 2);
        }
        erro /= 2;

        return erro;
    }

    public void funcao_ativacao(Neuronio n, double total, int tp_act) {
        if (tp_act == 0) {
            n.setNet(linear(total));
        } else if (tp_act == 1) {
            n.setNet(logistica(total));
        } else {
            n.setNet(hiper(total));
        }
    }

    public void funcao_ativacao_der(Neuronio n, double total, int tp_act) {
        if (tp_act == 0) {
            n.setErro(total / 10);
        } else if (tp_act == 1) {
            double net = n.getNet();
            n.setErro(total * (net * (1 - net)));
        } else {
            double net = n.getNet();
            n.setErro(total * (1 - Math.pow(net, 2)));
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
        return i;
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

    public void addTreino(DataSet ds) {
        Random random = new Random();
        if (dataset.size() == 0) {
            dataset.add(ds);
        } else {
            int index = random.nextInt(dataset.size());
            dataset.add(index, ds);
        }

    }

    public int saidaMax() {
        CamadaOculta oc = camadas.get(icamadas);
        double maior = -1.0;
        int pos = 0;
        for (int i = 0; i < oc.getCamada().size(); i++) {

            if (maior < oc.getCamada().get(i).getNet()) {
                pos = i;
                maior = oc.getCamada().get(i).getNet();
            }
        }

        return pos;
    }

    public boolean verifica(String dir) {//verifica enquanto cria dataset

        menor = new ArrayList();
        maior = new ArrayList();

        dataset = new ArrayList();
        tpSaidas = new ArrayList();

        int indexS;
        int dataset = 0;
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
            dataset = var.length;
            ientradas = var.length - 1;
            int indexL = 0;
            while (br.ready() && flag) {

                linha = br.readLine();

                var = linha.split(div);

                //posui tam igual
                if (var.length == dataset) {
                    ds = new DataSet();

                    try {
                        for (int i = 0; i < var.length - 1; i++) {
                            valor = Double.parseDouble(var[i]);

                            if (indexL == 0) {
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
                    }

                    
                    ds.setSaida(addDatasetSaida(var[var.length - 1]));
                    addTreino(ds);

                } else {// nao possui tam
                    flag = false;
                    System.out.println("Falta valor !");
                }
                indexL++;
            }

            br.close();
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());

        }

        if (flag) {//Normalizar
            isaida = tpSaidas.size();
        }
        return flag;
    }

    public boolean verifica_teste(String dir) {//verifica enquanto cria dataset
        dsTeste = new ArrayList();

        int indexS;
        int dataset = 0;
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
            dataset = var.length;
            if (var.length - 1 == ientradas) {//verificar se entradas igual is
                while (br.ready() && flag) {

                    linha = br.readLine();

                    var = linha.split(div);

                    //posui tam igual
                    if (var.length == dataset) {
                        ds = new DataSet();

                        try {
                            for (int i = 0; i < var.length - 1; i++) {
                                valor = Double.parseDouble(var[i]);
                                
                                if (maior.get(i) < valor) {
                                    maior.set(i, valor);
                                }
                                if (menor.get(i) > valor) {
                                    menor.set(i, valor);
                                }
                                
                                ds.addEntrada(valor);
                            }
                        } catch (Exception ex) {// nao é valor
                            System.out.println("Erro: " + ex.getMessage());
                            flag = false;
                        }

                        if(buscaDatasetSaida(var[var.length - 1])){
                            ds.setSaida(addDatasetSaida(var[var.length - 1]));
                        } else {
                            flag = false;
                            System.out.println("Saida diferente !!: "+var[var.length - 1]);
                            
                            for (String tpSaida : tpSaidas) {
                                System.out.println("Tp: "+tpSaida);
                            }
                        }
                            

                        dsTeste.add(ds);

                    } else {// nao possui tam
                        flag = false;
                        System.out.println("Falta valor !");
                    }
                }

                br.close();
            }else{
                flag = false;
                System.out.println("Entradas difentes !");
            }

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());

        }

        if (flag) {//Normalizar
            isaida = tpSaidas.size();
        }
        return flag;
    }

    public void treinar(int tp_act, double taxaA) {

        for (DataSet ds : dataset) {

            teste(ds.getEntrada(), tp_act);
            backpropagation(ds.getEntrada(), ds.getSaida(), tp_act, taxaA);

        }
        addErroLog(redeErro);
        //System.out.println(String.format("%.12f", redeErro));
    }

    public void erroSaida(int saida, int tp_act) {
        CamadaOculta oc;
        oc = camadas.get(camadas.size() - 1);
        redeErro = 0;
        Neuronio n;

        for (int i = 0; i < oc.getCamada().size(); i++) {

            n = oc.getCamada().get(i);

            if (i == saida) {
                funcao_ativacao_der(n, 1.0 - n.getNet(), tp_act);

            } else {
                funcao_ativacao_der(n, 0.0 - n.getNet(), tp_act);
            }
            redeErro += Math.pow(n.getErro(), 2);
        }

        redeErro /= 2;

    }

    public void erroOculto(int tp_act) {
        double total = 0;
        double aux;
        CamadaOculta oc, oc1;

        for (int i = camadas.size() - 1; i >= 1; i--) {

            oc1 = camadas.get(i);
            oc = camadas.get(i - 1);

            Neuronio n, n1;

            for (int j = 0; j < oc.getCamada().size(); j++) {//Navengando pela camada
                n = oc.getCamada().get(j);
                total = 0;

                for (int k = 0; k < oc1.getCamada().size(); k++) {//Pelos pesos
                    n1 = oc1.getCamada().get(k);
                    aux = n1.getErro() * n1.getSinapse().get(j);

                    funcao_ativacao_der(n, aux, tp_act);
                    total += n.getErro();
                }
                n.setErro(total);

            }
        }

    }

    public void atualizarPeso(double taxaA, ArrayList<Double> entrada) {
        double erro = 0, peso, net;
        CamadaOculta oc1, oc;
        Neuronio n1;

        for (int i = camadas.size() - 1; i >= 1; i--) {//pelas camadas
            oc = camadas.get(i - 1);
            oc1 = camadas.get(i);

            for (int j = 0; j < oc1.getCamada().size(); j++) {// neuronios da camada
                n1 = oc1.getCamada().get(j);
                erro = n1.getErro();

                for (int k = 0; k < n1.getSinapse().size(); k++) { //pesos
                    net = oc.getCamada().get(k).getNet();
                    peso = n1.getSinapse().get(k);
                    n1.getSinapse().set(k, peso + taxaA * erro * net);
                    //System.out.println("\nAtualizarPezo: " + k + " " + peso + " + " + taxaA + "*" + erro + "*" + net + "=" + n1.getSinapse().get(k));
                }
            }
        }

        oc = camadas.get(0);
        for (int i = 0; i < oc.getCamada().size(); i++) {
            n1 = oc.getCamada().get(i);
            erro = n1.getErro();
            for (int j = 0; j < n1.getSinapse().size(); j++) {
                net = entrada.get(j);
                peso = n1.getSinapse().get(j);
                n1.getSinapse().set(j, peso + taxaA * erro * net);
            }
        }

    }

    public void backpropagation(ArrayList<Double> entrada, int saida, int tp_act, double taxaA) {
        erroSaida(saida, tp_act);
        erroOculto(tp_act);
        atualizarPeso(taxaA, entrada);
    }

    public void teste(ArrayList<Double> entrada, int tp_act) {
        CamadaOculta oc = camadas.get(0);
        double total = 0;

        //System.out.println("Entrada: ");
        for (Neuronio n : oc.getCamada()) {// neuronio camada oculta 1
            for (int i = 0; i < entrada.size(); i++) {
                total += n.getSinapse().get(i) * entrada.get(i);

                //System.out.println(n.getSinapse().get(i) +"*"+ entrada.get(i));
            }
            funcao_ativacao(n, total, tp_act);

        }
        CamadaOculta oc1;

        //Camada Oculta até saida
        //System.out.println("\n\nOculto");
        for (int i = 0; i < camadas.size() - 1; i++) {//demais camadas
            oc = camadas.get(i);
            oc1 = camadas.get(i + 1);
            total = 0;

            for (Neuronio n1 : oc1.getCamada()) {
                //System.out.println("\n"+i);
                for (int j = 0; j < n1.getSinapse().size(); j++) {
                    total += n1.getSinapse().get(j) * oc.getCamada().get(j).getNet();

                    //System.out.println("\n"+n1.getSinapse().get(j) +"*"+ oc.getCamada().get(j).getNet());
                }

                funcao_ativacao(n1, total, tp_act);
                //System.out.print("Total:"+total+" Resul = "+n1.getNet());
            }
        }

    }

    public String getSaidaString() {
        String text = "Saida: ";
        for (String string : tpSaidas) {
            text += string + " ";
        }

        return text;
    }

    public void addErroLog(double erro) {
        logErro.add(erro);
    }

    public String stringArquitura() {
        String aux = "Entrada: " + ientradas;

        for (int i = 0; i < camadas.size(); i++) {
            aux += " \ncamada " + i;

            for (int j = 0; j < camadas.get(i).getCamada().size(); j++) {
                aux += "\nNeuronio: " + j;
                Neuronio n = camadas.get(i).getCamada().get(j);
                for (int k = 0; k < n.getSinapse().size(); k++) {
                    aux += "\t peso " + k + " " + n.getSinapse().get(i);

                }
            }
        }

        return aux;

    }

    public ArrayList<Double> getLogErro() {
        return logErro;
    }

    public void predic() {
        CamadaOculta oc = camadas.get(icamadas);
        String teste = "Ok: ";

        for (Neuronio n : oc.getCamada()) {
            teste += n.getNet() + " ";
        }

    }

}
