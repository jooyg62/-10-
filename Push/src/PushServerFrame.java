import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PushServerFrame extends JFrame {
	private int deliveredCount = 0;
	private int threadCount = 0;
	private MyTextField text = new MyTextField(10);
	private JLabel clientCountLabel = new JLabel("0");
	private JLabel deliveredCountLabel = new JLabel("0");
	private JTextArea clientList = new JTextArea(7, 30);

	public PushServerFrame() {
		setTitle("서버");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);

		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("텍스트"));
		c.add(text);
		c.add(new JLabel("접속자수"));
		c.add(clientCountLabel);
		c.add(new JLabel("전송한수"));
		c.add(deliveredCountLabel);
		c.add(new JScrollPane(clientList));
		setVisible(true);
		
		//서버 스레드 작동 시작
		ServerThread th = new ServerThread();
		th.start();
	}

	class ServerThread extends Thread {
		public void run() {
			ServerSocket listener = null;
			Socket socket = null;
			try {
				listener = new ServerSocket(9999);
			} catch (IOException e) {
				e.printStackTrace();
			}

			while (true) {
				try {
					socket = listener.accept();
					clientList.append("client 접속\n");
					ServiceThread th = new ServiceThread(socket);
					th.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class ServiceThread extends Thread {
		private Socket socket = null;
		public ServiceThread(Socket socket) {
			this.socket = socket;
			threadCount++;
			clientCountLabel.setText(Integer.toString(threadCount));
			text.increaseDeliveredCount();
		}

		public void run() {
			try {
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				while(true) {
					String msg = text.get();
					clientList.append(msg+"\n");
					out.write(msg+"\n");
					out.flush();
				}
			} catch(java.net.SocketException e) {
				//상대 소켓이 닫힌 경우
				threadCount--;
				clientCountLabel.setText(Integer.toString(threadCount));
				text.decreaseDeliveredCount();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	class MyTextField extends JTextField {
		public MyTextField(int n) {
			super(n);
			this.addActionListener(new ActionListener(){ // <Enter> 키
				
				public void actionPerformed(ActionEvent e) {
					put();
				}
			});
		}
		private void clearDeliveredCount() {
			deliveredCount = 0;
			deliveredCountLabel.setText("0");
		}
		
		private void increaseDeliveredCount() {
			deliveredCount++;
			deliveredCountLabel.setText(Integer.toString(deliveredCount));
		}
		private void decreaseDeliveredCount() {
			deliveredCount--;
			deliveredCountLabel.setText(Integer.toString(deliveredCount));
		}
		
		public synchronized void put() {
			if(deliveredCount != threadCount) {
				try {
					this.wait(); // wait for notify()
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			clearDeliveredCount();
			this.notifyAll(); // wait()하고 있는 모든 스레드 깨움.
		}
		
		public synchronized String get() {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			increaseDeliveredCount();
			if(deliveredCount == threadCount) {
				notify();
			}
			
			return this.getText();
		}


	}

	public static void main(String[] args) {
		new PushServerFrame();

	}

}
