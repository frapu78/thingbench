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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.frapu.code.visualization.ProcessNode;
import net.frapu.code.visualization.ProcessUtils;
import net.frapu.code.visualization.editors.ListSelectionPropertyEditor;

/**
 *
 * @author fpu
 */
public class Lamp extends ProcessNode {

    public final static String PROP_POWER = "power";

    public static final String POWER_ON = "ON";
    public static final String POWER_OFF = "OFF";

    public Lamp() {
        init();
    }

    public Lamp(String name) {
        init();
        setText(name);
    }

    protected void init() {
        setSize(60, 60);
        setBackground(new Color(204, 204, 0));
        // Add POWER property
        setProperty(PROP_POWER, POWER_OFF);
        String[] atype = {POWER_OFF, POWER_ON};
        setPropertyEditor(PROP_POWER, new ListSelectionPropertyEditor(atype));
    }

    @Override
    public void setSize(int w, int h) {
        super.setSize(100, 140);
    }

    @Override
    protected void paintInternal(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Ellipse2D bulb = new Ellipse2D.Double(getPos().x - getSize().width / 2, getPos().y - getSize().height / 2,
                getSize().width, getSize().width);

        Rectangle2D socket = new Rectangle2D.Double(getPos().x - getSize().width / 4, getPos().y,
                getSize().width / 2, getSize().height / 2);

        g2.setStroke(ProcessUtils.boldStroke);

        // Draw Socket       
        g2.setPaint(new Color(200, 200, 200));
        g2.fill(socket);

        g2.setPaint(Color.black);
        g2.draw(socket);

        // Draw Bulb
        Color c = getBackground();
        if (getProperty(PROP_POWER).equals(POWER_ON)) {
            // Increase color
            c = c.brighter().brighter().brighter();

            g2.setPaint(Color.BLACK);
        }

        g2.setPaint(c);
        g2.fill(bulb);

        g2.setPaint(Color.BLACK);
        g2.draw(bulb);

        if (getText() != null) {
            drawText(g2);
        }

    }

    private Rectangle drawText(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        return ProcessUtils.drawText(g2, getPos().x, getPos().y + (getSize().width / 2) + 20, 200,
                getText() + " (" + getProperty(PROP_POWER) + ")", ProcessUtils.Orientation.TOP);
    }

    @Override
    public Rectangle getBoundingBox() {
        // Get bounds of text
        BufferedImage dummyImg = new BufferedImage(200, 50, BufferedImage.BITMASK);
        Graphics2D g2 = dummyImg.createGraphics();
        Rectangle gfxBounds = super.getBoundingBox();
        Rectangle textBounds = new Rectangle(gfxBounds);
        if (getText() != null) {
            textBounds = drawText(g2);
        }
        // Merge bounds
        gfxBounds.add(textBounds);
        return gfxBounds;
    }

    @Override
    protected Shape getOutlineShape() {
        Rectangle2D outline = new Rectangle2D.Float(getPos().x - (getSize().width / 2),
                getPos().y - (getSize().height / 2), getSize().width, getSize().height);
        return outline;
    }

}
