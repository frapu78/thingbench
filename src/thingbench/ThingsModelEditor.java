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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.frapu.code.visualization.ProcessEditor;
import net.frapu.code.visualization.ProcessModel;
import net.frapu.code.visualization.ProcessNode;
import net.frapu.code.visualization.ProcessObject;
import thingbench.ThingsModel.Lamp;

/**
 *
 * @author fpu
 */
public class ThingsModelEditor extends ProcessEditor {

    public ThingsModelEditor() {
        super();
        init();
    }

    public ThingsModelEditor(ProcessModel model) {
        super(model);
        init();
    }

    private void init() {
        System.out.println("ThingsModel Editor additions loaded.");
        // Add context menu for Lamp
        addPowerContextMenu();
    }

    private void addPowerContextMenu() {        
        
        JMenuItem itemOn = new JMenuItem("ON");
        JMenuItem itemOff = new JMenuItem("OFF");
       
        JMenu menu = new JMenu("Power");
        itemOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                getLastSelectedNode().setProperty(Lamp.PROP_POWER, Lamp.POWER_ON);
                repaint();
            }
        });
        menu.add(itemOn);
        itemOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLastSelectedNode().setProperty(Lamp.PROP_POWER, Lamp.POWER_OFF);             
                repaint();
            }
        });        
        menu.add(itemOff);

        addCustomContextMenuItem(Lamp.class, menu);
    }
}
