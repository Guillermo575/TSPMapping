package DiccionarioDatos;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement

public class SolucionTSPConjunto 
{
    ArrayList<SolucionTSP> soluciones;
    
    public void setSoluciones(ArrayList<SolucionTSP> soluciones){this.soluciones = soluciones;}
    @XmlElementWrapper(name="Soluciones")@XmlElement(name="Item") public ArrayList<SolucionTSP> getSoluciones(){return soluciones;}
    
    public void XMLObject(File file)
    {
        try
        {
            FileOutputStream fs = new FileOutputStream(file+".xml");
            JAXBContext context = JAXBContext.newInstance(SolucionTSPConjunto.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //m.marshal(this, System.out);
            m.marshal(this, fs);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
   public static SolucionTSPConjunto ObjectXML(File file)
   {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(SolucionTSPConjunto.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SolucionTSPConjunto Solucion = (SolucionTSPConjunto) jaxbUnmarshaller.unmarshal(file);
            return Solucion;
        }catch(Exception ex)
        {
            return null;
        }
    }
   
    public void JSONObject(File file)
    {
        try
        {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            try 
            {
                FileWriter writer = new FileWriter(file.getAbsoluteFile() + ".json");
                writer.write(json);
                writer.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static SolucionTSPConjunto ObjectJSON(File file){
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return new Gson().fromJson(br, SolucionTSPConjunto.class);
        }catch(Exception ex)
        {
            return null;
        }
    }
    
}
