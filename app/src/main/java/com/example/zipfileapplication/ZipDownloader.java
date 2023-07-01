package com.example.zipfileapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipDownloader {

    private static final String TAG = "ZipDownloader";

    private Context context;
    private String downloadUrl;
    private String destinationFolder;

    public ZipDownloader(Context context, String downloadUrl, String destinationFolder) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.destinationFolder = destinationFolder;
    }

    public void downloadAndExtractZip() {
        new DownloadTask().execute();
    }

    private class DownloadTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String zipFilePath = Environment.getExternalStorageDirectory() + File.separator + "temp.zip";
                    File zipFile = new File(zipFilePath);

                    OutputStream outputStream = new FileOutputStream(zipFile);
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                    unzip(zipFilePath, destinationFolder);

                    zipFile.delete();

                    return true;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error downloading ZIP file: " + e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // ZIP file downloaded and extracted successfully, handle the data here
                // You can access the extracted files from the "destinationFolder"
                // ZIP file downloaded and extracted successfully, handle the data here
                File extractedFolder = new File(destinationFolder);
                if (extractedFolder.exists() && extractedFolder.isDirectory()) {
                    File[] files = extractedFolder.listFiles();
                    if (files != null && files.length > 0) {
                        // Process the extracted files
                        for (File file : files) {
                            // Do something with each file
                            // For example, if the file is a text file, read its contents
                            if (file.isFile() && file.getName().endsWith(".mp3")) {
                                String fileContents = readFileContents(file);
                                // Process the file contents
                            }
                            int a = 5;
                        }
                    } else {
                        // No files found in the extracted folder
                        int a = 5;
                    }
                } else {
                    // Extracted folder does not exist or is not a directory
                    int a = 5;
                }

            } else {
                // Failed to download or extract ZIP file
                int a = 5;
            }
        }
    }



    private String readFileContents(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    private void unzip(String zipFilePath, String destinationFolder) {
        try {
            File directory = new File(destinationFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String a = zipFilePath;
            String g = a;

            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zipInputStream = new ZipInputStream(fis);

//            ZipInputStream zipInputStream = new ZipInputStream(context.openFileInput(zipFilePath));



            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                String entryPath = destinationFolder + File.separator + entryName;

                File entryFile = new File(entryPath);
                if (zipEntry.isDirectory()) {
                    entryFile.mkdirs();
                } else {
                    FileOutputStream outputStream = new FileOutputStream(entryFile);
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.close();
                }

                zipInputStream.closeEntry();
            }

            zipInputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Error extracting ZIP file: " + e.getMessage());
        }
    }
}
