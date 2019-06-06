package Metaheuristicas;
import DiccionarioDatos.*;
import java.util.ArrayList;


public class RecocidoSimulado {
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
    
    public RecocidoSimulado(ArrayList<Coordenada> problema,BruteForce br){
        this.problema=problema;
        this.br=br;
    }
       
    public SolucionTSP start(int ciclos, double temperatura){
        SolucionTSP solucion = new SolucionTSP();
        solucion.setListaOriginal(problema);
        solucion.setMejorEspecie(problema);
        solucion.setBruteForce(br);
        solucion.setDescripcion("Recocido Simulado");
        ArrayList<Coordenada> mejor=Coordenada.clonar(problema) ;
        double temperaturamax=temperatura;
        ArrayList <Especie> nuevaespecie = new ArrayList <Especie>();
        for(int l=0;l<ciclos;l++){
            Especie semilla= new Especie(br,totalaleatorio,mejor.size(),maximo,minimo);
            ArrayList<Coordenada> lstpuntos = semilla.reOrdenamiento(Coordenada.clonar(mejor));
            if(semilla.getResultado()<br.distanciaTotal(mejor)){
                mejor = lstpuntos;
                nuevaespecie.add(semilla);
                solucion.setMejorEspecie(mejor);
            }else{
                if((double)Math.random()*1 < Math.exp((br.distanciaTotal(mejor)-semilla.getResultado())/temperatura)){
                    mejor = lstpuntos;
                    nuevaespecie.add(semilla);
                    solucion.setMejorEspecie(mejor);
                }
            }
            temperatura =temperaturamax-((l*temperaturamax)/ciclos);
        }    
        solucion.setEspeciesElegidas(nuevaespecie);
        return solucion;      
    }
       
}
