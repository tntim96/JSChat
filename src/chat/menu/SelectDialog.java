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

import sun.misc.BASE64Encoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

public class SelectDialog extends ChatDialog {

	public SelectDialog(final JFrame parent, String title, boolean modal, String label,
					final File file, final String ext, final byte[] raw) {
		super(parent, title, modal);

		StringBuffer rawHex = new StringBuffer();
		for (int i=0;i<raw.length;i++) {
			if (i!=0) rawHex.append(":");
			String hex = Integer.toHexString(raw[i] & 0xff);
			if (hex.length()==1) rawHex.append("0");
			rawHex.append(hex);
		}

		BASE64Encoder enc = new BASE64Encoder();
		String base64 = enc.encode(raw);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(1,4,1,4);
		c.anchor = GridBagConstraints.EAST;
		getContentPane().setLayout(gridbag);

		JLabel jLabel = new JLabel(label+" {"+file.getName()+"}");
		c.gridwidth = 2;
		c.gridx = 0;c.gridy = 0;
		getContentPane().add(jLabel, c);

		JButton jb = new JButton("Save Raw");
		c.gridwidth = 1;
		c.gridx = 2;c.gridy = 0;
		getContentPane().add(jb, c);

		JLabel jLabelB64 = new JLabel("Base64");
		c.gridx = 0;c.gridy = 1;
		getContentPane().add(jLabelB64, c);

		JTextField jtfB64 = new JTextField(base64);
		jtfB64.setBackground(Color.white);
		jtfB64.setEditable(false);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		c.gridx = 1;c.gridy = 1;
		getContentPane().add(jtfB64, c);

		JLabel jLabelHex = new JLabel("Hex");
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridx = 0;c.gridy = 2;
		getContentPane().add(jLabelHex, c);

		JTextField jtfHex = new JTextField(rawHex.toString());
		jtfHex.setBackground(Color.white);
		jtfHex.setEditable(false);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 2;
		c.gridx = 1;c.gridy = 2;
		getContentPane().add(jtfHex, c);

		jb.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                        fc.setDialogTitle("Choose file");
                        fc.setSelectedFile(new File(file.getName() + "." + ext));
                        int returnValue = fc.showSaveDialog(parent.getContentPane());
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            try {
                                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                                fos.write(raw);
                                fos.close();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }
                    }
                }
        );

		pack();
		setResizable(false);
		center();
		setVisible(true);
	}

	public void doClose() {dispose();}
}
