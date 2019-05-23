/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redeneural;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import redeneural.Rede.CamadaOculta;
import redeneural.Rede.Rede;

/**
 *
 * @author marceloimamura
 */
public class RedeNeural extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        Rede r = new Rede();
        r.iniciar(2, 1, 2);
        ArrayList<CamadaOculta> oculta = r.getRede();
        CamadaOculta c = oculta.get(0);
        
        double[][] peso = new double[2][2];
        peso[0] = new double[2];
        peso[1] = new double[2];
        
        peso[0][0] = -1.1;
        peso[0][1] = 1.4;
        
        peso[1][0] = 3.6;
        peso[1][1] = -2.1;
        
        c.setPeso(peso);
        c = oculta.get(1);
        peso = new double[2][2];
        peso[0] = new double[2];
        peso[1] = new double[2];
        
        peso[0][0] = 2.1;
        peso[0][1] = 3.2;
        
        peso[1][0] = 1.2;
        peso[1][1] = 1.6;
                
        c.setPeso(peso);

        r.exibeRede();
        
        double[] entrada = new double[2];
        entrada[0] = 1.0;
        entrada[1] = 0.0;
        
        r.testarRede(entrada, 0);
        r.calcular_erros(entrada, 0, 1);
        r.atualizar_pesos(entrada, 0.5);
        

        double resul = 2.1 + 0.5 * -0.65 * -0.55;
        System.out.println(resul);
        
        System.exit(0);
        
    }
    
}
