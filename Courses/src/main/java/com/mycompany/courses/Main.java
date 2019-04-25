package com.mycompany.courses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db" + "?serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";

    public Connection con;

    public static void main(String[] args) throws SQLException {

        try {
            Main m = new Main();

            m.insertStudent("Артем");
            m.insertStudent("Сергей");
            m.insertStudent("Любовь");
            m.insertStudent("Максим");
            m.insertStudent("Евгения");
            m.insertStudent("Александр");
            m.insertStudent("Даниил");

            m.insertCourse("Java");
            m.insertCourse("PHP");
            m.insertCourse("C++");
            m.insertCourse("English");

            m.addCourseForStudent(1, 1);
            m.addCourseForStudent(1, 4);
            m.addCourseForStudent(1, 2);
            m.addCourseForStudent(6, 1);
            m.addCourseForStudent(7, 1);
            m.addCourseForStudent(2, 1);
            m.addCourseForStudent(3, 1);
            m.addCourseForStudent(4, 2);

            m.getCoursesOfStudent("Артем");
            m.updateStudentsKnowJava("Good Student");
            m.getAllStudents();
            m.getStudentsAndCourses();
            m.closeConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Main() throws ClassNotFoundException, SQLException {
        this.con = getConnection();
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connection is successful");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void insertStudent(String student) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO students (student_name) VALUES (?);");
            ps.setString(1, student);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCourse(String course) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO courses (course_name) VALUES (?);");
            ps.setString(1, course);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCourseForStudent(int student, int course) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO students_courses (student_id, course_id) VALUES (?,?);");
            ps.setInt(1, student);
            ps.setInt(1, course);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllStudents() throws SQLException {
        try {
            Statement statement = con.createStatement();
            ResultSet rs = null;
            rs = statement.executeQuery(
                    "SELECT * FROM students;");
            while (rs.next()) {
                String str = rs.getInt(1) + ":" + rs.getString(2);
                System.out.println(str);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getStudentsAndCourses() throws SQLException {
        try {
            Statement statement = con.createStatement();
            ResultSet rs = null;
            rs = statement.executeQuery(
                    "SELECT student_name, course_name FROM students_courses s_c\n"
                    + "INNER JOIN students s ON s_c.student_id=s.id\n"
                    + "INNER JOIN courses c ON s_c.course_id=c.id;");
            while (rs.next()) {
                String str = rs.getString(1) + ":" + rs.getString(2);
                System.out.println(str);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCoursesOfStudent(String student) throws SQLException {
        try {
            Statement statement = con.createStatement();
            ResultSet rs = null;
            int i = 0;
            rs = statement.executeQuery(
                    "SELECT s.student_name, c.course_name FROM students_courses s_c "
                    + "INNER JOIN students s ON s_c.student_id=s.id "
                    + "INNER JOIN courses c ON s_c.course_id=c.id WHERE s.student_name='" + student + "';");
            while (rs.next()) {
                i = i + 1;
                String str = rs.getString(1) + " : " + rs.getString(2);
                System.out.println(str);
            }
            System.out.println("count = " + i);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudentsKnowJava(String student) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE students s\n"
                    + "INNER JOIN students_courses s_c ON s_c.student_id=s.id\n"
                    + "INNER JOIN courses c ON s_c.course_id=c.id \n"
                    + "SET student_name=? WHERE c.course_name='Java';");
            ps.setString(1, student);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() throws SQLException {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
