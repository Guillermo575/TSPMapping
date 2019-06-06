package Metaheuristicas;
import DiccionarioDatos.*;
import java.util.ArrayList;


public class Genetico {
    ArrayList<Coordenada> problema;
    BruteForce br=new BruteForce();
    int totalaleatorio=50;
    int maximo=5;
    int minimo=2;
    double MutacionMinimo = .10;
    double MutacionMaximo = .40;
    int RangoElitismo = 1;
   
    public int getMinimo(){return minimo;}
    public int getMaximo(){return maximo;} 
    public double getMutacionMinimo() {return MutacionMinimo;}
    public double getMutacionMaximo() {return MutacionMaximo;}
    public int getTotalAleatorio(){return totalaleatorio;}
    public int getRangoElitismo(){return RangoElitismo;}
        
    public void setMinimo(int minimo){this.minimo=minimo;}
    public void setMaximo(int maximo){this.maximo=maximo;}
    public void setMutacionMinimo(double MutacionMinimo) {this.MutacionMinimo = MutacionMinimo;}
    public void setMutacionMaximo(double MutacionMaximo) {this.MutacionMaximo = MutacionMaximo;}
    public void setTotalAleatorio(int totalaleatorio){this.totalaleatorio=totalaleatorio;}
    public void setRangoElitismo(int RangoElitismo){this.RangoElitismo=RangoElitismo;}
    
    public Genetico(ArrayList<Coordenada> problema,BruteForce br){
        this.problema=problema;
        this.br=br;
    }
     
    public ArrayList <Especie> iniciarPoblacion(int poblacion){
        ArrayList <Especie> nuevaespecie = new ArrayList <Especie>();
        RangoElitismo = (RangoElitismo>poblacion) ? poblacion : RangoElitismo;
        RangoElitismo = (int)(RangoElitismo*0.01);
        for(int l=0;l<poblacion;l++){
            Especie semilla= new Especie(br,totalaleatorio,problema.size(),maximo,minimo);
            semilla.setRemoveUnable(false);
            semilla.reOrdenamiento(Coordenada.clonar(problema));
            nuevaespecie.add(semilla);
        }
        return nuevaespecie;
    }
    
