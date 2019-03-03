package net.sharksystem.makan;

import net.sharksystem.SharkException;

public class MakanException extends SharkException {
    public MakanException() { super(); }

    public MakanException(String message) { super(message); }

    public MakanException(String message, Throwable cause) { super(message, cause); }

    public MakanException(Throwable cause) { super(cause); }
}
