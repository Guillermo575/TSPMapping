package DiccionarioDatos;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
@XmlRootElement
public class SolucionTSP 
{    
    ArrayList<Coordenada> listaOriginal = new ArrayList<Coordenada>();
    ArrayList<Coordenada> mejorEspecie = new ArrayList<Coordenada>();
    ArrayList<Especie> especiesElegidas = new ArrayList<Especie>();
    ArrayList<Integer[]> genesElegidos = new ArrayList<Integer[]>(); 
    BruteForce br = new BruteForce();
    Integer[] mejorGen;
    String Descripcion="SolucionTSP";
    
    public void setListaOriginal(ArrayList<Coordenada> ListaOriginal){this.listaOriginal=ListaOriginal;}
    @XmlElementWrapper(name="ListaOriginal")@XmlElement(name="Item") public ArrayList<Coordenada> getListaOriginal(){return listaOriginal;}
      
    public void setEspeciesElegidas(ArrayList <Especie> especiesElegidas){this.especiesElegidas=especiesElegidas;}
    @XmlElementWrapper(name="Especies")@XmlElement(name="Item") public ArrayList <Especie> getEspeciesElegidas(){return especiesElegidas;}
    
    public void setGenesElegidos(ArrayList<Integer[]> genesElegidos){this.genesElegidos=genesElegidos;}
    @XmlElementWrapper(name="Genes")@XmlElement(name="Item") public ArrayList<Integer[]> getGenesElegidos(){return genesElegidos;}   
    
    public void setBruteForce(BruteForce br){this.br=br;}
    @XmlElement(name="BruteForce") public BruteForce getBruteForce(){return br;} 
    
    public void setMejorEspecie(ArrayList<Coordenada> mejorEspecie){this.mejorEspecie=mejorEspecie;}
    @XmlElementWrapper(name="MejorEspecie")@XmlElement(name="Item") public ArrayList<Coordenada> getMejorEspecie(){return mejorEspecie;} 
    
    public void setMejorGen(Integer[] mejorGen){this.mejorGen=mejorGen;}
    @XmlElementWrapper(name="MejorGen")@XmlElement(name="item") public Integer[] getMejorGen(){return mejorGen;}   
    
    public void setDescripcion(String Descripcion){this.Descripcion=Descripcion;}
    @XmlElement(name="Descripcion") public String getDescripcion(){return Descripcion;}   
    
    public ArrayList<Coordenada> reOrdenamientoEspecie(int l)
    {
        return especiesElegidas.get(l).reOrdenamiento(Coordenada.clonar(listaOriginal));
    }

    public ArrayList<Coordenada> reOrdenamientoGen(int l)
    {
        ArrayList<Coordenada> mejor = new ArrayList<Coordenada>();
        for(int c = 0; c < genesElegidos.get(l).length; c++)
        {
            mejor.add(listaOriginal.get(genesElegidos.get(l)[c]));
        }    
        return mejor;
    }

    public ArrayList<Coordenada> mejorSolucionEspecie()
    {
        return mejorEspecie;
    }
    
    public ArrayList<Coordenada> mejorSolucionGen()
    {
        ArrayList<Coordenada> mejor = new ArrayList<Coordenada>();
        for(int c = 0; c < mejorGen.length; c++)
        {
            mejor.add(listaOriginal.get(mejorGen[c]));
        }    
        return mejor;
    }
}
