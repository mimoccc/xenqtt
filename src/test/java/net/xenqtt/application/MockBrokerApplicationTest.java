/**
    Copyright 2013 James McClure

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.xenqtt.application;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.xenqtt.AppContext;
import net.xenqtt.MqttInvocationException;
import net.xenqtt.application.MockBrokerApplication;
import net.xenqtt.client.MqttClient;
import net.xenqtt.client.MqttClientConfig;
import net.xenqtt.client.MqttClientListener;
import net.xenqtt.client.NullReconnectStrategy;
import net.xenqtt.client.PublishMessage;
import net.xenqtt.client.Subscription;
import net.xenqtt.client.SyncMqttClient;
import net.xenqtt.message.ConnectReturnCode;
import net.xenqtt.message.QoS;
import net.xenqtt.mockbroker.MockBroker;

import org.junit.Test;

public class MockBrokerApplicationTest {

	List<String> flags = new ArrayList<String>();
	Map<String, String> args = new HashMap<String, String>();
	MqttClientConfig config = new MqttClientConfig().setReconnectionStrategy(new NullReconnectStrategy()).setConnectTimeoutSeconds(0)
			.setMessageResendIntervalSeconds(10).setBlockingTimeoutSeconds(0);

	MockBrokerApplication application = new MockBrokerApplication();

	@Test
	public void testStart_NotAuthorized() {
		args.put("-p", "0");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.NOT_AUTHORIZED, client.connect("clientId", true));
	}

	@Test
	public void testStart_NotAuthorized_BadCredentials() throws Exception {
		args.put("-p", "0");
		args.put("-u", "user1:pass1");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.BAD_CREDENTIALS, client.connect("clientId", true, "user1", "pass2"));
	}

	@Test
	public void testStart_AnonymousAuthorization() throws Exception {
		flags.add("-a");
		args.put("-p", "0");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		final List<String> messagePayloads = new ArrayList<String>();
		final CountDownLatch latch = new CountDownLatch(1);
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
				messagePayloads.add(message.getPayloadString());
				message.ack();
				latch.countDown();
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.ACCEPTED, client.connect("clientId", true));

		Subscription[] subscriptions = new Subscription[] { new Subscription("grand/foo/bar", QoS.AT_LEAST_ONCE) };
		assertArrayEquals(subscriptions, client.subscribe(subscriptions));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));
		assertTrue(latch.await(5, TimeUnit.SECONDS));
		assertEquals(1, messagePayloads.size());
		assertEquals("onyx", messagePayloads.get(0));
	}

	@Test(expected = IllegalStateException.class)
	public void testStart_CredentialsFlagUsedWithoutArgs() throws Exception {
		args.put("-p", "0");
		flags.add("-u");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);
	}

	@Test
	public void testStart_CredentialAuthorization() throws Exception {
		args.put("-p", "0");
		args.put("-u", "user1:pass1");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		final List<String> messagePayloads = new ArrayList<String>();
		final CountDownLatch latch = new CountDownLatch(1);
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
				messagePayloads.add(message.getPayloadString());
				message.ack();
				latch.countDown();
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.ACCEPTED, client.connect("clientId", true, "user1", "pass1"));

		Subscription[] subscriptions = new Subscription[] { new Subscription("grand/foo/bar", QoS.AT_LEAST_ONCE) };
		assertArrayEquals(subscriptions, client.subscribe(subscriptions));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));
		assertTrue(latch.await(5, TimeUnit.SECONDS));
		assertEquals(1, messagePayloads.size());
		assertEquals("onyx", messagePayloads.get(0));
	}

	@Test
	public void testStart_CredentialsIgnored() throws Exception {
		flags.add("-i");
		args.put("-p", "0");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		final List<String> messagePayloads = new ArrayList<String>();
		final CountDownLatch latch = new CountDownLatch(1);
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
				messagePayloads.add(message.getPayloadString());
				message.ack();
				latch.countDown();
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.ACCEPTED, client.connect("clientId", true, "user1", "pass1"));

		Subscription[] subscriptions = new Subscription[] { new Subscription("grand/foo/bar", QoS.AT_LEAST_ONCE) };
		assertArrayEquals(subscriptions, client.subscribe(subscriptions));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));
		assertTrue(latch.await(5, TimeUnit.SECONDS));
		assertEquals(1, messagePayloads.size());
		assertEquals("onyx", messagePayloads.get(0));
	}

	@Test
	public void testStart_NonDefaultResendInterval() throws Exception {
		args.put("-p", "0");
		args.put("-t", "2");
		args.put("-u", "user1:pass1");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		final List<String> messagePayloads = new ArrayList<String>();
		final CountDownLatch latch = new CountDownLatch(1);
		final AtomicLong firstReceived = new AtomicLong();
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
				long now = System.currentTimeMillis();
				if (firstReceived.get() == 0) {
					firstReceived.set(now);
					return;
				}

				messagePayloads.add(message.getPayloadString());
				message.ack();
				latch.countDown();
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.ACCEPTED, client.connect("clientId", true, "user1", "pass1"));

		Subscription[] subscriptions = new Subscription[] { new Subscription("grand/foo/bar", QoS.AT_LEAST_ONCE) };
		assertArrayEquals(subscriptions, client.subscribe(subscriptions));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));
		assertTrue(latch.await(2500, TimeUnit.MILLISECONDS));

		long variance = System.currentTimeMillis() - firstReceived.get();
		assertEquals(1, messagePayloads.size());
		assertEquals("onyx", messagePayloads.get(0));
		assertTrue(String.valueOf(variance), variance > 1500 && variance < 2500);
	}

	@Test
	public void testStart_NonDefaultMaxInFlightMessages() throws Exception {
		flags.add("-a");
		args.put("-p", "0");
		args.put("-t", "2");
		args.put("-m", "2");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);

		int port = getPort();
		final AtomicInteger messageCount = new AtomicInteger();
		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
				messageCount.incrementAndGet();
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};
		SyncMqttClient client = new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
		assertSame(ConnectReturnCode.ACCEPTED, client.connect("clientId", true));

		Subscription[] subscriptions = new Subscription[] { new Subscription("grand/foo/bar", QoS.AT_LEAST_ONCE) };
		assertArrayEquals(subscriptions, client.subscribe(subscriptions));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));
		client.publish(new PublishMessage("grand/foo/bar", QoS.AT_LEAST_ONCE, "onyx"));

		Thread.sleep(500);
		assertEquals(2, messageCount.get());
	}

	@Test(expected = MqttInvocationException.class)
	public void testStop() throws Exception {
		flags.add("-a");
		args.put("-p", "0");
		AppContext arguments = new AppContext(flags, args, null);
		application.start(arguments);
		int port = getPort();

		application.stop();

		MqttClientListener listener = new MqttClientListener() {

			@Override
			public void publishReceived(MqttClient client, PublishMessage message) {
			}

			@Override
			public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
			}

		};

		new SyncMqttClient(String.format("tcp://localhost:%d", port), listener, 1, config);
	}

	private int getPort() {
		try {
			Field field = MockBrokerApplication.class.getDeclaredField("broker");
			field.setAccessible(true);
			MockBroker broker = (MockBroker) field.get(application);

			return broker.getPort();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Test
	public void testGetOptsText() {
		assertNotNull(application.getOptsText());
	}

	@Test
	public void testGetOptsUsageText() {
		assertNotNull(application.getOptsUsageText());
	}
}
