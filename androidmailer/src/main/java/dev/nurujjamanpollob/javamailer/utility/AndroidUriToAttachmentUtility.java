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

package dev.nurujjamanpollob.javamailer.utility;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.nurujjamanpollob.javamailer.CommonFunctions;
import dev.nurujjamanpollob.javamailer.Variables;
import dev.nurujjamanpollob.javamailer.entity.Attachment;

public class AndroidUriToAttachmentUtility {

    private final Uri fileUri;
    private final Activity activity;

    public AndroidUriToAttachmentUtility(Uri uri, Activity activity){
        this.activity = activity;
        this.fileUri = uri;
    }

    /**
     * Method to get byte array content resolver from uri
     * @return the file byte array from uri
     * @throws AttachmentException if the file length is more than 25 MB
     */
    public byte[] getFileByte() throws AttachmentException{

        // check attachment length
        CommonFunctions.ByteUnitInformation byteUnitInformation = CommonFunctions.convertByteToHumanReadableUnit(getAttachmentSize());

        // File is larger than 25 MB? throw exception
        if(byteUnitInformation.getUnit() == CommonFunctions.ByteUnit.MEGA_BYTE && byteUnitInformation.getValue() > Variables.MAX_FILE_UPLOAD_SIZE_IN_MB){
            throw new AttachmentException("Attachment size is more than 25 MB");
        }

        //get file path
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            InputStream in = activity.getContentResolver().openInputStream(fileUri);

            byteArrayOutputStream = new ByteArrayOutputStream();
            int nRead;

            byte[] d = new byte[16384];
            while ((nRead = in.read(d, 0, d.length)) != -1) {
                byteArrayOutputStream.write(d, 0, nRead);
            }
        } catch (IOException e) {
            return null;
        }
       return byteArrayOutputStream.toByteArray();
    }

    /**
     * Method to get file name from uri
     * @return the file name from uri
     */
    public String getFileName() {
        String result;
        Uri fileNameUri = null;

        //if uri is content
        if (fileUri.getScheme() != null && fileUri.getScheme().equals("content")) {
            try (Cursor cursor = activity.getContentResolver().query(fileUri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    //local filesystem
                    int index = cursor.getColumnIndex("_data");
                    if (index == -1)
                        //google drive
                        index = cursor.getColumnIndex("_display_name");
                    result = cursor.getString(index);
                    if (result != null)
                        fileNameUri = Uri.parse(result);
                    else
                        return null;
                }
            }
        }

        result = fileNameUri != null ? fileNameUri.getPath() : null;

        //get filename + ext of path
        int cut = result != null ? result.lastIndexOf('/') : 0;
        if (cut != 0)
            result = result.substring(cut + 1);

        return result;
    }

    /**
     * Get content type of file from uri
     * @return the content type of file
     */
    public String getMimeType() {
        String mimeType;
        if (ContentResolver.SCHEME_CONTENT.equals(fileUri.getScheme())) {
            ContentResolver cr = activity.getContentResolver();
            mimeType = cr.getType(fileUri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public Attachment getAttachmentInstance() throws AttachmentException{

        return new Attachment(getFileByte(), getFileName(), getMimeType());
    }

    /**
     * Method to calculate the file byte length
     * @return the file byte length from uri
     */
    private long getAttachmentSize(){

        Cursor returnCursor = activity.getContentResolver().query(fileUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        long contentSize = returnCursor.getLong(sizeIndex);

        // close the cursor
        returnCursor.close();

        return contentSize;
    }

}
