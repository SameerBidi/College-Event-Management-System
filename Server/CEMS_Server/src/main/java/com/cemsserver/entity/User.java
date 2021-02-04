package com.cemsserver.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.cemsserver.model.UserType;

@Entity
public class User
{
	@Id
	private String userID;

	@Enumerated(EnumType.STRING)
	private UserType userType;

	private String username;
	private String password;
	
	private String rollno;
	private String name;
	private String email;
	private String cno;
	
	private String year;
	private String branch;

	private String emailConfirmID;
	private String passResetCode;

	private boolean emailConfirmed = false;
	private boolean accountActivated = false;
	
	public User() { super(); }

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public UserType getUserType()
	{
		return userType;
	}

	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getRollno()
	{
		return rollno;
	}

	public void setRollno(String rollno)
	{
		this.rollno = rollno;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCno()
	{
		return cno;
	}

	public void setCno(String cno)
	{
		this.cno = cno;
	}

	public String getYear()
	{
		return year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getBranch()
	{
		return branch;
	}

	public void setBranch(String branch)
	{
		this.branch = branch;
	}

	public String getEmailConfirmID()
	{
		return emailConfirmID;
	}

	public void setEmailConfirmID(String emailConfirmID)
	{
		this.emailConfirmID = emailConfirmID;
	}

	public String getPassResetCode()
	{
		return passResetCode;
	}

	public void setPassResetCode(String passResetCode)
	{
		this.passResetCode = passResetCode;
	}

	public boolean isEmailConfirmed()
	{
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed)
	{
		this.emailConfirmed = emailConfirmed;
	}

	public boolean isAccountActivated()
	{
		return accountActivated;
	}

	public void setAccountActivated(boolean accountActivated)
	{
		this.accountActivated = accountActivated;
	}

	@Override
	public String toString()
	{
		return "User [userID=" + userID + ", userType=" + userType + ", username=" + username + ", password=" + password
				+ ", rollno=" + rollno + ", name=" + name + ", email=" + email + ", cno=" + cno + ", year=" + year
				+ ", branch=" + branch + ", emailConfirmID=" + emailConfirmID + ", passResetCode=" + passResetCode
				+ ", emailConfirmed=" + emailConfirmed + ", accountActivated=" + accountActivated + "]";
	}
}
