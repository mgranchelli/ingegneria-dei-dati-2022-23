package statistics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class Charts extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	// Pie chart - type cells distribution
	public Charts(String applicationTitle, String chartTitle, HashMap<String, Integer> map) {
		super(applicationTitle);
		JFreeChart pieChart = ChartFactory.createPieChart(
				chartTitle,
				createPieDataset(map), 
				true,
				true, false);
		
		saveChart(pieChart, chartTitle);

//		showChart(pieChart);
	}
	
	// Bar chart - avg rows/columns
	public Charts(String applicationTitle, String chartTitle, int rows, int columns) {
		super(applicationTitle);
		JFreeChart barChart = ChartFactory.createBarChart(
				chartTitle, 
				"", 
				"# Avg", 
				createRowColumDataset(rows, columns),
				PlotOrientation.VERTICAL, 
				true, true, false);
		
		saveChart(barChart, chartTitle);

//		showChart(barChart);
	}
	
	// Bar chart - rows/columns distribution
	public Charts(String applicationTitle, String chartTitle, HashMap<Integer, Integer> map, String type) {
		super(applicationTitle);
		JFreeChart barChart = ChartFactory.createBarChart(
				chartTitle, 
				type, 
				"# Values", 
				createBarChartDataset(map, type),
				PlotOrientation.VERTICAL, 
				false, true, false);
		
		saveChart(barChart, chartTitle);
		
//		showChart(barChart);
	}
	
	
	private CategoryDataset createRowColumDataset(int rows, int columns) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(rows, "Rows", "");
		dataset.addValue(columns, "Columns", "");
		return dataset;
	}

	private CategoryDataset createBarChartDataset(HashMap<Integer, Integer> map, String type) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Map<Integer, Integer> sortedMap = SortMapByValue.sortByValue(map, false);
		int count = 0;
        for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
            if(count > 10) {
                break;
            }
            dataset.addValue(entry.getValue(), type, Integer.toString(entry.getKey()));
            count++;
        }
        
		return dataset;
	}
	
	private PieDataset<String> createPieDataset(HashMap<String, Integer> map) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
		for (String i: map.keySet()) {
			dataset.setValue(i, map.get(i));
		}
		return dataset;
	}
	
	// Show chart
	@SuppressWarnings("unused")
	private void showChart(JFreeChart chart) {
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);
	}
	
	// Save png with chart
	private void saveChart(JFreeChart chart, String chartTitle) {
		int width = 640;
		int height = 480;
		File file = new File("./outputCharts/" + chartTitle + ".png"); 
	    try {
			ChartUtils.saveChartAsPNG(file, chart, width, height);
			System.out.println("Saved in ./outputCharts/" + chartTitle + ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
