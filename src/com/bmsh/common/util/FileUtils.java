/** Copyright © 2015-2020 100msh.com All Rights Reserved */
package com.bmsh.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

/**
 * 文件工具类
 * 
 * @author Frank
 * @date 2015年8月7日下午3:24:41
 */
public class FileUtils {

	private static final double KB = 1024.0;
	private static final double MB = KB * KB;
	private static final double GB = KB * KB * KB;

	/**
	 * 文件重命名
	 * 
	 * @param path
	 *            文件目录
	 * @param oldname
	 *            原来的文件名
	 * @param newname
	 *            新文件名
	 */
	public static void reNameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if (!oldfile.exists()) {
				return;// 重命名文件不存在
			}
			if (newfile.exists()) // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
			{
				return;
			} else {
				oldfile.renameTo(newfile);
			}
		} else {
			return;
		}
	}

	/**
	 * 格式化大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatSize(long size) {
		String fileSize;
		if (size < KB)
			fileSize = size + "B";
		else if (size < MB)
			fileSize = String.format("%.1f", size / KB) + " KB";
		else if (size < GB)
			fileSize = String.format("%.1f", size / MB) + " MB";
		else
			fileSize = String.format("%.1f", size / GB) + " GB";

		return fileSize;
	}

	/**
	 * 读取Assert目录文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getFileFromAssets(Context context, String fileName) {
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 文件如果不存在就创建
	 * 
	 * @param path
	 * @return
	 */
	public static boolean createFile(String path) {
		File file = new File(path);
		boolean mk = false;
		if (!file.exists()) {
			try {
				mk = file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mk;
	}

	public static boolean isFileExists(String path) {
		boolean flag = false;
		File f = new File(path);
		if (f.isFile())
			flag = true;
		Log.i("flag", flag + "");
		return flag;
	}

	/**
	 * getFileSize
	 * 
	 * @param path
	 *            : can be path of file or directory.
	 */
	public static long getFileSize(String path) {
		return getFileSize(new File(path));
	}

	/**
	 * getFileSize
	 * 
	 * @param file
	 *            : can be file or directory.
	 */
	public static long getFileSize(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				long size = 0;
				for (File subFile : file.listFiles()) {
					size += getFileSize(subFile);
				}
				return size;
			} else {
				return file.length();
			}
		} else {
			throw new IllegalArgumentException("File does not exist!");
		}
	}

	/**
	 * copyFile
	 * 
	 * @param originalFilePath
	 * @param destFilePath
	 * @throws IOException
	 */
	public static void copyFile(String originalFilePath, String destFilePath) throws IOException {
		copyFile(new File(originalFilePath), destFilePath);
	}

	/**
	 * copyFile
	 * 
	 * @param originalFilePath
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(String originalFilePath, File destFile) throws IOException {
		copyFile(new File(originalFilePath), destFile);
	}

	/**
	 * copyFile
	 * 
	 * @param originalFile
	 * @param destFilePath
	 * @throws IOException
	 */
	public static void copyFile(File originalFile, String destFilePath) throws IOException {
		copyFile(originalFile, new File(destFilePath));
	}

	/**
	 * copyFile
	 * 
	 * @param originalFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(File originalFile, File destFile) throws IOException {
		copy(new FileInputStream(originalFile), new FileOutputStream(destFile));
	}

	/**
	 * copy
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @throws IOException
	 */
	public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte buf[] = new byte[1024];
		int numRead = 0;

		while ((numRead = inputStream.read(buf)) != -1) {
			outputStream.write(buf, 0, numRead);
		}

		close(outputStream, inputStream);
	}

	/**
	 * deleteFile
	 * 
	 * @param path
	 *            : can be file's absolute path or directories' path.
	 */
	public static void deleteFile(String path) {
		deleteFile(new File(path));
	}

	/**
	 * deleteFile
	 * 
	 * @param file
	 *            : can be file or directory.
	 */
	public static void deleteFile(File file) {
		if (!file.exists()) {
			Log.e("FileUtils", "The file to be deleted does not exist! File's path is: " + file.getPath());
		} else {
			deleteFileRecursively(file);
		}
	}

	/**
	 * deleteFileRecursively Invoker must ensure that the file to be deleted
	 * exists.
	 */
	private static void deleteFileRecursively(File file) {
		if (file.isDirectory()) {
			for (String fileName : file.list()) {
				File item = new File(file, fileName);

				if (item.isDirectory()) {
					deleteFileRecursively(item);
				} else {
					if (!item.delete()) {
						Log.e("FileUtils", "Failed in recursively deleting a file, file's path is: " + item.getPath());
					}
				}
			}

			if (!file.delete()) {
				Log.e("FileUtils",
						"Failed in recursively deleting a directory, directories' path is: " + file.getPath());
			}
		} else {
			if (!file.delete()) {
				Log.e("FileUtils", "Failed in deleting this file, its path is: " + file.getPath());
			}
		}
	}

	/**
	 * readToString
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readToString(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		StringBuffer buffer = new StringBuffer();
		String readLine = null;

		while ((readLine = reader.readLine()) != null) {
			buffer.append(readLine);
			buffer.append("\n");
		}

		reader.close();

		return buffer.toString();
	}

	/**
	 * readToByteArray
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readToByteArray(File file) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		copy(new FileInputStream(file), outputStream);

		return outputStream.toByteArray();
	}

	/**
	 * writeByteArray
	 * 
	 * @param byteArray
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeByteArray(byte[] byteArray, String filePath) throws IOException {
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
		outputStream.write(byteArray);
		outputStream.close();
	}

	/**
	 * saveBitmapToFile
	 * 
	 * @param fileForSaving
	 * @param bitmap
	 */
	public static void saveBitmapToFile(String fileForSaving, Bitmap bitmap) {
		File targetFile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileForSaving + ".png");

		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fos = new FileOutputStream(targetFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * toCatenatedPath If sequentialPathStrs are "a","b","c", below method
	 * return "a/b/c"
	 */
	public static String toCatenatedPath(String... sequentialPathStrs) {
		String catenatedPath = "";
		for (int i = 0; i < sequentialPathStrs.length - 1; i++) {
			catenatedPath += sequentialPathStrs[i] + File.separator;
		}
		catenatedPath += sequentialPathStrs[sequentialPathStrs.length - 1];
		return catenatedPath;
	}

	/**
	 * close
	 * 
	 * @param closeables
	 */
	public static void close(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
