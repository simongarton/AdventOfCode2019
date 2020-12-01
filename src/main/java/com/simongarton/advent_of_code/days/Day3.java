package com.simongarton.advent_of_code.days;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day3 {

    int x;
    int y;
    List<String> wire1;
    List<String> wire2;
    List<Coord> path1 = new ArrayList<>();
    List<Coord> path2 = new ArrayList<>();

    public void run() {
        try {
            System.out.println("Day 3: Crossed Wires");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String filename = "day3.txt";
            File file = new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile());
            List<String> wires = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            wire1 = Arrays.asList(wires.get(0).split(","));
            wire2 = Arrays.asList(wires.get(1).split(","));
            System.out.println(wire1.size() + " points in wire1.");
            System.out.println(wire2.size() + " points in wire2.");
            mapWires();
            findIntersections();
            wire1 = Arrays.asList(wires.get(0).split(","));
            wire2 = Arrays.asList(wires.get(1).split(","));
            mapWiresWithDistance();
            findIntersectionsWithDistance();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private void findIntersectionsWithDistance() {
        Map<String, Coord> points1 = new HashMap<>();
        Map<String, Coord> points2 = new HashMap<>();
        for (Coord c : path1) {
            if (!points1.containsKey(c.asCoord())) {
                points1.put(c.asCoord(), c);
            }
        }
        for (Coord c : path2) {
            if (!points2.containsKey(c.asCoord())) {
                points2.put(c.asCoord(), c);
            }
        }
        Coord origin = new Coord(0, 0);
        int best = 0;
        Coord bestCoord = origin;
        for (Map.Entry<String, Coord> entry : points1.entrySet()) {
            if (!points2.containsKey(entry.getKey())) {
                continue;
            }
            Coord point1 = points1.get(entry.getKey());
            Coord point2 = points2.get(entry.getKey());
            int distance = point1.distance + point2.distance;
            if (distance > 0) {
                System.out.println(point1.asCoord() + " = " + distance);
                if ((best == 0) || (distance < best)) {
                    best = distance;
                    bestCoord = point1;
                }
            }
        }
        System.out.println("best = " + best + " at " + bestCoord.asCoord());
    }

    private void findIntersections() {
        Map<String, Coord> points1 = new HashMap<>();
        Map<String, Coord> points2 = new HashMap<>();
        path1.forEach(c -> points1.put(c.asCoord(), c));
        path2.forEach(c -> points2.put(c.asCoord(), c));
        Coord origin = new Coord(0, 0);
        int best = 0;
        Coord bestCoord = origin;
        for (Map.Entry<String, Coord> entry : points1.entrySet()) {
            if (!points2.containsKey(entry.getKey())) {
                continue;
            }
            Coord point = points1.get(entry.getKey());
            int distance = manhattanDistance(point, origin);
            if (distance > 0) {
                System.out.println(point.asCoord() + " = " + distance);
                if ((best == 0) || (distance < best)) {
                    best = distance;
                    bestCoord = point;
                }
            }
        }
        System.out.println("best = " + best + " at " + bestCoord.asCoord());
    }

    private int manhattanDistance(Coord point, Coord origin) {
        return Math.abs(point.x - origin.x) + Math.abs(point.y - origin.y);
    }

    private void mapWires() {
        mapWire(wire1, path1);
        mapWire(wire2, path2);
    }

    private void mapWiresWithDistance() {
        mapWireWithDistance(wire1, path1);
        mapWireWithDistance(wire2, path2);
    }

    private void mapWireWithDistance(List<String> wire, List<Coord> path) {
        x = 0;
        y = 0;
        path.clear();
        int travelledDistance = 0;
        path.add(new Coord(x, y));
        for (String v : wire) {
            travelledDistance = mapTurnWithDistance(v, path, travelledDistance);
        }
    }

    private void mapWire(List<String> wire, List<Coord> path) {
        x = 0;
        y = 0;
        path.clear();
        path.add(new Coord(x, y));
        wire.forEach(v -> mapTurn(v, path));
    }

    private int mapTurnWithDistance(String turn, List<Coord> path, int travelledDistance) {
        String cardinal = turn.substring(0, 1);
        int distance = Integer.parseInt(turn.substring(1));
        switch (cardinal) {
            case "U":
                for (int i = 0; i < distance; i++) {
                    y = y + 1;
                    travelledDistance = travelledDistance + 1;
                    path.add(new Coord(x, y, travelledDistance));
                }
                break;
            case "D":
                for (int i = 0; i < distance; i++) {
                    y = y - 1;
                    travelledDistance = travelledDistance + 1;
                    path.add(new Coord(x, y, travelledDistance));
                }
                break;
            case "R":
                for (int i = 0; i < distance; i++) {
                    x = x + 1;
                    travelledDistance = travelledDistance + 1;
                    path.add(new Coord(x, y, travelledDistance));
                }
                break;
            case "L":
                for (int i = 0; i < distance; i++) {
                    x = x - 1;
                    travelledDistance = travelledDistance + 1;
                    path.add(new Coord(x, y, travelledDistance));
                }
                break;
            default:
                throw new RuntimeException("Bad direction " + cardinal);
        }
        ;
        return travelledDistance;
    }

    private void mapTurn(String turn, List<Coord> path) {
        String cardinal = turn.substring(0, 1);
        int distance = Integer.parseInt(turn.substring(1));
        switch (cardinal) {
            case "U":
                for (int i = 0; i < distance; i++) {
                    y = y + 1;
                    path.add(new Coord(x, y));
                }
                break;
            case "D":
                for (int i = 0; i < distance; i++) {
                    y = y - 1;
                    path.add(new Coord(x, y));
                }
                break;
            case "R":
                for (int i = 0; i < distance; i++) {
                    x = x + 1;
                    path.add(new Coord(x, y));
                }
                break;
            case "L":
                for (int i = 0; i < distance; i++) {
                    x = x - 1;
                    path.add(new Coord(x, y));
                }
                break;
            default:
                throw new RuntimeException("Bad direction " + cardinal);
        }
        path.add(new Coord(x, y));
    }

    private static class Coord {
        public int x;
        public int y;
        public int distance = 0;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coord(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        public String asCoord() {
            return x + "," + y;
        }
    }
}
