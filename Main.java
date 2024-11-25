
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
    static Vuelo[] vuelosOrdenados = null;

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

            System.out.println("=== MENU PRINCIPAL ===");
            System.out.println("1. Agregar un nuevo avion");
            System.out.println("2. Agregar un nuevo vuelo");
            System.out.println("3. Realizar un vuelo");
            System.out.println("4. Calcular promedio de pasajeros que volaron");
            System.out.println("5. Para un dia dado, ordenar los vuelos en base a sus KM de forma ascendente");
            System.out.println("6. Mostrar datos de un avion");
            System.out.println("7. Arreglo con vuelos en base a dos distancias");
            System.out.println("8. Calcular cantidad de horarios sin vuelos en la semana");
            System.out.println("9. Mostrar vuelos internacionales del primer horario de cada dia de la semana");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcionTexto = scanner.nextLine();

            switch (opcionTexto) {
                case "1":
                    agregarAvion();
                    break;
                case "2":
                    agregarVuelo();
                    break;
                case "3":
                    realizarVuelo();
                    break;
                case "4":
                    saberPromedioPasajeros();
                    break;
                case "5":
                    metodosOrdenamientoListaVuelos();
                    break;
                case "6":
                    mostrarInformacionAvion();
                    break;
                case "7":
                    vuelosEnRango();
                    break;
                case "8":
                    saberHorariosSinVuelos();
                    break;
                case "9":
                    mostrarPrimerHorarioVueloInt();
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

    // METODOS DEL MENU

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

    public static void agregarVuelo() {
        boolean valido = true;
        int i = 0;
        String idVuelo, IdAvion, IdRuta, dia, hora, pasajeros;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el ID del vuelo");
        idVuelo = scanner.nextLine();

        System.out.println("Ingrese el ID del avion");
        IdAvion = scanner.nextLine();
        Avion avion = idAAvion(IdAvion);

        System.out.println("Ingrese el ID de la ruta");
        IdRuta = scanner.nextLine();
        Ruta ruta = idARuta(IdRuta);

        System.out.println("Ingrese el dia del vuelo");
        dia = scanner.nextLine();

        System.out.println("Ingrese la hora del vuelo");
        hora = scanner.nextLine();

        System.out.println("Ingrese los pasajeros del vuelo");
        pasajeros = scanner.nextLine();

        if (!verificarRutaVuelo(idVuelo) || idAVuelo(idVuelo) != null) {
            System.out.println(
                    "Identificacion invalida o ya existe el vuelo. No se puede agregar este Vuelo a la base de datos");
            valido = false;
        }

        if (avion == null) {
            System.out.println("Identificacion invalida. No existe este avion en la base de datos.");
            valido = false;
        }

        if (ruta == null) {
            System.out.println("Identificacion invalida. No existe esta ruta en la base de datos.");
            valido = false;
        }

        if (traductorDias(dia.toLowerCase()) == -1) {
            System.out.println(
                    "Dia invalido. Ingrese un dia de la semana, por ejemplo: lunes, martes o miercoles.");
            valido = false;
        }

        if ((stringAEntero(hora) < 8 || stringAEntero(hora) > 22)) {
            System.out.println("Hora invalida. Ingrese una hora entre 8 y 22.");
            valido = false;
        }

        if (valido && stringAEntero(pasajeros) > avion.getCantAsientos()) {
            System.out.println(
                    "La cantidad de pasajeros ingresados sobrepasa la capacidad del avion. Ingrese una cantidad igual o mayor a: "
                            + avion.getCantAsientos());
            valido = false;
        }
        // Revisa que en el dia del vuelo no haya un vuelo con el mismo avion.
        while (valido && i < horarios[0].length) {
            Vuelo elegido = horarios[traductorDias(dia.toLowerCase())][i];
            if (elegido != null && elegido.getAvion() != null && elegido.getAvion().getId().equals(avion.getId())) {
                valido = false;
            }
            i++;
        }
        if (valido && horarios[traductorDias(dia.toLowerCase())][traductorHoras(hora)] != null) {
            System.out.println("Ya existe un vuelo en este horario");
            valido = false;
        }

        // Agrega el vuelo a la matriz
        if (valido) {
            String diaVerdadero = dia.toUpperCase().charAt(0) + dia.substring(1, dia.length()).toLowerCase();
            horarios[traductorDias(dia.toLowerCase())][traductorHoras(hora)] = new Vuelo(idVuelo, avion, ruta,
                    diaVerdadero,
                    hora + ":00");
            guardarArchivos("vuelos");
            System.out.println("Vuelo agregado a la base de datos.");
        }

    }

    public static void realizarVuelo() {
        String idVuelo;
        System.out.println("Ingrese el ID del vuelo para marcarlo");
        Scanner scanner = new Scanner(System.in);
        idVuelo = scanner.nextLine();
        Vuelo vuelo = idAVuelo(idVuelo);
        if (vuelo != null) {
            vuelo.realizarVuelo();
            guardarArchivos("aviones");
            guardarArchivos("vuelos");
        } else {
            System.out.println("Identificacion invalida. No encontrado.");
        }
    }

    public static void saberPromedioPasajeros() {

        double cantidad = promedioPasajeros(0, 0);

        System.out.println("El promedio de pasajeros que volaron es de:" + cantidad);

    }

    public static void metodosOrdenamientoListaVuelos() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Realizar ordenamiento ascendente por metodo BURBUJA");
        System.out.println("2. Realizar ordenamiento ascendente por metodo HEAPSORT");

        String opcion = scanner.nextLine();

        System.out.println(
                "Elija un dia para ordenar los vuelos (lunes,martes,miercoles,jueves,viernes,sabado,domingo)");

        opcion = scanner.nextLine();
        int dia = traductorDias(opcion.toLowerCase());

        if (dia != -1) {

            int cuenta = 0;
            int j;
            Vuelo aux;
            Vuelo[] array;
            int i = dia;

            for (j = 0; j < horarios[0].length; j++) {
                if (horarios[i][j] != null && horarios[i][j].getAvion() != null) {
                    cuenta++;
                }
            }

            array = new Vuelo[cuenta];
            cuenta = 0;

            for (j = 0; j < horarios[0].length; j++) {
                if (horarios[i][j] != null && horarios[i][j].getAvion() != null) {
                    array[cuenta] = horarios[i][j];
                    cuenta++;
                }
            }

            switch (opcion) {
                case "1":
                    metodoBurbujaAscendente(array);
                    break;
                case "2":
                    metodoHeapsortAscendente(array);
                    break;
                default:
                    System.out.println("ERROR, opcion mal ingresada");
                    break;
            }

        } else {
            System.out.println("Error, dia invalido");
        }

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

        }

    }

    public static void vuelosEnRango() {

        Scanner scanner = new Scanner(System.in);

        int primerRango;
        int segundoRango;
        int dim = 0;

        System.out.println("1. Mostrar vuelos en rango");
        System.out.println("2. Ingresar rangos");

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

    public static void saberHorariosSinVuelos() {

        int cantidad = calcularHorariosSinVuelos(0, 0);

        System.out.println("Hay " + cantidad + " de horarios que no tienen vuelos");

    }

    public static void mostrarPrimerHorarioVueloInt() {

        int j;

        for (int i = 0; i < horarios.length; i++) {

            boolean encontrado = false;
            j = 0;
            String horario = "";

            while (!encontrado && j < horarios[0].length) {

                if (horarios[i][j] != null && horarios[i][j].getRuta() != null) {
                    if (horarios[i][j].getRuta().getEsInternacional()) {
                        encontrado = true;
                        horario = (j + 8) + ":00";
                        if (horario.split(":")[0].length() == 1) {
                            horario = "0" + horario;
                        }
                    }
                }

                j++;
            }

            String dia = traductorNumADia(i).toUpperCase();

            if (encontrado && !dia.equals("")) {
                System.out.println("EL HORARIO PARA EL DIA " + dia + " ES " + horario);
            } else {
                System.out.println("No se encontraron horarios para el dia " + dia);
            }

        }

    }

    // METODOS DE ORDENAMIENTO

    public static void metodoBurbujaAscendente(Vuelo[] array) {

        boolean ordenado = false;
        int j;
        Vuelo aux;
        int n;

        n = array.length;
        int i = 0;

        while (i < n - 1 && !ordenado) {
            ordenado = true;
            for (j = 0; j <= n - 2 - i; j++) {
                if (array[j + 1].getAvion().getKmRecorridos() < array[j].getAvion().getKmRecorridos()) {
                    ordenado = false;
                    aux = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = aux;
                }
            }
            i++;
        }

        vuelosOrdenados = array;

        guardarArchivos("vuelosOrdenados");

    }

    public static void metodoHeapsort(Vuelo arr[], int n, int i) {

        // Initialize largest as root
        int largest = i; 

        // left index = 2*i + 1
        int l = 2 * i + 1; 

        // right index = 2*i + 2
        int r = 2 * i + 2;

        // If left child is larger than root
        if (l < n && arr[l].getAvion().getKmRecorridos() > arr[largest].getAvion().getKmRecorridos() ) {
            largest = l;
        }

        // If right child is larger than largest so far
        if (r < n && arr[r].getAvion().getKmRecorridos() > arr[largest].getAvion().getKmRecorridos()) {
            largest = r;
        }

        // If largest is not root
        if (largest != i) {
            Vuelo temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            // Recursively heapify the affected sub-tree
            metodoHeapsort(arr, n, largest);
        }
    }

    public static void metodoHeapsortAscendente(Vuelo[] array) {

        int n = array.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            metodoHeapsort(array, n, i);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {

            // Move current root to end
            Vuelo temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Call max heapify on the reduced heap
            metodoHeapsort(array, i, 0);
        }

    }

    // TRADUCTORES

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

    public static String traductorNumADia(int num) {

        String elegido;

        switch (num) {
            case 0:
                elegido = "lunes";
                break;
            case 1:
                elegido = "martes";
                break;
            case 2:
                elegido = "miercoles";
                break;
            case 3:
                elegido = "jueves";
                break;
            case 4:
                elegido = "viernes";
                break;
            case 5:
                elegido = "sabado";
                break;
            case 6:
                elegido = "domingo";
                break;
            default:
                elegido = "";
                break;
        }

        return elegido;

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

    // METODOS DE CARGA

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

                        if (split.length == 6) {
                            horarios[fila][columna] = new Vuelo(split[0], idAAvion(split[1]),
                                    idARuta(split[2]), split[3], split[4], (split[5].equals("true")));
                        } else {
                            horarios[fila][columna] = new Vuelo(split[0], idAAvion(split[1]),
                                    idARuta(split[2]), split[3], split[4]);
                        }

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

            while (bucle) {

                linea = bufferLectura.readLine();

                switch (camino) {
                    case "aviones":
                        if (fila < listaAviones.length && listaAviones[fila] != null) {
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
                        if (fila < horarios.length) {
                            Vuelo vuelo = horarios[fila][columna];
                            if (vuelo != null) {
                                System.out.println(fila + " " + columna);
                                System.out.println(vuelo.getDia() + " " + vuelo.getHora() + " " + vuelo.getAvion() + " "
                                        + vuelo.getRuta());
                                guardar += (vuelo.getId() + ";" + vuelo.getAvion().getId() + ";" +
                                        vuelo.getRuta().getId() + ";" + vuelo.getDia()
                                        + ";"
                                        + vuelo.getHora() + ";" + vuelo.getRealizado() + "\n");
                            }
                        } else {
                            bucle = false;
                        }
                        break;
                    case "vuelosOrdenados":
                        if (fila < vuelosOrdenados.length && vuelosOrdenados[fila] != null) {
                            Vuelo vuelo = vuelosOrdenados[fila];
                            guardar += (vuelo.getId() + ";" + vuelo.getAvion().getId() + ";" +
                                    vuelo.getRuta().getId() + ";" + vuelo.getDia()
                                    + ";"
                                    + vuelo.getHora() + ";" + vuelo.getAvion().getKmRecorridos() + "\n");
                        } else {
                            bucle = false;
                        }
                        break;
                    default:
                        break;
                }

                if (camino.equals("vuelos")) {
                    if (columna >= 14) {
                        fila++;
                        columna = 0;
                    } else {
                        columna++;
                    }
                } else {
                    fila++;
                }

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

    // METODOS RECURSIVOS

    public static double promedioPasajeros(int i, int j) {

        int promedio = 0;

        if (i < horarios.length) {
            if (j < horarios[0].length) {
                if (horarios[i][j] != null && horarios[i][j].getRealizado()) {
                    promedio += promedioPasajeros(i, j + 1) + horarios[i][j].getAvion().getCantAsientos();
                } else {
                    promedio += promedioPasajeros(i, j + 1);
                }
            } else {
                promedio += promedioPasajeros(i + 1, 0);
            }
        }

        if (i == 0 && j == 0) {
            int pasajeros = totalPasajeros(0, 0);
            if (pasajeros == 0) {
                pasajeros = 1;
            }
            promedio /= pasajeros;
        }

        return promedio;

    }

    public static int totalPasajeros(int i, int j) {

        int cantidadVuelos = 0;

        if (i < horarios.length) {
            if (j < horarios[0].length) {
                if (horarios[i][j] != null && horarios[i][j].getRealizado()) {
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

    public static int calcularHorariosSinVuelos(int i, int j) {

        int cant = 0;

        if (i < horarios.length) {
            if (j < horarios[0].length) {
                if (horarios[i][j] == null) {
                    cant += calcularHorariosSinVuelos(i, j + 1) + 1;
                } else {
                    cant += calcularHorariosSinVuelos(i, j + 1);
                }
            } else {
                cant += calcularHorariosSinVuelos(i + 1, 0);
            }
        }

        return cant;

    }

    // Busqueda de Avion, Ruta y Vuelo

    public static Avion idAAvion(String idAvion) {

        Avion avion = null;
        int i = 0;

        if (esIdentificacionValida(idAvion)) {
            while (avion == null && i < listaAviones.length) {
                if (listaAviones[i] != null && listaAviones[i].getId().equals(idAvion)) {
                    avion = listaAviones[i];
                }
                i++;
            }
        }

        return avion;

    }

    public static Ruta idARuta(String idRuta) {

        Ruta ruta = null;
        int i = 0;
        if (verificarRutaVuelo(idRuta)) {
            while (ruta == null && i < listaRutas.length) {
                if (listaRutas[i] != null && listaRutas[i].getId().equals(idRuta)) {
                    ruta = listaRutas[i];
                }
                i++;
            }
        }

        return ruta;

    }

    public static Vuelo idAVuelo(String idVuelo) {

        Vuelo vuelo = null;
        int i = 0;
        int j = 0;
        if (verificarRutaVuelo(idVuelo)) {
            while (vuelo == null && i < horarios.length) {
                while (vuelo == null && j < horarios[0].length) {
                    if (horarios[i][j] != null && horarios[i][j].getId().equals(idVuelo)) {
                        vuelo = horarios[i][j];
                    }
                    j++;
                }
                i++;
            }
        }

        return vuelo;

    }

    // METODOS DE VERIFICACION

    public static boolean esIdentificacionValida(String identificacion) {

        boolean esValida = true;
        int i = 0;
        int max = 3;

        String[] split = identificacion.split("-");
        String parteA = split[0];

        if (split.length == 2) {
            String parteB = split[1];
            if ((parteA.equals("LV") && (parteB.length() <= 3)) || parteA.equals("LQ")) {
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

    public static boolean verificarRutaVuelo(String ID) {
        return (ID.length() > 2 && ID.charAt(0) == 'A' && ID.charAt(1) == 'R'
                && stringAEntero(ID.substring(2, ID.length() - 1)) != -1);
    }

    // METODOS DE RETORNO DE ARRAY

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

}
