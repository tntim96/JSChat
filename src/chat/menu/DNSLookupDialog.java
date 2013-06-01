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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class DNSLookupDialog extends ChatDialog {
	private JTextField inputJText;
	private JTextArea outputJText;
	private JScrollPane scrollPane;

	public DNSLookupDialog(JFrame parent) {
		super(parent, "Enter Host/IP Address", true);
		getContentPane().setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(2,1));
		top.setBorder(BorderFactory.createLineBorder(getContentPane().getBackground(),4));
		JPanel middle = new JPanel();
		middle.setLayout(new BorderLayout());
		middle.setBorder(BorderFactory.createLineBorder(getContentPane().getBackground(),4));
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		setSize(360, 250);
		setResizable(false);

		JLabel labelInput = new JLabel("Enter Host / IP:");
		inputJText = new JTextField();
		inputJText.setBackground(Color.white);
		inputJText.setEditable(true);

		
		outputJText = new JTextArea();
		outputJText.setLineWrap(true);
		outputJText.setBackground(Color.white);
		outputJText.setEditable(false);
		
		JButton go = new JButton("Go");
		JButton exit = new JButton("Exit");

		top.add(labelInput);
		top.add(inputJText);
		scrollPane = new JScrollPane(outputJText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		middle.add(scrollPane);
		bottom.add(go);
		bottom.add(exit);

		getContentPane().add("North",top);
		getContentPane().add("Center",middle);
		getContentPane().add("South",bottom);

		inputJText.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {updateDisplay();}});
		go.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {updateDisplay();}});
		exit.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {doClose();}});

		center();
		setVisible(true);
	}

	public void updateDisplay() {
		outputJText.setText("Calculating.");
		try {
			outputJText.setText("");
			InetAddress addresses[] = InetAddress.getAllByName(inputJText.getText());
			outputJText.append("** Alias\n"+addresses[0].getHostName()+"\n** IP Addresses\n");
			for (int i=0;i<addresses.length;i++)
				outputJText.append(addresses[i].getHostAddress()+"\n");
		} catch (Exception uh) {
			outputJText.setText("Unknown host or IP");
		}
		outputJText.append("\n");
	}
}
