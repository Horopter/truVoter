/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collegevoting;

/**
 *
 * @author Santosh Kumar Desai
 */

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
class Reader
{
	String nameReader(int a) throws URISyntaxException
	{
		String s = a + ".txt";
                URL path = ClassLoader.getSystemResource(s);
                if(path==null) 
                {
                        System.out.println("Path not found");
                        System.out.println(new File("."));
                        //The file was not found, insert error handling here
                }
                File f = new File(path.toURI());
		//File f = new File(s);
		try
		{
                    try (Scanner in = new Scanner(f)) {
                        while (in.hasNextLine())
                        {
                            String name = in.nextLine();
                            return name;
                        }
                    }
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	int readNumber()
	{		
		Scanner in = new Scanner(System.in);
		String line = in.nextLine();
		if (line.length() == 1)
		{
	                try
                        {
                            int digit = Integer.parseInt(line);
                            return digit;
	                }
	                catch (NumberFormatException e)
					{
						System.out.println("Symbol encountered! Enter a single digit number");
	                }
			
		}
		else
		{
			System.out.println("Enter a single digit number!");
		}
		return 100;
	}
}
class Voting
{
	private final String Passwd = "Santosh";
	public static void clearScr()
	{
		try
		{
			Runtime r = Runtime.getRuntime();
			r.exec("cls");
		}
		catch(IOException e)
		{
			for(int i=0;i<5;i++)
				System.out.print("\n");
		}
	}
	boolean MatchPasswd(String s)
	{
		return(s.equals(Passwd));
	}
}
class MyWriter
{
	void writer(int i, String s, int votes) throws URISyntaxException
	{
		Reader reading = new Reader();
		Writer update;
		try
		{
		String c_name = reading.nameReader(i);
		update = new BufferedWriter(new FileWriter(s));
		update.append(c_name+"\r\n"+"The number of votes obtained is \t" + votes);
		update.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	void starer(int s)
	{
		for(int i = 0; i<s;i++)
				{
					System.out.print("*");
				}
	}
}		
public class CollegeVoting
{
	public static void main(String args[]) throws NullPointerException, URISyntaxException 
	{
		Voting v = new Voting();
		Reader r = new Reader();
		MyWriter w = new MyWriter();
		final int star = 70;
		final int c_count = 5;
		boolean result = false;
		String passerr = "Password mismatch!";
		int []counter = new int[c_count];
		int max = 0 , maxid = 0;
		int count_equal=0;
		try
		{
			Voting.clearScr();
		}
		catch(Exception e)
		{
			// do nothing.
		}
		System.out.print("Enter the admin password: \t");
                char ch[];
                String str;
		Console console = System.console();
                if (console == null) 
                {
                    //System.out.println("Couldn't get Console instance");
                    Scanner in = new Scanner(System.in);
                    str=in.nextLine();
                }
                else
                {
                    ch = console.readPassword("Enter your secret password: ");
                    str = new String(ch);
                }
		if(v.MatchPasswd(str))
		{
			while(!result)
			{
				try
				{
					Voting.clearScr();
				}
				catch(Exception e)
				{
					// do nothing.
				}
				System.out.println("Cast your vote :");
				w.starer(star);
				System.out.println();
				for(int i =1; i<=c_count;i++)
				{
					String s1 = r.nameReader(i);
					System.out.println(i+". "+s1);
				}
				w.starer(star);
				System.out.println();
				System.out.println("Check results: press 0");
				w.starer(star);
				System.out.println();
				int choice = r.readNumber();
				if(choice >= 0 && choice <= c_count)
				{
					switch(choice)
					{
						case 1 : counter[0]++;
							break;
						case 2 : counter[1]++;
							break;
						case 3 : counter[2]++;
							break;
						case 4 : counter[3]++;
							break;
						case 5 : counter[4]++;
							break;
						case 0 : result = true;
							break;
					}
					for(int i=1;i<=c_count;i++)
					{
						if(counter[i-1]>max)
						{
							max = counter[i-1];
							maxid = i;
						}
					}
				}
				else
				{
					System.out.println("Illegal vote!");
					try
					{
						Voting.clearScr();
					}
					catch(Exception e)
					{
						// do nothing.
					}
				}
			}
			try
			{
				Voting.clearScr();
			}
			catch(Exception e)
			{
				// do nothing.
			}
			boolean p =true;
			while(p)
			{
				System.out.print("Enter the admin password: \t");
                                if (console == null) 
                                {
                                    //System.out.println("Couldn't get Console instance");
                                    Scanner in = new Scanner(System.in);
                                    str=in.nextLine();
                                }
                                else
                                {
                                    ch = console.readPassword("Enter your secret password: ");
                                    str = new String(ch);
                                }			
				if(v.MatchPasswd(str))
				{
					for(int i=1;i<=c_count;i++)
					{
						if(max == counter[i-1])
						{
							count_equal++;
						}
					}					
					if(count_equal == 1)
					{					
						System.out.print("The winner is \t");
						String Winner = r.nameReader(maxid);
						System.out.println(Winner+" with "+max+" number of votes.");
						for(int i = 1;i<=c_count;i++)
						{
							String fname = i + ".txt";
							try
							{
								w.writer(i,fname,counter[i-1]);
							}
							catch(Exception e)
							{
								System.out.println(i+".txt could not be updated.");
							}	
						}
					}
					else
					{
						System.out.println("Election unsuccessful.\n There has been a tie. Resolve by re-election.");
					}
					p = false;
				}	
				else
				{
					System.out.println(passerr);
				}

			}
		}
		else
		{
			System.out.println(passerr);
			System.out.println("Restart the program to start voting. Good luck!");
		}
		System.out.println("\n \n \n ***************************** Compiled by Santosh Kumar Desai \n \n \n ");
	}
}