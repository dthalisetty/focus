package com.zetaglobal.focusapi.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "focus_user_file_uploads")
public class FileUploadMetaData {

	// Columns to insert file-related data
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name = "custom-id-generator", strategy = "com.zetaglobal.focus.IdGenerator")
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "user_nm")
	private String userName;
	@Column(name = "user_email")
	private String userEmail;
	@Column(name = "upload_file_nm")
	private String uploadFileName;
	@Column(name = "upload_file_dtm")
	private LocalDateTime uploadFileDtm;
	// Column to read file processing status
	@Column(name = "status")
	private String status;
	@Column(name = "message")
	private String message;

	public FileUploadMetaData(Long userId, String userName, String userEmail, String uploadFileName, LocalDateTime uploadFileDtm,
			String status, String message) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.uploadFileName = uploadFileName;
		this.uploadFileDtm = uploadFileDtm;
		this.status = status;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FileUploadMetaData() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public LocalDateTime getUploadFileDtm() {
		return uploadFileDtm;
	}

	public void setUploadFileDtm(LocalDateTime uploadFileDtm) {
		this.uploadFileDtm = uploadFileDtm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

