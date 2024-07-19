package com.youtube.crud.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Mail {
    // Getters and Setters
    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailContent;

}
