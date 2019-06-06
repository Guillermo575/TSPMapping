package DiccionarioDatos;

import java.util.ArrayList;

public class BruteForce {
    
    public String TipoRuta="EUC_2D";
    
    public BruteForce(){}

     public ArrayList<Coordenada> start(ArrayList<Coordenada>lstcoordenadas,Coordenada destino){
        return start(lstcoordenadas,destino,true);
     } 
    public ArrayList<Coordenada> start(ArrayList<Coordenada>lstcoordenadas,Coordenada destino,boolean omitirorigen) {
        double totalmayor = 0;
        double totalmenor = 0;
        ArrayList<Integer> pointer = new ArrayList<Integer>();
        ArrayList<Integer> rutas = new ArrayList<Integer>();
        ArrayList<Integer> puntos = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> mejoresrutas = new ArrayList<ArrayList<Integer>>();
        if(omitirorigen)rutas.add(0);
       for (int c = 0; c <lstcoordenadas.size(); c++) {
           if((!omitirorigen)||(omitirorigen && c>0)){
                puntos.add(c);
                pointer.add(0);
           }
       }
        boolean cerrar = true;
        while (cerrar == true) {
            ArrayList<Integer> nuevaruta = (ArrayList) rutas.clone();
            ArrayList<Integer> nuevospuntos = (ArrayList) puntos.clone();
            for (int m = 0; m < puntos.size(); m++) {
                nuevaruta.add(nuevospuntos.get(pointer.get(m)));
                nuevospuntos.remove(nuevospuntos.get(pointer.get(m)));
            }
            double totalpuntos = 0;
            for (int p = 0; p < nuevaruta.size()-2; p++) {
                int adelante = p + 1;
                if (adelante >= nuevaruta.size()) {adelante = nuevaruta.size()-1;}
//                if(md!=null && md.rutas!=null)totalpuntos+=md.rutas[nuevaruta.get(p)][nuevaruta.get(adelante)];else 
                totalpuntos+=getDistance(lstcoordenadas.get(nuevaruta.get(p)),lstcoordenadas.get(nuevaruta.get(adelante)));
            }
            totalpuntos+=getDistance(lstcoordenadas.get(nuevaruta.get(nuevaruta.size()-1)),destino);
            if (totalpuntos > totalmayor || totalmayor == 0) {
                totalmayor = totalpuntos;
            }
            if (totalpuntos < totalmenor || totalmenor == 0) {
                totalmenor = totalpuntos;
                mejoresrutas = new ArrayList<ArrayList<Integer>>();
            }
            if (totalpuntos == totalmenor) {
                mejoresrutas.add(nuevaruta);
            }              
            //////////////////////
            for (int n = pointer.size() - 1; n >= 0; n--) {
                int nuevo = pointer.get(n);
                if (nuevo + 1 < (puntos.size() - n)) {
                    pointer.set(n, pointer.get(n) + 1);
                    n = -1;
                } else {
                    if (n == 0) {
                        cerrar = false;
                    }
                    pointer.set(n, 0);
                }
            }
            if(pointer.size()==0)cerrar = false;
        }
        ArrayList<Integer> mejorruta=mejoresrutas.get(0);
        ArrayList<Coordenada> lstnueva=new ArrayList<Coordenada>();
        for(int l=0;l<mejorruta.size();l++){
            lstnueva.add(lstcoordenadas.get(mejorruta.get(l)));
        }
        return lstnueva;
    }
    
    public double distanciaTotal(ArrayList<Coordenada> puntos){
        double distanciatotal=0;
        for(int l=0;l<puntos.size();l++){
            if(l>0) distanciatotal+=getDistance(puntos.get(l),puntos.get(l-1));
            else distanciatotal+=getDistance(puntos.get(l),puntos.get(puntos.size()-1));
        }
        return distanciatotal;
    }
    
       public double getDistance(Coordenada a,Coordenada b){
                 if(TipoRuta.equals("EUC_2D"))return getEuclidianDistance(a,b);
            else if(TipoRuta.equals("ATT"))return getPseudoEuclidianDistance(a,b);
            else if(TipoRuta.equals("GEO"))return getGeographicalDistance(a,b);
            else if(TipoRuta.equals("CEIL_2D"))return getCeilingDistance(a,b);
            else return getEuclidianDistance(a,b);
       }
    
        public double getEuclidianDistance(Coordenada a,Coordenada b){
    	  //return Math.round(Math.sqrt((a.x-b.x)+(a.y-b.y))+0.5);
            double xd =a.x-b.x;
            double yd =a.y-b.y;
            return Math.rint(Math.sqrt((xd*xd)+(yd*yd))+0.5);
       }

       public double getPseudoEuclidianDistance(Coordenada a,Coordenada b){
    	  //return Math.round(Math.sqrt((a.x-b.x)+(a.y-b.y))+0.5);
            double xd =a.x-b.x;
            double yd =a.y-b.y;
            double rij = Math.sqrt(((xd*xd)+(yd*yd))/10.0);
            double tij = Math.rint(rij);
            if(tij<rij) return tij+1;
            else return tij;
       }
        public static double toGeographical(double x) {
            final double PI = 3.141592;
            double deg = Math.rint(x);
            double min = x - deg;
            return PI * (deg + 5.0 * min / 3.0) / 180.0;
	}

        public double getGeographicalDistance(Coordenada a,Coordenada b){
		double latitude1 = toGeographical(a.x);
		double latitude2 = toGeographical(b.x);
		double longitude1 = toGeographical(a.y);
		double longitude2 = toGeographical(b.y);
		double radius = 6378.388;
		double q1 = Math.cos(longitude1 - longitude2);
		double q2 = Math.cos(latitude1 - latitude2);
		double q3 = Math.cos(latitude1 + latitude2);
                return Math.floor(radius * Math.acos(0.5 * ((1.0 + q1)*q2 - (1.0 - q1)*q3)) + 1.0);
        }
        
        public double getCeilingDistance(Coordenada a,Coordenada b) {
		return Math.ceil(getEuclidianDistance(a,b));
	}
}
