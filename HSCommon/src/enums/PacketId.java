package enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public enum PacketId {
	 REQUIRE_BOOLEAN   (0),
	 REQUIRE_INT   (1),
	 REQUIRE_FLOAT   (2),
	 REQUIRE_DOUBLE  (3),
	 REQUIRE_STRING   (4),
	 REQUIRE_ARRAY_LIST (5),
	 REQUIRE_USER_ENTITY (6);
	
	private int packetId;
	PacketId(int packetId) {
        this.packetId = packetId;
    }
	public int getPacketId() {
		return packetId;
	}
	
}
