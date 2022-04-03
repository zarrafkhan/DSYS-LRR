import java.net.*;
import java.util.ArrayList;
import java.io.*;  
class MyClient{  
public static void main(String args[])throws Exception{  

String mess = ""; //last message
String data = ""; //server state info 
String[] nRecs; 

try{

Socket s=new Socket("localhost",50000);  
//DataInputStream din=new DataInputStream(s.getInputStream());  
DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
BufferedReader bin = new BufferedReader(new InputStreamReader(s.getInputStream()));  

dout.write(("HELO\n").getBytes());
dout.flush();
bin.readLine();
dout.write(("AUTH zeek\n").getBytes());
dout.flush();
//step 5
dout.write(("REDY\n").getBytes());      
mess = bin.readLine(); //recieves jobn first

//IDENTIFY LARGEST
dout.write(("GETS ALL\n").getBytes());
nRecs = bin.readLine().substring(5).split(" ");
dout.write(("OK\n").getBytes());
data = bin.readLine(); //first split by /n, then  " " into string[][] or arraylist
for (int i = 0;i < Integer.parseInt(nRecs[1]); i++){ //foreach record

}

while(!mess.equals("NONE\n")){
    //step 7
    //check for largest server type
    //and 
    if (mess.startsWith("JOBN")){                     
        //SEND GETS
        //RECIEVE DATA NRECS SIZE
        //SEND OK
        //For NRECS Arr
        //
    }
}

dout.write(("QUIT\n").getBytes());
dout.flush();
data = bin.readLine();
dout.close();  
s.close(); 
 
}catch(Exception e){System.out.println(e);}
} 
}



/************************ 
POSSIBLE LAYOUT

DO-ONLY ONCE STUFF FIRST (AUTH, READLINE (RECIEVE JOBN), IDENTIFY LARGEST SERVER TYPE USING GETS ALL - CORES)
(IF NONE -> QUIT)

WHILE NONE LOOP{ ROUND ROBIN
    IF (JOBN) SCHD
    IF (ANYTHING ELSE) X...
}










*/

