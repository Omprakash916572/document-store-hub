package com.dev.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.DTO.UserImageDocumentsDTO;
import com.dev.DTO.UserImageDocumentsResponseDTO;
import com.dev.service.UserDocumentService;

@RestController
@RequestMapping("/document/v1")
@CrossOrigin("*")
public class UserDocumentController {

	private static final Logger _logger = LoggerFactory.getLogger(UserDocumentController.class);

	@Autowired
	private UserDocumentService userDocumentService;

	@PostMapping("/upload-document")
	public UserImageDocumentsResponseDTO uploadUserImage(@ModelAttribute UserImageDocumentsDTO userImageDocumentsDTO) throws Exception {
		_logger.info("create user function call");

		UserImageDocumentsResponseDTO uploadUserImageData = userDocumentService.uploadUserImageData(userImageDocumentsDTO);
		_logger.info("create user function ended");
		return uploadUserImageData;
	}

	@GetMapping("/download-image-to-pdf-document/{imageId}")
	public Resource downloadImagePdf(@PathVariable Integer imageId,HttpServletResponse response) throws NotFoundException {
		_logger.info("download pdf function called.");
		Resource downloadImageResource = userDocumentService.downloadImageResource(imageId,response);
		return downloadImageResource;
	}
	
//	@PostMapping("/multiFile")
//    public @ResponseBody List<String> handleFileUpload(
//    		@ModelAttribute("files") List<MultipartFile> files) throws IOException {
//        List<String> uploadedFileNames = new ArrayList<>();
//        
//        for (MultipartFile file : files) {
//            if (!file.isEmpty()) {
//                String fileName = file.getOriginalFilename();
//                // You can save or process the file here.
//                // For demonstration purposes, we're just collecting the file names.
//                uploadedFileNames.add(fileName);
//            }
//        }
//
//        return uploadedFileNames;
//    }
}
