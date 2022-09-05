package com.al3xkras.triangulation_project_knu;

import com.al3xkras.triangulation_project_knu.commons.Point2D;
import com.al3xkras.triangulation_project_knu.commons.Triangle2D;

import java.util.*;

import static com.al3xkras.triangulation_project_knu.ProjectUtils.*;

public class TriangulationTask {

    private static final boolean debug = true;
    private static final int debugLoopCount = 20;
    private final ArrayList<Point2D> points;
    private final ArrayList<Triangle2D> triangulation = new ArrayList<>();
    private final int rectWidth;
    private final int rectHeight;
    int pointToAddIndex = 0;
    private int debugLoops = 0;
    private final Point2D[] boundaryRectangle = new Point2D[4];

    private LinkedList<Point2D> border;

    public TriangulationTask(List<Point2D> points, int rectWidth, int rectHeight){
        this.points = new ArrayList<>(points);
        this.points.sort(Point2D::compareTo); //O(n*log(n)), quick sort
        this.rectWidth=rectWidth;
        this.rectHeight=rectHeight;
        boundaryRectangle[0]=new Point2D(0,0);
        boundaryRectangle[1]=new Point2D(rectWidth,0);
        boundaryRectangle[2]=new Point2D(rectWidth,rectHeight);
        boundaryRectangle[3]=new Point2D(0,rectHeight);
    }

    private void log(Object o){
        if (debug){
            System.out.println(o);
        }
    }

    public ArrayList<Triangle2D> triangulate(){
        init();
        while (debugLoops<debugLoopCount && pointToAddIndex<points.size()){
            debugLoops++;
            Point2D[] iterPoints = borderPointsNext();

            Point2D toAdd = points.get(pointToAddIndex);

            if (iterPoints[1].equals(boundaryRectangle[0]) ||
                iterPoints[1].equals(boundaryRectangle[1])){
                moveForward(1);
                log("skipped");
                continue;
            }


            log(Arrays.toString(iterPoints));
            log("border: "+border);
            log("\n\n");

            if (!between(iterPoints[0].getX()-iterPoints[1].getX(),
                    iterPoints[0].getY()-iterPoints[1].getY(),
                    iterPoints[2].getX()-iterPoints[1].getX(),
                    iterPoints[2].getY()-iterPoints[1].getY(),
                    toAdd.getX()-iterPoints[1].getX(),
                    toAdd.getY()-iterPoints[1].getY())){
                log("point "+toAdd+" is outside of the angle");

                double angle1 = getAngleBetweenPoints(iterPoints[2],iterPoints[1],toAdd);
                double angle2 = getAngleBetweenPoints(toAdd,iterPoints[1],iterPoints[0]);

                log("angles: "+angle1+" "+angle2);

                Triangle2D triangle2D;
                if (angle1<=angle2){
                    triangle2D = new Triangle2D(iterPoints[1], toAdd, iterPoints[2]);
                    border.removeLast();
                    border.addLast(toAdd);
                    pointToAddIndex++;
                    border.addLast(iterPoints[2]);
                } else {
                    triangle2D = new Triangle2D(toAdd, iterPoints[1], iterPoints[0]);
                    border.removeFirst();
                    border.removeFirst();
                    border.addFirst(toAdd);
                    pointToAddIndex++;
                    border.addFirst(iterPoints[1]);
                    border.addFirst(iterPoints[0]);
                }
                log("created "+triangle2D);

                log("border after change: "+border);
                triangulation.add(triangle2D);
                convexify(toAdd);
                log("border after convexifying: "+border);
            }



            moveForward(1);
        }
        return triangulation;
    }

    private void moveForward(int positions){
        assert positions>0;
        for (int i=0;i<positions;i++){
            border.addLast(border.removeFirst());
        }
    }
    private void moveBackward(int positions){
        assert positions>0;
        for (int i=0;i<positions;i++){
            border.addFirst(border.removeLast());
        }
    }

    double getAngleBetweenPoints(Point2D... points){
        double angle = angle(points[0].getX()-points[1].getX(),
                points[0].getY()-points[1].getY(),
                points[2].getX()-points[1].getX(),
                points[2].getY()-points[1].getY());
        angle=angle<0?2*Math.PI+angle:angle;
        angle=angle*180/Math.PI;
        return angle;
    }

    public Point2D[] borderPointsNext(){
        Point2D[] retVal = new Point2D[]{
                border.removeFirst(),
                border.removeFirst(),
                border.getFirst()
        };
        border.addFirst(retVal[1]);
        border.addFirst(retVal[0]);
        return retVal;
    }

    private void convexify(Point2D toAdd){
        convexify(border.descendingIterator());
    }

    private void convexify(Iterator<Point2D> it){
        Point2D last2;
        Point2D last=null;
        Point2D current=null;
        LinkedList<Point2D> afterUpdate = new LinkedList<>();
        while (it.hasNext()) {
            last2=last;
            last=current;
            current = it.next();
            if (last==null || last2==null)
                continue;

            double angle = getAngleBetweenPoints(last2,last,current);
            log("angle: "+angle);
            if (angle<180){
                Triangle2D triangle2D = new Triangle2D(last2,last,current);
                triangulation.add(triangle2D);
                log("created: "+triangle2D);
            } else {
                afterUpdate.addLast(last);
                continue;
            }
        }
        it.forEachRemaining(afterUpdate::addLast);
        afterUpdate.addFirst(current);
        border=afterUpdate;
    }

    private void init(){
        points.add(boundaryRectangle[2]);
        points.add(boundaryRectangle[3]);

        border = new LinkedList<>();
        border.addLast(boundaryRectangle[1]);
        border.addLast(points.get(pointToAddIndex));
        pointToAddIndex++;
        border.addLast(boundaryRectangle[0]);
    }

}
