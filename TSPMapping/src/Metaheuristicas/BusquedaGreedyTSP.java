package Metaheuristicas;
import DiccionarioDatos.*;
import java.util.ArrayList;


public class BusquedaGreedyTSP {
    ArrayList<Coordenada> problema;
    BruteForce br=new BruteForce();
    int totalaleatorio=50;
    int maximo=5;
    int minimo=2;
    
    public int getMinimo(){return minimo;}
    public int getMaximo(){return maximo;}
    public int getTotalAleatorio(){return totalaleatorio;}
    
    public void setMinimo(int minimo){this.minimo=minimo;}
    public void setMaximo(int maximo){this.maximo=maximo;}
    public void setTotalAleatorio(int totalaleatorio){this.totalaleatorio=totalaleatorio;}
    
    public BusquedaGreedyTSP(ArrayList<Coordenada> problema,BruteForce br) {
        this.problema = problema;
        this.br=br;
    }

    public SolucionTSP start(int iteraciones) {
        SolucionTSP solucion = new SolucionTSP();
        ArrayList<Coordenada> rutaActual = Coordenada.clonar(problema);
        solucion.setListaOriginal(problema);
        solucion.setBruteForce(br);
        solucion.setMejorEspecie(problema);
        solucion.setDescripcion("Busqueda Greedy");
        Especie nuevaespecie = new Especie();
        nuevaespecie.setBR(br);
        nuevaespecie.setResultado(br.distanciaTotal(rutaActual));
        solucion.getEspeciesElegidas().add(nuevaespecie);
        double costoActual = br.distanciaTotal(rutaActual);
        for (int i = 1; i <= iteraciones; i++) {
            Especie semilla= new Especie(br,totalaleatorio,rutaActual.size(),maximo,minimo);
            ArrayList<Coordenada> rutaTemp = Coordenada.clonar(rutaActual);
            semilla.reOrdenamiento(rutaTemp);
            double costoruta = semilla.getResultado();
            if (costoruta < costoActual) {
                costoActual = costoruta;
                rutaActual = Coordenada.clonar(rutaTemp);
                solucion.setMejorEspecie(rutaActual);
                solucion.getEspeciesElegidas().add(semilla);
            }
        }
        return solucion;
    }
}