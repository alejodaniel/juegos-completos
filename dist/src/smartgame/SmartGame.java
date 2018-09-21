/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartgame;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import javax.swing.UIManager;
import util.Mensaje;
import views.CargaV;

public class SmartGame {

    public static void main(String[] args) {
        try {
            NimRODTheme nt= new NimRODTheme(System.getProperty("user.dir")+ "/lib/NimRODThemeFile.theme");
            NimRODLookAndFeel nr = new NimRODLookAndFeel();
            nr.setCurrentTheme(nt);
            UIManager.setLookAndFeel(nr);
            new CargaV(null, true).setVisible(true);
        } catch (Exception ex) {
            System.out.println("error al iniciar: " + ex);
            Mensaje.mensajeError("Error al iniciar el sistema: " + ex + "\nConsulte al administrador..");
        }
    }
}
