package com.recruvia.backend.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction for file storage operations.
 * Implement this interface to switch between local storage,
 * Cloudinary, Cloudflare R2, AWS S3, etc.
 */
public interface StorageService {

    /**
     * Stores the given file and returns a publicly accessible URL or path.
     *
     * @param file the multipart file to store
     * @return the public URL or relative path of the stored file
     */
    String storeFile(MultipartFile file);

    /**
     * Deletes the file at the given URL or path.
     *
     * @param fileUrl the URL or path of the file to delete
     */
    void deleteFile(String fileUrl);

}
