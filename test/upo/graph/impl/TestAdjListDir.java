package upo.graph.impl;

import static org.junit.jupiter.api.Assertions.*;

import upo.graph.base.VisitForest.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upo.graph.base.VisitForest;

class TestAdjListDir {
	
	AdjListDir graph;
	
	@BeforeEach
	void setUp() throws Exception {
		this.graph = new AdjListDir();
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
	void testIsDirected() {
		assertTrue(graph.isDirected());
	}
	
	@Test
	void testGetDFSTree() {
		
		String[] v = {"A", "B", "C", "D"};
		
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		
		graph.addEdge("A", "D");
		graph.addEdge("B", "A");
		graph.addEdge("B", "D");
		graph.addEdge("C", "A");
		graph.addEdge("D", "B");
		
		VisitForest visit = graph.getDFSTree("C");
		
		
		for(String s : v) {
			assertEquals(Color.BLACK, visit.getColor(s));
		}		
	}
	
	@Test
	void testGetDFSTOTForest() {
		
		String[] v = {"A", "B", "C", "D"};
		
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		
		graph.addEdge("A", "D");
		graph.addEdge("B", "A");
		graph.addEdge("B", "D");
		graph.addEdge("C", "A");
		graph.addEdge("D", "B");
		
		VisitForest visit = graph.getDFSTOTForest("A");
		
		for(String s : v) {
			assertEquals(Color.BLACK, visit.getColor(s));
		}		
	}
	
	@Test
void testGetBFSTree() {
		
		String[] v = {"A", "B", "C", "D"};
		
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		
		graph.addEdge("A", "D");
		graph.addEdge("B", "A");
		graph.addEdge("B", "D");
		graph.addEdge("C", "A");
		graph.addEdge("D", "B");
		
		VisitForest visit = graph.getBFSTree("C");
		
		
		for(String s : v) {
			assertEquals(Color.BLACK, visit.getColor(s));
		}		
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
	void stronglyConnectedComponents() {
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");
		graph.addVertex("F");
		
		graph.addEdge("A", "D");
		graph.addEdge("D", "B");
		graph.addEdge("B", "C");
		graph.addEdge("C", "D");
		graph.addEdge("D", "E");
		graph.addEdge("E", "F");
		graph.addEdge("F", "E");
		
		Set<Set<String>> sCC = graph.stronglyConnectedComponents();
		Set<Set<String>> sCCCorrect = new HashSet<>();
		
		Set<String> set = new HashSet<>();
		set.add("A");
		sCCCorrect.add(set);
		
		set.clear();
		set.add("B");
		set.add("C");
		set.add("D");
		sCCCorrect.add(set);
		
		set.clear();
		set.add("E");
		set.add("F");
		sCCCorrect.add(set);
		
		assertEquals(sCC, sCCCorrect);
	}

}
