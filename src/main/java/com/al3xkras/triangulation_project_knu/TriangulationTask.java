package com.al3xkras.triangulation_project_knu;

import com.al3xkras.triangulation_project_knu.commons.Point2D;
import com.al3xkras.triangulation_project_knu.commons.Triangle2D;

import java.util.*;

import static com.al3xkras.triangulation_project_knu.ProjectUtils.*;

public class TriangulationTask {

    private static final boolean debug = true;
    private static final int debugLoopCount = 1;
    private ArrayList<Point2D> points;
    private final ArrayList<Triangle2D> triangulation = new ArrayList<>();
    private final int rectWidth;
    private final int rectHeight;
    int pointToAddIndex = 0;
    private int debugLoops = 0;
    private final Point2D[] boundaryRectangle = new Point2D[4];

    private LinkedList<Point2D> border;

    private ListIterator<Point2D> convexHullIterator(int i){
        return new ListIterator<Point2D>() {
            ListIterator<Point2D> listIterator = border.listIterator(i);
            @Override
            public boolean hasNext() {
                return listIterator.hasNext() || listIterator.hasPrevious();
            }

            @Override
            public Point2D next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if (listIterator.hasNext())
                    return listIterator.next();
                listIterator=border.listIterator();
                return listIterator.next();
            }

            @Override
            public boolean hasPrevious() {
                return listIterator.hasNext() || listIterator.hasPrevious();
            }

            @Override
            public Point2D previous() {
                if (!hasPrevious())
                    throw new NoSuchElementException();
                if (listIterator.hasPrevious())
                    return listIterator.previous();
                listIterator=border.listIterator(border.size());
                return listIterator.previous();
            }

            @Override
            public int nextIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int previousIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void remove() {
                listIterator.remove();
            }

            @Override
            public void set(Point2D point2D) {
                listIterator.set(point2D);
            }

            @Override
            public void add(Point2D point2D) {
                listIterator.add(point2D);
            }
        };
    }

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
        //init();

        border = new LinkedList<>(Arrays.asList(
                new Point2D(0,0),
                new Point2D(3,1),
                new Point2D(8,3),
                new Point2D(14,4),
                new Point2D(15,2),
                new Point2D(16,0)
        ));
        points = new ArrayList<>(Arrays.asList(
                new Point2D(3,4)));

        while ((!debug || debugLoops<debugLoopCount) && pointToAddIndex<points.size()){
            debugLoops++;

            Point2D toAdd = points.get(pointToAddIndex);
            Point2D v3 = new Point2D(toAdd.getX(),toAdd.getY()-rectHeight);
            //pointToAddIndex++;

            ListIterator<Point2D> listIterator = border.listIterator();
            Point2D first=listIterator.next();
            double lastAngle = ProjectUtils.getAngleBetweenPoints(v3,toAdd,first);

            LinkedList<Point2D> pointsToRemove = new LinkedList<>();
            LinkedList<Point2D> convexHullUpdated = new LinkedList<>();
            convexHullUpdated.addLast(first);

            log("add: "+toAdd);
            log("v3: "+v3);
            double angle=-1;
            while (listIterator.hasNext()){
                Point2D next = listIterator.next();


                angle = ProjectUtils.getAngleBetweenPoints(v3,toAdd,next);

                if (angle>180)
                    angle=angle-360;

                log("\n");
                log(lastAngle);
                log(angle);
                log(next);

                if (!listIterator.hasNext()) {
                    if (lastAngle>=0 && angle<0){
                        convexHullUpdated.addLast(toAdd);
                    }
                    convexHullUpdated.addLast(next);
                    break;
                }

                if (lastAngle>=0 && angle<0){
                    convexHullUpdated.addLast(toAdd);
                    pointsToRemove.addLast(next);
                    lastAngle=angle;
                }
                if (angle>=lastAngle){
                    convexHullUpdated.addLast(next);
                    log("added");
                    lastAngle=angle;
                } else {
                    if (angle<0){
                        pointsToRemove.addLast(convexHullUpdated.removeLast());
                        convexHullUpdated.addLast(next);
                        lastAngle=angle;
                    } else {
                        pointsToRemove.addLast(next);
                    }
                    log("removed");
                }
            }

            log("hull: "+border);
            log("points to remove: "+pointsToRemove);
            log("updated convex hull: "+convexHullUpdated);
            log("\n\n");

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
        points.add(boundaryRectangle[2]);
        points.add(boundaryRectangle[3]);
        this.points.sort(Point2D::compareTo); //O(n*log(n)), quick sort

        border = new LinkedList<>();
        border.addLast(boundaryRectangle[0]);
        border.addLast(points.get(pointToAddIndex));
        pointToAddIndex++;
        border.addLast(boundaryRectangle[1]);
    }

}
