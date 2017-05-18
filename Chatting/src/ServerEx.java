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
			System.out.println("클라이언트 연결됨");
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			scanner = new Scanner(System.in);//in은 inputStream 바이트 단위이지만 Scanner class가 문자를 바이트로 바꿔줌.
			
			String line;
			while(true) {
				line = in.readLine();// \n을 만나면 한 라인이구나 하고 버퍼는 인식을 함. 읽으면 \n을 떼고 준다.
				System.out.println(line);
				
				if(line.equalsIgnoreCase("bye"))//equalsIgnoreCase 대소문자 구분없이 비교한다.
					break;
				System.out.print(">>");
				String msg = scanner.nextLine();// \n이 들어올때까지 통째로 보내줌. 넘겨줄 때는 \n을 빼서 넘겨준다.
				out.write(msg+"\n");
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // finally를 정상적인경우일 때도 예외일 때도 거친다.
			try {
				socket.close();
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}

	}

}
