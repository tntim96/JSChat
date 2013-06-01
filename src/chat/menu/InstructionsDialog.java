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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InstructionsDialog extends ChatDialog {
	String text = 
	"JSChat By tntim96\nhttps://github.com/tntim96/JSChat\n\nJSChat is an internet utility that allows you to \"text\" chat and send files securely across the internet.\n\nTo connect to a friend you must both be connected to the internet. You must also decide who is to select \"Network\">\"Listen for a connection\" and who is to select \"Network\">\"Connect to Server\". Whoever selects \"Network\">\"Connect to Server\" must first select \"Setup\">\"Connect IP Address\" and set the \"Connect IP Address\" to the IP address of the other person.\n\nTo prepare for an encrypted conversation:\n1)Person A and B select \"Crypt\">\"Generate Assymetric Keys\"\n2)Person A and B give each other their public keys\n\nThe typical series of events to set-up a connection are:\n1)Arrange a time for you and your friend to connect to the Internet\n2)Person A selects \"Utilties\">\"Obtain your IP Address\"\n3)Person A then emails his/her IP Address to person B\n4)Person A then selects \"Network\">\"Listen for Connection\"\n5)Person B then reads the email and sets the \"Connect IP Address\" address by selecting \"Setup\">\"Connect IP Address\"\n6)Person B then selects \"Network\">\"Connect to Server\"\n\nTo secure the channel:\n1)Person A and B select \"Crypt\">\"Load Private Key\"\n2)Each person selects their own private key\n3)Person A and B select \"Crypt\">\"Load Public Key\"\n4)Each person selects the other's public key\n5)Person A or B selects \"Crypt\">\"Secure Channel\"\n\nTo send a file:\n1)Person A selects \"Utilities\">\"Send a File\" then select a file to send\n2)Person B will see a save dialog to save the file\n\nIf the file transfer is interrupted or cancelled, you can resume the file transfer:\n1)Person A selects \"Utilities\">\"Send a File\", then selects the file to resume sending\n2)Person B will see a save dialog and should select the original file from the first file transfer.\n\nTo compare large files on different computers (e.g. after sending a file)\n1)Person A selects \"File Integrity\">\"CRC32\" then select the file\n2)Person B selects \"File Integrity\">\"CRC32\" then select the same file on his/her system\n3)Person A and B can then compare the CRC32 value - if the CRC32 values are the same the files should be the same\n4)To improve the comparison Person A and B can agree on a different seed and generate a new CRC32 value by selecting \"File Integrity\">\"SEED\" and entering the new seed. The seed can consist of 8 Hex digits (characters between 0-9 and a-f). Person A and B then follow steps 1 to 3.\n\nWhen using JSChat for long periods, JSChat may be set to run in the background. To regain the other users attention, select \"Utilities\">\"Wake Remote User\". This will cause the remote user's computer to beep, and also bring JSChat to the foreground of their screen.";

	Color orange = new Color(255,204,153);
	JTextArea instructText = new JTextArea(text);
	JButton ok = new JButton("GOOD LUCK!");    			

	public InstructionsDialog(JFrame parent) {
		super(parent, "Instructions", true);
		getContentPane().setLayout(new BorderLayout());
		instructText.setBackground(orange);
		instructText.setEditable(false);
		instructText.setLineWrap(true);
		instructText.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		JScrollPane scrollPane = new JScrollPane(instructText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add("Center", scrollPane);
		getContentPane().add("South", bottom);
		bottom.add("South",ok);

		ok.addActionListener( 
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			}
		);

		ok.addKeyListener( 
			new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					if (((int)e.getKeyChar() == 10) || ((int)e.getKeyChar() == 13))
					dispose();
				}
			}
		);

		instructText.addFocusListener (
			new FocusListener() {
				public void focusGained(FocusEvent e) {
    					ok.requestFocus();
				}
				public void focusLost(FocusEvent e) {}
			}
		);

		setSize(590, 250);
		setResizable(true);
		center();
		setVisible(true);
	}
} // End of class instructionsDialog
