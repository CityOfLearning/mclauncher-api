package sk.tomsik68.mclauncher.impl.servers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

/**
 * This is implementation of http://wiki.vg/Server_List_Ping
 */
final class Protocol47PingPacketFactory extends ServerPingPacketFactory {

	private static void writeRequestPacket(DataOutputStream dos, ServerInfo serverInfo) throws IOException {
		// packet ID
		dos.writeByte(0);
		// protocol version
		writeVarInt(dos, 47);
		// server address
		dos.writeUTF(serverInfo.getIP());
		// server port
		dos.writeShort(serverInfo.getPort());
		// next state = 1 (status)
		writeVarInt(dos, 1);

		// request
		dos.writeByte(0);
	}

	private static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte((paramInt & 0x7F) | 0x80);
			paramInt >>>= 7;
		}
	}

	@Override
	byte[] createPingPacket(ServerInfo serverInfo) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);

			writeRequestPacket(dos, serverInfo);

			baos.flush();
			baos.close();
			return baos.toByteArray();
		} catch (Exception e) {
			return new byte[0];
		}
	}
}
