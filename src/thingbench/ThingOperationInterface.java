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

import java.util.List;

/**
 * 
 * This interface provides support for operations on Things via the REST
 * API as well as the ThingsModelEditor.
 * 
 * @todo: Implement reference for Lamp
 * 
 * @author fpu
 */
public interface ThingOperationInterface {
    
    /**
     * Gets the list of supported operations by a certain thing.
     * @return 
     */
    public List<ThingOperation> getThingOperations();
  
}
