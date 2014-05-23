package ru.terra.jbrss.dto.captcha;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CaptchaResponseDTO extends CaptchaCommonDTO {
    public String val = "";

    public CaptchaResponseDTO() {
    }
}
