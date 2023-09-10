package upo.graph.impl;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashSet;
import java.util.LinkedList;

import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.base.WeightedGraph;

public class AdjListDirWeight implements WeightedGraph {
	
	protected LinkedList<Vertex> vertexList;
	
	public AdjListDirWeight() {
		vertexList = new LinkedList<>();
	}
	
	protected class Vertex {
		private String label;
		private int index;
		private LinkedList<Edge> edgeList;
		
		public Vertex(String label, int index) {
			this.index = index;
			this.label = label;
			this.edgeList = new LinkedList<>();
		}
		
		public void addEdge(Edge e) {
			edgeList.add(e);
		}
		
		public String getLabel() {
			return label;
		}
		
		public int getIndex() {
			return index;
		}
		
		public void setIndex(int index) {
			this.index = index;
		}
		
		public LinkedList<Edge> getEdgeList() {
			return edgeList;
		}
	}
	
	protected class Edge {
		private Vertex vertex;
		private double weight;
		
		public Edge(Vertex vertex, double weight) {
			this.vertex = vertex;
			this.weight = weight;
		}
		
		public Vertex getVertex() {
			return vertex;
		}
		
		public double getWeight() {
			return weight;
		}
		
		public void setVertex(Vertex vertex) {
			this.vertex = vertex;
		}

		public void setWeight(double weight) {
			this.weight = weight;			
		}
	}

	@Override
	public int getVertexIndex(String label) throws NoSuchElementException {
		
		for(Vertex v : vertexList) {
			if(v.getLabel().equals(label)) {
				return v.getIndex();
			}
		}
		
		throw new NoSuchElementException("Non esiste nessun vertice con la label specificata.");
	}

	@Override
	public String getVertexLabel(Integer index) throws NoSuchElementException {
		for(Vertex v : vertexList) {
			if(v.getIndex() == index) {
				return v.getLabel();
			}
		}
		
		throw new NoSuchElementException("Non esiste nessun vertice con la label specificata.");
	}

	@Override
	public int addVertex(String label) {
		
		if(this.containsVertex(label)) throw new IllegalArgumentException("Il vertice esiste gia' all'interno del grafo.");
		
		Vertex v = new Vertex(label, this.size());
		
		vertexList.add(v);
		
		return this.size() - 1;
	}

	@Override
	public boolean containsVertex(String label) {
		try {
			if (this.getVertexIndex(label) >= 0) {
				return true;
			}
		}
		catch(NoSuchElementException e) { }
		
		return false;
	}

	@Override
	public void removeVertex(String label) throws NoSuchElementException {
		
		if (!this.containsVertex(label)) throw new NoSuchElementException();
			
		Set<String> adjacent = this.getAdjacent(label);
		
		for(String adj : adjacent) {
			this.removeEdge(adj, label);
		}
		
		int vertexIndex = this.getVertexIndex(label);
		
		for (int i = 0; i < this.size(); i++) {
			Vertex v = vertexList.get(i);
			
			if(v.getIndex() > vertexIndex) {
				v.setIndex(v.getIndex() - 1);
			}
		}
		
		this.vertexList.remove(vertexIndex);		
	}

	@Override
	public void addEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException {
		
		if (!this.containsVertex(sourceVertex) || !this.containsVertex(targetVertex)) throw new IllegalArgumentException("I vertici forniti devono far parte del grafo.");
		
		if(this.containsEdge(sourceVertex, targetVertex)) throw new IllegalArgumentException("L'edge specificato esiste gia'");
		
		Vertex v = vertexList.get(this.getVertexIndex(sourceVertex));
		
		Edge e = new Edge(vertexList.get(this.getVertexIndex(targetVertex)), 0);
		
		v.edgeList.add(e);
	}

