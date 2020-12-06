package com.simongarton.advent_of_code.days;

import com.simongarton.advent_of_code.utility.Djikstra;
import com.simongarton.advent_of_code.utility.Graph;
import com.simongarton.advent_of_code.utility.Node;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day6 {

    public static final String SOLAR_SYSTEM_FILENAME = "solar-system.dot";
    public static final String GRAPH_FILENAME = "solar-system-graph.dot";
    private final List<Body> bodies = new ArrayList<>();

    public void run() {
        try {
            System.out.println("Day 6: Universal Orbit Map\n");
            File file = Paths.get("data", "day6.txt").toFile();
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            System.out.println(totalOrbits(lines) + " total orbits.");
            System.out.println((shortestPath(lines, "YOU", "SAN") - 2) + " shortest path.");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    private Integer shortestPath(List<String> lines, String from, String to) {
        Graph graph = buildGraph(lines);
        Djikstra djikstra = new Djikstra(graph);
        djikstra.calculateShortestPathFromSource(graph.getNode(from));
        graph.explain();
        graph.writeDotFile(GRAPH_FILENAME,from,to);
        return graph.getNode(to).getDistance();
    }

    private Graph buildGraph(List<String> lines) {
        Graph graph = new Graph();
        for (String line : lines) {
            String[] names = line.split("\\)");
            Node a = graph.getOrCreateNode(names[0]);
            Node b = graph.getOrCreateNode(names[1]);
            a.addDestination(b, 1);
            b.addDestination(a, 1);
        }
        return graph;
    }

    private long totalOrbits(List<String> lines) {
        for (String line : lines) {
            String[] names = line.split("\\)");
            Body left = build(names[0]);
            Body right = build(names[1]);
            orbit(left, right);
        }
        long totalOrbits = calculateOrbits();
        buildDotFile();
        return totalOrbits;
    }

    private long calculateOrbits() {
        /*

        Interesting. But not as interesting as I first thought.

        Build up the orbits, so each has parent and children.
        Then loop over all the bodies. For each keep walking up the tree, adding one until I reach a parent
        that has no parent.
        Don't double count - the first one is direct, and so isn't included in the indirect count.

         */

        for (Body body : bodies) {
            walkUpTree(body);
        }
        return bodies.stream().mapToLong(b -> b.indirectCount + b.directCount).sum();
    }

    private void walkUpTree(Body child) {
        int indirectCount = 0;
        Body parent = child.orbits;
        while (parent != null) {
            indirectCount = indirectCount + 1;
            parent = parent.orbits;
        }
        child.setDirectCount(indirectCount > 0 ? 1 : 0);
        child.setIndirectCount(indirectCount > 0 ? indirectCount - 1 : 0);
    }

    private void buildDotFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SOLAR_SYSTEM_FILENAME));
            writer.write("digraph SolarSystem {\n");
            writer.write("    overlap = false \n");
            writer.write("    rankdir = RL\n");
            for (Body b : bodies) {
                if (b.orbits != null) {
                    writer.write("    \"" + b.toString() + "\" -> \"" + b.orbits.toString() + "\"\n");
                }
            }
            writer.write("}\n");
            writer.close();
            System.out.println("wrote graphviz file " + SOLAR_SYSTEM_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void orbit(Body left, Body right) {
        // right orbits left
        if (right.orbits == left) {
            return;
        }
        right.orbits = left;
        left.orbittedBy.add(right);
    }

    private Body build(String name) {
        Optional<Body> optionalBody = bodies.stream().filter(b -> b.name.equalsIgnoreCase(name)).findFirst();
        if (optionalBody.isPresent()) {
            return optionalBody.get();
        }
        Body body = new Body(name);
        bodies.add(body);
        return body;
    }

    @Data
    public static class Body {
        private String name;
        private Body orbits;
        int directCount;
        int indirectCount;
        private List<Body> orbittedBy = new ArrayList<>();

        public Body(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            // necessary as the @Data annotation means I get a generated toString(),
            // which refers to orbits and orbittedBy, and different bodies reference each other,
            // so I get circular references.
            if (this.orbits == null) {
                return this.name;
            }
            return this.name + " (" + directCount + indirectCount + ")";
        }
    }
}
