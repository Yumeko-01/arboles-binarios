/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unipiloto.estdatos.binarios.interfaz;

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class ReconstructorArbol implements IReconstructorArbol {
    private String[] preorden;
    private String[] inorden;
    private Nodo raiz;

    @Override
    public void cargarArchivo(String nombre) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("data/" + nombre));
        preorden = props.getProperty("preorden").split(",");
        inorden = props.getProperty("inorden").split(",");
    }

    @Override
    public void crearArchivo(String info) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("data/arbolPlantado.json", "UTF-8");
        writer.println(info);
        writer.close();
    }

    @Override
    public void reconstruir() {
        if (preorden != null && inorden != null) {
            raiz = construirArbol(preorden, inorden);
        }
    }

   private Nodo construirArbol(String[] preorden, String[] inorden) {
    return construirArbolRec(preorden, 0, preorden.length - 1, inorden, 0, inorden.length - 1);
}

private Nodo construirArbolRec(String[] preorden, int preInicio, int preFin, String[] inorden, int inInicio, int inFin) {
    if (preInicio > preFin || inInicio > inFin) {
        return null;
    }

    String raizValor = preorden[preInicio];
    Nodo raiz = new Nodo(raizValor);
    int raizIndex = findIndex(inorden, inInicio, inFin, raizValor);

    int izquierdaTamaño = raizIndex - inInicio;

    raiz.izquierdo = construirArbolRec(
        preorden, preInicio + 1, preInicio + izquierdaTamaño,
        inorden, inInicio, raizIndex - 1
    );

    raiz.derecho = construirArbolRec(
        preorden, preInicio + izquierdaTamaño + 1, preFin,
        inorden, raizIndex + 1, inFin
    );

    return raiz;
}

private int findIndex(String[] arr, int inicio, int fin, String value) {
    for (int i = inicio; i <= fin; i++) {
        if (arr[i].equals(value)) {
            return i;
        }
    }
    return -1;
}

    public String imprimirArbol() {
    if (raiz == null) {
        return "";
    }
    StringBuilder sb = new StringBuilder();
    imprimirArbol(raiz, 0, sb);
    return sb.toString();
}

private void imprimirArbol(Nodo nodo, int nivel, StringBuilder sb) {
    if (nodo == null) {
        return;
    }
    imprimirArbol(nodo.derecho, nivel + 1, sb);
    for (int i = 0; i < nivel; i++) {
        sb.append("   ");
    }
    sb.append(nodo.valor).append("\n");
    imprimirArbol(nodo.izquierdo, nivel + 1, sb);
}
}