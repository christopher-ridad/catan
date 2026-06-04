package domain;

import java.util.*;

public class SetupPhase {
    private final Game game;
    private final List<Player> placementOrder;
    private int currentPlacementIndex;
    private Vertex lastPlacedSettlement;
    private final Map<Player, List<Vertex>> placedSettlements;

    public SetupPhase(Game game) {
        validateGame(game);
        this.game = game;
        this.placementOrder = buildPlacementOrder();
        this.currentPlacementIndex = 0;
        this.lastPlacedSettlement = null;
        this.placedSettlements = new HashMap<>();
    }

    public List<Player> getPlacementOrder() {
        return Collections.unmodifiableList(placementOrder);
    }

    public Player getCurrentPlayer() {
        if (isComplete()) {
            throw new IllegalStateException("Setup phase is complete");
        }
        return placementOrder.get(currentPlacementIndex);
    }

    public int getCurrentRound() {
        int playerCount = game.getPlayerCount();
        if (currentPlacementIndex < playerCount) {
            return 1;
        }
        return 2;
    }

    public boolean isComplete() {
        return currentPlacementIndex >= placementOrder.size();
    }

    public void placeSettlement(Player player, int vertexId) {
        validatePlayer(player);
        validateCurrentPlayersTurn(player);
        validateVertexId(vertexId);

        Vertex vertex = game.getBoard().getVertex(vertexId);

        validateVertexNotOccupied(vertex, vertexId);
        validateDistanceRule(vertex, vertexId);

        vertex.setOwner(player);
        lastPlacedSettlement = vertex;
        placedSettlements.computeIfAbsent(player, k -> new ArrayList<>()).add(vertex);
    }

    public void placeRoad(Player player, int edgeId) {
        validatePlayer(player);
        validateCurrentPlayersTurn(player);
        validateSettlementPlacedThisTurn();

        Edge edge = game.getBoard().getEdge(edgeId);

        validateEdgeNotOccupied(edge, edgeId);
        validateEdgeAdjacentToSettlement(edge, edgeId);

        edge.setOwner(player);

        if (getCurrentRound() == 2) {
            distributeStartingResources(player);
        }

        advanceTurn();
    }

    public void distributeStartingResources(Player player) {
        if (lastPlacedSettlement == null) {
            return;
        }

        List<Hex> adjacentHexes = lastPlacedSettlement.getAdjacentHexes();

        for (Hex hex : adjacentHexes) {
            if (hex.producesResource()) {
                ResourceType resource = getResourceFromTerrain(hex.getTerrainType());
                player.addResources(resource, 1);
            }
        }
    }

    private void validateGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
    }

    private List<Player> buildPlacementOrder() {
        List<Player> order = new ArrayList<>();
        List<Player> players = game.getPlayers();

        order.addAll(players);

        for (int i = players.size() - 1; i >= 0; i--) {
            order.add(players.get(i));
        }

        return order;
    }

    private void advanceTurn() {
        lastPlacedSettlement = null;
        currentPlacementIndex++;
    }

    private void validateVertexId(int vertexId) {
        if (vertexId < 0 || vertexId > 53) {
            throw new IllegalArgumentException("vertexId must be between 0 and 53");
        }
    }

    private void validateVertexNotOccupied(Vertex vertex, int vertexId) {
        if (vertex.isOccupied()) {
            throw new IllegalStateException(
                    "Vertex " + vertexId + " is already occupied by " +
                            vertex.getOwner().get().getName()
            );
        }
    }

    private void validateDistanceRule(Vertex vertex, int vertexId) {
        if (!game.getBoard().satisfiesDistanceRule(vertex)) {
            throw new IllegalStateException(
                    "Vertex " + vertexId + " violates the distance rule"
            );
        }
    }

    private void validateSettlementPlacedThisTurn() {
        if (lastPlacedSettlement == null) {
            throw new IllegalStateException(
                    "Settlement must be placed before road. No settlement placed in current turn."
            );
        }
    }

    private void validateEdgeNotOccupied(Edge edge, int edgeId) {
        if (edge.hasRoad()) {
            throw new IllegalStateException(
                    "Edge " + edgeId + " already has a road owned by " +
                            edge.getOwner().get().getName()
            );
        }
    }

    private void validateEdgeAdjacentToSettlement(Edge edge, int edgeId) {
        if (!edge.connectsTo(lastPlacedSettlement)) {
            throw new IllegalStateException(
                    "Road at edge " + edgeId + " is not adjacent to the placed settlement. " +
                            "Road must connect to an endpoint of the settlement vertex."
            );
        }
    }

    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
    }

    private void validateCurrentPlayersTurn(Player player) {
        if (!player.equals(getCurrentPlayer())) {
            throw new IllegalStateException(
                    "It is not " + player.getName() + "'s turn. Current player is " +
                            getCurrentPlayer().getName()
            );
        }
    }

    private ResourceType getResourceFromTerrain(TerrainType terrain) {
        if (terrain == TerrainType.FIELDS) {
            return ResourceType.GRAIN;
        }
        if (terrain == TerrainType.PASTURE) {
            return ResourceType.WOOL;
        }
        if (terrain == TerrainType.FOREST) {
            return ResourceType.LUMBER;
        }
        if (terrain == TerrainType.MOUNTAINS) {
            return ResourceType.ORE;
        }
        if (terrain == TerrainType.HILLS) {
            return ResourceType.BRICK;
        }
        throw new IllegalArgumentException("DESERT does not produce resources");
    }
}