package Compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author carlos
 */
public class AnalizadorLexico {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        /**
         * ArrayList con contenido para buscar la categoria
         */

        List<String> letras = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
        List<String> palabrasReservadas = Arrays.asList("hola","prog", "var", "proc", "inicio", "fin", "entero", "real", "string", "limpiar", "vexy", "leer", "escribir", "repite", "hasta", "mientras", "si", "sino", "ejecuta", "and", "or");
        List<String> operadoresAritmeticos = Arrays.asList( "-", "*", "/");
        List<String> operadoresRelacionales = Arrays.asList("<", "<=", "<>", ">", ">=", "=");
        List<String> operadoresLogicos = Arrays.asList("&&", "||", "!");
        List<String> caracteresEspeciales = Arrays.asList(";", "[", "]", ",", ":", "(", ")", ":=");
        List<String> numeros = Arrays.asList("0", "1", "2", "3", "4", "5", "5", "7", "8", "9");

        /**
         * ArrayList sin contenido para ir guardando los tokens segun vayan apareciendo y asi tener su numero
         * Cada variable tiene el prefijo "t" para diferenciarla de su contraparte populada
         */
        
        ArrayList<String> tPalabrasReservadas = new ArrayList<>();
        ArrayList<String> tOperadoresAritmeticos = new ArrayList<>();
        ArrayList<String> tOperadoresRelacionales = new ArrayList<>();
        ArrayList<String> tOperadoresLogicos = new ArrayList<>();
        ArrayList<String> tCaracteresEspeciales = new ArrayList<>();
        //ArrayList<String> tConstantesEnteras = new ArrayList<>();
        //ArrayList<String> tConstantesReales = new ArrayList<>();
        ArrayList<String> tStrings = new ArrayList<>();
        ArrayList<String> tIdentificadores = new ArrayList<>();
        
        //abrir archivo
        GFileChooser archivo = new GFileChooser();
        String ruta = archivo.askForFile();
        File file = new File(ruta);
        FileReader fR = new FileReader(file);
        BufferedReader bR = new BufferedReader(fR);
        
        String linea = "";
        String minusculas = "";
        int numCons = 0; //Aqui se guarda un numero que solo aumenta para saber cuantos tokens hemos guardado
        int numConsErrores = 0; //Para contar los errores, solo por estetica
        int numLinea = 0; //El numero de la linea que se encuentra se guarda aqui
        String comilla = "\"";
        String backslash = "\\\\";
        
        int palRes, opArit, opRel, opLog, carEsp, ident, constEnt, constReal, constStr; //Numero de ID para cada categoria
        palRes = 100 ; //El ID de las Palabras Reservadas es 100
        opArit = 200; //El ID de los Operadores Aritmeticas es 200
        opRel = 300; // El ID de los Operadores Relacionales es 300
        opLog = 350; //El ID de los Operadores Logicos es 350 
        carEsp = 400; //El ID de los Caracteres Especiales que si generan token es 400
        ident = 500; //El ID de los identificadores es 500
        constEnt = 600; //El ID de las Constantes Enteras es 600
        constReal = 700; //El ID de las Constantes Reales es 700
        constStr = 800; //El ID de las Constantes String es 800
        
        boolean contLet =false;
        
        String tablaDeTokens = "TABLA DE TOKENS \n# | CADENA | No. Token/Ident. | No. Linea\n"; //Guarda toda la informacion de los tokens para al final imprimirla o guardarla en un documento
        //String tablaDeErrores = "TABLA DE ERRORES\n# | CADENA | No. Linea\n";

        while (bR.ready()) {
            int numeroPuntos = 0; //Variable para contar el numero de puntos para saber si es entero, real o invalido el numero

            numLinea++; //Aumenta el valor del numero de la linea para saber en que linea se encuentra el token
            linea = bR.readLine();
            minusculas = linea.toLowerCase();
            String palabras[] = minusculas.split(" ");
        

            for (int i = 0; i < palabras.length; i++) {
                try { //Try para evitar errores cuando hay mas de 1 espacio en la cadena 
                    if (letras.contains("" + palabras[i].charAt(0))) {//If para checar si el primer caracter de cada "palabra" contiene una letra
                        
                        if (palabrasReservadas.contains(palabras[i])) {//Se checa si la palabra esta dentro de las palabras reservadas
                            
                            if(tPalabrasReservadas.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tPalabrasReservadas.indexOf(palabras[i])+101)+" | "+numLinea+"\n";
                            }else{
                                palRes++;
                                tPalabrasReservadas.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(palRes)+" | "+numLinea+"\n";
                            }

                        } else if (palabras[i].length() <= 7) {//Se checa si es un identificador
                            if(tIdentificadores.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tIdentificadores.indexOf(palabras[i])+501)+" | "+numLinea+"\n";
                            }
                            else{
                                ident++;
                                tIdentificadores.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(ident)+" | "+numLinea+"\n";
                            }
                            //System.out.println("Es un identificador " + numLinea+ " " + palabras[i]);
                        } else {//No es una palabra reservada y no cumple con las especificaciones para ser identificador
                            tablaDeTokens += ++numCons+" | "+palabras[i]+" | "+"ERROR"+" | "+numLinea+"\n";
                        }

                    } else { //En caso de que no contenga letra inicial 
                        if (operadoresAritmeticos.contains(palabras[i])) {
                            if(tOperadoresAritmeticos.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tOperadoresAritmeticos.indexOf(palabras[i])+201)+" | "+numLinea+"\n";
                            }
                            else{
                                opArit++;
                                tOperadoresAritmeticos.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(opArit)+" | "+numLinea+"\n";
                            }
                            //System.out.println("Es un operador aritmetico " + numLinea+ " " + palabras[i]);
                        } else if (operadoresRelacionales.contains(palabras[i])) {
                            if(tOperadoresRelacionales.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tOperadoresRelacionales.indexOf(palabras[i])+301)+" | "+numLinea+"\n";
                            }
                            else{
                                opRel++;
                                tOperadoresRelacionales.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(opRel)+" | "+numLinea+"\n";
                            }
                            
                            //System.out.println("Es un operador relacional " + numLinea+ " " + palabras[i]);
                        } else if (operadoresLogicos.contains(palabras[i])) {
                            if(tOperadoresLogicos.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tOperadoresLogicos.indexOf(palabras[i])+351)+" | "+numLinea+"\n";
                            }
                            else{
                                opLog++;
                                tOperadoresLogicos.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(opLog)+" | "+numLinea+"\n";
                            }
                            
