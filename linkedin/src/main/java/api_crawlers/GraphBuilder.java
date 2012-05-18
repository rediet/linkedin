package api_crawlers;

import java.util.Collection;
import java.util.HashSet;


import structure.LInPerson;

public class GraphBuilder {

	// connections stores connections between people - a connection can only
	// exist once - therefore a HashSet is used
	private HashSet<Connection> connections;

	public GraphBuilder() {
		this.connections = new HashSet<Connection>();
	}

	public HashSet<Connection> getConnections() {
		return connections;
	}

	public void addConnection(LInPerson from, Collection<LInPerson> toMultiple) {
		for (LInPerson to : toMultiple) {
			connections.add(new Connection(from, to));
		}
	}

	public void addConnection(LInPerson from, LInPerson to) {
		connections.add(new Connection(from, to));
	}
	
	
	

}
