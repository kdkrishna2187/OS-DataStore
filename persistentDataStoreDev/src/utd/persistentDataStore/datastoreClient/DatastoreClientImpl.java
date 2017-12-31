package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient {
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;
	private InputStream inData;
	private OutputStream outData;

	public DatastoreClientImpl(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	private void getSocketConnection() throws IOException {
		SocketAddress saddr = new InetSocketAddress("localhost", 10023);
		Socket socket = new Socket(address, port);
		try {
			socket.connect(saddr);
		} catch (Exception e) {

		}
		inData = socket.getInputStream();
		outData = socket.getOutputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.
	 * String, byte[])
	 */
	@Override
	public void write(String name, byte data[]) throws ClientException, ConnectionException {
		// Write command
		logger.debug("Executing Write Operation");
		try {
			
			// Output Stream
			// Opens a connection with the server and writes the commands
			getSocketConnection();
			StreamUtil.writeLine("write", outData);
			StreamUtil.writeLine(name, outData);
			StreamUtil.writeLine(String.valueOf(data.length), outData);
			StreamUtil.writeData(data, outData);
			logger.debug("waiting for response.");
			
			// Input Stream
			// If the response is not "ok" throws exception and displays message.
			// Otherwise it notifies the user of a successful operation.
			String responseMsg = StreamUtil.readLine(inData);
			System.out.println("response is " + responseMsg);
			if (!responseMsg.equalsIgnoreCase("OK")) {
				throw new ClientException("Oops! Write operation Failed.");
			}
			else {
				logger.debug("Delete Operation Successful");
			}
		} catch (Exception ex) {
			throw new ClientException(ex.getMessage());
		} finally {
			StreamUtil.closeSocket(inData);
			try {
				outData.close();
			} catch (Exception ex) {
				logger.debug(ex.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.
	 * String)
	 */
	@Override
	public byte[] read(String name) throws ClientException, ConnectionException {
		// Read command
		logger.debug("Executing Read Operation");
		byte[] data = null;
		try {
			
			// Output Stream
			// Writes the read command and filename to output stream
			getSocketConnection();
			StreamUtil.writeLine("read", outData);
			StreamUtil.writeLine(name, outData);
			logger.debug("waiting for response.");
			
			// Input Stream
			// Checks for "ok" response, exception if not received correctly.
			// If OK received, checks data size then reads data form stream. Returns data on success.
			String responseMsg = StreamUtil.readLine(inData);
			System.out.println("response is "+responseMsg);
			if (responseMsg.equalsIgnoreCase("OK")) {
				logger.debug("Read Operation Successful.");
				int byteDataSize = Integer.parseInt(StreamUtil.readLine(inData));
				data=StreamUtil.readData(byteDataSize, inData);
			} else {
				throw new ClientException("Oops! Read operation Failed.");
			}
		}  catch (Exception ex) {
			throw new ClientException(ex.getMessage());
		} finally {
			StreamUtil.closeSocket(inData);
			try {
				outData.close();
			} catch (Exception ex) {
				logger.debug(ex.getMessage());
			}
		}
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.
	 * String)
	 */
	@Override
	public void delete(String name) throws ClientException, ConnectionException {
		// Delete command
		logger.debug("Executing Delete Operation");
		try {
			
			// Output Stream
			// Sends delete command and a filename to delete.
			getSocketConnection();
			StreamUtil.writeLine("delete", outData);
			StreamUtil.writeLine(name, outData);
			logger.debug("waiting for response.");
			
			// Input Stream
			// Checks for the OK response.
			String responseMsg = StreamUtil.readLine(inData);
			System.out.println("response is "+responseMsg);
			if (!responseMsg.equalsIgnoreCase("OK")) {
				throw new ClientException("Oops! Delete operation Failed.");
			}
			else {
				logger.debug("Delete Operation Successful");
			}
		} catch (Exception ex) {
			throw new ClientException(ex.getMessage());
		} finally {
			StreamUtil.closeSocket(inData);
			try {
				outData.close();
			} catch (Exception ex) {
				logger.debug(ex.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
	public List<String> directory() throws ClientException, ConnectionException {
		// Directory Command
		logger.debug("Executing Directory Operation");
		List<String> list=new ArrayList<String>();
		try {
			
			// Output Stream
			// Sends directory command
			getSocketConnection();
			StreamUtil.writeLine("directory", outData);
			logger.debug("waiting for response.");
			
			// Input Stream
			// Checks for OK, then grabs list length and list from input stream. Returns list.
			String responseMsg = StreamUtil.readLine(inData);
			if (responseMsg.equalsIgnoreCase("OK")) {
				logger.debug("Directory Operation Successful.");
				int filesNum = Integer.parseInt(StreamUtil.readLine(inData));
				System.out.println("No.of. files in the directory:" + filesNum);
				for (int i = 0; i < filesNum; i++) {
					String fileName=StreamUtil.readLine(inData);
					list.add(fileName);
				}	
			} else {
				throw new ClientException("Oops! Directory operation Failed.");
			}
		} catch (IOException ex) {
			throw new ClientException(ex.getMessage());
		} finally {
			StreamUtil.closeSocket(inData);
			try {
				outData.close();
			} catch (Exception ex) {
				logger.debug(ex.getMessage());
			}
		}
		return list;
	}

}