                            //System.out.println("Es un operador logico " + numLinea+ " " + palabras[i]);
                        } else if (caracteresEspeciales.contains(palabras[i])) {
                            if(tCaracteresEspeciales.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tCaracteresEspeciales.indexOf(palabras[i])+401)+" | "+numLinea+"\n";
                            }
                            else{
                                carEsp++;
                                tCaracteresEspeciales.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(carEsp)+" | "+numLinea+"\n";
                            }
                            
                            //System.out.println("Es un caracter especial " + numLinea+ " " + palabras[i]);
                        } else if (palabras[i].startsWith(comilla) && palabras[i].endsWith(comilla)) {
                            if(tStrings.contains(palabras[i])){
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(tStrings.indexOf(palabras[i])+901)+" | "+numLinea+"\n";
                            }
                            else{
                                constStr++;
                                tStrings.add(palabras[i]);
                                tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(constStr)+" | "+numLinea+"\n";
                            }
                            
                            //System.out.println("Es una constante String " + numLinea+ " " + palabras[i]);
                        } else if (palabras[i].startsWith(backslash)) {//Comentarios
                            tablaDeTokens += ++numCons +" | "+palabras[i]+"..."+" | "+(900)+" | "+numLinea+"\n";
                            
                            //System.out.println("Es un comentario " + numLinea+ " " + palabras[i]);
                            break;
                        } else if (numeros.contains(palabras[i].charAt(0) + "")) {
                            contLet = false;
                            if (!palabras[i].endsWith(".")) {
                                numeroPuntos = 0;
                                for (int j = 0; j < palabras[i].length(); j++) {
                                    if (palabras[i].charAt(j) == '.') {
                                        numeroPuntos++;
                                    }
                                    /*
                                    if(letras.contains(palabras[i].charAt(j)+"")){
                                        tablaDeTokens += ++numCons+" | "+palabras[i]+" | "+"ERROR"+" | "+numLinea+"\n";
                                        contLet=true;
                                        break;
                                    }
                                    */
                                    if(palabras[i].charAt(j)=='0'|| palabras[i].charAt(j)=='1'||palabras[i].charAt(j)=='2'|| palabras[i].charAt(j)=='3'|| palabras[i].charAt(j)=='4'|| palabras[i].charAt(j)=='5'|| palabras[i].charAt(j)=='6'|| palabras[i].charAt(j)=='7'|| palabras[i].charAt(j)=='8'|| palabras[i].charAt(j)=='9' || palabras[i].charAt(j)=='.'){
                                        contLet = false;
                                        
                                    }else{
                                        tablaDeTokens += ++numCons+" | "+palabras[i]+" | "+"ERROR"+" | "+numLinea+"\n";
                                        contLet=true;
                                        break;
                                    }
                                }
                                if(!contLet){
                                switch (numeroPuntos) {
                                    case 0:
                                        
                                        tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(600)+" | "+numLinea+"\n";
                                        
                                        break;
                                    case 1:
                                        tablaDeTokens += ++numCons +" | "+palabras[i]+" | "+(700)+" | "+numLinea+"\n";
                                        break;
                                    default:
                                        tablaDeTokens += ++numCons+" | "+palabras[i]+" | "+"ERROR"+" | "+numLinea+"\n";
                                        break;
                                }
                            }
                            }
                            else{
                                tablaDeTokens += ++numCons+" | "+palabras[i]+" | "+"ERROR"+" | "+numLinea+"\n";
                            }

                        } else {
                            tablaDeTokens += ++numCons+" | "+palabras[i]+" | "+"ERROR"+" | "+numLinea+"\n";
                        }

                    }
                } catch (StringIndexOutOfBoundsException e) {

                }
            }

        }
        bR.close();

        System.out.print(tablaDeTokens);
        //System.out.println("\n"+tablaDeErrores);
        
        //Guardar a archivo
        String fileToSave = archivo.saveToFile();
	File fileSave = new File(fileToSave);
	FileOutputStream fOS = new FileOutputStream(fileSave);
        PrintWriter pW = new PrintWriter(fOS);
        
        pW.print(tablaDeTokens);
        //pW.println(tablaDeErrores);
        
        pW.close();
    }
    /**
     * Este metodo sirve para cuando se tienen varios numeros unidos por un operador
     * para separarlos y regresar
     * @param regex 
     * @return una cadena con los valores 
     */
    public static String analisisAnidado(String regex){
        return null;
    }
}
