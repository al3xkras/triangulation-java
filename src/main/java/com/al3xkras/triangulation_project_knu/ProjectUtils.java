package com.al3xkras.triangulation_project_knu;


import Jama.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public static boolean between(double x1, double y1, double x2, double y2, double x3, double y3){
        double alpha = angle(x1,y1,x2,y2);
        double theta = angle(x1,y1,x3,y3);
        boolean clockwise=true;
        if (alpha<0) {
            clockwise=false;
            alpha = 2*Math.PI+alpha;
        }
        if (theta<0) theta = 2*Math.PI+theta;
        return clockwise==(alpha*theta>0 && alpha>theta);
    }

}
