package DiccionarioDatos;
import java.util.ArrayList;

public class Coordenada
{
    public double x = 0;
    public double y = 0;
    
    public Coordenada(){}
    
    public Coordenada(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setCoords(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public static ArrayList<Coordenada> clonar(ArrayList<Coordenada> lstpuntos)
    {
        ArrayList<Coordenada> nuevo = new ArrayList<Coordenada>();
        for(int l = 0; l< lstpuntos.size(); l++)
        {
            nuevo.add(new Coordenada(lstpuntos.get(l).x, lstpuntos.get(l).y));
        }
        return nuevo;
    }

    public static Coordenada clonar(Coordenada punto)
    {
        return new Coordenada(punto.x, punto.y);
    }
    
    public static ArrayList<Coordenada> randomList(int total, double min, double max)
    {
        ArrayList<Coordenada> nuevo = new ArrayList<Coordenada>();
        for(int l = 0; l < total; l++)
        {
            nuevo.add(new Coordenada((double) (Math.random() * (max - min)) + min, (double) (Math.random() * (max - min)) + min));
        }
        return nuevo;
    }
}
