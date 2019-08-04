package GraphLayout;

public class Browser {
    public static void open(String url) {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            Runtime rt = Runtime.getRuntime();
            if (osName.indexOf("win") >= 0) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (osName.indexOf("mac") >= 0) {
                rt.exec("open " + url);
            } else if (osName.indexOf("ix") >= 0 || osName.indexOf("ux") >= 0 || osName.indexOf("sun") >= 0) {
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");

                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


