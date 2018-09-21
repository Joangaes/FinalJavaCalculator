package application;


import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Controller {
	private long number1=0;
	private String operator = "";
	private boolean start = true;
	@FXML
	private Label result;
	@FXML
	private TextArea text_area;
	private Model model = new Model();
	Server server = new Server(this);
	
	@SuppressWarnings("static-access")
	@FXML
	public void processNumber(ActionEvent event)
	{
		if(!server.server_up)
		{
			server.initializeServer();
			Thread t = new Thread(server);
			t.start();
		}
		if(start)
		{
			result.setText("");
			start=false;
		}
		String value = ((Button)event.getSource()).getText();
		result.setText(result.getText()+value);
	}
	
	@FXML
	public void processOperators(ActionEvent event) throws IOException
	{
		
		String value = ((Button)event.getSource()).getText();
		if(!value.equals("="))
		{
			if(!operator.isEmpty())
			{
				return;
			}
			operator = value;
			number1 = Long.parseLong(result.getText());
			result.setText("");
		}
		else
		{
			if(operator.isEmpty())
			{
				return;
			}
			long number2 = Long.parseLong(result.getText());
			Message message = model.MessageComposer(number1, number2, operator);
			operator = "";
			start = true;
			Server.SendMessage(message);
			text_area.setText(text_area.getText() + "\nSe ha solicitado la operacion:" +number1 +" " + message.operator + " " + number2);
		}
	}
	
	@FXML
	public void PutAnswerInLabel(Message message)
	{
		System.out.println("Ha llegado un mensaje con la respuesta:" + message.value_1);
		String answer = Double.toString(message.value_1);
		Platform.runLater(new Runnable()
				{
					@Override
					public void run() {
						result.setText(answer);
						text_area.setText(text_area.getText() + "\nSe ha recibido la respuesta:" +answer);
					}
				});
		
	}
	
	
}
