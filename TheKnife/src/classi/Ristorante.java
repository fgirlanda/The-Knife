package classi;

public class Ristorante {
    private String nomeRistorante;
    private Location location;
    private boolean delivery;
    private boolean prenotazioneOnline;
    private String tipoCucina;
    public String getNomeRistorante() {
        return nomeRistorante;
    }
    public void setNomeRistorante(String nomeRistorante) {
        this.nomeRistorante = nomeRistorante;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public boolean isDelivery() {
        return delivery;
    }
    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }
    public boolean isPrenotazioneOnline() {
        return prenotazioneOnline;
    }
    public void setPrenotazioneOnline(boolean prenotazioneOnline) {
        this.prenotazioneOnline = prenotazioneOnline;
    }
    public String getTipoCucina() {
        return tipoCucina;
    }
    public void setTipoCucina(String tipoCucina) {
        this.tipoCucina = tipoCucina;
    }
}
