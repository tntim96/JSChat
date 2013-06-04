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
package chat.io.text;

import chat.Gui;
import chat.State;
import chat.io.file.FileThread;
import chat.menu.CryptMenu;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;

/** Text communication and signalling */
public class TalkThread implements Runnable {
	protected static int count;
	protected final int id;
	/** Event Listeners */
	protected EventListenerList listenerList = new EventListenerList();
	/** Communications Socket (Client AND Server) */
	protected Socket socket;
	protected String remoteIP;
	/** Talk Buffered Reader - public so Gui can close this */
	private DataInputStream inputStream=null;
	/** Talk Print Stream - public so Gui send text and close this */
	private DataOutputStream outputStream=null;
	private boolean connected = false;
	private File file;
	/** Number of bytes previously transmitted */
	private long bytesTx;
	
	private byte[] firstHalf = new byte[64];
	private byte[] secondHalf = new byte[64];
	private byte[] iv = new byte[8];
	IvParameterSpec spec;
	SecretKey fileSessionKey;
	private Cipher fileEncrypter, fileDecrypter;

	/** Text communication constructor */
	public TalkThread(Socket socket){
		this.socket = socket;
		remoteIP = socket.getInetAddress().getHostAddress().toString();
		id = ++count;
	}

	protected void createStreams(){
		try{
			outputStream = new DataOutputStream(socket.getOutputStream());
			inputStream  = new DataInputStream(socket.getInputStream());
			connected = true;
		} catch(IOException ioe) {
			fireMessageEvent(MessageListener.FAILED, "Creation of Object I/O Streams failed",null);
		}
	}

	public void disconnect() {
		connected = false;
		try {if(inputStream!=null)inputStream.close();}catch(IOException ioe){}
		try {if(outputStream!=null)outputStream.close();}catch(IOException ioe){}
		try {if(socket!=null)socket.close();}catch(IOException ioe){}
	}

	public synchronized void sendMessage(String text) {
		try {
			outputStream.write(Message.MESSAGE);
			outputStream.writeUTF(text);
			System.out.print("Sent "+text);
			outputStream.flush();
			System.out.println(" - flushed");
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		}
	}

	public synchronized void wakeUp() {
		try {
			outputStream.write(Message.WAKE);
			outputStream.writeUTF("WAKE UP!!!");
			outputStream.flush();
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		}
	}

	public synchronized void cancelXfer() {
		try {
			outputStream.write(Message.CANCEL_FXFER);
			outputStream.flush();
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		}
	}

	public synchronized void keysLoaded() {
		try {
			outputStream.write(Message.KEYS_LOADED);
			outputStream.flush();
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		}
	}

