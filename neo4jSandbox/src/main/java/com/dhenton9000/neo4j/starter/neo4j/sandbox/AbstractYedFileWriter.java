package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import java.io.*;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractYedFileWriter writes a Graph for the yEd Graph Editor to a GraphML
 * OutputStream. This is adapted from code authored by
 * Benny Neugebauer (http://www.bennyn.de)
 *
 * @author Dhenton
 */
public abstract class AbstractYedFileWriter {

    private Graph graph = null;
    private String xml = null;
    private final Logger logger = LoggerFactory.getLogger(AbstractYedFileWriter.class);
   
  
    public AbstractYedFileWriter(Graph graph) {
       
         this.graph = graph;
    }

    private String getGraphMLHeader() {
        String header = "<?xml version=\"1.0\" ?>";
        header += "\r\n<graphml\r\n  xmlns=\"http://graphml.graphdrawing.org/xmlns\"\r\n  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n  xmlns:y=\"http://www.yworks.com/xml/graphml\"\r\n  xmlns:yed=\"http://www.yworks.com/xml/yed/3\"\r\n  xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\r\n  http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\"\r\n>";
        header += "\r\n  <key for=\"node\" id=\"d5\" attr.name=\"description\" attr.type=\"string\" />";
        header += "\r\n  <key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>";
        header += "\r\n  <key for=\"edge\" id=\"d9\" yfiles.type=\"edgegraphics\"/>";
        header += "\r\n  <graph id=\"G\" edgedefault=\"directed\">";
        return header;
    }

    private String getGraphMLFooter() {
        String footer = "\r\n  </graph>\r\n</graphml>";
        return footer;
    }

    private String getNode(String id, String label) {
        String node = "\r\n    <node id=\"" + id + "\">";
        node += "\r\n      <data key=\"d5\"/>";
        node += "\r\n      <data key=\"d6\">";
        node += "\r\n        <y:ShapeNode>";
        node += "\r\n          <y:NodeLabel>" + label + "</y:NodeLabel>";
        node += "\r\n          <y:Shape type=\"rectangle\"/>";
        node += "\r\n        </y:ShapeNode>";
        node += "\r\n      </data>";
        node += "\r\n    </node>";
        return node;
    }

    private String getEdge(Edge edge) {
        Vertex source = edge.getOutVertex();
        Vertex target = edge.getInVertex();
        String edgeId = edge.getId().toString();
        String sourceId = source.getId().toString();
        String targetId = target.getId().toString();
        String label = edge.getLabel();

        String edgeXml = "\r\n    <edge id=\""
                + edgeId + "\" source=\""
                + sourceId + "\" target=\""
                + targetId +   "\">";
        
        edgeXml += "\r\n    <data key=\"d9\">";
        edgeXml += "\r\n           <y:PolyLineEdge>";
        
        edgeXml += "\r\n               <y:EdgeLabel>"+label+"</y:EdgeLabel>";
        edgeXml += "\r\n               <y:BendStyle smoothed=\"false\"/> ";
        edgeXml += "\r\n           </y:PolyLineEdge>";
        edgeXml += "\r\n    </data>";
        edgeXml += "\r\n    </edge>";

        return edgeXml;
    }

    
    public abstract String computeVertexLabel(Vertex vertex);
    
    
    
    
    private void createGraphXml() {
        xml = getGraphMLHeader();

        // Create nodes
        Iterable<Vertex> vertices = graph.getVertices();

        int cc = 0;
        Iterator<Vertex> verticesIterator = vertices.iterator();
        while (verticesIterator.hasNext()) {
            Vertex vertex = verticesIterator.next();
            String labelValue = computeVertexLabel(vertex);
            String id = vertex.getId().toString();
            String node = getNode(id, labelValue);
            xml += node;
            cc++;
        }
        logger.debug("vertex count " + cc);
        // Create edges
        Iterable<Edge> edges = graph.getEdges();
        Iterator<Edge> edgesIterator = edges.iterator();
        while (edgesIterator.hasNext()) {
            Edge edge = edgesIterator.next();
            String edgeXml = getEdge(edge);
            xml += edgeXml;
        }

        xml += getGraphMLFooter();
    }

    public void outputGraph(final OutputStream out) throws IOException {
        createGraphXml();
        //logger.debug("xml\n" + xml);
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(out));
        br.write(xml);
        br.flush();

        out.flush();
        out.close();
    }

    public void outputGraph(String filePath) throws IOException {
        outputGraph(new FileOutputStream(filePath));
    }
}