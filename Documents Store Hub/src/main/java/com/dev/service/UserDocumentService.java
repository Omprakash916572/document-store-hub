package com.dev.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import com.dev.DTO.DocumentDTO;
import com.dev.DTO.DocumentsResponseDTO;
import com.dev.DTO.UserImageDocumentsDTO;
import com.dev.DTO.UserImageDocumentsResponseDTO;
import com.dev.entity.Documents;
import com.dev.entity.UserImageDocuments;
import com.dev.repo.DocumentsRepo;
import com.dev.repo.UserImageDocumentsRepo;
import com.dev.repo.UserRepository;
import com.dev.utils.MIMETypes;
import com.dev.utils.ProjectJsonUtils;
import com.dev.utils.ProjectJsonUtils.DocumentCategory;
import com.dev.utils.ProjectJsonUtils.ImageExtensions;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class UserDocumentService {

	private static final Logger _logger = LoggerFactory.getLogger(UserDocumentService.class);

	@Autowired
	private UserImageDocumentsRepo userImageDocumentsRepo;

	@Autowired
	private DocumentsRepo cocumentsRepo;
	
	@Autowired
    private UserRepository userRepository;

	@Value("${user.documents.image.location}")
	private String userDocumentImageLocation;

	@Value("${user.documents.pdf.location}")
	private String userDocumentPdfLocation;

//	public User userLogin(User user) {
//
//		User userObj = null;
//		if (!user.getEmail().equals("") && !user.getEmail().isEmpty()) {
//
//			if (user.getPassword().equals("") && user.getPassword().isEmpty()) {
//				// throw new CustomException("password can not be empty.");
//			}
//			userObj = userRepo.findByEmail(user.getEmail());
//
//			if (Objects.isNull(userObj)) {
//				_logger.debug("user not exist.");
//				// throw new CustomException("User not exist.");
//			} else {
//				String inputPassword = user.getPassword();
//				if (!userObj.getPassword().equals(inputPassword)) {
//					// throw new CustomException("password is wrong!!.");
//				}
//			}
//		} else {
//			// throw new CustomException("email can not be empty.");
//		}
//		return userObj;
//
//	}
//
//	public User userLoginV2(String email, String password) {
//
//		User userObj = null;
//		if (!email.equals("") && !email.isEmpty()) {
//
//			if (password.equals("") && password.isEmpty()) {
//				// throw new CustomException("password can not be empty.");
//			}
//			userObj = userRepo.findByEmail(email);
//
//			if (Objects.isNull(userObj)) {
//				_logger.debug("user not exist.");
//				/// throw new CustomException("User not exist.");
//			} else {
//
//				if (!userObj.getPassword().equals(password)) {
//					// throw new CustomException("password is wrong!!.");
//				}
//			}
//		} else {
//			// throw new CustomException("email can not be empty.");
//		}
//		return userObj;
//
//	}

	@Transactional
	public UserImageDocumentsResponseDTO uploadUserImageData(UserImageDocumentsDTO userImageDocumentsDTO)
			throws Exception {

		UserImageDocumentsResponseDTO userImageDocumentsResponseDTO = null;
		if (Objects.nonNull(userImageDocumentsDTO)) {

			if (Objects.nonNull(userImageDocumentsDTO.getImage()) && !userImageDocumentsDTO.getImage().isEmpty()) {

				UserImageDocuments userImageDocumentsObject = new UserImageDocuments();

				// Entity to DTO
				userImageDocumentsObject = this.dtoToEntity(userImageDocumentsDTO, userImageDocumentsObject);

				Documents documents = new Documents();

				ImageExtensions imageExtensions = ProjectJsonUtils.ImageExtensions
						.fromString(userImageDocumentsDTO.getFile_extension());

				if (Objects.nonNull(imageExtensions)) {
					documents.setDocumentType(userImageDocumentsDTO.getFile_extension().toLowerCase());
				} else {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This format not supported .");
				}
//				if (userImageDocumentsDTO.getFile_extension().equalsIgnoreCase("jpg")
//						|| userImageDocumentsDTO.getFile_extension().equalsIgnoreCase("png")
//						|| userImageDocumentsDTO.getFile_extension().equalsIgnoreCase("jpeg")) {
//
//					documents.setDocumentType(userImageDocumentsDTO.getFile_extension().toLowerCase());
//
//				} else if (userImageDocumentsDTO.getFile_extension().equalsIgnoreCase("txt")
//						|| userImageDocumentsDTO.getFile_extension().equalsIgnoreCase("doc")
//						|| userImageDocumentsDTO.getFile_extension().equalsIgnoreCase("xls")) {
//					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "txt , doc ,xls not supported yet .");
//				}else {
//					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This format not supported .");
//				}

				if (Objects.nonNull(userImageDocumentsDTO.getDocument_category())
						&& !userImageDocumentsDTO.getDocument_category().isEmpty()) {
					DocumentCategory documentCategoryEnum = ProjectJsonUtils.DocumentCategory
							.fromString(userImageDocumentsDTO.getDocument_category());
					if (Objects.nonNull(documentCategoryEnum)) {
						documents.setDocumenCategory(userImageDocumentsDTO.getDocument_category());
					} else {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
								"Please select valid document category.", null);
					}
				} else {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doucument category required", null);
				}

				// Save Documents into Local storage
				String modifiedFileName = this.saveImageToLocalStorage(userImageDocumentsDTO.getImage());
				String convertImageExtensionIntoLowerCase = convertImageExtensionIntoLowerCase(modifiedFileName);

				if (!convertImageExtensionIntoLowerCase.equals("")) {
					userImageDocumentsObject.setLocalStorageImageDocumentName(convertImageExtensionIntoLowerCase);
				} else {
					userImageDocumentsObject.setLocalStorageImageDocumentName(modifiedFileName);
				}

				// Convert image to pdf
				String imageToPdf = this.imageToPdf(userImageDocumentsObject.getLocalStorageImageDocumentName());

				if (!imageToPdf.equals("")) {
					userImageDocumentsObject.setLocalStoragePdfDocumentName(imageToPdf);
				} else {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went to wrong.",
							null);
				}

				documents.setCreateTs(dateAndTime());
				documents.setUpdateTs(dateAndTime());
				Documents documentObjectFromFb = cocumentsRepo.save(documents);

				userImageDocumentsObject.setDocument(documentObjectFromFb);
				userImageDocumentsObject.setIsActive(true);

				UserImageDocuments userDocumentsResponseFromDB = userImageDocumentsRepo.save(userImageDocumentsObject);
				userImageDocumentsObject.setLocalStoragePdfDocumentName(imageToPdf);
				userImageDocumentsResponseDTO = new UserImageDocumentsResponseDTO();
				userImageDocumentsResponseDTO = entityToDTO(userDocumentsResponseFromDB, userImageDocumentsResponseDTO);

			} else {
				_logger.info("documents is empty");
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "documents is empty", null);
			}

		}
		return userImageDocumentsResponseDTO;
	}

	private UserImageDocumentsResponseDTO entityToDTO(UserImageDocuments sourceObject,
			UserImageDocumentsResponseDTO targetObject) {

		if (sourceObject.getId() != null) {
			targetObject.setId(sourceObject.getId());
		}
		if (sourceObject.getOrignalDocumentName() != null) {
			targetObject.setDocument_name(sourceObject.getOrignalDocumentName());
		}
//		if (sourceObject.getDocumenCategory() != null) {
//			targetObject.setDocument_category(sourceObject.getDocumenCategory());
//		}
		if (sourceObject.getId() != null) {
			targetObject.setDocument_status("document save successfully !!.");
		}
//		if (sourceObject.getImageDocumentData() != null) {
//			targetObject.setImageData(sourceObject.getImageDocumentData());
//		}
		return targetObject;

	}

	public Resource downloadImageResource(Integer imageId, HttpServletResponse response) throws NotFoundException {
		UserImageDocuments userImageDocuments = userImageDocumentsRepo.findById(imageId).orElse(null);
		if (userImageDocuments == null) {
			_logger.info("file not found.");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "file not found.", null);
		}

		Resource resource = null;
		if (userImageDocuments.getLocalStoragePdfDocumentName() != null
				&& !userImageDocuments.getLocalStoragePdfDocumentName().isEmpty()) {

			resource = loadAsResource(userImageDocuments.getLocalStoragePdfDocumentName());
			userImageDocuments.setOrignalDocumentName(userImageDocuments.getOrignalDocumentName()
					.replaceAll(".jpg", ".pdf").replaceAll(".png", ".pdf").replaceAll(".jpeg", ".pdf"));

		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "document not exist.", null);
		}

		if (resource.exists()) {

			String encodedFilename = UriUtils.encode(userImageDocuments.getOrignalDocumentName(),
					StandardCharsets.UTF_8);
			response.setHeader("Content-disposition", "attachment; filename=\"" + encodedFilename + "\"");
			response.addHeader("mime-type", this.getContentTypeForFile(userImageDocuments.getOrignalDocumentName()));
			response.setContentType(this.getContentTypeForFile(userImageDocuments.getDocument().getDocumentType()));

			_logger.info("documents downloded.");
			return resource;
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "document not exist in local disk.",
					null);
		}
	}

	public Resource loadAsResource(String filename) {

		Resource resource = new FileSystemResource(this.userDocumentPdfLocation + filename);

		if (resource.exists() || resource.isReadable()) {
			return resource;
		} else {
			_logger.error("Could not read file from local storage: " + filename);
		}

		return null;
	}

	public String getContentTypeForFile(String fileType) {

		String contentType = MIMETypes.mappings.get(fileType.toLowerCase());

		if (Objects.isNull(contentType)) {
			contentType = MediaType.TEXT_PLAIN_VALUE;
		}

		return contentType;
	}