	@Override
	public boolean containsEdge(String sourceVertex, String targetVertex) throws IllegalArgumentException {
		
		if (!this.containsVertex(sourceVertex) || !this.containsVertex(targetVertex)) throw new IllegalArgumentException("I vertici forniti devono far parte del grafo.");
		
		Vertex v = vertexList.get(this.getVertexIndex(sourceVertex));
		
		for(Edge e : v.edgeList) {
			if (e.getVertex().getIndex() == this.getVertexIndex(targetVertex)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void removeEdge(String sourceVertex, String targetVertex)
			throws IllegalArgumentException, NoSuchElementException {
		
		if (!this.containsVertex(sourceVertex) || !this.containsVertex(targetVertex)) throw new IllegalArgumentException("I vertici forniti devono far parte del grafo.");
		
		if (!this.containsEdge(sourceVertex, targetVertex)) throw new NoSuchElementException("L'arco non appartiene al grafo.");
		
		Vertex v = vertexList.get(this.getVertexIndex(sourceVertex));
		LinkedList<Edge> e = v.getEdgeList();
		
		for (int i = 0; i < e.size(); i++) {
			if(e.get(i).getVertex().getIndex() == this.getVertexIndex(targetVertex)) {
				v.getEdgeList().remove(i);
				return;
			}
		}
	}

	@Override
	public Set<String> getAdjacent(String vertex) throws NoSuchElementException {
		
		if (!this.containsVertex(vertex)) throw new NoSuchElementException();
		
		Set<String> adj = new HashSet<>();
		Vertex v = vertexList.get(this.getVertexIndex(vertex));
		
		for(Edge e : v.getEdgeList()) {
			adj.add(e.getVertex().getLabel());
		}
		
		return adj;
	}

	@Override
	public boolean isAdjacent(String targetVertex, String sourceVertex) throws IllegalArgumentException {
		
		if (!this.containsVertex(sourceVertex) || !this.containsVertex(targetVertex)) throw new IllegalArgumentException("I vertici forniti devono far parte del grafo.");
		
		Vertex v = vertexList.get(this.getVertexIndex(sourceVertex));
		
		for (Edge e : v.getEdgeList()) {
			if (e.getVertex().getIndex() == this.getVertexIndex(targetVertex)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int size() {
		return vertexList.size();
	}

	@Override
	public boolean isDirected() {
		return true;
	}

	@Override
	public boolean isCyclic() {
		VisitForest visit = new VisitForest(this, VisitType.DFS);
		
		for(int i = 0; i < this.size(); i++) {
			String u = this.getVertexLabel(i);
			if(visit.getColor(u) == Color.WHITE && this.visitRicCyclic(u, visit)) return true;
		}
		
		return false;
	}
	
	private boolean visitRicCyclic(String u, VisitForest visit) {
		visit.setColor(u, Color.GRAY);
		
		for(String v : this.getAdjacent(u)) {
			if(visit.getColor(v) == Color.WHITE) {
				visit.setParent(v, u);
				if(visitRicCyclic(v, visit)) return true;
			}
			else if(visit.getColor(v) == Color.GRAY) return true;
		}
		
		visit.setColor(u, Color.BLACK);
		return false;
	}

	@Override
	public boolean isDAG() {
		return isDirected() && !isCyclic();
	}

	@Override
	public VisitForest getBFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("La visita BFS non è applicabile a questo tipo di grafo.");
	}

	@Override
	public VisitForest getDFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		
		throw new UnsupportedOperationException("La visita DFS non è applicabile a questo tipo di grafo.");
	}

	@Override
	public VisitForest getDFSTOTForest(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("La visita BFS non è applicabile a questo tipo di grafo.");
	}

	@Override
	public VisitForest getDFSTOTForest(String[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("La visita BFS non è applicabile a questo tipo di grafo.");
	}

	@Override
	public String[] topologicalSort() throws UnsupportedOperationException {
		
		if(!this.isDAG()) throw new UnsupportedOperationException("L'ordinamento topologico e' disponibile solo per DAG");
		
		String[] ord = new String[this.size()];
		AtomicInteger t = new AtomicInteger(this.size() - 1);
		VisitForest visit = new VisitForest(this, VisitType.DFS);
		
		for(int i = 0; i < this.size(); i++) {
			String u = this.getVertexLabel(i);
			if(visit.getColor(u) == Color.WHITE) this.topologicalSortRic(u, ord, t, visit);
		}
		return ord;
	}
	
	private void topologicalSortRic(String u, String[] ord, AtomicInteger t, VisitForest visit) {
		visit.setColor(u, Color.GRAY);
		
		for (String v : this.getAdjacent(u)) {
			if (visit.getColor(v) == Color.WHITE) {
				visit.setParent(v, u);
				topologicalSortRic(v, ord, t, visit);
			}
		}
		visit.setColor(u, Color.BLACK);
		ord[t.get()] = u;
		t.set(t.get() - 1);;
	}

	@Override
	public Set<Set<String>> stronglyConnectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("L'operazione non e' supportata per questo tipo di grafo.");
	}
	
	@Override
	public Set<Set<String>> connectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Le componenti connesse non sono disponibili per i grafi orientati.");
	}

	@Override
	public double getEdgeWeight(String sourceVertex, String targetVertex)
			throws IllegalArgumentException, NoSuchElementException {
		
		if (!this.containsVertex(sourceVertex) || !this.containsVertex(targetVertex)) throw new IllegalArgumentException("I vertici forniti devono far parte del grafo.");
		
		Vertex v = vertexList.get(this.getVertexIndex(sourceVertex));
		LinkedList<Edge> edgeList = v.getEdgeList();
		
		for (Edge e : edgeList) {
			if (e.getVertex().getIndex() == this.getVertexIndex(targetVertex)) {
				return e.getWeight();
			}
		}
		
		throw new NoSuchElementException("L'edge non è stato trovato all'interno del grafo.");
	}

	@Override
	public void setEdgeWeight(String sourceVertex, String targetVertex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		
		if (!this.containsVertex(sourceVertex) || !this.containsVertex(targetVertex)) throw new IllegalArgumentException("I vertici forniti devono far parte del grafo.");
		
		Vertex v = vertexList.get(this.getVertexIndex(sourceVertex));
		LinkedList<Edge> edgeList = v.getEdgeList();
		
		for (Edge e : edgeList) {
			if (e.getVertex().getIndex() == this.getVertexIndex(targetVertex)) {
				e.setWeight(weight);
				return;
			}
		}
		
		throw new NoSuchElementException("L'edge non è stato trovato all'interno del grafo.");
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		
		VisitForest visit = new VisitForest(this, VisitType.DFS_TOT);
		WeightedGraph bfspGraph = new AdjListDirWeight();
		visit.setDistance(startingVertex, 0);
		
		for(int i = 0; i < this.size() - 1; i++) {
			for(Vertex u : vertexList) {
				for(Edge e : u.getEdgeList()) {
					String v = e.getVertex().getLabel();
					Double w = e.getWeight();
					
					if (visit.getDistance(v) == null || visit.getDistance(v) > visit.getDistance(u.getLabel()) + w) {
						visit.setParent(v, u.getLabel());
						visit.setDistance(v, visit.getDistance(u.getLabel()) + w);
						
					}
				}
			}
		}
		
		for(int i = 0; i < this.size() - 1; i++) {
			for(Vertex u : vertexList) {
				for(Edge e : u.getEdgeList()) {
					String v = e.getVertex().getLabel();
					Double w = e.getWeight();
					
					if (visit.getDistance(v) == null || visit.getDistance(v) > visit.getDistance(u.getLabel()) + w) {
						throw new UnsupportedOperationException("Impossibile calcolare Bellman Ford Shortest Path, il grafo presente un ciclo negativo.");
					}
				}
			}
		}
		
		Set<String> roots = visit.getRoots();
		for(int i = 0; i < this.size(); i++) {
			Vertex v = vertexList.get(i);
			if(roots.contains(v.getLabel())) {
				bfspGraph.addVertex(v.getLabel());
			}
			else {
				if(!bfspGraph.containsVertex(v.getLabel())) {
					bfspGraph.addVertex(v.getLabel());
				}
				
				if(!bfspGraph.containsVertex(visit.getPartent(v.getLabel()))) {
					bfspGraph.addVertex(visit.getPartent(v.getLabel()));
				}
				
				bfspGraph.addEdge(visit.getPartent(v.getLabel()), v.getLabel());
				bfspGraph.setEdgeWeight(visit.getPartent(v.getLabel()), v.getLabel(), visit.getDistance(v.getLabel()));
			}
		}
		
		return bfspGraph;
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public WeightedGraph getPrimMST(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {

		throw new UnsupportedOperationException();
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {

		throw new UnsupportedOperationException();
	}
}
