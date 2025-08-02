
package kr.co.ucp.mntr.gis.util;

public class Ellip2Ellipsoid {
	private static double degrad = Math.atan(1.0) / 45.0;
	private final Ellipsoid srcEllipse;
	private final Ellipsoid dstEllipse;
	private final Parameters7 params;
	
	public Ellip2Ellipsoid(Ellipsoid srcEllipse, Ellipsoid dstEllipse, Parameters7 params) {
		this.srcEllipse = srcEllipse;
		this.dstEllipse = dstEllipse;
		this.params = params;
	}
	
	public void transfom(Values3 src, Values3 dst) {
		Values3 in = new Values3(src);

		LatLong2Geocentric(in, dst, srcEllipse);
		params.transform(dst, in);
		Geocentric2LatLong(in, dst, dstEllipse);
	}
	
	public void reverseTransform(Values3 src, Values3 dst) {
		Values3 in = new Values3(src);

		LatLong2Geocentric(in, dst, dstEllipse);
		params.reverseTransfom(dst, in);
		Geocentric2LatLong(in, dst, srcEllipse);
	}
	
	private void LatLong2Geocentric(Values3 src, Values3 dst, Ellipsoid ellipse) {
		double sphi, slam, recf, b, es, n, f;
		
 	   	f = ellipse.f;
		sphi = src.v1 * degrad;
		slam = src.v2 * degrad;
		recf = 1.0 / f;
		b = ellipse.a * (recf - 1.0) / recf;
		es = (ellipse.a*ellipse.a - b*b) / (ellipse.a*ellipse.a);
		
 	    n = ellipse.a / Math.sqrt(1.0 - es * Math.sin(sphi)*Math.sin(sphi));
		   
	    dst.v1 = (n + src.v3) * Math.cos(sphi) * Math.cos(slam);
		dst.v2 = (n + src.v3) * Math.cos(sphi) * Math.sin(slam);
		dst.v3 = (((b*b) / (ellipse.a*ellipse.a)) * n + src.v3) * Math.sin(sphi);
	}
	
	private void Geocentric2LatLong(Values3 src, Values3 dst, Ellipsoid ellipse) {
		double sphiold, sphinew, slam, recf, b, es, n, p, t1, f;
		int icount;
		
		f = ellipse.f;
		recf = 1.0 / f;
		b = ellipse.a * (recf - 1.0) / recf;
		es = (ellipse.a*ellipse.a - b*b) / (ellipse.a*ellipse.a);

        slam = Math.atan(src.v2 / src.v1);
        p = Math.sqrt(src.v1*src.v1 + src.v2*src.v2);
		   
        n = ellipse.a;
        dst.v3 = 0.0;
		sphiold = 0.0;
		icount = 0;
		do {
		      icount++;
		      t1 = Math.pow(((b*b) / (ellipse.a*ellipse.a) * n + dst.v3), 2.0) - (src.v3*src.v3);
		      t1 = src.v3 / Math.sqrt(t1);
		      sphinew = Math.atan(t1);

		      if(Math.abs(sphinew - sphiold) < 1E-18) break;
		      
		      n = ellipse.a / Math.sqrt(1.0 - es * Math.pow(Math.sin(sphinew), 2.0));
		      dst.v3 = p / Math.cos(sphinew) - n;
		      sphiold = sphinew;
		} while(icount < 300);
		
		dst.v1 = sphinew / degrad;
		dst.v2 = slam / degrad;
		
		if(src.v1 < 0.0) dst.v2 = 180.0 + dst.v2;
		if(dst.v2 < 0.0) dst.v2 = 360.0 + dst.v2;
	}
}
