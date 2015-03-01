package com.hashtag.util;

public class JsonRespEntity {
	private String msg = "success";
	private int msgCode = 1;
	private Object jsonResponse;

	/**
	 * The http json response message
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * the http json response code.
	 * 
	 * @return -1 indicates an error happened; 1 indicates success
	 */
	public int getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

	/**
	 * The http json response object
	 * 
	 * @return
	 */
	public Object getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(Object jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
}
