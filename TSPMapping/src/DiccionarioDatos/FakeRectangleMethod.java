package DiccionarioDatos;
import java.util.ArrayList;

public class FakeRectangleMethod {
   
    public ArrayList<Coordenada> centros = new ArrayList<Coordenada>();
    public ArrayList<Coordenada> mins = new ArrayList<Coordenada>();     
    public ArrayList<Coordenada> maxs = new ArrayList<Coordenada>();
    public BruteForce br = new BruteForce();
    public String resultados = "";   
    int limite = 3;
    double distanciatotal = 0;
    
    public FakeRectangleMethod(){}
        
    public ArrayList<Coordenada> start(MarkerDictionary md)
    {
        //Inicio de la resolucion
        if(md.marcadores.size() > 0)
        {
            ArrayList<Coordenada> lstpuntos = md.marcadores;
            br = new BruteForce();
            br.TipoRuta = md.Type;
            resolverRuta(lstpuntos);
            resultados = resultados(lstpuntos);
            return md.marcadores;
        }else 
        {
            return md.marcadores;
        }
    }
    
    public void resolverRuta(ArrayList<Coordenada> lstpuntos)
    {
        Coordenada centro = new Coordenada(0, 0);
        double MinX = 0;
        double MaxX = 0;
        double MinY = 0;
        double MaxY = 0;
        for(int l = 0; l < lstpuntos.size(); l++)
        {
            Coordenada punto=lstpuntos.get(l);
            if(punto.x < MinX || l == 0)MinX = punto.x;
            if(punto.x > MaxX || l == 0)MaxX = punto.x;
            if(punto.y < MinY || l == 0)MinY = punto.y;
            if(punto.y > MaxY || l == 0)MaxY = punto.y; 
        }
        centro.x = (MinX + MaxX) / 2;
        centro.y = (MinY + MaxY) / 2;
        centros.add(centro);
        mins.add(new Coordenada(MinX, MinY));
        maxs.add(new Coordenada(MaxX, MaxY));
        if(lstpuntos.size() > limite)
        {
           resolverCuadrante(lstpuntos);
        }
    }
    
    public void resolverCuadrante(ArrayList<Coordenada> lstpuntos)
    {
        ArrayList<ArrayList<Coordenada>> cuadrantes = new ArrayList<ArrayList<Coordenada>>();
        ArrayList<Coordenada> destinos = new ArrayList<Coordenada>(4);
        int[]priori = new int[0];
        Coordenada centro = centros.get(centros.size() - 1);
        for(int l = 0; l < 4; l++)
        {
            cuadrantes.add(new ArrayList<Coordenada>());
            destinos.add(new Coordenada(0, 0));
        }          
        for(int l = 0; l < lstpuntos.size(); l++)
        {
            /*Dependiendo de su posicion con respecto a los ejes del centro se iran a un determinado cuadrante*/
            Coordenada punto = lstpuntos.get(l);
            if(punto.x <= centro.x && punto.y <= centro.y){cuadrantes.get(0).add(punto);}
            else if(punto.x > centro.x && punto.y <= centro.y){cuadrantes.get(1).add(punto);}
            else if(punto.x <= centro.x && punto.y > centro.y){cuadrantes.get(3).add(punto);}
            else if(punto.x > centro.x && punto.y > centro.y){cuadrantes.get(2).add(punto);} 
        }             
        for(int l = 0; l < 4; l++)
        {
            ArrayList<Coordenada> puntos = cuadrantes.get(l);
            if(puntos.size() > 0)
            {
                resolverRuta(puntos);
            }
        }   
    }
    
    public String resultados(ArrayList<Coordenada> puntos)
    {
        String resultados = "";
        distanciatotal = 0;
        resultados = ("");
        for(int l = 0; l < puntos.size(); l++)
        {
            if(l > 0) distanciatotal += br.getDistance(puntos.get(l), puntos.get(l - 1));
            else distanciatotal += br.getDistance(puntos.get(l), puntos.get(puntos.size() - 1));
            resultados += (l + ".- (" + (float)puntos.get(l).x + "  ,  " + (float)puntos.get(l).y + ") \n");
        }
        resultados += ("TOTAL = " + (float)distanciatotal + " \n");
        return resultados;
    }

}
