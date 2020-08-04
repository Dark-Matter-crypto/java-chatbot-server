package csc2b.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    public static void runBot() {

        try {
            ServerSocket ss = new ServerSocket(8080);
            System.out.println("Ready for clients to connect. . .");
            Socket clientConnection = ss.accept();

            //Set up streams
            BufferedReader br = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            PrintWriter pw = new PrintWriter(clientConnection.getOutputStream());

            //Pattern for question starting with Why
            Pattern query1Pattern = Pattern.compile("^ASK\\sWhy\\s[(\\w+)(\\s)(?)]+");
            //Pattern for question starting with Are
            Pattern query2Pattern = Pattern.compile("^ASK\\sAre\\s[(\\w+)(\\s)(?)]+");
            //Pattern for any other question
            Pattern query3Pattern = Pattern.compile("^ASK\\s[(\\w+)(\\s)(?)]+");

            String userCommand;
            pw.println("HELLO - You may ask me 4 questions");
            pw.flush();
            userCommand = br.readLine();
            
            boolean ongoing = true;

            do{
                if (userCommand.equals("HELLO BOT")){
                    pw.println("ASK me a question or DONE");
                    pw.flush();
                    Random rand = new Random();
                    int count = 1;

                    for(int i = 1; i < 5; i++){
                        userCommand = br.readLine();
                        Matcher whyMatcher = query1Pattern.matcher(userCommand);
                        Matcher areMatcher = query2Pattern.matcher(userCommand);
                        Matcher anyMatcher = query3Pattern.matcher(userCommand);

                        if(userCommand.equals("DONE")){
                            pw.println("0" + count + " OK BYE - " + (count-1) + " questions answered");
                            pw.flush();
                            ongoing = false;
                            break;
                        }
                        else if(anyMatcher.matches() && (userCommand.contains("covid") || userCommand.contains("virus"))){
                            pw.println("0" + count + " Please see sacoronavirus.co.za.");
                            pw.flush();
                        }
                        else if (whyMatcher.matches()){
                            pw.println("0" + count + " Because the boss says so - see Ulink");
                            pw.flush();
                        }
                        else if (areMatcher.matches()){
                            int randNum = rand.nextInt(3);
                            switch(randNum){
                                case 0:
                                    pw.println("0" + count + " Maybe");
                                    pw.flush();
                                    break;
                                case 1:
                                    pw.println("0" + count + " No");
                                    pw.flush();
                                    break;
                                case 2:
                                    pw.println("0" + count + " Yes");
                                    pw.flush();
                                    break;
                            }
                        }
                        else{
                            int randNum = rand.nextInt(3);
                            switch(randNum){
                                case 0:
                                    pw.println("0" + count + " Meh");
                                    pw.flush();
                                    break;
                                case 1:
                                    pw.println("0" + count + " Oh ok!");
                                    pw.flush();
                                    break;
                                case 2:
                                    pw.println("0" + count + " Escusez-moi?");
                                    pw.flush();
                                    break;
                            }
                        }
                        count++;
                    }

                    if (count > 4){
                        pw.println("HAPPY TO HAVE HELPED - 4 Questions answered");
                        pw.flush();
                        ongoing = false;
                    }
                }
                else{
                    pw.println("Unrecognized command. Type 'HELLO BOT' to ask me questions.");
                    pw.flush();
                    userCommand = br.readLine();
                }
                
            }while (ongoing);

        } catch (IOException e) {
            System.err.println("Unable to bind to port.");
        }

    }
}
