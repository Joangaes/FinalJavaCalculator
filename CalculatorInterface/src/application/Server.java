package application;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements Runnable{
	
	public static int init_port = 7001;
	public static int end_port = 7100;
	static BufferedReader stdIn;
	public static boolean server_up = false;
	public static Socket socket_connected;
	static ObjectOutputStream outputStream;
	ObjectInputStream inStream;
	public Controller controller;
	
	public Server(Controller controller)
	{
		this.controller = controller;
	}
	
	public static void main(String []args) throws InterruptedException, IOException
	 {
		 stdIn = new BufferedReader(new InputStreamReader(System.in));
		 System.out.println("Inicializando el puerto para anclaje");
	 }
	
	public static void initializeServer()
	 {
		 for(int i = init_port;i<end_port;i++)
		 {
			 try {
					socket_connected = new Socket("localhost",i);
					server_up = true;
					System.out.println("Se ha conected con el puerto: " + i);
					outputStream = new ObjectOutputStream(socket_connected.getOutputStream());
					outputStream.writeObject(new Message(Message.Im_a_user_interface,0,0,""));
					break;
				} catch (UnknownHostException e) {
					System.out.println("No se pudo conectar al puerto:"+i);
				} catch (IOException e) {
				}
		 }
	 }
	
	public void ListenForMessage()
	{
		System.out.println("Creando objeto para la entrada");	
		Message mensaje;
		try {
			inStream = new ObjectInputStream(socket_connected.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(server_up)
		{
				try {
					
					mensaje = (Message) inStream.readObject();
					System.out.println("Llego mensaje con el codigo: " + mensaje.content_code);
					this.DoActionForMessage(mensaje);
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Se perdio la conexion");
					server_up = false;
				}
		}
	}
	
	public void DoActionForMessage(Message mensaje) throws IOException
	{
		switch(mensaje.content_code)
		{
		case Message.operation_response:
			controller.PutAnswerInLabel(mensaje);
			break;
		/*case Message.division_operation_request:
			
			break;
		case Message.multiplication_operation_request:

			break;
		case Message.sum_operation_request:

			break;
		case Message.rest_operation_request:

			break;*/
		default:
			break;
		}
	}
	
	public static void SendMessage(Message message)
	{
		System.out.println("Solicitando operacion");
		try {
			outputStream.writeObject(message);
			System.out.println("Operacion solicitada");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No se pudo mandar mensaje");
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ListenForMessage();
	}
	
	
}
