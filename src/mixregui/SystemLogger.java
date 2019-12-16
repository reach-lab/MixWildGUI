/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jixin
 */
public class SystemLogger {

    public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static Handler fileHandler;
    static MyFormatter formatter;
    public static String logPath;

    static int getLine() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public SystemLogger() {
        formatter = new MyFormatter();

        try {
            System.out.print(logPath);
//            fileHandler = new FileHandler("/Users/jixin/Documents/logger.log");
            fileHandler = new FileHandler(logPath + "logger.log");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SystemLogger init error");
        }

        fileHandler.setFormatter(formatter);
        LOGGER.addHandler(fileHandler);

    }

}

class MyFormatter extends Formatter {

    // Create a DateFormat to format the logger timestamp.
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append("] - ");
//        builder.append(record.getSourceMethodName()).append(Thread.currentThread().getStackTrace()[2].getLineNumber()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }
    
}
