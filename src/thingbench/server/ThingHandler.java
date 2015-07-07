/**
 *
 * ThingBench - Things and Devices Simulator
 *
 * http://github.com/frapu78/thingbench
 *
 * @author Frank Puhlmann
 *
 */
package thingbench.server;

import com.inubit.research.gui.Workbench;
import com.inubit.research.server.ProcessEditorServerUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.frapu.code.visualization.ProcessEditor;
import net.frapu.code.visualization.ProcessModel;
import net.frapu.code.visualization.ProcessNode;
import net.frapu.code.visualization.ProcessUtils;
import org.w3c.dom.DOMException;
import thingbench.ThingsModel.ThingsModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import thingbench.ThingOperation;
import thingbench.ThingOperationInterface;

public class ThingHandler implements HttpHandler {

    public static final String TAG_THING = "thing";
    public static final String TAG_OPERATION = "operation";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_LINK = "link";

    private Workbench workbench;

    public ThingHandler(Workbench wb) {
        this.workbench = wb;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        // Create list of all things in all models(!) - Q&D Style

        System.out.println(t.getRequestMethod() + " " + t.getRequestURI());

        // Check method
        if (t.getRequestMethod().equalsIgnoreCase("GET")) {
            // GET
            handleGetRequest(t);
        } else if (t.getRequestMethod().equalsIgnoreCase("PUT")) {
            // PUT
            handlePutRequest(t);
        } else if (t.getRequestMethod().equalsIgnoreCase("POST")) {
            // PUT
            handlePostRequest(t);
        } else {
            // ANYTHING ELSE := ERROR
            String response = "METHOD NOT ALLOWED (TRY GET, PUT, POST)";
            t.sendResponseHeaders(405, response.length());
            {
                try (OutputStream os = t.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    protected void handlePostRequest(HttpExchange t) throws IOException {
        String response = "";
        int statusCode = 200;
        try {
            // @todo: Implement POST

            // Here we wait for the messages...
        } catch (Exception e) {
            statusCode = 404;
            response += "Invalid URL or board or thing not found!";
        }

        t.sendResponseHeaders(statusCode, response.length());
        {
            try (OutputStream os = t.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    protected void handlePutRequest(HttpExchange t) throws IOException {
        String response = "";
        int statusCode = 200;
        try {
            ProcessNode thingNode = getThingNodeFromRequest(t);

            if (thingNode != null) {

                // PARSE XML...                        
                DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
                xmlFactory.setNamespaceAware(false);
                DocumentBuilder builder = xmlFactory.newDocumentBuilder();
                Document xmlDoc = builder.parse(t.getRequestBody());

                // Simply take all properties @todo: Check if root node is a thing!
                NodeList propList = xmlDoc.getElementsByTagName("property");
                if (propList.getLength() > 0) {
                    for (int i2 = 0; i2 < propList.getLength(); i2++) {

                        // Ok, properties found
                        Node thingXMLNode = propList.item(i2);

                        NamedNodeMap propAttr = thingXMLNode.getAttributes();
                        String propName = propAttr.getNamedItem("name").getTextContent();
                        String propValue = propAttr.getNamedItem("value").getTextContent();
                        // Skip private nodes - Cannot be written!
                        if (propName.startsWith("#")) {
                            continue;
                        }
                        System.out.println("UPDATING " + propName + " TO " + propValue + " FOR " + t.getRequestURI());
                        thingNode.setProperty(propName, propValue);
                    }

                } else {
                    statusCode = 400;
                    response += "BAD REQUEST (SYNTAX IN PUT: PLEASE USE STRUCTURE FROM GET)";
                }
                response = listThingPropertiesAndOps(thingNode);
                // Refresh editor
                workbench.getSelectedProcessEditor().repaint();
            }

        } catch (Exception e) {
            statusCode = 404;
            response += "Invalid URL or board or thing not found!";
        }

        t.sendResponseHeaders(statusCode, response.length());
        {
            try (OutputStream os = t.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    protected void handleGetRequest(HttpExchange t) throws IOException {
        String response = "";
        int statusCode = 200;

        if (t.getRequestURI().toString().equals("/things")) {
            response += listThings(t);
        } else {

            ProcessNode thingNode = getThingNodeFromRequest(t);

            if (thingNode != null) {
                try {
                    response = listThingPropertiesAndOps(thingNode);
                } catch (Exception ex) {
                    statusCode = 500;
                    response = "INTERNAL SERVER ERROR: "+ex.toString();
                }                
            } else {
                statusCode = 404;
                response += "Invalid URL or board or thing not found!";
            }
        }
        t.sendResponseHeaders(statusCode, response.length());
        {
            try (OutputStream os = t.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    protected String listThingPropertiesAndOps(ProcessNode thingNode) throws IOException, ParserConfigurationException, DOMException {
        String response;
        // Create HashMap with properties
        HashMap props = new HashMap();
        for (String key : thingNode.getPropertyKeys()) {
            props.put(key, thingNode.getProperty(key));
        }
        // Create response document
        DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
        xmlFactory.setNamespaceAware(false);
        DocumentBuilder builder = xmlFactory.newDocumentBuilder();
        Document resultDOM = builder.newDocument();
        Element thingDOMNode = resultDOM.createElement(TAG_THING);
        resultDOM.appendChild(thingDOMNode);
        ProcessUtils.writeProperties(resultDOM, thingDOMNode, props);
        // Add all Operations if applicable
        if (thingNode instanceof ThingOperationInterface) {
            ThingOperationInterface opsIf = (ThingOperationInterface) thingNode;
            for (ThingOperation op : opsIf.getThingOperations()) {
                // Render name
                Element opNode = resultDOM.createElement(TAG_OPERATION);
                opNode.setAttribute(ATTR_LINK, "n/a (yet)");
                opNode.setAttribute(ATTR_NAME, op.getOperationName());
                thingDOMNode.appendChild(opNode);
            }
        }

        Writer writer = new StringWriter();
        ProcessEditorServerUtils.writeXMLtoStream(writer, resultDOM);
        writer.close();
        response = writer.toString();
        return response;
    }

    /**
     * @todo: Refactor to DOM!
     * @param t
     * @return
     */
    private String listThings(HttpExchange t) {
        String response = "<things>\n";
        for (int i = 0; i < workbench.getNumOfProcessEditors(); i++) {
            ProcessEditor editor = workbench.getProcessEditor(i);
            ProcessModel model = editor.getModel();
            // Check if model is ThingsModel
            if (model instanceof ThingsModel) {
                response += "   <board name=\"" + model.getProcessName() + "\" id=\"" + model.getId() + "\">\n";
                for (ProcessNode node : model.getNodes()) {
                    response += "      <thing name=\"" + node.getName() + "\" id=\"" + node.getId()
                            + "\" link=\"http:/" + t.getLocalAddress() + "/things/" + model.getId() + "/" + node.getId() + "\"/>\n";
                }
                response += "   </board>\n";
            }
        }
        response += "</things>";
        return response;
    }

    private ProcessNode getThingNodeFromRequest(HttpExchange t) {
        ProcessNode thingNode = null;
        // Another Q&D hack: We expect things/boardnumber/thingnumber (!)
        // @todo: Refactor to use RegEx!
        // Try to parse model number out...
        StringTokenizer tok = new StringTokenizer(t.getRequestURI().toString(), "/");
        String things = tok.nextToken();
        String board = tok.nextToken();
        String thing = tok.nextToken();
        // Iterate over all editors/model to check match
        for (int i = 0; i < workbench.getNumOfProcessEditors(); i++) {
            ProcessEditor editor = workbench.getProcessEditor(i);
            ProcessModel model = editor.getModel();
            if (model.getId().equals(board)) {
                // Yep, found; search for node
                thingNode = model.getNodeById(thing);
                break;
            }
        }
        return thingNode;
    }
}
