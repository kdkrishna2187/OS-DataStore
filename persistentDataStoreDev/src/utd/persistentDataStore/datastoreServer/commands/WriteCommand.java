package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class WriteCommand extends ServerCommand {
	// Write command handler

	@Override
	public void run() throws IOException, ServerException {
		
		// Input Stream
		// Reads the file name, number of bytes, and byte data from stream
		String fileName =StreamUtil.readLine(inputStream);
		int flength = Integer.parseInt(StreamUtil.readLine(inputStream));
		byte[] writeData  = StreamUtil.readData(flength, inputStream);
		
		// Carries out the write with the received data
		FileUtil.writeData(fileName, writeData);
		
		// Output Stream
		// If no exceptions thrown to this point, send OK.
		sendOK();
	}

}
