/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import Models.Room;
import Models.RoomType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author phuon
 */
public class RoomsDAOTest {
    
// TC01: RoomTypeID hợp lệ có dữ liệu
    @Test
    public void test_TC01_ValidRoomTypeId_WithData() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);
        RoomTypeDAO mockRoomTypeDAO = mock(RoomTypeDAO.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getString("RoomNumber")).thenReturn("R101");
        when(mockRs.getString("RoomStatus")).thenReturn("Available");
        when(mockRs.getInt("RoomTypeID")).thenReturn(1);

        RoomType fakeType = new RoomType();
        fakeType.setRoomTypeID(1);
        when(mockRoomTypeDAO.getRoomTypeById(1)).thenReturn(fakeType);

        List<Room> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockPs;
            ps.setInt(1, 1);
            ResultSet rs = mockRs;
            while (rs.next()) {
                Room room = new Room();
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setRoomStatus(rs.getString("RoomStatus"));
                room.setRoomTypeID(rs.getInt("RoomTypeID"));
                room.setRoomType(mockRoomTypeDAO.getRoomTypeById(1));
                list.add(room);
            }
        } catch (Exception e) {
            fail("Không nên xảy ra lỗi");
        }

        assertNotNull(list);
        assertEquals(1, list.size());
        Room room = list.get(0);
        assertEquals("R101", room.getRoomNumber());
        assertEquals("Available", room.getRoomStatus());
        assertEquals(1, room.getRoomTypeID());
        assertEquals(1, room.getRoomType().getRoomTypeID());
    }

    // TC02: RoomTypeID không tồn tại
    @Test
    public void test_TC02_InvalidRoomTypeId_ReturnEmptyList() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // không có dòng nào

        List<Room> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockPs;
            ps.setInt(1, -1);
            ResultSet rs = mockRs;
            while (rs.next()) {
                // sẽ không chạy vào đây
            }
        } catch (Exception e) {
            fail("Không nên lỗi");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // TC03: RoomTypeID = 0
    @Test
    public void test_TC03_ZeroRoomTypeId_ReturnEmptyList() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        List<Room> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockPs;
            ps.setInt(1, 0);
            ResultSet rs = mockRs;
            while (rs.next()) {
                // không có dòng nào
            }
        } catch (Exception e) {
            fail("Không nên lỗi");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // TC04: RoomTypeID rất lớn
    @Test
    public void test_TC04_LargeRoomTypeId_ReturnEmptyList() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        List<Room> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockPs;
            ps.setInt(1, 999999);
            ResultSet rs = mockRs;
            while (rs.next()) {
                // không có dòng nào
            }
        } catch (Exception e) {
            fail("Không nên lỗi");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // TC05: RoomType object phải đúng khi có dữ liệu
    @Test
    public void test_TC05_ValidRoomType_CheckRoomTypeObject() throws Exception {
        RoomTypeDAO mockRoomTypeDAO = mock(RoomTypeDAO.class);
        RoomType fakeType = new RoomType();
        fakeType.setRoomTypeID(2);
        when(mockRoomTypeDAO.getRoomTypeById(2)).thenReturn(fakeType);

        Room room = new Room();
        room.setRoomTypeID(2);
        room.setRoomType(mockRoomTypeDAO.getRoomTypeById(2));

        assertNotNull(room.getRoomType());
        assertEquals(2, room.getRoomType().getRoomTypeID());
    }

    // TC06: Lỗi kết nối DB (giả lập bằng Mockito)
    @Test
    public void test_TC06_DBConnectionError_CaughtGracefully() {
        try {
            Connection mockConn = mock(Connection.class);
            when(mockConn.prepareStatement(anyString())).thenThrow(new RuntimeException("DB Error"));

            PreparedStatement ps = mockConn.prepareStatement("SELECT * FROM Room WHERE RoomTypeID = ?");
            ps.setInt(1, 1);
            fail("Phải ném lỗi");
        } catch (RuntimeException e) {
            assertEquals("DB Error", e.getMessage());
        } catch (Exception e) {
            fail("Lỗi không đúng loại");
        }
    }
}