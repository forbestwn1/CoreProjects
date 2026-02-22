package interview;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ForInterview {

	
	public static void main(String[] args) {

		
		
	}
	
	public static List<List<Point2D.Double>> generateIntermediateCurves(
            List<Point2D.Double> upperCurve,
            List<Point2D.Double> lowerCurve,
            int numberOfIntermediateCurves) {

		List<List<Point2D.Double>> out = new ArrayList<List<Point2D.Double>>();
		
		for(int j=0; j<numberOfIntermediateCurves; j++) {
			out.add(new ArrayList<Point2D.Double>(upperCurve.size()));
		}
		
		for(int i=0; i<upperCurve.size(); i++) {
			Point2D.Double upperEle = upperCurve.get(i);
			Point2D.Double lowerEle = lowerCurve.get(i);
			
			double step = (upperEle.getY()-lowerEle.getY())/(numberOfIntermediateCurves+1);
			
			double y = lowerEle.getY();
            for(int j=0; j<numberOfIntermediateCurves; j++) {
				y = y + step;
				Point2D.Double point = new Point2D.Double(lowerEle.getX(), y);
				out.get(j).add(point);
			}
		}
		
        return out;		
	}
}
