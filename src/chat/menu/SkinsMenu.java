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
package chat.menu;

import chat.State;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** SkinsMenu Menu */
public class SkinsMenu extends JMenu {
	final JFrame mainFrame;

	private static JRadioButtonMenuItem miJava,miMotif,miWin,miMac;
	/** javax.swing.plaf.metal.MetalLookAndFeel */
	public final static String metalClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
	/** com.sun.java.swing.plaf.motif.MotifLookAndFeel */
	public final static String motifClassName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	/** com.sun.java.swing.plaf.windows.WindowsLookAndFeel */
	public final static String windowsClassName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	/** com.sun.java.swing.plaf.mac.MacLookAndFeel */
	public final static String macClassName = "com.sun.java.swing.plaf.mac.MacLookAndFeel";

	public SkinsMenu(final JFrame mainFrame) {
		super("Skins");
		this.mainFrame = mainFrame;

		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem miJava = new JRadioButtonMenuItem("Java Look and Feel");
		miJava.setEnabled(isAvailableLookAndFeel(metalClassName));
		add(miJava);
		group.add(miJava);
		if (State.currentUI.equals(metalClassName)) miJava.setSelected(true);
		miJava.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setUI(metalClassName);
				}
			}
		);

		JRadioButtonMenuItem miMotif = new JRadioButtonMenuItem("Motif Look and Feel");
		miMotif.setEnabled(isAvailableLookAndFeel(motifClassName));
		add(miMotif);
		group.add(miMotif);
		if (State.currentUI.equals(motifClassName)) miMotif.setSelected(true);
		miMotif.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setUI(motifClassName);
				}
			}
		);

		JRadioButtonMenuItem miWin = new JRadioButtonMenuItem("Windows Style Look and Feel");
		miWin.setEnabled(isAvailableLookAndFeel(windowsClassName));
		add(miWin);
		group.add(miWin);
		if (State.currentUI.equals(windowsClassName)) miWin.setSelected(true);
		miWin.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setUI(windowsClassName);
				}
			}
		);

		JRadioButtonMenuItem miMac = new JRadioButtonMenuItem("Macintosh Look and Feel");
		miMac.setEnabled(isAvailableLookAndFeel(macClassName));
		add(miMac);
		group.add(miMac);
		if (State.currentUI.equals(macClassName)) miMac.setSelected(true);
		miMac.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setUI(macClassName);
				}
			}
		);
	}
	
	public void setUI(String newUI) {
		try  {
			UIManager.setLookAndFeel(newUI);
			SwingUtilities.updateComponentTreeUI(SwingUtilities.getRoot(mainFrame));
			State.currentUI = newUI;
			repaint();
		}catch(Exception ui) {System.out.println("LAF Failed");}
	}
	
	/** Tests whether Look And Feel is available on this system */
	private static boolean isAvailableLookAndFeel(String classname) {
		try { // Try to create a L&F given a String
			Class lnfClass = Class.forName(classname);
			LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
			return newLAF.isSupportedLookAndFeel();
		} catch(Exception e) { // If ANYTHING weird happens, return false
			return false;
		}
	}
}