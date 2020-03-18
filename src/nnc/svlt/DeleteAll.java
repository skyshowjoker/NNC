package nnc.svlt;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import nnc.been.Music;
import java.util.List;
import nnc.utils.DbConn;
import nnc.utils.FindService;

        import net.sf.json.JSONArray;
import nnc.utils.DbConn;

import net.sf.json.JSONObject;

public class DeleteAll extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public DeleteAll() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置传输数据编码方式
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        HttpSession session=request.getSession();

        //创建json对象
        JSONObject json=new JSONObject();
        JSONArray jsonResult=new JSONArray();

        //创建数据库操作对象
        DbConn db=new DbConn();

        String sqls="select music.* from music";

        ResultSet rs=db.query(sqls);
        List<Music> alist= FindService.getMusic(rs);
        request.setAttribute("alist", alist);
        for(Music m:alist){
            int du=db.deleteOrUpdate("delete from music where name='"+m.getName()+"'");

            File filename=new File("/Users/mac/Desktop/NNC/web/AIMusic/result/"+m.getName()+".mid");
            if(filename.exists()){
                filename.delete();
            }
        }



        response.sendRedirect("/NNC_war_exploded/home.jsp");
        //request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

}
