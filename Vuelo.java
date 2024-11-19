
public class Vuelo{
    private String id;
    private String avion;
    private String ruta;
    private String dia;
    private String hora;

    //Constructores
    public Vuelo(String id,String avion,String ruta,String dia,String hora){
        this.id=id;
        this.avion=avion;
        this.ruta=ruta;
        this.dia=dia;
        this.hora=hora;
    }
    //Observadores
    public String getAvion() {
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
    public String getRuta() {
        return ruta;
    }
    
    public String toString(){
        return "ID:"+this.id+
        "\nAvion:"+this.avion+
        "\nRuta:"+this.ruta+
        "\nDia:"+this.dia+
        "\nHora:"+this.hora;
    }

}