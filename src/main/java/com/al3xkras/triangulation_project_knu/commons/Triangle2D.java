package com.al3xkras.triangulation_project_knu.commons;
import com.al3xkras.triangulation_project_knu.ProjectUtils;

import java.util.*;

public class Triangle2D {
    private final ArrayList<Point2D> points;
    public Triangle2D(Point2D... points){
        assert points.length==3;
        this.points=new ArrayList<>(Arrays.asList(points));
        assert isValid();
    }

    private boolean isValid(){
        double angle = ProjectUtils.angle(points.get(0).getX()-points.get(1).getX(),
                points.get(0).getY()-points.get(1).getY(),
                points.get(0).getX()-points.get(2).getX(),
                points.get(0).getY()-points.get(2).getY());
        return angle!=0 && angle!=Math.PI;
    }

    public List<Point2D> getPoints() {
        return Collections.unmodifiableList(points);
    }

    @Override
    public String toString() {
        return "Triangle2D{" +
                "points=" + points +
                '}';
    }
}
