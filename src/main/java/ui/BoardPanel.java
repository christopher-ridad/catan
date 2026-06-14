package ui;

import domain.Board;
import domain.BoardInitialization;
import domain.Edge;
import domain.Hex;
import domain.HarborType;
import domain.Player;
import domain.PlayerColor;
import domain.TerrainType;
import domain.Vertex;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;

/**
 * Renders the Catan board: hexes, number tokens, harbors, vertices, and edges.
 *
 * Vertex and edge clicks are dispatched to caller-supplied callbacks,
 * so this panel works for both the setup phase and the turn phase
 * without knowing game logic itself.
 *
 * Construct with callbacks for interactive use:
 *   new BoardPanel(board, this::onVertexClicked, this::onEdgeClicked)
 *
 * Construct with nulls for a static/read-only view:
 *   new BoardPanel(board, null, null)
 */
public class BoardPanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int BOARD_ORIGIN_X = 20;
    private static final int BOARD_ORIGIN_Y = 20;

    private static final int VERTEX_RADIUS          = 8;
    private static final int VERTEX_CLICK_TOLERANCE = 14;
    private static final int EDGE_CLICK_TOLERANCE   = 10;

    private static final int NUMBER_TOKEN_RADIUS = 16;
    private static final int NUMBER_FONT_SIZE    = 13;
    private static final int HARBOR_FONT_SIZE    = 10;
    private static final int HARBOR_BADGE_W      = 44;
    private static final int HARBOR_BADGE_H      = 18;

    private static final Color COLOR_HILLS          = new Color(178,  98,  54);
    private static final Color COLOR_FOREST         = new Color( 34, 110,  34);
    private static final Color COLOR_MOUNTAINS      = new Color(120, 120, 120);
    private static final Color COLOR_FIELDS         = new Color(210, 180,  40);
    private static final Color COLOR_PASTURE        = new Color(100, 200, 100);
    private static final Color COLOR_DESERT         = new Color(210, 195, 140);
    private static final Color COLOR_OCEAN          = new Color( 70, 130, 180);
    private static final Color COLOR_HEX_OUTLINE    = new Color(0, 0, 0, 80);
    private static final Color COLOR_VERTEX         = new Color(200, 200, 200);
    private static final Color COLOR_VERTEX_OUTLINE = Color.DARK_GRAY;
    private static final Color COLOR_EDGE           = new Color(160, 120,  60);
    private static final Color COLOR_NUMBER_TOKEN   = new Color(255, 250, 220);
    private static final Color COLOR_RED_NUMBER     = new Color(180,  30,  30);
    private static final Color COLOR_BLACK_NUMBER   = Color.BLACK;
    private static final Color COLOR_HARBOR_BG      = new Color(255, 255, 255, 210);
    private static final Color COLOR_HARBOR_BORDER  = new Color( 50,  80, 140);
    private static final Color COLOR_HARBOR_TEXT    = new Color( 20,  40, 100);
    private static final Color COLOR_HARBOR_LINE    = new Color(255, 255, 255, 160);

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final Board board;
    private final IntConsumer onVertexClicked;
    private final IntConsumer onEdgeClicked;
    private final int[][] edgeEndpoints;

    private Point[] hexCenters;
    private Point[] vertexPositions;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @param board           the board to render
     * @param onVertexClicked called with the vertex index when a vertex is clicked;
     *                        pass null for a static/read-only board
     * @param onEdgeClicked   called with the edge index when an edge is clicked;
     *                        pass null for a static/read-only board
     */
    public BoardPanel(Board board, IntConsumer onVertexClicked, IntConsumer onEdgeClicked) {
        this.board = board;
        this.onVertexClicked = onVertexClicked;
        this.onEdgeClicked = onEdgeClicked;
        this.edgeEndpoints = BoardInitialization.getEdgeEndpoints();
        setBackground(COLOR_OCEAN);
        addMouseListener(new BoardClickListener());
    }

    // -------------------------------------------------------------------------
    // Painting
    // -------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        recomputeGeometry();

        drawHexes(g2);
        drawHarbors(g2);
        drawEdges(g2);
        drawVertices(g2);
    }

    // -------------------------------------------------------------------------
    // Geometry
    // -------------------------------------------------------------------------

    private void recomputeGeometry() {
        hexCenters = HexDrawing.computeHexCenters(BOARD_ORIGIN_X, BOARD_ORIGIN_Y);
        vertexPositions = HexDrawing.computeVertexPositions(hexCenters);
    }

    // -------------------------------------------------------------------------
    // Hex Drawing
    // -------------------------------------------------------------------------

    private void drawHexes(Graphics2D g2) {
        List<Hex> hexes = board.getHexes();
        for (int i = 0; i < hexes.size(); i++) {
            Hex hex = hexes.get(i);
            Point center = hexCenters[i];
            Polygon poly = HexDrawing.buildHexPolygon(center);

            g2.setColor(terrainColor(hex.getTerrainType()));
            g2.fillPolygon(poly);

            g2.setColor(COLOR_HEX_OUTLINE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawPolygon(poly);

            if (!hex.isDesert()) {
                drawNumberToken(g2, center, hex.getNumberToken());
            }
        }
    }

    private void drawNumberToken(Graphics2D g2, Point center, int number) {
        g2.setColor(COLOR_NUMBER_TOKEN);
        g2.fillOval(
                center.x - NUMBER_TOKEN_RADIUS,
                center.y - NUMBER_TOKEN_RADIUS,
                NUMBER_TOKEN_RADIUS * 2,
                NUMBER_TOKEN_RADIUS * 2);

        g2.setColor(number == 6 || number == 8 ? COLOR_RED_NUMBER : COLOR_BLACK_NUMBER);
        g2.setFont(new Font("SansSerif", Font.BOLD, NUMBER_FONT_SIZE));

        FontMetrics fm = g2.getFontMetrics();
        String text = String.valueOf(number);
        int textX = center.x - fm.stringWidth(text) / 2;
        int textY = center.y + fm.getAscent() / 2 - 2;
        g2.drawString(text, textX, textY);

        drawProbabilityDots(g2, center, number);
    }

    private void drawProbabilityDots(Graphics2D g2, Point center, int number) {
        int dots    = probabilityDots(number);
        int dotSize = 3;
        int spacing = 5;
        int totalWidth = dots * dotSize + (dots - 1) * (spacing - dotSize);
        int startX = center.x - totalWidth / 2;
        int dotY   = center.y + NUMBER_TOKEN_RADIUS - 5;

        g2.setColor(number == 6 || number == 8 ? COLOR_RED_NUMBER : COLOR_BLACK_NUMBER);
        for (int i = 0; i < dots; i++) {
            g2.fillOval(startX + i * spacing, dotY, dotSize, dotSize);
        }
    }

    // -------------------------------------------------------------------------
    // Harbor Drawing
    // -------------------------------------------------------------------------

    private static final int[][] HARBOR_PAIRS = {
            { 0,  3},  // GENERIC
            { 1,  5},  // GRAIN
            {10, 15},  // ORE
            {26, 32},  // GENERIC
            {42, 46},  // WOOL
            {49, 52},  // GENERIC
            {47, 51},  // GENERIC
            {33, 38},  // BRICK
            {11, 16},  // LUMBER
    };

    private void drawHarbors(Graphics2D g2) {
        Map<Integer, HarborType> harborMap = BoardInitialization.getHarborVertices();
        for (int[] pair : HARBOR_PAIRS) {
            drawHarborPair(g2, harborMap, pair[0], pair[1]);
        }
    }

    private void drawHarborPair(Graphics2D g2, Map<Integer, HarborType> harborMap,
                                int v1Id, int v2Id) {
        HarborType type = harborMap.get(v1Id);
        if (type == null) {
            return;
        }
        Point p1 = vertexPositions[v1Id];
        Point p2 = vertexPositions[v2Id];
        if (p1 == null || p2 == null) {
            return;
        }
        drawHarborLine(g2, p1, p2);
        drawHarborBadge(g2, p1, p2, type);
    }

    private void drawHarborLine(Graphics2D g2, Point p1, Point p2) {
        g2.setColor(COLOR_HARBOR_LINE);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    private void drawHarborBadge(Graphics2D g2, Point p1, Point p2, HarborType type) {
        Point badgeCenter = computeBadgeCenter(p1, p2);
        int badgeX = badgeCenter.x - HARBOR_BADGE_W / 2;
        int badgeY = badgeCenter.y - HARBOR_BADGE_H / 2;
        drawBadgeBackground(g2, badgeX, badgeY);
        drawBadgeLabel(g2, badgeCenter, type);
    }

    private Point computeBadgeCenter(Point p1, Point p2) {
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;
        Point boardCenter = boardCenter();
        double dx = midX - boardCenter.x;
        double dy = midY - boardCenter.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        double unitX = (length > 0) ? dx / length : 0;
        double unitY = (length > 0) ? dy / length : 0;
        int offsetPixels = 24;
        return new Point(midX + (int) (unitX * offsetPixels), midY + (int) (unitY * offsetPixels));
    }

    private void drawBadgeBackground(Graphics2D g2, int badgeX, int badgeY) {
        g2.setColor(COLOR_HARBOR_BG);
        g2.fillRoundRect(badgeX, badgeY, HARBOR_BADGE_W, HARBOR_BADGE_H, 6, 6);
        g2.setColor(COLOR_HARBOR_BORDER);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(badgeX, badgeY, HARBOR_BADGE_W, HARBOR_BADGE_H, 6, 6);
    }

    private void drawBadgeLabel(Graphics2D g2, Point badgeCenter, HarborType type) {
        g2.setColor(COLOR_HARBOR_TEXT);
        g2.setFont(new Font("SansSerif", Font.BOLD, HARBOR_FONT_SIZE));
        String label = harborLabel(type);
        FontMetrics fm = g2.getFontMetrics();
        int labelX = badgeCenter.x - fm.stringWidth(label) / 2;
        int labelY = badgeCenter.y + fm.getAscent() / 2 - 1;
        g2.drawString(label, labelX, labelY);
    }

    private Point boardCenter() {
        int totalWidth  = HexDrawing.HEX_WIDTH * 5;
        int totalHeight = (int) (HexDrawing.HEX_HEIGHT * 4 * 0.75) + HexDrawing.HEX_SIZE;
        return new Point(
                BOARD_ORIGIN_X + totalWidth  / 2,
                BOARD_ORIGIN_Y + totalHeight / 2);
    }

    // -------------------------------------------------------------------------
    // Edge Drawing
    // -------------------------------------------------------------------------

    private void drawEdges(Graphics2D g2) {
        List<Edge> edges = board.getEdges();

        for (Edge edge : edges) {
            Point v1 = vertexPositions[edge.getEndpoints().get(0).getId()];
            Point v2 = vertexPositions[edge.getEndpoints().get(1).getId()];
            if (v1 == null || v2 == null) {
                continue;
            }

            if (edge.hasRoad()) {
                // Draw road in player color — thicker and on top
                Player owner = edge.getOwner().get();
                g2.setColor(playerColor(owner.getColor()));
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(v1.x, v1.y, v2.x, v2.y);

                // Dark outline for contrast
                g2.setColor(playerColor(owner.getColor()).darker());
                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            } else {
                g2.setColor(COLOR_EDGE);
                g2.setStroke(new BasicStroke(3f));
                g2.drawLine(v1.x, v1.y, v2.x, v2.y);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Vertex Drawing
    // -------------------------------------------------------------------------

    private void drawVertices(Graphics2D g2) {
        List<Vertex> vertices = board.getVertices();

        for (Vertex vertex : vertices) {
            Point pos = vertexPositions[vertex.getId()];
            if (pos == null) {
                continue;
            }

            if (vertex.isOccupied()) {
                Player owner = vertex.getOwner().get();
                Color color  = playerColor(owner.getColor());

                if (vertex.isCity()) {
                    drawCity(g2, pos, color);
                } else {
                    drawSettlement(g2, pos, color);
                }
            } else {
                drawEmptyVertex(g2, pos);
            }
        }
    }

    private void drawSettlement(Graphics2D g2, Point pos, Color color) {
        int size = VERTEX_RADIUS * 2;
        g2.setColor(color);
        g2.fillRect(pos.x - VERTEX_RADIUS, pos.y - VERTEX_RADIUS, size, size);
        g2.setColor(color.darker());
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(pos.x - VERTEX_RADIUS, pos.y - VERTEX_RADIUS, size, size);
    }

    private void drawCity(Graphics2D g2, Point pos, Color color) {
        int size = (int) (VERTEX_RADIUS * 2.6);
        int half = size / 2;
        g2.setColor(color);
        g2.fillRect(pos.x - half, pos.y - half, size, size);
        g2.setColor(color.darker());
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRect(pos.x - half, pos.y - half, size, size);

        // Small notch on top to visually distinguish from settlement
        g2.setColor(color);
        g2.fillRect(pos.x - half / 2, pos.y - half - 5, half, 6);
        g2.setColor(color.darker());
        g2.drawRect(pos.x - half / 2, pos.y - half - 5, half, 6);
    }

    private void drawEmptyVertex(Graphics2D g2, Point pos) {
        g2.setColor(COLOR_VERTEX);
        g2.fillOval(
                pos.x - VERTEX_RADIUS,
                pos.y - VERTEX_RADIUS,
                VERTEX_RADIUS * 2,
                VERTEX_RADIUS * 2);
        g2.setColor(COLOR_VERTEX_OUTLINE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(
                pos.x - VERTEX_RADIUS,
                pos.y - VERTEX_RADIUS,
                VERTEX_RADIUS * 2,
                VERTEX_RADIUS * 2);
    }

    // -------------------------------------------------------------------------
    // Click Handling
    // -------------------------------------------------------------------------

    private class BoardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (vertexPositions == null) {
                return;
            }

            int vertexIndex = HexDrawing.findVertexAt(
                    e.getX(), e.getY(), vertexPositions, VERTEX_CLICK_TOLERANCE);
            if (vertexIndex != -1) {
                if (onVertexClicked != null) {
                    onVertexClicked.accept(vertexIndex);
                }
                return;
            }

            int edgeIndex = HexDrawing.findEdgeAt(
                    e.getX(), e.getY(), vertexPositions, edgeEndpoints, EDGE_CLICK_TOLERANCE);
            if (edgeIndex != -1) {
                if (onEdgeClicked != null) {
                    onEdgeClicked.accept(edgeIndex);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static Color terrainColor(TerrainType terrain) {
        switch (terrain) {
            case HILLS:     return COLOR_HILLS;
            case FOREST:    return COLOR_FOREST;
            case MOUNTAINS: return COLOR_MOUNTAINS;
            case FIELDS:    return COLOR_FIELDS;
            case PASTURE:   return COLOR_PASTURE;
            case DESERT:    return COLOR_DESERT;
            default:        return COLOR_OCEAN;
        }
    }

    private static int probabilityDots(int number) {
        switch (number) {
            case 2:  case 12: return 1;
            case 3:  case 11: return 2;
            case 4:  case 10: return 3;
            case 5:  case 9:  return 4;
            case 6:  case 8:  return 5;
            default:          return 0;
        }
    }

    private static Color playerColor(PlayerColor color) {
        switch (color) {
            case RED:    return new Color(200,  60,  60);
            case BLUE:   return new Color( 60, 100, 200);
            case WHITE:  return new Color(220, 220, 220);
            case ORANGE: return new Color(230, 130,  30);
            default:     return Color.GRAY;
        }
    }

    private static String harborLabel(HarborType type) {
        switch (type) {
            case GRAIN:   return Messages.get("harbor_wheat");
            case WOOL:    return Messages.get("harbor_sheep");
            case ORE:     return Messages.get("harbor_ore");
            case BRICK:   return Messages.get("harbor_brick");
            case LUMBER:  return Messages.get("harbor_wood");
            case GENERIC: return Messages.get("harbor_generic");
            default:      return "?";
        }
    }
}