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
			System.out.println("서버에 연결됨");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			scanner = new Scanner(System.in);//in은 inputStream 바이트 단위이지만 Scanner class가 문자를 바이트로 바꿔줌.
			
			String line;
			while(true) {
				System.out.print(">>");
				String msg = scanner.nextLine();
				out.write(msg+"\n");//보내줘야 상대방이 \n을 받아서 인식을 하니까.
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
			}// socket은 system자원이기 때문에 port를 물고 있다.
		}

	}

}
