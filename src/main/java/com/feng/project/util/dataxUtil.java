package com.feng.project.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Component("datax")
public class DataxUtil {
    private static String DEFAULTCHART = "UTF-8";

    public static Connection login(String ip, String username, String password) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = new Connection(ip);
            connection.connect();// 连接
            flag = connection.authenticateWithPassword(username, password);// 认证
            if (flag) {
                return connection;
            }
        } catch (IOException e) {
            System.out.println("=========登录失败=========" + e);
            connection.close();
        }
        return connection;
    }

    /**
     * 远程执行shll脚本或者命令
     *
     * @param cmd
     *            即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    public static String execmd(Connection connection, String cmd) {
        String result = "";
        try{
            if (connection != null) {
                Session session = connection.openSession();// 打开一个会话
                session.execCommand(cmd);// 执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                //System.out.println(result);
                // 如果为得到标准输出为空，说明脚本执行出错了
                /*if (StringUtils.isBlank(result)) {
                    System.out.println("得到标准输出为空,链接conn:" + connection + ",执行的命令：" + cmd);
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
                    System.out.println("执行命令成功,链接conn:" + connection + ",执行的命令：" + cmd);
                }*/
                connection.close();
                session.close();
            }
        } catch (IOException e) {
            System.out.println("执行命令失败,链接conn:" + connection + ",执行的命令：" + cmd + "  " + e);
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in
     *            输入流对象
     * @param charset
     *            编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                System.out.println(line);
            }
            br.close();
            writeLog(buffer.toString());
        } catch (UnsupportedEncodingException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 远程传输单个文件
     *
     * @param localFile
     * @param remoteTargetDirectory
     * @throws IOException
     */

    public static void transferFile(Connection conn,String localFile, String remoteTargetDirectory) throws IOException {
        File file = new File(localFile);

        SCPClient sCPClient = conn.createSCPClient();
        SCPOutputStream scpOutputStream = sCPClient.put(file.getName(), file.length(), remoteTargetDirectory, "0600");

        String content = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);
        scpOutputStream.write(content.getBytes());
        scpOutputStream.flush();
        scpOutputStream.close();
    }

    /**
     * 从远程服务器端下载文件到本地指定的目录中
     * @param remoteFile
     * @param localTargetDirectory
     */
    public static void downloadFile(String remoteFile,String localTargetDirectory,Connection conn)    {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(localTargetDirectory)));
            SCPClient scpClient = conn.createSCPClient();
            BufferedReader br = new BufferedReader(new InputStreamReader(scpClient.get(remoteFile)));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                bw.write(buffer.toString());
                bw.flush();
            }
            bw.close();
            br.close();
        }catch (Exception e){
            System.out.println("获取ssh下载客户端失败!");
            e.printStackTrace();
        }
    }

    public static String getFileName(String uploadFileName){
        String name = "_datax_job_"+uploadFileName+"_json-"+"*.log";
        return name;
    }


    public static void writeLog(String result) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/static/log/log.log",true)));
        bw.write(result);
        bw.flush();
        bw.close();
    }

}
