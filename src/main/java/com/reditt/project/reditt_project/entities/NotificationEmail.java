package com.reditt.project.reditt_project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationEmail {

    private String body;
    private String recipient;
    private String subject;
}

