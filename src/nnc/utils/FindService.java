package nnc.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nnc.been.Music;

//该类的作用就是将结果集转化成链表
public class FindService {
    //将用户信息结果集转化成链表
    public static List<Music> getMusic(ResultSet rs){
        List<Music> alist=new ArrayList();
        if(rs!=null){
            try {
                rs.beforeFirst();
                while(rs.next()){
                    Music a=new Music();
                    a.setId(rs.getInt("id"));
                    a.setName(rs.getString("name"));
                    a.setMaxlength(rs.getString("maxlength"));
                    a.setBeat(rs.getString("beat"));
                    a.setBasicbeat(rs.getString("basicbeat"));
                    a.setMotion(rs.getString("motion"));
                    a.setMode(rs.getString("mode"));
                    alist.add(a);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return alist;
    }

}
