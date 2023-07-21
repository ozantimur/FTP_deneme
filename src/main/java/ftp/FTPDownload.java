package ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.FileOutputStream;
import java.io.IOException;

public class FTPDownload {
    public static void main(String[] args) {
        String server = "127.0.0.1";
        int port = 21;
        String username = "DESKTOP-8V8Q74T";
        String password = "***";
        String targetPath = "C:/Users/ozant/OneDrive/Masaüstü/denemeFolder";

        String[] resourcePaths = {
                "denemeText.txt"
        };

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            for (String resourcePath : resourcePaths) {
                FTPFile[] files = ftpClient.listFiles(resourcePath);
                for (FTPFile file : files) {
                    if (!file.isDirectory()) {
                        String remoteFilePath = resourcePath + "/" + file.getName();
                        String localFilePath = targetPath + "/" + file.getName();
                        downloadFile(ftpClient, remoteFilePath, localFilePath);
                    }
                }
            }

            ftpClient.logout();
            ftpClient.disconnect();

            System.out.println("Download completed!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadFile(FTPClient ftpClient, String remoteFile, String localFile)
            throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(localFile)) {
            ftpClient.retrieveFile(remoteFile, fileOutputStream);
        }
    }
}

