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
package chat.io.file;

import chat.menu.ChatDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

/** Progress and cancel dialog for file transfers */
//public class FileXferDialog extends ChatDialog implements Runnable {
	public class FileXferDialog extends ChatDialog {
	private boolean stillXfering = true; 
	long bytesTx = 0;
	long bytesTxLast = 0;
	long bytesTotal = 0;
	long bytesTxStart = 0;
	
	long timeStart, timeLast, timeNow, timeDiff;
	String titleG = "";
	JButton cancel;
	JLabel labelFinish, labelTime, labelTimeLeft, labelRateAv, labelRateCurr, labelPc, labelSent;
	JProgressBar progress;

	/** Constructor */
	public FileXferDialog(JFrame parent, String title, long fileLength, long bytesTxStart) {
		super(parent, title, false);
		bytesTotal = fileLength;
		this.bytesTxStart = bytesTxStart;
		timeStart = timeLast = timeNow = System.currentTimeMillis();

		getContentPane().setLayout(new BorderLayout());
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new GridLayout(4,1));
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		JPanel panelSouth = new JPanel();

		JPanel panelFinish = new JPanel();
		JPanel panelTime = new JPanel();
		JPanel panelTimeL = new JPanel();
		JPanel panelTimeR = new JPanel();
		JPanel panelRate = new JPanel();
		JPanel panelRateL = new JPanel();
		JPanel panelRateR = new JPanel();
		JPanel panelSent = new JPanel();
		JPanel panelSentL = new JPanel();
		JPanel panelSentR = new JPanel();
		JPanel panelProg = new JPanel();
		JPanel panelCanc = new JPanel();
		panelFinish.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelTime.setLayout(new GridLayout(1,2));
		panelTimeL.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelTimeR.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelTime.add(panelTimeL);
		panelTime.add(panelTimeR);
		panelRate.setLayout(new GridLayout(1,2));
		panelRateL.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelRateR.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelRate.add(panelRateL);
		panelRate.add(panelRateR);
		panelSent.setLayout(new GridLayout(1,2));
		panelSentL.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSentR.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSent.add(panelSentL);
		panelSent.add(panelSentR);
		panelProg.setLayout(new BorderLayout());
		panelCanc.setLayout(new FlowLayout(FlowLayout.CENTER));

		panelNorth.add(panelFinish);
		panelNorth.add(panelTime);
		panelNorth.add(panelRate);
		panelNorth.add(panelSent);
		panelCenter.add("Center",panelProg);
		panelSouth.add(panelCanc);

		getContentPane().add("North",panelNorth);
		getContentPane().add("Center",panelCenter);
		getContentPane().add("South",panelSouth);

		labelFinish = new JLabel("Finish time: Calculating");
		panelFinish.add(labelFinish);

		labelTime = new JLabel("Time taken: Calculating");
		panelTimeL.add("West",labelTime);
		labelTimeLeft = new JLabel("Time left: Calculating");
		panelTimeR.add("East",labelTimeLeft);

		labelRateCurr = new JLabel("Current Rate: Calculating K");
		panelRateL.add("West",labelRateCurr);
		labelRateAv = new JLabel("Average Rate: Calculating K");
		panelRateR.add("East",labelRateAv);
		
		labelPc = new JLabel("0 %");
		panelSentL.add(labelPc);

		String sentString = "Sent: Calculating Bytes";
		labelSent = new JLabel(sentString);
		panelSentR.add(labelSent);

		progress = new JProgressBar();
		progress.setMaximum((int)fileLength);
		progress.setMinimum(0);
		progress.setValue((int)bytesTx);
		progress.setBorderPainted(true);
		panelProg.add("West",new JLabel(" "));
		panelProg.add("Center",progress);
		panelProg.add("East",new JLabel(" "));

		cancel = new JButton(" Cancel ");
		panelCanc.add(cancel);
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					stillXfering = false;
					dispose();
				}
			}
		);
		addWindowListener (
			new WindowListener() {
				public void windowActivated(WindowEvent e) {}
				public void windowClosed(WindowEvent e) {}
				public void windowClosing(WindowEvent e) {stillXfering = false;dispose();}
				public void windowDeactivated(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {}
				public void windowIconified(WindowEvent e) {}
				public void windowOpened(WindowEvent e) {}
			}
		);

		//setSize(440, 200);
		pack();
		setResizable(false);
		center();
		setVisible(true);
	}

	/** Is the file tranfer still going */
	public boolean stillXfering() {return stillXfering;}

	/** Kill this Gui */
	public void disposeDisplay()
	{
		//try{Thread.sleep(800);} catch (InterruptedException ie) {}
		//dispose();
		cancel.setText(" Close ");
	}
	
	/** Update progress bar */
	public void updateDisplay(long bytesSent)
	{
		if (!stillXfering)
			dispose();
		bytesTx = bytesSent;
		timeNow = System.currentTimeMillis();

//		if (false) {
		if (((timeNow-timeLast)>1000) && ((bytesTx-bytesTxLast)>1024L)) {
			double rateCurr = (((double)(bytesTx-bytesTxLast))/(double)(timeNow-timeLast));
			String rateCurrS = ""+rateCurr;
			if (rateCurrS.indexOf(".")==-1) {
				rateCurrS = rateCurrS + ".0";
			} else {
				if (rateCurrS.length()>rateCurrS.indexOf(".")+1)
				rateCurrS = rateCurrS.substring(0,rateCurrS.indexOf(".")+2);
			}
			labelRateCurr.setText("Current Rate: "+rateCurrS+" K");
			
			double rateAv = (((double)(bytesTx-bytesTxStart))/(double)(timeNow-timeStart));
			String rateAvS = ""+rateAv;
			if (rateAvS.indexOf(".")==-1) {
				rateAvS = rateAvS + ".0";
			} else {
				if (rateAvS.length()>rateAvS.indexOf(".")+1)
				rateAvS = rateAvS.substring(0,rateAvS.indexOf(".")+2);
			}
			labelRateAv.setText("Average Rate: "+rateAvS+" K");
			
			String timeS;
			int time[] = getTime(timeNow-timeStart);
			timeS = "Time taken: ";
			if (time[0]>0)
				timeS = timeS + time[0] + " H ";
			if (time[1]>0)
				timeS = timeS + time[1] + " m ";
			if (time[2]>0)
				timeS = timeS + time[2] + " s";
			labelTime.setText(timeS);
			
			long timeLeftL = timeStart-timeNow+(long)((timeNow-timeStart)*(bytesTotal-bytesTxStart)/(bytesTx-bytesTxStart));
			int timeLeft[] = getTime(timeLeftL);
			timeS = "Time left: ";
			if (timeLeft[0]>0)
				timeS = timeS + timeLeft[0] + " H ";
			if (timeLeft[1]>0)
				timeS = timeS + timeLeft[1] + " m ";
			if (timeLeft[2]>0)
				timeS = timeS + timeLeft[2] + " s";
			labelTimeLeft.setText(timeS);
			
			labelFinish.setText("Finish time: "+(new Date(timeNow+timeLeftL)));
	
			bytesTxLast = bytesTx;
			timeLast = timeNow;
		} else {
			if (((timeNow-timeLast)>30000) && ((bytesTx-bytesTxLast)==0)) {
				stillXfering = false;
			}
		}

		progress.setValue((int)bytesTx);
		
		double percentDone = ((double)bytesTx/(double)bytesTotal)*100;
		String complete = Integer.toString((int) percentDone);
		labelPc.setText(complete+" %");

		String sentString = "Sent: "+Long.toString(bytesTx)+" / "+Long.toString(bytesTotal)+" Bytes";
		if (bytesTotal >= 102400)
			sentString = "Sent:  "+Long.toString(bytesTx/1024)+" / "+Long.toString(bytesTotal/1024)+" kBytes";
		labelSent.setText(sentString);
		repaint();
	}
	
	private int[] getTime(long time) {
		int[] timeInt = new int[3];
		timeInt[0] = (int)(time/3600000);
		long timeH = timeInt[0]*3600000;
		timeInt[1] = (int)((time-timeH)/60000);
		long timeM = timeInt[1]*60000;
		timeInt[2] = (int)((time-timeH-timeM)/1000);
		return timeInt;
	}

	//public void run() {
		//Start xfer and monitor
	//}
}
