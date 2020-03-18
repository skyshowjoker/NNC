package nnc.svlt;

		import java.io.*;

		import javax.servlet.ServletException;
		import javax.servlet.http.HttpServlet;
		import javax.servlet.http.HttpServletRequest;
		import javax.servlet.http.HttpServletResponse;
		import javax.servlet.http.HttpSession;

		import net.sf.json.JSONArray;
		import nnc.utils.DbConn;

		import net.sf.json.JSONObject;

public class Add extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Add() {
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


		//String id=request.getParameter("id");
		String name=request.getParameter("name");
		String motion=request.getParameter("motion");
		String maxlength=request.getParameter("maxlength");
		String beat=request.getParameter("beat");
		String basicbeat=request.getParameter("basicbeat");
		String mode=request.getParameter("mode");

		int du=db.deleteOrUpdate("delete from music where name='"+name+"'");

		File filename=new File(request.getSession().getServletContext().getRealPath("/")+"AIMusic\\result\\"+name+".mid");
		if(filename.exists()){
			filename.delete();
		}

		boolean bl=db.insort("insert into music(name,motion,maxlength,beat,basicbeat,mode) values('"+name+"','"+motion+"','"+maxlength+"','"+beat+"','"+basicbeat+"','"+mode+"')");
		File file=new File(request.getSession().getServletContext().getRealPath("/")+"AIMusic\\parameter.json"
		);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fileWriter=new FileWriter(file);
		fileWriter.write("");
		fileWriter.flush();
		fileWriter.close();
		//输出到文件
		FileOutputStream fileOutoutStream=new FileOutputStream(file);
		OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutoutStream);
		BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
		json.put("N",name);
		json.put("ML",maxlength);
		json.put("O",motion);
		json.put("M",beat);
		json.put("L",basicbeat);
		json.put("K",mode);
		String jsonString=json.toString();
		bufferedWriter.write(jsonString);
		bufferedWriter.flush();
		bufferedWriter.close();

		//运行python文件
		String[] command = { "cmd", };
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());
			/** 以下可以输入自己想输入的cmd命令 */
			stdin.println("cd "+request.getSession().getServletContext().getRealPath("/")+"AIMusic");
			stdin.println("python "+"LSTM.py");//此处自行填写，切记有空格，跟cmd的执行语句一致。
			stdin.close();
		} catch (Exception e) {
			throw new RuntimeException("编译出现错误：" + e.getMessage());
		}
		System.out.println(request.getSession().getServletContext().getRealPath("/")+"AIMusic");
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
	class SyncPipe implements Runnable {

		private final OutputStream ostrm_;
		private final InputStream istrm_;
		public SyncPipe(InputStream istrm, OutputStream ostrm) {
			istrm_ = istrm;
			ostrm_ = ostrm;
		}

		public void run() {
			try {
				final byte[] buffer = new byte[1024];
				for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
					ostrm_.write(buffer, 0, length);
				}
			} catch (Exception e) {
				throw new RuntimeException("处理命令出现错误：" + e.getMessage());
			}
		}
	}

}
