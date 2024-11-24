
public class Vuelo{
    private String id;
    private Avion avion;
    private Ruta ruta;
    private String dia;
    private String hora;
    private boolean volo;

    //Constructores
    public Vuelo(String id,Avion avion,Ruta ruta,String dia,String hora){
        this.id=id;
        this.avion=avion;
        this.ruta=ruta;
        this.dia=dia;
        this.hora=hora;
        volo = false;
    }
    //Observadores
    public Avion getAvion() {
        return avion;
    }
    public String getDia() {
        return dia;
    }
    public String getHora() {
        return hora;
    }
    public String getId() {
        return id;
    }
    public Ruta getRuta() {
        return ruta;
    }
    public boolean getVolo() {
        return volo;
    }
    
    public String toString(){
        return "ID:"+this.id+
        "\nAvion: "+this.avion.toString()+
        "\nRuta: "+this.ruta.toString()+
        "\nDia:"+this.dia+
        "\nHora:"+this.hora+
        "\nVolo:"+this.volo;
    }

}