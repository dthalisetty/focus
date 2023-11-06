package com.zetaglobal.focusapi.service;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zetaglobal.focusapi.model.FileUploadMetaData;
import com.zetaglobal.focusapi.repository.FilesHandlingRepository;

@Service
public class FileHandlingService {
	private static final Logger logger = LoggerFactory.getLogger(FileHandlingService.class);

	@Autowired
	FilesHandlingRepository filesHandlingRepository;
	
	boolean connectionEstablished;

	public String uploadFile(MultipartFile multipartFile, FileUploadMetaData fileUploadMetaData) {
		logger.info("Begin uploadFile() - Service");
		try {
			String fileName = multipartFile.getOriginalFilename();
			fileUploadMetaData.setUploadFileName(fileName);
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect("eu-central-1.sftpcloud.io");
			ftpClient.login("1a071db18c5e40e89b58258c271f967f", "Ea2qNhiBdkMwCAdycAzyOaXKhXj16Mf0");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
//			String fileName = multipartFile.getOriginalFilename();
//			fileUploadMetaData.setUploadFileName(fileName);
			if (!fileName.endsWith(".xlsx")) {
				return "File format not supported. Please upload a .xlsx file.";
			}
			Long userId = filesHandlingRepository.save(fileUploadMetaData).getUserId();
			String fileNameToBeSaved = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + String.valueOf(userId)
					+ ".xlsx";
			connectionEstablished = ftpClient.storeFile(fileNameToBeSaved, multipartFile.getInputStream());
			if (connectionEstablished) {
				saveIntoAuditLog(fileUploadMetaData, "R", "File " + fileName + " uploaded successfully !");
				ftpClient.logout();
				ftpClient.disconnect();
				return "File uploaded successfully!";
			} else {
				saveIntoAuditLog(fileUploadMetaData, "F", "File " + fileName + " upload failed !");
				ftpClient.logout();
				ftpClient.disconnect();
				return "File upload failed.";
			}
		} catch (UnknownHostException e) {
			logger.error("Excpetion occurred at uploadFile() - Service : " + e);
			saveIntoAuditLog(fileUploadMetaData, "F", "Error occurred in uploading the file due to improper host details !");
			return "Please enter the SFTP Host details properly !";
		} catch (FTPConnectionClosedException e) {
			logger.error("Excpetion occurred at uploadFile() - Service : " + e);
			saveIntoAuditLog(fileUploadMetaData, "F", "Error occurred in uploading the file due to server connection issue !");
			return "Error occurred in uploading the file due to server connection issue.";
		} catch (Exception e) {
			logger.error("Exception occurred at uploadFile() - Service : " + e);
			saveIntoAuditLog(fileUploadMetaData, "F", "Error occurred in uploading the file !");
			return "Error occurred in uploading the file.";
		} finally {
			logger.info("Completed uploadFile() - Service");
		}
	}
	
	public void saveIntoAuditLog(FileUploadMetaData fileUploadMetaData, String status, String message) {
		fileUploadMetaData.setMessage(message);
		fileUploadMetaData.setUploadFileDtm(LocalDateTime.now(ZoneId.of("UTC")));
		fileUploadMetaData.setStatus(status);
		filesHandlingRepository.save(fileUploadMetaData);
	}

	public Object getAllFileEntryDetails() {
		logger.info("Begin getAllFileEntryDetails() - Service");
		try {
			List<FileUploadMetaData> fileEntryDetails = filesHandlingRepository.findAll();
			if (!fileEntryDetails.isEmpty()) {
				return fileEntryDetails;
			} else {
				return "There is no data available !";
			}
		} catch (Exception e) {
			logger.error("Exception occurred at getAllFileEntryDetails() - Service : " + e);
		} finally {
			logger.info("Completed getAllFileEntryDetails() - Service");
		}
		return null;
	}

}
