package com.maolintu.flashsale.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maolintu.flashsale.domain.SaleUser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUtil {

	private static Logger logger = LoggerFactory.getLogger(UserUtil.class);

	private static void insertDB(List<SaleUser> users) throws Exception{
		logger.info("Insert users");
		//insert database
		Connection conn = DBUtil.getConn();
		String sql = "insert into users(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for(int i=0;i<users.size();i++) {
			SaleUser user = users.get(i);
			pstmt.setInt(1, user.getLoginCount());
			pstmt.setString(2, user.getNickname());
			pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
			pstmt.setString(4, user.getSalt());
			pstmt.setString(5, user.getPassword());
			pstmt.setLong(6, user.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		conn.close();
		logger.info("insert to db");
	}
	private static void createUser(int count) throws Exception {
		List<SaleUser> users = new ArrayList<SaleUser>(count);
		//Genereate users
		for(int i=0;i<count;i++) {
			SaleUser user = new SaleUser();
			user.setId(13000000000L+i);
			user.setLoginCount(1);
			user.setNickname("user"+i);
			user.setRegisterDate(new Date());
			user.setSalt("1a2b3c");
			user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
			users.add(user);
		}
		//insert database
		insertDB(users);

		//generate tokens
    generateTokens(users);
	}

	public static void generateTokens(List<SaleUser> users) throws Exception{
    String urlString = "http://localhost:8080/login/do_login";
    File file = new File("tokens.txt");
    if(file.exists()) {
      file.delete();
    }
    RandomAccessFile raf = new RandomAccessFile(file, "rw");
    file.createNewFile();
    raf.seek(0);
    for(int i=0;i<users.size();i++) {
      SaleUser user = users.get(i);
      URL url = new URL(urlString);
      HttpURLConnection co = (HttpURLConnection)url.openConnection();
      co.setRequestMethod("POST");
      co.setDoOutput(true);
      OutputStream out = co.getOutputStream();
      String params = "mobile="+user.getId()+"&password="+MD5Util.inputPassFormPass("123456");
      out.write(params.getBytes());
      out.flush();
      InputStream inputStream = co.getInputStream();
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      byte buff[] = new byte[1024];
      int len = 0;
      while((len = inputStream.read(buff)) >= 0) {
        bout.write(buff, 0 ,len);
      }
      inputStream.close();
      bout.close();
      String response = new String(bout.toByteArray());
      JSONObject jo = JSON.parseObject(response);
      String token = jo.getString("data");
      System.out.println("create token : " + user.getId());

      String row = user.getId()+","+token;
      raf.seek(raf.length());
      raf.write(row.getBytes());
      raf.write("\r\n".getBytes());
      System.out.println("write to file : " + user.getId());
    }
    raf.close();

    System.out.println("over");
  }
	
	public static void main(String[] args)throws Exception {
		createUser(5000);
	}
}
