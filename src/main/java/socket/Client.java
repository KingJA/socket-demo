package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Description:TODO
 * Create Time:2019/12/12 0012 下午 2:33
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        System.out.println("发起连接");
        System.out.println("客户端信息：地址|" + socket.getLocalAddress() + " 端口号|" + socket.getLocalPort());
        System.out.println("服务端信息：地址|" + socket.getInetAddress() + " 端口号|" + socket.getPort());
        try {
            todo(socket);
        } catch (IOException e) {
            System.out.println("异常关闭");
        }
        socket.close();
        System.out.println("客户端已退出");
    }


    private static void todo(Socket client) throws IOException {
        //从键盘读取输入
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        //得到socket，并转化为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean flag = true;
        do {
            //键盘读取一行
            String readLine = input.readLine();
            //发送到服务端
            socketPrintStream.println(readLine);
            String echo = socketBufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);
        //释放资源
        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
