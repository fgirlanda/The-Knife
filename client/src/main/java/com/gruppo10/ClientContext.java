/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
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

    /** Nome host del server che pubblica i servizi RMI. */
    private final String HOST = "theknife-server.local";

    /** Porta del registro RMI utilizzato dal server. */
    private final int PORTA = 1099;

    /** Servizio di autenticazione. */
    private AuthServiceInt authService;
    /** Servizio di geolocalizzazione. */
    private GeoServiceInt geoService;
    /** Servizio per ristoranti. */
    private RistorantiServiceInt ristorantiService;
    /** Servizio per recensioni. */
    private RecensioniServiceInt recensioniService;
    /** Servizio per preferiti. */
    private PreferitiServiceInt preferitiService;
    /** Servizio per profilo. */
    private ProfiloServiceInt profiloService;

    /**
     * Recupera dal registro RMI gli stub dei sei servizi remoti dell'applicazione.
     *
     * @throws RemoteException se il registro non è raggiungibile
     * @throws NotBoundException se un servizio non è pubblicato con il nome atteso
     */
    public void connetti() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(HOST, PORTA);
        this.authService = (AuthServiceInt) registry.lookup("AuthService");
        this.geoService = (GeoServiceInt) registry.lookup("GeoService");
        this.ristorantiService = (RistorantiServiceInt) registry.lookup("RistorantiService");
        this.recensioniService = (RecensioniServiceInt) registry.lookup("RecensioniService");
        this.preferitiService = (PreferitiServiceInt) registry.lookup("PreferitiService");
    }

    /**
     * Restituisce il servizio remoto per autenticazione e registrazione.
     *
     * @return stub del servizio di autenticazione
     */
    public AuthServiceInt getAuthService() {
        return authService;
    }

    /**
     * Restituisce il servizio remoto di geocodifica.
     *
     * @return stub del servizio geografico
     */
    public GeoServiceInt getGeoService() {
        return geoService;
    }

    /**
     * Restituisce il servizio remoto per la gestione dei ristoranti.
     *
     * @return stub del servizio ristoranti
     */
    public RistorantiServiceInt getRistorantiService() {
        return ristorantiService;
    }

    /**
     * Restituisce il servizio remoto per la gestione delle recensioni.
     *
     * @return stub del servizio recensioni
     */
    public RecensioniServiceInt getRecensioniService() {
        return recensioniService;
    }

    /**
     * Restituisce il servizio remoto per la gestione dei preferiti.
     *
     * @return stub del servizio preferiti
     */
    public PreferitiServiceInt getPreferitiService() {
        return preferitiService;
    }

    /**
     * Restituisce il servizio remoto per la gestione del profilo.
     *
     * @return stub del servizio profilo
     */
    public ProfiloServiceInt getProfiloService() {
        return profiloService;
    }
}