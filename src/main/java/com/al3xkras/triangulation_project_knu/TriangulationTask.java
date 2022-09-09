package com.al3xkras.triangulation_project_knu;

import com.al3xkras.triangulation_project_knu.commons.Point2D;
import com.al3xkras.triangulation_project_knu.commons.Triangle2D;

import java.util.*;

import static com.al3xkras.triangulation_project_knu.ProjectUtils.*;

public class TriangulationTask {

    boolean debug = false;
    private static final int debugLoopCount = 10;
    private ArrayList<Point2D> points;
    private final ArrayList<Triangle2D> triangulation = new ArrayList<>();
    private final int rectWidth;
    private final int rectHeight;
    int pointToAddIndex = 0;
    private int debugLoops = 0;
    private final Point2D[] boundaryRectangle = new Point2D[4];

    private LinkedList<Point2D> border;

    public TriangulationTask(List<Point2D> points, int rectWidth, int rectHeight){
        this.points = new ArrayList<>(points);
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

        Point2D p1 = points.get(1);

        double a0 = getAngleBetweenPoints(p1,points.get(0),border.getFirst());
        double a1 = getAngleBetweenPoints(border.getLast(),points.get(0),p1);
        if (a0<=180 || a1<=180){
            if (a0>a1){
                triangulation.add(new Triangle2D(border.getFirst(),p1,points.get(0)));
            } else {
                triangulation.add(new Triangle2D(points.get(0),p1,border.getLast()));
            }
        }

        System.out.println(a0);
        System.out.println(a1);

        while (pointToAddIndex<points.size()){

            Point2D toAdd = points.get(pointToAddIndex);
            Point2D v3 = new Point2D(toAdd.getX(),toAdd.getY()-rectHeight);


            //pointToAddIndex++;

            ListIterator<Point2D> listIterator = border.listIterator();
            Point2D first=listIterator.next();
            double lastAngle = ProjectUtils.getAngleBetweenPoints(v3,toAdd,first);
            if (lastAngle>180)
                lastAngle=lastAngle-360;
            LinkedList<Point2D> convexHullUpdated = new LinkedList<>();
            convexHullUpdated.addLast(first);

            log("add: "+toAdd);
            log("v3: "+v3);
            double angle;
            while (listIterator.hasNext()){
                Point2D next = listIterator.next();


                angle = ProjectUtils.getAngleBetweenPoints(v3,toAdd,next);

                if (angle>180)
                    angle=angle-360;

                log("\n");
                log(lastAngle);
                log(angle);
                log(next);

                if (lastAngle>=0 && angle<0){
                    convexHullUpdated.addLast(toAdd);
                    lastAngle=angle;
                }
                if (angle>=lastAngle){
                    convexHullUpdated.addLast(next);
                    log("added");
                    lastAngle=angle;
                } else {
                    if (angle<0){
                        convexHullUpdated.removeLast();
                        convexHullUpdated.addLast(next);
                        lastAngle=angle;
                    }
                    log("removed");
                }
            }

            log("hull: "+border);
            log("updated convex hull: "+convexHullUpdated);
            log("\n\n");


            ListIterator<Point2D> iter = border.listIterator();
            HashSet<Point2D> notRemoved = new HashSet<>(convexHullUpdated);
            while (iter.hasNext()){
                Point2D p = iter.next();
                if (!iter.hasNext())
                    break;
                Point2D p2 = iter.next();
                iter.previous();

                if (!notRemoved.contains(p) || !notRemoved.contains(p2) ||
                        (!(pointToAddIndex==1) && getAngleBetweenPoints(toAdd,p,p2)<180)){
                    try {
                        triangulation.add(new Triangle2D(p,toAdd,p2));
                    } catch (AssertionError ignored){}
                }
            }

            pointToAddIndex++;
            border=convexHullUpdated;
        }

        log(border);

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



    private void init(){
        this.points.add(boundaryRectangle[3]);
        this.points.add(boundaryRectangle[2]);
        this.points.sort(Point2D::compareTo); //O(n*log(n)), quick sort
        triangulation.add(new Triangle2D(boundaryRectangle[0],points.get(pointToAddIndex),boundaryRectangle[1]));

        border = new LinkedList<>();
        border.addLast(boundaryRectangle[0]);
        border.addLast(points.get(pointToAddIndex));
        pointToAddIndex++;
        border.addLast(boundaryRectangle[1]);
    }

}
