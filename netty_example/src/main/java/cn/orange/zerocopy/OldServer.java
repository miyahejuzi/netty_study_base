package cn.orange.zerocopy;


import javax.net.ssl.SSLServerSocket;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 用于测试用bio和nio在zero copy 性能上的差异
 *
 * @author miya
 * @date 19-9-21
 */
public class OldServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8899);

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());

            try {
                byte[] array = new byte[4096];

                while (true)  {
                    int readSize = in.read(array, 0, array.length);
                    if(readSize == -1) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
