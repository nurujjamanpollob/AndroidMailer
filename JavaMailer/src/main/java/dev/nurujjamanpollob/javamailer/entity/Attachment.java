package dev.nurujjamanpollob.javamailer.entity;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class to store attachment data
 */
public class Attachment {

    private final byte[] attachmentByte;
    private final String attachmentName;
    private final String attachmentMimeType;


    /**
     * Constructor param of Attachment Class
     * @param attachmentByte the bytes of attachment file.
     * @param attachmentName Name of the Attachment file
     * @param attachmentMimeType Mime type of Attachment file.
     */
    public Attachment(byte[] attachmentByte, String attachmentName, String attachmentMimeType) {
        this.attachmentByte = attachmentByte;
        this.attachmentName = attachmentName;
        this.attachmentMimeType = attachmentMimeType;
    }

    /**
     * Method to get attachment byte from passed constructor arguments
     * @return the byte array
     */
    public byte[] getAttachmentByte() {
        return attachmentByte;
    }

    /**
     * Method to get Attachment Mime Type from passed constructor arguments
     * @return the String containing file mime type
     */
    public String getAttachmentMimeType() {
        return attachmentMimeType;
    }

    /**
     * Method to get Attachment File name from passed constructor arguments
     * @return the Attachment file name
     */
    public String getAttachmentName() {
        return attachmentName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentByte=" + Arrays.toString(attachmentByte) +
                ", attachmentName='" + attachmentName + '\'' +
                ", attachmentMimeType='" + attachmentMimeType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment)) return false;
        Attachment that = (Attachment) o;
        return Arrays.equals(attachmentByte, that.attachmentByte) && Objects.equals(attachmentName, that.attachmentName) && Objects.equals(attachmentMimeType, that.attachmentMimeType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(attachmentName, attachmentMimeType);
        result = 31 * result + Arrays.hashCode(attachmentByte);
        return result;
    }

    /**
     * Method to check if attachment byte length is not null
     * @return true if attachment byte is not null and the array length > 0 else false
     */
    public boolean isAttachmentNotNull(){
        return (attachmentByte != null ? attachmentByte.length : 0) > 0;
    }
}
