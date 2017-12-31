package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class ReadCommand extends ServerCommand {
	// Read command handler

	@Override
	public void run() throws IOException, ServerException {

		// Input Stream
		// Reads file name from input stream
		String filename = StreamUtil.readLine(inputStream);
		
		// Uses filename to read a file
		byte data[] = FileUtil.readData(filename);
		
		// Output stream
		// Sends OK followed by length and file data to output stream
		sendOK();
		System.out.println("length of data stored " +data.length);
		StreamUtil.writeLine(String.valueOf(data.length), outputStream);
		StreamUtil.writeData(data, outputStream);
	}
}
