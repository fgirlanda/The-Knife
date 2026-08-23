package com.gruppo10;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.gruppo10.servizi_int.AuthServiceInt;
import com.gruppo10.servizi_int.GeoServiceInt;
import com.gruppo10.servizi_int.PreferitiServiceInt;
import com.gruppo10.servizi_int.ProfiloServiceInt;
import com.gruppo10.servizi_int.RecensioniServiceInt;
import com.gruppo10.servizi_int.RistorantiServiceInt;


/**
 * Si connette al registro RMI del server e tiene pronti i 5 stub remoti da
 * passare ai controller della GUI.
 *
 * <p>
 * Corrisponde all'attore "Client" (ClientTK) del diagramma di sequenza di
 * login nella parte di lookup; separato dalla classe di bootstrap
 * {@link ClientTK} per lo stesso motivo per cui {@link ServerContext} è
 * separato da {@link ServerTK} lato server.
 * </p>
 */
public class ClientContext {

    private final String HOST = "192.168.1.109";
    private final int PORTA = 1099;

    private AuthServiceInt authService;
    private GeoServiceInt geoService;
    private RistorantiServiceInt ristorantiService;
    private RecensioniServiceInt recensioniService;
    private PreferitiServiceInt preferitiService;
    private ProfiloServiceInt profiloService;

    /** Corrisponde a "Client->>Reg: lookup(...)" ripetuto per i 6 servizi. */
    public void connetti() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(HOST, PORTA);
        this.authService = (AuthServiceInt) registry.lookup("AuthService");
        this.geoService = (GeoServiceInt) registry.lookup("GeoService");
        this.ristorantiService = (RistorantiServiceInt) registry.lookup("RistorantiService");
        this.recensioniService = (RecensioniServiceInt) registry.lookup("RecensioniService");
        this.preferitiService = (PreferitiServiceInt) registry.lookup("PreferitiService");
    }

    public AuthServiceInt getAuthService() {
        return authService;
    }

    public GeoServiceInt getGeoService() {
        return geoService;
    }

    public RistorantiServiceInt getRistorantiService() {
        return ristorantiService;
    }

    public RecensioniServiceInt getRecensioniService() {
        return recensioniService;
    }

    public PreferitiServiceInt getPreferitiService() {
        return preferitiService;
    }

    public ProfiloServiceInt getProfiloService() {
        return profiloService;
    }
}