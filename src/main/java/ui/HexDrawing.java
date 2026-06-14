package ui;

import java.awt.Point;
import java.awt.Polygon;

/**
 * Pure geometry helper for pointy-top hex rendering.
 *
 * Computes pixel coordinates for:
 *   - The 19 hex centers arranged in Catan's 3-4-5-4-3 row layout
 *   - The 6 corner points of any hex polygon
 *   - The 54 vertex pixel positions (derived from hex corners)
 *
 * This class has no dependencies on domain or Swing state.
 * All methods are static — it is not instantiable.
 *
 * Pointy-top hex geometry:
 *   Corner i is at angle (60 * i - 30) degrees from center.
 *   Corner order: top, top-right, bottom-right, bottom, bottom-left, top-left.
 */
public final class HexDrawing {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Pixel radius from hex center to any corner. */
    public static final int HEX_SIZE = 60;

    /** Horizontal distance between adjacent hex centers (pointy-top). */
    public static final int HEX_WIDTH = (int) (Math.sqrt(3) * HEX_SIZE);

    /** Vertical distance between hex center rows (pointy-top). */
    public static final int HEX_HEIGHT = 2 * HEX_SIZE;

    /** Number of hexes per row in Catan's 3-4-5-4-3 layout. */
    private static final int[] ROW_SIZES = {3, 4, 5, 4, 3};

    /** Total number of hexes on the board. */
    private static final int HEX_COUNT = 19;

    /** Total number of vertices on the board. */
    private static final int VERTEX_COUNT = 54;

    /**
     * Vertex index assignments per hex corner, in pointy-top corner order
     * (top, top-right, bottom-right, bottom, bottom-left, top-left).
     *
     * Derived from BoardInitialization.ADJACENT_HEX_INDICES:
     * each vertex knows which hexes it touches; corners are assigned
     * by matching vertex-to-hex adjacency with hex index.
     *
     * Layout: HEX_VERTEX_IDS[hexIndex][cornerIndex] = vertexId
     */
    private static final int[][] HEX_VERTEX_IDS = {
            { 4,  8, 12,  7,  3,  0}, // hex  0
            { 5,  9, 13,  8,  4,  1}, // hex  1
            { 6, 10, 14,  9,  5,  2}, // hex  2
            {12, 17, 22, 16, 11,  7}, // hex  3
            {13, 18, 23, 17, 12,  8}, // hex  4
            {14, 19, 24, 18, 13,  9}, // hex  5
            {15, 20, 25, 19, 14, 10}, // hex  6
            {22, 28, 33, 27, 21, 16}, // hex  7
            {23, 29, 34, 28, 22, 17}, // hex  8
            {24, 30, 35, 29, 23, 18}, // hex  9
            {25, 31, 36, 30, 24, 19}, // hex 10
            {26, 32, 37, 31, 25, 20}, // hex 11
            {34, 39, 43, 38, 33, 28}, // hex 12
            {35, 40, 44, 39, 34, 29}, // hex 13
            {36, 41, 45, 40, 35, 30}, // hex 14
            {37, 42, 46, 41, 36, 31}, // hex 15
            {44, 48, 51, 47, 43, 39}, // hex 16
            {45, 49, 52, 48, 44, 40}, // hex 17
            {46, 50, 53, 49, 45, 41}, // hex 18
    };

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    private HexDrawing() {
        // Utility class — not instantiable
    }

    // -------------------------------------------------------------------------
    // Hex Center Coordinates
    // -------------------------------------------------------------------------

    /**
     * Returns the pixel center of each hex, indexed 0–18.
     * Hexes are laid out in Catan's 3-4-5-4-3 row arrangement.
     *
     * @param boardOriginX left edge of the board drawing area
     * @param boardOriginY top edge of the board drawing area
     * @return array of 19 Points, one per hex
     */
    public static Point[] computeHexCenters(int boardOriginX, int boardOriginY) {
        Point[] centers = new Point[HEX_COUNT];
        int hexIndex = 0;

        for (int row = 0; row < ROW_SIZES.length; row++) {
            int rowSize = ROW_SIZES[row];
            int maxRowSize = ROW_SIZES[2]; // widest row is row 2 (5 hexes)

            // Offset each row so the board is centered horizontally
            double rowOffsetX = (maxRowSize - rowSize) * HEX_WIDTH / 2.0;

            for (int col = 0; col < rowSize; col++) {
                int cx = boardOriginX + (int) (rowOffsetX + col * HEX_WIDTH + HEX_WIDTH / 2.0);
                int cy = boardOriginY + (int) (row * HEX_HEIGHT * 0.75 + HEX_SIZE);
                centers[hexIndex] = new Point(cx, cy);
                hexIndex++;
            }
        }

        return centers;
    }

