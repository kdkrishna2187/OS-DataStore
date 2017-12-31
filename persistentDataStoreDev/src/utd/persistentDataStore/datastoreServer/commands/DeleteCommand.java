package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DeleteCommand extends ServerCommand {
	// Delete handler class
	@Override
	public void run() throws IOException, ServerException {
		
		//Input Stream
		//Gets the file name from the inputStream
		String fileName=StreamUtil.readLine(inputStream);
		
		//Output Stream
		//Attempts to delete the named file and sends OK is successful
		if(FileUtil.deleteData(fileName)){
			sendOK();
		}
		// Or an exception if it could not
		else {
			throw new ServerException("Could not delete");
		}
	}

}
