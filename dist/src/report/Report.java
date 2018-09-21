/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package report;

import dao.config.BaseDeDatosUCC;
import domain.ResultadoJuego;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import model.AumentoTableModel;
import model.ResultadoJuegoTableModel;
import model.ResultadoNivelTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import ucc.JuegosUCC;
import util.Mensaje;
import util.Metodos;

public class Report {
    
    private Connection conn;
    private JuegosUCC juegosUCC;
    
    public Report() {
        try {
            BaseDeDatosUCC controladorDB = new BaseDeDatosUCC();
            String user = controladorDB.getUsuario(controladorDB.cargarConfiguracionDB());
            String pass = controladorDB.getClave(controladorDB.cargarConfiguracionDB());
            String basd = controladorDB.getNombreDB(controladorDB.cargarConfiguracionDB());
            String host = controladorDB.getHost(controladorDB.cargarConfiguracionDB());
            Class.forName("com.mysql.jdbc.Driver"); //se carga el driver
            String url = "jdbc:mysql://" + host + "/" + basd;
            conn = DriverManager.getConnection(url, user, pass);
            juegosUCC = new JuegosUCC();
        } catch (Exception ex) {
            System.out.println("errrrrrrrrrrrrrrrrrror: " + ex);
            ex.printStackTrace();
        }
    }
    
    public void printReportView(JasperReport masterReport, Map parametro, String titul) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, conn);
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            visualizacionReporte(jviewer, titul);
        } catch (JRException ex) {
            System.out.println("Error en el reporte: " + ex);
        }
    }
    
    public void printReport(JasperReport masterReport, Map parametro, JRTableModelDataSource datos, String titul) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametro, datos);
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            visualizacionReporte(jviewer, titul);

        } catch (JRException ex) {
            System.out.println("Error en el reporte: " + ex);
        }
    }
    
    public void visualizacionReporte(JasperViewer jviewer, String titul) {

        JDialog vistaPrevia = new JDialog(jviewer, true);
        vistaPrevia.setTitle(titul);
        vistaPrevia.setContentPane(jviewer.getContentPane());
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        vistaPrevia.setBounds(100, 100, d.width - 200, d.height - 200);
        vistaPrevia.setLocationRelativeTo(null);
        vistaPrevia.validate();

        vistaPrevia.setVisible(true);
    }
    
    public void printResultados(ResultadoJuegoTableModel rjtm, String usuario) {
        try {

            String reporte = System.getProperty("user.dir") + "/src/report/ReporteResultado.jasper";
            if (reporte == null) {
                Mensaje.mensajeError("ERROR DEL ARCHIVO MAESTRO");
                return;
            }
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(reporte);
            } catch (Exception jre) {
                JOptionPane.showMessageDialog(null, "Problema al cargar archivo maestro", "ERROR REPORTE", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error: " + jre);
                return;
            }
            Map parametro = new HashMap();
            parametro.put("user", usuario);

            JRTableModelDataSource datosIngredientes = new JRTableModelDataSource(rjtm);
            printReport(masterReport, parametro, datosIngredientes, "REPORTE DE RESULTADOS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printResultadoNiveles(ResultadoNivelTableModel rjtm, String usuario, ResultadoJuego rj) {
        try {

            String reporte = System.getProperty("user.dir") + "/src/report/ReporteNiveles.jasper";
            if (reporte == null) {
                Mensaje.mensajeError("ERROR DEL ARCHIVO MAESTRO");
                return;
            }
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(reporte);
            } catch (Exception jre) {
                JOptionPane.showMessageDialog(null, "Problema al cargar archivo maestro", "ERROR REPORTE", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error: " + jre);
                return;
            }
            Map parametro = new HashMap();
            parametro.put("user", usuario);
            parametro.put("juego", rj.getNombreJuego());
            parametro.put("alumno", rj.getAlumno().toString());
            parametro.put("fecha", Metodos.formFechaToLarge(rj.getFechaJuego()));

            JRTableModelDataSource datosIngredientes = new JRTableModelDataSource(rjtm);
            printReport(masterReport, parametro, datosIngredientes, "REPORTE DE RESULTADOS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printAumento(AumentoTableModel atm){
        try {

            String reporte = System.getProperty("user.dir") + "/src/report/ReportAumento.jasper";
            if (reporte == null) {
                Mensaje.mensajeError("ERROR DEL ARCHIVO MAESTRO");
                return;
            }
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObject(reporte);
            } catch (Exception jre) {
                JOptionPane.showMessageDialog(null, "Problema al cargar archivo maestro", "ERROR REPORTE", JOptionPane.ERROR_MESSAGE);
                System.out.println("Error: " + jre);
                return;
            }
            Map parametro = new HashMap();            

            JRTableModelDataSource datosIngredientes = new JRTableModelDataSource(atm);
            printReport(masterReport, parametro, datosIngredientes, "REPORTE DE RESULTADOS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
