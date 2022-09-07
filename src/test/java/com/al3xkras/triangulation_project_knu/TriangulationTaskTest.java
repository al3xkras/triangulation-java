package com.al3xkras.triangulation_project_knu;

import com.al3xkras.triangulation_project_knu.commons.Point2D;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TriangulationTaskTest {

    @Test
    void triangulate1() {
        assertTimeoutPreemptively(Duration.ofSeconds(2),()->{
            TriangulationTask triangulationTask = new TriangulationTask(Arrays.asList(
                    new Point2D(2,2),
                    new Point2D(7,1),
                    new Point2D(5,6),
                    new Point2D(8,6),
                    new Point2D(9,15)
            ),20,20);

            triangulationTask.triangulate().forEach(System.out::println);
        });
    }

    @Test
    void triangulate2() {
        assertTimeoutPreemptively(Duration.ofSeconds(2),()->{
            TriangulationTask triangulationTask = new TriangulationTask(Arrays.asList(
                    new Point2D(9,2),
                    new Point2D(7,1),
                    new Point2D(6,6),
                    new Point2D(7,7),
                    new Point2D(9,14),
                    new Point2D(6,2),
                    new Point2D(9,3),
                    new Point2D(10,5)
            ),20,20);

            triangulationTask.triangulate().forEach(System.out::println);
        });
    }

}