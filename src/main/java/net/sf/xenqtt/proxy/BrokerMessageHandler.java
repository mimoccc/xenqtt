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
package net.sf.xenqtt.proxy;

import net.sf.xenqtt.message.ConnAckMessage;
import net.sf.xenqtt.message.ConnectMessage;
import net.sf.xenqtt.message.DisconnectMessage;
import net.sf.xenqtt.message.MessageHandler;
import net.sf.xenqtt.message.MqttChannel;
import net.sf.xenqtt.message.MqttMessage;
import net.sf.xenqtt.message.PubAckMessage;
import net.sf.xenqtt.message.PubCompMessage;
import net.sf.xenqtt.message.PubMessage;
import net.sf.xenqtt.message.PubRecMessage;
import net.sf.xenqtt.message.PubRelMessage;
import net.sf.xenqtt.message.SubAckMessage;
import net.sf.xenqtt.message.SubscribeMessage;
import net.sf.xenqtt.message.UnsubAckMessage;
import net.sf.xenqtt.message.UnsubscribeMessage;

/**
 * Handles messages for the broker end of connections to clients for a proxied cluster.
 */
public final class BrokerMessageHandler implements MessageHandler {

	private final String brokerUri;
	private final ConnectMessage message;

	public BrokerMessageHandler(String brokerUri, ConnectMessage message) {
		this.brokerUri = brokerUri;
		this.message = message;
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#messageSent(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.MqttMessage)
	 */
	@Override
	public void messageSent(MqttChannel channel, MqttMessage message) {
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#messageReceived(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.MqttMessage)
	 */
	@Override
	public void messageReceived(MqttChannel channel, MqttMessage message) {
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#messageReceived(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.MqttMessage, long)
	 */
	@Override
	public void messageReceived(MqttChannel channel, MqttMessage message, long latency) {
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#connect(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.ConnectMessage)
	 */
	@Override
	public void connect(MqttChannel channel, ConnectMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#connAck(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.ConnAckMessage)
	 */
	@Override
	public void connAck(MqttChannel channel, ConnAckMessage message) throws Exception {
		// should never happen
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#publish(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.PubMessage)
	 */
	@Override
	public void publish(MqttChannel channel, PubMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#pubAck(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.PubAckMessage)
	 */
	@Override
	public void pubAck(MqttChannel channel, PubAckMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#pubRec(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.PubRecMessage)
	 */
	@Override
	public void pubRec(MqttChannel channel, PubRecMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#pubRel(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.PubRelMessage)
	 */
	@Override
	public void pubRel(MqttChannel channel, PubRelMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#pubComp(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.PubCompMessage)
	 */
	@Override
	public void pubComp(MqttChannel channel, PubCompMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#subscribe(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.SubscribeMessage)
	 */
	@Override
	public void subscribe(MqttChannel channel, SubscribeMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#subAck(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.SubAckMessage)
	 */
	@Override
	public void subAck(MqttChannel channel, SubAckMessage message) throws Exception {
		// should never happen
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#unsubscribe(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.UnsubscribeMessage)
	 */
	@Override
	public void unsubscribe(MqttChannel channel, UnsubscribeMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#unsubAck(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.UnsubAckMessage)
	 */
	@Override
	public void unsubAck(MqttChannel channel, UnsubAckMessage message) throws Exception {
		// should never happen
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#disconnect(net.sf.xenqtt.message.MqttChannel, net.sf.xenqtt.message.DisconnectMessage)
	 */
	@Override
	public void disconnect(MqttChannel channel, DisconnectMessage message) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#channelOpened(net.sf.xenqtt.message.MqttChannel)
	 */
	@Override
	public void channelOpened(MqttChannel channel) {
		// should never happen
	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#channelClosed(net.sf.xenqtt.message.MqttChannel, java.lang.Throwable)
	 */
	@Override
	public void channelClosed(MqttChannel channel, Throwable cause) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#channelAttached(net.sf.xenqtt.message.MqttChannel)
	 */
	@Override
	public void channelAttached(MqttChannel channel) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.sf.xenqtt.message.MessageHandler#channelDetached(net.sf.xenqtt.message.MqttChannel)
	 */
	@Override
	public void channelDetached(MqttChannel channel) {
		// should never happen
	}
}
