package domain;

import java.util.List;

public class VictoryPointCalculator {
    public VictoryPointCalculator() {}

    public int getSettlementVP(Player player, Board board) {
        validatePlayer(player);
        validateBoard(board);

        return playerSettlementCount(player, board);
    }

    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
    }

    private void validateBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException(("Board cannot be null"));
        }
    }

    private int playerSettlementCount(Player player, Board board) {
        List<Vertex> vertexList = board.getVertices();

        int settlementCount = 0;
        for (Vertex vertex : vertexList) {
            if (vertex.getOwner().map(owner -> owner == player).orElse(false) && !vertex.isCity()) {
                settlementCount += 1;
            }
        }
        return settlementCount;
    }
}
