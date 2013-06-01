/*
This file is part of JSChat.

JSChat is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JSChat is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with JSChat.  If not, see <http://www.gnu.org/licenses/>.
*/
package chat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/** This static class application-wide static variables */
public class State {
	/** Are we listening or connecting */
	public static boolean weAreServer;
	/** CurrentUI (Java/Motif/Windows/Macintosh) */
	public static String currentUI = chat.menu.SkinsMenu.metalClassName;
	/** IP of server to connect to */
	public static String serverIP = "127.0.0.1";
	/** IP of this machine */
	public static String currentIP="127.0.0.1";
	/** Default name of user */
	public static final String yourNameInit = "Remote User";
	/** Name of user */
	public static String yourName = yourNameInit;

	/** Are we currently connected */
	public static boolean connected = false;
	/** Text port */
	public static int talkPort = 5000;
	/** File transfer port */
	public static int filePort = 5001;
	/** Number of connections */
	public static int numConnections = 0;
	/** Seed for CRC32 */
	public static long seed=0xFFFFFFFF;
	
	/** Gui left x co-ordinate */
	public static int x = 50;
	/** Gui top y co-ordinate */
	public static int y = 50;
	/** Gui width */
	public static int width = 640;
	/** Gui height */
	public static int height = 300;
	
	private static Properties applicationProps = new Properties();;
	
	/** Reads in saved state variables */
	public State(){
		readState();
	}

	/** Saves state variables and terminates application */
	public static void shutDown(){
		saveState();
		System.exit(0);
	}

	/** Save last page visited for each subject and proxy settings */
	private static void saveState(){
		try {
			applicationProps.put("currentUI", currentUI);
			applicationProps.put("yourName", yourName);
			applicationProps.put("serverIP", serverIP);
			applicationProps.put("talkPort", Integer.toString(talkPort));
			applicationProps.put("filePort", Integer.toString(filePort));
			applicationProps.put("numConnections", Integer.toString(numConnections));
			applicationProps.put("GuiX", Integer.toString(x));
			applicationProps.put("GuiY", Integer.toString(y));
			applicationProps.put("GuiWidth", Integer.toString(width));
			applicationProps.put("GuiHeight", Integer.toString(height));
			FileOutputStream out = new FileOutputStream("appProperties");
			applicationProps.store(out, "---Properties for JSChat---");
			out.close();
		} catch (Exception e) {}
	}		
	
	/** Restore last page visited for each subject and proxy settings */
	private static void readState(){
		try {
			FileInputStream in = new FileInputStream("appProperties");
			applicationProps.load(in);
			in.close();

			if (applicationProps.containsKey("currentUI"))
				currentUI = applicationProps.getProperty("currentUI");
			if (applicationProps.containsKey("yourName"))
				yourName = applicationProps.getProperty("yourName");
			if (applicationProps.containsKey("serverIP"))
				serverIP = applicationProps.getProperty("serverIP");
			if (applicationProps.containsKey("talkPort"))
				talkPort = Integer.parseInt(applicationProps.getProperty("talkPort"));
			if (applicationProps.containsKey("filePort"))
				filePort = Integer.parseInt(applicationProps.getProperty("filePort"));
			if (applicationProps.containsKey("filePort"))
				numConnections = Integer.parseInt(applicationProps.getProperty("numConnections"));
			if (applicationProps.containsKey("GuiX"))
				x = Integer.parseInt(applicationProps.getProperty("GuiX"));
			if (applicationProps.containsKey("GuiY"))
				y = Integer.parseInt(applicationProps.getProperty("GuiY"));
			if (applicationProps.containsKey("GuiWidth"))
				width = Integer.parseInt(applicationProps.getProperty("GuiWidth"));
			if (applicationProps.containsKey("GuiHeight"))
				height = Integer.parseInt(applicationProps.getProperty("GuiHeight"));
		} catch (Exception e) {}
	}		
}