    public SolucionTSP start(int poblacion,int generaciones){
        SolucionTSP solucion = new SolucionTSP();
        solucion.setListaOriginal(Coordenada.clonar(problema));
        solucion.setMejorEspecie(Coordenada.clonar(problema));
        solucion.setBruteForce(br);
        solucion.setDescripcion("Algoritmo Genetico");
        ArrayList<Coordenada> mejor = problema;
        Especie mejorEspecie = new Especie(br,totalaleatorio);
        mejorEspecie.reOrdenamiento(mejor);
        ArrayList <Especie> nuevaespecie = iniciarPoblacion(poblacion);
        for(int l=0;l<generaciones;l++){
            for (int i = 1; i <= nuevaespecie.size() - 1; i++) {
                int limite =i-1;
                Especie x = nuevaespecie.get(i);
                Especie j =  nuevaespecie.get(limite);
                while (limite >= 0 && x.getResultado() < j.getResultado() ){
                    nuevaespecie.set(limite+ 1,j);
                    limite = limite - 1;
                    if(limite>=0){
                        j =  nuevaespecie.get(limite);
                    }
                }
                nuevaespecie.set(limite+ 1,x);
            }
            if(nuevaespecie.size()%2 >0){
                nuevaespecie.remove(nuevaespecie.size()-1);
            }
            if(br.distanciaTotal(nuevaespecie.get(0).reOrdenamiento(Coordenada.clonar(problema)))<br.distanciaTotal(mejor)){
                mejor = nuevaespecie.get(0).reOrdenamiento(Coordenada.clonar(problema));
                mejorEspecie = nuevaespecie.get(0);               
            } 
            
            nuevaespecie.remove(nuevaespecie.size()-1);
            if(mejorEspecie.getResultado()>0){
                solucion.getEspeciesElegidas().add(mejorEspecie);
                nuevaespecie.add(0,mejorEspecie);
            }else{
                solucion.getEspeciesElegidas().add(nuevaespecie.get(0));
            }
           
            ArrayList <Especie> hijos = new ArrayList <Especie>();
            if(l<generaciones-1){
                for (int i = 0; i < nuevaespecie.size(); i=0){
                   Especie especieA = nuevaespecie.get(0);
                   Especie especieB = nuevaespecie.get((int)((Math.random()*(nuevaespecie.size()-1)*RangoElitismo)+1));
//                   especieB = nuevaespecie.get(1);
                   hijos.add(mutarEspecie(cruzaEspecieZigZag(especieA,especieB)));
                   hijos.add(mutarEspecie(cruzaEspecieZigZag(especieB,especieA)));
                   nuevaespecie.remove(especieA);
                   nuevaespecie.remove(especieB);
               }
               nuevaespecie = hijos;           
            }
        }
//        mejor = mejorEspecie.reOrdenamiento(Coordenada.clonar(problema));
        solucion.setMejorEspecie(mejor);    
        return solucion;       
    }
    public Especie cruzaEspecieZigZag(Especie padre,Especie madre){
      Especie hija = new Especie();
      Boolean zigzag = true;
      for(int l=0;l<padre.getGenes().size();l++){
          if(zigzag){
              hija.addGen(padre.getGenes().get(l), padre.getVariantes().get(l));
          }else{
              hija.addGen(madre.getGenes().get(l), madre.getVariantes().get(l));
          }
          zigzag = !zigzag;
      }
      hija.setRemoveUnable(false);
      hija.reOrdenamiento(Coordenada.clonar(problema));
      return hija;
    }
    public Especie cruzaEspecieMitad(Especie padre,Especie madre){
      Especie hija = new Especie();
      for(int l=0;l<padre.getGenes().size();l++){
          if(l<padre.getGenes().size()/2){
              hija.addGen(padre.getGenes().get(l), padre.getVariantes().get(l));
          }else{
              hija.addGen(madre.getGenes().get(l), madre.getVariantes().get(l));
          }
      }
      hija.setRemoveUnable(false);
      hija.reOrdenamiento(Coordenada.clonar(problema));
      return hija;
    }
    public Especie mutarEspecie(Especie hija){
        if(hija.getRecesivos().size()>0){
            int rangomin = (int)(hija.getRecesivos().size()*MutacionMinimo);
            int rangomax = (int)(hija.getRecesivos().size()*MutacionMaximo);
            int rangomutacion = (int)((Math.random()*rangomax)+rangomin);
            for(int l=0;l<rangomutacion;l++){
                int genseleccionado = hija.getRecesivos().get((int)((Math.random()*hija.getRecesivos().size())));
                hija.getGenes().set(genseleccionado, (int)((Math.random()*(problema.size()-getMaximo()-1))+1));
                hija.getVariantes().set(genseleccionado, (int)((Math.random()*(getMaximo()-getMinimo()))+getMinimo()));
            }
            hija.setRemoveUnable(false);
            hija.reOrdenamiento(Coordenada.clonar(problema));      
        }
        return hija;
    }
}

