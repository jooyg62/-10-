import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientEx {

	public static void main(String[] args) {
		Socket socket = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		Scanner scanner = null;
		
		try {
			socket = new Socket("localhost",9999);
			System.out.println("������ �����");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			scanner = new Scanner(System.in);//in�� inputStream ����Ʈ ���������� Scanner class�� ���ڸ� ����Ʈ�� �ٲ���.
			
			String line;
			while(true) {
				System.out.print(">>");
				String msg = scanner.nextLine();
				out.write(msg+"\n");//������� ������ \n�� �޾Ƽ� �ν��� �ϴϱ�.
				out.flush();
				if(msg.equalsIgnoreCase("bye"))
					break;
				
				line = in.readLine();
				System.out.println(line);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// socket�� system�ڿ��̱� ������ port�� ���� �ִ�.
		}

	}

}
