package com.gruppo10;

import javafx.application.Application;

/**
 * Classe di bootstrap del server JavaFX dell'applicazione.
 * Avvia la schermata di amministrazione {@link ServerTK}.
 */
public class ServerBoot {
    
    /**
     * Metodo principale di avvio del server.
     *
     * @param args argomenti della riga di comando
     */
    public static void main(String[] args) {
        String mioIp = ottieniIpReale();
        System.setProperty("java.rmi.server.hostname", mioIp);
        System.out.println("IP RMI impostato correttamente all'avvio: " + mioIp);
        
        Application.launch(ServerTK.class, args);
    }

    /**
     * Trova automaticamente il vero indirizzo IP di rete della macchina
     * scartando gli indirizzi di loopback (127.0.x.x).
     */
    private static String ottieniIpReale() {
        try {
            java.util.Enumeration<java.net.NetworkInterface> interfacce = java.net.NetworkInterface.getNetworkInterfaces();
            while (interfacce.hasMoreElements()) {
                java.net.NetworkInterface scheda = interfacce.nextElement();
                if (scheda.isLoopback() || !scheda.isUp() || scheda.isVirtual()) continue;
                
                java.util.Enumeration<java.net.InetAddress> indirizzi = scheda.getInetAddresses();
                while (indirizzi.hasMoreElements()) {
                    java.net.InetAddress ip = indirizzi.nextElement();
                    if (ip instanceof java.net.Inet4Address && !ip.isLoopbackAddress()) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "localhost";
    }
}