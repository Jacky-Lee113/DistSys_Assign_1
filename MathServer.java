import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathServer {
    private ServerSocket serverSocket;

    public MathServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        System.out.println("Server Listening on port " + port + "....");
    }

   public static int summation(int a, int b) {
	   int summ = 0;
	    if (a > b)	{
			summ = b;
		  while (a > b)	{
			  summ += a;
			  a--;
		  }
		   return summ;
	  }
	  else if (b > a)	{
		  summ = a;
		  while (b > a)	{
			  summ += b;
			  b--;
		  }
		  return summ;
	  }
	  else 	{
		  return a + b;
	  }
   }

    public static void main(String argv[]) throws Exception {
        MathServer mathServer = new MathServer(3500);
        while (true) {
			Socket client = mathServer.serverSocket.accept();
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			System.out.println("New client connected: " + client.getInetAddress().getHostAddress());
			ClientHandler clientSock = new ClientHandler(client);
			new Thread(clientSock).start();
			dos.writeBytes("Enter 1 for calculator, 2 for tester, 3 to quit\n");
        }
    }


    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private DataInputStream br;
        private DataOutputStream dos;

        public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            this.br = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void run() {
            try {
                br = new DataInputStream(clientSocket.getInputStream());
                dos = new DataOutputStream(clientSocket.getOutputStream());
				
				int choice = br.readInt();
                if (choice == 1)	{
				dos.writeBytes("1 was selected");
				dos.writeBytes("\n");
				dos.flush();
				int x = br.readInt();
                System.out.println("I got: " + x);
                int y = br.readInt();
                System.out.println("I got: " + y);
                int ans = summation(x, y);
                System.out.println("The summation is: " + ans + "\n");
                System.out.println("I am sending the average...");
				int avg;
                if (x == y) {
                    avg = ans / x;
                } else if (x > y) {
                    avg = ans / (x - y + 1);
                } else {
                    avg = ans / (y - x + 1);
                }
					dos.writeBytes("The average is: " + avg);
					dos.flush();
				}
				else if (choice == 2)	{
				dos.writeBytes("2 was selected");
				dos.flush();
				}
				else	{
				System.out.println("Server connection terminated");
				dos.writeBytes("Server connection has been terminated");
                dos.close();
                clientSocket.close();
				System.exit(0);
				}
            } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                dos.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
        }
    }
}