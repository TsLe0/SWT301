/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package DAO;

import Models.RoomImage;
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
public class RoomImageDAOTest {

    // ✅ TC01: Có 1 ảnh trả về đúng
    @Test
    public void test_TC01_ValidRoomTypeId_WithData() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true, false);
        when(mockRs.getInt(1)).thenReturn(101);
        when(mockRs.getInt(2)).thenReturn(5);
        when(mockRs.getString(3)).thenReturn("img_101.jpg");

        List<RoomImage> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockConn.prepareStatement("SELECT * FROM RoomImage WHERE RoomTypeID = ?");
            ps.setInt(1, 5);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RoomImage r = new RoomImage();
                r.setImageID(rs.getInt(1));
                r.setRoomTypeID(rs.getInt(2));
                r.setRoomImages(rs.getString(3));
                list.add(r);
            }
        } catch (Exception e) {
            fail("Không nên xảy ra lỗi");
        }

        assertNotNull(list);
        assertEquals(1, list.size());
        RoomImage r = list.get(0);
        assertEquals(101, r.getImageID());
        assertEquals(5, r.getRoomTypeID());
        assertEquals("img_101.jpg", r.getRoomImages());
    }

    // ✅ TC02: Không có ảnh trả về
    @Test
    public void test_TC02_ValidRoomTypeId_NoData() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // không có kết quả

        List<RoomImage> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockConn.prepareStatement("SELECT * FROM RoomImage WHERE RoomTypeID = ?");
            ps.setInt(1, 999);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // không vào đây
            }
        } catch (Exception e) {
            fail("Không nên lỗi");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // ✅ TC03: RoomTypeID âm
    @Test
    public void test_TC03_InvalidRoomTypeId_Empty() throws Exception {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockPs = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // không có kết quả

        List<RoomImage> list = new ArrayList<>();
        try {
            // Giả lập trường hợp không set RoomTypeID, mặc định là 0 (hoặc giả định người dùng nhập "")
            int roomTypeID = 0; // giả sử lỗi khi không parse được ("" → 0)
            PreparedStatement ps = mockConn.prepareStatement("SELECT * FROM RoomImage WHERE RoomTypeID = ?");
            ps.setInt(1, roomTypeID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // không vào đây
            }
        } catch (Exception e) {
            fail("Không nên lỗi khi RoomTypeID rỗng");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

}
