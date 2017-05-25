import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DicClientFrame extends JFrame {
	private JTextField eng = new JTextField(10);
	private JTextField kor = new JTextField(10);
	private JButton searchBtn = new JButton("검색");
	private BufferedReader in = null;
	private BufferedWriter out = null;

	public DicClientFrame() {
		setSize(300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("영어"));
		c.add(eng);
		c.add(new JLabel("한글"));
		c.add(kor);
		c.add(searchBtn);

		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String engWord = eng.getText();
				try {
					out.write(engWord+"\n");
					out.flush();
					String korWord = in.readLine();
					kor.setText(korWord);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		setVisible(true);

		setup();
	}

	private void setup() {
		try {
			Socket socket = new Socket("localhost", 9999);
			setTitle("연결");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new DicClientFrame();

	}

}
