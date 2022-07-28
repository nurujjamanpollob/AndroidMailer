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
 */

package dev.nurujjamanpollob.javamailer;

import dev.nurujjamanpollob.javamailer.entity.Attachment;
import dev.nurujjamanpollob.javamailer.utility.AttachmentException;

/**
 * This class can't be instantiated
 */
public final class CommonFunctions {

    /**
     * Suppress default constructor for non-instantiability
     */
    private CommonFunctions() {
        throw new AssertionError("Class cannot be instantiated");
    }

    /**
     * Byte to KiloBye or MegaByte Converter
     * @param bytes the byte to convert to human readable format
     * @return {@link ByteUnitInformation} object
     */
    public static ByteUnitInformation convertByteToHumanReadableUnit(long bytes){

        ByteUnitInformation byteUI;

        if(bytes < 1000) {
            byteUI = new ByteUnitInformation(bytes, ByteUnit.BYTE);
        }
        else if(bytes < 1048576) {
            byteUI = new ByteUnitInformation(bytes / 1_000.0, ByteUnit.KILO_BYTE);
        }
        else {
            byteUI = new ByteUnitInformation(bytes / 1_000_000.0, ByteUnit.MEGA_BYTE);
        }
        return byteUI;

    }


    public enum ByteUnit {
        BYTE,
        KILO_BYTE,
        MEGA_BYTE
    }

    public static class ByteUnitInformation{

        private final ByteUnit unit;
        private final double value;

        public ByteUnitInformation(double value, ByteUnit unit){
            this.unit = unit;
            this.value = value;
        }

        public  double getValue() {
            return value;
        }

        public ByteUnit getUnit() {
            return unit;
        }
    }

    /**
     * Method to validate a single {@link Attachment#getAttachmentByte()} byte length.
     * @param attachment the attachment to validate file size
     */
    public static void validateAttachmentsSize(Attachment attachment) throws AttachmentException {

        CommonFunctions.ByteUnitInformation byteUnitInformation = CommonFunctions.convertByteToHumanReadableUnit(attachment.getAttachmentByte().length);

        if(byteUnitInformation.getUnit() == ByteUnit.MEGA_BYTE && byteUnitInformation.getValue() > Variables.MAX_FILE_UPLOAD_SIZE_IN_MB){
            throw new AttachmentException("Attachment size is too large. Max size is " + Variables.MAX_FILE_UPLOAD_SIZE_IN_MB + " MB");
        }

    }

    /**
     * Method to validate multiple {@link Attachment#getAttachmentByte()} byte length.
     * @param attachments the attachment to validate file size
     */
    public static void validateAttachmentsSize(Attachment[] attachments) throws AttachmentException {

        long totalSize = 0;
        // Run a iteration loop and collect each attachment size, and concat to total size.
        for(Attachment attachment : attachments){
            totalSize += attachment.getAttachmentByte().length;
        }

        CommonFunctions.ByteUnitInformation byteUnitInformation = CommonFunctions.convertByteToHumanReadableUnit(totalSize);

        if(byteUnitInformation.getUnit() == ByteUnit.MEGA_BYTE && byteUnitInformation.getValue() > Variables.MAX_FILE_UPLOAD_SIZE_IN_MB){
            throw new AttachmentException("Attachments size is too large. The collective length should not exceed " + Variables.MAX_FILE_UPLOAD_SIZE_IN_MB + " MB");
        }

    }
}
