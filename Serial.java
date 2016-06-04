import java.nio.*;
import java.nio.channels.*;
import jssc.*;
import java.awt.*;
import java.awt.event.*;

public class Serial
{	
	static SerialPort serialPort;
	
	public static void send(char c)
	{
	    String s = "" + c;
        try 
        {
			serialPort.writeBytes(s.getBytes()); //Write data to port 
		} 
        catch (SerialPortException e) 
        {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SerialPortException //add throws declaration to get rid of try/catch
	{	
		serialPort = new SerialPort("/dev/cu.usbmodem1411");
        
	    serialPort.openPort();
	    serialPort.setParams(9600, 8, 1, 0); //baudrate, databits, stopbits, parity

		System.out.println("Ready for inputs");
		System.out.println("----------------");

		
		ReadableByteChannel channel = Channels.newChannel(System.in);
	    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
  
        try //need this because the Robot port below is supposed to be in try catch statement
		{
			Robot console = new Robot(); //robot used for keyPress event

			int bytesRead = channel.read(byteBuffer); //read into buffer.
				
			while (bytesRead != -1) 
			{
				byteBuffer.flip();  //make buffer ready for read
				  
				while(byteBuffer.hasRemaining())
				{
				   send((char) byteBuffer.get()); // read 1 byte at a time
				   console.keyPress(KeyEvent.VK_ENTER);
				}
				  
				byteBuffer.clear(); //make buffer ready for writing
		        bytesRead = channel.read(byteBuffer);
				System.out.println("_____________________________________________________________________"); 
				try{ Thread.sleep(100); } catch(Exception e) {} 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 
        
        serialPort.closePort();				
   } 
}
