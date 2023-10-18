What this application is:  
It is a random password generator and a password-strength identifier. If the user chooses 1 when prompted, it will start the random password generator. The user will then need to write a hint for the password, in order to keep track of what the password is going to be used for. The user will then input the desired size (character count) which will be sent to the server. The server will then randomly generate a password using special characters, numbers, and upper and lower case letters to the desired size. It will then send the password to the client where it will write it to the file generated_passwords.txt along with the hint. This will act like a history. If the user chooses 2 when prompted, it will start the password-strength identifier. The user will then be prompted to type their password, which will be sent to the server. The server will then analyze the password sent, and will return 1 of 3 values: "Your password is strong", "Your password is moderate" or "Your password is weak". If the user chooses 3 when prompted, it will terminate the connection between the client and server, but keep the server running.
How to run:  
Step 1: Open a linux terminal  
Step 2: Type <java MathServer> into the terminal  
Step 3: Open a new linux terminal  
Step 4: Type <java MathClient> into the terminal  
Step 5: Follow prompts. Generated passwords and their respective hints are written to the client sided txt file <generated_passwords.txt>  
