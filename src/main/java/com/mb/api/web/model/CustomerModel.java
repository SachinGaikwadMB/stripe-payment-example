package com.mb.api.web.model;

public class CustomerModel
{
	private String name;
	
	private String email;
	
	private String customerId;
	
	public CustomerModel() {}

	public CustomerModel(String name, String email, String customerId)
	{
		super();
		this.name = name;
		this.email = email;
		this.customerId = customerId;
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

}