	public synchronized void secureChannelInit() {
		try {
			outputStream.write(Message.SECURE_INIT);
			System.out.println("SecureRandom");
			SecureRandom sr = new SecureRandom();
			sr.nextBytes(firstHalf);
			sr.nextBytes(iv);
			System.out.println("Cipher");
			Cipher cipher = CryptMenu.getAsymmetricCipher();
			cipher.init(Cipher.ENCRYPT_MODE, CryptMenu.publicKey);
			byte[] ciphertext = cipher.doFinal(concatenate(firstHalf, iv));
			System.out.println("Sent ciphertext.length="+ciphertext.length);
			outputStream.writeInt(ciphertext.length);
			outputStream.write(ciphertext);
			outputStream.flush();
			System.out.println("Sent 1st half");
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		} catch(Exception e) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, e.getMessage(),null);//"Security Exception???");
			e.printStackTrace();
		}
	}

	private synchronized void secureChannelResp(byte[] ciphertext) {
		try {
			outputStream.write(Message.SECURE_RESP);
			//Allow remote user to start generating their half of key
			outputStream.flush();
			System.out.println("Sent ciphertext.length="+ciphertext.length);
			outputStream.writeInt(ciphertext.length);
			outputStream.write(ciphertext);
			outputStream.flush();
			System.out.println("Sent 2nd half");
			setupCipherStreams(firstHalf, secondHalf, iv);
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		} catch(Exception e) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, e.getMessage(),null);//"Security Exception???");
			e.printStackTrace();
		}
	}

	public synchronized void sendFile(File file) {
		try {
			this.file = file;
			outputStream.write(Message.SEND_FILE);
			outputStream.writeUTF(file.getName());
			outputStream.writeLong(file.length());
			outputStream.flush();
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		}
	}
	
	private synchronized void sendFileLength(long fileLength) {
		try {
			outputStream.write(Message.SEND_FILE_LENGTH);
			outputStream.writeLong(fileLength);
			outputStream.flush();
			if (State.weAreServer) {
				outputStream.write(Message.FILE_LISTEN);
				outputStream.flush();
			}
		} catch(IOException ioe) {
			connected = false;
			fireMessageEvent(MessageListener.LOST, "Connection terminated",null);
		}
	}

	/** Start text communication */
	public void run() {
		createStreams();
		fireMessageEvent(MessageListener.ESTABLISHED, "Connected to "+remoteIP,null);
		while (connected) {
			try {
				Message m = new Message();
				m.code = inputStream.readByte();
				System.out.println("Got code "+m.code);
				switch(m.code){
					case Message.MESSAGE:
					case Message.WAKE:
						m.text = inputStream.readUTF();
						fireMessageEvent(MessageListener.MESSAGE, "Message received",m);
						break;
					case Message.CANCEL_FXFER:
						fireMessageEvent(MessageListener.MESSAGE, "Transfer cancelled remotely",m);
						break;
					case Message.KEYS_LOADED:
						fireMessageEvent(MessageListener.MESSAGE, "Remote keys loaded",m);
						break;
					case Message.SECURE_INIT: 
						fireMessageEvent(MessageListener.MESSAGE, "Securing channel",m);
						synchronized(this) {
							System.out.println("SecureRandom");
							(new SecureRandom()).nextBytes(secondHalf);
							int length = inputStream.readInt();
							System.out.println("Got 1st half, length="+length);
							byte[] remoteEncrypted = new byte[length];
							inputStream.readFully(remoteEncrypted);
							Cipher cipher = CryptMenu.getAsymmetricCipher();
							cipher.init(Cipher.DECRYPT_MODE, CryptMenu.privateKey);
							System.out.println("After cipherinit");
							byte[] decrypted = cipher.doFinal(remoteEncrypted);
							System.out.println("After do final");
							System.arraycopy(decrypted, 0, firstHalf, 0, firstHalf.length);
							System.arraycopy(decrypted, firstHalf.length, iv, 0, iv.length);
							cipher.init(Cipher.ENCRYPT_MODE, CryptMenu.publicKey);
							byte[] ciphertext = cipher.doFinal(concatenate(secondHalf, iv));
							secureChannelResp(ciphertext);
						}
						break;
					case Message.SECURE_RESP:
						synchronized(this) {
							int length = inputStream.readInt();
							System.out.println("Got 2nd half, length="+length);
							byte[] remoteEncrypted = new byte[length];
							inputStream.readFully(remoteEncrypted);
							Cipher cipher = Cipher.getInstance("RSA","BC");
							cipher.init(Cipher.DECRYPT_MODE, CryptMenu.privateKey);
							byte[] decrypted = cipher.doFinal(remoteEncrypted);
							System.arraycopy(decrypted, 0, secondHalf, 0, secondHalf.length);
							setupCipherStreams(firstHalf, secondHalf, iv);
						}
						break;
					case Message.SEND_FILE: {
							String fileName = inputStream.readUTF();
							long fileLength = inputStream.readLong();
							JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
							fc.setSelectedFile(new File(fileName));
							int returnValue;
							do {
								returnValue = fc.showSaveDialog(Gui.jFrame.getContentPane());
							} while (returnValue!=JFileChooser.APPROVE_OPTION);
							File file = fc.getSelectedFile();
							sendFileLength(fc.getSelectedFile().length());
							System.out.println("Receiving file");
							try {
								fileDecrypter.init(Cipher.DECRYPT_MODE, fileSessionKey, spec);
							} catch (NullPointerException npe) {}
							FileThread ft = new FileThread(file, fileLength, fileDecrypter, false);
							fireMessageEvent(MessageListener.FILE_XFER, "Start receiving file",ft);
							Thread t = new Thread(ft);
							t.start();
						}
						break;
					case Message.SEND_FILE_LENGTH: {
							bytesTx = inputStream.readLong();
							System.out.println("Got bytesTx="+bytesTx);
							if (State.weAreServer) {
								//We are sending and we are server
								//If not server, wait for FILE_LISTEN code
								try {
									fileEncrypter.init(Cipher.ENCRYPT_MODE, fileSessionKey, spec);
								} catch (NullPointerException npe) {}
								FileThread ft = new FileThread(file, bytesTx, fileEncrypter, true);
								fireMessageEvent(MessageListener.FILE_XFER, "Server start sending file",ft);
								Thread t = new Thread(ft);
								t.start();
							}
						}
						break;
					case Message.FILE_LISTEN: {
							//We are sending but we aren't server
							//Other side is listening for connection so connect
							try {
								fileEncrypter.init(Cipher.ENCRYPT_MODE, fileSessionKey, spec);
							} catch (NullPointerException npe) {}
							FileThread ft = new FileThread(file, bytesTx, fileEncrypter,true);
							fireMessageEvent(MessageListener.FILE_XFER, "Client start sending file",ft);
							Thread t = new Thread(ft);
							t.start();
						}
						break;
				}
			} catch(IOException ioe) {
				connected = false;
				fireMessageEvent(MessageListener.LOST, ioe.getMessage(),null);//"Connection terminated");
			} catch(Exception e) {
				connected = false;
				fireMessageEvent(MessageListener.LOST, e.getMessage(),null);//"Security Exception???");
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected() {return connected;}
	public String getRemoteIP() {return remoteIP;}

	public void addMessageListener(MessageListener l) {
		listenerList.add(MessageListener.class, l);
	}

	public void removeMessageListener(MessageListener l) {
		listenerList.remove(MessageListener.class, l);
	}

	protected void fireMessageEvent(byte code, String text, Object o) {
		Object[] listeners = listenerList.getListenerList();
		MessageEvent e = new MessageEvent(this,code,text,o);
		for (int i = listeners.length - 2;i >= 0;i-=2) {
			((MessageListener)listeners[i+1]).messageEvent(e);
		}
	}

	public String toString() {
		return "Connection "+id;
	}

	protected byte[] concatenate(byte[] a, byte[] b) {
		byte[] r =  new byte[a.length + b.length];
		System.arraycopy(a, 0, r, 0, 				a.length);
		System.arraycopy(b, 0, r, a.length, b.length);
		return r;
	}

	protected synchronized void setupCipherStreams(byte[] firstHalf, byte[] secondHalf, byte[] iv) throws Exception {
		System.out.println("Setting up cipher streams");
		byte[] keyBytes = concatenate(firstHalf, secondHalf);
		SecretKey sessionKey = CryptMenu.getSecretKey(keyBytes);
		spec = new IvParameterSpec(iv);
		Cipher ciphers[] = CryptMenu.getStreamCiphers(sessionKey);
		Cipher encrypter = ciphers[0];
		Cipher decrypter = ciphers[1];

		CipherOutputStream out = new CipherOutputStream(socket.getOutputStream(), encrypter);
		CipherInputStream in = new CipherInputStream(socket.getInputStream(), decrypter);
		outputStream = new DataOutputStream(out);
		inputStream  = new DataInputStream(in);
		System.out.println("Cipher streams set up");
		fireMessageEvent(MessageListener.SECURED, "Channel secured",sessionKey);

		//Make file session key with exchanged keyBytes
		fileSessionKey = CryptMenu.getFileSessionKey(keyBytes);
		ciphers = CryptMenu.getBlockCiphers();
		fileEncrypter = ciphers[0];
		fileDecrypter = ciphers[1];
	}
}
