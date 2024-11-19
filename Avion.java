public class Avion {

    private String id;
    private String modelo;
    private int cantVuelos;
    private int cantAsientos;
    private int kmRecorridos;

    //Constructores
    public Avion(String id,String modelo,int cantVuelos,int cantAsientos,int kmRecorridos){
        this.id=id;
        this.modelo=modelo;
        this.cantVuelos=cantVuelos;
        this.cantAsientos=cantAsientos;
        this.kmRecorridos=kmRecorridos;
    }
    public Avion(String id,String modelo,int cantAsientos){
        this.id=id;
        this.modelo=modelo;
        this.cantVuelos=0;
        this.cantAsientos=cantAsientos;
        this.kmRecorridos=0;
    }

    //Observadores
    public int getCantAsientos() {
        return cantAsientos;
    }
    public int getCantVuelos() {
        return cantVuelos;
    }
    public String getId() {
        return id;
    }
    public int getKmRecorridos() {
        return kmRecorridos;
    }
    public String getModelo() {
        return modelo;
    }


    public String toString(){
        return "ID:"+this.id+
        "\nModelo:"+this.modelo+
        "\nVuelos:"+this.cantVuelos+
        "\nAsientos:"+this.cantAsientos+
        "\nKilometros recorridos:"+this.kmRecorridos;
    }

}
