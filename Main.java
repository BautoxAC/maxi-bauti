
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
    static Vuelo[][] horarios = new Vuelo[7][15];
    static Vuelo[] vuelosEnRango = null;

    public static void main(String[] args) {
        leerArchivos("aviones");
        leerArchivos("rutas");
        leerArchivos("vuelos");

        menu();
    }

    public static void menu() {

        Scanner scanner = new Scanner(System.in);

        boolean bucle = true;

        String opcionTexto;

        do {

            System.out.println("=== MENÚ PRINCIPAL ===");
            System.out.println("1. Guardar");
            System.out.println("2. Agregar un nuevo avión");
            System.out.println("3. Agregar un nuevo vuelo");
            System.out.println("4. Mostrar información de un avión");
            System.out.println("5. Calcular promedio de pasajeros (recursivo)");
            System.out.println("7. Devolver o mostrar vuelos con cierta distancia");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcionTexto = scanner.nextLine();

            switch (opcionTexto) {
                case "1":
                    // Llamar a métodos para cargar datos
                    guardarArchivos("aviones");
                    break;
                case "2":
                    agregarAvion();
                    break;
                case "3":
                    // agregarVuelo();
                    agregarVuelo(id, opcionTexto, opcionTexto, opcionTexto, opcionTexto, opcionTexto);
                    break;
                case "4":
                    mostrarInformacionAvion();
                    break;
                case "5":
                    // Llamar a cálculo recursivo del promedio
                    break;
                case "7":
                    vuelosEnRango();
                    break;
                case "8":

                    break;
                case "0":
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (!opcionTexto.equals("0"));

        scanner.close();

    }

    public static void mostrarInformacionAvion() {

        Scanner scanner = new Scanner(System.in);

        String opcionTexto;
        System.out.println("Ingrese la ID del avion");
        opcionTexto = scanner.nextLine();

        if (esIdentificacionValida(opcionTexto)) {
            Avion avion = idAAvion(opcionTexto);

            if (avion != null) {
                System.out.println("Avion encontrado, sus datos son los siguientes:");
                System.out.println(avion.toString());
            } else {
                System.out.println("ERROR, avion no encontrado");
            }
        } else {
            System.out.println("ID invalida");
        }

    }

    public static void vuelosEnRango() {

        Scanner scanner = new Scanner(System.in);

        int primerRango;
        int segundoRango;
        int dim = 0;

        System.out.println("1. Mostrar vuelos en rango");
        System.out.println("2. Ingresar rangos para obtener los skibidi");

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":

                if (vuelosEnRango != null) {

                    for (int i = 0; i < vuelosEnRango.length; i++) {
                        System.out.println(vuelosEnRango[i].toString());
                        System.out.println("");
                    }

                } else {
                    System.out.println("No existen vuelos en rango");
                }

                break;
            case "2":

                System.out.println("Ingrese el primer rango de distancia");
                primerRango = stringAEntero(scanner.nextLine());
                System.out.println("Ingrese el segundo rango de distancia");
                segundoRango = stringAEntero(scanner.nextLine());

                if (primerRango >= 0 && segundoRango >= 0) {

                    if (primerRango > segundoRango) {
                        int aux = primerRango;
                        primerRango = segundoRango;
                        segundoRango = aux;
                    }

                    vuelosEnRango = devolverArregloVuelosEnRango(primerRango, segundoRango);

                    System.out.println("Vuelos guardados con los rangos " + primerRango + " y " + segundoRango);

                } else {
                    System.out.println("Rangos invalidos, son negativos o se ingreso una letra");
                }

                break;
            default:
                System.out.println("ERROR, opcion incorrecta o mal ingresada");
                break;

        }

    }

    public static Vuelo[] devolverArregloVuelosEnRango(int primerRango, int segundoRango) {

        Scanner scanner = new Scanner(System.in);

        Vuelo[] vuelos;
        int dim = 0;

        for (int i = 0; i < horarios.length; i++) {
            for (int j = 0; j < horarios.length; j++) {
                if (horarios[i][j] != null) {
                    int distancia = horarios[i][j].getRuta().getDistancia();
                    if (distancia >= primerRango && distancia <= segundoRango) {
                        dim++;
                    }
                }
            }
        }

        vuelos = new Vuelo[dim];

        dim = 0;

        for (int i = 0; i < horarios.length; i++) {
            for (int j = 0; j < horarios.length; j++) {
                if (horarios[i][j] != null) {
                    int distancia = horarios[i][j].getRuta().getDistancia();
                    if (distancia >= primerRango && distancia <= segundoRango) {
                        vuelos[dim] = horarios[i][j];
                        dim++;
                    }
                }
            }
        }

        return vuelos;

    }

    public static int traductorHoras(String hora) {

        hora = hora.split(":")[0];

        int elegido = stringAEntero(hora) - 8;

        return elegido;

    }

    public static int traductorDias(String dia) {

        int elegido;

        switch (dia) {
            case "lunes":
                elegido = 0;
                break;
            case "martes":
                elegido = 1;
                break;
            case "miercoles":
                elegido = 2;
                break;
            case "jueves":
                elegido = 3;
                break;
            case "viernes":
                elegido = 4;
                break;
            case "sabado":
                elegido = 5;
                break;
            case "domingo":
                elegido = 6;
                break;
            default:
                elegido = -1;
                break;
        }

        return elegido;

    }

    public static void agregarAvion() {

        Scanner scanner = new Scanner(System.in);

        boolean valido = true;
        int i = 0;
        int posNul = -1;
        String identificacion;
        boolean frenar = false;

        System.out.println("Ingrese una identificacion para el avion.");

        identificacion = scanner.nextLine();

        if (!esIdentificacionValida(identificacion)) {
            System.out.println("Identificacion invalida. Debe cumplir con el formato (Ej: LV-ZXS o LQ-AAA o LV-X888)");
            valido = false;
        }

        while (!frenar && valido && i < listaAviones.length) {
            if (listaAviones[i] != null) {
                if (listaAviones[i].getId().equals(identificacion)) {
                    System.out.println("Error: ya existe un avion con la misma identificacion.");
                    valido = false;
                }
            } else {
                posNul = i;
                frenar = true;
            }
            i++;
        }

        if (posNul == -1) {
            System.out.println("Error, la cantidad de aviones esta al maximo");
            valido = false;
        }

        if (valido) {

            System.out.println("Id Valido, ingrese el modelo");
            String modelo = scanner.nextLine();
            System.out.println("Ingrese la cantidad de asientos");
            int cantidadAsientos = stringAEntero(scanner.nextLine());

            System.out.println(cantidadAsientos);

            if (cantidadAsientos == -1) {
                valido = false;
            } else {
                listaAviones[posNul] = new Avion(identificacion, modelo, cantidadAsientos);
                guardarArchivos("aviones");
            }

        }

        if (valido) {
            System.out.println("Avion agregado correctamente.");
        } else {
            System.out.println("No se pudo agregar el avion");
        }

    }

    public static boolean esIdentificacionValida(String identificacion) {

        boolean esValida = true;
        int i = 0;
        int max = 3;

        String[] split = identificacion.split("-");
        String parteA = split[0];

        if (split.length == 2) {
            String parteB = split[1];
            if ((parteA.equals("LV") && (parteB.charAt(0) != 'X' && parteB.charAt(0) != 'S')) || parteA.equals("LQ")) {
                if (parteB.length() - max != 0) {
                    esValida = false;
                }
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

                max += i;

                if (parteB.length() - max != 0) {
                    esValida = false;
                }

                while (esValida && i < max) {
                    if ((parteB.charAt(i) < '0') || parteB.charAt(i) > '9') {
                        esValida = false;
                    }
                    i++;
                }
            } else {
                esValida = false;
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

                        int fila = traductorDias(split[3].toLowerCase());
                        int columna = traductorHoras(split[4]);

                        horarios[fila][columna] = new Vuelo(split[0], idAAvion(split[1]),
                                idARuta(split[2]), split[3], split[4]);
                        break;
                    default:
                        break;
                }

                filas++;

            }
            bufferLectura.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + "No existe el archivo.");
        } catch (IOException ex) {
            System.err.println("Error leyendo o escribiendo en algun archivo.");
        }

    }

    public static void guardarArchivos(String camino) {

        String nombreArchivoEntrada = "C:\\Users\\naran\\Desktop\\Programacion\\java\\java-2\\" + camino + ".txt";
        String nombreArchivoSalida = nombreArchivoEntrada;

        String linea = null;

        try {

            FileReader lectorArchivo = new FileReader(nombreArchivoEntrada);
            FileWriter escritorArchivo = new FileWriter(nombreArchivoSalida);

            BufferedReader bufferLectura = new BufferedReader(lectorArchivo);
            BufferedWriter bufferEscritura = new BufferedWriter(escritorArchivo);

            int fila = 0;
            int columna = 0;
            String guardar = "";
            boolean bucle = true;

            while (bucle) { // (linea = bufferLectura.readLine()) != null

                linea = bufferLectura.readLine();

                System.out.println(linea);

                switch (camino) {
                    case "aviones":
                        if (listaAviones[fila] != null) {
                            Avion avion = listaAviones[fila];
                            guardar += (avion.getId() + ";" + avion.getModelo() + ";" +
                                    avion.getCantVuelos() + ";" + avion.getCantAsientos()
                                    + ";"
                                    + avion.getKmRecorridos() + "\n");
                        } else {
                            bucle = false;
                        }
                        break;
                    case "vuelos":
                        if (horarios[fila][columna] != null) {
                            Vuelo vuelo = horarios[fila][columna];
                            guardar += (vuelo.getId() + ";" + vuelo.getAvion().getId() + ";" +
                                    vuelo.getRuta().getId() + ";" + vuelo.getDia()
                                    + ";"
                                    + vuelo.getHora() + "\n");
                        } else {
                            bucle = false;
                        }
                        break;
                    default:
                        break;
                }

                fila++;
                columna++;

            }
            bufferEscritura.write(guardar);
            bufferLectura.close();
            bufferEscritura.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage() + "No existe el archivo.");
        } catch (IOException ex) {
            System.err.println("Error leyendo o escribiendo en algun archivo.");
        }

    }

    public static int stringAEntero(String texto) {
        int num = 0;
        boolean valido = true;
        int i = 0;

        // Hacemos un for el cual recorre el numero que esta en String, cada caracter le
        // restamos 48 para obtener el valor numerico y lo multiplicamos
        // por 10 elevado a la longitud del numero - 1 - la iteracion.
        // Si se detecta un caracter que no sea numerico, el numero queda en -1 y
        // finaliza el bucle.

        while (valido && i < texto.length()) {
            if ((texto.charAt(i) >= '0') && texto.charAt(i) <= '9') {
                num += ((texto.charAt(i) - 48) * Math.pow(10, (texto.length() - 1 - i)));
            } else {
                valido = false;
                num = -1;
            }
            i++;
        }

        return num;

    }

    public static double promedioPasajeros(int i, int j) {

        int promedio = 0;

        if (i < horarios.length) {
            if (j < horarios[0].length) {
                if (horarios[i][j] != null && horarios[i][j].getVolo()) {
                    promedio += promedioPasajeros(i, j + 1) + horarios[i][j].getAvion().getCantAsientos();
                    if (i == 0 && j == 0) {
                        promedio /= totalPasajeros(0, 0);
                    }
                } else {
                    promedio += promedioPasajeros(i, j + 1);
                }
            } else {
                promedio += promedioPasajeros(i + 1, 0);
            }
        }

        return promedio;

    }

    public static int totalPasajeros(int i, int j) {

        int cantidadVuelos = 0;

        if (i < horarios.length) {
            if (j < horarios[0].length) {
                if (horarios[i][j] != null && horarios[i][j].getVolo()) {
                    cantidadVuelos += totalPasajeros(i, j + 1) + 1;
                } else {
                    cantidadVuelos += totalPasajeros(i, j + 1);
                }
            } else {
                cantidadVuelos += totalPasajeros(i + 1, 0);
            }
        }

        return cantidadVuelos;

    }

    // Busqueda de Avion, Ruta y Vuelo
    public static Avion idAAvion(String idAvion) {

        Avion avion = null;
        int i = 0;

        while (avion == null && i < listaAviones.length) {
            if (listaAviones[i] != null && listaAviones[i].getId().equals(idAvion)) {
                avion = listaAviones[i];
            }
            i++;
        }

        return avion;

    }

    public static Ruta idARuta(String idRuta) {

        Ruta ruta = null;
        int i = 0;

        while (ruta == null && i < listaRutas.length) {
            if (listaRutas[i] != null && listaRutas[i].getId().equals(idRuta)) {
                ruta = listaRutas[i];
            }
            i++;
        }

        return ruta;

    }

    public static Vuelo idAVuelo(String idVuelo) {

        Vuelo vuelo = null;
        int i = 0;
        int j = 0;
        while (vuelo == null && i < horarios.length) {
            while (vuelo == null && j < horarios[0].length) {
                if (horarios[i][j] != null && horarios[i][j].getId().equals(idVuelo)) {
                    vuelo = horarios[i][j];
                }
                j++;
            }
            i++;
        }

        return vuelo;

    }

    public static void agregarVuelo(String idVuelo, String IdAvion, String IdRuta, String dia, String hora,
            String pasajeros) {
        boolean valido = true;
        int i = 0;
        Avion avion = idAAvion(IdAvion);
        Ruta ruta = idARuta(IdRuta);

        if (avion == null) {
            System.out.println("Identificacion invalida. No existe este avion en la base de datos.");
            valido = false;
        }

        if (ruta == null) {
            System.out.println("Identificacion invalida. No existe esta ruta en la base de datos.");
            valido = false;
        }
        if (!verificarRutaVuelo(idVuelo) || idARuta(idVuelo) == null) {
            System.out.println("Identificacion invalida. No se puede agragar este Vuelo a la base de datos");
            valido = false;
        }

        if (stringAEntero(hora) < 8 || stringAEntero(hora) > 22) {
            System.out.println("Hora invalida. Ingrese una hora entre 8 y 22.");
            valido = false;
        }

        if (traductorDias(dia.toLowerCase()) != -1) {
            System.out.println(
                    "Dia invalido. Ingrese un dia de la semana, por ejemplo: lunes, martes o miercoles.");
            valido = false;
        }
        if (stringAEntero(pasajeros) > avion.getCantAsientos()) {
            System.out.println(
                    "La cantidad de pasajeros ingresados sobrepasa la capacidad del avion. Ingrese una cantidad igual o mayor a: "
                            + avion.getCantAsientos());
            valido = false;
        }

        while (valido && i < horarios[0].length) {
            if (horarios[stringAEntero(dia) - 1][i].getAvion().getId().equals(avion.getId())) {
                valido = false;
            }
            i++;
        }

        if (valido) {
            String diaVerdadero = dia.toUpperCase().charAt(0) + dia.substring(1, dia.length()).toLowerCase();
            horarios[stringAEntero(dia) - 1][traductorHoras(hora)] = new Vuelo(idVuelo, avion, ruta, diaVerdadero,
                    hora + ":00");
            guardarArchivos("vuelos");
        }

    }

    public static boolean verificarRutaVuelo(String ID) {
        return (ID.length() > 2 && ID.charAt(0) == 'A' && ID.charAt(1) == 'R' && stringAEntero(ID) != -1);
    }

}
/*
 * Agregar un nuevo vuelo al cronograma de vuelos de la semana, solicitando el
 * número de vuelo, el avión, la hora y el día que debe ocurrir, la ruta que
 * debe recorrer y la cantidad de pasajeros. Se deberán hacer los controles
 * correspondientes de acuerdo a las restricciones y emitir mensajes al usuario
 * en caso de que no se pueda cumplir con lo solicitado.
 */