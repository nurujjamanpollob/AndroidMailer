package dev.nurujjamanpollob.androidmailer;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidUriToAttachmentUtility {

    private final Uri fileUri;
    private final Activity activity;

    public AndroidUriToAttachmentUtility(Uri uri, Activity activity){
        this.activity = activity;
        this.fileUri = uri;
    }

    public byte[] getFileByte(){

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

    public String getMimeType() {
        String mimeType = null;
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

}
