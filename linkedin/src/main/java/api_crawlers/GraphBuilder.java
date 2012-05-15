package api_crawlers;

import java.util.Collection;
import java.util.HashSet;

import structure.LInPerson;

public class GraphBuilder {

	// connections stores connections between people - a connection can only
	// exist once - therefore a HashSet is used
	private HashSet<Connection> connections;
	private LInPerson center;

	public GraphBuilder(LInPerson center) {
		this.center = center;
		this.connections = new HashSet<Connection>();
	}

	public HashSet<Connection> getConnections() {
		return connections;
	}

	public void addFirstDegree(LInPerson person) {
		connections.add(new Connection(center, person));
	}

	public void addFirstDegree(Collection<LInPerson> people) {
		for (LInPerson person : people) {
			connections.add(new Connection(center, person));
		}
	}

	public void addShared(LInPerson connection, Collection<LInPerson> shared) {
		for (LInPerson person : shared) {
			connections.add(new Connection(connection, person));
		}
	}

	public void addShared(LInPerson connection, LInPerson shared) {
		connections.add(new Connection(connection, shared));
	}

}
