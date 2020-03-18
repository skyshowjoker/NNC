package nnc.svlt;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


public class DownloadSvlt extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename=request.getParameter("name");
        ServletContext context=this.getServletContext();
        String mimeType=context.getMimeType(filename);
        response.setContentType(mimeType);

        response.setHeader("content-disposition","attachment;filename="+filename+".mid");

        //对拷流
        InputStream is=context.getResourceAsStream("AIMusic/result/"+filename+".mid");
        ServletOutputStream os=response.getOutputStream();
        int len=-1;
        byte[] b=new byte[1024];
        while((len=is.read(b))!=-1){
            os.write(b,0,len);
        }
        os.close();
        is.close();
    }
}
