package javax.help;

import java.awt.Component;
import java.awt.MenuItem;

public interface HelpBroker {
    public void enableHelpKey (Component p, String s, HelpSet hs);
    public void enableHelpOnButton (Component c, String s, HelpSet hs);
    public void enableHelpOnButton (MenuItem c, String s, HelpSet hs);
    public void setDisplayed (boolean b);
    public void setViewDisplayed (boolean b);
}
