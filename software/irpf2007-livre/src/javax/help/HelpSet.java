package javax.help;

import java.net.URL;

public class HelpSet {
    public HelpSet (ClassLoader cl, URL url) throws HelpSetException {
    }

    public static class Presentation {
    }

    public static URL findHelpSet (ClassLoader cl, String name) {
	return cl.getResource (name);
    }
}
