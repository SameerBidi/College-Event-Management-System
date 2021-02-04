package com.cemsserver.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cemsserver.entity.User;
import com.cemsserver.model.Response;
import com.cemsserver.model.UserData;
import com.cemsserver.model.UserType;
import com.cemsserver.utility.CEMSMainRepo;
import com.cemsserver.utility.Emailer;

@RestController
public class AuthController
{
	@Autowired
	CEMSMainRepo mainRepo;
	
	@PostMapping("login")
	public Response login(@RequestBody User user)
	{
		System.out.println(user);
		Optional<User> userOpt = mainRepo.getUserRepo().findByUsername(user.getUsername());

		if (userOpt.isPresent())
		{
			User currentUser = userOpt.get();

			if (!currentUser.getPassword().equals(user.getPassword()))
			{
				return new Response(1, "Incorrect Password");
			}

			if (!currentUser.isEmailConfirmed())
			{
				return new Response(1, "Email not confirmed");
			}

			if (!currentUser.isAccountActivated())
			{
				return new Response(1, "Account not activated");
			}

			String apiKey = mainRepo.InitSession(currentUser.getUserType(), currentUser.getUserID());
			
			UserData userData = new UserData(currentUser.getUserID(), currentUser.getUserType(), currentUser.getName(), currentUser.getYear(), currentUser.getBranch(), apiKey);

			return new Response(0, "success", mainRepo.getGson().toJson(userData));
		}

		return new Response(1, "Account with such username not found");
	}

	@PostMapping("register")
	public Response register(@RequestBody User user, HttpServletRequest request)
	{
		System.out.println(user);
		if (mainRepo.getUserRepo().findByUsername(user.getUsername()).isPresent())
		{
			return new Response(1, "Username already in use");
		}

		if (mainRepo.getUserRepo().findByEmail(user.getEmail()).isPresent())
		{
			return new Response(1, "Email already in use");
		}
		
		if (mainRepo.getUserRepo().findByCno(user.getCno()).isPresent())
		{
			return new Response(1, "Contact No already in use");
		}

		String reqUrlString = request.getRequestURL().toString().replace(request.getRequestURI(), "");

		String emailConfirmID = mainRepo.generateRandomID(30);

		boolean emailSent = Emailer.SendEmail(user.getEmail(), "CEMS Email Confirmation",
				"Please click on the link below to confirm your email:\n" + reqUrlString
						+ "/cems/confirmEmail?confirmID=" + emailConfirmID);

		if (!emailSent)
		{
			return new Response(1, "Some problem with email service, try later");
		}

		user.setUserID(user.getUserType() == UserType.STUDENT ? mainRepo.generateStudentID()
				: mainRepo.generateStaffID());
		user.setEmailConfirmed(false);
		user.setAccountActivated(true);
		user.setEmailConfirmID(emailConfirmID);

		mainRepo.getUserRepo().save(user);

		return new Response(0, "success");
	}
	
	@GetMapping(value = { "confirmEmail", "/confirmEmail" })
	public String confirmEmail(String confirmID)
	{
		Optional<User> userOpt = mainRepo.getUserRepo().findByEmailConfirmID(confirmID);

		if (userOpt.isPresent())
		{
			User user = userOpt.get();

			user.setEmailConfirmed(true);
			user.setEmailConfirmID("");

			mainRepo.getUserRepo().save(user);

			return "Email Confirmation Success!";
		}

		return "Invalid Confirmation Link!";
	}
}

