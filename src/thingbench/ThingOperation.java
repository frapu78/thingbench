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

import java.util.HashMap;
import net.frapu.code.visualization.ProcessNode;

/**
 * This class provides an operation on a thing.
 * 
 * @author fpu
 */
public abstract class ThingOperation {
   
    private ProcessNode thingNode;
    private String operationName;

    public ThingOperation(ProcessNode thingNode, String operationName) {
        this.thingNode = thingNode;
        this.operationName = operationName;
    }

    public ProcessNode getThingNode() {
        return thingNode;
    }

    public String getOperationName() {
        return operationName;
    }
    
    /**
     * This class executes the Operation. Each operation has a set of properties
     * of the type <String, String> as input and output. What the operation
     * does with it remains to the operation.
     * @param properties
     * @return 
     * @throws thingbench.ThingExecutionException 
     */
    public abstract HashMap<String, String> execute(HashMap<String, String> properties) throws ThingExecutionException;
    
}
