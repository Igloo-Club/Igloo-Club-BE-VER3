package com.igloo_club.nungil_v3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.igloo_club.nungil_v3.exception.GeneralException;
import com.igloo_club.nungil_v3.exception.GlobalErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private static final String S3_BUCKET = "nungil-s3bucket";

    private final AmazonS3Client amazonS3Client;

    public String generatePresignedDownloadUrl(String filename) {
        filename = filename + ".png";

        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, filename)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(getExpiration());

            ResponseHeaderOverrides responseHeaderOverrides = new ResponseHeaderOverrides();
            responseHeaderOverrides.setContentDisposition("inline");
            responseHeaderOverrides.setContentType("image/jpeg");

            generatePresignedUrlRequest.setResponseHeaders(responseHeaderOverrides);

            return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            throw new GeneralException(GlobalErrorResult.PRESIGNED_URL_FAILED);
        }
    }

    public String generatePresignedUploadUrl(String filename) {
        filename = filename + ".png";

        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(S3_BUCKET, filename)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(getExpiration());

            return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (Exception e) {
            throw new GeneralException(GlobalErrorResult.PRESIGNED_URL_FAILED);
        }
    }

    private Date getExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
