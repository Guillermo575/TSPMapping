import DiccionarioDatos.SolucionTSP;
import DiccionarioDatos.SolucionTSPConjunto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartResult extends JPanel
{
    JFreeChart actual = null;
    
    public ChartResult() { }
    
    public void makeChart(SolucionTSPConjunto solucion, int width, int height)
    {
        this.removeAll();
        actual = createChart(solucion);
        ChartPanel chartPanel = new ChartPanel(actual);
        chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.add(chartPanel, "Center");
    }  
    
    public void makeChartImage(File archivo, int width, int height)
    {
        try
        {
            ChartUtilities.saveChartAsPNG(archivo, actual, width, height);
        }catch(Exception ex)
        {
        }
    }
    
    private JFreeChart createChart(SolucionTSPConjunto conjunto) 
    {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int conteo = 0;
        double resultadooriginal = (conjunto.getSoluciones().size() > 0) ? conjunto.getSoluciones().get(0).getBruteForce().distanciaTotal(conjunto.getSoluciones().get(0).getListaOriginal()) : 0;
        XYSeries original = new XYSeries("Original");
        if(resultadooriginal > 0)
        {
            dataset.addSeries(original);              
        }
        original.add(conteo, resultadooriginal);
        
        double minValue = -9999;
        double maxValue = resultadooriginal;
        double difValue = 0;
        
        for(int k = 0; k < conjunto.getSoluciones().size(); k++)
        {
            SolucionTSP solucion = conjunto.getSoluciones().get(k);
            XYSeries series1 = new XYSeries(k + 1 + ":" + solucion.getDescripcion());
            series1.add(conteo, dataset.getSeries(k).getY((int)dataset.getSeries(k).getItemCount() - 1));
            for(int l = 0; l < solucion.getEspeciesElegidas().size(); l++)
            {
                conteo++;
                double resultado = solucion.getEspeciesElegidas().get(l).getResultado();
                series1.add(conteo, resultado);
                original.add(conteo, resultadooriginal);
                if(resultado < minValue || minValue < 0) minValue = resultado;
                if(resultado > maxValue || maxValue == 0) maxValue = resultado;
            }   
            dataset.addSeries(series1); 
        }
        difValue = (maxValue-minValue)/4;
        if(maxValue == minValue)
        {
            difValue = maxValue/4;
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart
        (
            "Resultados",      // chart title
            "Progreso",                      // x axis label
            "Costo",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            false,                     // tooltips
            false                     // urls
        );
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for(int l = 0; l < dataset.getSeriesCount(); l++)
        {
            renderer.setSeriesLinesVisible(l, true);
            if(dataset.getSeries(l).getItemCount() > 2)
            renderer.setSeriesShapesVisible(l, false);  
        }
        plot.setRenderer(renderer);  
        
        NumberAxis rangeAxisX = (NumberAxis) plot.getDomainAxis();       
        NumberAxis rangeAxisY = (NumberAxis) plot.getRangeAxis();     
        rangeAxisX.setNumberFormatOverride(new DecimalFormat("########"));
        rangeAxisX.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxisX.setRange(0.3, conteo + 0.5);        
        rangeAxisY.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxisY.setRange(minValue - difValue, maxValue + difValue);
        
        return chart;
    }
}