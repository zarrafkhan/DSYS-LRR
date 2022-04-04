import java.net.*;
import java.util.ArrayList;
import java.io.*;  

class MyClient{  
	public static void main(String args[])throws Exception{  

		String mess = ""; //last message
		String[] minfo;   //specfic information from last message

		ArrayList<String[]> data = new ArrayList<String[]>();  
		String ltype = ""; //largest server
		int lnum = 0;  //largest server types count (limit)
		String jobID = ""; 
		String[] recs; 

		try{
			//default server connection
			String name = System.getProperty("user.name");
			Socket s=new Socket("localhost",50000);  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			BufferedReader bin = new BufferedReader(new InputStreamReader(s.getInputStream()));  

			//basic server exchanges
			dout.write(("HELO\n").getBytes());
			bin.readLine();
			dout.write(("AUTH " + name + "\n").getBytes());
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

			//Stores record entry into list
			for (int i = 0; i< nRecs;i++){
				data.add(bin.readLine().split(" "));
			}

			//Find largest server type and count from list
			ltype = data.get(0)[0];
			for (int i =1; i< nRecs; i++){
				if (Integer.parseInt(data.get(i)[4]) > Integer.parseInt(data.get(i-1)[4])){
					ltype = data.get(i)[0];
				}
			}
			for (String[] a: data){ 
				if (a[0].equals(ltype)){
					lnum++;
				}
			}

			dout.write(("OK\n").getBytes());  
			bin.readLine(); 
			int sID = 0;

			while(true){
				if (mess.startsWith("JOBN")){       
					dout.write(("SCHD " + jobID + " " + ltype + " " + sID + "\n").getBytes());  
					//iterate through serverID's ~ 0 to (lnum-1)  
					sID+=1;
					if (sID >= lnum){
						sID = 0;
					}

					bin.readLine();
					dout.write(("REDY\n").getBytes());   
					mess = bin.readLine();   //next message

					while (mess.startsWith("JCPL")){   
						dout.write(("REDY\n").getBytes());  
						mess = bin.readLine();
					}
					if (mess.startsWith("NONE")){
						break;
					}

					minfo = mess.split(" "); 
					jobID = minfo[2];
					dout.write(("GETS Capable " + minfo[4] + " " + minfo[5] + " " + minfo[6] + "\n").getBytes()); 
					recs = bin.readLine().split(" "); //DATA nRecs Size
					nRecs = Integer.parseInt(recs[1]); 
					dout.write(("OK\n").getBytes());  
					//Skipping server state infromation
					for (int i = 0; i< nRecs;i++){
						bin.readLine();  
					}
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