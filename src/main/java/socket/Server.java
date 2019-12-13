package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description:TODO
 * Create Time:2019/12/12 0012 下午 2:33
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("服务器准备就绪");
        System.out.println("服务端信息：地址|" + serverSocket.getInetAddress() + " 端口号|" + serverSocket.getLocalPort());
        for (; ; ) {
            //接受一个客户端连接，是阻塞操作
            Socket client = serverSocket.accept();
            //客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            //启动线程
            clientHandler.start();
        }
    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接：地址|" + socket.getLocalAddress() + " 端口号|" + socket.getPort());
            try {
                //得到打印流，用于数据输出，服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                //获取输入流，用户接受数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    //键盘读取一行
                    String msg = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(msg)) {
                        flag = false;
                        //回送
                        socketOutput.println("bye");
                    } else {
                        //打印到屏幕，并回送数据长度
                        System.out.println(msg);
                        socketOutput.println("回送："+msg.length());
                    }
                } while (flag);
                socketOutput.close();
                socketInput.close();
            } catch (Exception e) {
                System.out.println("连接应断开");
            }finally {
                //连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已退出：地址|" + socket.getLocalAddress() + " 端口号|" + socket.getPort());
        }
    }

}
