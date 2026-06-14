package domain;

import java.util.List;

public class BuildManager {
    private final Game game;
    private final Player activePlayer;
    private final Bank bank;

    public BuildManager(Game game, Player activePlayer, Bank bank) {
        this.game = game;
        this.activePlayer = activePlayer;
        this.bank = bank;
    }

    public void buildRoad(int edgeId) {
        Board board = game.getBoard();
        Edge edge = board.getEdge(edgeId);
        Vertex endpoint1 = edge.getEndpoints().get(0);
        Vertex endpoint2 = edge.getEndpoints().get(1);

        validateRoadResources();
        validateRoadConditions(edge, endpoint1, endpoint2, board);

        activePlayer.removeResources(ResourceType.BRICK, 1);
        activePlayer.removeResources(ResourceType.LUMBER, 1);
        bank.collect(ResourceType.BRICK, 1);
        bank.collect(ResourceType.LUMBER, 1);
        edge.setOwner(activePlayer);
    }

    public void buildSettlement(int vertexId) {
        Board board = game.getBoard();
        Vertex vertex = board.getVertex(vertexId);

        validateSettlementResources();

        validateSettlementConditions(board, vertex);

        activePlayer.removeResources(ResourceType.BRICK, 1);
        activePlayer.removeResources(ResourceType.LUMBER, 1);
        activePlayer.removeResources(ResourceType.WOOL, 1);
        activePlayer.removeResources(ResourceType.GRAIN, 1);
        bank.collect(ResourceType.BRICK, 1);
        bank.collect(ResourceType.LUMBER, 1);
        bank.collect(ResourceType.WOOL, 1);
        bank.collect(ResourceType.GRAIN, 1);
        vertex.setOwner(activePlayer);
    }

    public void buildCity(int vertexId) {
        Board board = game.getBoard();
        Vertex vertex = board.getVertex(vertexId);

        validateCityResources();
        validateCityConditions(vertex);

        activePlayer.removeResources(ResourceType.ORE, 3);
        activePlayer.removeResources(ResourceType.GRAIN, 2);
        bank.collect(ResourceType.ORE, 3);
        bank.collect(ResourceType.GRAIN, 2);
        vertex.upgradeToCity();
    }

    private void validateRoadResources() {
        if (activePlayer.getResourceCount(ResourceType.BRICK) < 1 ||
                activePlayer.getResourceCount(ResourceType.LUMBER) < 1) {
            throw new IllegalStateException("Player does not have the required resources for a settlement");
        }
    }

    private void validateRoadConditions(Edge edge, Vertex endpoint1, Vertex endpoint2, Board board) {
        if (edge.hasRoad()) {
            throw new IllegalStateException("Edge is already occupied by road");
        }
        if (playerRoadCount() == 15) {
            throw new IllegalStateException("Player has already built maximum number of roads");
        }
        if (!isConnected(endpoint1, endpoint2, board)) {
            throw new IllegalStateException("Road must be connected to existing network");
        }
    }

    private int playerRoadCount() {
        Board board = game.getBoard();
        List<Edge> edgeList = board.getEdges();

        int roadCount = 0;
        for (Edge edge : edgeList) {
            if (edge.getOwner().map(owner -> owner == activePlayer).orElse(false)) {
                roadCount += 1;
            }
        }
        return roadCount;
    }

    private boolean isConnected(Vertex endpoint1, Vertex endpoint2, Board board) {
        boolean connected = false;

        if (endpoint1.getOwner().filter(owner -> owner == activePlayer).isPresent() || endpoint2.getOwner().filter(owner -> owner == activePlayer).isPresent()) {
            connected = true;
        } else if (board.isConnectedToPlayer(endpoint1, activePlayer) && !endpoint1.isOccupied()) {
            connected = true;
        } else if (board.isConnectedToPlayer(endpoint2, activePlayer) && !endpoint2.isOccupied()) {
            connected = true;
        }

        return connected;
    }

    private void validateSettlementResources() {
        if (activePlayer.getResourceCount(ResourceType.BRICK) < 1 ||
                activePlayer.getResourceCount(ResourceType.LUMBER) < 1 ||
                activePlayer.getResourceCount(ResourceType.WOOL) < 1 ||
                activePlayer.getResourceCount(ResourceType.GRAIN) < 1) {
            throw new IllegalStateException("Player does not have the required resources for a settlement");
        }
    }

    private int playerSettlementCount() {
        Board board = game.getBoard();
        List<Vertex> vertexList = board.getVertices();

        int settlementCount = 0;
        for (Vertex vertex : vertexList) {
            if (vertex.getOwner().map(owner -> owner == activePlayer).orElse(false) && !vertex.isCity()) {
                settlementCount += 1;
            }
        }
        return settlementCount;
    }

    private void validateSettlementConditions(Board board, Vertex vertex) {
        if (!(board.isConnectedToPlayer(vertex, activePlayer))) {
            throw new IllegalStateException("Settlement is not connected to existing road");
        }

        if (vertex.isOccupied()) {
            throw new IllegalStateException("Vertex is already occupied");
        }

        if (playerSettlementCount() == 5) {
            throw new IllegalStateException("Player already has maximum number of settlements");
        }

        if (!(board.satisfiesDistanceRule(vertex))) {
            throw new IllegalStateException("Settlement does not satisfy distance rule");
        }
    }

    private void validateCityResources() {
        if (activePlayer.getResourceCount(ResourceType.ORE) < 3 ||
                activePlayer.getResourceCount(ResourceType.GRAIN) < 2) {
            throw new IllegalStateException("Player does not have the required resources for a city");
        }
    }

    private void validateCityConditions(Vertex vertex) {
        if (playerCityCount() == 4) {
            throw new IllegalStateException("Player already has maximum number of cities");
        }

        if (vertex.isOccupied() && vertex.getOwner().filter(owner -> owner == activePlayer).isEmpty()) {
            throw new IllegalStateException("Vertex is occupied by enemy settlement or city");
        }

        if (vertex.isOccupied() && vertex.isCity()) {
            throw new IllegalStateException("Vertex is occupied by player's own city");
        }
    }

    private int playerCityCount() {
        Board board = game.getBoard();
        List<Vertex> vertexList = board.getVertices();

        int cityCount = 0;
        for (Vertex vertex : vertexList) {
            if (vertex.getOwner().map(owner -> owner == activePlayer).orElse(false) && vertex.isCity()) {
                cityCount += 1;
            }
        }
        return cityCount;
    }

    public void buildFreeRoad(int edgeId) {
        Board board = game.getBoard();
        Edge edge = board.getEdge(edgeId);
        Vertex endpoint1 = edge.getEndpoints().get(0);
        Vertex endpoint2 = edge.getEndpoints().get(1);
        validateRoadConditions(edge, endpoint1, endpoint2, board);
        edge.setOwner(activePlayer);
    }
}