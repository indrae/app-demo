package com.app.demo.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.bean.ManagedProperty;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.app.demo.domain.Person;
import com.app.demo.service.PersonService;

public class Photo extends HttpServlet {
	private static final long serialVersionUID = 1961456512882456576L;
	
	@Autowired
	private  PersonService personService;
	
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        super.init(config);
    }
	
	
	// This method is called by the servlet container to process a GET request.
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//System.out.println("Photo.doGet()");
		
		/*Person p = new Person();
		p.setUsername("admin");
		p = (Person) personService.findUniqueOrNone(p);
		System.out.println("Name : "+p.getFirstName());*/
		
		HttpSession session = req.getSession();

		byte[] photoByte = (byte[]) session.getAttribute("bytePhoto");
		//System.out.println("byte : "+photoByte);
        
		if(photoByte != null){
			resp.setContentType("image/jpeg");
			resp.setContentLength(photoByte.length);
			resp.getOutputStream().write(photoByte);

		}else{
			// Get the absolute path of the image
			ServletContext sc = getServletContext();
			String filename = sc.getRealPath("/resources/images/nophoto.jpeg");

			// Get the MIME type of the image
			String mimeType = sc.getMimeType(filename);
			if (mimeType == null) {
				sc.log("Could not get MIME type of " + filename);
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Set content type
			resp.setContentType(mimeType);

			// Set content size
			File file = new File(filename);
			resp.setContentLength((int) file.length());

			// Open the file and output streams
			FileInputStream in = new FileInputStream(file);
			OutputStream out = resp.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.close();	
		}
		
	}
	
	
	
}
