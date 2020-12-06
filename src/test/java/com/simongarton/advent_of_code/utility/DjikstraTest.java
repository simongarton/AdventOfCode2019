package com.simongarton.advent_of_code.utility;

import org.junit.jupiter.api.Test;

class DjikstraTest {

    public static final String GRAPH_FILENAME = "graph.dot";

    @Test
    void fullTest() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        Djikstra djikstra = new Djikstra(graph);
        graph = djikstra.calculateShortestPathFromSource(nodeA);
        graph.explain();
        graph.writeDotFile(GRAPH_FILENAME,"A","F");
        System.out.println("done");
    }

}