/**
 * @author Qusay H. Mahmoud
 */

import java.io.*;
import java.util.Scanner;
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
		String text = br.readLine();
		System.out.println(text);
		Scanner choice = new Scanner(System.in);  // Create a Scanner object
		int option = choice.nextInt();  // Read user input
		dos.writeInt(option);
		dos.flush();
		String output = br.readLine();
		System.out.println(output);
		if (option == 3)	{
		String term = br.readLine();
		System.out.println(term);
		System.exit(0);
		}
		else	{
		Scanner num1 = new Scanner(System.in);  // Create a Scanner object
		System.out.println("\nEnter first number");
		int x = num1.nextInt();  // Read user input
		dos.writeInt(x);
		dos.flush();
		Scanner num2 = new Scanner(System.in);  // Create a Scanner object
		System.out.println("Enter second number");
		int y = num2.nextInt();  // Read user input
		dos.writeInt(y);
		dos.flush();
		String str = br.readLine();
		System.out.println("I got: "+str);
		}
	  }
   }
}
