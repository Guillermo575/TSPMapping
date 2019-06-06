package DiccionarioDatos;
import java.util.ArrayList;

public class CoordinateRegtanglesMethod {
   
    
    
    final int [][]izquierda=new int[][]{
                        new int[]{0,1,2,3},
                        new int[]{1,2,3,0}, 
                        new int[]{2,1,0,3},
                        new int[]{3,2,1,0},
                    }; 
    final int [][]derecha=new int[][]{
                        new int[]{0,3,2,1},
                        new int[]{1,0,3,2}, 
                        new int[]{2,3,0,1},
                        new int[]{3,0,1,2},
                    }; 
    final int [][]arriba=new int[][]{
                        new int[]{0,3,2,1},
                        new int[]{1,2,3,0}, 
                        new int[]{2,3,0,1},
                        new int[]{3,2,1,0},
                    };    
    final int [][]abajo=new int[][]{
                        new int[]{0,1,2,3},
                        new int[]{1,0,3,2}, 
                        new int[]{2,1,0,3},
                        new int[]{3,0,1,2},
                    };    
    
    
    public ArrayList<Coordenada> centros=new ArrayList<Coordenada>();
    public ArrayList<Coordenada> mins=new ArrayList<Coordenada>();     
    public ArrayList<Coordenada> maxs=new ArrayList<Coordenada>();
    public BruteForce br=new BruteForce();
    public String resultados="";
     int limite=3; //Si cada ruta a resolver excede este limite, se fragmentara por cuadrantes 

    public CoordinateRegtanglesMethod(){}
    
    public int[] getPriori(int cuadranteorigen,int origen, int destino){
        int [] nuevo=new int[0];
        if((cuadranteorigen==1 && destino==0)||(cuadranteorigen==2 && destino==3)|| (destino<0)||(destino>3))nuevo = izquierda[origen];
        if((cuadranteorigen==0 && destino==1)||(cuadranteorigen==3 && destino==2))nuevo = derecha[origen];
        if((cuadranteorigen==2 && destino==1)||(cuadranteorigen==3 && destino==0))nuevo = arriba[origen];
        if((cuadranteorigen==0 && destino==3)||(cuadranteorigen==1 && destino==2))nuevo = abajo[origen];
        return nuevo;
    }
    
    public ArrayList<Coordenada> start(MarkerDictionary md){
        //Inicio de la resolucion
        if(md.marcadores.size()>0){
            ArrayList<Coordenada> lstpuntos=md.marcadores;
            br=new BruteForce();
            br.TipoRuta =md.Type;
            ArrayList<Coordenada> resuelto = resolverRuta(lstpuntos,lstpuntos.get(0),-1,-1);
            resultados = resultados(resuelto);
            return resuelto;
        }else return md.marcadores;
    }
    
    public ArrayList<Coordenada> resolverRuta(ArrayList<Coordenada> lstpuntos,Coordenada destino,int cuadranteorigen, int cuadrantedestino){
        Coordenada centro=new Coordenada(0, 0);
        double MinX=0;
        double MaxX=0;
        double MinY=0;
        double MaxY=0;     
        
        for(int l=0;l<lstpuntos.size();l++){
           /*Se buscan los extremos de cada cuadrante para establecer un punto centro, esto es en caso de que se 
             necesite volver a defragmentar de nuevo el cuadrante*/
           Coordenada punto=lstpuntos.get(l);
           if(punto.x<MinX || l==0)MinX=punto.x;
           if(punto.x>MaxX || l==0)MaxX=punto.x;
           if(punto.y<MinY || l==0)MinY=punto.y;
           if(punto.y>MaxY || l==0)MaxY=punto.y; 
        }
        centro.x=(MinX+MaxX)/2;
        centro.y=(MinY+MaxY)/2;
        centros.add(centro);
        mins.add(new Coordenada(MinX, MinY));
        maxs.add(new Coordenada(MaxX, MaxY));
        /*Si ealcanza menos del limite se resuelve por el metodo de fuerza bruta, si no se fragmentara de manera
          infinita hasta satisfacer el limite*/
        if(lstpuntos.size()<=limite){
            return br.start(lstpuntos,destino);
        }
        else return resolverCuadrante(lstpuntos,cuadranteorigen,cuadrantedestino);
    }
    
