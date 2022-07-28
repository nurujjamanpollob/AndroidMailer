/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-", "$today.year")2022 Nurujjaman Pollob, All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * If you have contributed in codebase,
 * and want to add your name or copyright in a particular class or method,
 * you must follow this following pattern:
 * <code>
 *     // For a new method created by you,
 *     //like this example method with name fooMethod()
 *     //then use following format:
 *
 *     >>>
 *     @author $Name and $CurrentYear.
 *     $Documentation here.
 *     $Notes
 *     public boolean fooMethod(){}
 *     <<<
 *
 *     // For an existing method
 *
 *     >>>
 *     $Current Method Documentation(Update if needed)
 *
 *     Updated by $YourName
 *     $Update summery
 *     $Notes(If any)
 *     <<<
 *
 *     // For a new class of file, that is not created by anyone else
 *     >>>
 *     Copyright (c) $CurrentYear $Name, All right reserved.
 *
 *     $Copyright Text.
 *     $Notes(If Any)
 *     <<<
 *
 *     // For a existing class, if you want to add your own copyright for your work.
 *
 *     >>>
 *     $Current Copyright text
 *
 *     $YourCopyrightText
 *     <<<
 *
 *     Done! Clean code!!
 * </code>
 *
 *
 */

package dev.nurujjamanpollob.javamailer.entity;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Objects;

import dev.nurujjamanpollob.javamailer.CommonFunctions;
import dev.nurujjamanpollob.javamailer.utility.AttachmentException;

/**
 * Class to store attachment data
 *
 * This class is can't be inherited, can be used to store attachment data only.
 */
public final class Attachment {

    private final byte[] attachmentByte;
    private final String attachmentName;
    private final String attachmentMimeType;


    /**
     * Constructor param of Attachment Class
     * @param attachmentByte the bytes of attachment file.
     * @param attachmentName Name of the Attachment file
     * @param attachmentMimeType Mime type of Attachment file.
     */
    public Attachment(byte[] attachmentByte, String attachmentName, String attachmentMimeType) throws AttachmentException {

        // Check if byte length is greater than 25 MB, if yes throw exception
        CommonFunctions.ByteUnitInformation byteUnitInformation = CommonFunctions.convertByteToHumanReadableUnit(attachmentByte.length);

        if(byteUnitInformation.getUnit() == CommonFunctions.ByteUnit.MEGA_BYTE && byteUnitInformation.getValue() > 25){
            throw new AttachmentException("Attachment size is greater than 25 MB, please attachments less than 25 MB");
        }

        this.attachmentByte = attachmentByte;
        this.attachmentName = attachmentName;
        this.attachmentMimeType = attachmentMimeType;
    }

    /**
     * Suppress default constructor for non-instantiability without arguments
     */
    private Attachment() {
        throw new AssertionError("Class cannot be instantiated without arguments");
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
