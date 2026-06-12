package domain;

import java.util.*;

public class VictoryPointCalculator {
    public VictoryPointCalculator() {}

    public int getSettlementVP(Player player, Board board) {
        validatePlayer(player);
        validateBoard(board);

        return playerSettlementCount(player, board);
    }

    public int getCityVP(Player player, Board board) {
        validatePlayer(player);
        validateBoard(board);

        return playerCityCount(player, board) * 2;
    }

    public int getDevCardVP(Player player) {
        validatePlayer(player);

        return (int) player.getDevelopmentCards().stream()
                .filter(card -> card.getType() == DevelopmentCardType.VICTORY_POINT)
                .count();
    }

    public int computeLongestRoad(Player player, Board board) {
        validatePlayer(player);
        validateBoard(board);

        Map<Vertex, List<Edge>> adjacencyList = new HashMap<>();

        for (Edge edge : board.getEdges()) {
            if (edge.getOwner().orElse(null) == player) {
                List<Vertex> endpoints = edge.getEndpoints();
                if (endpoints.size() == 2) {
                    Vertex v1 = endpoints.get(0);
                    Vertex v2 = endpoints.get(1);

                    adjacencyList.computeIfAbsent(v1, k -> new ArrayList<>()).add(edge);
                    adjacencyList.computeIfAbsent(v2, k -> new ArrayList<>()).add(edge);
                }
            }
        }

        if (adjacencyList.isEmpty()) {
            return 0;
        }

        int maxLength = 0;
        Set<Edge> visitedEdges = new HashSet<>();

        for (Vertex startVertex : adjacencyList.keySet()) {
            maxLength = Math.max(maxLength, dfs(player, startVertex, adjacencyList, visitedEdges));
        }

        return maxLength;
    }

    public int computeKnightCount(Player player) {
        validatePlayer(player);
        
        return (int) player.getDevelopmentCards().stream()
                .filter(card -> card.getType() == DevelopmentCardType.KNIGHT && card.isPlayed())
                .count();
    }

    private int dfs(Player player, Vertex currentVertex, Map<Vertex, List<Edge>> adjacencyList, Set<Edge> visitedEdges) {
        int maxPath = visitedEdges.size();

        if (!visitedEdges.isEmpty() && currentVertex.getOwner().isPresent() && currentVertex.getOwner().get() != player) {
            return maxPath;
        }

        List<Edge> adjacentEdges = adjacencyList.getOrDefault(currentVertex, Collections.emptyList());

        for (Edge edge : adjacentEdges) {
            if (!visitedEdges.contains(edge)) {
                visitedEdges.add(edge);

                List<Vertex> endpoints = edge.getEndpoints();
                Vertex nextVertex = endpoints.get(0).equals(currentVertex) ? endpoints.get(1) : endpoints.get(0);

                maxPath = Math.max(maxPath, dfs(player, nextVertex, adjacencyList, visitedEdges));

                visitedEdges.remove(edge);
            }
        }

        return maxPath;
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

    private int playerCityCount(Player player, Board board) {
        List<Vertex> vertexList = board.getVertices();

        int cityCount = 0;
        for (Vertex vertex : vertexList) {
            if (vertex.getOwner().map(owner -> owner == player).orElse(false) && vertex.isCity()) {
                cityCount += 1;
            }
        }
        return cityCount;
    }


}
