/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voterregistration;

/**
 *
 * @author Santosh Kumar Desai
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.sql.*;  
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
public class VoterRegistration {

    /**
     * @param args the command line arguments
     */
        // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/voters";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "santosh";
   
 public static void createQRCode(String qrCodeData, String filePath,
   String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
   throws WriterException, IOException {
  BitMatrix matrix = new MultiFormatWriter().encode(
    new String(qrCodeData.getBytes(charset), charset),
    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
  MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
    .lastIndexOf('.') + 1), new File(filePath));
 }
    public static void main(String[] args) throws NullPointerException, URISyntaxException, WriterException, IOException,
   NotFoundException
    {
        Connection conn = null;
        Statement stmt = null;
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
                try
                {
                    Votingp.clearScr();
		}
		catch(Exception e)
		{
		// do nothing.
		}
                while(!result)
                {
                    Scanner in = new Scanner(System.in);
                    System.out.println("Enter your details : ");
                    System.out.print("Enter your age : ");
                    int age=18;
                    try 
                    {
                        age = Integer.parseInt(in.nextLine());
                        if(age<18)
                        {
                            System.out.println("Minor's entry detected...");
                            System.exit(0);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.print("Enter your name : ");
                    String name = in.nextLine();
                    System.out.print("Enter your DOB : (yyyy-mm-dd)");
                    String dob = in.nextLine();
                    System.out.print("Enter your address :");
                    String addr = in.nextLine();
                    int spouse=0;
                    String sp=null,fa=null;
                    System.out.println("Spouse / Father ? (0/1)");
                    try 
                    {
                        spouse = Integer.parseInt(in.nextLine());
                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                    if(spouse==1)
                    {
                        System.out.println("Enter Father's name : ");
                        fa=in.nextLine();
                    }
                    else
                    {
                        System.out.println("Enter Spouse's name : ");
                        sp=in.nextLine();
                    }
                    System.out.println("Enter your state : ");
                    String state = in.nextLine();                 
                    stmt = conn.createStatement();
                    String sql;
                    int resset=0,vid=0;
                    sql= "insert into voterlist (fname,age,dob,addr,spousen,fathern,state) values ('"+name+"',"+age+","+"'"+dob+"','"+addr+"','"+sp+"','"+fa+"','"+state+"');";
                    resset = stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
                    ResultSet res = stmt.getGeneratedKeys();
                    if (res.next())
                    {
                        System.out.println("Data upload complete... "+ resset);
                        vid = res.getInt(1);
                    }
                    else
                    {
                        System.out.println("information upload failed.\n");
                        System.exit(0);
                    }
                    String qrCodeData=name+","+vid+","+age+","+dob+","+addr+","+sp+","+fa+","+state;
                    String filePath = "C:\\images\\"+vid+".jpg";
                    String charset = "UTF-8"; // or "ISO-8859-1"
                    Map hintMap = new HashMap();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
                    System.out.println("QR Code image created successfully!");
                    try
                    {  
                        PreparedStatement ps=conn.prepareStatement("update voterlist set qrcode =? where vid = ?");
                        ps.setString(2,String.valueOf(vid));
                        FileInputStream fin=new FileInputStream("C:\\images\\"+String.valueOf(vid)+".jpg");  
                        ps.setBinaryStream(1,fin,fin.available());  
                        int i=ps.executeUpdate();  
                        System.out.println(i+" records affected");        
                    }
                    catch (SQLException | IOException e) 
                    {
                            e.printStackTrace();
                    }  
                    System.out.println("Do you want to continue? Press 1 to continue and 0 to exit...");
                    int choice=in.nextInt();
                    if(choice==0)
                    {
                        result=true;
                        System.out.println("Bye Admin....");
                        stmt.close();
                        conn.close();
                    }
                }
                
            }
            else
            {
                System.out.println(passerr);
            }
        }
        catch (ClassNotFoundException | SQLException e) 
        {
            e.printStackTrace();  
        }
    }
    
}
