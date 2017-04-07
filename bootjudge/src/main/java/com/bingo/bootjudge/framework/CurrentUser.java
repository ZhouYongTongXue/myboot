package com.bingo.bootjudge.framework;

import java.util.Set;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 当前登录用户信息
 * 
 * @author junjie
 *
 */
public class CurrentUser {

	private Long id;// 登录用户ID
	private String username;// 登录用户名
	private String realname;// 真实名
	private Boolean admin;// 是否是管理员
	@JSONField(serialize = false)
	private Set<String> permissions;// 登录用户的权限

	private String userToken;// 记录当时拷贝的那个user对象
	
	
	private String mobilePhone;
	
	private String email;
	
//	private String contactsEmail;
	
	private Boolean isAudit;//是否审核
	
	private String companyRegisterNo;
	
	
	private String contacts;
	private String companyName;
	
	private boolean uploadProductAuth = false;
	
	private boolean biddingPriceAuth = false;
	
	
	
	public boolean isUploadProductAuth() {
		return uploadProductAuth;
	}

	public void setUploadProductAuth(boolean uploadProductAuth) {
		this.uploadProductAuth = uploadProductAuth;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getCompanyRegisterNo() {
		return companyRegisterNo;
	}

	public void setCompanyRegisterNo(String companyRegisterNo) {
		this.companyRegisterNo = companyRegisterNo;
	}

	public Boolean getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Boolean isAudit) {
		this.isAudit = isAudit;
	}


	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}


	public boolean isBiddingPriceAuth() {
		return biddingPriceAuth;
	}

	public void setBiddingPriceAuth(boolean biddingPriceAuth) {
		this.biddingPriceAuth = biddingPriceAuth;
	}

	
	
}
