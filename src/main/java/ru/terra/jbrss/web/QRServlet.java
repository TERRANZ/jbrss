package ru.terra.jbrss.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.terra.jbrss.helper.QRHelper;

public class QRServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3095083436034592947L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String qrtext = request.getParameter("text");
		response.setContentType("image/png");
		ByteArrayOutputStream out = QRHelper.gen(qrtext);
		response.setContentLength(out.size());

		OutputStream outStream = response.getOutputStream();

		outStream.write(out.toByteArray());

		outStream.flush();
		outStream.close();
	}
}