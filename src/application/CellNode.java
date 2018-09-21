package application;

import java.io.IOException;
import java.net.Socket;

public class CellNode {
	
	 static int init_port = 7004;
	 static int end_port = 7100;
	
	 public static void main(String []args) throws InterruptedException, IOException
	 {
		 Cell cell_node = new Cell(init_port, end_port);
		 cell_node.SendMessageAlertingImACell();
		 cell_node.ListenForMessage();
	 }
}
