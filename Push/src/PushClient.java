import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PushClient extends JFrame {
	private Socket socket = null;
	private BufferedReader in = null;
	private Receiver text = new Receiver();
	private JButton startBtn = new JButton("시작");

	public PushClient() {
		super("클라이언트");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new JScrollPane(text), BorderLayout.CENTER);
		c.add(startBtn, BorderLayout.SOUTH);
		startBtn.addActionListener(new MyActionListener());

		setVisible(true);
	}

	public boolean setup() {
		try {
			socket = new Socket("localhost", 9999);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean b = setup();
			if (b == true) {
				new Thread(text).start();
				((JButton)e.getSource()).setEnabled(false);
			}
		}

	}

	class Receiver extends JTextArea implements Runnable {
		public Receiver() {
			// this.setEditable(false);
		}

		public void run() {
			while(true) {
				try {
					String msg = in.readLine();
					this.append(msg+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new PushClient();
	}
}
