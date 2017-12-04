package edu.sjsu.cloudbread.response;

import java.util.List;

public class GraphPlotResponse extends GenericResponse {

	private static final long serialVersionUID = 1L;
	private List series1;
	private List series2;
	private List series3;
	private List series4;
	

	public GraphPlotResponse() {
	}

	

	public GraphPlotResponse(List series1, List series2, List series3, List series4) {
		super();
		this.series1 = series1;
		this.series2 = series2;
		this.series3 = series3;
		this.series4 = series4;
	}

	public List getSeries1() {
		return series1;
	}

	public void setSeries1(List series1) {
		this.series1 = series1;
	}

	public List getSeries2() {
		return series2;
	}

	public void setSeries2(List series2) {
		this.series2 = series2;
	}

	public List getSeries4() {
		return series4;
	}

	public void setSeries4(List series4) {
		this.series4 = series4;
	}

	public List getSeries3() {
		return series3;
	}

	public void setSeries3(List series3) {
		this.series3 = series3;
	}



	@Override
	public String toString() {
		return "GraphPlotResponse [series1=" + series1 + ", series2=" + series2 + ", series3=" + series3 + ", series4="
				+ series4 + "]";
	}
	
	

}

