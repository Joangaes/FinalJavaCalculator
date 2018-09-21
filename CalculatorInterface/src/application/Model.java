package application;
public class Model {
	public Message MessageComposer(double number1, double number2, String operator)
	{
		int content_code = 0;
		switch(operator)
		{
		case "+":
			content_code = Message.sum_operation_request;
			break;
		case "-":
			content_code = Message.rest_operation_request;
			break;
		case "*":
			content_code = Message.multiplication_operation_request;
			break;
		case "/":
			content_code = Message.division_operation_request;
			break;
		}
		Message message = new Message(content_code,number1,number2,operator);
		
		return message;
	}
}
