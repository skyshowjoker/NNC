package nnc.utils;

import java.sql.*;

public class DbConn {
    //数据库连接字符串定义
    private String url;
    //数据库登陆账号和密码变量
    private String username;
    private String passawd;
    //数据库连接对象
    private Connection con;
    //结果集对象
    private ResultSet rs;
    private Statement st;

    public DbConn(){
        //数据库连接字符串初始化
        url="jdbc:mysql://localhost:3306/NNC?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
        //账号密码初始化
        username = "root";
        passawd = "123456";
        //数据库连接初始化
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, passawd);
            st = con.createStatement();
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    //数据添加方法
    public boolean insort(String sql){
        boolean row =false;
        try{
            row=st.execute(sql);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return row;
    }
    //数据修改删除方法
    public int deleteOrUpdate(String sql){
        int row=0;
        try{
            row=st.executeUpdate(sql);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return row;
    }
    //数据查询方法
    public ResultSet query(String sql) {
        try{
            rs=st.executeQuery(sql);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }
    //查询一行一列数据
    public Object getOnlyOne(String sql){
        Object str=null;
        ResultSet rs=query(sql);
        try {
            if(rs.first()){
                str=rs.getObject(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
    //判断是否有查询结果
    public boolean checkTrue(String sql){
        ResultSet rs=query(sql);
        try {
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    //数据库连接关闭
    public void close() {
        try {
            if (con != null)
                con.close();
        } catch (Exception e) {
            System.out.print(e);
        }
        try {
            if (rs != null)
                rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
