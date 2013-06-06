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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InstructionsDialog extends ChatDialog {
	Color orange = new Color(255,204,153);
	JTextArea instructText = new JTextArea(getInstructionText());
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

    private String getInstructionText() {
        try {
            StringBuilder result = new StringBuilder();
            int bufSize = 1024;
            char buf[] = new char[bufSize];
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/instructions.txt")));
            for (int read = 0; (read = br.read(buf)) != -1; ) {
                result.append(buf, 0, read);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
} // End of class instructionsDialog
