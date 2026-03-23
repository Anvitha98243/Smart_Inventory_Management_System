package com.inventory.dto;
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
    public ApiResponse(){}
    public ApiResponse(boolean success, String message, Object data){ this.success=success; this.message=message; this.data=data; }
    public boolean isSuccess(){ return success; } public void setSuccess(boolean v){ success=v; }
    public String getMessage(){ return message; } public void setMessage(String v){ message=v; }
    public Object getData(){ return data; } public void setData(Object v){ data=v; }
}
