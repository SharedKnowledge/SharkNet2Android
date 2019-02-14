package net.sharksystem.bubble.android.contentProvider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class LogWriter {
    private int era = 0;
    private String currentFileName;
    private String fileName;
    private String fullPath;
    private DataOutputStream os;

    private final String ERA_EXTENSION = "_syncEra";

    LogWriter(File dir, String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.fullPath = dir.getAbsolutePath() + "/";

        // try to read previous era
        try {
            DataInputStream eraIN = new DataInputStream(new FileInputStream(this.getEraFileName()));
            this.era = eraIN.read();
            this.nextEra(); // don't overwrite existing file
        } catch (Exception e) {
            // no era file - ok
        }

        // open stream
        this.os = new DataOutputStream(new FileOutputStream (this.getLogFileName()));
    }

    private String getEraFileName() {
        return this.fullPath + this.fileName + ERA_EXTENSION;
    }

    private String getLogFileName() {
        return this.fullPath + this.fileName + Integer.toString(this.era);
    }

    DataOutputStream getLogStream() {
        return this.os;
    }

    private void nextEra() throws IOException {
        if(this.os != null) {
            this.os.close();
            this.os = null;
        }

        this.era++;

        // keep in file
        DataOutputStream eraOS = new DataOutputStream(new FileOutputStream (this.getEraFileName()));
        eraOS.write(this.era);
        eraOS.close();
    }
}
