/**
 *
 * ThingBench - Things and Devices Simulator
 *
 * http://github.com/frapu78/thingbench
 *
 * @author Frank Puhlmann
 *
 */
package thingbench;

import com.inubit.research.gui.Workbench;
import com.inubit.research.gui.WorkbenchHelper;
import com.inubit.research.gui.plugins.NodeViewPlugin;
import com.inubit.research.gui.plugins.PluginHelper;
import com.sun.net.httpserver.HttpServer;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.swing.JFrame;
import net.frapu.code.simulation.petrinets.PetriNetSimulationEditor;
import net.frapu.code.visualization.ProcessModel;
import net.frapu.code.visualization.ProcessUtils;
import net.frapu.code.visualization.SwingUtils;
import net.frapu.code.visualization.petrinets.PetriNetModel;
import thingbench.ThingsModel.Lamp;
import thingbench.ThingsModel.ThingsModel;
import thingbench.server.ThingHandler;

/**
 *
 * @author fpu
 */
public class ThingBench {

    /**
     * The PORT to be used for the server
     */
    private final int PORT = 9099;

    private final Workbench workbench;

    public ThingBench() throws IOException {
        // Reconfigure Workbench Helper
        WorkbenchHelper.removeAllProcessModels();
        WorkbenchHelper.addSupportedProcessModel(ThingsModel.class, ThingsModelEditor.class);
        WorkbenchHelper.addSupportedProcessModel(PetriNetModel.class, PetriNetSimulationEditor.class);
        // Configure Plugin Helper       
        PluginHelper.removeAllPlugins();
        PluginHelper.addPlugin(NodeViewPlugin.class);
        // Create new Workbench
        Workbench.setWorkbenchTitle("ThingBench");
        Workbench.setWorkbenchVersion("0.2015.07.06");
        workbench = new Workbench(false);
        ProcessModel model = createDefaultModel();
        workbench.addModel(model.getProcessName(), model);
        // Create server
        createServer();
    }

    private void createServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/things", new ThingHandler(workbench));
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Browse your things at http://localhost:" + PORT + "/things");
    }

    private ProcessModel createDefaultModel() {
        try {
            // Try to load default model
            System.out.println("Loading default model from models/default.model...");
            return ProcessUtils.parseProcessModelSerialization(
                    new FileInputStream("models/default.model")
            );
        } catch (Exception ex) {
            System.out.println("WARNING: Unable to load default model from file models/default.model");
        }

        ThingsModel model = new ThingsModel();

        Lamp lamp1 = new Lamp("Lamp A");
        lamp1.setPos(200, 100);
        model.addNode(lamp1);

        Lamp lamp2 = new Lamp("Lamp B");
        lamp2.setBackground(new Color(153, 0, 0));
        lamp2.setPos(400, 100);
        model.addNode(lamp2);

        return model;
    }

    public Workbench getWorkbench() {
        return workbench;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ThingBench thingbench = new ThingBench();
        JFrame f = thingbench.getWorkbench();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtils.center(f);
        f.setVisible(true);
    }

}
