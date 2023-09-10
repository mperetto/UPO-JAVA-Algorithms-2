package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upo.graph.base.WeightedGraph;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Codice di test per classe AdjListDirWeight.
 * 
 * @author Marco Peretto 20031272
 * 
 * */
class TestAdjListDirWeight {

	AdjListDirWeight graph;
	
	@BeforeEach
	void setUp() throws Exception {
		this.graph = new AdjListDirWeight();
	}

	@Test
	void testAddVertex() {
		graph.addVertex("A");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.addVertex("A");
		});
		
		assertTrue(graph.containsVertex("A"));
		assertEquals(graph.size(), 1);
	}
	
	@Test
	void testRemoveVertex() {
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.removeVertex("A");
		});
		
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		
		assertTrue(graph.containsVertex("A"));
		assertTrue(graph.containsVertex("B"));
		assertTrue(graph.containsVertex("C"));
		
		graph.removeVertex("A");
		assertFalse(graph.containsVertex("A"));
		
		graph.removeVertex("C");
		assertFalse(graph.containsVertex("C"));
		
		graph.removeVertex("B");
		assertFalse(graph.containsVertex("B"));
	}
	
	@Test
	void testAddEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.addEdge("A", "C");
		});
		
		graph.addEdge("A", "B");
		
		assertTrue(graph.containsEdge("A", "B"));
		assertFalse(graph.containsEdge("B", "A"));
		
		graph.addEdge("B", "A");
		
		assertTrue(graph.containsEdge("B", "A"));
	}
	
	@Test
	void testRemoveEdge() {
		graph.addVertex("A");
		graph.addVertex("B");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.removeEdge("A", "C");
		});
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.removeEdge("A", "B");
		});
		
		graph.addEdge("A", "B");
		assertTrue(graph.containsEdge("A", "B"));
		
		graph.removeEdge("A", "B");
		assertFalse(graph.containsEdge("A", "B"));
	}
	
	@Test
	void testContainsEdge() {
		graph.addVertex("A");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.containsEdge("A", "C");
		});
		
		graph.addVertex("B");
		
		assertFalse(graph.containsEdge("A", "B"));
		
		graph.addEdge("A", "B");
		assertTrue(graph.containsEdge("A", "B"));
		
	}
	
	@Test
	void testGetVertexIndex() {
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getVertexIndex("A");
		});
		
		graph.addVertex("A");
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getVertexIndex("B");
		});
		
		graph.addVertex("B");
		graph.addVertex("C");
		
		assertEquals(graph.getVertexIndex("A"), 0);
		assertEquals(graph.getVertexIndex("B"), 1);
		assertEquals(graph.getVertexIndex("C"), 2);
		
		graph.removeVertex("B");
		
		assertEquals(graph.getVertexIndex("A"), 0);
		assertEquals(graph.getVertexIndex("C"), 1);
	}
	
	@Test
	void testGetVertexLabel() {
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getVertexLabel(0);
		});
		
		graph.addVertex("A");
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getVertexLabel(1);
		});
		
		graph.addVertex("B");
		graph.addVertex("C");
		
		assertEquals(graph.getVertexLabel(0), "A");
		assertEquals(graph.getVertexLabel(1), "B");
		assertEquals(graph.getVertexLabel(2), "C");
		
		graph.removeVertex("B");
		
		assertEquals(graph.getVertexLabel(0), "A");
		assertEquals(graph.getVertexLabel(1), "C");
	}
	
	@Test
	void testContainsVertex() {
		
		graph.addVertex("A");
		
		assertTrue(graph.containsVertex("A"));
	}
	
	@Test
	void testGetAdjacent() {
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getAdjacent("A");
		});
		
		graph.addVertex("A");
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getAdjacent("B");
		});
		
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		
		graph.addEdge("A", "B");
		graph.addEdge("C", "B");
		graph.addEdge("D", "B");
		
		assertTrue(graph.getAdjacent("B").isEmpty());
		assertTrue(graph.getAdjacent("C").contains("B"));
		assertTrue(graph.getAdjacent("A").contains("B"));
		assertTrue(graph.getAdjacent("D").contains("B"));
	}
	
	@Test
	void testIsAdjacent() {
		graph.addVertex("A");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.isAdjacent("A", "B");
		});
		
		graph.addVertex("B");
		
		graph.addEdge("A", "B");
		
		assertTrue(graph.isAdjacent("B", "A"));
		assertFalse(graph.isAdjacent("A", "B"));
	}
	
	@Test
	void testGetEdgeWeight() {
		graph.addVertex("A");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.getEdgeWeight("A", "B");
		});
		
		graph.addVertex("B");
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.getEdgeWeight("A", "B");
		});
		
		graph.addEdge("A", "B");
		
		graph.setEdgeWeight("A", "B", 12.5);
		
		assertEquals(graph.getEdgeWeight("A", "B"), 12.5);
	}
	
	@Test
	void testSetEdgeWeight() {
		graph.addVertex("A");
		
		assertThrows(IllegalArgumentException.class, () -> {
			graph.setEdgeWeight("A", "B", 3);
		});
		
		graph.addVertex("B");
		
		assertThrows(NoSuchElementException.class, () -> {
			graph.setEdgeWeight("A", "B", 3);
		});
		
		graph.addEdge("A", "B");
		
		assertEquals(graph.getEdgeWeight("A", "B"), 0);
		
		graph.setEdgeWeight("A", "B", 12.5);
		
		assertEquals(graph.getEdgeWeight("A", "B"), 12.5);
	}
	
	@Test
	void testIsDirected() {
		assertTrue(graph.isDirected());
	}
	
	@Test
	void testGetDFSTree() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getDFSTree("A");
		});	
	}
	
	@Test
	void testGetBFSTree() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getBFSTree("A");
		});	
	}
	
	@Test
	void testGetDFSTTOTForest() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.getDFSTOTForest("A");
		});	
	}
	
	@Test
	void testIsCyclic() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		
		graph.addEdge("A", "B");
		graph.addEdge("B", "A");
		graph.addEdge("C", "A");
		
		assertTrue(graph.isCyclic());
		
		graph.removeEdge("B", "A");
		
		assertFalse(graph.isCyclic());
	}
	
	@Test
	void testIsDAG() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		
		graph.addEdge("A", "B");
		graph.addEdge("B", "A");
		graph.addEdge("C", "A");
		
		assertFalse(graph.isDAG());
		
		graph.removeEdge("B", "A");
		
		assertTrue(graph.isDAG());
	}
	
	@Test
	void testTopologicalSort() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		graph.addVertex("F");
		
		graph.addEdge("A", "B");
		graph.addEdge("C", "D");
		graph.addEdge("F", "E");
		graph.addEdge("C", "E");
		
		String[] ord = graph.topologicalSort();
		String[] correct = {"F", "C", "E", "D","A", "B"};
		
		assertTrue(Arrays.equals(ord, correct));
	}
	
	@Test
	void testConnectedComponents() {
		assertThrows(UnsupportedOperationException.class, () -> {
			graph.connectedComponents();
		});
	}
	
	@Test
	void testBellmanFordShortestPaths() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		
		graph.addEdge("A", "D");
		graph.setEdgeWeight("A", "D", 5);
		graph.addEdge("A", "E");
		graph.setEdgeWeight("A", "E", 2);
		graph.addEdge("A", "C");
		graph.setEdgeWeight("A", "C", 3);
		graph.addEdge("D", "E");
		graph.setEdgeWeight("D", "E", -6);
		graph.addEdge("D", "B");
		graph.setEdgeWeight("D", "B", -3);
		graph.addEdge("E", "C");
		graph.setEdgeWeight("E", "C", 0);
		graph.addEdge("C", "B");
		graph.setEdgeWeight("C", "B", 1);
		
		WeightedGraph bfsp = graph.getBellmanFordShortestPaths("A");
		
		assertTrue(bfsp.containsVertex("A"));
		assertTrue(bfsp.containsVertex("B"));
		assertTrue(bfsp.containsVertex("C"));
		assertTrue(bfsp.containsVertex("D"));
		assertTrue(bfsp.containsVertex("E"));
		
		assertTrue(bfsp.containsEdge("A", "D"));
		assertEquals(bfsp.getEdgeWeight("A", "D"), 5);
		assertTrue(bfsp.containsEdge("D", "E"));
		assertEquals(bfsp.getEdgeWeight("D", "E"), -1);
		assertTrue(bfsp.containsEdge("E", "C"));
		assertEquals(bfsp.getEdgeWeight("E", "C"), -1);
		assertTrue(bfsp.containsEdge("C", "B"));
		assertEquals(bfsp.getEdgeWeight("C", "B"), 0);
	}
}
