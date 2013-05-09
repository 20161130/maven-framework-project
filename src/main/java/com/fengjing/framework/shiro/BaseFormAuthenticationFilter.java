package com.fengjing.framework.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.Exceptions;

public class BaseFormAuthenticationFilter extends FormAuthenticationFilter {
	
	private static final Logger log = LoggerFactory.getLogger(BaseFormAuthenticationFilter.class);

	/*
	* ����Ĭ��ʵ�֣���ӡ��־���ڵ��ԣ��鿴�����¼��ʲô����
	*��������չ�Ѵ���д�����ݿ�֮��ġ���
	* (non-Javadoc)
	* @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginFailure(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationException, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	*/
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request,ServletResponse response) {
		if (log.isDebugEnabled()) {
			Class<?> clazz = e.getClass();
			if (clazz.equals(AuthenticationException.class)) {
				log.debug(Exceptions.getStackTraceAsString(e));
			}
		}
		return super.onLoginFailure(token, e, request, response);
	}
	
	
	/**
	* ����isAccessAllowed���ı�shiro����֤�߼���
	* ���ⲻ�ܶ�ε�¼�Ĵ���
	* @param request
	* @param response
	* @param mappedValue
	* @return
	* @see org.apache.shiro.web.filter.authc.AuthenticatingFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	*/
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		try {
			// ���ж��Ƿ��ǵ�¼����
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected. Attempting to execute login.");
				}
				return false;
			}
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		
		return super.isAccessAllowed(request, response, mappedValue);
	}
    
	/**
	* ����Ĭ��ʵ�֣���sendRedirectֱ��������ܣ��������js����ظ�����js����
	* @param token
	* @param subject
	* @param request
	* @param response
	* @return
	* @throws Exception
	* @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.subject.Subject, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	*/
	@Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))|| request.getParameter("ajax") == null) {// ����ajax����
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + this.getSuccessUrl());
		} else {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/shiro/login/success.do");
		}
		return false;
	}
}
