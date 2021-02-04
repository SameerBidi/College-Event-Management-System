package com.cemsserver.model;

public class Response 
{
	private int statusCode;
	private String message;
	private String responseJSON;

	public Response(int statusCode, String message)
	{
		this.statusCode = statusCode;
		this.message = message;
	}

	public Response(int statusCode, String message, String responseJSON)
	{
		this.statusCode = statusCode;
		this.message = message;
		this.responseJSON = responseJSON;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getResponseJSON()
	{
		return responseJSON;
	}

	public void setResponseJSON(String responseJSON)
	{
		this.responseJSON = responseJSON;
	}

	@Override
	public String toString()
	{
		return "Response [statusCode=" + statusCode + ", message=" + message + ", responseJSON=" + responseJSON + "]";
	}
}
