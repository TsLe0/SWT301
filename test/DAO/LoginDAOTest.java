/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import Models.User;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author phuon
 */
public class UserDAOTest {
  private UserDAO userDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws Exception {
        // Dùng Reflection để inject mock Connection vào class cha DBContext
        userDAO = new UserDAO();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Gán connection thông qua Reflection
        java.lang.reflect.Field connectionField = DBContext.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(userDAO, mockConnection);
    }

    @Test
    public void testLogin_ValidCredentials_ReturnUser() throws Exception {
        String username = "admin";
        String password = "admin123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn(hashedPassword);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getString("username")).thenReturn(username);
        when(mockResultSet.getString("email")).thenReturn("admin@example.com");
        when(mockResultSet.getString("role")).thenReturn("admin");

        User result = userDAO.login(username, password);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("admin@example.com", result.getEmail());
    }

    @Test
    public void testLogin_InvalidPassword_ReturnNull() throws Exception {
        String username = "admin";
        String password = "wrongpassword";
        String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt()); // Mật khẩu đúng là admin123

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn(hashedPassword);

        User result = userDAO.login(username, password);
        assertNull(result);
    }

    @Test
    public void testLogin_UsernameNotFound_ReturnNull() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Không có username trong DB

        User result = userDAO.login("unknown", "anything");
        assertNull(result);
    }

    @Test
    public void testLogin_SQLException_ReturnNull() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));

        User result = userDAO.login("admin", "admin123");
        assertNull(result);
    }
}   

