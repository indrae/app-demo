/**
 * @author <a href=mailto:volkodavav@gmail.com>volkodavav</a>
 */
package com.app.demo.web.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import com.app.demo.web.util.FacesUtil;

@ManagedBean(name = "localeBean")
@SessionScoped
public class LocaleBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	// application available locales
	private static final Map<String, Locale> AVAILABLE_LOCALES = new LinkedHashMap<String, Locale>();

	// ---------------------------
	// Bean property start
	// ---------------------------
	// application locale
	private Locale locale;
	// user preferred language [from combobox]
	private String selectedLocaleKey;
	// ---------------------------
	// Bean property end
	// ---------------------------

	static
	{
		AVAILABLE_LOCALES.put("my", new Locale("my", "MY"));
		AVAILABLE_LOCALES.put("en", new Locale("en", "US"));
	}

	// ---------------------------
	// Bean constructor/post constructor/pre destroy start
	// ---------------------------
	public LocaleBean()
	{
		super();
	}

	@PostConstruct
	public void postConstruct()
	{
		init();
	}

	private void init()
	{
		this.locale = getApplicationLocale();

		String language = this.locale.getLanguage().toLowerCase();
		this.selectedLocaleKey = language;
	}

	@PreDestroy
	public void preDestroy()
	{
		destroy();
	}

	private void destroy()
	{
		this.locale = null;
		this.selectedLocaleKey = null;
	}
	// ---------------------------
	// Bean constructor/post constructor/pre destroy end
	// ---------------------------

	// ---------------------------
	// Bean actions start
	// ---------------------------
	public void changeLocale(AjaxBehaviorEvent event) throws IOException
	{
		// update application locale by user selected [from combobox]
		this.locale = AVAILABLE_LOCALES.get(this.selectedLocaleKey);

		setApplicationLocale(this.locale);

		// redirected to the same URL to update the localization of the page
		HttpServletRequest httpServletRequest = FacesUtil
				.getHttpServletRequest();
		String uri = httpServletRequest.getRequestURI();
		FacesUtil.getExternalContext().redirect(uri);
	}
	// ---------------------------
	// Bean actions end
	// ---------------------------

	// ---------------------------
	// Bean getters/setters start
	// ---------------------------
	public Locale getApplicationLocale()
	{
		HttpServletRequest request = FacesUtil.getHttpServletRequest();
		UIViewRoot uiViewRoot = FacesUtil.getViewRoot();
		Locale locale = null;

		locale = getLocale();
		if ((uiViewRoot != null) && (locale == null))
		{
			locale = uiViewRoot.getLocale();
		}
		if ((request != null) && (locale == null))
		{
			locale = request.getLocale();
		}

		if (locale == null)
		{
			locale = Locale.getDefault();
		}

		return locale;
	}

	public Object[] getAvailableLocaleKeys()
	{
		return AVAILABLE_LOCALES.keySet().toArray();
	}

	public Locale getLocale()
	{
		return this.locale;
	}

	public String getSelectedLocaleKey()
	{
		return this.selectedLocaleKey;
	}

	public void setApplicationLocale(Locale locale)
	{
		UIViewRoot uiViewRoot = FacesUtil.getViewRoot();

		if (uiViewRoot != null)
		{
			uiViewRoot.setLocale(locale);
		}
	}

	public void setSelectedLocaleKey(String selectedLocaleKey)
	{
		this.selectedLocaleKey = selectedLocaleKey;
	}
	// ---------------------------
	// Bean getters/setters end
	// ---------------------------
}