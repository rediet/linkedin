package model;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;



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

	private static final String DANIELE = "scar4b1FXy";
	private static final String REMO = "7fV1HdMLHo";

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
								+ "linkedin.properties"); // TODO: change path!
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create request factory to do API calls
		final Request requester = new Request(generator.getService(),
				generator.getAccessToken());
		
		//Open an interface asking what action to perform
		JFrame frame = new JFrame("LinkedIn Analyzer");
		JButton button1 = new JButton("First Degree Connections");
		button1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PeopleCrawler crawler = new PeopleCrawler(requester);
				GraphBuilder graph = new GraphBuilder();
				LInPerson self = crawler.getOwnProfile();
				List<LInPerson> people = crawler.getFirstDegreeConnections();
				graph.addConnection(self, people);
				Graph<LInPerson, Connection> g = new SparseMultigraph<LInPerson, Connection>();

				for (Connection c : graph.getConnections()) {
					//System.out.println(c);
					LInPerson v1 = c.getFirst();
					LInPerson v2 = c.getSecond();
					g.addEdge(c, v1, v2);
				}
				
				visualizeGraph(g, "First Degree Connections");
				
			}
			
		});
		JButton button2 = new JButton("Second Degree Connections");
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PeopleCrawler crawler = new PeopleCrawler(requester);
				GraphBuilder graph = new GraphBuilder();
				LInPerson self = crawler.getOwnProfile();
				List<LInPerson> people = crawler.searchPeople("facet=network,S");
				graph.addConnection(self, people);
				Graph<LInPerson, Connection> g = new SparseMultigraph<LInPerson, Connection>();

				for (Connection c : graph.getConnections()) {
					//System.out.println(c);
					LInPerson v1 = c.getFirst();
					LInPerson v2 = c.getSecond();
					g.addEdge(c, v1, v2);
				}
				
				visualizeGraph(g, "Second Degree Connections");
				
			}
			
		});
		JButton button3 = new JButton("Groups");
		button3.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				PeopleCrawler crawler = new PeopleCrawler(requester);
				GraphBuilder graph = new GraphBuilder();
				 // people-search inside one of the user's groups
				
				LInPerson self = crawler.getOwnProfile();
				List<LInPerson> people = crawler.getFirstDegreeConnections();
				crawler.searchPeople("facet=network,A");
				graph.addConnection(self, people);
				Graph<LInPerson, Connection> g = new SparseMultigraph<LInPerson, Connection>();

				for (Connection c : graph.getConnections()) {
					//System.out.println(c);
					LInPerson v1 = c.getFirst();
					LInPerson v2 = c.getSecond();
					g.addEdge(c, v1, v2);
				}
				
				visualizeGraph(g, "people-search inside one of the user's groups");
				
				
			}
			
		});
		JButton button4 = new JButton("Shared connections");
		button4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PeopleCrawler crawler = new PeopleCrawler(requester);
				GraphBuilder graph = new GraphBuilder();
				List<LInPerson> people = crawler.getFirstDegreeConnections();
				// // shared connections
				 for (LInPerson p : people) {
					 graph.addConnection(p, crawler.getSharedConnections(p.getId()));
				 }
					Graph<LInPerson, Connection> g = new SparseMultigraph<LInPerson, Connection>();

					for (Connection c : graph.getConnections()) {
						//System.out.println(c);
						LInPerson v1 = c.getFirst();
						LInPerson v2 = c.getSecond();
						g.addEdge(c, v1, v2);
					}
					
					visualizeGraph(g, "Shared Connections");			
				
			}
			
		});
		
		frame.setLayout(new GridLayout(2, 2));
		frame.add(button1);
		frame.add(button2);
		frame.add(button3);
		frame.add(button4);
		
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		
	}
	
	
	public static void visualizeGraph(Graph g, String title) {
		Layout<String, String> layout = new CircleLayout(g);
		   
        BasicVisualizationServer<String,String> vv = new BasicVisualizationServer<String,String>(layout);
        
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
       // vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);        
        
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true); 
		
	}
}
