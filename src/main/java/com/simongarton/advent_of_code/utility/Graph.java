package com.simongarton.advent_of_code.utility;

import lombok.Getter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public class Graph {

    private final Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public void explain() {
        for (Node node : nodes) {
            StringBuilder line = new StringBuilder(node.getName() + " " + node.getDistance() + " [");
            for (Node n : node.getShortestPath()) {
                line.append(n.getName()).append(" ");
            }
            line = new StringBuilder(line.toString().trim() + "]");
            System.out.println(line);
        }
    }

    public void writeDotFile(String filename, String fromNodeName, String toNodeName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("digraph NodeGraph {\n");
            writer.write("    overlap = false \n");
            writer.write("    rankdir = LR\n");
            writer.write(fromNodeName + "[color=red]");
            writer.write(toNodeName + "[color=blue]");
            // careful, the graph may not have been run for from->to yet
            Node toNode = getNode(toNodeName);
            String from = fromNodeName;
            String to = "";
            Set<String> done = new HashSet<>();
            for (Node node : toNode.getShortestPath()) {
                to = node.getName();
                if (to.equalsIgnoreCase(fromNodeName)) {
                    continue;
                }
                writer.write("    \"" + from + "\" -> \"" + to + "\" [color=red, dir=none]\n");
                done.add(from + "->" + to);
                from = to;
            }
            writer.write("    \"" + to + "\" -> \"" + toNodeName + "\" [color=red, dir=none]\n");
            done.add(to + "->" + toNodeName);

            for (Node node : nodes) {
                for (Node adjacent : node.getAdjacentNodes().keySet()) {
                    if (done.contains(node.getName() + "->" + adjacent.getName())) {
                        continue;
                    }
                    if (done.contains(adjacent.getName() + "->" + node.getName())) {
                        continue;
                    }
                    writer.write("    \"" + node.getName() + "\" -> \"" + adjacent.getName() + "\" [dir=none]\n");
                    done.add(node.getName() + "->" + adjacent.getName());
                }
            }
            writer.write("}\n");
            writer.close();
            System.out.println("wrote graphviz file " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node getNode(String fromNodeName) {
        return nodes.stream().filter(n -> n.getName().equalsIgnoreCase(fromNodeName)).findFirst().orElseThrow(RuntimeException::new);
    }

    public void writeDotFile(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("digraph NodeGraph {\n");
            writer.write("    overlap = false \n");
            writer.write("    rankdir = LR\n");
            for (Node node : nodes) {
                for (Node adjacent : node.getAdjacentNodes().keySet()) {
                    writer.write("    \"" + node.getName() + "\" - \"" + adjacent.getName() + "\"\n");
                }
            }
            writer.write("}\n");
            writer.close();
            System.out.println("wrote graphviz file " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node getOrCreateNode(String name) {
        Optional<Node> optionalNode = nodes.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst();
        if (optionalNode.isPresent()) {
            return optionalNode.get();
        }
        Node node = new Node(name);
        nodes.add(node);
        return node;
    }
}
