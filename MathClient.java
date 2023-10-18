/**
 * @author Qusay H. Mahmoud
 */

import java.io.*;
import java.util.*;
import java.net.*;

public class MathClient {
   public static void main(String argv[]) throws Exception {
     Socket echo;
     DataInputStream br;
     DataOutputStream dos;
	 while (true)	{
		echo = new Socket("localhost", 3500);
		br = new DataInputStream(echo.getInputStream());
		dos = new DataOutputStream(echo.getOutputStream());
		String fact = br.readLine();
		System.out.println(fact);
		String text = br.readLine();
		System.out.println(text);
		Scanner choice = new Scanner(System.in);  // Create a Scanner object
		int option = choice.nextInt();  // Read user input
		dos.writeInt(option);
		dos.flush();
		String output = br.readLine();
		System.out.println(output);
		if (option == 1)	{
			Scanner passCode = new Scanner(System.in);
			System.out.println("\nEnter a hint for the password");
			String gen = passCode.nextLine();
			dos.writeUTF(gen);
			dos.flush();
			Scanner num1 = new Scanner(System.in);  // Create a Scanner object
			System.out.println("\nEnter the size of password you want");
			int x = num1.nextInt();  // Read user input
			dos.writeInt(x);
			dos.flush();
			String str = br.readLine();
			System.out.println(str + "\n");
		}
		else if (option == 2)	{
			Scanner passWord = new Scanner(System.in);  // Create a Scanner object
			System.out.println("\nEnter the password you want to check");
			String input = passWord.nextLine();  // Read user input
			dos.writeInt(input.length());
			dos.flush();
			dos.writeUTF(input);
			dos.flush();
			String str = br.readLine();
			System.out.println(str + "\n");
		}
		else	{
			String term = br.readLine();
			System.out.println(term);
			System.exit(0);		
		}
	  }
   }
}