    public ArrayList<Coordenada> resolverCuadrante(ArrayList<Coordenada> lstpuntos,int cuadranteorigen, int cuadrantedestino){
        Coordenada centro=centros.get(centros.size()-1);
        Coordenada max=maxs.get(maxs.size()-1);
        Coordenada min=mins.get(mins.size()-1);
        ArrayList<Coordenada> total=new ArrayList<Coordenada>(); //Resultado final
        ArrayList<ArrayList<Coordenada>> cuadrantes= new ArrayList<ArrayList<Coordenada>>();
        ArrayList<Coordenada> destinos=new ArrayList<Coordenada>(4);
        int[]priori=new int[0];
        
        for(int l=0;l<4;l++){
          /*Se crea los nuevos 4 cuadrantes junto con puntos intermedios para conectar cuadrante-cuadrante*/
            cuadrantes.add(new ArrayList<Coordenada>());
            destinos.add(new Coordenada(0,0));
        }

        for(int l=0;l<lstpuntos.size();l++){
           /*Dependiendo de su posicion con respecto a los ejes del centro se iran a un determinado cuadrante*/
           Coordenada punto=lstpuntos.get(l);
                if(punto.x<=centro.x && punto.y<=centro.y){
                    cuadrantes.get(0).add(punto);
                    if(cuadranteorigen==-1)cuadranteorigen=0;
                    if(l==0)priori=getPriori(cuadranteorigen,0,cuadrantedestino);
                }
           else if(punto.x>centro.x && punto.y<=centro.y){
                    cuadrantes.get(1).add(punto);
                    if(cuadranteorigen==-1)cuadranteorigen=1;
                    if(l==0)priori=getPriori(cuadranteorigen,1,cuadrantedestino);
                }
           else if(punto.x<=centro.x && punto.y>centro.y){
                    cuadrantes.get(3).add(punto);
                    if(cuadranteorigen==-1)cuadranteorigen=3;
                    if(l==0)priori=getPriori(cuadranteorigen,3,cuadrantedestino);
                }
           else if(punto.x>centro.x && punto.y>centro.y){
                    cuadrantes.get(2).add(punto);
                    if(cuadranteorigen==-1)cuadranteorigen=2;
                    if(l==0)priori=getPriori(cuadranteorigen,2,cuadrantedestino);                     
                } 
        }             
         
         /*Debido a que se conectan al final los 4 cuadrantes sin relacionarse y el primer punto es el origen 
          se crea unos puntos intermedios para verificar la cercania que hay en el eje X o Y dependiendo de la 
          cuadrante, el mas cercano se convierte en el primer punto*/           
        destinos.set(0,new Coordenada(centro.x,min.y));
        destinos.set(1,new Coordenada(max.x,centro.y));
        destinos.set(2,new Coordenada(centro.x,max.y));       
        destinos.set(3,new Coordenada(min.x,centro.y));    
        
        /*Despues de preparar dichos parametros se llama otra ves al metodo resolverruta en cada cuadrante, 
         el resultado se añadira a la lista total, cabe decir que este cambio de metodos de resolverRuta y 
         resolverCuadrante se pueden llegar a repetir hasta el infinito, hasta que satisfaga el limite*/             
        for(int l=0;l<4;l++){
            
            int siguientedestino=l+1;
            if(siguientedestino>3)siguientedestino=0;
            ArrayList<Coordenada> puntos=cuadrantes.get(priori[l]);
            if(puntos.size()>0){
                
                    if(l==3){
                        switch(priori[3]){
                            case 0: destinos.set(0,new Coordenada(min.x,min.y));
                            case 1: destinos.set(1,new Coordenada(max.x,min.y));
                            case 2: destinos.set(2,new Coordenada(max.x,max.y));                               
                            case 3: destinos.set(3,new Coordenada(min.x,max.y));
                            default : destinos.set(3,new Coordenada(centro.x,centro.y)); 
                        }                    
                    }
                    
                    int origencuadrante=priori[l];
                    int siguiencuadrante=priori[siguientedestino];
                    if(l==3 && cuadrantedestino>=0) {
                        origencuadrante = cuadranteorigen;
                        siguiencuadrante = cuadrantedestino;
                    }
                    puntos=resolverRuta(puntos,destinos.get(priori[l]),origencuadrante,siguiencuadrante);
                    
                    for(int m=0;m<puntos.size();m++){total.add(puntos.get(m));}   
                    /*Busca el punto mas cercano del cuadrante*/
                    if(l<3){
                        Coordenada ultimo=puntos.get(puntos.size()-1);
                        int cuadrantesiguiente=priori[l+1];
                        if(cuadrantes.get(cuadrantesiguiente).isEmpty() &&l<2){
//                            ultimo=destinos.get(cuadrantesiguiente);
//                            ultimo=destinos.get(priori[l]);
                            ultimo = new Coordenada(centro.x,centro.y);
                            cuadrantesiguiente=priori[l+2];  
                        }                     
                        if(cuadrantes.get(cuadrantesiguiente).isEmpty() &&l<1){
//                            ultimo=destinos.get(cuadrantesiguiente);
//                            ultimo=destinos.get(priori[l]);
                            ultimo = new Coordenada(centro.x,centro.y);
                            cuadrantesiguiente=priori[l+3]; 
                        }
                        if(!cuadrantes.get(cuadrantesiguiente).isEmpty()){
                            ArrayList<Coordenada> puntossiguientes=cuadrantes.get(cuadrantesiguiente); 
                            double distanciamenor=br.getDistance(ultimo,puntossiguientes.get(0));
                            int cursor=0;                            
                            Coordenada cambio= new Coordenada(puntossiguientes.get(cursor).x,puntossiguientes.get(cursor).y);  
                            Coordenada cambio2= new Coordenada(puntossiguientes.get(0).x, puntossiguientes.get(0).y);                   
 
                            for(int m=1;m<puntossiguientes.size();m++){
                                double distancianueva=br.getDistance(ultimo,puntossiguientes.get(m));
                              if(distancianueva<distanciamenor){                               
                                    distanciamenor=distancianueva;
                                    cursor=m;
                                    cambio=new Coordenada(puntossiguientes.get(0).x, puntossiguientes.get(0).y);
                                    cambio2=new Coordenada(puntossiguientes.get(cursor).x,puntossiguientes.get(cursor).y);                       
                              }
                            }
                            puntossiguientes.get(0).setCoords(cambio2.x, cambio2.y);
                            puntossiguientes.get(cursor).setCoords(cambio.x, cambio.y);
                            //destinos.get(cuadrantesiguiente).x=puntossiguientes.get(0).x;
                            //destinos.get(cuadrantesiguiente).y=puntossiguientes.get(0).y;                           
                        }
                   }   
            }
//           cuadrantes.set(priori[l],null);
        }   
        return total;
    }
    public String resultados(ArrayList<Coordenada> puntos){
        String resultados="";
        int distanciatotal=0;
        resultados=("");
        for(int l=0;l<puntos.size();l++){
          if(l>0) distanciatotal+=br.getDistance(puntos.get(l),puntos.get(l-1));
          else distanciatotal+=br.getDistance(puntos.get(l),puntos.get(puntos.size()-1));
          resultados += (l+".- ("+(float)puntos.get(l).x+"  ,  "+(float)puntos.get(l).y+") \n");
        }
        resultados += ("TOTAL = "+(float)distanciatotal+" \n");
        return resultados;
    }

}
