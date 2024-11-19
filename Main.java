
//import java.io.*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static Avion[] listaAviones = new Avion[100];
    static Ruta[] listaRutas = new Ruta[20];
    static Vuelo[][] horarios = new Vuelo[15][7];

    public static void main(String[] args) {

        leerArchivos("aviones");
        leerArchivos("rutas");
        leerArchivos("vuelos");

        menu();

    }

    public static void menu() {

        Scanner scanner = new Scanner(System.in);

        boolean bucle = true;

        int opcion;

        do {

            System.out.println("=== MENÚ PRINCIPAL ===");
            System.out.println("1. Cargar datos desde archivos");
            System.out.println("2. Agregar un nuevo avión");
            System.out.println("3. Agregar un nuevo vuelo");
            System.out.println("4. Mostrar información de un avión");
            System.out.println("5. Calcular promedio de pasajeros (recursivo)");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Llamar a métodos para cargar datos
                    break;
                case 2:
                    // Lógica para agregar un avión

                    break;
                case 3:
                    // Lógica para agregar un vuelo
                    System.out.println("2. Agregar un nuevo avión");
                    System.out.println("3. Agregar un nuevo vuelo");
                    System.out.println("4. Mostrar información de un avión");
                    System.out.println("5. Calcular promedio de pasajeros (recursivo)");
                    agregarVuelo();
                    break;
                case 4:
                    // Lógica para mostrar información de un avión
                    break;
                case 5:
                    // Llamar a cálculo recursivo del promedio
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        scanner.close();

    }

    public static void ejecutarInstrucciones(String instruccion) {

        switch (instruccion) {
            case "S":
                System.out.println("S - SALIR");
                break;
            case "elegirCarga":
                System.out.println("Elija una opcion");
                System.out.println("A - Aviones");
                System.out.println("R - Rutas");
                System.out.println("H - Horarios");
                break;
            case "imprimirObjetos":
                System.out.println("Elija una opcion");
                System.out.println("A - Aviones");
                break;
            default:
                break;
        }

    }

    public static void imprimirOpciones(String menu) {

        switch (menu) {
            case "principal":
                System.out.println("Elija una opcion");
                System.out.println("S - SALIR DEL PROGRAMA");
                System.out.println("L - Carga");
                System.out.println("I - Mostrar Objetos");
                break;
            case "elegirCarga":
                System.out.println("Elija una opcion");
                System.out.println("A - Aviones");
                System.out.println("R - Rutas");
                System.out.println("H - Horarios");
                break;
            case "imprimirObjetos":
                System.out.println("Elija una opcion");
                System.out.println("A - Aviones");
                break;
            default:
                break;
        }

    }

    public static int traductorHoras(String hora) {

        hora = hora.split(";")[0];

        int elegido = stringAEntero(hora) - 8;

        return elegido;

    }

    public static int traductorDias(String dia) {

        int elegido;

        switch (dia) {
            case "Lunes":
                elegido = 0;
                break;
            case "Martes":
                elegido = 1;
                break;
            case "Miercoles":
                elegido = 2;
                break;
            case "Jueves":
                elegido = 3;
                break;
            case "Viernes":
                elegido = 4;
                break;
            case "Sabado":
                elegido = 5;
                break;
            case "Domingo":
                elegido = 6;
                break;
            default:
                elegido = -1;
                break;
        }

        return elegido;

    }

    public static boolean agregarAvion(String identificacion, String modelo, int cantidadAsientos) {

        boolean valido = true;
        int i = 0;
        int posNul = 0;

        if (!esIdentificacionValida(identificacion)) {
            System.out.println("Identificacion invalida. Debe cumplir con el formato (ej. LV-XXX o LQ-AAA).");
            valido = false;
        }

        while (valido && i < listaAviones.length) {
            if (listaAviones[i] != null) {
                if (listaAviones[i].getId().equals(identificacion)) {
                    System.out.println("Error: ya existe un avion con la misma identificacion.");
                    valido = false;
                }
            } else {
                posNul = i;
            }
            i++;
        }

        if (valido) {

            listaAviones[posNul] = new Avion(identificacion, modelo, cantidadAsientos);

            System.out.println("Avión agregado correctamente.");

        }

        return valido;
    }

    public static boolean esIdentificacionValida(String identificacion) {

        boolean esValida = true;
        int i = 0;

        String[] split = identificacion.split("-");
        String parteA = split[0];
        String parteB = split[1].toUpperCase();

        if ((parteA.equals("LV") && (parteB.charAt(0) != 'X' && parteB.charAt(0) != 'S' )) || parteA.equals("LQ")) {
            while (esValida && i < parteB.length()) {
                if ((parteB.charAt(i) < 'A') || parteB.charAt(i) > 'Z') {
                    esValida = false;
                }
                i++;
            }
        } else if (parteA.equals("LV") || parteA.equals("LQ")) {

            if (parteB.charAt(1) == 'X') {
                i = 2;
            } else {
                i = 1;
            }

            while (esValida && i < parteB.length()) {
                if ((parteB.charAt(i) < '0') || parteB.charAt(i) > '9') {
                    esValida = false;
                }
                i++;
            }

        } else {

            esValida = false;

        }

        return esValida;
    }

    public static void leerArchivos(String camino) {

        String nombreArchivoEntrada = "C:\\Users\\naran\\Desktop\\Programacion\\java\\java-2\\" + camino + ".txt";

        String linea = null;

        try {

            FileReader lectorArchivo = new FileReader(nombreArchivoEntrada);

            BufferedReader bufferLectura = new BufferedReader(lectorArchivo);

            int filas = 0;

            while ((linea = bufferLectura.readLine()) != null) {

                String[] split = linea.split(";");

                switch (camino) {
                    case "aviones":
                        listaAviones[filas] = new Avion(split[0], split[1], stringAEntero(split[2]),
                                stringAEntero(split[3]), stringAEntero(split[4]));
                        break;
                    case "rutas":
                        listaRutas[filas] = new Ruta(split[0], split[1], split[2], stringAEntero(split[3]),
                                (split[4].equals("Si")));
                        break;
                    case "vuelos":
                        horarios[traductorDias(split[3])][traductorHoras(split[4])] = new Vuelo(split[0], split[1],
                                split[2], split[3], split[4]);
                        break;
                    default:
                        break;
                }

                filas++;

            }
            bufferLectura.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + "\nSignifica que el archivo del "
                    + "que queriamos leer no existe.");
        } catch (IOException ex) {
            System.err.println("Error leyendo o escribiendo en algun archivo.");
        }

    }

    public static int stringAEntero(String texto) {

        int num = 0;

        for (int i = 0; i < texto.length(); i++) {
            num += texto.charAt(i) * (10 ^ (texto.length() - 1 - i));
        }

        return num;

    }

    public static boolean agregarVuelo(String id, String avion, String ruta, String dia, String hora) {
        boolean creado = true;
        return creado;
    }
}
