package com.al3xkras.triangulation_project_knu;

import com.al3xkras.triangulation_project_knu.commons.Point2D;
import com.al3xkras.triangulation_project_knu.commons.Triangle2D;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        String filename = args[2];
        String outputFile = args[3];
        String delimiter=" ";
        String outputFormat = String.join(delimiter,"%.3f","%.3f","%.3f");

        ArrayList<Point2D> points = new ArrayList<>(256);
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                points.add(new Point2D(Arrays.stream(line.split(delimiter,3)).mapToDouble(Double::parseDouble).toArray()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        TriangulationTask triangulationTask = new TriangulationTask(points,width,height);
        ArrayList<Triangle2D> triangulation = triangulationTask.triangulate();

        PrintWriter outputWriter = new PrintWriter(new FileWriter(outputFile));

        triangulation.forEach(t-> outputWriter.println(String.format(outputFormat,t.getPoints().get(0),
                t.getPoints().get(1), t.getPoints().get(2))));
        outputWriter.close();
        System.out.println("executed successfully");
    }
}
