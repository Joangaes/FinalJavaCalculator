package application;

import java.io.Serializable;

public class Message implements Serializable {
String operator;
int content_code;
double value_1, value_2;
boolean resent_flag;

public static final int Im_a_node = 1;
public static final int Im_a_cell = 3;
public static final int Im_a_user_interface=2;
public static final int sum_operation_request = 4;
public static final int rest_operation_request = 5;
public static final int division_operation_request = 6;
public static final int multiplication_operation_request = 7;
public static final int operation_response = 8;
public static final int print_operator = 9;
	public Message(int content_code, double value_1,double value_2,String operator)
	{
		this.content_code = content_code;
		this.value_1 = value_1;
		this.value_2 = value_2;
		this.operator = operator;
		this.resent_flag=false;
	}
	
	public void UpdateFlagToResent()
	{
		this.resent_flag = true;
	}
}
