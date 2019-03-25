package com.dttr.tchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dttr.tchat.dao.UserinfoDAO;



@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserinfoDAO userDAO;

	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		com.dttr.tchat.entities.Userinfo user = userDAO.loadUserByUsername(username);
		if (user == null) {
//			System.out.println("user"+username+ "null");
			throw new UsernameNotFoundException("User not found!");
		}

		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
//		System.out.println(user.getUsername()+">>"+user.getAuthorities().get(0));
		return new User(username, user.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, user.getAuthorities());
	}

}
