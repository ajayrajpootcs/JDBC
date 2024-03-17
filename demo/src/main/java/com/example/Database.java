package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.RowSetProvider;

import com.mysql.cj.jdbc.DatabaseMetaData;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class Database {

    /*-----------CREATING CONNECTION WITH DATABASE................ */

    private static String url = "jdbc:mysql://localhost:3306/demodb";
    private static String user = "root";
    private static String pass = "root";
    private static Connection con = null;
    static {
        try {
            // Properties properties = new Properties();
            // properties.put("test", "testingExtraValue");
            // properties.put("user", "root");
            // properties.put("password", "root");
            // con = DriverManager.getConnection(url, properties);
            con = DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    /*-----------FETCHING DATABASE USING STATEMENT................ */

    public void showDB() {
        try (Statement statement = con.createStatement()) {
            ResultSet re = statement.executeQuery("SELECT * FROM demodb.Newemployee");
            while (re.next()) {

                System.out.println(re.getInt(1) + " " + re.getString(2) + " " + re.getString(3) + " " + re.getInt(4));

            }
            System.out.println("Completed.....");
        } catch (SQLException e) {
            System.out.println("Exeception");
            System.out.println(e.getMessage());
        }

    }

    /*-----------INSERT DATA IN DB USING PREPARED STATEMENT................ */

    public void insertByPrepared(employee e) {
        String sql = "insert into Newemployee(id,name,branch,age) values(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // ps.setInt(1, 2);
            // ps.setString(2, "Vijay");
            // ps.setString(3, "CSE");
            // ps.setInt(4, 32);
            // ps.executeUpdate();

            ps.setInt(1, e.getId());
            ps.setString(2, e.getName());
            ps.setString(3, e.getBranch());
            ps.setInt(4, e.getAge());
            ps.executeUpdate();
            System.out.println("Data Inserted.....");
        } catch (SQLException exc) {
            exc.printStackTrace();

        }

    }

    /*-----------FETCHING DATA USING PREPARED DATABASE................ */

    public void FetchByPrepared() {
        String sql = "select * from Newemployee";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            System.out.println("fetching Data....");
            while (rs.next()) {

                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getInt(4));

            }
            System.out.println("Data feteched.....");
        } catch (SQLException exc) {
            exc.printStackTrace();

        }

    }

    /*-----------DELETING DATA USING PREPARED................ */

    public void deleteByPrepared(int delete) {
        String sql = "delete from Newemployee where id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println("Deleting Data....");
            ps.setInt(1, delete);
            int del = ps.executeUpdate();

            System.out.println("Data Deleted....." + del);
        } catch (SQLException exc) {
            exc.printStackTrace();

        }

    }

    /*-----------UPDATE DATA USING PREPARED................ */

    public void updateByPrepared(String name, int id) {
        String sql = "update Newemployee set name=? where id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.println("Updating Data....");
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();

            System.out.println("Update Name.....");
        } catch (SQLException exc) {
            exc.printStackTrace();

        }

    }

    /* .......................CallableStatement.......................... */

    public void updateEmpSalaryByCallable(int id, double salaryInrcement) {

        try (CallableStatement cs = con.prepareCall("{call updateEmpSalary(?,?)}")) {
            System.out.println("Increasing Salary....");
            cs.setInt(1, id);
            cs.setDouble(2, salaryInrcement);
            cs.executeUpdate();
            System.out.println("Increased Successfully....");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* .....................ResultSet..................... */
    ArrayList<employee> AlData = new ArrayList<>();

    public void resultSet() {

        try (Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = stmt.executeQuery("select * from Newemployee");

            // System.out.println("First " + rs.first());
            // System.out.println("Last :" + rs.last());

            // rs.first(); // ->Return First Row Data
            // rs.last(); // -> Return Last Row Data

            // rs.absolute(3);
            // System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " +
            // rs.getString(3) + " " + rs.getInt(4));
            // rs.previous(); // return previous row data from current row
            // System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " +
            // rs.getString(3) + " " + rs.getInt(4));

            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getInt(4));
                employee empObj = new employee();
                empObj.setId(rs.getInt(1));
                empObj.setName(rs.getString(2));
                empObj.setBranch(rs.getString(3));
                empObj.setAge(rs.getInt(4));
                AlData.add(empObj);

            }
            System.out.println(AlData);
            System.out.println("Printing ArrayList.....................");
            Iterator itr = AlData.iterator();
            while (itr.hasNext()) {
                employee data = (employee) itr.next();
                System.out.println(data.getId() + " " + data.getName() + " " + data.getBranch() + " " + data.getAge());

            }

            // System.out.println(empData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ..................ResultSetMetaData........................... */

    public void getMetaData() {
        try (PreparedStatement ps = con.prepareStatement("select * from Newemployee")) {
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            System.out.println("Total No of Column:" + rsmd.getColumnCount());
            System.out.println("First  Column Name:" + rsmd.getColumnName(1));
            System.out.println("Total No of Column:" + rsmd.getColumnTypeName(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ..................DatabaseMetaData................ */

    public void getDBMetaData() {
        try {
            DatabaseMetaData dbmd = (DatabaseMetaData) con.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            System.out.println("UserName: " + dbmd.getUserName());
            System.out.println("Database Product Name: " + dbmd.getDatabaseProductName());
            System.out.println("Database Product Version: " + dbmd.getDatabaseProductVersion());

            String table[] = { "TABLE" };
            ResultSet rs = dbmd.getTables(null, null, null, table);

            while (rs.next()) {
                System.out.println(rs.getString("Newemployee"));
            }
            // con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* .....................Store IMAGES IN NEW TABLE(imgtable)................ */
    public void insertPhoto() {
        try (PreparedStatement ps = con.prepareStatement("insert into imgtable values(?,?)")) {

            ps.setString(1, "Family");

            FileInputStream fin = new FileInputStream("G:\\img2.jpg");
            ps.setBinaryStream(2, fin, fin.available());
            int i = ps.executeUpdate();
            System.out.println(i + " records affected");

            // con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* .....................RERIEVE IMAGES IN NEW TABLE(imgtable)................ */
    public void retrievePhoto() {
        try (PreparedStatement ps = con.prepareStatement("select * from imgtable")) {
            ResultSet rs = ps.executeQuery();
            byte bar[] = {};
            if (rs.next()) {
                Blob b = rs.getBlob(2);
                bar = b.getBytes(1, (int) b.length());
                FileOutputStream fout = new FileOutputStream("G:\\img2.jpg");
                fout.write(bar);
                fout.close();

            }
            // for (int i = 0; i < bar.length; i++) {
            // System.out.print(bar[i] + " ");
            // }
            for (byte b : bar) {
                System.out.print(b);

            }
            System.out.println("ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ..................StoreFile...................... */

    public void storeFile() {
        try (PreparedStatement ps = con.prepareStatement("insert into filetable values (?,?)")) {
            File f = new File("G:\\myfile.txt");
            FileReader fr = new FileReader(f);
            ps.setInt(1, 101);
            ps.setCharacterStream(2, fr, f.length());
            int i = ps.executeUpdate();
            System.out.println(i + " records affected");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* .................. Retrieve StoreFile...................... */

    public void retrieveFile() {
        try (PreparedStatement ps = con.prepareStatement("select * from filetable")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            Clob c = rs.getClob(2);
            Reader r = c.getCharacterStream();
            FileWriter fw = new FileWriter("G:\\myfilefile.txt");
            int i;
            while ((i = r.read()) != -1) {
                fw.write((char) i);

            }

            fw.close();

            System.out.println("success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConsoletxt() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("G:\\myfilefile.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* .................. BATCH PROCESSING USING STATEMENT...................... */

    public void batchByStatement() {
        try (Statement stmt = con.createStatement()) {
            stmt.addBatch("insert into newemployee values(5,'Ajay','IT','21')");
            stmt.addBatch("insert into newemployee values(6,'Bitto','IT','21')");
            stmt.addBatch("insert into newemployee values(7,'Nick','IT','21')");
            stmt.executeBatch();
            con.commit();
            System.out.println("Inserted Batch....");

        } catch (Exception e) {
            e.getMessage();
        }
    }

    /*
     * .................. BATCH PROCESSING USING PREPARED
     * STATEMENT......................
     */

    public void batchByPrepareStmt() {
        try (PreparedStatement ps = con.prepareStatement("insert into newemployee values(?,?,?,?)")) {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Enter id :");
                String s1 = br.readLine();
                int id = Integer.parseInt(s1);
                System.out.println("'Enter Name:");
                String name = br.readLine();
                System.out.println("'Enter Branch:");
                String branch = br.readLine();
                System.out.println("'Enter age");
                String age = br.readLine();
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, branch);
                ps.setString(4, age);

                ps.addBatch();

                System.out.println("DO YOU WANT TO CONTINUE Y/N ");
                String ans = br.readLine();
                if (ans.equals('n') || ans.equals("N")) {
                    break;
                }

            }
            ps.executeBatch();
            System.out.println("SuccessFully Inserted");

        } catch (Exception e) {
            e.getMessage();
        }
    }
    /* .................. JDBC ROWSET...................... */

    public void usingRowset() {
        try (RowSet rs = RowSetProvider.newFactory().createJdbcRowSet()) {
            rs.setUrl(url);
            rs.setUsername(user);
            rs.setPassword(pass);

            rs.setCommand("Select * from newemployee");
            rs.execute();
            while (rs.next()) {
                System.out.print("Id :" + rs.getInt(1));
                System.out.print(" Name : " + rs.getString(2));
                System.out.print(" Branch : " + rs.getString(3));
                System.out.print(" Age : " + rs.getString(4));
                System.out.println();

            }
            rs.close();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    /* .................JDBC RowSet with Event Handling.................... */

    public void usingRowsetEventHandling() {
        try (RowSet rs = RowSetProvider.newFactory().createJdbcRowSet()) {
            rs.setUrl(url);
            rs.setUsername(user);
            rs.setPassword(pass);

            rs.setCommand("Select * from newemployee");
            rs.execute();
            class MyListener implements RowSetListener {
                public void cursorMoved(RowSetEvent event) {
                    System.out.println("Cursor Moved...");
                }

                public void rowChanged(RowSetEvent event) {
                    System.out.println("Cursor Changed...");
                }

                public void rowSetChanged(RowSetEvent event) {
                    System.out.println("RowSet changed...");
                }
            }
            MyListener ref = new MyListener();
            rs.addRowSetListener(ref);
            // rs.addRowSetListener(new MyListener());
            while (rs.next()) {
                System.out.println("Id :" + rs.getInt(1));
                System.out.println(" Name : " + rs.getString(2));
                System.out.println(" Branch : " + rs.getString(3));
                System.out.println(" Age : " + rs.getString(4));

            }
            rs.close();

        } catch (Exception e) {
            e.getMessage();
        }

    }

}
