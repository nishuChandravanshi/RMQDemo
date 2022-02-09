package com.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1 {
	private static final String QUEUE_NAME_1        = "first_queue";
	private static final String DEV_BOT_TO_TELEGRAM = "bot_to_telegram";


	public static void main (String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel    channel    = connection.createChannel();
//		Channel    channel1   = connection.createChannel(1);

//		channel.queueDeclare(QUEUE_NAME_1, false, false, false, null);	//just to make sure the queue is present in rmq server, if not we can declare it here before consuming from it

//		Consumer1
		System.out.println("Consumer1 | Waiting for messages. To exit press CTRL+C");
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("Consumer1 | Received '" + message + "'");
		};

//		above deliveryCallback without using lambda function would look like below
//		DeliverCallback deliverCallback = new DeliverCallback() {
//			@Override
//			public void handle (String s, Delivery delivery) throws IOException {
//				String message = new String(delivery.getBody(), "UTF-8");
//				System.out.println("Consumer1 | Received '" + message + "'");
//			}
//		};

		channel.basicConsume(QUEUE_NAME_1, true, deliverCallback, consumerTag1 -> {
		});


//		Consumer2
		System.out.println("Consumer2 | Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("Consumer2| Received  '" + message + "'");
		};

		channel.basicConsume(QUEUE_NAME_1, true, deliverCallback1, consumerTag -> {
		});

	}
}
