//package pcoen235p2;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;

public class UDPServer 
{
	public static void main(String args[])
	{
		try
		{
			
			int match=0;
			
			//Creating the UDP Server socket
			DatagramSocket ds=new DatagramSocket(3756);
			System.out.println("The UDP Server Socket has been created");
			
			
			//Creating the packet to send to the client
			DatagramPacket send_packet;
			
			//Creating the packet to receive from the client
			DatagramPacket receive_packet;
			
			//Creating the acknowledgement to send to the client
			DatagramPacket send_acknowledgement;
			
			//Creating the packet array in which all the data will be present
			byte packet_array[]=new byte[21];
			
			//Making all the elements of the packet array as 0 in the beginning
			for(int i=0;i<packet_array.length;i++)
			{
				packet_array[i]=0;
			}
		
			
			//Creating a byte array to store only the data
			byte server_read[]=new byte[10];
			
			
			//Creating a Checksum object
			Checksum cs;
			
			
			//Creating a variable to store a new server side checksum value
			long server_checksum=0;
			
			//Creating a byte array to store the checksum coming from the client side
			byte checksum_array[]=new byte[10];
			
			//Creating a byte array to store the checksum calculated at the server side
			byte checksum_array_server[];
			
			//Creating the string to store the server side calculated checksum
			String checksum_string;
			
		
			//Creating an object of Random class 
			Random r=new Random();
			int random_number;
			int snr;
			
			
			//Creating a byte array to send the acknowledgement to the client
			byte acknowledgement_array_server[]=new byte[1];
			
			
			
			//Displaying the initial packet array
			//System.out.println("In the beginning the packet array is as follows:");
			//for(int i=0;i<packet_array.length;i++)
			//{
			//	System.out.print(packet_array[i]+"\t");
			//}
			
			
			//Receiving the number of iterations from the client
			byte no_of_iterations_array[]=new byte[1];
			DatagramPacket receive_no_of_iterations=new DatagramPacket(no_of_iterations_array,no_of_iterations_array.length);
			ds.receive(receive_no_of_iterations);
			System.out.println("Number of iterations received from the client");
			int number_of_iterations=no_of_iterations_array[0];
			System.out.println("The number of iterations received from the client are: "+number_of_iterations);
			
			
			
			
			
			outer:
			//Running the loop according to the number of iterations
			for(int i=0;i<number_of_iterations;i++)
			{
				
				//Receiving the fully loaded packet from the client side
				receive_packet=new DatagramPacket(packet_array,packet_array.length);
				ds.receive(receive_packet);
				
				//Displaying the packet received from the client
				System.out.println("The packet received from the client side is as follows:");
				for(int ij=0;ij<packet_array.length;ij++)
				{
					System.out.print(packet_array[ij]+"\t");
				}
				
				//Extracting the  byte array of the data from the packet
				for(int jk=0;jk<server_read.length;jk++)
				{
					server_read[jk]=packet_array[jk+11];
				}
				
				//Displaying only the byte array of the data
				//System.out.println("The byte array of the data is:");
				//for(int ik=0;ik<server_read.length;ik++)
				//{
				//	System.out.print(server_read[ik]+"\t");
				//}
				
				
				//Extracting the checksum coming from the client side
				for(int hp=0;hp<checksum_array.length;hp++)
				{
					checksum_array[hp]=packet_array[hp+1];
				}
				
				
				//Recalculating the checksum for the data at the server side
				cs=new CRC32();
				cs.update(server_read,0,server_read.length);
				server_checksum=cs.getValue();
				System.out.println("The long value of the  new server side checksum is: "+server_checksum);
				checksum_string=String.valueOf(server_checksum);
				if(checksum_string.length()==9)
				{
					checksum_string="0"+checksum_string;
				}
				else if(checksum_string.length()==8)
				{
					checksum_string="0"+"0"+checksum_string;
				}
				checksum_array_server=checksum_string.getBytes();
				
				
				
				//Comparing the checksums of the client side and the server side
				for(int pk=0;pk<checksum_array.length;pk++)
				{
					if(checksum_array[pk]==checksum_array_server[pk])
					{
						match=match+1;
						//System.out.println("CHECKSUM ELEMENT MATCHES");
					}
					else
					{
						System.out.println("CHECKSUM ELEMENT DOES NOT MATCH");
					}
				}
				
				
				if(match==10)
				{
					System.out.println("CHECKSUM ELEMENTS MATCH");
					match=0;
				}
				
				
				//Generating a random number
				random_number=r.nextInt(2);
				System.out.println("The sequence number(random) generated is:"+random_number);
				
				//Extracting the sequence number sent by the client
				snr=packet_array[0];
				

				//Comparing the acknowledgement numbers of both the sides
				if(snr==random_number)
				{
					System.out.println("Sequence Numbers Match");
					System.out.println("You can proceed with sending the next packet");
					
					acknowledgement_array_server[0]=packet_array[0];
					
					//Sending the same sequence number back to the client
					 send_acknowledgement=new DatagramPacket(acknowledgement_array_server,acknowledgement_array_server.length,receive_packet.getAddress(),receive_packet.getPort());
						ds.send(send_acknowledgement);
				}
				else
				{
				System.out.println("The sequence numbers do not match");
				System.out.println("Retransmission has to be done");
				acknowledgement_array_server[0]=(byte)7;
				
				//Sending an error value sequence number to the client
				send_acknowledgement=new DatagramPacket(acknowledgement_array_server,acknowledgement_array_server.length,receive_packet.getAddress(),receive_packet.getPort());
				ds.send(send_acknowledgement);
				
				//Blocking for the Retransmission of the packet
				i=i-1;
				continue outer;
				
				}
				
				
				//Making all the elements in the packet array as 0 in the end
				for(int yt=0;yt<packet_array.length;yt++)
				{
					packet_array[yt]=0;
				}
				
			}//end of the main for loop
			
			System.out.println("Server is closing");
			System.out.println("Operation Successful");
			
			
			//Closing the UDP socket
			ds.close();
		}
		catch(SocketException e)
		{
			System.out.println("There is a socket exception");
			System.out.println(e);
		}
		catch(IOException e)
		{
			System.out.println("There is an input-output exception");
			System.out.println(e);
		}
	}
}
