package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {

    @Test
    void Constructor_WithNegativeId_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(-1, endpoint1, endpoint2));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }

    @Test
    void Constructor_WithLowerBoundaryId_NoExceptionThrown() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        assertDoesNotThrow(() -> new Edge(0, endpoint1, endpoint2));
    }

    @Test
    void Constructor_WithUpperBoundaryId_NoExceptionThrown() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        assertDoesNotThrow(() -> new Edge(71, endpoint1, endpoint2));
    }

    @Test
    void Constructor_WithIdAboveUpperBoundary_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(72, endpoint1, endpoint2));
        assertEquals("Edge id must be between 0 and 71", exception.getMessage());
    }

    @Test
    void Constructor_WithNullEndpoint1_ThrowsIllegalArgumentException() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(0, endpoint1, null));
        assertEquals("Endpoints cannot be null", exception.getMessage());
    }

    @Test
    void Constructor_WithNullEndpoint2_ThrowsIllegalArgumentException() {
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Edge(0, null, endpoint2));
        assertEquals("Endpoints cannot be null", exception.getMessage());
    }

    @Test
    void GetId_ReturnsCorrectId() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(5, endpoint1, endpoint2);
        assertEquals(5, edge.getId());
    }

    @Test
    void HasRoad_WhenNoRoad_ReturnsFalse() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        assertFalse(edge.hasRoad());
    }

    @Test
    void HasRoad_WhenRoadPlaced_ReturnsTrue() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        Player player = EasyMock.createMock(Player.class);
        edge.setOwner(player);
        assertTrue(edge.hasRoad());
    }

    @Test
    void GetOwner_WhenNoRoad_ReturnsEmpty() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        assertTrue(edge.getOwner().isEmpty());
    }


    @Test
    void GetOwner_WhenRoadPlaced_ReturnsOwner() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        Player player = EasyMock.createMock(Player.class);
        edge.setOwner(player);
        Player owner = edge.getOwner().orElseThrow();
        assertEquals(player, owner);
    }

    @Test
    void GetEndpoints_ReturnsCorrectEndpoints() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        List<Vertex> endpoints = edge.getEndpoints();
        assertEquals(endpoint1, endpoints.get(0));
        assertEquals(endpoint2, endpoints.get(1));
    }

    @Test
    void ConnectsTo_WhenVertexIsEndpoint1_ReturnsTrue() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        assertTrue(edge.connectsTo(endpoint1));
    }

    @Test
    void ConnectsTo_WhenVertexIsEndpoint2_ReturnsTrue() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        assertTrue(edge.connectsTo(endpoint2));
    }

    @Test
    void ConnectsTo_WhenVertexIsNotEndpoint_ReturnsFalse() {
        Vertex endpoint1 = new Vertex(0, new ArrayList<>(), new ArrayList<>());
        Vertex endpoint2 = new Vertex(1, new ArrayList<>(), new ArrayList<>());
        Vertex otherVertex = new Vertex(2, new ArrayList<>(), new ArrayList<>());
        Edge edge = new Edge(0, endpoint1, endpoint2);
        assertFalse(edge.connectsTo(otherVertex));
    }
}
