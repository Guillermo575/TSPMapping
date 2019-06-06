package DiccionarioDatos;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Especie {
 /*Esta clase contiene una cantidad de numeros que altera la forma de la solucion*/
         ArrayList <Integer> genes = new ArrayList <Integer>();
         ArrayList <Integer> variantes = new ArrayList <Integer>();
         ArrayList <Integer> recesivos = new ArrayList <Integer>();
         
         BruteForce br=new BruteForce();
         boolean removeUnable =true;

         double resultado=0;
         
         public Especie(){}
         
         public void setBR(BruteForce br){this.br=br;}
         public BruteForce getB(){return br;};
         
         public void setResultado(double resultado){this.resultado=resultado;}
         public double getResultado(){return resultado;};
         
         public void setRemoveUnable(boolean removeUnable) {this.removeUnable = removeUnable;}
         public boolean isRemoveUnable() {return removeUnable;}
         
         public ArrayList<Integer> getGenes() { return genes;}
         public void setGenes(ArrayList<Integer> genes) {this.genes = genes;}   
        
         public ArrayList<Integer> getVariantes() {return variantes;}
         public void setVariantes(ArrayList<Integer> variantes) {this.variantes = variantes;}
         
         public ArrayList<Integer> getRecesivos() { return recesivos;}
         public void setRecesivos(ArrayList<Integer> recesivos) {this.recesivos = recesivos;}  
         
         public Especie(BruteForce br,int totalaleatorio,int maximo,int maximovariante,int minimovariante){
             this.br=br;
             for(int l=0;l<totalaleatorio;l++){
                 genes.add((int)((Math.random()*(maximo-maximovariante-1))+1));
                 variantes.add((int)((Math.random()*(maximovariante-minimovariante))+minimovariante));
             }
         }
        public Especie(BruteForce br,int totalaleatorio){
             this.br=br;
             for(int l=0;l<totalaleatorio;l++){
                 genes.add(0);
                 variantes.add(0);
             }
        }
        public static Especie clonarEspecie(Especie especie){
            Especie nuevo = new Especie ();
            nuevo.br=especie.br;
             for(int l=0;l<especie.getVariantes().size();l++){
                 nuevo.getGenes().add(especie.getGenes().get(l));
                 nuevo.getVariantes().add(especie.getVariantes().get(l));
             }
             return nuevo;
         }
         
        public void cambiarGen(int gen,int maximo,int maximovariante,int minimovariante){
            genes.set(gen,(int)((Math.random()*(maximo-maximovariante-1))+1));
            variantes.set(gen,(int)((Math.random()*(maximovariante-minimovariante))+minimovariante));
         }
         
         public void addGen(int gen, int variante){
             genes.add(gen);
             variantes.add(variante);
         }
         /*El siguiente metodo siguiente permite alterar una solucion mezclandolo con una especie,
           los genes representa la posicion de un arreglo y variante es un valor que estara relacionado con el cambio en
           cuestion, en este caso se cambiara de lugares*/
          public ArrayList<Coordenada> reOrdenamiento(ArrayList<Coordenada> lstpuntos){
           recesivos =  new ArrayList <Integer>();
           for(int c=0;c<this.genes.size();){
              int semilla=this.genes.get(c);
                /*Si es favorecedor se cambiaran de lugares*/
                if(br.getDistance(lstpuntos.get(semilla),lstpuntos.get(semilla+this.variantes.get(c))) < 
                   br.getDistance(lstpuntos.get(semilla),lstpuntos.get(semilla+1)))
                {
                   Coordenada cambio = new Coordenada(lstpuntos.get(semilla+1).x,lstpuntos.get(semilla+1).y);
                   Coordenada cambio2 = new Coordenada(lstpuntos.get(semilla+this.variantes.get(c)).x,lstpuntos.get(semilla+this.variantes.get(c)).y);
                   lstpuntos.get(semilla+1).setCoords(cambio2.x, cambio2.y);
                   lstpuntos.get(semilla+this.variantes.get(c)).setCoords(cambio.x, cambio.y);
                   c++;
                }else{
                    if(removeUnable){
                        genes.remove(c);
                        variantes.remove(c);                    
                    }else{
                        recesivos.add(c);
                        c++;
                    }
                }
           }
               setResultado(br.distanciaTotal(lstpuntos));
               return lstpuntos;
          }    
}
