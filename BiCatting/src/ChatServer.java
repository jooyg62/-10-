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
		setTitle("서버");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//창이 닫히면 Default로 수행할 작업을 정해라.
		setSize(300, 300);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());// Default가 BorderLayout이지만 명료하게 하기위해
										// 작성함.
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
		clientMessage.setEditable(false);// 수정 불가능하도록
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
			clientMessage.setText(t + "클라이언트 연결됨");
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
				int pos = this.getText().length();//커서의 길이를 알아서
				this.setCaretPosition(pos);//커서를 맨 우측으로 이동
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
