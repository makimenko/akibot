package com.akibot.kiss.launcher;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.akibot.kiss.message.Command;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceStatusMessage;
import com.akibot.kiss.server.Server;

public class SingleCommand {


	public static void main(String[] args) throws Exception {

		Socket socket = new Socket("localhost", 2002);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

		while(true) {
			CommandMessage commandMessage = new CommandMessage();
			commandMessage.setCommand(Command.GET_DISTANCE);
			objectOutputStream.writeObject(commandMessage);
			Thread.sleep(10000);
		}
		
		//objectOutputStream.close();
		//outputStream.close();
		//socket.close();
		
	}
	
}
