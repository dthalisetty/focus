package com.zetaglobal.focusapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zetaglobal.focusapi.model.FileUploadMetaData;
import com.zetaglobal.focusapi.service.FileHandlingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/focus")
public class FileHandlingController {

	private static final Logger logger = LoggerFactory.getLogger(FileHandlingController.class);

	@Autowired
	FileHandlingService filesHandlingService;

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> uploadFile(@RequestPart("file") MultipartFile multipartFile,
			@RequestParam("data") String userData) {
		logger.info("Begin uploadFile() - Controller");

		String response = null;
		try {
			if (multipartFile != null) {
				if (multipartFile.isEmpty()) {
					return new ResponseEntity<Object>("File is Empty!", HttpStatus.BAD_REQUEST);
				} else {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(userData);
					FileUploadMetaData fileUploadMetaData = new FileUploadMetaData();
					fileUploadMetaData.setUserName(jsonNode.get("user_nm").asText());
					fileUploadMetaData.setUserEmail(jsonNode.get("user_email").asText());
					response = filesHandlingService.uploadFile(multipartFile, fileUploadMetaData);
				}
			}
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception occured at uploadFile() - Controller: " + e.getMessage());
			return new ResponseEntity<Object>("There seems to be some error. Please check the file properly !",
					HttpStatus.BAD_REQUEST);
		} finally {
			logger.info("Completed uploadFile() - Controller");
		}

	}
	
	@RequestMapping(value = "/getAllFileEntryDetails", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllFileEntryDetails(HttpServletRequest httpServletRequest) {
		logger.info("Begin getAllFileEntryDetails() - Controller");
		try {
			Object fileEntryDetails = filesHandlingService.getAllFileEntryDetails();
			return new ResponseEntity<Object>(fileEntryDetails, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception occured at uploadFile() - Controller: " + e.getMessage());
			return new ResponseEntity<Object>("There seems to be some error. Please check the file properly !",
					HttpStatus.BAD_REQUEST);
		} finally {
			logger.info("Completed uploadFile() - Controller");
		}

	} 

}
