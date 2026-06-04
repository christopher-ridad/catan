package domain;

import java.util.*;

public class BoardInitialization {
    private BoardInitialization() {

    }

    private static final int[][] ADJACENT_VERTICES = {
            { 3,  4    }, // vertex  0
            { 4,  5    }, // vertex  1
            { 5,  6    }, // vertex  2
            { 0,  7    }, // vertex  3
            { 0,  1,  8}, // vertex  4
            { 1,  2,  9}, // vertex  5
            { 2, 10    }, // vertex  6
            { 3, 11, 12}, // vertex  7
            { 4, 12, 13}, // vertex  8
            { 5, 13, 14}, // vertex  9
            { 6, 14, 15}, // vertex 10
            { 7, 16    }, // vertex 11
            { 7,  8, 17}, // vertex 12
            { 8,  9, 18}, // vertex 13
            { 9, 10, 19}, // vertex 14
            {10, 20    }, // vertex 15
            {11, 21, 22}, // vertex 16
            {12, 22, 23}, // vertex 17
            {13, 23, 24}, // vertex 18
            {14, 24, 25}, // vertex 19
            {15, 25, 26}, // vertex 20
            {16, 27    }, // vertex 21
            {16, 17, 28}, // vertex 22
            {17, 18, 29}, // vertex 23
            {18, 19, 30}, // vertex 24
            {19, 20, 31}, // vertex 25
            {20, 32    }, // vertex 26
            {21, 33    }, // vertex 27
            {22, 33, 34}, // vertex 28
            {23, 34, 35}, // vertex 29
            {24, 35, 36}, // vertex 30
            {25, 36, 37}, // vertex 31
            {26, 37    }, // vertex 32
            {27, 28, 38}, // vertex 33
            {28, 29, 39}, // vertex 34
            {29, 30, 40}, // vertex 35
            {30, 31, 41}, // vertex 36
            {31, 32, 42}, // vertex 37
            {33, 43    }, // vertex 38
            {34, 43, 44}, // vertex 39
            {35, 44, 45}, // vertex 40
            {36, 45, 46}, // vertex 41
            {37, 46    }, // vertex 42
            {38, 39, 47}, // vertex 43
            {39, 40, 48}, // vertex 44
            {40, 41, 49}, // vertex 45
            {41, 42, 50}, // vertex 46
            {43, 51    }, // vertex 47
            {44, 51, 52}, // vertex 48
            {45, 52, 53}, // vertex 49
            {46, 53    }, // vertex 50
            {47, 48    }, // vertex 51
            {48, 49    }, // vertex 52
            {49, 50    }, // vertex 53
    };


    private static final int[][] ADJACENT_HEX_INDICES = {
            { 0         }, // vertex  0
            { 1         }, // vertex  1
            { 2         }, // vertex  2
            { 0         }, // vertex  3
            { 0,  1     }, // vertex  4
            { 1,  2     }, // vertex  5
            { 2         }, // vertex  6
            { 0,  3     }, // vertex  7
            { 0,  1,  4 }, // vertex  8
            { 1,  2,  5 }, // vertex  9
            { 2,  6     }, // vertex 10
            { 3         }, // vertex 11
            { 0,  3,  4 }, // vertex 12
            { 1,  4,  5 }, // vertex 13
            { 2,  5,  6 }, // vertex 14
            { 6         }, // vertex 15
            { 3,  7     }, // vertex 16
            { 3,  4,  8 }, // vertex 17
            { 4,  5,  9 }, // vertex 18
            { 5,  6, 10 }, // vertex 19
            { 6, 11     }, // vertex 20
            { 7         }, // vertex 21
            { 3,  7,  8 }, // vertex 22
            { 4,  8,  9 }, // vertex 23
            { 5,  9, 10 }, // vertex 24
            { 6, 10, 11 }, // vertex 25
            {11         }, // vertex 26
            { 7         }, // vertex 27
            { 7,  8, 12 }, // vertex 28
            { 8,  9, 13 }, // vertex 29
            { 9, 10, 14 }, // vertex 30
            {10, 11, 15 }, // vertex 31
            {11         }, // vertex 32
            { 7, 12     }, // vertex 33
            { 8, 12, 13 }, // vertex 34
            { 9, 13, 14 }, // vertex 35
            {10, 14, 15 }, // vertex 36
            {11, 15     }, // vertex 37
            {12         }, // vertex 38
            {12, 13, 16 }, // vertex 39
            {13, 14, 17 }, // vertex 40
            {14, 15, 18 }, // vertex 41
            {15         }, // vertex 42
            {12, 16     }, // vertex 43
            {13, 16, 17 }, // vertex 44
            {14, 17, 18 }, // vertex 45
            {15, 18     }, // vertex 46
            {16         }, // vertex 47
            {16, 17     }, // vertex 48
            {17, 18     }, // vertex 49
            {18         }, // vertex 50
            {16         }, // vertex 51
            {17         }, // vertex 52
            {18         }, // vertex 53
    };

