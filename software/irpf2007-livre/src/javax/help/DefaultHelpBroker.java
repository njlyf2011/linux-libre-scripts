package javax.help;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionListener;

public class DefaultHelpBroker implements HelpBroker {
    public DefaultHelpBroker (HelpSet hs) {
    }

    public void enableHelpKey (Component p, String s, HelpSet hs) {
    }

    public void enableHelpOnButton (Component c, String s, HelpSet hs) {
    }

    public void enableHelpOnButton (MenuItem c, String s, HelpSet hs) {
    }

    public void setDisplayed (boolean b) {
    }

    public void setViewDisplayed (boolean b) {
    }

    protected ActionListener getDisplayHelpFromSource () {
	return null;
    }
}
