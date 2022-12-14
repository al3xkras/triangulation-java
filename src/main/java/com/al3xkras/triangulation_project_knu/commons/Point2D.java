package com.al3xkras.triangulation_project_knu.commons;

import java.util.Objects;

public class Point2D implements Comparable<Point2D>{
    private double x;
    private double y;

    private static final double eps = 1e-3;

    public Point2D(double... doubles){
        this.x=doubles[0];
        this.y=doubles[1];
    }
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    @Override
    public int compareTo(Point2D o) {
        int d =  Double.compare(this.y, o.getY());
        return d==0?Double.compare(this.getX(),o.getX()):d;
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2D point2D = (Point2D) o;
        return Double.compare(point2D.x, x) == 0 && Double.compare(point2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
