package utd.persistentDataStore.datastoreServer.commands;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DirectoryCommand extends ServerCommand {
	// Directory command handler

	@Override
	public void run() throws IOException, ServerException {

		// Creates a list of the files in the directory
		List<String> fileList=new ArrayList<String>();
		fileList = FileUtil.directory();
		
		// Output Stream
		// Sends, the number of files, and then the list of files to OS stream
		sendOK();
		StreamUtil.writeLine(String.valueOf(fileList.size()), outputStream);
		for (String file : fileList) {
			StreamUtil.writeLine(file, outputStream);
		}
	}
}
