
public class Ruta{

    private String id;
    private String ciudadOrigen;
    private String ciudadDestino;
    private int distancia;
    private boolean esInternacional;

    //Constructores
    public Ruta(String id,String ciudadOrigen,String ciudadDestino,int distancia,boolean esInternacional){
        this.id=id;
        this.ciudadOrigen=ciudadOrigen;
        this.ciudadDestino=ciudadDestino;
        this.distancia=distancia;
        this.esInternacional=esInternacional;
    }

    //Observadores
    public String getCiudadDestino() {
        return ciudadDestino;
    }
    public String getCiudadOrigen() {
        return ciudadOrigen;
    }
    public int getDistancia() {
        return distancia;
    }
    public String getId() {
        return id;
    }
    
    public String toString(){
        return "ID:"+this.id+
        "\nOrigen:"+this.ciudadOrigen+
        "\nDestino:"+this.ciudadDestino+
        "\nDistancia:"+this.distancia+
        "\nEs internacional:"+this.esInternacional;
    }

}