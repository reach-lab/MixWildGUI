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


    public SystemLogger() {
        LOGGER.removeHandler(fileHandler);
        
        LOGGER.setUseParentHandlers(false);
        formatter = new MyFormatter();

        try {
            System.out.print(logPath);
            fileHandler = new FileHandler(logPath + "logger.log", true);
            // For logging fine clicking behavior
            fileHandler.setLevel(Level.FINEST);
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SystemLogger init error");
        }

        fileHandler.setFormatter(formatter);
        LOGGER.addHandler(fileHandler);
        // For logging fine clicking behavior
        LOGGER.setLevel(Level.ALL);
    }
    
    public static String getLineNum() {
        StringBuilder builder = new StringBuilder(1000);
        String fullClassName;
        String className;
        String methodName;
        int lineNumber;
        
        for (int lvl = 2; lvl < 50; lvl++){
            try {
            fullClassName = Thread.currentThread().getStackTrace()[lvl].getClassName();
            className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            methodName = Thread.currentThread().getStackTrace()[lvl].getMethodName();
            lineNumber = Thread.currentThread().getStackTrace()[lvl].getLineNumber();
            
            builder.append("\n                                       at ");
            builder.append("[").append(className).append(".");
            builder.append(methodName).append("():");
            builder.append(lineNumber).append("]");

            
            } catch (ArrayIndexOutOfBoundsException ex){
                
            }
        }
        return builder.toString();
    }

}

class MyFormatter extends Formatter {

    // Create a DateFormat to format the logger timestamp.
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getLevel()).append("] - ");
//        builder.append("[").append(record.getSourceClassName()).append(".");
//        builder.append(record.getSourceMethodName()).append("] - ");
//        builder.append(record.getSourceMethodName()).append(Thread.currentThread().getStackTrace()[2].getLineNumber()).append("] - ");
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
