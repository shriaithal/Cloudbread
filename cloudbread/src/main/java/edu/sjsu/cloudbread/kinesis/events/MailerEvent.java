package edu.sjsu.cloudbread.kinesis.events;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Anushri Srinath Aithal
 *
 */
public class MailerEvent {

	private String id;
	private EventType eventType;

	private final static ObjectMapper JSON = new ObjectMapper();
	static {
		JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public MailerEvent() {

	}

	public MailerEvent(String id, EventType eventType) {
		super();
		this.id = id;
		this.eventType = eventType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public byte[] toJsonAsBytes() {
		try {
			return JSON.writeValueAsBytes(this);
		} catch (IOException e) {
			return null;
		}
	}

	public static MailerEvent fromJsonAsBytes(byte[] bytes) {
		try {
			return JSON.readValue(bytes, MailerEvent.class);
		} catch (IOException e) {
			return null;
		}
	}

	public String toJsonString() {
		try {
			return JSON.writeValueAsString(this);
		} catch (IOException e) {
			return null;
		}
	}

	public static MailerEvent fromJsonString(String json) {
		try {
			return JSON.readValue(json, MailerEvent.class);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "MailerEvent [id=" + id + ", eventType=" + eventType + "]";
	}

}
