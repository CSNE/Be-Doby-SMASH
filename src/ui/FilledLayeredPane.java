package ui;

import java.awt.Component;
import javax.swing.JLayeredPane;
/* A panel widget that allows components to be layered on top of one another,
        * where each component fills the entirety of this container.
        * This is only useful when all but the lowest layer is set transparent, as
        * the highest opaque layer will obscure any layers below it.
        * @NOTE If one wants to move the layers to choose which one is visible, your
        * better option is to use a JPanel with the CardLayout.
        * @author kkyzivat*/
public class FilledLayeredPane extends JLayeredPane {
    /* Layout each of the components in this JLayeredPane so that they all fill
    * the entire extents of the layered pane -- from (0,0) to (getWidth(), getHeight())*/
    @Override
    public void doLayout() {
        // Synchronizing on getTreeLock, because I see other layouts doing that.
        // see BorderLayout::layoutContainer(Container)
        synchronized (getTreeLock()) {
            int w = getWidth();
            int h = getHeight();
            for (Component c : getComponents()) {
                c.setBounds(0, 0, w, h);
            }
        }
    }
}