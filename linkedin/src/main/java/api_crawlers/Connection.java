package api_crawlers;

import structure.LInPerson;

public class Connection {

	private LInPerson first, second;
	
	private int type; //1 first degree, 2 second degree etc...

	/**
	 * Stores two LInPerson objects in a Connection object. The ordering is not
	 * important - two connections with the same elements are considered as
	 * equal.
	 */
	public Connection(LInPerson node1, LInPerson node2) {
		int hash1 = (node1 == null) ? 0 : node1.hashCode();
		int hash2 = (node2 == null) ? 0 : node2.hashCode();
		// compare hashCodes and assign smaller one to first element
		if (hash1 < hash2) {
			this.first = node1;
			this.second = node2;
		} else {
			this.first = node2;
			this.second = node1;
		}
	}
	
	public LInPerson getFirst() {
		return first;
	}
	
	public LInPerson getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Connection))
			return false;
		Connection other = (Connection) obj;

		return (first == other.first && second == other.second);
	}

	@Override
	public String toString() {
		return "Connection [first=" + first.getId() + ", second="
				+ second.getId() + "]";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
