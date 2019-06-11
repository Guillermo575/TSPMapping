package DiccionarioDatos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MarkerDictionary 
{
    /* Guarda los marcadores, en opciones mas avanzadas guarda arreglos de n x n donde n= la cantidad de rutas
      totales y su distancia con cada una, pero por este ejemplo solo se usara la formula de distancias */    
    public ArrayList<Coordenada> marcadores = new ArrayList<Coordenada>();
    public String Type = "EUC_2D";    
    public MarkerDictionary() {}
    
    public void addMarker(String marcador)
    {
        String[] nuevo = marcador.split(",");
        marcadores.add(new Coordenada(Double.parseDouble(nuevo[0]), Double.parseDouble(nuevo[1])));
    }    
    
    public MarkerDictionary TSPLIBRead(File archivo)
    {
        MarkerDictionary nuevo = new MarkerDictionary();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea = reader.readLine();
            while(linea != null){
                try
                {
                    if(linea.replaceAll("\\s+", "").replaceAll(" ", "").trim().indexOf("EDGE_WEIGHT_TYPE:") >= 0)
                    {
                       nuevo.Type = linea.replaceAll("\\s+", " ").replaceAll(" ", "").trim().split("EDGE_WEIGHT_TYPE:")[1].trim();                     
                    }
                }catch(Exception ex){}
                try
                {
                    String []numeros = linea.trim().replaceAll("\\s+", " ").split(" ");
                    nuevo.marcadores.add(new Coordenada(Double.parseDouble(numeros[1]), Double.parseDouble(numeros[2])));
                }catch(Exception ex){}
                linea = reader.readLine();
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return nuevo;
    }
}