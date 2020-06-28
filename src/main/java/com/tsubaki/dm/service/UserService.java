package com.tsubaki.dm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsubaki.dm.dao.UserDao;

@Service
public class UserService {
	
	@Autowired
	UserDao dao;

}
