/**
 *
 * ThingBench - Things and Devices Simulator
 *
 * http://github.com/frapu78/thingbench
 *
 * @author Frank Puhlmann
 *
 */
package thingbench.ThingsModel;

import java.util.LinkedList;
import java.util.List;
import net.frapu.code.visualization.ProcessEdge;
import net.frapu.code.visualization.ProcessModel;
import net.frapu.code.visualization.ProcessNode;

/**
 *
 * @author fpu
 */
public class ThingsModel extends ProcessModel {
    
    public ThingsModel() {
        this(null);
    }
    
    public ThingsModel(String name) {
        super(name);    
        processUtils = new ThingsModelUtils();     
    }

    @Override
    public String getDescription() {
        return "Things Model";
    }

    @Override
    public List<Class<? extends ProcessNode>> getSupportedNodeClasses() {
        List<Class<? extends ProcessNode>> result = new LinkedList<>();
        result.add(Lamp.class);
        result.add(Artifact.class);
        return result;
    }

    @Override
    public List<Class<? extends ProcessEdge>> getSupportedEdgeClasses() {
        List<Class<? extends ProcessEdge>> result = new LinkedList<>();
        return result;    }
    
    @Override
    public String toString() {
        return this.getProcessName()+" (Things Model)";
    }
    
}

        