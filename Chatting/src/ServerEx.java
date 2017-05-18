import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerEx {

	public static void main(String[] args) {
		ServerSocket listener = null;
		Socket socket = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		Scanner scanner = null;
		try {
			listener = new ServerSocket(9999);
			socket = listener.accept();
			System.out.println("Ŭ���̾�Ʈ �����");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			scanner = new Scanner(System.in);//in�� inputStream ����Ʈ ���������� Scanner class�� ���ڸ� ����Ʈ�� �ٲ���.
			
			String line;
			while(true) {
				line = in.readLine();// \n�� ������ �� �����̱��� �ϰ� ���۴� �ν��� ��. ������ \n�� ���� �ش�.
				System.out.println(line);
				
				if(line.equalsIgnoreCase("bye"))//equalsIgnoreCase ��ҹ��� ���о��� ���Ѵ�.
					break;
				System.out.print(">>");
				String msg = scanner.nextLine();// \n�� ���ö����� ��°�� ������. �Ѱ��� ���� \n�� ���� �Ѱ��ش�.
				out.write(msg+"\n");
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // finally�� �������ΰ���� ���� ������ ���� ��ģ��.
			try {
				socket.close();
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}

	}

}
