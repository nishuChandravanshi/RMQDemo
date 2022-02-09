package com.publiser;


import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class Publisher1 {
	private static final String QUEUE_NAME_1 = "first_queue";


	public static void main (String[] args) {

//		create a connection to the server
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		try {
			Connection connection = factory.newConnection();
			Channel    channel    = connection.createChannel();

			factory.newConnection();
			channel.queueDeclare(QUEUE_NAME_1, false, false, false, null);

			System.out.println("Enter number of Messages to be send : ");
			Scanner scanner = new Scanner(System.in);
			int     counter = scanner.nextInt();

			for (int i = 1; i <= counter; i++) {
				String message = String.format("Message_%s", i);
				channel.basicPublish("", QUEUE_NAME_1, null, message.getBytes(StandardCharsets.UTF_8));
				System.out.println("Sent '" + message + "'");
			}

//			Block Current Thread
			new CountDownLatch(1).await();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception Occurred =>" + e.getMessage());
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}

//connection
//channel
//queuebind
