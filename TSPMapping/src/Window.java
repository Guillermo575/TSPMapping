import Metaheuristicas.*;
import DiccionarioDatos.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Window extends JFrame
{
    JMenuBar menuBarra = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
            JMenuItem btnGuardar = new JMenuItem("Guardar");
            JMenuItem btnGuardarXML = new JMenuItem("Guardar Prueba (XML)");
            JMenuItem btnGuardarJSON = new JMenuItem("Guardar Prueba (JSON)");
            JMenuItem btnExperimentoJSON = new JMenuItem("Realizar Experimento (JSON)");
            JMenuItem btnExperimentoMasivoJSON = new JMenuItem("Realizar Experimento Masivo (JSON)");
            JMenuItem btnAbrir = new JMenuItem("Abrir");
            JMenuItem btnAbrirSolucion = new JMenuItem("Abrir Prueba");
            JMenuItem btnAbrirSinResolver = new JMenuItem("Abrir Sin resolver");
        JMenu menuOpciones = new JMenu("Opciones");
            JMenuItem btnAleatorio = new JMenuItem("Aleatorio");
            JMenuItem btnBorrarResumen = new JMenuItem("Borrar Resumen");   
        JMenu menuVer = new JMenu("Ver");
            JRadioButton btnRuta = new JRadioButton("ruta", true);
            JRadioButton btnNodos = new JRadioButton("puntos", true);
            JRadioButton btnLineas = new JRadioButton("lineas", true);
            JRadioButton btnNumeros = new JRadioButton("numeros", true);
            JRadioButton btnDiferencias = new JRadioButton("diferencias", true);  
            JRadioButton btnFinal = new JRadioButton("final", true);  
        JMenu menuModificadores= new JMenu("Modificadores");
            JRadioButton btnRecocido = new JRadioButton("Recocido Simulado", false);
            JRadioButton btnGreedy = new JRadioButton("Busqueda Greedy", false);
            JRadioButton btnGenetico = new JRadioButton("Algoritmo Genetico", false);   
            JRadioButton btnExhaustivo = new JRadioButton("Busqueda Exhaustiva", false);
            JRadioButton btnCuadrantes = new JRadioButton("Cuadrantes", false); 
        JMenuItem btnRecargar = new JMenuItem("Recargar");
        
    JPanel ConfigPanel = new JPanel();
        JTextField txtTotalAleatorio = new JTextField("60");
        JTextField txtRecocidoCiclos = new JTextField("10000");
        JTextField txtRecocidoFactorTemp = new JTextField("600");
        JTextField txtRecocidoAleatorios = new JTextField("100");
        JTextField txtRecocidoGenMin = new JTextField("2");   
        JTextField txtRecocidoGenMax = new JTextField("5");

        JTextField txtGreedyCiclos = new JTextField("10000");
        JTextField txtGreedyAleatorios = new JTextField("100");
        JTextField txtGreedyGenMin = new JTextField("2");   
        JTextField txtGreedyGenMax = new JTextField("5");

        JTextField txtGeneticoPoblacion = new JTextField("100");
        JTextField txtGeneticoGeneraciones = new JTextField("1000");
        JTextField txtGeneticoAleatorios = new JTextField("100");
        JTextField txtGeneticoGenMin = new JTextField("2");   
        JTextField txtGeneticoGenMax = new JTextField("5");
        JTextField txtGeneticoMutMin = new JTextField("10");   
        JTextField txtGeneticoMutMax = new JTextField("40");
        JTextField txtGeneticoNumGen = new JTextField("300");
        JTextField txtGeneticoCullingPercentage = new JTextField("75");   
        JTextField txtGeneticoMutationRate = new JTextField("3");
        JTextField txtGeneticoRangoElitismo = new JTextField("10");
        
    ChartResult panelchart = new ChartResult();
        JPanel GraphicPanel = new JPanel();   
        
    PanelLinezo panel = new PanelLinezo();
    JTextArea area = new JTextArea("");
    JTextArea areaResumen = new JTextArea("");

    MarkerDictionary actual = new MarkerDictionary();
    SolucionTSPConjunto actualsolucion;
    double distanciatotal = 0; 
    String actualrute = "";
    String filename = "Ejemplo";
    
    public Window()
    {
        activarModo(new JRadioButton[]{btnCuadrantes});
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.setBounds(0,0,900,800);    
        this.setJMenuBar(menuBarra);
        
        menuBarra.add(menuArchivo);
        menuBarra.add(menuOpciones);
        menuBarra.add(menuModificadores);
        menuBarra.add(menuVer);
        
        menuArchivo.add(btnGuardar);
        menuArchivo.add(btnAbrir);
        menuArchivo.add(btnAbrirSinResolver);
        menuArchivo.add(new JSeparator());
        menuArchivo.add(btnAbrirSolucion);
        menuArchivo.add(btnGuardarXML);
        menuArchivo.add(btnGuardarJSON);
        menuArchivo.add(new JSeparator());
        menuArchivo.add(btnExperimentoJSON);
        menuArchivo.add(btnExperimentoMasivoJSON);
        
        JLabel labelHeuristicas = new JLabel ("Heuristicas");
        labelHeuristicas.setFont(labelHeuristicas.getFont ().deriveFont (11.0f));
        labelHeuristicas.setForeground(Color.GRAY);
        menuModificadores.add(labelHeuristicas);
        menuModificadores.add(btnCuadrantes); 
        menuModificadores.add(btnExhaustivo); 
        JLabel labelMetaheuristicas = new JLabel ("Metaheuristicas");
        labelMetaheuristicas.setFont(labelHeuristicas.getFont ().deriveFont (11.0f));
        labelMetaheuristicas.setForeground(Color.GRAY);
        menuModificadores.add(labelMetaheuristicas); 
        menuModificadores.add(btnRecocido);
        menuModificadores.add(btnGreedy);
        menuModificadores.add(btnGenetico);

        menuOpciones.add(btnAleatorio);
        menuOpciones.add(btnBorrarResumen);
        menuBarra.add(btnRecargar);
        
        menuVer.add(btnRuta);
        menuVer.add(btnNodos);
        menuVer.add(btnLineas);
        menuVer.add(btnNumeros);
        menuVer.add(btnDiferencias);
        menuVer.add(btnFinal);
        
        GraphicPanel.setLayout(new BorderLayout());
        GraphicPanel.add(panel,"Center");
        
        GridLayout gridLayout  = new GridLayout(33,2);
        ConfigPanel.setSize(100,100);
        ConfigPanel.setLayout(gridLayout);
        ConfigPanel.add(new JLabel("*Aleatorio"));
        ConfigPanel.add(new JLabel(""));
        ConfigPanel.add(new JLabel("lugares"));
        ConfigPanel.add(txtTotalAleatorio);
        ConfigPanel.add(new JLabel("*Recocido Simulado"));
        ConfigPanel.add(new JLabel(""));
        ConfigPanel.add(new JLabel("Ciclos"));
        ConfigPanel.add(txtRecocidoCiclos);
        ConfigPanel.add(new JLabel("Factor Temperatura"));
        ConfigPanel.add(txtRecocidoFactorTemp);
        ConfigPanel.add(new JLabel("Genes maximos"));
        ConfigPanel.add(txtRecocidoAleatorios);
        ConfigPanel.add(new JLabel("Distancia Gen Minimo"));
        ConfigPanel.add(txtRecocidoGenMin);
        ConfigPanel.add(new JLabel("Distancia Gen Maximo"));
        ConfigPanel.add(txtRecocidoGenMax);
        
        ConfigPanel.add(new JLabel("*Greedy"));
        ConfigPanel.add(new JLabel(""));
        ConfigPanel.add(new JLabel("Ciclos"));
        ConfigPanel.add(txtGreedyCiclos);
        ConfigPanel.add(new JLabel("Genes maximos"));
        ConfigPanel.add(txtGreedyAleatorios);
        ConfigPanel.add(new JLabel("Distancia Gen Minimo"));
        ConfigPanel.add(txtGreedyGenMin);
        ConfigPanel.add(new JLabel("Distancia Gen Maximo"));
        ConfigPanel.add(txtGreedyGenMax);
        
        ConfigPanel.add(new JLabel("*Genetico"));
        ConfigPanel.add(new JLabel(""));
        ConfigPanel.add(new JLabel("Generaciones"));
        ConfigPanel.add(txtGeneticoGeneraciones);
        ConfigPanel.add(new JLabel("Poblacion"));
        ConfigPanel.add(txtGeneticoPoblacion);
        ConfigPanel.add(new JLabel("Genes maximos"));
        ConfigPanel.add(txtGeneticoAleatorios);
        ConfigPanel.add(new JLabel("Distancia Gen Minimo"));
        ConfigPanel.add(txtGeneticoGenMin);
        ConfigPanel.add(new JLabel("Distancia Gen Maximo"));
        ConfigPanel.add(txtGeneticoGenMax);
        ConfigPanel.add(new JLabel("Rango de Mutacion Minimo"));
        ConfigPanel.add(txtGeneticoMutMin);
        ConfigPanel.add(new JLabel("Rango de Mutacion Maximo"));
        ConfigPanel.add(txtGeneticoMutMax);
        ConfigPanel.add(new JLabel("Rango de Elitismo"));
        ConfigPanel.add(txtGeneticoRangoElitismo);        
        
//        ConfigPanel.add(new JLabel("Generaciones"));
//        ConfigPanel.add(txtGeneticoNumGen);
//        ConfigPanel.add(new JLabel("Porcentaje de Exito"));
//        ConfigPanel.add(txtGeneticoCullingPercentage);
//        ConfigPanel.add(new JLabel("Rango de Mutacion"));
//        ConfigPanel.add(txtGeneticoMutationRate);
                
        JTabbedPane tp = new JTabbedPane();
        tp.add(GraphicPanel,"Grafico");
        tp.add(new JScrollPane(area),"Puntos");        
        tp.add(panelchart,"Diagrama");
        tp.add(ConfigPanel,"Configuracion");
        tp.add(new JScrollPane(areaResumen),"Resumen");
        
        this.setLayout(new BorderLayout());
        this.add(tp,"Center");
        
        area.setEditable(false);
        areaResumen.setEditable(false);
        
        btnRuta.addActionListener(pintar());
        btnNodos.addActionListener(pintar());
        btnLineas.addActionListener(pintar());
        btnNumeros.addActionListener(pintar());
        btnDiferencias.addActionListener(pintar());
        btnFinal.addActionListener(pintar());
        btnAleatorio.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    aleatorio();
                }
           } 
        );
        btnBorrarResumen.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    BorrarResumen();
                }
           } 
        );
        btnAbrir.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    abrir();
                }
           } 
        );
        btnGuardar.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    guardar();
                }
           } 
        );
        btnAbrirSinResolver.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    abrirSinResolver();
                }
           } 
        );
        btnRecargar.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            menuBarra.setVisible(false);
                            setPoints(actual);
                            menuBarra.setVisible(true);
                        }
                    }.start();
                }
           } 
        );
        btnAbrirSolucion.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    abrirSolucion();
                }
           } 
        );
        btnGuardarXML.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    guardarSolucionXML();
                }
           } 
        );
        btnGuardarJSON.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    guardarSolucionJSON();
                }
           } 
        );
        btnExperimentoJSON.addActionListener(
           new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    guardarExperimentoJSON();
                }
           } 
        );
        btnExperimentoMasivoJSON.addActionListener
        (
           new ActionListener()
           {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    guardarExperimentoMasivoJSON();
                }
           } 
        );
    }
    
    public ActionListener pintar()
    {
        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Recarga();
            }
        };
    }
    
    public void aleatorio()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                menuBarra.setVisible(false);
                MarkerDictionary nuevo = new MarkerDictionary();
                nuevo.marcadores = Coordenada.randomList(Integer.parseInt(txtTotalAleatorio.getText()),50,600);
                setPoints(nuevo);  //Visualiza los puntos recibiendolos como parametros 
                setVisible(true);    
                menuBarra.setVisible(true);
            }
        }.start();
    }
    
    public void BorrarResumen()
    {
        areaResumen.setText("");
    }
    
    public void abrir()
    {
        JFileChooser fileChooser=new JFileChooser(actualrute);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TSPLIB (.tsp,.vrp)", "tsp","vrp");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            final File fichero = fileChooser.getSelectedFile();
            this.setTitle(fichero.getName());
            actualrute = fichero.getParent();
            filename = fichero.getName();
            new Thread()
            {
                @Override
                public void run()
                {
                    menuBarra.setVisible(false);
                    MarkerDictionary md = new MarkerDictionary(); //Explicacion en el archivo de la clase
                    md=md.TSPLIBRead(fichero);
                    setPoints(md);  //Visualiza los puntos recibiendolos como parametros    
                    menuBarra.setVisible(true);
                }
            }.start();
        }
    }

    public void abrirSinResolver()
    {
        JFileChooser fileChooser=new JFileChooser(actualrute);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TSPLIB (.tsp,.vrp)", "tsp","vrp");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            final File fichero = fileChooser.getSelectedFile();
            actualrute = fichero.getParent();
            filename = fichero.getName();
            new Thread()
            {
               @Override
               public void run()
               {
                    menuBarra.setVisible(false);
                    MarkerDictionary md = new MarkerDictionary(); //Explicacion en el archivo de la clase
                    md = md.TSPLIBRead(fichero);
                    setFakePoints(md);  //Visualiza los puntos recibiendolos como parametros 
                    menuBarra.setVisible(true);
               }
            }.start();
        }
    }
    
    public void abrirSolucion()
    {
        JFileChooser fileChooser = new JFileChooser(actualrute);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Datos (.XML,.JSON)", "xml","json");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            File fichero = fileChooser.getSelectedFile();
            actualrute = fichero.getParent();
            filename = fichero.getName();
            int extensionIndex = fichero.getName().lastIndexOf(".");
            String fileExtension = fichero.getName().substring(extensionIndex + 1).toUpperCase();
            if(fileExtension.equals("JSON")||fileExtension.equals("XML"))
            {
                if(fileExtension.equals("JSON")) actualsolucion = SolucionTSPConjunto.ObjectJSON(fichero);
                if(fileExtension.equals("XML")) actualsolucion = SolucionTSPConjunto.ObjectXML(fichero);
                panelchart.makeChart(actualsolucion,(int)((double)this.getWidth()*0.75),(int)((double)this.getHeight()*0.75));                   
                MarkerDictionary md = new MarkerDictionary();
                md.marcadores=actualsolucion.getSoluciones().get(actualsolucion.getSoluciones().size()-1).getMejorEspecie();
                setTestPoints(md);
                Recarga();
            }
        }
    }
    
    public void guardar()
    {
        JFileChooser fileChooser = new JFileChooser(actualrute);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TSPLIB (.tsp)", "tsp");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File(filename+"_"));
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            File fichero = fileChooser.getSelectedFile();
            actualrute = fichero.getParent();
            try
            {
                BufferedWriter fw =new BufferedWriter( new FileWriter(fileChooser.getSelectedFile()+".tsp"));
                for(int l =0; l < panel.getPuntos().size(); l++)
                {
                    fw.write((l + 1) + " "+ panel.getPuntos().get(l).x + " " + panel.getPuntos().get(l).y);
                    fw.newLine();
                }
                fw.close();
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }   
    }    
    
    public void guardarSolucionXML()
    {
        JFileChooser fileChooser = new JFileChooser(actualrute);
        fileChooser.setSelectedFile(new File(filename));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Datos (.XML)", "xml");
        fileChooser.setFileFilter(filter);
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            File fichero = fileChooser.getSelectedFile();
            filename = fichero.getName();
            actualsolucion.XMLObject(fichero);
        }   
    } 
 
    public void guardarSolucionJSON()
    {
        JFileChooser fileChooser = new JFileChooser(actualrute);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Datos (.JSON)","json");
        fileChooser.setSelectedFile(new File(filename));
        fileChooser.setFileFilter(filter);
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            File fichero = fileChooser.getSelectedFile();
            filename = fichero.getName();
            actualsolucion.JSONObject(fichero);
        }   
    } 
    
    public void activarModo(JRadioButton[] botones)
    {
        btnRecocido.setSelected(false);
        btnGreedy.setSelected(false);
        btnGenetico.setSelected(false);   
        btnExhaustivo.setSelected(false);
        btnCuadrantes.setSelected(false);
        for(int l = 0; l < botones.length; l++)
        {
            botones[l].setSelected(true);
        }
   }
    
   public void guardarExperimentoJSON()
   {
        final int cantidad = Integer.parseInt(JOptionPane.showInputDialog(this,"Cantidad de ejecuciones",100));
        final JFileChooser fileChooser = new JFileChooser(actualrute);
        fileChooser.setSelectedFile(new File(filename));
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Datos (.JSON)","json");
        fileChooser.setFileFilter(filter);
        final int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    menuBarra.setVisible(false);
                    File fichero = fileChooser.getSelectedFile(); 
                    filename = fichero.getName(); 
                    fichero = new File(fileChooser.getSelectedFile().getAbsolutePath() + "/experimento/"); 
                    String resultadosGlobal = realizarExperimentoJSON(fichero,filename,cantidad);
                    registrarResultadosExperimentoJSON(resultadosGlobal,fileChooser.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "¡Experimento Completado!");      
                    menuBarra.setVisible(true);
                }
            }.start();
         }
    } 
   
    public void guardarExperimentoMasivoJSON()
    {
        final int cantidad = Integer.parseInt(JOptionPane.showInputDialog(this,"Cantidad de ejecuciones",100));
        final JFileChooser fileChooser = new JFileChooser(actualrute);
        fileChooser.setSelectedFile(new File(filename));
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Datos (.JSON)","json");
        fileChooser.setFileFilter(filter);
        final int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    menuBarra.setVisible(false);
                    File fichero = fileChooser.getSelectedFile(); 
                    filename = fichero.getName(); 
                    File ficheroexp = new File(fichero.getAbsolutePath());
                    String resultadosGlobal="";
                    Date dateMasivo1 =  new Date(System.currentTimeMillis());
                    areaResumen.setText("");
                    areaResumen.append("--------Iniciando Experimento Masivo---------\n\n");
                    resultadosGlobal += realizarExperimentoMasivoPaso("Original",new JRadioButton[]{btnCuadrantes},new File(ficheroexp.getAbsolutePath()+"/Con_Cuadrantes/Original/"),1);
                    resultadosGlobal += realizarExperimentoMasivoPaso("Recocido_simulado c/cuadrantes",new JRadioButton[]{btnCuadrantes,btnRecocido},new File(ficheroexp.getAbsolutePath()+"/Con_Cuadrantes/Recocido_simulado/"),cantidad);
                    resultadosGlobal += realizarExperimentoMasivoPaso("Greedy c/cuadrantes",new JRadioButton[]{btnCuadrantes,btnGreedy},new File(ficheroexp.getAbsolutePath()+"/Con_Cuadrantes/Greedy/"),cantidad);
                    resultadosGlobal += realizarExperimentoMasivoPaso("Genetico c/cuadrantes",new JRadioButton[]{btnCuadrantes,btnGenetico},new File(ficheroexp.getAbsolutePath()+"/Con_Cuadrantes/Genetico/"),cantidad);
                    resultadosGlobal += realizarExperimentoMasivoPaso("Recocido_simulado s/cuadrantes",new JRadioButton[]{btnRecocido},new File(ficheroexp.getAbsolutePath()+"/Sin_Cuadrantes/Recocido_simulado/"),cantidad);
                    resultadosGlobal += realizarExperimentoMasivoPaso("Greedy s/cuadrantes",new JRadioButton[]{btnGreedy},new File(ficheroexp.getAbsolutePath()+"/Sin_Cuadrantes/Greedy/"),cantidad);
                    resultadosGlobal += realizarExperimentoMasivoPaso("Genetico s/cuadrantes",new JRadioButton[]{btnGenetico},new File(ficheroexp.getAbsolutePath()+"/Sin_Cuadrantes/Genetico/"),cantidad);
                    Date dateMasivo2 =  new Date(System.currentTimeMillis());
                    areaResumen.append("\n" + "--------Fin de Experimento Masivo---------\n" + "Tiempo de Ejecucion " + getDateDiff(dateMasivo1, dateMasivo2, TimeUnit.SECONDS)+" Segundos "+"\n");
                    menuBarra.setVisible(true);
                    registrarResultadosExperimentoJSON(resultadosGlobal,ficheroexp.getAbsolutePath());
                    JOptionPane.showMessageDialog(null,"¡Experimento Completado!"); 
                }
            }.start();
         }
    } 
    public void registrarResultadosExperimentoJSON(String resultadosGlobal, String ficheroexp)
    {
        String [] resultadosArray = resultadosGlobal.split("\n");
        String resultadosGlobalTotal="";
        int mejorSerie = 0;
        int mejorSerieMeta = 0;
        int Serie = 0;
        Double ResultadoOriginal = 0.0;
        Double mejorResultado = 0.0;
        Double resultadoPromedio = 0.0;
        Double peorResultado = 0.0;
        Double mejorResultadoMeta = 0.0;
        Double peorResultadoMeta = 0.0;
        String MetaResultado = "";
        String MejorMetaResultado = "";
        Boolean comienzo=false;
        for(int l =0; l<resultadosArray.length; l++)
        {
            Boolean esDoble = false;
            try
            {
               Double.parseDouble(resultadosArray[l]);
               esDoble = true;
            }catch(Exception ex)
            {
            }
            if(esDoble)
            {
                Double valor = Double.parseDouble(resultadosArray[l]);
                resultadoPromedio += valor;
//                            resultadosGlobalTotal += resultadosArray[l] + "\n";
                if(MetaResultado.equals("Original"))
                {
                    ResultadoOriginal = valor;
                }
                if(valor < mejorResultado || mejorResultado == 0) 
                {
                    MejorMetaResultado = MetaResultado; 
                    mejorResultado = valor;
                    mejorSerie = Serie;
                }
                if(valor >= peorResultado || peorResultado == 0) peorResultado = valor;
                if(valor < mejorResultadoMeta || mejorResultadoMeta == 0) 
                {
                    mejorResultadoMeta = valor;
                    mejorSerieMeta = Serie;
                }
                if(valor >= peorResultadoMeta || peorResultadoMeta ==0) peorResultadoMeta = valor;
                Serie++;                       
            }
           if(!esDoble || l == resultadosArray.length - 1)
           {
                if((comienzo || l>0))
                {
                    if(!MetaResultado.equals("Original"))
                    {
                        resultadosGlobalTotal += "Mejor resultado : " + mejorResultadoMeta + "\n";
                        resultadosGlobalTotal += "Peor resultado : " + peorResultadoMeta + "\n";
                        resultadosGlobalTotal += "Promedio : " + resultadoPromedio/(Serie) + "\n";
                        resultadosGlobalTotal += "Mejor Serie : " + mejorSerieMeta + "\n";                    
                    }else
                    {
                        resultadosGlobalTotal += ResultadoOriginal + "\n";
                    }
                    resultadosGlobalTotal += "\n";
                    mejorResultadoMeta = 0.0;
                    peorResultadoMeta = 0.0;
                    resultadoPromedio = 0.0;
                    Serie = 0;
                    mejorSerieMeta = 0;
                }else
                {
                    comienzo=true;
                }
                if(!esDoble)
                {
                    resultadosGlobalTotal += resultadosArray[l] + "\n";  
                }
                MetaResultado = resultadosArray[l];
           }
        }
        resultadosGlobalTotal = "Mejor resultado : " + MejorMetaResultado+"(Serie No." + mejorSerie + ")" + "  " + mejorResultado + "\n\n" + resultadosGlobalTotal;
        try
        {
            File ficheroResultadoGlobal = new File(ficheroexp + "/" + filename + "_Global.txt");
            BufferedWriter fw = new BufferedWriter( new FileWriter(ficheroResultadoGlobal));
            fw.write(resultadosGlobal);
            fw.close();
            File ficheroResultadoLog = new File(ficheroexp + "/" + filename + "_Log.txt");
            fw =new BufferedWriter( new FileWriter(ficheroResultadoLog));
            fw.write(areaResumen.getText());
            fw.close();
            File ficheroResultadoDetallado = new File(ficheroexp + "/" + filename + "_Detallado.txt");
            fw = new BufferedWriter( new FileWriter(ficheroResultadoDetallado));
            fw.write(resultadosGlobalTotal);
            fw.close();
            File ficheroConfiguracion = new File(ficheroexp + "/" + filename + "_Config.txt");
            fw =new BufferedWriter( new FileWriter(ficheroConfiguracion));
            fw.write("*Recocido Simulado" + "\n");        
            fw.write("Ciclos : " + txtRecocidoCiclos.getText() + "\n" + "Factor Temperatura : " + txtRecocidoFactorTemp.getText() + "\n" + 
                     "Genes maximos : " + txtRecocidoAleatorios.getText() + "\n" + "Distancia Gen Minimo : " + txtRecocidoGenMin.getText() + "\n" +
                     "Distancia Gen Maximo : " + txtRecocidoGenMax.getText() + "\n\n");        
            fw.write("*Greedy" + "\n");        
            fw.write("Ciclos : " + txtGreedyCiclos.getText() + "\n"+"Genes maximos : " + txtGreedyAleatorios.getText() + "\n" + 
                     "Distancia Gen Minimo : " + txtGreedyGenMin.getText() + "\n" + "Distancia Gen Maximo : " + txtGreedyGenMax.getText() + "\n\n");        
            fw.write("*Genetico" + "\n");       
            fw.write("Generaciones : " + txtGeneticoGeneraciones.getText()+"\n" + "Poblacion : " + txtGeneticoPoblacion.getText() + "\n" +
                     "Genes maximos : " + txtGeneticoAleatorios.getText() + "\n" + "Distancia Gen Minimo : "+txtGeneticoGenMin.getText() + "\n" +
                     "Distancia Gen Maximo : " + txtGeneticoGenMax.getText() + "\n" + "Rango de Mutacion Minimo : " + txtGeneticoMutMin.getText() + "\n" +
                     "Rango de Mutacion Maximo : " + txtGeneticoMutMax.getText() + "\n\n");
            fw.close();
        }catch(Exception ex)
        {
            ex.printStackTrace();
            areaResumen.setText(ex.getMessage() + " \n" + " \n");
        }
    }
    public String realizarExperimentoMasivoPaso(String titulo, JRadioButton[] modalidad, File ficheroexp, int cantidad)
    {
        String resultados = "";
        Date date1 =  new Date(System.currentTimeMillis());
        areaResumen.append("--------Iniciando Experimento " + titulo + "---------\n");
        activarModo(modalidad);
        resultados += titulo + "\n" + realizarExperimentoJSON(ficheroexp, filename, cantidad);
        Date date2 = new Date(System.currentTimeMillis());
        areaResumen.append("--------Terminando Experimento " + titulo + "---------" + "\n" +
                           "          Tiempo de Ejecucion " + getDateDiff(date1,date2,TimeUnit.SECONDS) + " Segundos " + "\n");
        return resultados;
    }
    
    public String realizarExperimentoJSON(File fichero, String nombre, int cantidad)
    {
       String resultadoString = "";        
        try
        {
            if(!fichero.exists())fichero.mkdirs();
            String rutaFile = fichero.getPath() + "/" + nombre;
            File ficheroResultado = new File(rutaFile + ".txt");
            BufferedWriter fw = new BufferedWriter( new FileWriter(ficheroResultado));
            for(int l = 0; l < cantidad; l++)
            {
               areaResumen.setText(areaResumen.getText() + "Realizando Experimento No." + (l + 1) + " \n");
               setPoints(actual);
               File ficheronuevo = new File(rutaFile + "_" + l);
               File ficheroimagen = new File(rutaFile + "_" + l + ".png");
               actualsolucion.JSONObject(ficheronuevo);
               panelchart.makeChartImage(ficheroimagen, panelchart.getWidth(), panelchart.getHeight());
               String totalExp = (area.getText().split("TOTAL = ").length>0) ? area.getText().split("TOTAL = ")[1] : "";
               fw.write(totalExp);
               resultadoString += totalExp;
//                fw.newLine();
//               areaResumen.setText(areaResumen.getText()+"Experimento No."+(l+1)+" finalizado"+" \n");
            } 
            fw.close();
         }catch(Exception ex)
         {
             ex.printStackTrace();
             areaResumen.append(ex.getMessage() + " \n" + " \n");
         }
        return resultadoString;
    }
    
    public void setPoints(MarkerDictionary md)
    {
        Date date1 = new Date(System.currentTimeMillis());
        this.actual = md;
        GoldenRectangleMethod GRM = new GoldenRectangleMethod();
        FakeRectangleMethod FRM = new FakeRectangleMethod();
        ArrayList<Coordenada> puntos = new ArrayList<Coordenada>();
        ArrayList<Coordenada> puntosGRM = new ArrayList<Coordenada>();
        BruteForce BR = new BruteForce();
        ArrayList<Coordenada> centros = new ArrayList<Coordenada>();
        ArrayList<Coordenada> mins = new ArrayList<Coordenada>();
        ArrayList<Coordenada> maxs = new ArrayList<Coordenada>();
        if(btnCuadrantes.isSelected())
        {
            puntos = GRM.start(md);
            puntosGRM = Coordenada.clonar(puntos);
            distanciatotal = GRM.br.distanciaTotal(puntos);
            BR = GRM.br;
            centros = GRM.centros;
            mins = GRM.mins;
            maxs = GRM.maxs;
        }else
        {
            puntos=FRM.start(md);
            distanciatotal = FRM.br.distanciaTotal(puntos);
            BR = FRM.br;
            centros = FRM.centros;
            mins = FRM.mins;
            maxs = FRM.maxs;
        }
        ArrayList<SolucionTSP> soluciones = new ArrayList<SolucionTSP>();
        if(btnRecocido.isSelected()) 
        {
            RecocidoSimulado solucion = new RecocidoSimulado(puntos,BR);
            solucion.setMinimo(Integer.parseInt(txtRecocidoGenMin.getText()));
            solucion.setMaximo(Integer.parseInt(txtRecocidoGenMax.getText()));
            solucion.setTotalAleatorio(Integer.parseInt(txtRecocidoAleatorios.getText()));
            double temp = Double.parseDouble(txtRecocidoFactorTemp.getText()) * 0.0000000001;
            SolucionTSP mejor = solucion.start(Integer.parseInt(txtRecocidoCiclos.getText()), distanciatotal * temp);
            puntos = mejor.mejorSolucionEspecie();
            soluciones.add(mejor);
        }
        if(btnGreedy.isSelected())
        {
            BusquedaGreedyTSP solucion = new BusquedaGreedyTSP(puntos,BR);
            solucion.setMinimo(Integer.parseInt(txtGreedyGenMin.getText()));
            solucion.setMaximo(Integer.parseInt(txtGreedyGenMax.getText()));
            solucion.setTotalAleatorio(Integer.parseInt(txtGreedyAleatorios.getText()));
            SolucionTSP mejor = solucion.start(Integer.parseInt(txtGreedyCiclos.getText()));
            puntos = mejor.mejorSolucionEspecie();
            soluciones.add(mejor);
        }
        if(btnGenetico.isSelected())
        {
            Genetico solucion = new Genetico(puntos, BR);
            solucion.setMinimo(Integer.parseInt(txtGeneticoGenMin.getText()));
            solucion.setMaximo(Integer.parseInt(txtGeneticoGenMax.getText()));
            solucion.setMutacionMinimo(Double.parseDouble(txtGeneticoMutMin.getText()) * 0.01);
            solucion.setMutacionMaximo(Double.parseDouble(txtGeneticoMutMax.getText()) * 0.01);
            solucion.setTotalAleatorio(Integer.parseInt(txtGeneticoAleatorios.getText()));
            solucion.setRangoElitismo(Integer.parseInt(txtGeneticoRangoElitismo.getText()));            
            SolucionTSP mejor = solucion.start(Integer.parseInt(txtGeneticoPoblacion.getText()), Integer.parseInt(txtGeneticoGeneraciones.getText()));
            puntos = mejor.mejorSolucionEspecie();
            soluciones.add(mejor);
        }        
        if(btnExhaustivo.isSelected())
        {
            SolucionTSP mejor = new BusquedaExhaustivaTSP(puntos,BR).start();
            puntos = mejor.mejorSolucionEspecie();
            soluciones.add(mejor);
        }
        SolucionTSPConjunto conjunto = new SolucionTSPConjunto();
        conjunto.setSoluciones(soluciones);
        actualsolucion = conjunto;
        Date date2 =  new Date(System.currentTimeMillis());
        area.setText(GRM.resultados(puntos));       
        String totalExp = (area.getText().split("TOTAL = ").length>0) ? area.getText().split("TOTAL = ")[1] : "";
        areaResumen.setText(areaResumen.getText()+"Tiempo de Ejecucion "+getDateDiff(date1,date2,TimeUnit.SECONDS) + " Segundos " + "Resultado : " + totalExp);
        panelchart.makeChart(conjunto, (int)((double)this.getWidth() * 0.75), (int)((double)this.getHeight() * 0.75));
        panel.setPoints(puntosGRM, puntos, centros, mins, maxs); 
        setVisible(true);
        Recarga();   
    }    
    
    public void setFakePoints(MarkerDictionary md)
    {
        activarModo(new JRadioButton[]{});
        setPoints(md);
    }
    
    public void setTestPoints(MarkerDictionary md)
    {
        this.actual = md;
        FakeRectangleMethod FRM = new FakeRectangleMethod();
        ArrayList<Coordenada> puntos = FRM.start(md); 
        GoldenRectangleMethod GRM = new GoldenRectangleMethod();
        ArrayList<Coordenada> puntosGRM = new ArrayList<Coordenada>();
        puntosGRM = GRM.start(md);      
        panel.setPoints(puntosGRM, puntos, FRM.centros, FRM.mins, FRM.maxs); 
        area.setText(FRM.resultados);
        setVisible(true);
        Recarga();   
    }
   
    public void Recarga()
    {
        String resultado = (area.getText().split("TOTAL = ").length>0)?area.getText().split("TOTAL = ")[1]:"";
        panel.refrescar(btnNodos.isSelected(), btnRuta.isSelected(), btnFinal.isSelected(), btnNumeros.isSelected(), btnLineas.isSelected(), btnDiferencias.isSelected(), resultado);
    }
    
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) 
    {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
//            AlgoritmoGeneticoTSP solucion = new AlgoritmoGeneticoTSP(puntos,BR);
//            solucion.setConfigurationTSP(Integer.parseInt(txtGeneticoNumGen.getText()),
//                                         Double.parseDouble(txtGeneticoCullingPercentage.getText())*0.01,
//                                         Integer.parseInt(txtGeneticoMutationRate.getText()));
//            SolucionTSP mejor = solucion.start();
//            puntos = mejor.mejorSolucionGen();
//            soluciones.add(mejor);