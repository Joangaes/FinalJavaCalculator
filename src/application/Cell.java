package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cell {
	Socket socket_connected;
	int init_range_of_search;
	int end_range_of_search;
	boolean cell_up = false;
	ObjectOutputStream outputStream;
	ObjectInputStream inStream;
	public Cell(int init_range_of_search, int end_range_of_search)
	{
		this.init_range_of_search = init_range_of_search;
		this.end_range_of_search = end_range_of_search;
		this.connectToServer();
		try {
			outputStream = new ObjectOutputStream(socket_connected.getOutputStream());
			System.out.println("Se creo el objeto de salida");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldnt create output stream");
		}
	}
	
	public void connectToServer()
	{
		for(int i = init_range_of_search; i<end_range_of_search;i++)
		{
			try {
				this.socket_connected = new Socket("localhost",i);
				this.cell_up = true;
				System.out.println("Se ha conected con el puerto: " + i);
				break;
			} catch (UnknownHostException e) {
				System.out.println("No se pudo conectar al puerto:"+i);
			} catch (IOException e) {
			}
		}
	}
	
	public void ListenForMessage()
	{	
		Message mensaje;
		try {
			this.inStream = new ObjectInputStream(this.socket_connected.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		while(cell_up)
		{
				try {		
					System.out.println("Esperando mensaje");
					mensaje = (Message) inStream.readObject();
					System.out.println("Llego mensaje");
					this.DoActionForMessage(mensaje);
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Se perdio la conexion");
					cell_up = false;
				}
		}
	}
	
	public void DoActionForMessage(Message mensaje)
	{
		System.out.println("Haciendo accion del mensaje");
		switch(mensaje.content_code)
		{
		case Message.sum_operation_request:
			this.sendAnswerOfRequest(Suma(mensaje.value_1,mensaje.value_2));
			break;
		case Message.rest_operation_request:
			this.sendAnswerOfRequest(Resta(mensaje.value_1,mensaje.value_2));
			break;
		case Message.multiplication_operation_request:
			this.sendAnswerOfRequest(Multiplicacion(mensaje.value_1,mensaje.value_2));
			break;
		case Message.division_operation_request:
			this.sendAnswerOfRequest(Division(mensaje.value_1,mensaje.value_2));
			break;
		default:
			break;
		}
	}
	
	public void SendMessageAlertingImACell()
	{
		try {
			System.out.println("Avisando que soy una celula");
			outputStream.writeObject(new Message(Message.Im_a_cell,0,0,""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No se pudo avisar que era una celula");
		}
	}
	
	public void sendAnswerOfRequest(double answer)
	{
		System.out.println("Mandando respuesta:"+answer);
		try {
			this.outputStream.writeObject(new Message(Message.operation_response,answer,0,""));
			System.out.println("Respuesta mandada:"+answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No se pudo mandar mensaje");
		}

	}
	
	public double Suma(double value_1,double value_2)
	{
		return value_1 + value_2;
	}
	public double Resta(double value_1,double value_2)
	{
		return value_1-value_2;
	}
	public double Multiplicacion(double value_1,double value_2)
	{
		return value_1*value_2;
	}
	public double Division(double value_1,double value_2)
	{
		if(value_2==0)
		{
			return 0;
		}
		return value_1/value_2;
	}
}
