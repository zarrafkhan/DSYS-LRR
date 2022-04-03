import java.net.*;
import java.util.ArrayList;
import java.io.*;  

class MyClient{  
public static void main(String args[])throws Exception{  

String mess = ""; //last message
String[] minfo; //specfic information from last message

ArrayList<String[]> data = new ArrayList<String[]>();  
String ltype = "";
int lnum = 0; //number of largest server types (limit)
String jobID = ""; 
String[] recs; // Includes n.o. Records and size 

try{

//default server connection
Socket s=new Socket("localhost",50000);  
DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
BufferedReader bin = new BufferedReader(new InputStreamReader(s.getInputStream()));  

//basic server exchanges
dout.write(("HELO\n").getBytes());
bin.readLine();
dout.write(("AUTH zeek\n").getBytes());
bin.readLine();

dout.write(("REDY\n").getBytes());      
mess = bin.readLine(); //recieves jobn first           
minfo = mess.split(" "); 
jobID = minfo[2];
//Sends GETS Capable Core/Memory/Disk
dout.write(("GETS Capable " + minfo[4] + " " + minfo[5] + " " + minfo[6] + " \n").getBytes()); 
recs = bin.readLine().split(" "); //DATA nRecs Size
int nRecs = Integer.parseInt(recs[1]);
dout.write(("OK\n").getBytes());   

String[] d = bin.readLine().split("\n"); //Server state info
for(int i =0; i< nRecs; i++){ //for each record store into list
    data.add(d[i].split(" "));
}

//FIND LARGEST type based on core size
ltype = data.get(0)[0];
for (int i =1; i< nRecs; i++){
    if (Integer.parseInt(data.get(i)[4]) > Integer.parseInt(data.get(i-1)[4])){
        ltype = data.get(i)[0];
    }
}

for (String[] a: data){ //acquire largest server count
    if (a[0].equals(ltype)){
        lnum++;
    }
}


dout.write(("OK\n").getBytes());  
bin.readLine(); 

while( !(mess.equals("NONE\n")) ){

    if (mess.startsWith("JOBN")){        
        dout.write(("SCHD " + jobID + " " + ltype + "\n").getBytes());   //iterate through 0 to (lnum-1)

        dout.write(("REDY\n").getBytes());      
        mess = bin.readLine(); 
        minfo = mess.split(" "); 
        jobID = minfo[2];
        dout.write(("GETS Capable " + minfo[4] + " " + minfo[5] + " " + minfo[6] + " \n").getBytes()); 
        bin.readLine();
        dout.write(("OK\n").getBytes());  
        bin.readLine();
        dout.write(("OK\n").getBytes());  
        bin.readLine();  
    }
}

dout.write(("QUIT\n").getBytes());
dout.flush();
bin.readLine();
dout.close();  

s.close(); 
 
}catch(Exception e){System.out.println(e);}
} 
}



/************************ 
NOTES


*/

