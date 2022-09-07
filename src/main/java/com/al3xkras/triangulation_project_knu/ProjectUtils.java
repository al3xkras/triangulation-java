package com.al3xkras.triangulation_project_knu;


import Jama.Matrix;
import com.al3xkras.triangulation_project_knu.commons.Point2D;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class ProjectUtils {

    public static final int DEFAULT_SCALE = 3;

    public static double round(double val){
        return round(val,DEFAULT_SCALE);
    }
    public static double round(double val, int scale){
        BigDecimal bd = BigDecimal.valueOf(val);
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double angle(double x1, double y1, double x2, double y2){
        double dot = x2*x1 + y2*y1;
        double det = x2*y1 - y2*x1;
        return Math.atan2(det,dot);
    }

    public static boolean between(double x1, double y1,
                                  double x2, double y2,
                                  double x3, double y3){
        Matrix lhs = new Matrix(new double[][]{{x1,x2},{y1,y2}});
        Matrix rhs = new Matrix(new double[]{x3,y3}, 2);
        Matrix ans;
        try {
            ans = lhs.solve(rhs);
        } catch (RuntimeException e){
            return false;
        }
        double alpha = ans.get(0, 0);
        double beta = ans.get(1,0);
        double angle = -angle(x1-x2,y1-y2,x3-x2,y3-y2);

        return (angle>0 & alpha>0 & beta>0) || (angle<0 & alpha<0 & beta<0);
    }

    public static double getAngleBetweenPoints(Point2D... points){
        double angle = angle(points[0].getX()-points[1].getX(),
                points[0].getY()-points[1].getY(),
                points[2].getX()-points[1].getX(),
                points[2].getY()-points[1].getY());
        angle=angle<0?2*Math.PI+angle:angle;
        angle=angle*180/Math.PI;
        return angle;
    }

}
