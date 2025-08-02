package kr.co.ucp.mntr.gis.util;

public class Values3 {
	public Values3() {
		v1 = 0.0;
		v2 = 0.0;
		v3 = 0.0;
	}
	
	public Values3(Values3 v) {
		v1 = v.v1;
		v2 = v.v2;
		v3 = v.v3;
	}
	
	public Values3(double v1, double v2, double v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
	public String toString() {
		return "(" + v1 + ", " + v2 + ", " + v3 + ")";
	}
	
	public double v1;
	public double v2;
	public double v3;
}
