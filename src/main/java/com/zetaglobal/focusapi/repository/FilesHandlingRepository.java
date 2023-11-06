package com.zetaglobal.focusapi.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zetaglobal.focusapi.model.FileUploadMetaData;

@Repository
public interface FilesHandlingRepository extends JpaRepository<FileUploadMetaData, Long> {
		
}