//	public String storeFile(MultipartFile file) throws Exception {
//
//		_logger.info("Storing user ducuments before save into database");
//		Date date = new Date();
//
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		fileName = "document_" + date.getTime() + "_" + fileName;
//
//		Path fileStorageLocationPath = Paths.get(this.documetsStorelocation + fileName);
//
//		try {
//			// Copy file to the target location (Replacing existing file with the same name)
//			Files.copy(file.getInputStream(), fileStorageLocationPath, StandardCopyOption.REPLACE_EXISTING);
//			_logger.debug("Stored Documents file before uploading with name : " + fileName);
//			return fileName;
//		} catch (IOException ex) {
//			_logger.error("Something went wrong when user ducuments before save into database : " + ex.getMessage());
//			_logger.error("Detailed error is : ", ex);
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//					"Could not store file. Please try again", null);
//		} catch (NullPointerException ex) {
//			_logger.error("Something went wrong when user ducuments before save into database : " + ex.getMessage());
//			_logger.error("Detailed error is : ", ex);
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//					"File could not be converted from Csv to Xlsx format.", null);
//		}
//	}

	public String saveImageToLocalStorage(MultipartFile uploadedFile) {
		try {
			Date date = new Date();
			String fileName = StringUtils.cleanPath(uploadedFile.getOriginalFilename());
			fileName = "document_" + date.getTime() + "_" + fileName;

			File file = new File(this.userDocumentImageLocation + fileName);
			uploadedFile.transferTo(file);
			_logger.info("document saved into local storage.");
			return fileName;

		} catch (IOException ex) {
			_logger.error("Something went wrong when save user ducuments before into database : " + ex.getMessage());
			_logger.error("Detailed error is : ", ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Could not store file. Please try again", null);
		} catch (NullPointerException ex) {
			_logger.error("Something went wrong when user ducuments before save into database : " + ex.getMessage());
			_logger.error("Detailed error is : ", ex);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not store file. Please try again.", null);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not store file. Please try again.", null);
		}
	}

	private UserImageDocuments dtoToEntity(UserImageDocumentsDTO sourceObject, UserImageDocuments targetObject)
			throws IOException {

		if (Objects.nonNull(sourceObject.getFile_extension())) {

			String documentExtension = FilenameUtils.getExtension(sourceObject.getImage().getOriginalFilename());

			if (Objects.isNull(sourceObject.getFile_extension()) || sourceObject.getFile_extension().isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Document extension required.");
			}
			if (!sourceObject.getFile_extension().equalsIgnoreCase(documentExtension)) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
						"Extension not match with uploaded document .");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file extension required", null);
		}

		String convertImageExtensionIntoLowerCase = convertImageExtensionIntoLowerCase(
				sourceObject.getImage().getOriginalFilename());

		if (!convertImageExtensionIntoLowerCase.equals("")) {
			targetObject.setOrignalDocumentName(convertImageExtensionIntoLowerCase);
		} else {
			targetObject.setOrignalDocumentName(sourceObject.getImage().getOriginalFilename());
		}

		InputStream is = sourceObject.getImage().getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);
		String encoded = Base64.getEncoder().encodeToString(bytes);
		targetObject.setImageDocumentData(encoded);

		if (sourceObject.getName() != null && !sourceObject.getName().isEmpty()) {
			targetObject.setUserName(sourceObject.getName());
		} else {
			targetObject.setUserName("omprakash");
		}
		targetObject.setCreateTs(this.dateAndTime());
		targetObject.setUpdateTs(this.dateAndTime());

		return targetObject;
	}

	public Timestamp dateAndTime() {
		LocalDateTime date = LocalDateTime.now();
		DateTimeZone tz = DateTimeZone.getDefault();
		Timestamp ts = new Timestamp(date.toDateTime(tz).toDateTime(DateTimeZone.UTC).getMillis());
		return ts;
	}

	public String convertImageExtensionIntoLowerCase(String imageName) {

		String[] split = null;
		String modifedImageExtensionInLowerCase = "";

		if (imageName.contains(".JPG")) {
			split = imageName.split(".JPG");
			modifedImageExtensionInLowerCase = split[0] + ".jpg";

		} else if (imageName.contains(".JPEG")) {
			split = imageName.split(".JPEG");
			modifedImageExtensionInLowerCase = split[0] + ".jpeg";

		} else if (imageName.contains(".PNG")) {

			split = imageName.split(".PNG");
			modifedImageExtensionInLowerCase = split[0] + ".png";
		}
		return modifedImageExtensionInLowerCase;

	}

	public String imageToPdf(String documentOrignalName) {
		_logger.info("imageToPdf function called.");

		Document document = new Document();
		String fileName = documentOrignalName.replaceAll(".jpg", "").replaceAll(".png", "").replaceAll(".jpeg", "")
				+ ".pdf";

		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(document,
					new FileOutputStream(new File(this.userDocumentPdfLocation, fileName)));
			document.open();

			document.newPage();
			Image image = Image
					.getInstance(new File(this.userDocumentImageLocation, documentOrignalName).getAbsolutePath());
			image.setAbsolutePosition(0, 0);
			image.setBorderWidth(10);
			image.scaleAbsoluteHeight(PageSize.A4.getHeight());
			image.scaleAbsoluteWidth(PageSize.A4.getWidth());
			document.add(image);

			document.close();
			pdfWriter.close();

			_logger.info("image pdf name stored location : " + this.userDocumentPdfLocation);
			_logger.info("image document pdf name : " + fileName);
			_logger.info("image saved succesfully.");

			return fileName;
		} catch (Exception ex) {
			_logger.info("Something went wrong when convert image to pdf.");
			ex.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong to convert image to pdf.",
					null);
		}
	}

	public String getFileExtension(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null) {
			int dotIndex = originalFilename.lastIndexOf('.');
			if (dotIndex >= 0) {
				return originalFilename.substring(dotIndex + 1);
			}
		}
		return null;
	}

	public List<DocumentDTO> getDocumentsList(DocumentDTO documentDTO) {

		_logger.info("get document list function called.");

		List<DocumentDTO> userDocumentDTO =null;
		if (Objects.nonNull(documentDTO)) {

			if (documentDTO.getUserId() != null) {

				userDocumentDTO = userRepository.findDocumentsByUserId(documentDTO.getUserId());
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId Required !!.", null);
			}

		}
		return userDocumentDTO;
	}

}