    private static final int[][] EDGE_ENDPOINTS = {
            { 0,  3}, // edge  0
            { 0,  4}, // edge  1
            { 1,  4}, // edge  2
            { 1,  5}, // edge  3
            { 2,  5}, // edge  4
            { 2,  6}, // edge  5
            { 3,  7}, // edge  6
            { 4,  8}, // edge  7
            { 5,  9}, // edge  8
            { 6, 10}, // edge  9
            { 7, 11}, // edge 10
            { 7, 12}, // edge 11
            { 8, 12}, // edge 12
            { 8, 13}, // edge 13
            { 9, 13}, // edge 14
            { 9, 14}, // edge 15
            {10, 14}, // edge 16
            {10, 15}, // edge 17
            {11, 16}, // edge 18
            {12, 17}, // edge 19
            {13, 18}, // edge 20
            {14, 19}, // edge 21
            {15, 20}, // edge 22
            {16, 21}, // edge 23
            {16, 22}, // edge 24
            {17, 22}, // edge 25
            {17, 23}, // edge 26
            {18, 23}, // edge 27
            {18, 24}, // edge 28
            {19, 24}, // edge 29
            {19, 25}, // edge 30
            {20, 25}, // edge 31
            {20, 26}, // edge 32
            {21, 27}, // edge 33
            {22, 28}, // edge 34
            {23, 29}, // edge 35
            {24, 30}, // edge 36
            {25, 31}, // edge 37
            {26, 32}, // edge 38
            {27, 33}, // edge 39
            {28, 33}, // edge 40
            {28, 34}, // edge 41
            {29, 34}, // edge 42
            {29, 35}, // edge 43
            {30, 35}, // edge 44
            {30, 36}, // edge 45
            {31, 36}, // edge 46
            {31, 37}, // edge 47
            {32, 37}, // edge 48
            {33, 38}, // edge 49
            {34, 39}, // edge 50
            {35, 40}, // edge 51
            {36, 41}, // edge 52
            {37, 42}, // edge 53
            {38, 43}, // edge 54
            {39, 43}, // edge 55
            {39, 44}, // edge 56
            {40, 44}, // edge 57
            {40, 45}, // edge 58
            {41, 45}, // edge 59
            {41, 46}, // edge 60
            {42, 46}, // edge 61
            {43, 47}, // edge 62
            {44, 48}, // edge 63
            {45, 49}, // edge 64
            {46, 50}, // edge 65
            {47, 51}, // edge 66
            {48, 51}, // edge 67
            {48, 52}, // edge 68
            {49, 52}, // edge 69
            {49, 53}, // edge 70
            {50, 53}, // edge 71
    };

    public static List<Integer> getAdjacentVertexIds(int vertexId) {
        if (vertexId < 0 || vertexId >= ADJACENT_VERTICES.length) {
            throw new IllegalArgumentException("Invalid vertex id: " + vertexId);
        }
        int[] ids = ADJACENT_VERTICES[vertexId];
        List<Integer> result = new ArrayList<>();
        for (int id : ids) {
            result.add(id);
        }
        return Collections.unmodifiableList(result);
    }

    public static List<Integer> getAdjacentHexIndices(int vertexId) {
        if (vertexId < 0 || vertexId >= ADJACENT_HEX_INDICES.length) {
            throw new IllegalArgumentException("Invalid vertex id: " + vertexId);
        }
        int[] indices = ADJACENT_HEX_INDICES[vertexId];
        List<Integer> result = new ArrayList<>();
        for (int idx : indices) {
            result.add(idx);
        }
        return Collections.unmodifiableList(result);
    }

    public static int[][] getEdgeEndpoints() {
        return EDGE_ENDPOINTS;
    }

    private static final Map<Integer, HarborType> HARBOR_VERTICES;

    static {
        HARBOR_VERTICES = new HashMap<>();
        HARBOR_VERTICES.put(0,  HarborType.GENERIC);
        HARBOR_VERTICES.put(3,  HarborType.GENERIC);

        HARBOR_VERTICES.put(1,  HarborType.GRAIN);
        HARBOR_VERTICES.put(5,  HarborType.GRAIN);

        HARBOR_VERTICES.put(10, HarborType.ORE);
        HARBOR_VERTICES.put(15, HarborType.ORE);

        HARBOR_VERTICES.put(26, HarborType.GENERIC);
        HARBOR_VERTICES.put(32, HarborType.GENERIC);

        HARBOR_VERTICES.put(42, HarborType.WOOL);
        HARBOR_VERTICES.put(46, HarborType.WOOL);

        HARBOR_VERTICES.put(49, HarborType.GENERIC);
        HARBOR_VERTICES.put(52, HarborType.GENERIC);

        HARBOR_VERTICES.put(47, HarborType.GENERIC);
        HARBOR_VERTICES.put(51, HarborType.GENERIC);

        HARBOR_VERTICES.put(33, HarborType.BRICK);
        HARBOR_VERTICES.put(38, HarborType.BRICK);

        HARBOR_VERTICES.put(11, HarborType.LUMBER);
        HARBOR_VERTICES.put(16, HarborType.LUMBER);
    }

    public static Map<Integer, HarborType> getHarborVertices() {
        return Collections.unmodifiableMap(HARBOR_VERTICES);
    }
}
