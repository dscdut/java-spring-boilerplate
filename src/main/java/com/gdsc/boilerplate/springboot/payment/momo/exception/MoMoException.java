package com.gdsc.boilerplate.springboot.payment.momo.exception;

public class MoMoException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1319118136236037095L;

	public MoMoException(String message) {
        super(message);
    }

    public MoMoException(Throwable cause) {
        super(cause);
    }

}
