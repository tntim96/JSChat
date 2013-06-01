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

import chat.Gui;
import chat.State;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.swing.event.EventListenerList;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

/** File transfer */
public class FileThread implements Runnable
{
	/** Event Listeners */
	protected EventListenerList listenerList = new EventListenerList();
	/** Transfer Buffer size */
	private int bufFileXferSize = 8192;	//8K
	byte buf[] =new byte [bufFileXferSize];
	/** Transfer Socket (Client AND Server) */
	protected Socket socket;
	private File file;
	private long fileLength;
	private long bytesTx;
	private int bytesRead;
	private boolean sending;
	private boolean gotFileName;
	private FileXferDialog fileXferDialog;
	private Cipher encrypter, decrypter;
	private boolean cancelled;

	/** File sender constructor */
	public FileThread(File file, long length, Cipher cipher, boolean sending) {
		this.file = file;
		this.sending = sending;
		if (sending) {
			this.fileLength = file.length();
			this.bytesTx = length;
			this.encrypter = cipher;
		} else {
			this.fileLength = length;
			this.bytesTx = file.length();
			this.decrypter = cipher;
		}
	}
	
	/** Start file transfer */
	public void run() {
		try {
			if (State.weAreServer) {
				ServerSocket ss = new ServerSocket(State.filePort);
				//Throw java.io.InterruptedIOException after 1 minute
				ss.setSoTimeout(60000);
				this.socket = ss.accept();//This method in new thread so no blocking
			} else {
				this.socket = new Socket(State.serverIP, State.filePort);
			}
		} catch(ConnectException ce) {
			fireTransferEvent(TransferListener.FAILED, "Connection falied - check your file ports match");
			return;
		} catch(Exception e) {
			e.printStackTrace();
			fireTransferEvent(TransferListener.FAILED, e.getMessage());
			return;
		}

		if (sending)
			doFileTx();
		else
			doFileRx();
	}
	private void doFileTx () {
		DataOutputStream fileOs = null;
		try {
			System.out.println("Setting up sockets");
			if (encrypter!=null) {
				CipherOutputStream out = new CipherOutputStream(socket.getOutputStream(), encrypter);
				fileOs = new DataOutputStream(out);
			} else {
				fileOs = new DataOutputStream(socket.getOutputStream());
			}
			System.out.println("Sockets set up");

			RandomAccessFile fr = new RandomAccessFile(file,"r");
			fr.seek(bytesTx);
			DataInputStream dis = new DataInputStream(new FileInputStream(fr.getFD()));
			//DataInputStream dis = new DataInputStream(new FileInputStream(file));
			//dis.skip(bytesTx);
			fileXferDialog = new FileXferDialog(Gui.jFrame, "File Transfer Status", fileLength, bytesTx);

			System.out.println(" bytesTx="+bytesTx+" fileLength="+fileLength+" bytesRead="+bytesRead);

			while ( (bytesRead=dis.read(buf,0,bufFileXferSize))!=-1
				&& bytesTx<fileLength && fileXferDialog.stillXfering() && !cancelled) {
				Thread.yield();
				bytesTx += bytesRead;
				fileOs.write(buf,0,bytesRead);
				fileOs.flush();
				fileXferDialog.updateDisplay(bytesTx);
				//System.out.print(" bytesTx="+bytesTx+" fileLength="+fileLength+" bytesRead="+bytesRead);
			}
			fileXferDialog.updateDisplay(bytesTx);

			if (bytesTx == fileLength)
				fireTransferEvent(TransferListener.COMPLETE, "Transfer completed");
			else {
				if (!fileXferDialog.stillXfering())
					fireTransferEvent(TransferListener.CANCELLED_LOCAL_DURING, "Transfer stopped");
				else if (!cancelled)
					fireTransferEvent(TransferListener.FAILED, "Transfer bytesTx="+bytesTx+" fileLength="+fileLength);
			}

		} catch(Exception e) {
			e.printStackTrace();
			fireTransferEvent(TransferListener.FAILED, e.getMessage());
		} finally {
			if (fileXferDialog!=null) fileXferDialog.disposeDisplay();
			try {if(fileOs!=null)fileOs.close();}catch(IOException ioe){}
			try {if(socket!=null)socket.close();}catch(IOException ioe){}
		}
	} // End of fileTx()
	
	private synchronized void doFileRx() {
		DataInputStream fileIs = null;
		RandomAccessFile fw = null;
		FileOutputStream fos = null;
		try {
			System.out.println("Setting up sockets");
			if (decrypter!=null) {
				CipherInputStream in = new CipherInputStream(
					new BufferedInputStream(socket.getInputStream(), bufFileXferSize), decrypter);
				fileIs  = new DataInputStream(in);
			} else {
				fileIs = new DataInputStream(new BufferedInputStream(socket.getInputStream(), bufFileXferSize));
			}
			System.out.println("Sockets set up");

			fw = new RandomAccessFile(file,"rw");
			fw.seek(bytesTx);
			fos = new FileOutputStream(fw.getFD());

			fileXferDialog = new FileXferDialog(Gui.jFrame, "File Transfer Status", fileLength, bytesTx);
			System.out.println(" bytesTx="+bytesTx+" fileLength="+fileLength+" bytesRead="+bytesRead);

			while ( (bytesRead=fileIs.read(buf,0,bufFileXferSize))!=-1
				&& bytesTx<fileLength && fileXferDialog.stillXfering() && !cancelled) {
				Thread.yield();
				bytesTx += bytesRead;
				fos.write(buf,0,bytesRead);
				fos.flush();
				fileXferDialog.updateDisplay(bytesTx);
				//System.out.println(" bytesTx="+bytesTx+" fileLength="+fileLength+" bytesRead="+bytesRead);
			}
			fileXferDialog.updateDisplay(bytesTx);

			if (bytesTx == fileLength)
				fireTransferEvent(TransferListener.COMPLETE, "Transfer completed");
			else {
				if (!fileXferDialog.stillXfering())
					fireTransferEvent(TransferListener.CANCELLED_LOCAL_DURING, "Transfer stopped");
				else if (!cancelled)
					fireTransferEvent(TransferListener.FAILED, "Transfer bytesTx="+bytesTx+" fileLength="+fileLength);
			}

		} catch(Exception e) {
			e.printStackTrace();
			fireTransferEvent(TransferListener.FAILED, e.getMessage());
		} finally {
			if (fileXferDialog!=null) fileXferDialog.disposeDisplay();
			try {if(fileIs!=null)fileIs.close();}catch(IOException ioe){}
			try {if(fw!=null)fw.close();}catch(IOException ioe){}
			try {if(fos!=null)fos.close();}catch(IOException ioe){}
			try {if(socket!=null)socket.close();}catch(IOException ioe){}
		}
	} // End of fileRx()

	public void cancel() {cancelled=true;}

	public void addTransferListener(TransferListener l) {
		listenerList.add(TransferListener.class, l);
	}

	public void removeTransferListener(TransferListener l) {
		listenerList.remove(TransferListener.class, l);
	}

	protected void fireTransferEvent(byte code, String text) {
		Object[] listeners = listenerList.getListenerList();
		TransferEvent e = new TransferEvent(this,code,text);
		for (int i = listeners.length - 2;i >= 0;i-=2) {
			((TransferListener)listeners[i+1]).transferEvent(e);
		}
	}
}
