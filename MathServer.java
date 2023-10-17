import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MathServer {
    private ServerSocket serverSocket;

    public MathServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        System.out.println("Server Listening on port " + port + "....");
    }

   public static double epoxyToCon(double a, double b) {
	  double force = a * 3.1415 * b * 600;
	  return force;
   }
   public static double epoxyToSteel(double g, double h) {
	  double sforce = g * 3.1415 * h * 1600;
	  return sforce;
   }

    public static void main(String argv[]) throws Exception {
        MathServer mathServer = new MathServer(3500);
        while (true) {
			Socket client = mathServer.serverSocket.accept();
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			System.out.println("New client connected: " + client.getInetAddress().getHostAddress());
			ClientHandler clientSock = new ClientHandler(client);
			new Thread(clientSock).start();
			dos.writeBytes("Enter 1 to calculate the force it takes to remove a bolt in epoxy out of concrete and the force it takes to remove a steel bolt out of epoxy, 2 for password strength checker, 3 to quit\n");
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
                double con = epoxyToCon(x, y);
				int m = br.readInt();
                System.out.println("I got: " + m);
				int l = br.readInt();
                System.out.println("I got: " + l);
				double steel = epoxyToSteel(m, l);
                dos.writeBytes("The amount of force required to pullout the bold seperating at the epoxy to concrete is: " + con + "lbs and the force it takes to pull out a steel bolt out of epoxy is: " + steel + "lbs \n");
                dos.flush();
				}
				else if (choice == 2)	{
				dos.writeBytes("2 was selected\n");
				dos.flush();

				int passSize = br.readInt();
				String passWord = br.readUTF();
				System.out.println(passWord);

				boolean hasLower = false, hasUpper = false, hasDigit = false, specialChar = false;
				for (char i : passWord.toCharArray()) {
					if (Character.isLowerCase(i)) hasLower = true;
					if (Character.isUpperCase(i)) hasUpper = true;
					if (Character.isDigit(i)) hasDigit = true;
					if ("!@#$%^&*()-+".indexOf(i) != -1) specialChar = true;
				}

				if (hasDigit && hasLower && hasUpper && specialChar && (passSize >= 8)) {
					dos.writeBytes("Your password is strong");
					dos.flush();
				} else if ((hasLower || hasUpper || specialChar) && (passSize >= 6)) {
					dos.writeBytes("Your password is moderate");
					dos.flush();
				} else {
					dos.writeBytes("Your password is weak");
					dos.flush();
				}
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