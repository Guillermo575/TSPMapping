package Metaheuristicas;
import DiccionarioDatos.*;
import java.util.ArrayList;

public class BusquedaExhaustivaTSP 
{
    ArrayList<Coordenada> problema;
    BruteForce br=new BruteForce();
    
    public BusquedaExhaustivaTSP(ArrayList<Coordenada> problema, BruteForce br) 
    {
        this.problema = problema;
        this.br = br;
    }

    public SolucionTSP start()
    {
        //RUTA INICIAL DEL ALGORITMO
        SolucionTSP solucion = new SolucionTSP();
        solucion.setListaOriginal(problema);
        solucion.setBruteForce(br);
        solucion.setMejorEspecie(problema);
        solucion.setDescripcion("Busqueda Exhaustiva");
        ArrayList<Coordenada> rutaActual = Coordenada.clonar(problema);
        double costoActual = br.distanciaTotal(rutaActual);
        int iteraciones = problema.size() - 2;
        Especie nuevaespecie = new Especie();
        nuevaespecie.setBR(br);
        nuevaespecie.setResultado(costoActual);
        for (int i = 1; i <= iteraciones; i++) 
        {
            for (int j = i; j <= iteraciones; j++) 
            {
                ArrayList<Coordenada> rutaTemp = Coordenada.clonar(rutaActual);
                Coordenada cambio = rutaTemp.get(i);
                rutaTemp.set(i, rutaTemp.get(j));
                rutaTemp.set(j, cambio);
                double costoruta = br.distanciaTotal(rutaTemp);
                if (costoruta < costoActual) 
                {
                    costoActual = costoruta;
                    rutaActual = Coordenada.clonar(rutaTemp);
                    nuevaespecie.addGen(i, j-i);
                    nuevaespecie.setResultado(costoActual);
                    solucion.setMejorEspecie(rutaActual);
                }
            }
        }
        ArrayList <Especie> lista = new ArrayList<Especie>();
        lista.add(nuevaespecie);
        solucion.setEspeciesElegidas(lista);
        return solucion;
    }
}