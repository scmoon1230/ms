package kr.co.ucp.mntr.gis.util;


public class Parameters7 {
	public double tX;
	public double tY;
	public double tZ;
	public double tOmega;
	public double tPhi;
	public double tKappa;
	public double tScale;
	
	public Parameters7(double tX, double tY, double tZ, double tOmega, double tPhi, double tKappa, double tScale) {
		setParameters(tX, tY, tZ, tOmega, tPhi, tKappa, tScale);		
	}
	
	public void setParameters(double tX, double tY, double tZ, double tOmega, double tPhi, double tKappa, double tScale) {
	    double degrad;
	    degrad = Math.atan(1.0) / 45.0;
	   
		this.tX = tX;
		this.tY = tY;
		this.tZ = tZ;
		this.tOmega = tOmega / 3600.0 * degrad;
		this.tPhi = tPhi / 3600.0 * degrad;
		this.tKappa = tKappa / 3600.0 * degrad;
		this.tScale = tScale * 0.000001;
	}
	
	public void transform(Values3 src, Values3 dst) {
		double scale = 1.0 + tScale;
		
		dst.v1 = src.v1 + scale * (tKappa * src.v2 - tPhi * src.v3) + tX;
		dst.v2 = src.v2 + scale * (-tKappa * src.v1 + tOmega * src.v3) + tY;
		dst.v3 = src.v3 + scale * (tPhi * src.v1 - tOmega * src.v2) + tZ;
	}

	public void reverseTransfom(Values3 src, Values3 dst) {
		double xt, yt, zt;

		xt = (src.v1 - tX) * (1.0 + tScale);
		yt = (src.v2 - tY) * (1.0 + tScale);
		zt = (src.v3 - tZ) * (1.0 + tScale);
		   
		dst.v1 = 1.0 / (1 + tScale) * (xt - tKappa * yt + tPhi * zt);
		dst.v2 = 1.0 / (1 + tScale) * (tKappa * xt + yt - tOmega * zt);
		dst.v3 = 1.0 / (1 + tScale) * (-tPhi * xt + tOmega * yt + zt);
	}

}
