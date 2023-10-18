import java.io.*;
import java.net.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;	
import java.net.http.HttpClient;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MathServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public MathServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        System.out.println("Server Listening on port " + port + "....");
		}
	private static char[] generatePassword(int length) {
      String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
      String specialCharacters = "!@#$";
      String numbers = "1234567890";
      String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
      Random random = new Random();
      char[] password = new char[length];

      password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
      password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
      password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
      password[3] = numbers.charAt(random.nextInt(numbers.length()));
   
      for(int i = 4; i< length ; i++) {
         password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
      }
      return password;
   }

    public static void main(String argv[]) throws Exception {
        MathServer mathServer = new MathServer(3500);
        while (true) {
			Socket client = mathServer.serverSocket.accept();
			DataOutputStream dos = new DataOutputStream(client.getOutputStream());
			HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://facts-by-api-ninjas.p.rapidapi.com/v1/facts"))
			.header("X-RapidAPI-Key", "c411a227a8mshe33324fedb7ec72p114947jsndc89e2730ea9")
			.header("X-RapidAPI-Host", "facts-by-api-ninjas.p.rapidapi.com")
			.method("GET", HttpRequest.BodyPublishers.noBody())
			.build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			String jsonResponse = response.body();
			int startIndex = jsonResponse.indexOf("\"fact\":") + 9; 
			int endIndex = jsonResponse.indexOf("\"}", startIndex); 
			String fact = jsonResponse.substring(startIndex, endIndex);
			fact = fact.trim();
			dos.writeBytes("Fun fact!");
			dos.writeBytes(fact);
			dos.writeBytes("\nEnter 1 to generate a random password, 2 for password strength checker, 3 to quit\n");
			dos.flush();
			ClientHandler clientSock = new ClientHandler(client);
			new Thread(clientSock).start();
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
				File f1 = new File("generated_passwords.txt");
				if(!f1.exists()) {
					f1.createNewFile();
				}

				FileWriter fileWritter = new FileWriter(f1.getName(),true);
				BufferedWriter bw = new BufferedWriter(fileWritter);
				String passHint = br.readUTF();
				int x = br.readInt();
				bw.write("Hint for password: ");
				bw.write(passHint);
				bw.write("\n");
				bw.write("Password: ");
				bw.write(generatePassword(x));
				bw.write("\n");
				bw.close();
				dos.writeBytes("Password saved to generated_passwords.txt");
				dos.flush();
				}
				else if (choice == 2)	{
				dos.writeBytes("2 was selected\n");
				dos.flush();

				int passSize = br.readInt();
				String passWord = br.readUTF();
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