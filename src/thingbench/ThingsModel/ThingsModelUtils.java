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

import com.inubit.research.layouter.ProcessLayouter;
import java.util.LinkedList;
import java.util.List;
import net.frapu.code.visualization.ProcessEdge;
import net.frapu.code.visualization.ProcessNode;
import net.frapu.code.visualization.ProcessUtils;
import net.frapu.code.visualization.petrinets.PetriNetUtils;

/**
 *
 * @author fpu
 */
public class ThingsModelUtils extends PetriNetUtils {

    @Override
    public ProcessEdge createDefaultEdge(ProcessNode pn, ProcessNode pn1) {
        return super.createDefaultEdge(pn, pn1);
    }

    @Override
    public List<ProcessLayouter> getLayouters() {
        return super.getLayouters();
    }
    
}
