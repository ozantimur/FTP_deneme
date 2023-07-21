package ftp;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

public class SFTPDownload {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 22;
        String username = "ozant";
        String password = "***";
        String targetPath = "C:/Users/ozant/OneDrive/Masaüstü/denemeFolder";

        String[] resourcePaths = {
                "denemeText.txt"
        };

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            // Avoid checking host key
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            for (String resourcePath : resourcePaths) {
                Vector<ChannelSftp.LsEntry> fileList = sftpChannel.ls(resourcePath);
                for (ChannelSftp.LsEntry entry : fileList) {
                    if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                        String remoteFilePath = resourcePath + "/" + entry.getFilename();
                        String localFilePath = targetPath + "/" + entry.getFilename();
                        downloadFile(sftpChannel, remoteFilePath, localFilePath);
                    }
                }
            }

            sftpChannel.exit();
            session.disconnect();

            System.out.println("Download completed!");
        } catch (JSchException | SftpException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(ChannelSftp sftpChannel, String remoteFile, String localFile)
            throws SftpException, IOException {
        File local = new File(localFile);
        File parentDir = local.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(local)) {
            sftpChannel.get(remoteFile, fileOutputStream);
        }
    }
}