    // -------------------------------------------------------------------------
    // Hex Polygon
    // -------------------------------------------------------------------------

    /**
     * Builds the pointy-top hexagon polygon for a given center.
     *
     * @param center the pixel center of the hex
     * @return a Polygon with 6 corners
     */
    public static Polygon buildHexPolygon(Point center) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {
            double angleDeg = 60 * i - 30;
            double angleRad = Math.toRadians(angleDeg);
            xPoints[i] = center.x + (int) (HEX_SIZE * Math.cos(angleRad));
            yPoints[i] = center.y + (int) (HEX_SIZE * Math.sin(angleRad));
        }

        return new Polygon(xPoints, yPoints, 6);
    }

    // -------------------------------------------------------------------------
    // Vertex Pixel Positions
    // -------------------------------------------------------------------------

    /**
     * Computes the pixel position of all 54 vertices.
     *
     * Each vertex position is derived from the corner of its first adjacent hex.
     * HEX_VERTEX_IDS maps hex index → corner index → vertex id, so we can
     * invert that to find each vertex's pixel location.
     *
     * @param hexCenters the 19 hex centers from computeHexCenters()
     * @return array of 54 Points, one per vertex
     */
    public static Point[] computeVertexPositions(Point[] hexCenters) {
        Point[] positions = new Point[VERTEX_COUNT];

        for (int hexIndex = 0; hexIndex < HEX_COUNT; hexIndex++) {
            Point center = hexCenters[hexIndex];
            for (int corner = 0; corner < 6; corner++) {
                int vertexId = HEX_VERTEX_IDS[hexIndex][corner];
                if (positions[vertexId] == null) {
                    double angleDeg = 60 * corner - 30;
                    double angleRad = Math.toRadians(angleDeg);
                    int px = center.x + (int) (HEX_SIZE * Math.cos(angleRad));
                    int py = center.y + (int) (HEX_SIZE * Math.sin(angleRad));
                    positions[vertexId] = new Point(px, py);
                }
            }
        }

        return positions;
    }

    // -------------------------------------------------------------------------
    // Hit Testing
    // -------------------------------------------------------------------------

    /**
     * Returns the index of the vertex closest to the given pixel point,
     * or -1 if no vertex is within the click tolerance.
     *
     * @param px            clicked x pixel
     * @param py            clicked y pixel
     * @param vertexPositions the 54 vertex positions
     * @param tolerance     max pixel distance to count as a hit
     * @return vertex index 0–53, or -1 if none
     */
    public static int findVertexAt(int px, int py, Point[] vertexPositions, int tolerance) {
        for (int i = 0; i < vertexPositions.length; i++) {
            if (vertexPositions[i] != null && vertexPositions[i].distance(px, py) <= tolerance) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the edge whose midpoint is closest to the given
     * pixel point, or -1 if none is within the click tolerance.
     *
     * @param px            clicked x pixel
     * @param py            clicked y pixel
     * @param vertexPositions the 54 vertex positions
     * @param edgeEndpoints   the 72 edge endpoint pairs from BoardInitialization
     * @param tolerance     max pixel distance to count as a hit
     * @return edge index 0–71, or -1 if none
     */
    public static int findEdgeAt(int px, int py, Point[] vertexPositions,
                                 int[][] edgeEndpoints, int tolerance) {
        for (int i = 0; i < edgeEndpoints.length; i++) {
            Point v1 = vertexPositions[edgeEndpoints[i][0]];
            Point v2 = vertexPositions[edgeEndpoints[i][1]];
            if (v1 == null || v2 == null) {
                continue;
            }
            int midX = (v1.x + v2.x) / 2;
            int midY = (v1.y + v2.y) / 2;
            double dist = new Point(midX, midY).distance(px, py);
            if (dist <= tolerance) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the hex that contains the given pixel point,
     * or -1 if none of the 19 hexes contain it.
     * Uses point-in-polygon testing on each hex's polygon.
     *
     * @param px         clicked x pixel
     * @param py         clicked y pixel
     * @param hexCenters the 19 hex centers from computeHexCenters()
     * @return hex index 0–18, or -1 if none
     */
    public static int findHexAt(int px, int py, Point[] hexCenters) {
        for (int i = 0; i < hexCenters.length; i++) {
            if (hexCenters[i] != null) {
                Polygon poly = buildHexPolygon(hexCenters[i]);
                if (poly.contains(px, py)) {
                    return i;
                }
            }
        }
        return -1;
    }
}