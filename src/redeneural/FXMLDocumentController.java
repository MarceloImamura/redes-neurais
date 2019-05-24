/* * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redeneural;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import redeneural.DataSet.CarregaDS;
import redeneural.DataSet.DataSet;
import redeneural.Rede.Rede;

/**
 *
 * @author marceloimamura
 */
public class FXMLDocumentController implements Initializable {
    private boolean flagTeste;
    private Rede rn;
    private CarregaDS carrega;
    @FXML
    private LineChart<ObservableList, String> graficoE;
    @FXML
    private TableView<ObservableList<String>> tabela;
    @FXML
    private Button btTreinar;
    @FXML
    private TextField txEntrada;
    @FXML
    private TextField txOculta;
    @FXML
    private TextField txSaida;
    @FXML
    private Tab tabTeste;
    @FXML
    private TextField txItera;
    @FXML
    private Tab tabExe;
    @FXML
    private ProgressBar pbIteracoes;
    @FXML
    private ProgressBar pbTeste;
    @FXML
    private Button btExibe;
    @FXML
    private Button btTeste;
    @FXML
    private CheckBox ckFould;
    @FXML
    private Tab tabNormal;
    @FXML
    private Tab tabK;
    @FXML
    private TableView<ObservableList<String>> tabelaTeste;
    @FXML
    private TextField txAprendizagem;
    @FXML
    private Canvas canvas1;
    @FXML
    private Tab tabelaK;
    @FXML
    private TableView<?> tabela1;
    @FXML
    private TextArea txConfNormal;
    @FXML
    private CheckBox ckRandon;
    @FXML
    private ComboBox<String> cbTpFunc;
    @FXML
    private TextField txErro;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrega = new CarregaDS();
        rn = new Rede();
        tabK.setDisable(true);
        flagTeste = false;
        btTeste.setDisable(true);
        btTreinar.setDisable(true);
        txEntrada.setDisable(true);
        cbTpFunc.getItems().addAll("Linear","Logística","Hiperbólica");
        cbTpFunc.getSelectionModel().select(0);
        txAprendizagem.setText("0.1");
        txItera.setText("25");
        txOculta.setText("1");
        txSaida.setDisable(true);
        txErro.setText("0.00001");
        txOculta.requestFocus();

        graficoE.setTitle("Erro");

