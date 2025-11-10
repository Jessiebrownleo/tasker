package com.example.tasker.feature.attachment.dto;

import lombok.Data;

@Data
public class CreateAttachmentRequest {
    private String fileName;
    private String url;
}
