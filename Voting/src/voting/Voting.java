/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voting;

/**
 *
 * @author Santosh Kumar Desai
 */
import java.sql.*;  
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
class Votingp
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
public class Voting {

    /**
     * @param args the command line arguments
     * @throws java.net.URISyntaxException
     */
       // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/voters";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "santosh";
   public static String readQRCode(String filePath, String charset, Map hintMap)  throws FileNotFoundException, IOException, NotFoundException 
   {
    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
    Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
    return qrCodeResult.getText();
   }
    public static void main(String[] args)throws NullPointerException, URISyntaxException, IOException, FileNotFoundException, NotFoundException
    {
        Connection conn = null;
        Statement stmt = null;
        Scanner in = new Scanner(System.in);
        try
        {  
            Class.forName("com.mysql.jdbc.Driver");  
            conn=DriverManager.getConnection(DB_URL,USER,PASS); 
            boolean result = false;
            String passerr = "Password mismatch!";
            Votingp v = new Votingp();
            Reader r = new Reader();
            try
            {
                Votingp.clearScr();
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
                    str=in.nextLine();
            }
            else
            {
                ch = console.readPassword("Enter your secret password: ");
                str = new String(ch);
            }
            if(v.MatchPasswd(str))
            {    
                try
                {
                    Votingp.clearScr();
		}
		catch(Exception e)
		{
		// do nothing.
		}
                stmt = conn.createStatement();
                String sql;
                int resset;
                sql = "Update leaderlist Set votes = 0";
                resset = stmt.executeUpdate(sql);
                if(resset!=0)
                    System.out.println("Votes initialized to zero.");
                sql = "SELECT * FROM leaderlist";
                ResultSet rs = stmt.executeQuery(sql);
                //Extract data from result set
                int i=0;
                String [] polname = new String[10];
                String [] polpname = new String[10];
                int [] polpid = new int[10];
                int [] polage = new int[10];
                while(rs.next())
                {
                //Retrieve by column name
                polname[i]  = rs.getString("polname");
                polage[i] = rs.getInt("polage");
                polpname[i] = rs.getString("polpname");
                polpid[i] = rs.getInt("polpid");
                ++i;
                }
                //Display values
                while(!result)
                {
                    int confirm =0;
                    while(confirm==0)
                    {
                    System.out.println("Enter the name of the QR file with .jpg extension : ");
                    String path = in.nextLine();
                    if(path.equals("#$%^"))
                    {
                        System.out.println("Admin confirmed...");
                        result=true;
                        break;
                    }
                    String filePath = "C:/images/"+path;
                    String charset = "UTF-8"; // or "ISO-8859-1"
                    Map hintMap = new HashMap();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    String vout=readQRCode(filePath, charset, hintMap);
                    String vsplit[] = vout.split(",");
                    System.out.println(vout);
                    String vin;
                    try{  
                        System.out.println(vsplit[1]);
                        PreparedStatement ps=conn.prepareStatement("select * from voterlist where vid = "+vsplit[1]);  
                        rs=ps.executeQuery();  
                        if(rs.next())
                        {//now on 1st row  
                           String fname = rs.getString("fname");
                           int vid = rs.getInt("vid");
                           int age = rs.getInt("age");
                           String dob = rs.getString("dob");
                           String addr = rs.getString("addr");
                           String spousen = rs.getString("spousen");
                           String fathern = rs.getString("fathern");
                           String state = rs.getString("state");
                           vin=fname+","+vid+","+age+","+dob+","+addr+","+spousen+","+fathern+","+state;
                           System.out.println(vin+"\n"+vout);
                           if(vin.equals(vout))
                               confirm=1;
                           else
                               System.out.println("Voter mismatch....");
                        }//end of if    
                    }
                    catch (Exception e) {e.printStackTrace();  }
                    }
                    if(result)
                        break;
                    System.out.println("Cast your vote :");
                    for(int j=0;j<i;j++)
                    {
                    System.out.print(""+(j+1)+". ");
                    System.out.print("Name: " + polname[j]);
                    System.out.print(", Age: " + polage[j]);
                    System.out.print(", Party: " + polpname[j]);
                    System.out.println(", Party id: " + polpid[j]);
                    }
                    
                    int choice = r.readNumber();
                    if(choice>0 && choice<=10)
                    {
                        sql = "Update leaderlist Set votes = votes + 1 where polpid = "+choice+" ;";
                        resset = stmt.executeUpdate(sql);
                        if(resset!=0)
                            System.out.println("Updated!");
                        confirm=0;
                    }
                    else 
                    {
                        System.out.println("Illegal vote!");
					try
					{
						Votingp.clearScr();
					}
					catch(Exception e)
					{
						// do nothing.
					}
                    }
                }
                try
		{
                    Votingp.clearScr();
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
                                    str=in.nextLine();
                                }
                                else
                                {
                                    ch = console.readPassword("Enter your secret password: ");
                                    str = new String(ch);
                                }			
				if(v.MatchPasswd(str))
				{
                                    sql = "Select polname, polpname, votes as obt from leaderlist where votes= (select max(votes) from leaderlist)";
                                    rs = stmt.executeQuery(sql);
                                    int cnt=0;
                                    String [] poln = new String[10];
                                    String [] polp = new String[10];
                                    int [] maxy = new int[10];
                                    while(rs.next())
                                    {
                                        poln[cnt]=rs.getString("polname");
                                        polp[cnt]=rs.getString("polpname");
                                        maxy[cnt]=rs.getInt("obt");
                                        System.out.println(poln[cnt]+" of "+polp[cnt]+" has got majority with "+maxy[cnt]+" votes.");
                                        cnt++;
                                    }
                                    if(cnt!=1)
                                    {
                                        System.out.println("There is no clear majority. It's a tie. \n Time for re-election.");
                                    }
                                    p=false;
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
			
            }
            catch (ClassNotFoundException | SQLException e) 
            {
            e.printStackTrace();  
            }    
            System.out.println("\n \n \n ***************************** Compiled by Santosh Kumar Desai \n \n \n ");
    }
}
