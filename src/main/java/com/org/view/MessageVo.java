package com.org.view;

import java.util.ArrayList;
import java.util.List;

public class MessageVo {
	private String code;
	private boolean autoclose;
	private boolean autoopen;
	private MessageType type;
	private List<String> params = new ArrayList<String>();
	
	public static String NAME = "notification";

	public MessageVo(String message) {
		this.code = message;
		this.autoclose = true;
		this.autoopen = true;
		this.type = MessageType.INFO;
	}
	
	public MessageVo(String message, MessageType type) {
		this.code = message;
		if(type==MessageType.ERROR){this.autoclose = false;}else{this.autoclose = true;}
		this.autoopen = true;
		this.type = type;
	}

	public MessageVo(String message, boolean autoclose, boolean autoopen) {
		this.code = message;
		this.autoclose = autoclose;
		this.autoopen = autoopen;
		this.type = MessageType.INFO;
	}

	public MessageVo(String message, MessageType type, boolean autoclose, boolean autoopen) {
		this.code = message;
		this.autoclose = autoclose;
		this.autoopen = autoopen;
		this.type = type;
	}
	
	public MessageVo addParam(String param){
		this.getParams().add(param);
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String message) {
		this.code = message;
	}

	public boolean isAutoclose() {
		return autoclose;
	}

	public void setAutoclose(boolean autoclose) {
		this.autoclose = autoclose;
	}

	public boolean isAutoopen() {
		return autoopen;
	}

	public void setAutoopen(boolean autoopen) {
		this.autoopen = autoopen;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

}
