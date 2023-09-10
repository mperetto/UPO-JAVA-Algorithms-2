package upo.graph.impl;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.LinkedList;

import upo.graph.base.Graph;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import java.util.HashSet;

public class AdjListDir extends AdjListDirWeight implements Graph {
	
	public AdjListDir() {
		super();
	}
	
	@Override
	public VisitForest getBFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		
		VisitForest visit = new VisitForest(this, VisitType.BFS);
		int time = 0;
		
		LinkedList<String> f = new LinkedList<>();
		visit.setColor(startingVertex, Color.GRAY);
		visit.setStartTime(startingVertex, time++);
		f.add(startingVertex);
		
		while(!f.isEmpty()) {
			String u = f.getFirst();
			Set<String> adj = this.getAdjacent(u);
			String v = "";
			for(String ver : adj) {
				if(visit.getColor(ver) == Color.WHITE) {
					v = ver;
					break;
				}
			}
			
			if (!v.equals("")) {
				visit.setColor(v, Color.GRAY);
				visit.setStartTime(v, time++);
				visit.setParent(v, u);
				f.add(v);
			}
			else {
				visit.setColor(u, Color.BLACK);
				visit.setEndTime(u, time++);
				f.removeFirst();
			}
		}
		
		return visit;
	}

	@Override
	public VisitForest getDFSTree(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest visit = new VisitForest(this, VisitType.DFS);
		int time = 0;
		
		LinkedList<String> f = new LinkedList<>();
		visit.setColor(startingVertex, Color.GRAY);
		visit.setStartTime(startingVertex, time++);
		f.add(startingVertex);
		
		while(!f.isEmpty()) {
			String u = f.getLast();
			Set<String> adj = this.getAdjacent(u);
			String v = "";
			for(String ver : adj) {
				if(visit.getColor(ver) == Color.WHITE) {
					v = ver;
					break;
				}
			}
			
			if (!v.equals("")) {
				visit.setColor(v, Color.GRAY);
				visit.setStartTime(v, time++);
				visit.setParent(v, u);
				f.add(v);
			}
			else {
				visit.setColor(u, Color.BLACK);
				visit.setEndTime(u, time++);
				f.removeLast();
			}
		}
		
		return visit;
	}

	@Override
	public VisitForest getDFSTOTForest(String startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		
		VisitForest visitTot = new VisitForest(this, VisitType.DFS_TOT);
		
		for (Vertex v : vertexList) {
			if (visitTot.getColor(v.getLabel()) == Color.WHITE) {
				VisitForest visit = this.getDFSTree(v.getLabel());
				visitTot = this.mergeVisit(visitTot, visit);
			}
		}
		
		return visitTot;
	}
	
	private VisitForest mergeVisit(VisitForest v1, VisitForest v2) {
		for (Vertex vVisited : vertexList) {
			try {
				v1.setColor(vVisited.getLabel(), v2.getColor(vVisited.getLabel()));
				v1.setDistance(vVisited.getLabel(), v2.getDistance(vVisited.getLabel()));
				v1.setStartTime(vVisited.getLabel(), v2.getStartTime(vVisited.getLabel()));
				v1.setEndTime(vVisited.getLabel(), v2.getEndTime(vVisited.getLabel()));
				v1.setParent(vVisited.getLabel(), v2.getPartent(vVisited.getLabel()));
			}
			catch(Exception e) { }
		}
			
			return v1;
	}

	@Override
	public VisitForest getDFSTOTForest(String[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		
		VisitForest visitTot = new VisitForest(this, VisitType.DFS_TOT);
	    for (String vertexLabel : vertexOrdering) {
	        
	        if (visitTot.getColor(vertexLabel) == Color.WHITE) {
	            VisitForest visit = this.getDFSTree(vertexLabel);
	            visitTot = this.mergeVisit(visitTot, visit);
	        }
	    }
	    
	    return visitTot;
	}
	
	public AdjListDir getTransposedGraph() {
		AdjListDir gT = new AdjListDir();
		
		for (Vertex v : vertexList) {
			if (!gT.containsVertex(v.getLabel())) {
				gT.addVertex(v.getLabel());
			}
			for (Edge e : v.getEdgeList()) {
				if (!gT.containsVertex(e.getVertex().getLabel())) {
					gT.addVertex(e.getVertex().getLabel());
				}
				
				gT.addEdge(e.getVertex().getLabel(), v.getLabel());
			}
		}
		
		return gT;
	}

	@Override
	public Set<Set<String>> stronglyConnectedComponents() throws UnsupportedOperationException {
		
		AdjListDir gT = this.getTransposedGraph();
		
		Set<Set<String>> stronglyConnectedComponents = new HashSet<>();
		
		for (Vertex v : vertexList) {
			
			String[] vertices = new String[this.size()];
			String[] verticesGT = new String[this.size()];
			
			calcCFCRic(this, v.getLabel(), vertices, new AtomicInteger(0), new VisitForest(this, VisitType.DFS_TOT));
			calcCFCRic(gT, v.getLabel(), verticesGT, new AtomicInteger(0), new VisitForest(gT, VisitType.DFS_TOT));
			
			Set<String> component = new HashSet<>();
			for (String value : vertices) {
	            for (String s : verticesGT) {
	                if (value != null && value.equals(s)) {
	                	component.add(value);
	                    break;
	                }
	            }
	        }
			
			if(component.size() > 0) {
				stronglyConnectedComponents.add(component);
			}
		}
		
		return stronglyConnectedComponents;
	}
	
	private void calcCFCRic(AdjListDir g, String u, String[] vVisited, AtomicInteger t, VisitForest visit) {
		visit.setColor(u, Color.GRAY);
		
		for (String v : g.getAdjacent(u)) {
			if (visit.getColor(v) == Color.WHITE) {
				visit.setParent(v, u);
				calcCFCRic(g, v, vVisited, t, visit);
			}
		}
		visit.setColor(u, Color.BLACK);
		vVisited[t.get()] = u;
		t.set(t.get() + 1);;
	}

}