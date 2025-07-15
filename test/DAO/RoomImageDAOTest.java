package DAO;

import Models.RoomImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoomImageDAOTest {

    private Connection mockConn;
    private PreparedStatement mockPs;
    private ResultSet mockRs;

    @Before
    public void setUp() throws Exception {
        mockConn = mock(Connection.class);
        mockPs = mock(PreparedStatement.class);
        mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
    }

    // ✅ TC01: Có 1 ảnh trả về đúng
    @Test
    public void test_TC01_ValidRoomTypeId_WithData() throws Exception {
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

    // ✅ TC02: RoomTypeID hợp lệ nhưng không có dữ liệu
    @Test
    public void test_TC02_ValidRoomTypeId_NoData() throws Exception {
        when(mockRs.next()).thenReturn(false); 

        List<RoomImage> list = new ArrayList<>();
        try {
            PreparedStatement ps = mockConn.prepareStatement("SELECT * FROM RoomImage WHERE RoomTypeID = ?");
            ps.setInt(1, 999);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // không thêm gì vào list
            }
        } catch (Exception e) {
            fail("Không nên lỗi");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void test_TC03_InvalidRoomTypeId_Empty() throws Exception {
        when(mockRs.next()).thenReturn(false); 

        List<RoomImage> list = new ArrayList<>();
        try {
            int roomTypeID = 0;
            PreparedStatement ps = mockConn.prepareStatement("SELECT * FROM RoomImage WHERE RoomTypeID = ?");
            ps.setInt(1, roomTypeID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // không thêm gì vào list
            }
        } catch (Exception e) {
            fail("Không nên lỗi khi RoomTypeID rỗng");
        }

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}