        tabTeste.setDisable(true);

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaTeste.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btExibe.setVisible(false);
    }

    private void carregaTabela() {
        /*
        ObservableList<DataSet> modelo;
        modelo = FXCollections.observableArrayList(rn.getDataset());
        System.out.println(modelo.toString());
        tabela.setItems(modelo);*/
    }

    @FXML
    private void evtCarrega(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String diretorio = selectedFile.getAbsolutePath();
            diretorio = diretorio.replace("\\", "/");
            carrega = new CarregaDS();
            if (carrega.carregaTreinamento(diretorio)) {
                insert(diretorio);
                btTeste.setDisable(false);

                txEntrada.setText(""+carrega.getIentradas());
                txSaida.setText(""+carrega.getIsaida());
            }
        }
    }

    public void insert(String dir) {
        tabela.getColumns().clear();
        List<String> columns = new ArrayList<String>();
        List<String> rows = new ArrayList<String>();
        ObservableList<ObservableList<String>> csvData = FXCollections.observableArrayList();
        String div = ";";
        File f = new File(dir);
        if (f.exists() && !f.isDirectory()) {

            try (
                    FileReader fin = new FileReader(f);
                    BufferedReader in = new BufferedReader(fin);) {
                String l;
                int i = 0;

                while ((l = in.readLine()) != null) {

                    if (i < 1) {
                        if (l.contains(",")) {
                            div = ",";
                        }

                        String[] headers = l.split(div);

                        for (String w : headers) {
                            columns.add(w);

                        }
                        for (int k = 0; k < columns.size(); k++) {
                            final int finalIdx = k;
                            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(k));

                            // column.setText("hghjghjg");
                            column.setCellValueFactory(
                                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));

                            /*
                             * System.out.println(new
                             * ReadOnlyObjectWrapper<>(param.getValue().get(
                             * finalIdx)));
                             */
                            tabela.getColumns().add(column);
                        }

                        // tableView.getColumns().addAll(tableColumns);
                    } else {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        row.clear();
                        String[] items = l.split(div);
                        for (String item : items) {
                            row.add(item);
                        }
                        csvData.add(row);
                    }
                    i++;

                }

                tabela.setItems(csvData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File Not Found");
        }

    }
    
    public void insertTeste(String dir) {
        tabelaTeste.getColumns().clear();
        List<String> columns = new ArrayList<String>();
        List<String> rows = new ArrayList<String>();
        ObservableList<ObservableList<String>> csvData = FXCollections.observableArrayList();
        String div = ";";
        File f = new File(dir);
        if (f.exists() && !f.isDirectory()) {

            try (
                    FileReader fin = new FileReader(f);
                    BufferedReader in = new BufferedReader(fin);) {
                String l;
                int i = 0;

                while ((l = in.readLine()) != null) {

                    if (i < 1) {
                        if (l.contains(",")) {
                            div = ",";
                        }

                        String[] headers = l.split(div);

                        for (String w : headers) {
                            columns.add(w);

                        }
                        for (int k = 0; k < columns.size(); k++) {
                            final int finalIdx = k;
                            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(k));

                            // column.setText("hghjghjg");
                            column.setCellValueFactory(
                                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));

                            /*
                             * System.out.println(new
                             * ReadOnlyObjectWrapper<>(param.getValue().get(
                             * finalIdx)));
                             */
                            tabelaTeste.getColumns().add(column);
                        }

                        // tableView.getColumns().addAll(tableColumns);
                    } else {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        row.clear();
                        String[] items = l.split(div);
                        for (String item : items) {
                            row.add(item);
                        }
                        csvData.add(row);
                    }
                    i++;

                }

                tabelaTeste.setItems(csvData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File Not Found");
        }

    }

    @FXML
    private void evtTreinar(ActionEvent event) {
        double erro = Double.parseDouble(txErro.getText());
        int tp_act = cbTpFunc.getSelectionModel().getSelectedIndex();
        
        int camadas = Integer.parseInt(txOculta.getText());
        double txApredizagem = Double.parseDouble(txAprendizagem.getText());
        double epoca = Double.parseDouble(txItera.getText());
        ArrayList<DataSet> ds;
        ArrayList<DataSet> teste;
        
        if(!carrega.isNormalizado())
            carrega.normalizar();
        
        if(ckRandon.isSelected()){
            ds = carrega.randomDS(carrega.getDataset());
            teste = carrega.randomDS(carrega.getDsTeste());
        }else{
            ds = carrega.getDataset();
            teste = carrega.getDsTeste();
        }
        
        
        pbIteracoes.setProgress(0.0);
        pbTeste.setProgress(0.0);
        rn.iniciar(carrega.getIentradas(), camadas, carrega.getIsaida());
        rn.setTpSaidas(carrega.getTpSaidas());
        
        if(ckFould.isSelected()){
            
        }else{
            treino_N(erro,epoca, txApredizagem, tp_act, ds, teste);
        }
    }
    
    public void treino_N(double erro, double epoca, double txApredizagem, int tp_act, ArrayList<DataSet> ds,ArrayList<DataSet> teste ){
        new Thread() {

            @Override
            public void run() {
                boolean flagErro = true;
                try {
                    for (int i = 0; i < epoca && flagErro; i++) {
                        pbIteracoes.setProgress(i / epoca);
                        //Epoca, Camada,tp_act,taxaA
                        rn.treinar(ds, teste, tp_act, txApredizagem);
                        Thread.sleep(5);

                        if(erro>=rn.getRedeErro())
                            flagErro = false;
                    }
                    
                    pbIteracoes.setProgress(1.0);
                    rn.iniciarTeste();
                    
                    double iTeste = Double.parseDouble(""+teste.size());
                    for (int i = 0; i < teste.size(); i++) {
                        rn.testar(teste.get(i),tp_act);
                        pbTeste.setProgress(i/iTeste);
                        Thread.sleep(5);
                    }
                    
                    pbTeste.setProgress(1.0);
                    btExibe.setVisible(true);
                    
                } catch (Exception ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
        }.start();
    }


    @FXML
    private void evtExibe(ActionEvent event) {
        txConfNormal.setText(rn.exibeMatriz());
        graficoE.getData().clear();
        
        XYChart.Series series = new XYChart.Series();
        series.setName("Erros do Treinamento");
        ArrayList erros = rn.getLogErro();
        
        for (int i = 0; i < erros.size(); i++) {
            series.getData().add(new XYChart.Data("" + i, erros.get(i)));

        }
        graficoE.getData().add(series);
    }

    @FXML
    private void evtFould(ActionEvent event) {
        if(ckFould.isSelected()){
            btTreinar.setDisable(false);
            tabK.setDisable(false);
            tabNormal.setDisable(true);
        }else{
            if(!flagTeste)
                btTreinar.setDisable(true);
            tabK.setDisable(true);
            tabNormal.setDisable(false);
        }
    }

    @FXML
    private void evtCarregaTeste(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String diretorio = selectedFile.getAbsolutePath();
            diretorio = diretorio.replace("\\", "/");

            if (carrega.carregaTeste(diretorio)) {

                insertTeste(diretorio);
                btTreinar.setDisable(false);
                tabTeste.setDisable(false);
                flagTeste = true;
                
            }
        }
    }

}
