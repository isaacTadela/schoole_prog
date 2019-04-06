package common;
import java.io.Serializable;

import enums.PacketId;
import enums.PacketSub;
/**
 * 
 * @author saree
 *this class is packet class
 */
// class is used for transfer through the connection 
public class Packet implements Serializable{
	private PacketId packetId;
	private PacketSub packetSub;
	private int stage;
	Object data;
	
	public PacketSub getPacketSub() {
		return packetSub;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public void setNextStage() {
		this.stage += 1;
	}	
	public void setPacketSub(PacketSub packetSub) {
		this.packetSub = packetSub;
	}
	public Packet(PacketId packetId,PacketSub packetSub, int stage, Object data) {
		super();
		this.packetId = packetId;
		this.packetSub = packetSub;
		this.data = data;
		this.stage = stage;
	}
	public PacketId getPacketId() {
		return packetId;
	}
	public void setPacketId(PacketId packetId) {
		this.packetId = packetId;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
	
}
