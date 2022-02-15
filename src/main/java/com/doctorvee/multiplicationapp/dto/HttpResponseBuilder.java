package com.doctorvee.multiplicationapp.dto;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HttpResponseBuilder {

    private static final String FILE_NAME_HEADER_FORMAT = "attachment; filename=\"%s\"";

    public ResponseEntity<Resource> fromDownloadableResource(DownloadableResource resource) {

        if (resource.isEmpty() || resource.getFile().length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentLength(resource.getFile().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(FILE_NAME_HEADER_FORMAT, resource.getFileName()))
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(resource.getFile()));
    }
}
