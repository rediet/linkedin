package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import structure.LInPerson;

import api_crawlers.Connection;
import api_crawlers.GraphBuilder;
import api_crawlers.PeopleCrawler;

public class Main {

	public static final String DANIELE = "scar4b1FXy";
	public static final String REMO = "7fV1HdMLHo";

	public static void main(String[] args) {
		AccessGenerator generator = null;
		try {
			if (args.length == 1) {
				generator = AccessGenerator.generateFromProperties(args[0]);
			} else {
				// Get access to the LinkedIn server
				generator = AccessGenerator
						.generateFromProperties(File.separator + "home"
								+ File.separator + "daniele" + File.separator
								+ "linkedin" + File.separator
								+ "linkedin.properties"); // TODO: change path!
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create request factory to do API calls
		Request requester = new Request(generator.getService(),
				generator.getAccessToken());

		GraphBuilder graph = crawlNetwork(requester);

		// Graph<V, E> where V is the type of the vertices and E is the type of
		// the edges
		Graph<LInPerson, Connection> g = new SparseMultigraph<LInPerson, Connection>();

		for (Connection c : graph.getConnections()) {
			// System.out.println(c);
			LInPerson v1 = c.getFirst();
			LInPerson v2 = c.getSecond();
			g.addEdge(c, v1, v2);
		}

	}

	public static GraphBuilder crawlNetwork(Request requester) {
		PeopleCrawler crawler = new PeopleCrawler(requester);
		GraphBuilder graph = new GraphBuilder();

		// // first degree connections
		// LInPerson self = crawler.getOwnProfile();
		// List<LInPerson> people = crawler.getFirstDegreeConnections();
		// graph.addConnection(self, people);

		// // 2nd degree people-search
		// people = crawler.searchPeople("facet=network,S");
		// graph.addConnection(self, people);

		// // people-search inside one of the user's groups
		// crawler.searchPeople("facet=network,A");
		// graph.addConnection(self, people);

		// specific 2nd degree or group people-search
		crawler.searchPeople("company-name=University of Bern",
				"facet=network,S");

		// // shared connections
		// for (LInPerson p : people) {
		// graph.addConnection(p, crawler.getSharedConnections(p.getId()));
		// }

		return graph;
	}
}
