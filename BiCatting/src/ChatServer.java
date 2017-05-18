import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatServer extends JFrame {
	JTextField serverMessage = new JTextField();
	Receiver clientMessage = new Receiver();
	ServerSocket listener = null;
	Socket socket = null;
	BufferedReader in = null;
	BufferedWriter out = null;

	public ChatServer() {
		setTitle("����");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//â�� ������ Default�� ������ �۾��� ���ض�.
		setSize(300, 300);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());// Default�� BorderLayout������ ����ϰ� �ϱ�����
										// �ۼ���.
		serverMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField me = (JTextField) e.getSource();
				String text = me.getText();
				try {
					out.write(text + "\n");
					out.flush();
					me.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});
		clientMessage.setEditable(false);// ���� �Ұ����ϵ���
		c.add(new JScrollPane(clientMessage), BorderLayout.CENTER);
		c.add(serverMessage, BorderLayout.SOUTH);
		setVisible(true);
		
		Thread th = new Thread(clientMessage); // JTextArea
		setupConnection();
		th.start();
	}

	void setupConnection() {
		try {
			listener = new ServerSocket(9999);
			socket = listener.accept();
			String t = clientMessage.getText();
			clientMessage.setText(t + "Ŭ���̾�Ʈ �����");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Receiver extends JTextArea implements Runnable {
		public void run() {
			while(true){
			try {
				String line = in.readLine();// blocking call
				this.append("\n" + line);
				int pos = this.getText().length();//Ŀ���� ���̸� �˾Ƽ�
				this.setCaretPosition(pos);//Ŀ���� �� �������� �̵�
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}

}