//    public SolucionTSP start(int poblacion,int generaciones){
//        SolucionTSP solucion = new SolucionTSP();
//        solucion.setListaOriginal(Coordenada.clonar(problema));
//        solucion.setMejorEspecie(Coordenada.clonar(problema));
//        solucion.setBruteForce(br);
//        solucion.setDescripcion("Algoritmo Genetico");
//        ArrayList<Coordenada> mejor = problema;
//        Especie mejorEspecie = new Especie(br,totalaleatorio);
//        mejorEspecie.reOrdenamiento(mejor);
//        ArrayList <Especie> nuevaespecie = iniciarPoblacion(poblacion);
//        ArrayList <Especie> especiesElite = new ArrayList <Especie>();
//        for(int l=0;l<generaciones;l++){
//            ArrayList <Especie> eliteGeneracion = new ArrayList <Especie>();
//            for (int i = 1; i <= nuevaespecie.size() - 1; i++) {
//                int limite =i-1;
//                Especie x = nuevaespecie.get(i);
//                Especie j =  nuevaespecie.get(limite);
//                while (limite >= 0 && x.getResultado() < j.getResultado() ){
//                    nuevaespecie.set(limite+ 1,j);
//                    limite = limite - 1;
//                    if(limite>=0){
//                        j =  nuevaespecie.get(limite);
//                    }
//                }
//                nuevaespecie.set(limite+ 1,x);
//            }
//            if(nuevaespecie.size()%2 >0){
//                nuevaespecie.remove(nuevaespecie.size()-1);
//            }
//                        
//            if(br.distanciaTotal(nuevaespecie.get(0).reOrdenamiento(Coordenada.clonar(problema)))<br.distanciaTotal(mejor)){
//                mejor = nuevaespecie.get(0).reOrdenamiento(Coordenada.clonar(problema));
//                mejorEspecie = nuevaespecie.get(0);    
//            } 
//
//            solucion.getEspeciesElegidas().add(mejorEspecie);
//            
////            for(int elite=0;elite<getRangoElitismo()&&elite<nuevaespecie.size();elite++){
////               eliteGeneracion.add(nuevaespecie.get(elite));         
////            }
////            if(especiesElite.size()>0){
////                for(int elite=0;elite<especiesElite.size();elite++){
////                    if(eliteGeneracion.get(0).getResultado()<especiesElite.get(elite).getResultado()){
////                        especiesElite.remove(elite);
////                        especiesElite.add(elite,eliteGeneracion.get(0));
////                        eliteGeneracion.remove(0);  
////                    }    
////                }        
////            }else{
////                especiesElite = eliteGeneracion;
////            }       
//            
////            for(int elite=0;elite<getRangoElitismo()&&elite<nuevaespecie.size();elite++){
////                nuevaespecie.remove(nuevaespecie.size()-(1+elite));
////                nuevaespecie.add(elite,especiesElite.get(elite));        
////            }    
//            
//            nuevaespecie.remove(nuevaespecie.size()-1);
//            nuevaespecie.add(0,mejorEspecie); 
//                
//            ArrayList <Especie> hijos = new ArrayList <Especie>();
//            if(l<generaciones-1){
//                for (int i = 0; i < nuevaespecie.size();i++){
//                   Especie especieA = nuevaespecie.get(0);
//                   Especie especieB = nuevaespecie.get((int)((Math.random()*nuevaespecie.size()-1)+1));
//                   //especieB = nuevaespecie.get(1);
//                   hijos.add(mutarEspecie(cruzaEspecieZigZag(especieA,especieB)));
//                   hijos.add(mutarEspecie(cruzaEspecieZigZag(especieB,especieA)));
//                   nuevaespecie.remove(especieA);
//                   nuevaespecie.remove(especieB);
//               }
//               nuevaespecie = hijos;           
//            }
//        }
//        solucion.setMejorEspecie(mejor);    
//        return solucion;      
//    }

//        for (int i = 1; i <= solucion.getEspeciesElegidas().size() - 1; i++) {
//            int limite =i-1;
//            Especie x = solucion.getEspeciesElegidas().get(i);
//            Especie j =  solucion.getEspeciesElegidas().get(limite);
//            while (limite >= 0 && x.getResultado() < j.getResultado() ){
//                solucion.getEspeciesElegidas().set(limite+ 1,j);
//                limite = limite - 1;
//                if(limite>=0){
//                    j =  solucion.getEspeciesElegidas().get(limite);
//                }
//            }
//            solucion.getEspeciesElegidas().set(limite + 1,x);
//        }
//        mejor=solucion.getEspeciesElegidas().get(0).reOrdenamiento(Coordenada.clonar(problema));
//        mejor =nuevaespecie.get(0).reOrdenamiento(Coordenada.clonar(problema));