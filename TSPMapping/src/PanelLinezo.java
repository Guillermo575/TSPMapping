import DiccionarioDatos.Coordenada;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PanelLinezo extends JPanel
{
    private ArrayList<Coordenada> puntosGRM = new ArrayList<Coordenada>();
    private ArrayList<Coordenada> puntosModificado = new ArrayList<Coordenada>();
    private ArrayList<Coordenada> centros = new ArrayList<Coordenada>();         
    private ArrayList<Coordenada> maxs = new ArrayList<Coordenada>();  
    private ArrayList<Coordenada> mins = new ArrayList<Coordenada>();          

    public ArrayList<Coordenada> getPuntos(){ return puntosGRM; }
    public ArrayList<Coordenada> getCentros(){ return centros; }

    private int WidthPan = 600;
    private int HeightPan = 600;
    private boolean Nodos = true;
    private boolean Ruta = true;
    private boolean Final = true;
    private boolean Numeros = true;
    private boolean Diferencias = true;
    private boolean Lineas = true;
    private String resultado = "";
        
    public void setPoints(ArrayList<Coordenada> puntosGRM, ArrayList<Coordenada> centros, ArrayList<Coordenada> minis, ArrayList<Coordenada> maxs)
    {
        this.puntosGRM = puntosGRM;
        this.centros = centros;
        this.maxs = maxs;
        this.mins = minis;
    }
        
    public void setPoints(ArrayList<Coordenada> puntos,ArrayList<Coordenada> puntosModificado, ArrayList<Coordenada> centros,ArrayList<Coordenada> minis, ArrayList<Coordenada> maxs)
    {
        this.puntosGRM = puntos;
        this.puntosModificado = puntosModificado;
        this.centros = centros;
        this.maxs = maxs;
        this.mins = minis;
    }
    
    public void setPanelSize(int WidthPan , int HeightPan)
    {
        this.WidthPan = WidthPan;
        this.HeightPan = HeightPan;
    }
    
    public void refrescar(boolean Nodos, boolean Ruta, boolean Final, boolean Numeros, boolean Lineas, boolean Diferencias, String resultado)
    {
        this.Nodos = Nodos;
        this.Ruta = Ruta;
        this.Final = Final;
        this.Numeros = Numeros;
        this.Lineas = Lineas;
        this.Diferencias = Diferencias;
        this.resultado = resultado; 
        repaint();
    }
        
    public void paint(Graphics g)
    {
        ArrayList<Coordenada> puntosDibujo = new ArrayList<Coordenada>();
        puntosDibujo = puntosGRM;
        if(puntosGRM.isEmpty() || !puntosModificado.isEmpty())
        {
            puntosDibujo = puntosModificado;
        } 
        if(puntosDibujo.size() > 0)
        {
            setPanelSize((getWidth() < getHeight()) ? getWidth() - 100 : getHeight() - 100, (getWidth() < getHeight()) ? getWidth() - 100 : getHeight() - 100);
            double escalahorizontal = (maxs.get(0).x - mins.get(0).x) / WidthPan;
            double escalavertical = (maxs.get(0).y - mins.get(0).y) / HeightPan;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.getWidth(), this.getHeight()); 
            g.setColor(Color.BLACK);
            g.drawString("Resultado : " + resultado, 10, 20);
            for(int l = 0; l < puntosDibujo.size(); l++)
            {
                if(Nodos)
                {
                    g.setColor(Color.BLACK); 
                    g.fillOval(50 + (int)((puntosDibujo.get(l).x - mins.get(0).x) / escalahorizontal) - 2, 
                               50 + (int)((puntosDibujo.get(l).y - mins.get(0).y) / escalavertical) - 2, 
                               5, 5);
                }              
                if(Diferencias)
                {
                    if(!puntosGRM.isEmpty() && (puntosGRM.get(l).x != puntosModificado.get(l).x || puntosGRM.get(l).y != puntosModificado.get(l).y))
                    {
                        g.drawOval(50 +(int)((puntosDibujo.get(l).x - mins.get(0).x) / escalahorizontal) - 15, 
                                   50 + (int)((puntosDibujo.get(l).y - mins.get(0).y) / escalavertical) - 15, 
                                   30, 30);
                    }                  
                }
                if(Ruta)
                {
                    g.setColor(Color.BLACK);
                    if(l > 0)
                    {
                       g.drawLine(50 +(int)((puntosDibujo.get(l).x - mins.get(0).x) / escalahorizontal), 50+(int)((puntosDibujo.get(l).y - mins.get(0).y) / escalavertical), 
                                  50 +(int)((puntosDibujo.get(l-1).x - mins.get(0).x) / escalahorizontal), 50+(int)((puntosDibujo.get(l - 1).y - mins.get(0).y) / escalavertical));
                    }else
                    { 
                        if(Final)
                        {  
                            g.drawLine(50 + (int)((puntosDibujo.get(l).x - mins.get(0).x) / escalahorizontal), 50 + (int)((puntosDibujo.get(l).y - mins.get(0).y) / escalavertical), 
                                       50 + (int)((puntosDibujo.get(puntosDibujo.size()-1).x - mins.get(0).x) / escalahorizontal), 50+(int)((puntosDibujo.get(puntosDibujo.size()-1).y - mins.get(0).y) / escalavertical));
                        }
                    }
                }
                if(Numeros)
                {
                    g.setColor(Color.RED);
                    g.drawString(l + "", 50+(int)((puntosDibujo.get(l).x - mins.get(0).x) / escalahorizontal), 50 + (int)((puntosDibujo.get(l).y - mins.get(0).y) / escalavertical));
                }
            }      
            if(Lineas)
            {
                for(int l = 0; l < centros.size() && l < mins.size() && l < maxs.size(); l++)
                {
                    g.setColor(Color.BLACK);
                    if(l > 0)
                    {
                        g.setColor(new Color((int)(Math.random() * 150),(int)(Math.random() * 150),(int)(Math.random() * 150)));
                    }
                    g.drawLine(50 + (int)((centros.get(l).x - mins.get(0).x) / escalahorizontal), 50 + (int)((mins.get(l).y - mins.get(0).y) / escalavertical), 
                               50 + (int)((centros.get(l).x - mins.get(0).x) / escalahorizontal), 50 + (int)((maxs.get(l).y - mins.get(0).y) / escalavertical));
                    g.drawLine(50 + (int)((mins.get(l).x - mins.get(0).x) / escalahorizontal), 50 + (int)((centros.get(l).y - mins.get(0).y) / escalavertical),
                               50 + (int)((maxs.get(l).x - mins.get(0).x) / escalahorizontal), 50 + (int)((centros.get(l).y - mins.get(0).y) / escalavertical));
                }
            }
            setVisible(true);
        }
    }
}
