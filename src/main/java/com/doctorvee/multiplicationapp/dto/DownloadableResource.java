package com.doctorvee.multiplicationapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadableResource {

    public static final DownloadableResource EMPTY = new DownloadableResource(null, new byte[0]);

    private String fileName;
    private byte[] file;

    public boolean isEmpty() {
        return equals(EMPTY);
    }
}
